import { useState, useEffect, useRef } from "react";
import { Link, useNavigate } from "react-router-dom";
import toast from "react-hot-toast";
import {
  Heart, LogOut, Receipt, CreditCard, DollarSign,
  Smartphone, ArrowLeftRight, Loader2, X, CheckCircle,
  Calendar, ShoppingCart, ChevronLeft, Printer,
  BarChart3, Clock, AlertCircle
} from "lucide-react";
import { format } from "date-fns";
import { es } from "date-fns/locale";
import useAuthStore from "../../store/authStore";
import { listarFacturas, obtenerFactura, cobrarFactura, cierreCaja, generarRecibo, obtenerConfiguracion, actualizarConfiguracion } from "../../api/pagosApi";
import jsPDF from "jspdf";

// ── Modal genérico ───────────────────────────────────────────────────────────
const Modal = ({ title, onClose, children, size = "md" }) => (
  <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/40 backdrop-blur-sm">
    <div className={`bg-white rounded-2xl shadow-xl w-full max-h-[90vh] overflow-y-auto ${
      size === "lg" ? "max-w-2xl" : size === "sm" ? "max-w-sm" : "max-w-lg"
    }`}>
      <div className="flex items-center justify-between p-6 border-b border-gray-100">
        <h3 className="font-semibold text-gray-900">{title}</h3>
        <button onClick={onClose} className="text-gray-400 hover:text-gray-600">
          <X className="w-5 h-5" />
        </button>
      </div>
      <div className="p-6">{children}</div>
    </div>
  </div>
);

// ── Badge de estado ──────────────────────────────────────────────────────────
const EstadoBadge = ({ estado }) => {
  const colores = {
    pendiente: "bg-yellow-100 text-yellow-700 border-yellow-200",
    pagada:    "bg-green-100 text-green-700 border-green-200",
    cancelada: "bg-red-100 text-red-600 border-red-200",
    vencida:   "bg-gray-100 text-gray-600 border-gray-200",
  };
  return (
    <span className={`inline-flex items-center px-2.5 py-1 rounded-full text-xs font-medium border ${colores[estado] || colores.pendiente}`}>
      {estado}
    </span>
  );
};

// ── Método de pago icon ──────────────────────────────────────────────────────
const medioIcon = {
  efectivo:      <DollarSign className="w-4 h-4" />,
  qr:            <Smartphone className="w-4 h-4" />,
  transferencia: <ArrowLeftRight className="w-4 h-4" />,
  tarjeta:       <CreditCard className="w-4 h-4" />,
};

// ── PuntoVentaPage ───────────────────────────────────────────────────────────
const PuntoVentaPage = () => {
  const navigate             = useNavigate();
  const { logout, usuario }  = useAuthStore();

  const [facturas, setFacturas]         = useState([]);
  const [loading, setLoading]           = useState(true);
  const [filtroEstado, setFiltroEstado] = useState("pendiente");
  const [filtroBusqueda, setFiltroBusqueda] = useState("");

  // Modal cobro
  const [modalCobro, setModalCobro]     = useState(false);
  const [facturaSeleccionada, setFacturaSeleccionada] = useState(null);
  const [medioPago, setMedioPago]       = useState("efectivo");
  const [referencia, setReferencia]     = useState("");
  const [cobrandoId, setCobrandoId]     = useState(null);
  const [qrUrl, setQrUrl]             = useState("");
const [modalQr, setModalQr]         = useState(false);
const [nuevaUrlQr, setNuevaUrlQr]   = useState("");
const [guardandoQr, setGuardandoQr] = useState(false);

  // Modal recibo
  const [modalRecibo, setModalRecibo]   = useState(false);
  const [reciboData, setReciboData]     = useState(null);

  // Modal cierre de caja
  const [modalCierre, setModalCierre]   = useState(false);
  const [cierreData, setCierreData]     = useState(null);
  const [loadingCierre, setLoadingCierre] = useState(false);
  const [fechaCierre, setFechaCierre]   = useState(format(new Date(), "yyyy-MM-dd"));

  const cargarFacturas = async () => {
    setLoading(true);
    try {
      const res = await listarFacturas();
      setFacturas(res.data);
    } catch {
      toast.error("Error al cargar facturas");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
  cargarFacturas();
  cargarQr();
}, []);

const cargarQr = async () => {
  try {
    const res = await obtenerConfiguracion("qr_pago_url");
    setQrUrl(res.data.valor);
  } catch {
    // No hay QR configurado aún
  }
};

const handleGuardarQr = async () => {
  if (!nuevaUrlQr.trim()) {
    toast.error("Ingresa una URL válida");
    return;
  }
  setGuardandoQr(true);
  try {
    await actualizarConfiguracion("qr_pago_url", {
      valor: nuevaUrlQr,
      descripcion: "URL de imagen QR para pagos",
    });
    setQrUrl(nuevaUrlQr);
    setModalQr(false);
    setNuevaUrlQr("");
    toast.success("QR actualizado correctamente");
  } catch {
    toast.error("Error al guardar el QR");
  } finally {
    setGuardandoQr(false);
  }
};

  const handleLogout = () => { logout(); navigate("/"); };

  // Filtrar facturas
  const facturasFiltradas = facturas.filter((f) => {
    const coincideEstado = filtroEstado === "todas" || f.estado === filtroEstado;
    const coincideBusqueda = !filtroBusqueda ||
      f.clienteNombre?.toLowerCase().includes(filtroBusqueda.toLowerCase()) ||
      f.numero?.toLowerCase().includes(filtroBusqueda.toLowerCase());
    return coincideEstado && coincideBusqueda;
  });

  // Abrir modal de cobro
  const handleAbrirCobro = async (factura) => {
    setFacturaSeleccionada(factura);
    setMedioPago("efectivo");
    setReferencia("");
    setModalCobro(true);
  };

  // Cobrar factura
  const handleCobrar = async () => {
    if (!facturaSeleccionada) return;
    if ((medioPago === "transferencia" || medioPago === "qr") && !referencia.trim()) {
      toast.error("Ingresa el número de referencia");
      return;
    }
    setCobrandoId(facturaSeleccionada.id);
    try {
      await cobrarFactura({
        facturaId:  facturaSeleccionada.id,
        medio:      medioPago,
        monto:      facturaSeleccionada.total,
        referencia: referencia || null,
      });
      toast.success("¡Pago registrado correctamente! 🎉");
      setModalCobro(false);

      // Cargar recibo automáticamente
      const reciboRes = await generarRecibo(facturaSeleccionada.id);
      setReciboData(reciboRes.data);
      setModalRecibo(true);

      cargarFacturas();
    } catch (err) {
      toast.error(err.response?.data?.message || "Error al registrar pago");
    } finally {
      setCobrandoId(null);
    }
  };

  // Ver recibo de factura ya pagada
  const handleVerRecibo = async (factura) => {
    try {
      const res = await generarRecibo(factura.id);
      setReciboData(res.data);
      setModalRecibo(true);
    } catch {
      toast.error("Error al generar recibo");
    }
  };

  // Cierre de caja
  const handleCierreCaja = async () => {
    setLoadingCierre(true);
    try {
      const res = await cierreCaja(fechaCierre);
      setCierreData(res.data);
    } catch {
      toast.error("Error al generar cierre de caja");
    } finally {
      setLoadingCierre(false);
    }
  };

  // Generar PDF del recibo
  const handleDescargarRecibo = () => {
    if (!reciboData) return;
    const doc = new jsPDF();
    const margen = 20;
    let y = 20;

    // Header
    doc.setFontSize(20);
    doc.setTextColor(124, 58, 237);
    doc.text("PawSpa", margen, y);
    y += 8;
    doc.setFontSize(10);
    doc.setTextColor(100, 100, 100);
    doc.text("Recibo de pago", margen, y);
    y += 14;

    // Línea separadora
    doc.setDrawColor(200, 200, 200);
    doc.line(margen, y, 190, y);
    y += 10;

    // Datos factura
    doc.setFontSize(11);
    doc.setTextColor(30, 30, 30);
    doc.text(`Factura N°: ${reciboData.numeroFactura}`, margen, y); y += 7;
    doc.text(`Cliente: ${reciboData.clienteNombre}`, margen, y); y += 7;
    if (reciboData.clienteTelefono) {
      doc.text(`Teléfono: ${reciboData.clienteTelefono}`, margen, y); y += 7;
    }
    doc.text(`Fecha de pago: ${reciboData.pagadoEn
      ? format(new Date(reciboData.pagadoEn), "dd/MM/yyyy HH:mm")
      : format(new Date(), "dd/MM/yyyy HH:mm")}`, margen, y);
    y += 7;
    doc.text(`Método de pago: ${reciboData.medioPago || "—"}`, margen, y);
    if (reciboData.referenciaPago) {
      y += 7;
      doc.text(`Referencia: ${reciboData.referenciaPago}`, margen, y);
    }
    y += 12;

    doc.line(margen, y, 190, y); y += 10;

    // Detalle
    doc.setFontSize(12);
    doc.setTextColor(30, 30, 30);
    doc.text("Detalle", margen, y); y += 8;
    doc.setFontSize(10);

    if (reciboData.tipo === "servicio") {
      doc.text(`Servicio: ${reciboData.servicioNombre}`, margen, y); y += 6;
      doc.text(`Mascota: ${reciboData.mascotaNombre}`, margen, y); y += 6;
      if (reciboData.fechaCita) {
        doc.text(`Fecha del servicio: ${format(new Date(reciboData.fechaCita), "dd/MM/yyyy HH:mm")}`, margen, y);
        y += 6;
      }
    } else if (reciboData.items?.length > 0) {
      reciboData.items.forEach((item) => {
        doc.text(
          `${item.productoNombre} - ${item.varianteNombre}  x${item.cantidad}  Bs. ${Number(item.subtotal).toFixed(2)}`,
          margen, y
        );
        y += 6;
      });
    }

    y += 6;
    doc.line(margen, y, 190, y); y += 10;

    // Totales
    doc.setFontSize(11);
    if (Number(reciboData.descuento) > 0) {
      doc.text(`Subtotal: Bs. ${Number(reciboData.subtotal).toFixed(2)}`, margen, y); y += 7;
      doc.setTextColor(34, 197, 94);
      doc.text(`Descuento: - Bs. ${Number(reciboData.descuento).toFixed(2)}`, margen, y); y += 7;
      doc.setTextColor(30, 30, 30);
    }
    doc.setFontSize(14);
    doc.setFont(undefined, "bold");
    doc.setTextColor(124, 58, 237);
    doc.text(`TOTAL: Bs. ${Number(reciboData.total).toFixed(2)}`, margen, y);

    doc.save(`recibo-${reciboData.numeroFactura}.pdf`);
    toast.success("PDF descargado");
  };

  // Generar PDF del cierre de caja
  const handleDescargarCierre = () => {
    if (!cierreData) return;
    const doc = new jsPDF();
    let y = 20;
    const m = 20;

    doc.setFontSize(18);
    doc.setTextColor(124, 58, 237);
    doc.text("PawSpa — Cierre de Caja", m, y); y += 10;
    doc.setFontSize(11);
    doc.setTextColor(80, 80, 80);
    doc.text(`Fecha: ${cierreData.fecha}`, m, y); y += 10;

    doc.setDrawColor(200, 200, 200);
    doc.line(m, y, 190, y); y += 10;

    doc.setFontSize(12);
    doc.setTextColor(30, 30, 30);
    doc.text("Resumen por método de pago", m, y); y += 8;
    doc.setFontSize(10);

    const metodos = [
      { label: "Efectivo",      valor: cierreData.totalEfectivo },
      { label: "QR",            valor: cierreData.totalQr },
      { label: "Transferencia", valor: cierreData.totalTransferencia },
      { label: "Tarjeta",       valor: cierreData.totalTarjeta },
    ];
    metodos.forEach(({ label, valor }) => {
      doc.text(`${label}: Bs. ${Number(valor).toFixed(2)}`, m, y); y += 7;
    });

    y += 4;
    doc.setFontSize(13);
    doc.setFont(undefined, "bold");
    doc.setTextColor(124, 58, 237);
    doc.text(`TOTAL GENERAL: Bs. ${Number(cierreData.totalGeneral).toFixed(2)}`, m, y);
    doc.text(`(${cierreData.cantidadPagos} pago${cierreData.cantidadPagos !== 1 ? "s" : ""})`, 140, y);
    y += 14;

    doc.setFont(undefined, "normal");
    doc.setDrawColor(200, 200, 200);
    doc.line(m, y, 190, y); y += 10;

    doc.setFontSize(11);
    doc.setTextColor(30, 30, 30);
    doc.text("Detalle de pagos", m, y); y += 8;
    doc.setFontSize(9);

    cierreData.detalle?.forEach((d) => {
      if (y > 270) { doc.addPage(); y = 20; }
      doc.text(
        `${d.numeroFactura}  ${d.clienteNombre}  [${d.tipo}]  ${d.medio}  Bs. ${Number(d.monto).toFixed(2)}`,
        m, y
      );
      y += 6;
    });

    doc.save(`cierre-caja-${cierreData.fecha}.pdf`);
    toast.success("PDF descargado");
  };

  const stats = {
    pendientes: facturas.filter(f => f.estado === "pendiente").length,
    pagadas:    facturas.filter(f => f.estado === "pagada").length,
    total:      facturas.filter(f => f.estado === "pagada")
                  .reduce((acc, f) => acc + Number(f.total), 0),
  };

  const inputClass = "w-full px-4 py-2.5 rounded-xl border border-gray-200 focus:outline-none focus:ring-2 focus:ring-purple-300 text-sm";

  return (
    <div className="min-h-screen bg-gray-50">

      {/* Navbar */}
      <nav className="bg-white border-b border-gray-100 px-6 py-4">
        <div className="max-w-7xl mx-auto flex items-center justify-between">
          <div className="flex items-center gap-3">
            <div className="w-8 h-8 rounded-lg bg-gradient-to-br from-purple-500 to-green-500 flex items-center justify-center">
              <Heart className="w-4 h-4 text-white" />
            </div>
            <span className="font-semibold text-gray-900">
              Paw<span className="bg-gradient-to-r from-purple-600 to-green-500 bg-clip-text text-transparent">Spa</span>
            </span>
            <span className="text-gray-300 mx-1">|</span>
            <Link to="/recepcion" className="flex items-center gap-1.5 text-sm text-gray-500 hover:text-purple-600 transition-colors">
              <ChevronLeft className="w-4 h-4" />
              Agenda
            </Link>
            <span className="text-sm text-gray-400">Punto de Venta</span>
          </div>
          <div className="flex items-center gap-3">
            <button
              onClick={() => setModalCierre(true)}
              className="flex items-center gap-2 text-sm font-medium text-gray-700 hover:text-purple-600 px-3 py-2 rounded-lg hover:bg-purple-50 transition-colors"
            >
              <BarChart3 className="w-4 h-4" />
              Cierre de caja
            </button>
            <span className="text-sm text-gray-600">Hola, <strong>{usuario?.nombre}</strong></span>
            <button
              onClick={handleLogout}
              className="flex items-center gap-2 text-sm text-gray-500 hover:text-red-500 transition-colors px-3 py-2 rounded-lg hover:bg-red-50"
            >
              <LogOut className="w-4 h-4" />
              Salir
            </button>
          </div>
        </div>
      </nav>

      <div className="max-w-7xl mx-auto px-6 py-8">

        {/* Stats */}
        <div className="grid grid-cols-1 md:grid-cols-3 gap-4 mb-8">
          {[
            { label: "Facturas pendientes", valor: stats.pendientes, color: "text-yellow-600", icon: <Clock className="w-5 h-5" /> },
            { label: "Cobros del día",      valor: stats.pagadas,    color: "text-green-600",  icon: <CheckCircle className="w-5 h-5" /> },
            { label: "Total cobrado",       valor: `Bs. ${stats.total.toFixed(2)}`, color: "text-purple-600", icon: <DollarSign className="w-5 h-5" /> },
          ].map((s, i) => (
            <div key={i} className="bg-white rounded-2xl border border-gray-100 p-5 flex items-center gap-4">
              <div className={`w-12 h-12 rounded-xl bg-gray-50 flex items-center justify-center ${s.color}`}>
                {s.icon}
              </div>
              <div>
                <div className={`text-2xl font-bold ${s.color}`}>{s.valor}</div>
                <div className="text-sm text-gray-500 mt-0.5">{s.label}</div>
              </div>
            </div>
          ))}
        </div>

        {/* Tabla de facturas */}
        <div className="bg-white rounded-2xl border border-gray-100">
          <div className="flex flex-col sm:flex-row items-start sm:items-center justify-between gap-4 p-6 border-b border-gray-100">
            <div className="flex items-center gap-2">
              <Receipt className="w-5 h-5 text-purple-500" />
              <h2 className="font-semibold text-gray-900">Facturas</h2>
            </div>
            <div className="flex flex-col sm:flex-row gap-3 w-full sm:w-auto">
              {/* Búsqueda */}
              <input
                type="text"
                placeholder="Buscar por cliente o N° factura..."
                value={filtroBusqueda}
                onChange={(e) => setFiltroBusqueda(e.target.value)}
                className="px-4 py-2 rounded-xl border border-gray-200 text-sm focus:outline-none focus:ring-2 focus:ring-purple-300 w-full sm:w-64"
              />
              {/* Filtro estado */}
              <div className="flex gap-2">
                {["todas", "pendiente", "pagada"].map((estado) => (
                  <button
                    key={estado}
                    onClick={() => setFiltroEstado(estado)}
                    className={`px-3 py-2 rounded-xl text-xs font-medium transition-all ${
                      filtroEstado === estado
                        ? "bg-purple-600 text-white"
                        : "bg-gray-50 text-gray-600 hover:bg-gray-100"
                    }`}
                  >
                    {estado === "todas" ? "Todas" : estado === "pendiente" ? "Pendientes" : "Pagadas"}
                  </button>
                ))}
              </div>
            </div>
          </div>

          {loading ? (
            <div className="flex items-center justify-center py-16">
              <Loader2 className="w-8 h-8 text-purple-500 animate-spin" />
            </div>
          ) : facturasFiltradas.length === 0 ? (
            <div className="text-center py-16">
              <Receipt className="w-12 h-12 text-gray-200 mx-auto mb-3" />
              <p className="text-gray-400 text-sm">No hay facturas {filtroEstado !== "todas" ? filtroEstado + "s" : ""}</p>
            </div>
          ) : (
            <div className="divide-y divide-gray-50">
              {facturasFiltradas.map((f) => (
                <div key={f.id} className="p-5 hover:bg-gray-50 transition-colors">
                  <div className="flex items-center justify-between gap-4">
                    <div className="flex items-start gap-4">
                      {/* Tipo icono */}
                      <div className={`w-10 h-10 rounded-xl flex items-center justify-center shrink-0 ${
                        f.citaId ? "bg-purple-50 text-purple-600" : "bg-green-50 text-green-600"
                      }`}>
                        {f.citaId
                          ? <Calendar className="w-5 h-5" />
                          : <ShoppingCart className="w-5 h-5" />}
                      </div>
                      {/* Info */}
                      <div>
                        <div className="font-medium text-gray-900">{f.clienteNombre}</div>
                        <div className="text-xs text-gray-400 mt-0.5">
                          {f.numero} • {f.citaId ? "Servicio" : "Tienda"}
                        </div>
                        <div className="text-xs text-gray-400">
                          {f.emitidaEn && format(new Date(f.emitidaEn), "dd MMM yyyy HH:mm", { locale: es })}
                        </div>
                      </div>
                    </div>

                    {/* Total y acciones */}
                    <div className="flex items-center gap-4">
                      <div className="text-right">
                        <div className="font-bold text-gray-900">Bs. {Number(f.total).toFixed(2)}</div>
                        <EstadoBadge estado={f.estado} />
                      </div>
                      <div className="flex gap-2">
                        {f.estado === "pendiente" && (
                          <button
                            onClick={() => handleAbrirCobro(f)}
                            className="flex items-center gap-1.5 px-4 py-2 rounded-xl bg-gradient-to-r from-purple-500 to-purple-600 text-white text-xs font-medium hover:from-purple-600 hover:to-purple-700 transition-all shadow-sm"
                          >
                            <CreditCard className="w-3.5 h-3.5" />
                            Cobrar
                          </button>
                        )}
                        {f.estado === "pagada" && (
                          <button
                            onClick={() => handleVerRecibo(f)}
                            className="flex items-center gap-1.5 px-3 py-2 rounded-xl border border-gray-200 text-gray-600 text-xs font-medium hover:bg-gray-50 transition-colors"
                          >
                            <Receipt className="w-3.5 h-3.5" />
                            Recibo
                          </button>
                        )}
                      </div>
                    </div>
                  </div>
                </div>
              ))}
            </div>
          )}
        </div>
      </div>

      {/* ── Modal Cobro ── */}
      {modalCobro && facturaSeleccionada && (
        <Modal title="Registrar pago" onClose={() => setModalCobro(false)}>
          <div className="space-y-5">
            {/* Resumen factura */}
            <div className="bg-gray-50 rounded-xl p-4 space-y-2">
              <div className="flex justify-between text-sm">
                <span className="text-gray-500">Factura</span>
                <span className="font-medium">{facturaSeleccionada.numero}</span>
              </div>
              <div className="flex justify-between text-sm">
                <span className="text-gray-500">Cliente</span>
                <span className="font-medium">{facturaSeleccionada.clienteNombre}</span>
              </div>
              <div className="flex justify-between text-sm">
                <span className="text-gray-500">Tipo</span>
                <span className="font-medium">{facturaSeleccionada.citaId ? "Servicio" : "Tienda"}</span>
              </div>
              <div className="border-t border-gray-200 pt-2 flex justify-between">
                <span className="font-semibold text-gray-900">Total a cobrar</span>
                <span className="text-xl font-bold text-purple-600">
                  Bs. {Number(facturaSeleccionada.total).toFixed(2)}
                </span>
              </div>
            </div>

            {/* Método de pago */}
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-3">
                Método de pago
              </label>
              <div className="grid grid-cols-2 gap-3">
                {[
  { value: "efectivo",      label: "Efectivo",      icon: <DollarSign className="w-5 h-5" /> },
  { value: "tarjeta",       label: "Tarjeta",       icon: <CreditCard className="w-5 h-5" /> },
  { value: "transferencia", label: "Transferencia", icon: <ArrowLeftRight className="w-5 h-5" /> },
  { value: "qr",            label: "QR",            icon: <Smartphone className="w-5 h-5" /> },
].map((m) => (
  <button
    key={m.value}
    type="button"
    onClick={() => setMedioPago(m.value)}
    className={`flex items-center gap-3 p-4 rounded-xl border-2 transition-all ${
      medioPago === m.value
        ? "border-purple-400 bg-purple-50 text-purple-700"
        : "border-gray-200 text-gray-600 hover:border-purple-200"
    }`}
  >
    {m.icon}
    <span className="text-sm font-medium">{m.label}</span>
  </button>
))}
              </div>
            </div>

            {/* Referencia (solo para transferencia y QR) */}
            {(medioPago === "transferencia" || medioPago === "qr") && (
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">
                  N° de referencia / comprobante <span className="text-red-500">*</span>
                </label>
                <input
                  type="text"
                  value={referencia}
                  onChange={(e) => setReferencia(e.target.value)}
                  placeholder="Ej: TRX-123456"
                  className={inputClass}
                />
              </div>
            )}

            {/* QR — mostrar imagen si está seleccionado */}
{medioPago === "qr" && (
  <div className="col-span-2 mt-2 space-y-3">
    {qrUrl ? (
      <div className="flex flex-col items-center gap-3 bg-gray-50 rounded-xl p-4 border border-gray-100">
        <p className="text-xs text-gray-500">
          El cliente escanea este QR y realiza el pago
        </p>
        <img
  src={qrUrl}
  alt="QR de pago"
  className="w-48 h-48 object-contain rounded-xl border border-gray-200 bg-white p-2"
  onError={(e) => {
    console.log("Error cargando QR:", qrUrl);
  }}
/>
        <button
          type="button"
          onClick={() => { setNuevaUrlQr(qrUrl); setModalQr(true); }}
          className="text-xs text-purple-600 hover:underline"
        >
          Cambiar QR
        </button>
      </div>
    ) : (
      <div className="col-span-2 bg-yellow-50 border border-yellow-200 rounded-xl p-4 text-center">
        <p className="text-sm text-yellow-700 mb-3">
          No hay un QR configurado aún
        </p>
        <button
          type="button"
          onClick={() => setModalQr(true)}
          className="text-sm font-medium text-purple-600 hover:underline"
        >
          + Configurar QR ahora
        </button>
      </div>
    )}
  </div>
)}

            <div className="flex gap-3 pt-2">
              <button
                onClick={() => setModalCobro(false)}
                className="flex-1 py-3 rounded-xl border border-gray-200 text-sm text-gray-600 hover:bg-gray-50"
              >
                Cancelar
              </button>
              <button
                onClick={handleCobrar}
                disabled={!!cobrandoId}
                className="flex-1 py-3 rounded-xl bg-gradient-to-r from-purple-500 to-purple-600 text-white font-medium text-sm disabled:opacity-60 flex items-center justify-center gap-2 hover:from-purple-600 hover:to-purple-700 transition-all"
              >
                {cobrandoId ? (
                  <><Loader2 className="w-4 h-4 animate-spin" /> Procesando...</>
                ) : (
                  <><CheckCircle className="w-4 h-4" /> Confirmar pago</>
                )}
              </button>
            </div>
          </div>
        </Modal>
      )}

      {/* ── Modal Recibo ── */}
      {modalRecibo && reciboData && (
        <Modal title="Recibo de pago" onClose={() => setModalRecibo(false)} size="lg">
          <div className="space-y-4">
            {/* Header recibo */}
            <div className="text-center pb-4 border-b border-gray-100">
              <div className="w-12 h-12 rounded-xl bg-gradient-to-br from-purple-500 to-green-500 flex items-center justify-center mx-auto mb-3">
                <Heart className="w-6 h-6 text-white" />
              </div>
              <h3 className="font-bold text-gray-900 text-lg">PawSpa</h3>
              <p className="text-xs text-gray-400">Recibo de pago</p>
            </div>

            {/* Datos */}
            <div className="space-y-2.5 text-sm">
              {[
                { label: "N° Factura",  valor: reciboData.numeroFactura },
                { label: "Cliente",     valor: reciboData.clienteNombre },
                { label: "Teléfono",    valor: reciboData.clienteTelefono || "—" },
                { label: "Tipo",        valor: reciboData.tipo === "servicio" ? "Servicio grooming" : "Compra tienda" },
                { label: "Método",      valor: reciboData.medioPago || "—" },
                { label: "Referencia",  valor: reciboData.referenciaPago || "—" },
                { label: "Fecha",       valor: reciboData.pagadoEn
                    ? format(new Date(reciboData.pagadoEn), "dd/MM/yyyy HH:mm")
                    : format(new Date(), "dd/MM/yyyy HH:mm") },
              ].map((item, i) => item.valor !== "—" && (
                <div key={i} className="flex justify-between">
                  <span className="text-gray-500">{item.label}</span>
                  <span className="font-medium text-gray-900">{item.valor}</span>
                </div>
              ))}
            </div>

            {/* Detalle servicio */}
            {reciboData.tipo === "servicio" && (
              <div className="bg-purple-50 rounded-xl p-4 space-y-1.5 border border-purple-100">
                <div className="text-xs font-medium text-purple-700 mb-2">Detalle del servicio</div>
                <div className="flex justify-between text-sm">
                  <span className="text-gray-600">Servicio</span>
                  <span className="font-medium">{reciboData.servicioNombre}</span>
                </div>
                <div className="flex justify-between text-sm">
                  <span className="text-gray-600">Mascota</span>
                  <span className="font-medium">{reciboData.mascotaNombre}</span>
                </div>
                {reciboData.fechaCita && (
                  <div className="flex justify-between text-sm">
                    <span className="text-gray-600">Fecha</span>
                    <span className="font-medium">
                      {format(new Date(reciboData.fechaCita), "dd/MM/yyyy HH:mm")}
                    </span>
                  </div>
                )}
              </div>
            )}

            {/* Detalle pedido */}
            {reciboData.tipo === "pedido" && reciboData.items?.length > 0 && (
              <div className="bg-green-50 rounded-xl p-4 border border-green-100">
                <div className="text-xs font-medium text-green-700 mb-3">Productos</div>
                <div className="space-y-2">
                  {reciboData.items.map((item, i) => (
                    <div key={i} className="flex justify-between text-sm">
                      <span className="text-gray-600">
                        {item.productoNombre} - {item.varianteNombre} x{item.cantidad}
                      </span>
                      <span className="font-medium">Bs. {Number(item.subtotal).toFixed(2)}</span>
                    </div>
                  ))}
                </div>
              </div>
            )}

            {/* Totales */}
            <div className="border-t border-gray-100 pt-4 space-y-2">
              {Number(reciboData.descuento) > 0 && (
                <div className="flex justify-between text-sm">
                  <span className="text-gray-500">Subtotal</span>
                  <span>Bs. {Number(reciboData.subtotal).toFixed(2)}</span>
                </div>
              )}
              {Number(reciboData.descuento) > 0 && (
                <div className="flex justify-between text-sm text-green-600">
                  <span>Descuento</span>
                  <span>- Bs. {Number(reciboData.descuento).toFixed(2)}</span>
                </div>
              )}
              <div className="flex justify-between font-bold text-lg">
                <span className="text-gray-900">TOTAL</span>
                <span className="text-purple-600">Bs. {Number(reciboData.total).toFixed(2)}</span>
              </div>
            </div>

            {/* Acciones */}
            <div className="flex gap-3 pt-2">
              <button
                onClick={() => setModalRecibo(false)}
                className="flex-1 py-2.5 rounded-xl border border-gray-200 text-sm text-gray-600 hover:bg-gray-50"
              >
                Cerrar
              </button>
              <button
                onClick={handleDescargarRecibo}
                className="flex-1 py-2.5 rounded-xl bg-gradient-to-r from-purple-500 to-purple-600 text-white text-sm font-medium flex items-center justify-center gap-2 hover:from-purple-600 hover:to-purple-700 transition-all"
              >
                <Printer className="w-4 h-4" />
                Descargar PDF
              </button>
            </div>
          </div>
        </Modal>
      )}


      {/* ── Modal Configurar QR ── */}
{modalQr && (
  <Modal title="Configurar QR de pago" onClose={() => setModalQr(false)} size="sm">
    <div className="space-y-4">
      <div className="bg-blue-50 rounded-xl p-4 text-sm text-blue-700 border border-blue-100">
        Ingresa la URL de la imagen de tu QR bancario. Puede ser de Cloudinary, Google Drive, Imgur u otro servicio de imágenes.
      </div>

      <div>
        <label className="block text-sm font-medium text-gray-700 mb-1">
          URL de la imagen QR
        </label>
        <input
          type="url"
          value={nuevaUrlQr}
          onChange={(e) => setNuevaUrlQr(e.target.value)}
          placeholder="https://..."
          className={inputClass}
        />
      </div>

      {/* Preview */}
      {nuevaUrlQr && (
        <div className="flex justify-center">
          <img
            src={nuevaUrlQr}
            alt="Preview QR"
            className="w-40 h-40 object-contain rounded-xl border border-gray-200 bg-gray-50 p-2"
            onError={(e) => { e.target.style.display = "none"; }}
          />
        </div>
      )}

      <div className="flex gap-3 pt-2">
        <button
          onClick={() => { setModalQr(false); setNuevaUrlQr(""); }}
          className="flex-1 py-2.5 rounded-xl border border-gray-200 text-sm text-gray-600 hover:bg-gray-50"
        >
          Cancelar
        </button>
        <button
          onClick={handleGuardarQr}
          disabled={guardandoQr}
          className="flex-1 py-2.5 rounded-xl bg-gradient-to-r from-purple-500 to-purple-600 text-white text-sm font-medium disabled:opacity-60 flex items-center justify-center gap-2"
        >
          {guardandoQr && <Loader2 className="w-4 h-4 animate-spin" />}
          Guardar QR
        </button>
      </div>
    </div>
  </Modal>
)}  


      {/* ── Modal Cierre de Caja ── */}
      {modalCierre && (
        <Modal title="Cierre de caja" onClose={() => { setModalCierre(false); setCierreData(null); }} size="lg">
          <div className="space-y-5">
            {/* Selector de fecha */}
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">Fecha</label>
              <div className="flex gap-3">
                <input
                  type="date"
                  value={fechaCierre}
                  onChange={(e) => setFechaCierre(e.target.value)}
                  max={format(new Date(), "yyyy-MM-dd")}
                  className="flex-1 px-4 py-2.5 rounded-xl border border-gray-200 focus:outline-none focus:ring-2 focus:ring-purple-300 text-sm"
                />
                <button
                  onClick={handleCierreCaja}
                  disabled={loadingCierre}
                  className="px-5 py-2.5 rounded-xl bg-gradient-to-r from-purple-500 to-purple-600 text-white text-sm font-medium disabled:opacity-60 flex items-center gap-2"
                >
                  {loadingCierre
                    ? <Loader2 className="w-4 h-4 animate-spin" />
                    : <BarChart3 className="w-4 h-4" />}
                  Generar
                </button>
              </div>
            </div>

            {cierreData && (
              <>
                {/* Resumen por método */}
                <div className="grid grid-cols-2 gap-3">
                  {[
                    { label: "Efectivo",      valor: cierreData.totalEfectivo,      icon: <DollarSign className="w-4 h-4" />,     color: "bg-green-50 text-green-700 border-green-100" },
                    { label: "QR",            valor: cierreData.totalQr,            icon: <Smartphone className="w-4 h-4" />,     color: "bg-blue-50 text-blue-700 border-blue-100" },
                    { label: "Transferencia", valor: cierreData.totalTransferencia, icon: <ArrowLeftRight className="w-4 h-4" />, color: "bg-yellow-50 text-yellow-700 border-yellow-100" },
                    { label: "Tarjeta",       valor: cierreData.totalTarjeta,       icon: <CreditCard className="w-4 h-4" />,     color: "bg-purple-50 text-purple-700 border-purple-100" },
                  ].map((m, i) => (
                    <div key={i} className={`flex items-center gap-3 p-4 rounded-xl border ${m.color}`}>
                      {m.icon}
                      <div>
                        <div className="text-xs opacity-70">{m.label}</div>
                        <div className="font-bold">Bs. {Number(m.valor).toFixed(2)}</div>
                      </div>
                    </div>
                  ))}
                </div>

                {/* Total general */}
                <div className="bg-gradient-to-r from-purple-500 to-purple-600 rounded-xl p-5 text-white flex items-center justify-between">
                  <div>
                    <div className="text-sm text-purple-200">Total general</div>
                    <div className="text-3xl font-bold">Bs. {Number(cierreData.totalGeneral).toFixed(2)}</div>
                  </div>
                  <div className="text-right">
                    <div className="text-sm text-purple-200">Pagos</div>
                    <div className="text-2xl font-bold">{cierreData.cantidadPagos}</div>
                  </div>
                </div>

                {/* Detalle */}
                {cierreData.detalle?.length > 0 && (
                  <div>
                    <div className="text-sm font-medium text-gray-700 mb-3">Detalle de cobros</div>
                    <div className="space-y-2 max-h-48 overflow-y-auto">
                      {cierreData.detalle.map((d, i) => (
                        <div key={i} className="flex items-center justify-between p-3 rounded-xl bg-gray-50 border border-gray-100">
                          <div>
                            <div className="text-sm font-medium text-gray-900">{d.clienteNombre}</div>
                            <div className="text-xs text-gray-400">
                              {d.numeroFactura} • {d.tipo} • {d.medio}
                            </div>
                          </div>
                          <span className="font-semibold text-gray-900">Bs. {Number(d.monto).toFixed(2)}</span>
                        </div>
                      ))}
                    </div>
                  </div>
                )}

                <button
                  onClick={handleDescargarCierre}
                  className="w-full py-3 rounded-xl bg-gradient-to-r from-purple-500 to-purple-600 text-white font-medium text-sm flex items-center justify-center gap-2 hover:from-purple-600 hover:to-purple-700 transition-all"
                >
                  <Printer className="w-4 h-4" />
                  Descargar PDF del cierre
                </button>
              </>
            )}

            {!cierreData && !loadingCierre && (
              <div className="text-center py-8 text-gray-400">
                <BarChart3 className="w-12 h-12 mx-auto mb-3 text-gray-200" />
                <p className="text-sm">Selecciona una fecha y presiona "Generar"</p>
              </div>
            )}
          </div>
        </Modal>
      )}

    </div>
  );
};

export default PuntoVentaPage;
import { useState, useEffect } from "react";
import { Link, useNavigate, useLocation } from "react-router-dom";
import toast from "react-hot-toast";
import {
  Heart, ShoppingCart, Trash2, Plus, Minus,
  Loader2, ArrowLeft, CheckCircle, MessageCircle,
  Package, ChevronRight, X
} from "lucide-react";
import {
  verCarrito, obtenerOCrearCarrito, agregarItem,
  actualizarItem, eliminarItem, confirmarPedido,
  generarMensaje, getProductoDetalle
} from "../../api/tiendaApi";
import useAuthStore from "../../store/authStore";

// ── Modal de variantes ───────────────────────────────────────────────────────
const ModalVariantes = ({ producto, pedidoId, onClose, onAgregado }) => {
  const [varianteSeleccionada, setVarianteSeleccionada] = useState(null);
  const [cantidad, setCantidad] = useState(1);
  const [loading, setLoading] = useState(false);


  const variantesDisponibles = producto?.variantes?.filter(
    (v) => v.activa && v.stock > 0
  ) || [];

  const handleAgregar = async () => {
    if (!varianteSeleccionada) {
      toast.error("Selecciona una variante");
      return;
    }
    setLoading(true);
    try {
      await agregarItem(pedidoId, { varianteId: varianteSeleccionada.id, cantidad });
      toast.success("Producto agregado al carrito 🛒");
      onAgregado();
      onClose();
    } catch (err) {
      toast.error(err.response?.data?.message || "Error al agregar");
    } finally {
      setLoading(false);
    }
  };

  


  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/40 backdrop-blur-sm">
      <div className="bg-white rounded-2xl shadow-xl w-full max-w-md">
        <div className="p-6 border-b border-gray-100">
          <h3 className="font-semibold text-gray-900">{producto?.nombre}</h3>
          <p className="text-sm text-gray-400 mt-1">Elige la variante</p>
        </div>
        <div className="p-6 space-y-3">
          {variantesDisponibles.map((v) => (
            <button
              key={v.id}
              onClick={() => setVarianteSeleccionada(v)}
              className={`w-full flex items-center justify-between p-3 rounded-xl border transition-all text-left ${
                varianteSeleccionada?.id === v.id
                  ? "border-purple-400 bg-purple-50"
                  : "border-gray-200 hover:border-purple-200"
              }`}
            >
              <div>
                <span className="text-sm font-medium text-gray-900">{v.nombre}</span>
                {v.sku && <span className="text-xs text-gray-400 ml-2">SKU: {v.sku}</span>}
                <div className="text-xs text-gray-400 mt-0.5">Stock: {v.stock}</div>
              </div>
              <span className="text-sm font-bold text-purple-600">
                Bs. {Number(v.precio).toFixed(2)}
              </span>
            </button>
          ))}

          {/* Cantidad */}
          <div className="flex items-center justify-between pt-2">
            <span className="text-sm font-medium text-gray-700">Cantidad</span>
            <div className="flex items-center gap-3">
              <button
                onClick={() => setCantidad((c) => Math.max(1, c - 1))}
                className="w-8 h-8 rounded-lg border border-gray-200 flex items-center justify-center hover:bg-gray-50"
              >
                <Minus className="w-3.5 h-3.5" />
              </button>
              <span className="text-sm font-semibold w-6 text-center">{cantidad}</span>
              <button
                onClick={() => setCantidad((c) => c + 1)}
                className="w-8 h-8 rounded-lg border border-gray-200 flex items-center justify-center hover:bg-gray-50"
              >
                <Plus className="w-3.5 h-3.5" />
              </button>
            </div>
          </div>
        </div>

        <div className="flex gap-3 p-6 pt-0">
          <button
            onClick={onClose}
            className="flex-1 py-2.5 rounded-xl border border-gray-200 text-sm text-gray-600 hover:bg-gray-50"
          >
            Cancelar
          </button>
          <button
            onClick={handleAgregar}
            disabled={loading || !varianteSeleccionada}
            className="flex-1 py-2.5 rounded-xl bg-gradient-to-r from-purple-500 to-purple-600 text-white text-sm font-medium disabled:opacity-60 flex items-center justify-center gap-2"
          >
            {loading && <Loader2 className="w-4 h-4 animate-spin" />}
            Agregar
          </button>
        </div>
      </div>
    </div>
  );
};

// ── Modal mensaje ────────────────────────────────────────────────────────────
const ModalMensaje = ({ mensaje, onClose }) => (
  <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/40 backdrop-blur-sm">
    <div className="bg-white rounded-2xl shadow-xl w-full max-w-md">
      <div className="p-6 border-b border-gray-100">
        <h3 className="font-semibold text-gray-900">
          Tu pedido está listo 🎉
        </h3>

        <p className="text-sm text-gray-400 mt-1">
          Copia el mensaje o ábrelo directamente en{" "}
          {mensaje.canal === "whatsapp" ? "WhatsApp" : "Telegram"}
        </p>
      </div>

      <div className="p-6">
        <pre className="bg-gray-50 rounded-xl p-4 text-xs text-gray-700 whitespace-pre-wrap font-mono border border-gray-100 max-h-48 overflow-y-auto">
          {mensaje.mensaje}
        </pre>

        <div className="flex gap-3 mt-4">
          <button
            onClick={() => {
              navigator.clipboard.writeText(mensaje.mensaje);
              toast.success("Mensaje copiado");
            }}
            className="flex-1 py-2.5 rounded-xl border border-gray-200 text-sm text-gray-600 hover:bg-gray-50"
          >
            Copiar mensaje
          </button>

          {mensaje.linkWhatsapp && (
            <a
              href={mensaje.linkWhatsapp}
              target="_blank"
              rel="noreferrer"
              className="flex-1 py-2.5 rounded-xl bg-green-500 hover:bg-green-600 text-white text-sm font-medium text-center transition-colors flex items-center justify-center gap-1.5"
            >
              <MessageCircle className="w-4 h-4" />
              WhatsApp
            </a>
          )}

          {mensaje.linkTelegram && (
            <a
              href={mensaje.linkTelegram}
              target="_blank"
              rel="noreferrer"
              className="flex-1 py-2.5 rounded-xl bg-blue-500 hover:bg-blue-600 text-white text-sm font-medium text-center transition-colors flex items-center justify-center gap-1.5"
            >
              <MessageCircle className="w-4 h-4" />
              Telegram
            </a>
          )}
        </div>

        <button
          onClick={onClose}
          className="w-full mt-3 py-2.5 rounded-xl bg-gradient-to-r from-purple-500 to-purple-600 text-white text-sm font-medium"
        >
          Cerrar
        </button>
      </div>
    </div>
  </div>
);

// ── CarritoPage ──────────────────────────────────────────────────────────────
const CarritoPage = () => {
  const navigate   = useNavigate();
  const location   = useLocation();
  const { token }  = useAuthStore();
  const [hidratado, setHidratado] = useState(false);
  const [carrito, setCarrito]           = useState(null);
  const [loading, setLoading]           = useState(true);
  const [confirmando, setConfirmando]   = useState(false);
  const [pedidoConfirmado, setPedidoConfirmado] = useState(false);
  const [mensajeData, setMensajeData]   = useState(null);
  const [modalVariantes, setModalVariantes] = useState(null); // { producto, pedidoId }
  const [actualizandoItem, setActualizandoItem] = useState(null);
  const [modalPago, setModalPago]   = useState(false);
const [medioPago, setMedioPago]   = useState("efectivo");
const [referenciaPago, setReferenciaPago] = useState("");
const [qrUrl, setQrUrl]           = useState("");

  // Si no está logueado, redirige
  // Esperar a que Zustand hidrate el store
useEffect(() => {
  const unsub = useAuthStore.persist.onFinishHydration(() => {
    setHidratado(true);
  });
  // Si ya está hidratado no dispara el evento, verificar manualmente
  if (useAuthStore.persist.hasHydrated()) {
    setHidratado(true);
  }
  return () => unsub();
}, []);

// Redirigir si no hay token (solo después de hidratar)
// Cargar QR configurado
useEffect(() => {
  if (!hidratado || !token) return;
  import("../../api/pagosApi").then(({ obtenerConfiguracion }) => {
    obtenerConfiguracion("qr_pago_url")
      .then((res) => setQrUrl(res.data.valor))
      .catch(() => {});
  });
}, [hidratado, token]);

  // Restaurar items pendientes del sessionStorage después del login
const restaurarPendientes = async (pedidoId) => {
  const raw = sessionStorage.getItem("carritosPendientes");
  if (!raw) return;

  try {
    const pendientes = JSON.parse(raw);
    for (const item of pendientes) {
      if (item.variantes?.length === 1) {
        // Una sola variante: agregar directo
        await agregarItem(pedidoId, {
          varianteId: item.variantes[0].id,
          cantidad:   item.cantidad,
        });
      } else if (item.variantes?.length > 1) {
        // Varias variantes: abrir modal
        setModalVariantes({ producto: item, pedidoId });
      }
    }
    sessionStorage.removeItem("carritosPendientes");
    toast.success("Productos restaurados en tu carrito 🛒");
  } catch {
    // silencioso
  }
};

  const cargarCarrito = async () => {
  try {
    setLoading(true);
    const res = await obtenerOCrearCarrito();
    setCarrito(res.data);
    sessionStorage.setItem("pedidoId", res.data.pedidoId);

    // Restaurar items que quedaron pendientes antes del login
    await restaurarPendientes(res.data.pedidoId);

    // Recargar carrito con los items restaurados
    const actualizado = await verCarrito();
    setCarrito(actualizado.data);
  } catch {
    toast.error("Error al cargar el carrito");
  } finally {
    setLoading(false);
  }
};

  useEffect(() => {
  if (!hidratado || !token) return;
  cargarCarrito();
}, [hidratado, token]);

  // Si viene desde la landing con un producto de múltiples variantes
  useEffect(() => {
    const { productoId, pedidoId } = location.state || {};
    if (productoId && pedidoId) {
      getProductoDetalle(productoId).then((res) => {
        setModalVariantes({ producto: res.data, pedidoId });
      });
    }
  }, [location.state]);

  const handleActualizarCantidad = async (itemId, nuevaCantidad) => {
    if (nuevaCantidad < 1) return;
    setActualizandoItem(itemId);
    try {
      const res = await actualizarItem(carrito.pedidoId, itemId, nuevaCantidad);
      setCarrito(res.data);
    } catch (err) {
      toast.error(err.response?.data?.message || "Error al actualizar");
    } finally {
      setActualizandoItem(null);
    }
  };

  const handleEliminarItem = async (itemId) => {
    setActualizandoItem(itemId);
    try {
      const res = await eliminarItem(carrito.pedidoId, itemId);
      setCarrito(res.data);
      toast.success("Producto eliminado");
    } catch {
      toast.error("Error al eliminar el producto");
    } finally {
      setActualizandoItem(null);
    }
  };

  const handleConfirmar = async () => {
  if ((medioPago === "transferencia" || medioPago === "qr") && !referenciaPago.trim()) {
    toast.error("Ingresa el número de comprobante");
    return;
  }
  if (!carrito?.items?.length) {
    toast.error("Tu carrito está vacío");
    return;
  }
  setConfirmando(true);
  try {
    await confirmarPedido(carrito.pedidoId);
    const msgRes = await generarMensaje(carrito.pedidoId);
    // Agregar método de pago al mensaje
    const mensajeConPago = {
      ...msgRes.data,
      mensaje: msgRes.data.mensaje
        + `\nMétodo de pago: ${medioPago}`
        + (referenciaPago ? `\nComprobante: ${referenciaPago}` : ""),
    };
    setMensajeData(mensajeConPago);
    setPedidoConfirmado(true);
    setModalPago(false);
    toast.success("¡Pedido confirmado! 🎉");
  } catch (err) {
    toast.error(err.response?.data?.message || "Error al confirmar el pedido");
  } finally {
    setConfirmando(false);
  }
};

  if (loading) {
    return (
      <div className="min-h-screen bg-gray-50 flex items-center justify-center">
        <Loader2 className="w-8 h-8 text-purple-500 animate-spin" />
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gray-50">

      {/* Navbar */}
      <nav className="bg-white border-b border-gray-100 px-6 py-4 sticky top-0 z-40">
        <div className="max-w-4xl mx-auto flex items-center justify-between">
          <div className="flex items-center gap-3">
            <button
              onClick={() => navigate("/#tienda")}
              className="flex items-center gap-1.5 text-sm text-gray-500 hover:text-purple-600 transition-colors"
            >
              <ArrowLeft className="w-4 h-4" />
              Seguir comprando
            </button>
          </div>
          <Link to="/" className="flex items-center gap-2">
            <div className="w-8 h-8 rounded-lg bg-gradient-to-br from-purple-500 to-green-500 flex items-center justify-center">
              <Heart className="w-4 h-4 text-white" />
            </div>
            <span className="font-semibold text-gray-900">
              Paw<span className="bg-gradient-to-r from-purple-600 to-green-500 bg-clip-text text-transparent">Spa</span>
            </span>
          </Link>
          <Link
            to="/cliente"
            className="text-sm text-gray-500 hover:text-purple-600 transition-colors"
          >
            Mi cuenta
          </Link>
        </div>
      </nav>

      <div className="max-w-4xl mx-auto px-4 py-8">
        <div className="flex items-center gap-3 mb-8">
          <ShoppingCart className="w-6 h-6 text-purple-500" />
          <h1 className="text-2xl font-bold text-gray-900">Mi carrito</h1>
          {carrito?.items?.length > 0 && (
            <span className="bg-purple-100 text-purple-600 text-xs font-medium px-2.5 py-1 rounded-full">
              {carrito.items.length} {carrito.items.length === 1 ? "producto" : "productos"}
            </span>
          )}
        </div>

        {/* Carrito vacío */}
        {!carrito?.items?.length ? (
          <div className="bg-white rounded-2xl border border-gray-100 p-16 text-center">
            <ShoppingCart className="w-14 h-14 text-gray-200 mx-auto mb-4" />
            <h3 className="font-semibold text-gray-900 mb-2">Tu carrito está vacío</h3>
            <p className="text-sm text-gray-400 mb-6">
              Explora nuestra tienda y agrega los productos que necesita tu mascota.
            </p>
            <button
              onClick={() => navigate("/#tienda")}
              className="inline-flex items-center gap-2 px-6 py-3 rounded-xl bg-gradient-to-r from-purple-500 to-purple-600 text-white text-sm font-medium"
            >
              <Package className="w-4 h-4" />
              Ver tienda
            </button>
          </div>
        ) : (
          <div className="grid md:grid-cols-3 gap-6">

            {/* Lista de items */}
            <div className="md:col-span-2 space-y-3">
              {carrito.items.map((item) => (
                <div
                  key={item.itemId}
                  className="bg-white rounded-2xl border border-gray-100 p-4 flex items-center gap-4"
                >
                  {/* Imagen */}
                  <div className="w-16 h-16 rounded-xl bg-gray-50 flex items-center justify-center shrink-0 overflow-hidden">
                    {item.productoImagenUrl ? (
                      <img
                        src={item.productoImagenUrl}
                        alt={item.productoNombre}
                        className="w-full h-full object-cover"
                      />
                    ) : (
                      <span className="text-2xl">🐾</span>
                    )}
                  </div>

                  {/* Info */}
                  <div className="flex-1 min-w-0">
                    <div className="font-medium text-gray-900 truncate">{item.productoNombre}</div>
                    <div className="text-xs text-gray-400 mt-0.5">{item.varianteNombre}</div>
                    <div className="text-sm font-semibold text-purple-600 mt-1">
                      Bs. {Number(item.precioUnitario).toFixed(2)}
                    </div>
                  </div>

                  {/* Cantidad */}
                  <div className="flex items-center gap-2 shrink-0">
                    {actualizandoItem === item.itemId ? (
                      <Loader2 className="w-5 h-5 text-purple-400 animate-spin" />
                    ) : (
                      <>
                        <button
                          onClick={() => handleActualizarCantidad(item.itemId, item.cantidad - 1)}
                          className="w-7 h-7 rounded-lg border border-gray-200 flex items-center justify-center hover:bg-gray-50 text-gray-600"
                        >
                          <Minus className="w-3 h-3" />
                        </button>
                        <span className="text-sm font-semibold w-6 text-center">{item.cantidad}</span>
                        <button
                          onClick={() => handleActualizarCantidad(item.itemId, item.cantidad + 1)}
                          className="w-7 h-7 rounded-lg border border-gray-200 flex items-center justify-center hover:bg-gray-50 text-gray-600"
                        >
                          <Plus className="w-3 h-3" />
                        </button>
                      </>
                    )}
                  </div>

                  {/* Subtotal y eliminar */}
                  <div className="text-right shrink-0">
                    <div className="text-sm font-bold text-gray-900">
                      Bs. {Number(item.subtotal).toFixed(2)}
                    </div>
                    <button
                      onClick={() => handleEliminarItem(item.itemId)}
                      className="mt-1 text-gray-300 hover:text-red-400 transition-colors"
                    >
                      <Trash2 className="w-4 h-4" />
                    </button>
                  </div>
                </div>
              ))}
            </div>

            {/* Resumen */}
            <div className="space-y-4">
              <div className="bg-white rounded-2xl border border-gray-100 p-6">
                <h3 className="font-semibold text-gray-900 mb-4">Resumen</h3>
                <div className="space-y-2.5 text-sm">
                  <div className="flex justify-between text-gray-600">
                    <span>Subtotal</span>
                    <span>Bs. {Number(carrito.subtotal).toFixed(2)}</span>
                  </div>
                  {Number(carrito.descuento) > 0 && (
                    <div className="flex justify-between text-green-600">
                      <span>Descuento</span>
                      <span>- Bs. {Number(carrito.descuento).toFixed(2)}</span>
                    </div>
                  )}
                  <div className="border-t border-gray-100 pt-2.5 flex justify-between font-bold text-gray-900 text-base">
                    <span>Total</span>
                    <span>Bs. {Number(carrito.total).toFixed(2)}</span>
                  </div>
                </div>

                {/* Modal selección método de pago */}
{modalPago && (
  <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/40 backdrop-blur-sm">
    <div className="bg-white rounded-2xl shadow-xl w-full max-w-md">
      <div className="flex items-center justify-between p-6 border-b border-gray-100">
        <h3 className="font-semibold text-gray-900">Método de pago</h3>
        <button onClick={() => setModalPago(false)} className="text-gray-400 hover:text-gray-600">
          <X className="w-5 h-5" />
        </button>
      </div>
      <div className="p-6 space-y-5">

        {/* Resumen */}
        <div className="bg-gray-50 rounded-xl p-4 flex justify-between items-center">
          <span className="text-sm text-gray-600">Total a pagar</span>
          <span className="text-xl font-bold text-purple-600">
            Bs. {Number(carrito?.total).toFixed(2)}
          </span>
        </div>

        {/* Métodos */}
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-3">
            Selecciona cómo vas a pagar
          </label>
          <div className="grid grid-cols-2 gap-3">
            {[
              { value: "efectivo",      label: "Efectivo",      icon: "💵" },
              { value: "tarjeta",       label: "Tarjeta",       icon: "💳" },
              { value: "transferencia", label: "Transferencia", icon: "🔄" },
              { value: "qr",            label: "QR",            icon: "📱" },
            ].map((m) => (
              <button
                key={m.value}
                type="button"
                onClick={() => setMedioPago(m.value)}
                className={`flex items-center gap-2 p-4 rounded-xl border-2 transition-all text-left ${
                  medioPago === m.value
                    ? "border-purple-400 bg-purple-50 text-purple-700"
                    : "border-gray-200 text-gray-600 hover:border-purple-200"
                }`}
              >
                <span className="text-lg">{m.icon}</span>
                <span className="text-sm font-medium">{m.label}</span>
              </button>
            ))}
          </div>
        </div>

        {/* QR imagen */}
        {medioPago === "qr" && (
          <div className="flex flex-col items-center gap-3 bg-gray-50 rounded-xl p-4 border border-gray-100">
            <p className="text-xs text-gray-500 text-center">
              Escanea el QR y realiza el pago, luego ingresa el número de comprobante
            </p>
            {qrUrl ? (
              <img
                src={qrUrl}
                alt="QR de pago"
                className="w-44 h-44 object-contain rounded-xl border border-gray-200 bg-white p-2"
              />
            ) : (
              <div className="w-44 h-44 rounded-xl border-2 border-dashed border-gray-200 flex items-center justify-center text-gray-400 text-sm text-center p-4">
                QR no configurado aún
              </div>
            )}
          </div>
        )}

        {/* Referencia para transferencia y QR */}
        {(medioPago === "transferencia" || medioPago === "qr") && (
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              N° de comprobante <span className="text-red-500">*</span>
            </label>
            <input
              type="text"
              value={referenciaPago}
              onChange={(e) => setReferenciaPago(e.target.value)}
              placeholder="Ej: TRX-123456"
              className="w-full px-4 py-2.5 rounded-xl border border-gray-200 focus:outline-none focus:ring-2 focus:ring-purple-300 text-sm"
            />
          </div>
        )}

        <div className="flex gap-3 pt-2">
          <button
            onClick={() => setModalPago(false)}
            className="flex-1 py-3 rounded-xl border border-gray-200 text-sm text-gray-600 hover:bg-gray-50"
          >
            Cancelar
          </button>
          <button
            onClick={handleConfirmar}
            disabled={confirmando}
            className="flex-1 py-3 rounded-xl bg-gradient-to-r from-purple-500 to-purple-600 text-white font-medium text-sm disabled:opacity-60 flex items-center justify-center gap-2"
          >
            {confirmando
              ? <><Loader2 className="w-4 h-4 animate-spin" /> Confirmando...</>
              : <><CheckCircle className="w-4 h-4" /> Confirmar pedido</>
            }
          </button>
        </div>
      </div>
    </div>
  </div>
)}

                
<button
  onClick={() => setModalPago(true)}
  disabled={confirmando || pedidoConfirmado || !carrito?.items?.length}
  className="w-full mt-5 py-3 rounded-xl bg-gradient-to-r from-purple-500 to-purple-600 hover:from-purple-600 hover:to-purple-700 text-white font-medium text-sm transition-all shadow-sm disabled:opacity-60 flex items-center justify-center gap-2"
>
  {pedidoConfirmado ? (
    <><CheckCircle className="w-4 h-4" /> Pedido confirmado</>
  ) : (
    <>Confirmar pedido <ChevronRight className="w-4 h-4" /></>
  )}
</button>

                {pedidoConfirmado && (
                  <button
                    onClick={() => setMensajeData(mensajeData)}
                    className="w-full mt-3 py-2.5 rounded-xl border border-green-200 bg-green-50 text-green-700 text-sm font-medium flex items-center justify-center gap-2 hover:bg-green-100 transition-colors"
                  >
                    <MessageCircle className="w-4 h-4" />
                    Ver mensaje de pedido
                  </button>
                )}
              </div>

              <div className="bg-purple-50 rounded-2xl p-4 text-xs text-purple-600 border border-purple-100">
                🐾 Al confirmar tu pedido, te enviaremos el detalle por {" "}
                <strong>{carrito.items[0] ? "WhatsApp o Telegram" : "tu canal preferido"}</strong>{" "}
                para coordinar la entrega.
              </div>
            </div>

          </div>
        )}
      </div>

      {/* Modal variantes */}
      {modalVariantes && (
        <ModalVariantes
          producto={modalVariantes.producto}
          pedidoId={modalVariantes.pedidoId}
          onClose={() => setModalVariantes(null)}
          onAgregado={cargarCarrito}
        />
      )}

      {/* Modal mensaje */}
      {mensajeData && (
        <ModalMensaje
          mensaje={mensajeData}
          onClose={() => {
            setMensajeData(null);
            setCarrito((prev) => ({ ...prev, items: [] }));
            navigate("/cliente");
          }}
        />
      )}

    </div>
  );
};

export default CarritoPage;
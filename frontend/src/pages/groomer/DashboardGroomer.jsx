import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import toast from "react-hot-toast";
import {
  Heart, LogOut, Calendar, Clock,
  CheckCircle, AlertCircle, Loader2,
  Scissors, X, ChevronDown, ChevronUp,
  ClipboardList, User
} from "lucide-react";

import {
  getMisCitasHoy, getMisCitas,
  abrirFicha, actualizarFicha, cerrarFicha, getFichaPorCita,
  getProductosDisponibles, getInsumosPorFicha,
  registrarInsumo, eliminarInsumo, getAlertasStockBajo
} from "../../api/groomerApi";

import useAuthStore from "../../store/authStore";
import { format } from "date-fns";
import { es } from "date-fns/locale";

const estadoColor = {
  pendiente:  "bg-yellow-100 text-yellow-700",
  confirmada: "bg-blue-100 text-blue-700",
  en_proceso: "bg-purple-100 text-purple-700",
  completada: "bg-green-100 text-green-700",
  cancelada:  "bg-red-100 text-red-600",
};

const temperamentoColor = {
  tranquilo:   "bg-green-100 text-green-700",
  normal:      "bg-blue-100 text-blue-700",
  nervioso:    "bg-yellow-100 text-yellow-700",
  agresivo:    "bg-red-100 text-red-600",
  desconocido: "bg-gray-100 text-gray-600",
};

const Modal = ({ title, onClose, children }) => (
  <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/40 backdrop-blur-sm">
    <div className="bg-white rounded-2xl shadow-xl w-full max-w-lg max-h-[90vh] overflow-y-auto">
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

const DashboardGroomer = () => {
  const [citasHoy, setCitasHoy]     = useState([]);
  const [todasCitas, setTodasCitas] = useState([]);
  const [loadingHoy, setLoadingHoy] = useState(true);
  const [loadingTodas, setLoadingTodas] = useState(true);
  const [vista, setVista]           = useState("hoy");
  const [fichaActual, setFichaActual] = useState(null);
  const [citaFichaAbierta, setCitaFichaAbierta] = useState(null);
  const [loadingFicha, setLoadingFicha] = useState(false);
  const [expandida, setExpandida]   = useState(null);
  const { logout, usuario }         = useAuthStore();
  const [insumos, setInsumos]               = useState([]);
const [productos, setProductos]           = useState([]);
const [alertasStock, setAlertasStock]     = useState([]);
const [varianteSeleccionada, setVarianteSeleccionada] = useState("");
const [cantidadInsumo, setCantidadInsumo] = useState("1");
const [unidadInsumo, setUnidadInsumo]     = useState("unidad");
const [tabFicha, setTabFicha]             = useState("checklist");
const [loadingInsumo, setLoadingInsumo]   = useState(false);
  const navigate                    = useNavigate();

  const inputClass = "w-full px-4 py-2.5 rounded-xl border border-gray-200 focus:outline-none focus:ring-2 focus:ring-purple-300 text-sm";

  const cargarCitasHoy = async () => {
    try {
      const res = await getMisCitasHoy();
      setCitasHoy(res.data);
    } catch {
      toast.error("Error al cargar citas de hoy");
    } finally {
      setLoadingHoy(false);
    }
  };

  const cargarTodasCitas = async () => {
    try {
      const res = await getMisCitas();
      setTodasCitas(res.data);
    } catch {
      toast.error("Error al cargar citas");
    } finally {
      setLoadingTodas(false);
    }
  };

  const cargarProductos = async () => {
  try {
    const res = await getProductosDisponibles();
    setProductos(res.data);
  } catch {
    toast.error("Error al cargar productos");
  }
};

const cargarInsumos = async (fichaId) => {
  try {
    const res = await getInsumosPorFicha(fichaId);
    setInsumos(res.data);
  } catch {
    toast.error("Error al cargar insumos");
  }
};

const cargarAlertas = async () => {
  try {
    const res = await getAlertasStockBajo();
    setAlertasStock(res.data);
  } catch {}
};

const handleAgregarInsumo = async () => {
  if (!varianteSeleccionada) {
    toast.error("Selecciona un producto");
    return;
  }
  setLoadingInsumo(true);
  try {
    await registrarInsumo(fichaActual.id, {
      varianteId: varianteSeleccionada,
      cantidad:   parseFloat(cantidadInsumo),
      unidad:     unidadInsumo,
    });
    toast.success("Insumo registrado");
    setVarianteSeleccionada("");
    setCantidadInsumo("1");
    cargarInsumos(fichaActual.id);
  } catch (err) {
    toast.error(err.response?.data?.message || "Error al registrar insumo");
  } finally {
    setLoadingInsumo(false);
  }
};

const handleEliminarInsumo = async (id) => {
  try {
    await eliminarInsumo(id);
    toast.success("Insumo eliminado y stock restaurado");
    cargarInsumos(fichaActual.id);
  } catch {
    toast.error("Error al eliminar insumo");
  }
};

  useEffect(() => {
  cargarCitasHoy();
  cargarTodasCitas();
  cargarAlertas();
}, []);

  const handleLogout = () => { logout(); navigate("/"); };

  const handleAbrirFicha = async (cita) => {
  setLoadingFicha(true);
  try {
    let ficha;
    const res = await getFichaPorCita(cita.id);
    
    if (res.status === 404 || !res.data) {
      // No existe, crear nueva
      const nuevaRes = await abrirFicha(cita.id);
      ficha = nuevaRes.data;
      toast.success("Ficha abierta");
    } else {
      ficha = res.data;
    }
    
    setFichaActual(ficha);
    setCitaFichaAbierta(cita);
    cargarInsumos(ficha.id);
    cargarProductos();
  } catch (err) {
    if (err.response?.status === 404) {
      // Crear nueva ficha
      try {
        const nuevaRes = await abrirFicha(cita.id);
        setFichaActual(nuevaRes.data);
        setCitaFichaAbierta(cita);
        cargarInsumos(nuevaRes.data.id);
        cargarProductos();
        toast.success("Ficha abierta");
      } catch (err2) {
        toast.error(err2.response?.data?.message || "Error al abrir ficha");
      }
    } else {
      toast.error(err.response?.data?.message || "Error al abrir ficha");
    }
  } finally {
    setLoadingFicha(false);
  }
};

  const handleGuardarFicha = async () => {
    setLoadingFicha(true);
    try {
      const res = await actualizarFicha(fichaActual.id, fichaActual);
      setFichaActual(res.data);
      toast.success("Ficha guardada");
    } catch {
      toast.error("Error al guardar ficha");
    } finally {
      setLoadingFicha(false);
    }
  };

  const handleCerrarFicha = async () => {
    setLoadingFicha(true);
    try {
      await cerrarFicha(fichaActual.id);
      toast.success("¡Servicio completado!");
      setCitaFichaAbierta(null);
      setFichaActual(null);
      cargarCitasHoy();
      cargarTodasCitas();
    } catch (err) {
      toast.error(err.response?.data?.message || "Error al cerrar ficha");
    } finally {
      setLoadingFicha(false);
    }
  };

  const citas   = vista === "hoy" ? citasHoy : todasCitas;
  const loading = vista === "hoy" ? loadingHoy : loadingTodas;

  const stats = {
    hoy:        citasHoy.length,
    pendientes: citasHoy.filter(c => c.estado === "pendiente").length,
    enProceso:  citasHoy.filter(c => c.estado === "en_proceso").length,
    completadas: citasHoy.filter(c => c.estado === "completada").length,
  };

  return (
    <div className="min-h-screen bg-gray-50">

      {/* Navbar */}
      <nav className="bg-white border-b border-gray-100 px-6 py-4">
        <div className="max-w-6xl mx-auto flex items-center justify-between">
          <div className="flex items-center gap-2">
            <div className="w-8 h-8 rounded-lg bg-gradient-to-br from-purple-500 to-green-500 flex items-center justify-center">
              <Heart className="w-4 h-4 text-white" />
            </div>
            <span className="font-semibold text-gray-900">
              Paw<span className="bg-gradient-to-r from-purple-600 to-green-500 bg-clip-text text-transparent">Spa</span>
            </span>
            <span className="ml-3 text-sm text-gray-400">Panel Groomer</span>
          </div>
          <div className="flex items-center gap-3">
            <span className="text-sm text-gray-600">Hola, <strong>{usuario?.nombre}</strong> ✂️</span>
            <button onClick={handleLogout} className="flex items-center gap-2 text-sm text-gray-500 hover:text-red-500 transition-colors px-3 py-2 rounded-lg hover:bg-red-50">
              <LogOut className="w-4 h-4" />
              Salir
            </button>
          </div>
        </div>
      </nav>

      <div className="max-w-6xl mx-auto px-6 py-8">

        {/* Bienvenida */}
        <div className="bg-gradient-to-r from-purple-500 to-green-500 rounded-2xl p-6 mb-8 text-white">
          <div className="flex items-center gap-3 mb-1">
            <Scissors className="w-6 h-6" />
            <h1 className="text-2xl font-bold">Hola, {usuario?.nombre} ✂️</h1>
          </div>
          <p className="text-purple-100 text-sm">
            {format(new Date(), "EEEE dd 'de' MMMM yyyy", { locale: es })}
          </p>
        </div>

        {/* Stats */}
        <div className="grid grid-cols-2 md:grid-cols-4 gap-4 mb-8">
          {[
            { label: "Citas hoy",   valor: stats.hoy,         color: "text-purple-600" },
            { label: "Pendientes",  valor: stats.pendientes,  color: "text-yellow-600" },
            { label: "En proceso",  valor: stats.enProceso,   color: "text-blue-600"   },
            { label: "Completadas", valor: stats.completadas, color: "text-green-600"  },
          ].map((s, i) => (
            <div key={i} className="bg-white rounded-2xl border border-gray-100 p-5">
              <div className={`text-2xl font-bold ${s.color}`}>{s.valor}</div>
              <div className="text-sm text-gray-500 mt-1">{s.label}</div>
            </div>
          ))}
        </div>

        {/* Alertas stock bajo */}
{alertasStock.length > 0 && (
  <div className="bg-yellow-50 border border-yellow-200 rounded-2xl p-4 mb-6">
    <div className="flex items-center gap-2 mb-2">
      <AlertCircle className="w-4 h-4 text-yellow-600" />
      <span className="text-sm font-medium text-yellow-700">
        ⚠️ {alertasStock.length} producto(s) con stock bajo
      </span>
    </div>
    <div className="flex flex-wrap gap-2">
      {alertasStock.map((p, i) => (
        <span key={i} className="text-xs bg-yellow-100 text-yellow-700 px-2 py-1 rounded-lg">
          {p.nombre}
        </span>
      ))}
    </div>
  </div>
)}

        {/* Tabs */}
        <div className="bg-white rounded-2xl border border-gray-100">
          <div className="flex items-center gap-2 p-4 border-b border-gray-100">
            <button
              onClick={() => setVista("hoy")}
              className={`px-4 py-2 rounded-xl text-sm font-medium transition-all ${vista === "hoy" ? "bg-purple-100 text-purple-700" : "text-gray-500 hover:bg-gray-50"}`}
            >
              Hoy ({stats.hoy})
            </button>
            <button
              onClick={() => setVista("todas")}
              className={`px-4 py-2 rounded-xl text-sm font-medium transition-all ${vista === "todas" ? "bg-purple-100 text-purple-700" : "text-gray-500 hover:bg-gray-50"}`}
            >
              Todas las citas
            </button>
          </div>

          {loading ? (
            <div className="flex items-center justify-center py-16">
              <Loader2 className="w-8 h-8 text-purple-500 animate-spin" />
            </div>
          ) : citas.length === 0 ? (
            <div className="text-center py-16">
              <Calendar className="w-12 h-12 text-gray-200 mx-auto mb-3" />
              <p className="text-gray-400">{vista === "hoy" ? "No tienes citas para hoy" : "No tienes citas registradas"}</p>
            </div>
          ) : (
            <div className="divide-y divide-gray-50">
              {citas.map((c) => (
                <div key={c.id} className="p-5">
                  {/* Header de la cita */}
                  <div className="flex items-start justify-between gap-4">
                    <div className="flex items-start gap-4">
                      {/* Hora */}
                      <div className="text-center min-w-[55px] bg-purple-50 rounded-xl p-2">
                        <div className="text-sm font-bold text-purple-600">
                          {c.fechaInicio && format(new Date(c.fechaInicio), "HH:mm")}
                        </div>
                        <div className="text-xs text-gray-400">
                          {c.fechaFin && format(new Date(c.fechaFin), "HH:mm")}
                        </div>
                      </div>

                      {/* Info mascota */}
                      <div>
                        <div className="font-medium text-gray-900">
                          {c.mascotaNombre}
                          <span className="text-gray-400 font-normal text-sm"> — {c.servicioNombre}</span>
                        </div>
                        <div className="text-xs text-gray-500 mt-0.5 capitalize">
                          {c.mascotaEspecie} •{" "}
                          <span className={`inline-flex items-center px-1.5 py-0.5 rounded-full text-xs ${temperamentoColor[c.mascotaTemperamento]}`}>
                            {c.mascotaTemperamento}
                          </span>
                        </div>
                        <div className="text-xs text-gray-400 mt-0.5">
                          ⏱ {c.servicioDuracionMin} min
                        </div>
                        {vista === "todas" && (
                          <div className="text-xs text-gray-400 mt-0.5">
                            {c.fechaInicio && format(new Date(c.fechaInicio), "dd MMM yyyy", { locale: es })}
                          </div>
                        )}
                      </div>
                    </div>

                    {/* Estado y acciones */}
                    <div className="flex flex-col items-end gap-2">
                      <span className={`inline-flex items-center gap-1 px-2.5 py-1 rounded-full text-xs font-medium ${estadoColor[c.estado]}`}>
                        {c.estado}
                      </span>
                      <div className="flex gap-2">
                        {c.estado !== "cancelada" && c.estado !== "completada" && (
                          <button
                            onClick={() => handleAbrirFicha(c)}
                            disabled={loadingFicha}
                            className="text-xs px-3 py-1.5 rounded-lg bg-purple-50 text-purple-600 hover:bg-purple-100 transition-colors flex items-center gap-1"
                          >
                            <ClipboardList className="w-3.5 h-3.5" />
                            {c.estado === "en_proceso" ? "Ver ficha" : "Abrir ficha"}
                          </button>
                        )}
                        <button
                          onClick={() => setExpandida(expandida === c.id ? null : c.id)}
                          className="text-xs px-3 py-1.5 rounded-lg bg-gray-50 text-gray-600 hover:bg-gray-100 transition-colors flex items-center gap-1"
                        >
                          {expandida === c.id ? <ChevronUp className="w-3.5 h-3.5" /> : <ChevronDown className="w-3.5 h-3.5" />}
                          Detalle
                        </button>
                      </div>
                    </div>
                  </div>

                  {/* Detalle expandido */}
                  {expandida === c.id && (
                    <div className="mt-4 ml-[71px] bg-gray-50 rounded-xl p-4 space-y-2">
                      <div className="flex items-center gap-2 text-sm">
                        <User className="w-4 h-4 text-gray-400" />
                        <span className="text-gray-500">Especie:</span>
                        <span className="font-medium capitalize">{c.mascotaEspecie}</span>
                      </div>
                      <div className="flex items-center gap-2 text-sm">
                        <AlertCircle className="w-4 h-4 text-yellow-500" />
                        <span className="text-gray-500">Temperamento:</span>
                        <span className={`px-2 py-0.5 rounded-full text-xs font-medium ${temperamentoColor[c.mascotaTemperamento]}`}>
                          {c.mascotaTemperamento}
                        </span>
                      </div>
                      <div className="flex items-center gap-2 text-sm">
                        <Scissors className="w-4 h-4 text-purple-400" />
                        <span className="text-gray-500">Servicio:</span>
                        <span className="font-medium">{c.servicioNombre} — {c.servicioDuracionMin} min</span>
                      </div>
                      {c.notasCliente && (
                        <div className="text-sm text-gray-500 italic">
                          📝 "{c.notasCliente}"
                        </div>
                      )}
                    </div>
                  )}
                </div>
              ))}
            </div>
          )}
        </div>
      </div>

      {/* Modal Ficha Grooming */}
      {citaFichaAbierta && fichaActual && (
  <Modal
    title={`Ficha — ${citaFichaAbierta.mascotaNombre}`}
    onClose={() => { setCitaFichaAbierta(null); setFichaActual(null); setInsumos([]); }}
  >
    <div className="space-y-4">

      {/* Info mascota */}
      <div className="bg-purple-50 rounded-xl p-4 space-y-1">
        <div className="font-medium text-gray-900">{citaFichaAbierta.mascotaNombre}</div>
        <div className="text-sm text-gray-600 capitalize">
          {citaFichaAbierta.mascotaEspecie} •{" "}
          <span className={`px-1.5 py-0.5 rounded-full text-xs ${temperamentoColor[citaFichaAbierta.mascotaTemperamento]}`}>
            {citaFichaAbierta.mascotaTemperamento}
          </span>
        </div>
        <div className="text-sm text-gray-600">
          🔧 {citaFichaAbierta.servicioNombre} — ⏱ {citaFichaAbierta.servicioDuracionMin} min
        </div>
        {fichaActual.mascotaAlergias && (
          <div className="text-xs text-red-600">⚠️ Alergias: {fichaActual.mascotaAlergias}</div>
        )}
        {fichaActual.mascotaRestricciones && (
          <div className="text-xs text-orange-600">🚫 Restricciones: {fichaActual.mascotaRestricciones}</div>
        )}
      </div>

      {/* Tabs */}
      <div className="flex gap-2 border-b border-gray-100 pb-2">
        {["checklist", "insumos"].map((tab) => (
          <button
            key={tab}
            onClick={() => setTabFicha(tab)}
            className={`px-4 py-2 rounded-xl text-sm font-medium transition-all ${
              tabFicha === tab
                ? "bg-purple-100 text-purple-700"
                : "text-gray-500 hover:bg-gray-50"
            }`}
          >
            {tab === "checklist" ? "📋 Checklist" : "🧴 Insumos"}
            {tab === "insumos" && insumos.length > 0 && (
              <span className="ml-1.5 bg-purple-200 text-purple-700 text-xs px-1.5 py-0.5 rounded-full">
                {insumos.length}
              </span>
            )}
          </button>
        ))}
      </div>

      {/* Tab Checklist */}
      {tabFicha === "checklist" && (
        <div className="space-y-4">

          {/* Condición ingreso */}
          <div>
            <p className="text-sm font-medium text-gray-700 mb-2">Condición al ingreso</p>
            <div className="space-y-2">
              {[
                { key: "tieneNudos",   label: "Tiene nudos"   },
                { key: "tienePulgas",  label: "Tiene pulgas"  },
                { key: "tieneHeridas", label: "Tiene heridas" },
              ].map((item) => (
                <label key={item.key} className="flex items-center gap-3 p-3 rounded-xl border border-gray-100 hover:bg-gray-50 cursor-pointer">
                  <input
                    type="checkbox"
                    checked={fichaActual[item.key] || false}
                    disabled={fichaActual.estado === "cerrada"}
                    onChange={(e) => setFichaActual({ ...fichaActual, [item.key]: e.target.checked })}
                    className="w-4 h-4 text-purple-600 rounded"
                  />
                  <span className="text-sm text-gray-700">{item.label}</span>
                </label>
              ))}
            </div>
            {fichaActual.tieneNudos && (
              <div className="mt-2">
                <label className="block text-xs text-gray-500 mb-1">Nivel de nudos</label>
                <select
                  value={fichaActual.nivelNudos || ""}
                  disabled={fichaActual.estado === "cerrada"}
                  onChange={(e) => setFichaActual({ ...fichaActual, nivelNudos: e.target.value })}
                  className={inputClass}
                >
                  <option value="">Seleccionar</option>
                  <option value="leve">Leve</option>
                  <option value="moderado">Moderado</option>
                  <option value="severo">Severo</option>
                </select>
              </div>
            )}
          </div>

          {/* Peso */}
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Peso actual (kg) <span className="text-gray-400">(opcional)</span>
            </label>
            <input
              type="number" step="0.01"
              value={fichaActual.pesoKgActual || ""}
              disabled={fichaActual.estado === "cerrada"}
              onChange={(e) => setFichaActual({ ...fichaActual, pesoKgActual: e.target.value })}
              placeholder="0.00"
              className={inputClass}
            />
          </div>

          {/* Observaciones */}
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Observaciones de ingreso</label>
            <textarea rows={2}
              value={fichaActual.observacionesIngreso || ""}
              disabled={fichaActual.estado === "cerrada"}
              onChange={(e) => setFichaActual({ ...fichaActual, observacionesIngreso: e.target.value })}
              placeholder="Estado general al ingreso..."
              className={inputClass}
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Observaciones de salida</label>
            <textarea rows={2}
              value={fichaActual.observacionesSalida || ""}
              disabled={fichaActual.estado === "cerrada"}
              onChange={(e) => setFichaActual({ ...fichaActual, observacionesSalida: e.target.value })}
              placeholder="Estado general al terminar..."
              className={inputClass}
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Recomendaciones para el dueño</label>
            <textarea rows={2}
              value={fichaActual.recomendaciones || ""}
              disabled={fichaActual.estado === "cerrada"}
              onChange={(e) => setFichaActual({ ...fichaActual, recomendaciones: e.target.value })}
              placeholder="Cuidados recomendados..."
              className={inputClass}
            />
          </div>
        </div>
      )}

      {/* Tab Insumos */}
      {tabFicha === "insumos" && (
        <div className="space-y-4">

          {/* Agregar insumo */}
          {fichaActual.estado !== "cerrada" && (
            <div className="bg-gray-50 rounded-xl p-4 space-y-3">
              <p className="text-sm font-medium text-gray-700">Agregar insumo usado</p>
              <select
                value={varianteSeleccionada}
                onChange={(e) => setVarianteSeleccionada(e.target.value)}
                className={inputClass}
              >
                <option value="">Seleccionar producto</option>
                {productos.map((p) => (
                  <option key={p.id} value={p.id}>{p.nombre}</option>
                ))}
              </select>
              <div className="grid grid-cols-2 gap-3">
                <div>
                  <label className="block text-xs text-gray-500 mb-1">Cantidad</label>
                  <input
                    type="number" step="0.1" min="0.1"
                    value={cantidadInsumo}
                    onChange={(e) => setCantidadInsumo(e.target.value)}
                    className={inputClass}
                  />
                </div>
                <div>
                  <label className="block text-xs text-gray-500 mb-1">Unidad</label>
                  <select
                    value={unidadInsumo}
                    onChange={(e) => setUnidadInsumo(e.target.value)}
                    className={inputClass}
                  >
                    <option value="unidad">Unidad</option>
                    <option value="ml">ml</option>
                    <option value="gr">gr</option>
                    <option value="litro">Litro</option>
                  </select>
                </div>
              </div>
              <button
                onClick={handleAgregarInsumo}
                disabled={loadingInsumo}
                className="w-full py-2.5 rounded-xl bg-gradient-to-r from-purple-500 to-purple-600 text-white text-sm font-medium disabled:opacity-60 flex items-center justify-center gap-2"
              >
                {loadingInsumo && <Loader2 className="w-4 h-4 animate-spin" />}
                + Registrar insumo
              </button>
            </div>
          )}

          {/* Lista insumos */}
          {insumos.length === 0 ? (
            <div className="text-center py-6 text-gray-400 text-sm">
              No hay insumos registrados
            </div>
          ) : (
            <div className="space-y-2">
              {insumos.map((i) => (
                <div key={i.id} className="flex items-center justify-between p-3 rounded-xl border border-gray-100">
                  <div>
                    <div className="text-sm font-medium text-gray-900">{i.productoNombre}</div>
                    <div className="text-xs text-gray-400">{i.varianteNombre} — {i.cantidad} {i.unidad}</div>
                  </div>
                  {fichaActual.estado !== "cerrada" && (
                    <button
                      onClick={() => handleEliminarInsumo(i.id)}
                      className="p-1.5 rounded-lg text-gray-400 hover:text-red-500 hover:bg-red-50 transition-colors"
                    >
                      <X className="w-4 h-4" />
                    </button>
                  )}
                </div>
              ))}
            </div>
          )}
        </div>
      )}

      {/* Botones acción */}
      <div className="flex gap-3 pt-2 border-t border-gray-100">
        <button
          onClick={handleGuardarFicha}
          disabled={loadingFicha || fichaActual.estado === "cerrada"}
          className="flex-1 py-2.5 rounded-xl border border-purple-200 text-purple-600 text-sm font-medium hover:bg-purple-50 disabled:opacity-50 flex items-center justify-center gap-2"
        >
          {loadingFicha && <Loader2 className="w-4 h-4 animate-spin" />}
          Guardar
        </button>
        <button
          onClick={handleCerrarFicha}
          disabled={loadingFicha || fichaActual.estado === "cerrada"}
          className="flex-1 py-2.5 rounded-xl bg-gradient-to-r from-green-500 to-green-600 text-white text-sm font-medium disabled:opacity-50 flex items-center justify-center gap-2"
        >
          {loadingFicha && <Loader2 className="w-4 h-4 animate-spin" />}
          {fichaActual.estado === "cerrada" ? "✅ Completado" : "Cerrar servicio"}
        </button>
      </div>

      {fichaActual.estado === "cerrada" && (
        <div className="text-center text-sm text-green-600 bg-green-50 rounded-xl p-3">
          ✅ Servicio completado el{" "}
          {fichaActual.cerradaEn && format(new Date(fichaActual.cerradaEn), "dd/MM/yyyy HH:mm")}
        </div>
      )}
    </div>
  </Modal>
)}

    </div>
  );
};

export default DashboardGroomer;
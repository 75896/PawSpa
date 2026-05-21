import { useState, useEffect } from "react";
import { Link, useNavigate } from "react-router-dom";
import toast from "react-hot-toast";
import {
  Heart, LogOut, Calendar, Plus, ChevronLeft,
  ChevronRight, Clock, CheckCircle, XCircle,
  AlertCircle, Loader2, X, Search
} from "lucide-react";

import useAuthStore from "../../store/authStore";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { z } from "zod";
import { format, addDays, subDays } from "date-fns";
import { es } from "date-fns/locale";
import { listarCitasPorFecha, agendarCita, confirmarCita, cancelarCita, listarServicios, listarGroomers } from "../../api/recepcionApi";
import api from "../../utils/axiosConfig";


const schema = z.object({
  mascotaId:   z.string().min(1, "Selecciona una mascota"),
  groomerId:   z.string().min(1, "Selecciona un groomer"),
  servicioId:  z.string().min(1, "Selecciona un servicio"),
  fechaInicio: z.string().min(1, "Selecciona fecha y hora"),
  notasCliente: z.string().optional(),
});

const estadoColor = {
  pendiente:  "bg-yellow-100 text-yellow-700 border-yellow-200",
  confirmada: "bg-blue-100 text-blue-700 border-blue-200",
  en_proceso: "bg-purple-100 text-purple-700 border-purple-200",
  completada: "bg-green-100 text-green-700 border-green-200",
  cancelada:  "bg-red-100 text-red-600 border-red-200",
};

const estadoIcon = {
  pendiente:  <Clock className="w-3 h-3" />,
  confirmada: <CheckCircle className="w-3 h-3" />,
  en_proceso: <Loader2 className="w-3 h-3 animate-spin" />,
  completada: <CheckCircle className="w-3 h-3" />,
  cancelada:  <XCircle className="w-3 h-3" />,
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

const AgendaPage = () => {
  const [fecha, setFecha]           = useState(new Date());
  const [citas, setCitas]           = useState([]);
  const [servicios, setServicios]   = useState([]);
  const [groomers, setGroomers]     = useState([]);
  const [loading, setLoading]       = useState(true);
  const [modalNueva, setModalNueva] = useState(false);
  const [modalCancelar, setModalCancelar] = useState(false);
  const [citaSeleccionada, setCitaSeleccionada] = useState(null);
  const [motivoCancelacion, setMotivoCancelacion] = useState("");
  const [loadingAccion, setLoadingAccion] = useState(false);
  const [busquedaMascota, setBusquedaMascota] = useState("");
  const [clientes, setClientes]         = useState([]);
const [mascotasCliente, setMascotasCliente] = useState([]);
const [clienteSeleccionado, setClienteSeleccionado] = useState(null);
const [busquedaCliente, setBusquedaCliente] = useState("");
const [clientesFiltrados, setClientesFiltrados] = useState([]);
  const { logout, usuario } = useAuthStore();
  const navigate = useNavigate();

  const { register, handleSubmit, reset, formState: { errors } } = useForm({
    resolver: zodResolver(schema),
  });

  const cargarCitas = async (d) => {
    setLoading(true);
    try {
      const fechaStr = format(d, "yyyy-MM-dd");
      const res = await listarCitasPorFecha(fechaStr);
      setCitas(res.data);
    } catch {
      toast.error("Error al cargar citas");
    } finally {
      setLoading(false);
    }
  };

  const cargarDatos = async () => {
    try {
      const [sRes, gRes] = await Promise.all([
        listarServicios(),
        listarGroomers(),
      ]);
      setServicios(sRes.data);
      setGroomers(gRes.data);
    } catch {
      toast.error("Error al cargar datos");
    }
  };

const cargarClientes = async () => {
  try {
    const res = await api.get("/recepcion/clientes");
    setClientes(res.data);
  } catch {
    toast.error("Error al cargar clientes");
  }
};

const cargarMascotasDeCliente = async (clienteId) => {
  try {
    const res = await api.get(`/recepcion/clientes/${clienteId}/mascotas`);
    setMascotasCliente(res.data);
  } catch {
    toast.error("Error al cargar mascotas");
  }
};

  useEffect(() => {
  cargarCitas(fecha);
  cargarDatos();
  cargarClientes();
}, []);

  const handleFechaAnterior = () => {
    const nueva = subDays(fecha, 1);
    setFecha(nueva);
    cargarCitas(nueva);
  };

  const handleFechaSiguiente = () => {
    const nueva = addDays(fecha, 1);
    setFecha(nueva);
    cargarCitas(nueva);
  };

  const handleLogout = () => {
    logout();
    navigate("/");
  };

  const handleAgendar = async (data) => {
    setLoadingAccion(true);
    try {
      await agendarCita({
        mascotaId:    data.mascotaId,
        groomerId:    data.groomerId,
        servicioId:   data.servicioId,
        fechaInicio:  new Date(data.fechaInicio).toISOString(),
        notasCliente: data.notasCliente,
        estado:       "pendiente",
      });
      toast.success("¡Cita agendada correctamente!");
      setModalNueva(false);
      reset();
      cargarCitas(fecha);
    } catch (err) {
      toast.error(err.response?.data?.message || "Error al agendar cita");
    } finally {
      setLoadingAccion(false);
    }
  };

  const handleConfirmar = async (id) => {
    try {
      await confirmarCita(id);
      toast.success("Cita confirmada");
      cargarCitas(fecha);
    } catch {
      toast.error("Error al confirmar");
    }
  };

  const handleCancelar = async () => {
    if (!motivoCancelacion.trim()) {
      toast.error("Ingresa un motivo de cancelación");
      return;
    }
    setLoadingAccion(true);
    try {
      await cancelarCita(citaSeleccionada.id, motivoCancelacion);
      toast.success("Cita cancelada");
      setModalCancelar(false);
      setMotivoCancelacion("");
      cargarCitas(fecha);
    } catch {
      toast.error("Error al cancelar");
    } finally {
      setLoadingAccion(false);
    }
  };

  const inputClass = "w-full px-4 py-2.5 rounded-xl border border-gray-200 focus:outline-none focus:ring-2 focus:ring-purple-300 text-sm";

  const stats = {
    total:      citas.length,
    pendientes: citas.filter(c => c.estado === "pendiente").length,
    confirmadas: citas.filter(c => c.estado === "confirmada").length,
    completadas: citas.filter(c => c.estado === "completada").length,
  };

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
            <span className="ml-2 text-sm text-gray-400">Agenda</span>
          </div>
          <div className="flex items-center gap-3">
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
        <div className="grid grid-cols-2 md:grid-cols-4 gap-4 mb-8">
          {[
            { label: "Total hoy",   valor: stats.total,       color: "text-purple-600" },
            { label: "Pendientes",  valor: stats.pendientes,  color: "text-yellow-600" },
            { label: "Confirmadas", valor: stats.confirmadas, color: "text-blue-600"   },
            { label: "Completadas", valor: stats.completadas, color: "text-green-600"  },
          ].map((s, i) => (
            <div key={i} className="bg-white rounded-2xl border border-gray-100 p-5">
              <div className={`text-2xl font-bold ${s.color}`}>{s.valor}</div>
              <div className="text-sm text-gray-500 mt-1">{s.label}</div>
            </div>
          ))}
        </div>

        <div className="bg-white rounded-2xl border border-gray-100">

          {/* Header con navegación de fecha */}
          <div className="flex flex-col sm:flex-row items-start sm:items-center justify-between gap-4 p-6 border-b border-gray-100">
            <div className="flex items-center gap-3">
              <button
                onClick={handleFechaAnterior}
                className="p-2 rounded-xl hover:bg-gray-100 transition-colors"
              >
                <ChevronLeft className="w-4 h-4 text-gray-600" />
              </button>
              <div className="text-center">
                <div className="font-semibold text-gray-900 capitalize">
                  {format(fecha, "EEEE dd 'de' MMMM yyyy", { locale: es })}
                </div>
              </div>
              <button
                onClick={handleFechaSiguiente}
                className="p-2 rounded-xl hover:bg-gray-100 transition-colors"
              >
                <ChevronRight className="w-4 h-4 text-gray-600" />
              </button>
              <button
                onClick={() => { setFecha(new Date()); cargarCitas(new Date()); }}
                className="px-3 py-1.5 rounded-xl bg-purple-50 text-purple-600 text-xs font-medium hover:bg-purple-100 transition-colors"
              >
                Hoy
              </button>
            </div>
            <button
              onClick={() => setModalNueva(true)}
              className="flex items-center gap-2 px-4 py-2 rounded-xl bg-gradient-to-r from-purple-500 to-purple-600 text-white text-sm font-medium hover:from-purple-600 hover:to-purple-700 transition-all"
            >
              <Plus className="w-4 h-4" />
              Nueva cita
            </button>
          </div>

          {/* Lista de citas */}
          {loading ? (
            <div className="flex items-center justify-center py-16">
              <Loader2 className="w-8 h-8 text-purple-500 animate-spin" />
            </div>
          ) : citas.length === 0 ? (
            <div className="text-center py-16">
              <Calendar className="w-12 h-12 text-gray-200 mx-auto mb-3" />
              <p className="text-gray-400 text-sm">No hay citas para este día</p>
              <button
                onClick={() => setModalNueva(true)}
                className="mt-3 text-sm text-purple-600 hover:underline"
              >
                Agendar una cita
              </button>
            </div>
          ) : (
            <div className="divide-y divide-gray-50">
              {citas.map((c) => (
                <div key={c.id} className="p-5 hover:bg-gray-50 transition-colors">
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

                      {/* Info */}
                      <div>
                        <div className="font-medium text-gray-900">
                          {c.mascotaNombre}
                          <span className="text-gray-400 font-normal text-sm"> — {c.servicioNombre}</span>
                        </div>
                        <div className="text-xs text-gray-500 mt-0.5">
                          Groomer: {c.groomerNombre} {c.groomerApellido}
                        </div>
                        <div className="text-xs text-gray-400 mt-0.5 capitalize">
                          {c.mascotaEspecie} • {c.mascotaTemperamento}
                        </div>
                        {c.notasCliente && (
                          <div className="text-xs text-gray-400 mt-1 italic">
                            "{c.notasCliente}"
                          </div>
                        )}
                      </div>
                    </div>

                    {/* Estado y acciones */}
                    <div className="flex flex-col items-end gap-2">
                      <span className={`inline-flex items-center gap-1 px-2.5 py-1 rounded-full text-xs font-medium border ${estadoColor[c.estado]}`}>
                        {estadoIcon[c.estado]}
                        {c.estado}
                      </span>
                      <div className="flex gap-2">
                        {c.estado === "pendiente" && (
                          <button
                            onClick={() => handleConfirmar(c.id)}
                            className="text-xs px-3 py-1 rounded-lg bg-blue-50 text-blue-600 hover:bg-blue-100 transition-colors"
                          >
                            Confirmar
                          </button>
                        )}
                        {c.estado !== "cancelada" && c.estado !== "completada" && (
                          <button
                            onClick={() => { setCitaSeleccionada(c); setModalCancelar(true); }}
                            className="text-xs px-3 py-1 rounded-lg bg-red-50 text-red-500 hover:bg-red-100 transition-colors"
                          >
                            Cancelar
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

      {/* Modal Nueva Cita */}
      {modalNueva && (
  <Modal title="Nueva cita" onClose={() => {
    setModalNueva(false);
    reset();
    setClienteSeleccionado(null);
    setMascotasCliente([]);
    setBusquedaCliente("");
  }}>
    <form onSubmit={handleSubmit(handleAgendar)} className="space-y-4">

      {/* Buscar cliente */}
      <div>
        <label className="block text-sm font-medium text-gray-700 mb-1">
          Buscar cliente
        </label>
        <input
          value={busquedaCliente}
          onChange={(e) => setBusquedaCliente(e.target.value)}
          placeholder="Nombre o apellido del dueño..."
          className={inputClass}
        />
        {/* Lista de clientes filtrados */}
        {busquedaCliente && (
          <div className="mt-1 border border-gray-200 rounded-xl overflow-hidden max-h-40 overflow-y-auto">
            {clientes
              .filter(c =>
                `${c.nombre} ${c.apellido}`.toLowerCase()
                  .includes(busquedaCliente.toLowerCase())
              )
              .map(c => (
                <button
                  key={c.id}
                  type="button"
                  onClick={() => {
                    setClienteSeleccionado(c);
                    setBusquedaCliente(`${c.nombre} ${c.apellido}`);
                    cargarMascotasDeCliente(c.id);
                  }}
                  className="w-full text-left px-4 py-2.5 text-sm hover:bg-purple-50 transition-colors border-b border-gray-50 last:border-0"
                >
                  <div className="font-medium text-gray-900">{c.nombre} {c.apellido}</div>
                  <div className="text-xs text-gray-400">{c.correo}</div>
                </button>
              ))
            }
            {clientes.filter(c =>
              `${c.nombre} ${c.apellido}`.toLowerCase()
                .includes(busquedaCliente.toLowerCase())
            ).length === 0 && (
              <div className="px-4 py-3 text-sm text-gray-400">
                No se encontraron clientes
              </div>
            )}
          </div>
        )}
      </div>

      {/* Mascota */}
      <div>
        <label className="block text-sm font-medium text-gray-700 mb-1">
          Mascota
        </label>
        <select
          {...register("mascotaId")}
          className={inputClass}
          disabled={!clienteSeleccionado}
        >
          <option value="">
            {clienteSeleccionado
              ? "Seleccionar mascota"
              : "Primero selecciona un cliente"}
          </option>
          {mascotasCliente.map((m) => (
            <option key={m.id} value={m.id}>
              {m.nombre} — {m.especie} — {m.temperamento}
            </option>
          ))}
        </select>
        {errors.mascotaId && (
          <p className="text-red-500 text-xs mt-1">{errors.mascotaId.message}</p>
        )}
        {clienteSeleccionado && mascotasCliente.length === 0 && (
          <p className="text-yellow-600 text-xs mt-1">
            Este cliente no tiene mascotas registradas
          </p>
        )}
      </div>

      {/* Groomer */}
      <div>
        <label className="block text-sm font-medium text-gray-700 mb-1">Groomer</label>
        <select {...register("groomerId")} className={inputClass}>
          <option value="">Seleccionar groomer</option>
          {groomers.map((g) => (
            <option key={g.id} value={g.id}>
              {g.usuarioNombre} {g.usuarioApellido}
            </option>
          ))}
        </select>
        {errors.groomerId && (
          <p className="text-red-500 text-xs mt-1">{errors.groomerId.message}</p>
        )}
      </div>

      {/* Servicio */}
      <div>
        <label className="block text-sm font-medium text-gray-700 mb-1">Servicio</label>
        <select {...register("servicioId")} className={inputClass}>
          <option value="">Seleccionar servicio</option>
          {servicios.map((s) => (
            <option key={s.id} value={s.id}>
              {s.nombre} — {s.duracionMin} min — Bs. {s.precioBase}
            </option>
          ))}
        </select>
        {errors.servicioId && (
          <p className="text-red-500 text-xs mt-1">{errors.servicioId.message}</p>
        )}
      </div>

      {/* Fecha y hora */}
      <div>
        <label className="block text-sm font-medium text-gray-700 mb-1">
          Fecha y hora
        </label>
        <input
          {...register("fechaInicio")}
          type="datetime-local"
          className={inputClass}
        />
        {errors.fechaInicio && (
          <p className="text-red-500 text-xs mt-1">{errors.fechaInicio.message}</p>
        )}
      </div>

      {/* Notas */}
      <div>
        <label className="block text-sm font-medium text-gray-700 mb-1">
          Notas <span className="text-gray-400">(opcional)</span>
        </label>
        <textarea
          {...register("notasCliente")}
          rows={2}
          placeholder="Instrucciones especiales..."
          className={inputClass}
        />
      </div>

      <div className="flex gap-3 pt-2">
        <button
          type="button"
          onClick={() => {
            setModalNueva(false);
            reset();
            setClienteSeleccionado(null);
            setMascotasCliente([]);
            setBusquedaCliente("");
          }}
          className="flex-1 py-2.5 rounded-xl border border-gray-200 text-sm text-gray-600 hover:bg-gray-50"
        >
          Cancelar
        </button>
        <button
          type="submit"
          disabled={loadingAccion}
          className="flex-1 py-2.5 rounded-xl bg-gradient-to-r from-purple-500 to-purple-600 text-white text-sm font-medium disabled:opacity-60 flex items-center justify-center gap-2"
        >
          {loadingAccion && <Loader2 className="w-4 h-4 animate-spin" />}
          Agendar
        </button>
      </div>
    </form>
  </Modal>
)}

      {/* Modal Cancelar */}
      {modalCancelar && (
        <Modal title="Cancelar cita" onClose={() => { setModalCancelar(false); setMotivoCancelacion(""); }}>
          <div className="space-y-4">
            <div className="bg-red-50 rounded-xl p-4">
              <p className="text-sm text-red-700">
                ¿Estás seguro que deseas cancelar la cita de <strong>{citaSeleccionada?.mascotaNombre}</strong>?
              </p>
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">
                Motivo de cancelación
              </label>
              <textarea
                value={motivoCancelacion}
                onChange={(e) => setMotivoCancelacion(e.target.value)}
                rows={3}
                placeholder="Describe el motivo..."
                className="w-full px-4 py-2.5 rounded-xl border border-gray-200 focus:outline-none focus:ring-2 focus:ring-red-300 text-sm"
              />
            </div>
            <div className="flex gap-3">
              <button
                onClick={() => { setModalCancelar(false); setMotivoCancelacion(""); }}
                className="flex-1 py-2.5 rounded-xl border border-gray-200 text-sm text-gray-600 hover:bg-gray-50"
              >
                Volver
              </button>
              <button
                onClick={handleCancelar}
                disabled={loadingAccion}
                className="flex-1 py-2.5 rounded-xl bg-red-500 text-white text-sm font-medium hover:bg-red-600 disabled:opacity-60 flex items-center justify-center gap-2"
              >
                {loadingAccion && <Loader2 className="w-4 h-4 animate-spin" />}
                Cancelar cita
              </button>
            </div>
          </div>
        </Modal>
      )}

    </div>
  );
};

export default AgendaPage;
import { useState, useEffect } from "react";
import { Link, useNavigate } from "react-router-dom";
import toast from "react-hot-toast";
import {
  Heart, LogOut, Plus, Calendar,
  PawPrint, X, Loader2, Clock,
  CheckCircle, AlertCircle
} from "lucide-react";
//import { getMisMascotas, crearMascota, getMisCitas } from "../../api/clienteApi";
import useAuthStore from "../../store/authStore";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { z } from "zod";
import { format } from "date-fns";
import { es } from "date-fns/locale";
import {
  getMisMascotas, crearMascota, getMisCitas,
  listarRazas, listarRazasPorEspecie,
  solicitarCita, listarServicios
} from "../../api/clienteApi";
//, listarGroomers => por si necesito en el import
const schema = z.object({
  nombre:       z.string().min(2, "Mínimo 2 caracteres"),
  especie:      z.enum(["perro", "gato", "conejo", "ave", "otro"]),
  razaId:       z.string().optional(),
  sexo:         z.enum(["macho", "hembra", "desconocido"]),
  fechaNac:     z.string().optional(),
  temperamento: z.enum(["tranquilo", "normal", "nervioso", "agresivo", "desconocido"]),
  pesoKg:       z.string().optional(),
  colorPelaje:  z.string().max(100).optional(),
  alergias:     z.string().max(500).optional(),
  restricciones: z.string().max(500).optional(),
});

const estadoColor = {
  pendiente:   "bg-yellow-100 text-yellow-700",
  confirmada:  "bg-blue-100 text-blue-700",
  en_proceso:  "bg-purple-100 text-purple-700",
  completada:  "bg-green-100 text-green-700",
  cancelada:   "bg-red-100 text-red-600",
};

const estadoIcon = {
  pendiente:  <Clock className="w-3.5 h-3.5" />,
  confirmada: <CheckCircle className="w-3.5 h-3.5" />,
  en_proceso: <Loader2 className="w-3.5 h-3.5" />,
  completada: <CheckCircle className="w-3.5 h-3.5" />,
  cancelada:  <AlertCircle className="w-3.5 h-3.5" />,
};

const Modal = ({ title, onClose, children }) => (
  <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/40 backdrop-blur-sm">
    <div className="bg-white rounded-2xl shadow-xl w-full max-w-md max-h-[90vh] overflow-y-auto">
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

const DashboardCliente = () => {
  const [mascotas, setMascotas]       = useState([]);
  const [citas, setCitas]             = useState([]);
  const [loadingMascotas, setLoadingMascotas] = useState(true);
  const [loadingCitas, setLoadingCitas]       = useState(true);
  const [modalMascota, setModalMascota]       = useState(false);
  const [loadingAccion, setLoadingAccion]     = useState(false);
  const { logout, usuario }                   = useAuthStore();
  const navigate                              = useNavigate();
  const [razas, setRazas]               = useState([]);
  const [especieSeleccionada, setEspecieSeleccionada] = useState("perro");
  const [modalCita, setModalCita]       = useState(false);
const [servicios, setServicios]       = useState([]);
//const [groomers, setGroomers]         = useState([]);
const [loadingCita, setLoadingCita] = useState(false);

const schemasCita = z.object({
  mascotaId:    z.string().min(1, "Selecciona una mascota"),
  servicioId:   z.string().min(1, "Selecciona un servicio"),
  groomerId:    z.string().optional(),
  fechaInicio:  z.string().min(1, "Selecciona fecha y hora"),
  notasCliente: z.string().optional(),
});

const formCita = useForm({ resolver: zodResolver(schemasCita) });

  const { register, handleSubmit, reset, formState: { errors } } = useForm({
    resolver: zodResolver(schema),
    defaultValues: { especie: "perro", sexo: "desconocido", temperamento: "desconocido" }
  });

  const cargarMascotas = async () => {
    try {
      const res = await getMisMascotas();
      setMascotas(res.data);
    } catch {
      toast.error("Error al cargar mascotas");
    } finally {
      setLoadingMascotas(false);
    }
  };

  const cargarRazas = async (especie) => {
  try {
    const res = await listarRazasPorEspecie(especie);
    setRazas(res.data);
  } catch {
    toast.error("Error al cargar razas");
  }
};

  const cargarCitas = async () => {
    try {
      const res = await getMisCitas();
      setCitas(res.data);
    } catch {
      toast.error("Error al cargar citas");
    } finally {
      setLoadingCitas(false);
    }
  };

  const cargarServicios = async () => {
  try {
    const res = await listarServicios();
    setServicios(res.data);
  } catch {
    toast.error("Error al cargar servicios");
  }
};

//const cargarGroomers = async () => {
//  try {
//    const res = await listarGroomers();
//    setGroomers(res.data);
//  } catch {
//    toast.error("Error al cargar groomers");
//  }
//};

const handleSolicitarCita = async (data) => {
  setLoadingCita(true);
  try {
    await solicitarCita({
      mascotaId:    data.mascotaId,
      servicioId:   data.servicioId,
      fechaInicio:  new Date(data.fechaInicio).toISOString(),
      notasCliente: data.notasCliente,
      estado:       "pendiente",
    });
    toast.success("¡Cita solicitada! Recepción la confirmará pronto.");
    setModalCita(false);
    formCita.reset();
    cargarCitas();
  } catch (err) {
    toast.error(err.response?.data?.message || "Error al solicitar cita");
  } finally {
    setLoadingCita(false);
  }
};

  useEffect(() => {
  cargarMascotas();
  cargarCitas();
  cargarRazas("perro");
  cargarServicios();
  //cargarGroomers();
}, []);

  const handleLogout = () => {
    logout();
    navigate("/");
  };

  const handleCrearMascota = async (data) => {
  setLoadingAccion(true);
  try {
    const payload = {
      ...data,
      pesoKg:  data.pesoKg  ? parseFloat(data.pesoKg) : null,
      razaId:  data.razaId  || null,
      fechaNac: data.fechaNac || null,
    };
    await crearMascota(payload);
    toast.success("¡Mascota registrada!");
    setModalMascota(false);
    reset();
    cargarMascotas();
  } catch (err) {
    toast.error(err.response?.data?.message || "Error al registrar mascota");
  } finally {
    setLoadingAccion(false);
  }
};

  const inputClass = "w-full px-4 py-2.5 rounded-xl border border-gray-200 focus:outline-none focus:ring-2 focus:ring-purple-300 text-sm";
  const selectClass = inputClass;

  const especieEmoji = {
    perro: "🐶", gato: "🐱", conejo: "🐰", ave: "🦜", otro: "🐾"
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
          </div>
          <div className="flex items-center gap-3">
            <span className="text-sm text-gray-600">Hola, <strong>{usuario?.nombre}</strong> 🐾</span>
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

      <div className="max-w-6xl mx-auto px-6 py-8">

        {/* Bienvenida */}
        {/* Bienvenida */}
<div className="bg-gradient-to-r from-purple-500 to-green-500 rounded-2xl p-6 mb-8 text-white">
  <div className="flex items-center justify-between">
    <div>
      <h1 className="text-2xl font-bold mb-1">
        Bienvenido, {usuario?.nombre} 🐾
      </h1>
      <p className="text-purple-100 text-sm">
        Gestiona tus mascotas y citas desde aquí.
      </p>
    </div>
    <button
      onClick={() => setModalCita(true)}
      className="flex items-center gap-2 px-4 py-2.5 rounded-xl bg-white/20 hover:bg-white/30 text-white text-sm font-medium transition-all"
    >
      <Plus className="w-4 h-4" />
      Agendar cita
    </button>
  </div>
</div>

        <div className="grid md:grid-cols-2 gap-8">

          {/* Mis Mascotas */}
          <div className="bg-white rounded-2xl border border-gray-100">
            <div className="flex items-center justify-between p-6 border-b border-gray-100">
              <div className="flex items-center gap-2">
                <span className="text-xl">🐾</span>
                <h2 className="font-semibold text-gray-900">Mis mascotas</h2>
              </div>
              <button
                onClick={() => setModalMascota(true)}
                className="flex items-center gap-1.5 px-3 py-2 rounded-xl bg-gradient-to-r from-purple-500 to-purple-600 text-white text-sm font-medium hover:from-purple-600 hover:to-purple-700 transition-all"
              >
                <Plus className="w-4 h-4" />
                Agregar
              </button>
            </div>

            {loadingMascotas ? (
              <div className="flex items-center justify-center py-12">
                <Loader2 className="w-6 h-6 text-purple-500 animate-spin" />
              </div>
            ) : mascotas.length === 0 ? (
              <div className="text-center py-12">
                <span className="text-4xl">🐾</span>
                <p className="text-gray-400 text-sm mt-2">No tienes mascotas registradas</p>
                <button
                  onClick={() => setModalMascota(true)}
                  className="mt-3 text-sm text-purple-600 hover:underline"
                >
                  Registra tu primera mascota
                </button>
              </div>
            ) : (
              <div className="divide-y divide-gray-50">
                {mascotas.map((m) => (
                  <div key={m.id} className="flex items-center gap-4 p-4 hover:bg-gray-50 transition-colors">
                    <div className="w-12 h-12 rounded-xl bg-purple-50 flex items-center justify-center text-2xl">
                      {especieEmoji[m.especie] || "🐾"}
                    </div>
                    <div className="flex-1">
                      <div className="font-medium text-gray-900">{m.nombre}</div>
                      <div className="text-xs text-gray-400 capitalize">
                        {m.especie} • {m.sexo} • {m.temperamento}
                      </div>
                    </div>
                    {m.pesoKg && (
                      <span className="text-xs text-gray-400">{m.pesoKg} kg</span>
                    )}
                  </div>
                ))}
              </div>
            )}
          </div>

          {/* Mis Citas */}
          <div className="bg-white rounded-2xl border border-gray-100">
            <div className="flex items-center justify-between p-6 border-b border-gray-100">
              <div className="flex items-center gap-2">
                <Calendar className="w-5 h-5 text-purple-500" />
                <h2 className="font-semibold text-gray-900">Mis citas</h2>
              </div>
            </div>

            {loadingCitas ? (
              <div className="flex items-center justify-center py-12">
                <Loader2 className="w-6 h-6 text-purple-500 animate-spin" />
              </div>
            ) : citas.length === 0 ? (
              <div className="text-center py-12">
                <Calendar className="w-10 h-10 text-gray-200 mx-auto mb-2" />
                <p className="text-gray-400 text-sm">No tienes citas agendadas</p>
              </div>
            ) : (
              <div className="divide-y divide-gray-50">
                {citas.map((c) => (
                  <div key={c.id} className="p-4 hover:bg-gray-50 transition-colors">
                    <div className="flex items-start justify-between gap-2">
                      <div>
                        <div className="font-medium text-gray-900 text-sm">
                          {c.mascotaNombre} — {c.servicioNombre}
                        </div>
                        <div className="text-xs text-gray-400 mt-0.5">
                          {c.fechaInicio && format(
                            new Date(c.fechaInicio),
                            "dd MMM yyyy, HH:mm",
                            { locale: es }
                          )}
                        </div>
                        <div className="text-xs text-gray-400">
                          Groomer: {c.groomerNombre} {c.groomerApellido}
                        </div>
                      </div>
                      <span className={`inline-flex items-center gap-1 px-2 py-1 rounded-full text-xs font-medium ${estadoColor[c.estado]}`}>
                        {estadoIcon[c.estado]}
                        {c.estado}
                      </span>
                    </div>
                  </div>
                ))}
              </div>
            )}
          </div>

        </div>
      </div>

      {/* Modal Nueva Mascota */}
      {modalMascota && (
        <Modal title="Registrar mascota" onClose={() => { setModalMascota(false); reset(); }}>
          <form onSubmit={handleSubmit(handleCrearMascota)} className="space-y-4">

  {/* Nombre */}
  <div>
    <label className="block text-sm font-medium text-gray-700 mb-1">Nombre</label>
    <input
      {...register("nombre")}
      placeholder="Nombre de tu mascota"
      className={inputClass}
    />
    {errors.nombre && <p className="text-red-500 text-xs mt-1">{errors.nombre.message}</p>}
  </div>

  {/* Especie y Sexo */}
  <div className="grid grid-cols-2 gap-3">
    <div>
      <label className="block text-sm font-medium text-gray-700 mb-1">Especie</label>
      <select
        {...register("especie")}
        onChange={(e) => {
          setEspecieSeleccionada(e.target.value);
          cargarRazas(e.target.value);
        }}
        className={selectClass}
      >
        <option value="perro">🐶 Perro</option>
        <option value="gato">🐱 Gato</option>
        <option value="conejo">🐰 Conejo</option>
        <option value="ave">🦜 Ave</option>
        <option value="otro">🐾 Otro</option>
      </select>
    </div>
    <div>
      <label className="block text-sm font-medium text-gray-700 mb-1">Sexo</label>
      <select {...register("sexo")} className={selectClass}>
        <option value="macho">Macho</option>
        <option value="hembra">Hembra</option>
        <option value="desconocido">Desconocido</option>
      </select>
    </div>
  </div>

  {/* Raza */}
  <div>
    <label className="block text-sm font-medium text-gray-700 mb-1">
      Raza <span className="text-gray-400">(opcional)</span>
    </label>
    <select {...register("razaId")} className={selectClass}>
      <option value="">Seleccionar raza</option>
      {razas.map((r) => (
        <option key={r.id} value={r.id}>
          {r.nombre} — {r.tamanio}
        </option>
      ))}
    </select>
  </div>

  {/* Fecha nacimiento y Peso */}
  <div className="grid grid-cols-2 gap-3">
    <div>
      <label className="block text-sm font-medium text-gray-700 mb-1">
        Fecha de nacimiento <span className="text-gray-400">(opcional)</span>
      </label>
      <input
        {...register("fechaNac")}
        type="date"
        max={new Date().toISOString().split("T")[0]}
        className={inputClass}
      />
    </div>
    <div>
      <label className="block text-sm font-medium text-gray-700 mb-1">
        Peso (kg) <span className="text-gray-400">(opcional)</span>
      </label>
      <input
        {...register("pesoKg")}
        type="number"
        step="0.01"
        placeholder="0.00"
        className={inputClass}
      />
    </div>
  </div>

  {/* Temperamento y Color */}
  <div className="grid grid-cols-2 gap-3">
    <div>
      <label className="block text-sm font-medium text-gray-700 mb-1">Temperamento</label>
      <select {...register("temperamento")} className={selectClass}>
        <option value="tranquilo">Tranquilo</option>
        <option value="normal">Normal</option>
        <option value="nervioso">Nervioso</option>
        <option value="agresivo">Agresivo</option>
        <option value="desconocido">Desconocido</option>
      </select>
    </div>
    <div>
      <label className="block text-sm font-medium text-gray-700 mb-1">
        Color de pelaje <span className="text-gray-400">(opcional)</span>
      </label>
      <input
        {...register("colorPelaje")}
        placeholder="Ej: negro, blanco"
        className={inputClass}
      />
    </div>
  </div>

  {/* Alergias */}
  <div>
    <label className="block text-sm font-medium text-gray-700 mb-1">
      Alergias <span className="text-gray-400">(opcional)</span>
    </label>
    <textarea
      {...register("alergias")}
      rows={2}
      placeholder="Describe las alergias si las tiene..."
      className={inputClass}
    />
  </div>

  {/* Restricciones */}
  <div>
    <label className="block text-sm font-medium text-gray-700 mb-1">
      Restricciones <span className="text-gray-400">(opcional)</span>
    </label>
    <textarea
      {...register("restricciones")}
      rows={2}
      placeholder="Cuidados especiales o restricciones..."
      className={inputClass}
    />
  </div>

  <div className="flex gap-3 pt-2">
    <button
      type="button"
      onClick={() => { setModalMascota(false); reset(); }}
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
      Registrar
    </button>
  </div>
</form>
        </Modal>
      )}

      {/* Modal Nueva Cita */}
{modalCita && (
  <Modal title="Solicitar cita" onClose={() => { setModalCita(false); formCita.reset(); }}>
    <form onSubmit={formCita.handleSubmit(handleSolicitarCita)} className="space-y-4">

      {/* Mascota */}
      <div>
        <label className="block text-sm font-medium text-gray-700 mb-1">
          Tu mascota
        </label>
        <select {...formCita.register("mascotaId")} className={inputClass}>
          <option value="">Seleccionar mascota</option>
          {mascotas.map((m) => (
            <option key={m.id} value={m.id}>
              {m.nombre} — {m.especie}
            </option>
          ))}
        </select>
        {formCita.formState.errors.mascotaId && (
          <p className="text-red-500 text-xs mt-1">
            {formCita.formState.errors.mascotaId.message}
          </p>
        )}
        {mascotas.length === 0 && (
          <p className="text-yellow-600 text-xs mt-1">
            Primero registra una mascota
          </p>
        )}
      </div>

      {/* Servicio */}
      <div>
        <label className="block text-sm font-medium text-gray-700 mb-1">
          Servicio
        </label>
        <select {...formCita.register("servicioId")} className={inputClass}>
          <option value="">Seleccionar servicio</option>
          {servicios.map((s) => (
            <option key={s.id} value={s.id}>
              {s.nombre} — {s.duracionMin} min — Bs. {s.precioBase}
            </option>
          ))}
        </select>
        {formCita.formState.errors.servicioId && (
          <p className="text-red-500 text-xs mt-1">
            {formCita.formState.errors.servicioId.message}
          </p>
        )}
      </div>

    {/*  groomer no necesario
      <div> 
        <label className="block text-sm font-medium text-gray-700 mb-1">
          Groomer preferido <span className="text-gray-400">(opcional)</span>
        </label>
        <select {...formCita.register("groomerId")} className={inputClass}>
          <option value="">Sin preferencia</option>
          {groomers.map((g) => (
            <option key={g.id} value={g.id}>
              {g.usuarioNombre} {g.usuarioApellido}
            </option>
          ))}
        </select>
        <p className="text-xs text-gray-400 mt-1">
          Si no eliges, recepción asignará uno disponible
        </p>
      </div> */}

      {/* Fecha y hora */}
      <div>
        <label className="block text-sm font-medium text-gray-700 mb-1">
          Fecha y hora preferida
        </label>
        <input
          {...formCita.register("fechaInicio")}
          type="datetime-local"
          min={new Date().toISOString().slice(0, 16)}
          className={inputClass}
        />
        {formCita.formState.errors.fechaInicio && (
          <p className="text-red-500 text-xs mt-1">
            {formCita.formState.errors.fechaInicio.message}
          </p>
        )}
      </div>

      {/* Notas */}
      <div>
        <label className="block text-sm font-medium text-gray-700 mb-1">
          Notas o instrucciones <span className="text-gray-400">(opcional)</span>
        </label>
        <textarea
          {...formCita.register("notasCliente")}
          rows={2}
          placeholder="Instrucciones especiales para el groomer..."
          className={inputClass}
        />
      </div>

      {/* Info */}
      <div className="bg-blue-50 border border-blue-100 rounded-xl p-3">
        <p className="text-xs text-blue-600">
          ℹ️ Tu cita quedará en estado <strong>pendiente</strong> hasta que recepción la confirme. Recibirás un correo de confirmación.
        </p>
      </div>

      <div className="flex gap-3 pt-2">
        <button
          type="button"
          onClick={() => { setModalCita(false); formCita.reset(); }}
          className="flex-1 py-2.5 rounded-xl border border-gray-200 text-sm text-gray-600 hover:bg-gray-50"
        >
          Cancelar
        </button>
        <button
          type="submit"
          disabled={loadingCita || mascotas.length === 0}
          className="flex-1 py-2.5 rounded-xl bg-gradient-to-r from-purple-500 to-purple-600 text-white text-sm font-medium disabled:opacity-60 flex items-center justify-center gap-2"
        >
          {loadingCita && <Loader2 className="w-4 h-4 animate-spin" />}
          Solicitar cita
        </button>
      </div>
    </form>
  </Modal>
)}

    </div>
  );
};

export default DashboardCliente;
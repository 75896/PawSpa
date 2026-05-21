import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import toast from "react-hot-toast";
import {
  Heart, LogOut, Calendar, Clock,
  CheckCircle, AlertCircle, Loader2,
  Scissors, ChevronRight
} from "lucide-react";
import { getMisCitasHoy, getMisCitas } from "../../api/groomerApi";
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

const estadoIcon = {
  pendiente:  <Clock className="w-3.5 h-3.5" />,
  confirmada: <CheckCircle className="w-3.5 h-3.5" />,
  en_proceso: <Loader2 className="w-3.5 h-3.5 animate-spin" />,
  completada: <CheckCircle className="w-3.5 h-3.5" />,
  cancelada:  <AlertCircle className="w-3.5 h-3.5" />,
};

const temperamentoColor = {
  tranquilo:   "bg-green-100 text-green-700",
  normal:      "bg-blue-100 text-blue-700",
  nervioso:    "bg-yellow-100 text-yellow-700",
  agresivo:    "bg-red-100 text-red-600",
  desconocido: "bg-gray-100 text-gray-600",
};

const DashboardGroomer = () => {
  const [citasHoy, setCitasHoy]   = useState([]);
  const [todasCitas, setTodasCitas] = useState([]);
  const [loadingHoy, setLoadingHoy] = useState(true);
  const [loadingTodas, setLoadingTodas] = useState(true);
  const [vista, setVista]         = useState("hoy");
  const { logout, usuario }       = useAuthStore();
  const navigate                  = useNavigate();

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

  useEffect(() => {
    cargarCitasHoy();
    cargarTodasCitas();
  }, []);

  const handleLogout = () => {
    logout();
    navigate("/");
  };

  const citas = vista === "hoy" ? citasHoy : todasCitas;
  const loading = vista === "hoy" ? loadingHoy : loadingTodas;

  const stats = {
    hoy:       citasHoy.length,
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
            <span className="text-sm text-gray-600">
              Hola, <strong>{usuario?.nombre}</strong> ✂️
            </span>
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
        <div className="bg-gradient-to-r from-purple-500 to-green-500 rounded-2xl p-6 mb-8 text-white">
          <div className="flex items-center gap-3 mb-1">
            <Scissors className="w-6 h-6" />
            <h1 className="text-2xl font-bold">
              Hola, {usuario?.nombre} ✂️
            </h1>
          </div>
          <p className="text-purple-100 text-sm">
            {format(new Date(), "EEEE dd 'de' MMMM yyyy", { locale: es })}
          </p>
        </div>

        {/* Stats */}
        <div className="grid grid-cols-2 md:grid-cols-4 gap-4 mb-8">
          {[
            { label: "Citas hoy",    valor: stats.hoy,         color: "text-purple-600" },
            { label: "Pendientes",   valor: stats.pendientes,  color: "text-yellow-600" },
            { label: "En proceso",   valor: stats.enProceso,   color: "text-blue-600"   },
            { label: "Completadas",  valor: stats.completadas, color: "text-green-600"  },
          ].map((s, i) => (
            <div key={i} className="bg-white rounded-2xl border border-gray-100 p-5">
              <div className={`text-2xl font-bold ${s.color}`}>{s.valor}</div>
              <div className="text-sm text-gray-500 mt-1">{s.label}</div>
            </div>
          ))}
        </div>

        {/* Tabs */}
        <div className="bg-white rounded-2xl border border-gray-100">
          <div className="flex items-center gap-2 p-4 border-b border-gray-100">
            <button
              onClick={() => setVista("hoy")}
              className={`px-4 py-2 rounded-xl text-sm font-medium transition-all ${
                vista === "hoy"
                  ? "bg-purple-100 text-purple-700"
                  : "text-gray-500 hover:bg-gray-50"
              }`}
            >
              Hoy ({stats.hoy})
            </button>
            <button
              onClick={() => setVista("todas")}
              className={`px-4 py-2 rounded-xl text-sm font-medium transition-all ${
                vista === "todas"
                  ? "bg-purple-100 text-purple-700"
                  : "text-gray-500 hover:bg-gray-50"
              }`}
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
              <p className="text-gray-400">
                {vista === "hoy" ? "No tienes citas para hoy" : "No tienes citas registradas"}
              </p>
            </div>
          ) : (
            <div className="divide-y divide-gray-50">
              {citas.map((c) => (
                <div key={c.id} className="p-5 hover:bg-gray-50 transition-colors">
                  <div className="flex items-start justify-between gap-4">
                    <div className="flex items-start gap-4">

                      {/* Hora */}
                      <div className="text-center min-w-[50px]">
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
                          <span className="text-gray-400 font-normal"> — {c.servicioNombre}</span>
                        </div>
                        <div className="text-xs text-gray-400 mt-0.5">
                          {c.mascotaEspecie} •{" "}
                          <span className={`inline-flex items-center px-1.5 py-0.5 rounded-full text-xs ${temperamentoColor[c.mascotaTemperamento]}`}>
                            {c.mascotaTemperamento}
                          </span>
                        </div>
                        {vista === "todas" && (
                          <div className="text-xs text-gray-400 mt-0.5">
                            {c.fechaInicio && format(
                              new Date(c.fechaInicio),
                              "dd MMM yyyy",
                              { locale: es }
                            )}
                          </div>
                        )}
                      </div>
                    </div>

                    {/* Estado */}
                    <span className={`inline-flex items-center gap-1 px-2.5 py-1 rounded-full text-xs font-medium ${estadoColor[c.estado]}`}>
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
  );
};

export default DashboardGroomer;
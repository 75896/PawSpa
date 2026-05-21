import { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import toast from "react-hot-toast";
import {
  Heart, Shield, Search, Loader2,
  CheckCircle, XCircle, AlertCircle,
  LogIn, UserPlus, Key, ArrowLeft,
  Filter
} from "lucide-react";
import {
  listarAuditoria, auditoriaPorAccion, auditoriaPorResultado
} from "../../api/auditoriaApi";
import { format } from "date-fns";
import { es } from "date-fns/locale";

const accionIcon = {
  LOGIN:           <LogIn    className="w-4 h-4" />,
  REGISTRO:        <UserPlus className="w-4 h-4" />,
  CAMBIO_PASSWORD: <Key      className="w-4 h-4" />,
};

const accionColor = {
  LOGIN:           "bg-blue-100 text-blue-700",
  REGISTRO:        "bg-purple-100 text-purple-700",
  CAMBIO_PASSWORD: "bg-orange-100 text-orange-700",
};

const resultadoColor = {
  EXITOSO:  "bg-green-100 text-green-700",
  FALLIDO:  "bg-red-100 text-red-600",
  BLOQUEADO: "bg-yellow-100 text-yellow-700",
};

const resultadoIcon = {
  EXITOSO:   <CheckCircle  className="w-3.5 h-3.5" />,
  FALLIDO:   <XCircle      className="w-3.5 h-3.5" />,
  BLOQUEADO: <AlertCircle  className="w-3.5 h-3.5" />,
};

const AuditoriaPage = () => {
  const [registros, setRegistros]     = useState([]);
  const [loading, setLoading]         = useState(true);
  const [busqueda, setBusqueda]       = useState("");
  const [filtroAccion, setFiltroAccion]     = useState("TODOS");
  const [filtroResultado, setFiltroResultado] = useState("TODOS");

  const cargarTodos = async () => {
    setLoading(true);
    try {
      const res = await listarAuditoria();
      setRegistros(res.data);
    } catch {
      toast.error("Error al cargar auditoría");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => { cargarTodos(); }, []);

  const handleFiltroAccion = async (accion) => {
    setFiltroAccion(accion);
    setLoading(true);
    try {
      if (accion === "TODOS") {
        const res = await listarAuditoria();
        setRegistros(res.data);
      } else {
        const res = await auditoriaPorAccion(accion);
        setRegistros(res.data);
      }
    } catch {
      toast.error("Error al filtrar");
    } finally {
      setLoading(false);
    }
  };

  const handleFiltroResultado = async (resultado) => {
    setFiltroResultado(resultado);
    setLoading(true);
    try {
      if (resultado === "TODOS") {
        const res = await listarAuditoria();
        setRegistros(res.data);
      } else {
        const res = await auditoriaPorResultado(resultado);
        setRegistros(res.data);
      }
    } catch {
      toast.error("Error al filtrar");
    } finally {
      setLoading(false);
    }
  };

  const registrosFiltrados = registros.filter((r) =>
    `${r.correo || ""} ${r.accion} ${r.resultado} ${r.ip || ""} ${r.detalle || ""}`
      .toLowerCase()
      .includes(busqueda.toLowerCase())
  );

  const stats = {
    total:     registros.length,
    exitosos:  registros.filter(r => r.resultado === "EXITOSO").length,
    fallidos:  registros.filter(r => r.resultado === "FALLIDO").length,
    bloqueados: registros.filter(r => r.resultado === "BLOQUEADO").length,
  };

  return (
    <div className="min-h-screen bg-gray-50">

      {/* Navbar */}
      <nav className="bg-white border-b border-gray-100 px-6 py-4">
        <div className="max-w-7xl mx-auto flex items-center justify-between">
          <div className="flex items-center gap-3">
            <Link to="/admin" className="text-gray-400 hover:text-gray-600 transition-colors">
              <ArrowLeft className="w-5 h-5" />
            </Link>
            <div className="w-8 h-8 rounded-lg bg-gradient-to-br from-purple-500 to-green-500 flex items-center justify-center">
              <Heart className="w-4 h-4 text-white" />
            </div>
            <span className="font-semibold text-gray-900">
              Paw<span className="bg-gradient-to-r from-purple-600 to-green-500 bg-clip-text text-transparent">Spa</span>
            </span>
            <span className="ml-2 text-sm text-gray-400">Panel Auditoría</span>
          </div>
          <div className="flex items-center gap-2">
            <Shield className="w-4 h-4 text-purple-500" />
            <span className="text-sm font-medium text-gray-700">Registro de accesos</span>
          </div>
        </div>
      </nav>

      <div className="max-w-7xl mx-auto px-6 py-8">

        {/* Stats */}
        <div className="grid grid-cols-2 md:grid-cols-4 gap-4 mb-8">
          {[
            { label: "Total eventos",  valor: stats.total,      color: "text-purple-600" },
            { label: "Exitosos",       valor: stats.exitosos,   color: "text-green-600"  },
            { label: "Fallidos",       valor: stats.fallidos,   color: "text-red-600"    },
            { label: "Bloqueados",     valor: stats.bloqueados, color: "text-yellow-600" },
          ].map((s, i) => (
            <div key={i} className="bg-white rounded-2xl border border-gray-100 p-5">
              <div className={`text-2xl font-bold ${s.color}`}>{s.valor}</div>
              <div className="text-sm text-gray-500 mt-1">{s.label}</div>
            </div>
          ))}
        </div>

        <div className="bg-white rounded-2xl border border-gray-100">

          {/* Filtros */}
          <div className="p-6 border-b border-gray-100">
            <div className="flex flex-col md:flex-row gap-4">

              {/* Busqueda */}
              <div className="relative flex-1">
                <Search className="w-4 h-4 text-gray-400 absolute left-3 top-1/2 -translate-y-1/2" />
                <input
                  value={busqueda}
                  onChange={(e) => setBusqueda(e.target.value)}
                  placeholder="Buscar por correo, IP, detalle..."
                  className="w-full pl-9 pr-4 py-2 rounded-xl border border-gray-200 text-sm focus:outline-none focus:ring-2 focus:ring-purple-300"
                />
              </div>

              {/* Filtro accion */}
              <div className="flex items-center gap-2">
                <Filter className="w-4 h-4 text-gray-400" />
                <select
                  value={filtroAccion}
                  onChange={(e) => handleFiltroAccion(e.target.value)}
                  className="px-3 py-2 rounded-xl border border-gray-200 text-sm focus:outline-none focus:ring-2 focus:ring-purple-300"
                >
                  <option value="TODOS">Todas las acciones</option>
                  <option value="LOGIN">Login</option>
                  <option value="REGISTRO">Registro</option>
                  <option value="CAMBIO_PASSWORD">Cambio contraseña</option>
                </select>
              </div>

              {/* Filtro resultado */}
              <div>
                <select
                  value={filtroResultado}
                  onChange={(e) => handleFiltroResultado(e.target.value)}
                  className="px-3 py-2 rounded-xl border border-gray-200 text-sm focus:outline-none focus:ring-2 focus:ring-purple-300"
                >
                  <option value="TODOS">Todos los resultados</option>
                  <option value="EXITOSO">Exitoso</option>
                  <option value="FALLIDO">Fallido</option>
                  <option value="BLOQUEADO">Bloqueado</option>
                </select>
              </div>

              {/* Refrescar */}
              <button
                onClick={cargarTodos}
                className="px-4 py-2 rounded-xl bg-purple-50 text-purple-600 text-sm font-medium hover:bg-purple-100 transition-colors"
              >
                Refrescar
              </button>
            </div>
          </div>

          {/* Tabla */}
          {loading ? (
            <div className="flex items-center justify-center py-16">
              <Loader2 className="w-8 h-8 text-purple-500 animate-spin" />
            </div>
          ) : registrosFiltrados.length === 0 ? (
            <div className="text-center py-16">
              <Shield className="w-12 h-12 text-gray-200 mx-auto mb-3" />
              <p className="text-gray-400 text-sm">No hay registros de auditoría</p>
            </div>
          ) : (
            <div className="overflow-x-auto">
              <table className="w-full">
                <thead>
                  <tr className="text-left text-xs font-medium text-gray-500 uppercase tracking-wider border-b border-gray-100">
                    <th className="px-6 py-3">Fecha y hora</th>
                    <th className="px-6 py-3">Correo</th>
                    <th className="px-6 py-3">Acción</th>
                    <th className="px-6 py-3">Resultado</th>
                    <th className="px-6 py-3">IP</th>
                    <th className="px-6 py-3">Detalle</th>
                  </tr>
                </thead>
                <tbody className="divide-y divide-gray-50">
                  {registrosFiltrados.map((r) => (
                    <tr key={r.id} className="hover:bg-gray-50 transition-colors">
                      <td className="px-6 py-4 text-xs text-gray-500 whitespace-nowrap">
                        {r.creadoEn && format(
                          new Date(r.creadoEn),
                          "dd/MM/yyyy HH:mm:ss",
                          { locale: es }
                        )}
                      </td>
                      <td className="px-6 py-4 text-sm text-gray-700">
                        {r.correo || <span className="text-gray-400">—</span>}
                      </td>
                      <td className="px-6 py-4">
                        <span className={`inline-flex items-center gap-1.5 px-2.5 py-1 rounded-full text-xs font-medium ${accionColor[r.accion] || "bg-gray-100 text-gray-600"}`}>
                          {accionIcon[r.accion]}
                          {r.accion}
                        </span>
                      </td>
                      <td className="px-6 py-4">
                        <span className={`inline-flex items-center gap-1 px-2.5 py-1 rounded-full text-xs font-medium ${resultadoColor[r.resultado] || "bg-gray-100 text-gray-600"}`}>
                          {resultadoIcon[r.resultado]}
                          {r.resultado}
                        </span>
                      </td>
                      <td className="px-6 py-4 text-xs text-gray-500 font-mono">
                        {r.ip || "—"}
                      </td>
                      <td className="px-6 py-4 text-xs text-gray-500 max-w-xs truncate">
                        {r.detalle || "—"}
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default AuditoriaPage;
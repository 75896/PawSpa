import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import toast from "react-hot-toast";
import {
  Users, Plus, Pencil, PowerOff, Power,
  LogOut, Heart, Search, X, Loader2,
  ShieldCheck, Scissors, PhoneCall, User
} from "lucide-react";
import {
  listarUsuarios, crearUsuario,
  actualizarUsuario, desactivarUsuario, activarUsuario
} from "../../api/usuariosApi";
import useAuthStore from "../../store/authStore";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { z } from "zod";
import { Shield } from "lucide-react";


const schemaCrear = z.object({
  nombre:   z.string().min(2, "Mínimo 2 caracteres"),
  apellido: z.string().min(2, "Mínimo 2 caracteres"),
  correo:   z.string().email("Correo inválido"),
  telefono: z.string().max(20).optional(),
  rol:      z.enum(["admin", "recepcion", "groomer", "cliente"]),
  // Password solo requerida para admin y cliente
  passwordHash: z.string().optional(),
});

const schemaEditar = z.object({
  nombre:      z.string().min(2, "Mínimo 2 caracteres"),
  apellido:    z.string().min(2, "Mínimo 2 caracteres"),
  telefono:    z.string().max(20).optional(),
  rol:         z.enum(["admin", "recepcion", "groomer", "cliente"]),
  passwordHash: z.string().optional(),
});

const rolIcon = {
  admin:     <ShieldCheck className="w-4 h-4" />,
  recepcion: <PhoneCall   className="w-4 h-4" />,
  groomer:   <Scissors    className="w-4 h-4" />,
  cliente:   <User        className="w-4 h-4" />,
};

const rolColor = {
  admin:     "bg-purple-100 text-purple-700",
  recepcion: "bg-blue-100 text-blue-700",
  groomer:   "bg-green-100 text-green-700",
  cliente:   "bg-gray-100 text-gray-700",
};

const Modal = ({ title, onClose, children }) => (
  <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/40 backdrop-blur-sm">
    <div className="bg-white rounded-2xl shadow-xl w-full max-w-md">
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

const DashboardAdmin = () => {
  const [usuarios, setUsuarios]     = useState([]);
  const [loading, setLoading]       = useState(true);
  const [busqueda, setBusqueda]     = useState("");
  const [modalCrear, setModalCrear] = useState(false);
  const [modalEditar, setModalEditar] = useState(false);
  const [usuarioEditar, setUsuarioEditar] = useState(null);
  const [loadingAccion, setLoadingAccion] = useState(false);
  const { logout, usuario } = useAuthStore();
  const navigate = useNavigate();

  const formCrear = useForm({ resolver: zodResolver(schemaCrear) });
  const formEditar = useForm({ resolver: zodResolver(schemaEditar) });

  const cargarUsuarios = async () => {
    try {
      const res = await listarUsuarios();
      setUsuarios(res.data);
    } catch {
      toast.error("Error al cargar usuarios");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => { cargarUsuarios(); }, []);

  const handleLogout = () => {
    logout();
    navigate("/");
  };

  const handleCrear = async (data) => {
    setLoadingAccion(true);
    try {
      await crearUsuario(data);
      toast.success("Usuario creado correctamente");
      setModalCrear(false);
      formCrear.reset();
      cargarUsuarios();
    } catch (err) {
      toast.error(err.response?.data?.message || "Error al crear usuario");
    } finally {
      setLoadingAccion(false);
    }
  };

  const abrirEditar = (u) => {
    setUsuarioEditar(u);
    formEditar.reset({
      nombre:   u.nombre,
      apellido: u.apellido,
      telefono: u.telefono || "",
      rol:      u.rol,
    });
    setModalEditar(true);
  };

  const handleEditar = async (data) => {
    setLoadingAccion(true);
    try {
      await actualizarUsuario(usuarioEditar.id, data);
      toast.success("Usuario actualizado");
      setModalEditar(false);
      cargarUsuarios();
    } catch (err) {
      toast.error(err.response?.data?.message || "Error al actualizar");
    } finally {
      setLoadingAccion(false);
    }
  };

  const handleToggleActivo = async (u) => {
    try {
      if (u.activo) {
        await desactivarUsuario(u.id);
        toast.success("Usuario desactivado");
      } else {
        await activarUsuario(u.id);
        toast.success("Usuario activado");
      }
      cargarUsuarios();
    } catch {
      toast.error("Error al cambiar estado");
    }
  };

  const usuariosFiltrados = usuarios.filter((u) =>
    `${u.nombre} ${u.apellido} ${u.correo} ${u.rol}`
      .toLowerCase()
      .includes(busqueda.toLowerCase())
  );

  const stats = {
    total:     usuarios.length,
    activos:   usuarios.filter((u) => u.activo).length,
    groomers:  usuarios.filter((u) => u.rol === "groomer").length,
    clientes:  usuarios.filter((u) => u.rol === "cliente").length,
  };

  const FormField = ({ label, error, children }) => (
    <div>
      <label className="block text-sm font-medium text-gray-700 mb-1">{label}</label>
      {children}
      {error && <p className="text-red-500 text-xs mt-1">{error}</p>}
    </div>
  );

  const inputClass = "w-full px-4 py-2.5 rounded-xl border border-gray-200 focus:outline-none focus:ring-2 focus:ring-purple-300 text-sm";

  return (
    <div className="min-h-screen bg-gray-50">

      {/* Navbar */}
      <nav className="bg-white border-b border-gray-100 px-6 py-4">
        <div className="max-w-7xl mx-auto flex items-center justify-between">
          <div className="flex items-center gap-2">
            <div className="w-8 h-8 rounded-lg bg-gradient-to-br from-purple-500 to-green-500 flex items-center justify-center">
              <Heart className="w-4 h-4 text-white" />
            </div>

            
<button
  onClick={() => navigate("/admin/auditoria")}
  className="flex items-center gap-2 text-sm text-gray-600 hover:text-purple-600 px-3 py-2 rounded-lg hover:bg-purple-50 transition-colors"
>
  <Shield className="w-4 h-4" />
  Auditoría
</button>

            <span className="font-semibold text-gray-900">
              Paw<span className="bg-gradient-to-r from-purple-600 to-green-500 bg-clip-text text-transparent">Spa</span>
            </span>
            <span className="ml-3 text-sm text-gray-400">Panel Administrador</span>
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
            { label: "Total usuarios",  valor: stats.total,    color: "text-purple-600 bg-purple-50" },
            { label: "Activos",         valor: stats.activos,  color: "text-green-600 bg-green-50"   },
            { label: "Groomers",        valor: stats.groomers, color: "text-blue-600 bg-blue-50"     },
            { label: "Clientes",        valor: stats.clientes, color: "text-gray-600 bg-gray-50"     },
          ].map((s, i) => (
            <div key={i} className="bg-white rounded-2xl border border-gray-100 p-5">
              <div className={`text-2xl font-bold ${s.color.split(" ")[0]}`}>{s.valor}</div>
              <div className="text-sm text-gray-500 mt-1">{s.label}</div>
            </div>
          ))}
        </div>

        {/* Header tabla */}
        <div className="bg-white rounded-2xl border border-gray-100">
          <div className="flex flex-col sm:flex-row items-start sm:items-center justify-between gap-4 p-6 border-b border-gray-100">
            <div className="flex items-center gap-2">
              <Users className="w-5 h-5 text-purple-500" />
              <h2 className="font-semibold text-gray-900">Gestión de usuarios</h2>
            </div>
            <div className="flex items-center gap-3 w-full sm:w-auto">
              <div className="relative flex-1 sm:w-64">
                <Search className="w-4 h-4 text-gray-400 absolute left-3 top-1/2 -translate-y-1/2" />
                <input
                  value={busqueda}
                  onChange={(e) => setBusqueda(e.target.value)}
                  placeholder="Buscar usuario..."
                  className="w-full pl-9 pr-4 py-2 rounded-xl border border-gray-200 text-sm focus:outline-none focus:ring-2 focus:ring-purple-300"
                />
              </div>
              <button
                onClick={() => setModalCrear(true)}
                className="flex items-center gap-2 px-4 py-2 rounded-xl bg-gradient-to-r from-purple-500 to-purple-600 text-white text-sm font-medium hover:from-purple-600 hover:to-purple-700 transition-all"
              >
                <Plus className="w-4 h-4" />
                Nuevo
              </button>
            </div>
          </div>

          {/* Tabla */}
          {loading ? (
            <div className="flex items-center justify-center py-16">
              <Loader2 className="w-8 h-8 text-purple-500 animate-spin" />
            </div>
          ) : (
            <div className="overflow-x-auto">
              <table className="w-full">
                <thead>
                  <tr className="text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    <th className="px-6 py-3">Usuario</th>
                    <th className="px-6 py-3">Correo</th>
                    <th className="px-6 py-3">Rol</th>
                    <th className="px-6 py-3">Estado</th>
                    <th className="px-6 py-3">Acciones</th>
                  </tr>
                </thead>
                <tbody className="divide-y divide-gray-50">
                  {usuariosFiltrados.map((u) => (
                    <tr key={u.id} className="hover:bg-gray-50 transition-colors">
                      <td className="px-6 py-4">
                        <div className="flex items-center gap-3">
                          <div className="w-8 h-8 rounded-full bg-gradient-to-br from-purple-400 to-green-400 flex items-center justify-center text-white text-xs font-medium">
                            {u.nombre[0]}{u.apellido[0]}
                          </div>
                          <div>
                            <div className="text-sm font-medium text-gray-900">
                              {u.nombre} {u.apellido}
                            </div>
                            <div className="text-xs text-gray-400">
                              {u.telefono || "Sin teléfono"}
                            </div>
                          </div>
                        </div>
                      </td>
                      <td className="px-6 py-4 text-sm text-gray-600">{u.correo}</td>
                      <td className="px-6 py-4">
                        <span className={`inline-flex items-center gap-1.5 px-2.5 py-1 rounded-full text-xs font-medium ${rolColor[u.rol]}`}>
                          {rolIcon[u.rol]}
                          {u.rol}
                        </span>
                      </td>
                      <td className="px-6 py-4">
                        <span className={`inline-flex items-center px-2.5 py-1 rounded-full text-xs font-medium ${
                          u.activo
                            ? "bg-green-100 text-green-700"
                            : "bg-red-100 text-red-600"
                        }`}>
                          {u.activo ? "Activo" : "Inactivo"}
                        </span>
                      </td>
                      <td className="px-6 py-4">
                        <div className="flex items-center gap-2">
                          <button
                            onClick={() => abrirEditar(u)}
                            className="p-1.5 rounded-lg text-gray-400 hover:text-purple-600 hover:bg-purple-50 transition-colors"
                            title="Editar"
                          >
                            <Pencil className="w-4 h-4" />
                          </button>
                          <button
                            onClick={() => handleToggleActivo(u)}
                            className={`p-1.5 rounded-lg transition-colors ${
                              u.activo
                                ? "text-gray-400 hover:text-red-500 hover:bg-red-50"
                                : "text-gray-400 hover:text-green-600 hover:bg-green-50"
                            }`}
                            title={u.activo ? "Desactivar" : "Activar"}
                          >
                            {u.activo
                              ? <PowerOff className="w-4 h-4" />
                              : <Power    className="w-4 h-4" />
                            }
                          </button>
                        </div>
                      </td>
                    </tr>
                  ))}
                  {usuariosFiltrados.length === 0 && (
                    <tr>
                      <td colSpan={5} className="px-6 py-12 text-center text-gray-400 text-sm">
                        No se encontraron usuarios
                      </td>
                    </tr>
                  )}
                </tbody>
              </table>
            </div>
          )}
        </div>
      </div>

      {/* Modal Crear */}
      {modalCrear && (
        <Modal title="Crear usuario" onClose={() => { setModalCrear(false); formCrear.reset(); }}>
          <form onSubmit={formCrear.handleSubmit(handleCrear)} className="space-y-4">
            <div className="grid grid-cols-2 gap-3">
              <FormField label="Nombre" error={formCrear.formState.errors.nombre?.message}>
                <input {...formCrear.register("nombre")} placeholder="Juan" className={inputClass} />
              </FormField>
              <FormField label="Apellido" error={formCrear.formState.errors.apellido?.message}>
                <input {...formCrear.register("apellido")} placeholder="Pérez" className={inputClass} />
              </FormField>
            </div>
            <FormField label="Correo" error={formCrear.formState.errors.correo?.message}>
              <input {...formCrear.register("correo")} type="email" placeholder="tu@correo.com" className={inputClass} />
            </FormField>
            
            {formCrear.watch("rol") === "admin" && (
  <FormField label="Contraseña" error={formCrear.formState.errors.passwordHash?.message}>
    <input
      {...formCrear.register("passwordHash")}
      type="password"
      placeholder="••••••••"
      className={inputClass}
    />
  </FormField>
)}

{(formCrear.watch("rol") === "groomer" ||
  formCrear.watch("rol") === "recepcion") && (
  <div className="bg-blue-50 border border-blue-100 rounded-xl p-3">
    <p className="text-xs text-blue-600">
      🔑 Se generará una contraseña automática y se enviará al correo del trabajador.
    </p>
  </div>
)}

            <FormField label="Teléfono" error={formCrear.formState.errors.telefono?.message}>
              <input {...formCrear.register("telefono")} placeholder="+591 76543210" className={inputClass} />
            </FormField>
            <FormField label="Rol" error={formCrear.formState.errors.rol?.message}>
              <select {...formCrear.register("rol")} className={inputClass}>
                <option value="">Seleccionar rol</option>
                <option value="admin">Administrador</option>
                <option value="recepcion">Recepción</option>
                <option value="groomer">Groomer</option>
                <option value="cliente">Cliente</option>
              </select>
            </FormField>
            <div className="flex gap-3 pt-2">
              <button
                type="button"
                onClick={() => { setModalCrear(false); formCrear.reset(); }}
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
                Crear usuario
              </button>
            </div>
          </form>
        </Modal>
      )}

      {/* Modal Editar */}
      {modalEditar && (
        <Modal title="Editar usuario" onClose={() => setModalEditar(false)}>
          <form onSubmit={formEditar.handleSubmit(handleEditar)} className="space-y-4">
            <div className="grid grid-cols-2 gap-3">
              <FormField label="Nombre" error={formEditar.formState.errors.nombre?.message}>
                <input {...formEditar.register("nombre")} className={inputClass} />
              </FormField>
              <FormField label="Apellido" error={formEditar.formState.errors.apellido?.message}>
                <input {...formEditar.register("apellido")} className={inputClass} />
              </FormField>
            </div>
            <FormField label="Teléfono">
              <input {...formEditar.register("telefono")} className={inputClass} />
            </FormField>
            <FormField label="Rol" error={formEditar.formState.errors.rol?.message}>
              <select {...formEditar.register("rol")} className={inputClass}>
                <option value="admin">Administrador</option>
                <option value="recepcion">Recepción</option> 
                <option value="groomer">Groomer</option>
                <option value="cliente">Cliente</option>
              </select>
            </FormField>
            <FormField label="Nueva contraseña (opcional)" error={formEditar.formState.errors.passwordHash?.message}>
              <input {...formEditar.register("passwordHash")} type="password" placeholder="Dejar vacío para no cambiar" className={inputClass} />
            </FormField>
            <div className="flex gap-3 pt-2">
              <button
                type="button"
                onClick={() => setModalEditar(false)}
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
                Guardar cambios
              </button>
            </div>
          </form>
        </Modal>
      )}

    </div>
  );
};

export default DashboardAdmin;
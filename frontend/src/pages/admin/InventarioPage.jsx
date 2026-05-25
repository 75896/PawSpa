import { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import toast from "react-hot-toast";
import {
  Heart, ArrowLeft, Package, AlertTriangle,
  CheckCircle, XCircle, Loader2, Plus,
  RefreshCw, Search, X
} from "lucide-react";
import {
  listarVariantes, listarAlertasStock,
  reponerStock, actualizarStockMinimo
} from "../../api/inventarioApi";

const nivelColor = {
  ok:      "bg-green-100 text-green-700",
  bajo:    "bg-yellow-100 text-yellow-700",
  critico: "bg-red-100 text-red-600",
};

const nivelIcon = {
  ok:      <CheckCircle className="w-3.5 h-3.5" />,
  bajo:    <AlertTriangle className="w-3.5 h-3.5" />,
  critico: <XCircle className="w-3.5 h-3.5" />,
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

const InventarioPage = () => {
  const [variantes, setVariantes]       = useState([]);
  const [alertas, setAlertas]           = useState([]);
  const [loading, setLoading]           = useState(true);
  const [busqueda, setBusqueda]         = useState("");
  const [filtroAlerta, setFiltroAlerta] = useState("TODOS");
  const [modalReponer, setModalReponer] = useState(false);
  const [modalMinimo, setModalMinimo]   = useState(false);
  const [varianteSeleccionada, setVarianteSeleccionada] = useState(null);
  const [cantidadReponer, setCantidadReponer] = useState("10");
  const [nuevoMinimo, setNuevoMinimo]   = useState("5");
  const [loadingAccion, setLoadingAccion] = useState(false);

  const inputClass = "w-full px-4 py-2.5 rounded-xl border border-gray-200 focus:outline-none focus:ring-2 focus:ring-purple-300 text-sm";

  const cargarDatos = async () => {
    setLoading(true);
    try {
      const [vRes, aRes] = await Promise.all([
        listarVariantes(),
        listarAlertasStock(),
      ]);
      setVariantes(vRes.data);
      setAlertas(aRes.data);
    } catch {
      toast.error("Error al cargar inventario");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => { cargarDatos(); }, []);

  const handleReponer = async () => {
    if (!cantidadReponer || parseInt(cantidadReponer) <= 0) {
      toast.error("Ingresa una cantidad válida");
      return;
    }
    setLoadingAccion(true);
    try {
      await reponerStock(varianteSeleccionada.id, parseInt(cantidadReponer));
      toast.success(`Stock repuesto: +${cantidadReponer} unidades`);
      setModalReponer(false);
      setCantidadReponer("10");
      cargarDatos();
    } catch (err) {
      toast.error(err.response?.data?.message || "Error al reponer stock");
    } finally {
      setLoadingAccion(false);
    }
  };

  const handleActualizarMinimo = async () => {
    setLoadingAccion(true);
    try {
      await actualizarStockMinimo(varianteSeleccionada.id, parseInt(nuevoMinimo));
      toast.success("Stock mínimo actualizado");
      setModalMinimo(false);
      cargarDatos();
    } catch {
      toast.error("Error al actualizar mínimo");
    } finally {
      setLoadingAccion(false);
    }
  };

  const variantesFiltradas = variantes.filter((v) => {
    const coincideBusqueda = `${v.productoNombre} ${v.nombre} ${v.sku || ""}`
      .toLowerCase().includes(busqueda.toLowerCase());
    const coincideFiltro = filtroAlerta === "TODOS" || v.nivelAlerta === filtroAlerta.toLowerCase();
    return coincideBusqueda && coincideFiltro;
  });

  const stats = {
    total:    variantes.length,
    ok:       variantes.filter(v => v.nivelAlerta === "ok").length,
    bajo:     variantes.filter(v => v.nivelAlerta === "bajo").length,
    critico:  variantes.filter(v => v.nivelAlerta === "critico").length,
  };

  return (
    <div className="min-h-screen bg-gray-50">

      {/* Navbar */}
      <nav className="bg-white border-b border-gray-100 px-6 py-4">
        <div className="max-w-7xl mx-auto flex items-center justify-between">
          <div className="flex items-center gap-3">
            <Link to="/admin" className="text-gray-400 hover:text-gray-600">
              <ArrowLeft className="w-5 h-5" />
            </Link>
            <div className="w-8 h-8 rounded-lg bg-gradient-to-br from-purple-500 to-green-500 flex items-center justify-center">
              <Heart className="w-4 h-4 text-white" />
            </div>
            <span className="font-semibold text-gray-900">
              Paw<span className="bg-gradient-to-r from-purple-600 to-green-500 bg-clip-text text-transparent">Spa</span>
            </span>
            <span className="ml-2 text-sm text-gray-400">Inventario</span>
          </div>
          <button
            onClick={cargarDatos}
            className="flex items-center gap-2 text-sm text-gray-500 hover:text-purple-600 px-3 py-2 rounded-lg hover:bg-purple-50 transition-colors"
          >
            <RefreshCw className="w-4 h-4" />
            Actualizar
          </button>
        </div>
      </nav>

      <div className="max-w-7xl mx-auto px-6 py-8">

        {/* Alertas críticas */}
        {alertas.length > 0 && (
          <div className="bg-red-50 border border-red-200 rounded-2xl p-5 mb-6">
            <div className="flex items-center gap-2 mb-3">
              <AlertTriangle className="w-5 h-5 text-red-500" />
              <span className="font-medium text-red-700">
                {alertas.length} producto(s) requieren reabastecimiento
              </span>
            </div>
            <div className="grid grid-cols-2 md:grid-cols-4 gap-2">
              {alertas.map((a) => (
                <div key={a.id} className="bg-white rounded-xl p-3 border border-red-100">
                  <div className="text-xs font-medium text-gray-900 truncate">{a.productoNombre}</div>
                  <div className="text-xs text-gray-500 truncate">{a.nombre}</div>
                  <div className="flex items-center justify-between mt-1">
                    <span className={`text-xs px-1.5 py-0.5 rounded-full font-medium ${nivelColor[a.nivelAlerta]}`}>
                      Stock: {a.stock}
                    </span>
                    <button
                      onClick={() => { setVarianteSeleccionada(a); setCantidadReponer("10"); setModalReponer(true); }}
                      className="text-xs text-purple-600 hover:underline"
                    >
                      Reponer
                    </button>
                  </div>
                </div>
              ))}
            </div>
          </div>
        )}

        {/* Stats */}
        <div className="grid grid-cols-2 md:grid-cols-4 gap-4 mb-8">
          {[
            { label: "Total productos", valor: stats.total,   color: "text-purple-600" },
            { label: "Stock OK",        valor: stats.ok,      color: "text-green-600"  },
            { label: "Stock bajo",      valor: stats.bajo,    color: "text-yellow-600" },
            { label: "Sin stock",       valor: stats.critico, color: "text-red-600"    },
          ].map((s, i) => (
            <div key={i} className="bg-white rounded-2xl border border-gray-100 p-5">
              <div className={`text-2xl font-bold ${s.color}`}>{s.valor}</div>
              <div className="text-sm text-gray-500 mt-1">{s.label}</div>
            </div>
          ))}
        </div>

        {/* Tabla */}
        <div className="bg-white rounded-2xl border border-gray-100">

          {/* Filtros */}
          <div className="flex flex-col sm:flex-row gap-4 p-6 border-b border-gray-100">
            <div className="relative flex-1">
              <Search className="w-4 h-4 text-gray-400 absolute left-3 top-1/2 -translate-y-1/2" />
              <input
                value={busqueda}
                onChange={(e) => setBusqueda(e.target.value)}
                placeholder="Buscar producto..."
                className="w-full pl-9 pr-4 py-2 rounded-xl border border-gray-200 text-sm focus:outline-none focus:ring-2 focus:ring-purple-300"
              />
            </div>
            <select
              value={filtroAlerta}
              onChange={(e) => setFiltroAlerta(e.target.value)}
              className="px-3 py-2 rounded-xl border border-gray-200 text-sm focus:outline-none focus:ring-2 focus:ring-purple-300"
            >
              <option value="TODOS">Todos</option>
              <option value="OK">Stock OK</option>
              <option value="BAJO">Stock bajo</option>
              <option value="CRITICO">Sin stock</option>
            </select>
          </div>

          {loading ? (
            <div className="flex items-center justify-center py-16">
              <Loader2 className="w-8 h-8 text-purple-500 animate-spin" />
            </div>
          ) : (
            <div className="overflow-x-auto">
              <table className="w-full">
                <thead>
                  <tr className="text-left text-xs font-medium text-gray-500 uppercase tracking-wider border-b border-gray-100">
                    <th className="px-6 py-3">Producto</th>
                    <th className="px-6 py-3">Variante</th>
                    <th className="px-6 py-3">Stock actual</th>
                    <th className="px-6 py-3">Stock mínimo</th>
                    <th className="px-6 py-3">Estado</th>
                    <th className="px-6 py-3">Acciones</th>
                  </tr>
                </thead>
                <tbody className="divide-y divide-gray-50">
                  {variantesFiltradas.map((v) => (
                    <tr key={v.id} className="hover:bg-gray-50 transition-colors">
                      <td className="px-6 py-4">
                        <div className="flex items-center gap-3">
                          <div className="w-8 h-8 rounded-lg bg-purple-50 flex items-center justify-center">
                            <Package className="w-4 h-4 text-purple-500" />
                          </div>
                          <span className="text-sm font-medium text-gray-900">{v.productoNombre}</span>
                        </div>
                      </td>
                      <td className="px-6 py-4 text-sm text-gray-600">{v.nombre}</td>
                      <td className="px-6 py-4">
                        <span className={`text-sm font-bold ${
                          v.stock === 0 ? "text-red-600" :
                          v.stock <= v.stockMinimo ? "text-yellow-600" : "text-gray-900"
                        }`}>
                          {v.stock}
                        </span>
                      </td>
                      <td className="px-6 py-4 text-sm text-gray-500">{v.stockMinimo}</td>
                      <td className="px-6 py-4">
                        <span className={`inline-flex items-center gap-1 px-2.5 py-1 rounded-full text-xs font-medium ${nivelColor[v.nivelAlerta]}`}>
                          {nivelIcon[v.nivelAlerta]}
                          {v.nivelAlerta === "ok" ? "OK" : v.nivelAlerta === "bajo" ? "Bajo" : "Sin stock"}
                        </span>
                      </td>
                      <td className="px-6 py-4">
                        <div className="flex gap-2">
                          <button
                            onClick={() => { setVarianteSeleccionada(v); setCantidadReponer("10"); setModalReponer(true); }}
                            className="flex items-center gap-1 text-xs px-3 py-1.5 rounded-lg bg-green-50 text-green-600 hover:bg-green-100 transition-colors"
                          >
                            <Plus className="w-3.5 h-3.5" />
                            Reponer
                          </button>
                          <button
                            onClick={() => { setVarianteSeleccionada(v); setNuevoMinimo(String(v.stockMinimo)); setModalMinimo(true); }}
                            className="text-xs px-3 py-1.5 rounded-lg bg-gray-50 text-gray-600 hover:bg-gray-100 transition-colors"
                          >
                            Mínimo
                          </button>
                        </div>
                      </td>
                    </tr>
                  ))}
                  {variantesFiltradas.length === 0 && (
                    <tr>
                      <td colSpan={6} className="px-6 py-12 text-center text-gray-400 text-sm">
                        No se encontraron productos
                      </td>
                    </tr>
                  )}
                </tbody>
              </table>
            </div>
          )}
        </div>
      </div>

      {/* Modal Reponer */}
      {modalReponer && varianteSeleccionada && (
        <Modal title="Reponer stock" onClose={() => setModalReponer(false)}>
          <div className="space-y-4">
            <div className="bg-gray-50 rounded-xl p-4">
              <div className="font-medium text-gray-900">{varianteSeleccionada.productoNombre}</div>
              <div className="text-sm text-gray-500">{varianteSeleccionada.nombre}</div>
              <div className="text-sm text-gray-700 mt-1">
                Stock actual: <strong>{varianteSeleccionada.stock}</strong> unidades
              </div>
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">
                Cantidad a reponer
              </label>
              <input
                type="number"
                min="1"
                value={cantidadReponer}
                onChange={(e) => setCantidadReponer(e.target.value)}
                className={inputClass}
              />
              {cantidadReponer && (
                <p className="text-xs text-gray-400 mt-1">
                  Nuevo stock: {varianteSeleccionada.stock + parseInt(cantidadReponer || 0)} unidades
                </p>
              )}
            </div>
            <div className="flex gap-3">
              <button
                onClick={() => setModalReponer(false)}
                className="flex-1 py-2.5 rounded-xl border border-gray-200 text-sm text-gray-600 hover:bg-gray-50"
              >
                Cancelar
              </button>
              <button
                onClick={handleReponer}
                disabled={loadingAccion}
                className="flex-1 py-2.5 rounded-xl bg-gradient-to-r from-green-500 to-green-600 text-white text-sm font-medium disabled:opacity-60 flex items-center justify-center gap-2"
              >
                {loadingAccion && <Loader2 className="w-4 h-4 animate-spin" />}
                Reponer stock
              </button>
            </div>
          </div>
        </Modal>
      )}

      {/* Modal Stock Mínimo */}
      {modalMinimo && varianteSeleccionada && (
        <Modal title="Actualizar stock mínimo" onClose={() => setModalMinimo(false)}>
          <div className="space-y-4">
            <div className="bg-gray-50 rounded-xl p-4">
              <div className="font-medium text-gray-900">{varianteSeleccionada.productoNombre}</div>
              <div className="text-sm text-gray-500">{varianteSeleccionada.nombre}</div>
              <div className="text-sm text-gray-700 mt-1">
                Mínimo actual: <strong>{varianteSeleccionada.stockMinimo}</strong> unidades
              </div>
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">
                Nuevo stock mínimo
              </label>
              <input
                type="number"
                min="0"
                value={nuevoMinimo}
                onChange={(e) => setNuevoMinimo(e.target.value)}
                className={inputClass}
              />
              <p className="text-xs text-gray-400 mt-1">
                Se emitirá alerta cuando el stock baje de este valor
              </p>
            </div>
            <div className="flex gap-3">
              <button
                onClick={() => setModalMinimo(false)}
                className="flex-1 py-2.5 rounded-xl border border-gray-200 text-sm text-gray-600 hover:bg-gray-50"
              >
                Cancelar
              </button>
              <button
                onClick={handleActualizarMinimo}
                disabled={loadingAccion}
                className="flex-1 py-2.5 rounded-xl bg-gradient-to-r from-purple-500 to-purple-600 text-white text-sm font-medium disabled:opacity-60 flex items-center justify-center gap-2"
              >
                {loadingAccion && <Loader2 className="w-4 h-4 animate-spin" />}
                Actualizar
              </button>
            </div>
          </div>
        </Modal>
      )}

    </div>
  );
};

export default InventarioPage;
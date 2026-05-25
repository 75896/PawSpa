import api from "../utils/axiosConfig";

export const getMisCitasHoy       = () => api.get("/groomers/citas/hoy");
export const getMisCitas          = () => api.get("/groomers/citas");
export const getMisFichas         = () => api.get("/groomers/fichas");
export const abrirFicha           = (citaId) => api.post(`/groomers/fichas/cita/${citaId}`);
export const actualizarFicha      = (fichaId, data) => api.put(`/groomers/fichas/${fichaId}`, data);
export const cerrarFicha          = (fichaId) => api.patch(`/groomers/fichas/${fichaId}/cerrar`);
export const getFichaPorCita      = (citaId) => api.get(`/groomers/fichas/cita/${citaId}`);
export const getProductosDisponibles = () => api.get("/groomers/insumos/disponibles");
export const getInsumosPorFicha   = (fichaId) => api.get(`/groomers/fichas/${fichaId}/insumos`);
export const registrarInsumo      = (fichaId, data) => api.post(`/groomers/fichas/${fichaId}/insumos`, data);
export const eliminarInsumo       = (id) => api.delete(`/groomers/insumos/${id}`);
export const getAlertasStockBajo  = () => api.get("/groomers/alertas/stock-bajo");
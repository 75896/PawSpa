import api from "../utils/axiosConfig";

export const listarAuditoria      = ()         => api.get("/admin/auditoria");
export const auditoriaPorCorreo   = (correo)   => api.get(`/admin/auditoria/correo/${correo}`);
export const auditoriaPorAccion   = (accion)   => api.get(`/admin/auditoria/accion/${accion}`);
export const auditoriaPorResultado = (resultado) => api.get(`/admin/auditoria/resultado/${resultado}`);
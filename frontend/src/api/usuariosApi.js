import api from "../utils/axiosConfig";

export const listarUsuarios  = ()        => api.get("/admin/usuarios");
export const obtenerUsuario  = (id)      => api.get(`/admin/usuarios/${id}`);
export const crearUsuario    = (data)    => api.post("/admin/usuarios", data);
export const actualizarUsuario = (id, data) => api.put(`/admin/usuarios/${id}`, data);
export const desactivarUsuario = (id)    => api.patch(`/admin/usuarios/${id}/desactivar`);
export const activarUsuario  = (id)      => api.patch(`/admin/usuarios/${id}/activar`);
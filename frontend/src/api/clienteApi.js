import api from "../utils/axiosConfig";

export const getMisMascotas  = ()    => api.get("/cliente/mascotas");
export const crearMascota    = (data) => api.post("/cliente/mascotas", data);
export const getMisCitas     = ()    => api.get("/cliente/citas");
export const actualizarMascota = (id, data) => api.put(`/cliente/mascotas/${id}`, data);
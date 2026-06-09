import api from "../utils/axiosConfig";

export const getMisMascotas  = ()    => api.get("/cliente/mascotas");
export const crearMascota    = (data) => api.post("/cliente/mascotas", data);
export const getMisCitas     = ()    => api.get("/cliente/citas");
export const actualizarMascota = (id, data) => api.put(`/cliente/mascotas/${id}`, data);
export const listarRazas = () => api.get("/razas");
export const listarRazasPorEspecie = (especie) => api.get(`/razas/especie/${especie}`);
export const solicitarCita    = (data) => api.post("/cliente/citas", data);
export const listarServicios = () => api.get("/cliente/servicios");
//export const listarGroomers   = ()     => api.get("/recepcion/groomers");
import api from "../utils/axiosConfig";

export const listarCitas          = ()           => api.get("/recepcion/citas");
export const listarCitasHoy       = ()           => api.get("/recepcion/citas/hoy");
export const listarCitasPorFecha  = (fecha)      => api.get(`/recepcion/citas/fecha?fecha=${fecha}`);
export const agendarCita          = (data)       => api.post("/recepcion/citas", data);
export const confirmarCita        = (id)         => api.patch(`/recepcion/citas/${id}/confirmar`);
export const cancelarCita         = (id, motivo) => api.patch(`/recepcion/citas/${id}/cancelar?motivo=${motivo}`);
export const listarServicios      = ()           => api.get("/recepcion/servicios");
export const listarGroomers       = ()           => api.get("/recepcion/groomers");
export const listarBloqueos       = ()           => api.get("/recepcion/bloqueos");
export const crearBloqueo         = (data)       => api.post("/recepcion/bloqueos", data);
export const eliminarBloqueo      = (id)         => api.delete(`/recepcion/bloqueos/${id}`);
export const buscarClientes  = ()        => api.get("/admin/usuarios?rol=cliente");
export const getMascotasCliente = (clienteId) => api.get(`/admin/clientes/${clienteId}/mascotas`);
export const asignarGroomer = (citaId, groomerId) => api.patch(`/recepcion/citas/${citaId}/asignar-groomer?groomerId=${groomerId}`);


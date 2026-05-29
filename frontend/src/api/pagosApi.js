import api from "../utils/axiosConfig";

// Facturas
export const listarFacturas         = ()        => api.get("/facturas");
export const obtenerFactura         = (id)      => api.get(`/facturas/${id}`);
export const generarRecibo          = (id)      => api.get(`/facturas/${id}/recibo`);
export const generarFacturaPedido   = (pedidoId) => api.post(`/facturas/generar/pedido/${pedidoId}`);

// Pagos
export const cobrarFactura          = (data)    => api.post("/pagos/cobrar", data);
export const cierreCaja             = (fecha)   => api.get(`/pagos/cierre-caja${fecha ? `?fecha=${fecha}` : ""}`);
export const obtenerConfiguracion   = (clave) => api.get(`/configuracion/${clave}`);
export const actualizarConfiguracion = (clave, data) => api.put(`/configuracion/${clave}`, data);
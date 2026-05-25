import api from "../utils/axiosConfig";
import axios from "axios";

// Cliente público sin auth (para la landing)
const publicApi = axios.create({
  baseURL: "http://localhost:5050/api",
  headers: { "Content-Type": "application/json" },
});

// =============================================
// PÚBLICOS — landing (sin auth)
// =============================================
export const getCatalogo         = (categoriaId) =>
  publicApi.get("/productos/publico", { params: categoriaId ? { categoriaId } : {} });

export const getProductoDetalle  = (id) =>
  publicApi.get(`/productos/publico/${id}`);

export const getCategorias       = () =>
  publicApi.get("/categorias-producto");

// =============================================
// CARRITO — requiere auth (cliente)
// =============================================
export const obtenerOCrearCarrito = () => api.post("/cliente/carrito");
export const verCarrito           = ()           => api.get("/cliente/carrito");
export const agregarItem          = (pedidoId, data) =>
  api.post(`/cliente/carrito/${pedidoId}/items`, data);
export const actualizarItem       = (pedidoId, itemId, cantidad) =>
  api.put(`/cliente/carrito/${pedidoId}/items/${itemId}`, null, { params: { cantidad } });
export const eliminarItem         = (pedidoId, itemId) =>
  api.delete(`/cliente/carrito/${pedidoId}/items/${itemId}`);
export const confirmarPedido      = (pedidoId)   => api.post(`/cliente/carrito/${pedidoId}/confirmar`);
export const generarMensaje       = (pedidoId)   => api.get(`/cliente/carrito/${pedidoId}/mensaje`);
export const getMisPedidos        = ()           => api.get("/cliente/pedidos");

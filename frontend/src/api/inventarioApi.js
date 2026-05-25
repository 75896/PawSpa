import api from "../utils/axiosConfig";

export const listarVariantes      = ()              => api.get("/admin/inventario/variantes");
export const listarAlertasStock   = ()              => api.get("/admin/inventario/alertas");
export const reponerStock         = (id, cantidad)  => api.patch(`/admin/inventario/variantes/${id}/reponer?cantidad=${cantidad}`);
export const actualizarStockMinimo = (id, stockMinimo) => api.patch(`/admin/inventario/variantes/${id}/stock-minimo?stockMinimo=${stockMinimo}`);
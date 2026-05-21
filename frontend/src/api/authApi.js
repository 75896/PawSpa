import api from "../utils/axiosConfig";

export const register = (data) => api.post("/auth/register", data);
export const login    = (data) => api.post("/auth/login", data);
export const activar  = (token) => api.get(`/auth/activar?token=${token}`);
export const reenviarActivacion = (correo) => 
    api.post(`/auth/reenviar-activacion?correo=${correo}`);
export const solicitarCambioPassword = (data) => 
    api.post("/auth/solicitar-cambio-password", data);
export const cambiarPassword = (data) => 
    api.post("/auth/cambiar-password", data);
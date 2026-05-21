import api from "../utils/axiosConfig";

export const getMisCitasHoy = () => api.get("/citas");
export const getMisCitas    = () => api.get("/citas");
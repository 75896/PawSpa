import axios from "axios";

const getToken = () => {
  try {
    const authStorage = localStorage.getItem("auth-storage");
    if (!authStorage) return null;
    const parsed = JSON.parse(authStorage);
    return parsed?.state?.token || null;
  } catch {
    return null;
  }
};

const api = axios.create({
  baseURL: "http://localhost:5050/api",
  headers: {
    "Content-Type": "application/json",
  },
});

api.interceptors.request.use(
  (config) => {
    const token = getToken();
    console.log("=== Axios token:", token ? "EXISTS" : "NULL");
    if (token) {
      config.headers["Authorization"] = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

api.interceptors.response.use(
  (response) => response,
  (error) => {
    console.log("=== Axios error:", error.response?.status, error.config?.url);
    if (error.response?.status === 401) {
      const token = getToken();
      if (!token) {
        window.location.href = "/login";
      }
    }
    return Promise.reject(error);
  }
);

export default api;
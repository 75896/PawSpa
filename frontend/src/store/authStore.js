import { create } from "zustand";
import { persist } from "zustand/middleware";

const useAuthStore = create(
  persist(
    (set) => ({
      token:   null,
      usuario: null,
      rol:     null,
      login: (token, usuario) => set({ token, usuario, rol: usuario.rol }),
      logout: () => set({ token: null, usuario: null, rol: null }),
      setUsuario: (usuario) => set({ usuario, rol: usuario.rol }),
    }),
    { name: "auth-storage" }
  )
);

export default useAuthStore;
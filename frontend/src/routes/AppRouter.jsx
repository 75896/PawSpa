import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import useAuthStore from "../store/authStore";

import LandingPage      from "../pages/public/LandingPage";
import LoginPage        from "../pages/auth/LoginPage";
import RegisterPage     from "../pages/auth/RegisterPage";
import NotFoundPage     from "../pages/shared/NotFoundPage";
import UnauthorizedPage from "../pages/shared/UnauthorizedPage";
import ActivarCuentaPage from "../pages/auth/ActivarCuentaPage";
import DashboardCliente   from "../pages/cliente/DashboardCliente";
import DashboardAdmin     from "../pages/admin/DashboardAdmin";
import DashboardRecepcion from "../pages/recepcion/DashboardRecepcion";
import DashboardGroomer   from "../pages/groomer/DashboardGroomer";
import ProtectedRoute from "../components/common/ProtectedRoute";
import OlvidePasswordPage  from "../pages/auth/OlvidePasswordPage";
import CambiarPasswordPage from "../pages/auth/CambiarPasswordPage";
import AuditoriaPage from "../pages/admin/AuditoriaPage";
import AgendaPage from "../pages/recepcion/AgendaPage";
import InventarioPage from "../pages/admin/InventarioPage";
import CarritoPage from "../pages/cliente/CarritoPage";
import PuntoVentaPage from "../pages/recepcion/PuntoVentaPage";


//const ProtectedRoute = ({ children, roles }) => {
//  const { token, rol } = useAuthStore();
//  if (!token) return <Navigate to="/login" replace />;
//  if (roles && !roles.includes(rol)) return <Navigate to="/unauthorized" replace />;
//  return children;
//};

const AppRouter = () => {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/"             element={<LandingPage />} />
        <Route path="/login"        element={<LoginPage />} />
        <Route path="/register"     element={<RegisterPage />} />
        <Route path="/unauthorized" element={<UnauthorizedPage />} />
        <Route path="*"             element={<NotFoundPage />} />
        <Route path="/activar" element={<ActivarCuentaPage />} />
        <Route path="/olvide-password"   element={<OlvidePasswordPage />} />
        <Route path="/cambiar-password"  element={<CambiarPasswordPage />} />
        <Route path="/cliente"   element={<DashboardCliente />} />
        <Route path="/groomer" element={
  <ProtectedRoute roles={["groomer"]}>
    <DashboardGroomer />
  </ProtectedRoute>
} />
<Route path="/admin" element={
  <ProtectedRoute roles={["admin"]}>
    <DashboardAdmin />
  </ProtectedRoute>
} />

<Route path="/admin/inventario" element={
  <ProtectedRoute roles={["admin"]}>
    <InventarioPage />
  </ProtectedRoute>
} />

<Route path="/carrito" element={
  <ProtectedRoute roles={["cliente"]}>
    <CarritoPage />
  </ProtectedRoute>
} />

<Route path="/recepcion" element={
  <ProtectedRoute roles={["admin", "recepcion"]}>
    <AgendaPage />
  </ProtectedRoute>
} />

<Route path="/recepcion/pagos" element={
  <ProtectedRoute roles={["admin", "recepcion"]}>
    <PuntoVentaPage />
  </ProtectedRoute>
} />

<Route path="/admin/auditoria" element={
  <ProtectedRoute roles={["admin"]}>
    <AuditoriaPage />
  </ProtectedRoute>
} />

<Route path="/recepcion" element={<DashboardRecepcion />} />
<Route path="/groomer"   element={<DashboardGroomer />} />
      </Routes>
    </BrowserRouter>
  );
};

export default AppRouter;
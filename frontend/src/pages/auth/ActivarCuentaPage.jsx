import { useEffect, useState } from "react";
import { useSearchParams, Link } from "react-router-dom";
import { CheckCircle, XCircle, Loader2, Heart } from "lucide-react";
import { activar } from "../../api/authApi";

const ActivarCuentaPage = () => {
  const [searchParams] = useSearchParams();
  const [estado, setEstado] = useState("cargando");
  const token = searchParams.get("token");

  useEffect(() => {
    if (!token) {
      setEstado("error");
      return;
    }
    activar(token)
      .then(() => setEstado("exitoso"))
      .catch(() => setEstado("error"));
  }, [token]);

  return (
    <div className="min-h-screen bg-gradient-to-br from-purple-50 via-white to-green-50 flex items-center justify-center p-4">
      <div className="bg-white rounded-2xl shadow-sm border border-gray-100 p-10 max-w-md w-full text-center">

        <Link to="/" className="inline-flex items-center gap-2 mb-8">
          <div className="w-8 h-8 rounded-lg bg-gradient-to-br from-purple-500 to-green-500 flex items-center justify-center">
            <Heart className="w-4 h-4 text-white" />
          </div>
          <span className="text-xl font-semibold text-gray-900">
            Paw<span className="bg-gradient-to-r from-purple-600 to-green-500 bg-clip-text text-transparent">Spa</span>
          </span>
        </Link>

        {estado === "cargando" && (
          <>
            <Loader2 className="w-12 h-12 text-purple-500 animate-spin mx-auto mb-4" />
            <h2 className="text-xl font-bold text-gray-900">Activando tu cuenta...</h2>
          </>
        )}

        {estado === "exitoso" && (
          <>
            <div className="w-16 h-16 bg-green-100 rounded-full flex items-center justify-center mx-auto mb-4">
              <CheckCircle className="w-8 h-8 text-green-500" />
            </div>
            <h2 className="text-xl font-bold text-gray-900 mb-2">¡Cuenta activada!</h2>
            <p className="text-gray-500 text-sm mb-6">Tu cuenta está lista, ya puedes iniciar sesión.</p>
            <Link
              to="/login"
              className="inline-block px-6 py-3 rounded-xl bg-gradient-to-r from-purple-500 to-purple-600 text-white font-medium text-sm"
            >
              Iniciar sesión
            </Link>
          </>
        )}

        {estado === "error" && (
          <>
            <div className="w-16 h-16 bg-red-100 rounded-full flex items-center justify-center mx-auto mb-4">
              <XCircle className="w-8 h-8 text-red-500" />
            </div>
            <h2 className="text-xl font-bold text-gray-900 mb-2">Link inválido o expirado</h2>
            <p className="text-gray-500 text-sm mb-6">El link expiró o ya fue usado. Solicita uno nuevo.</p>
            <Link
              to="/login"
              className="inline-block px-6 py-3 rounded-xl bg-gradient-to-r from-purple-500 to-purple-600 text-white font-medium text-sm"
            >
              Volver al login
            </Link>
          </>
        )}

      </div>
    </div>
  );
};

export default ActivarCuentaPage;
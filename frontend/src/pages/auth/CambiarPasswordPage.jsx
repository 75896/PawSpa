import { useState } from "react";
import { Link, useSearchParams, useNavigate } from "react-router-dom";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { z } from "zod";
import toast from "react-hot-toast";
import { Heart, Loader2, Eye, EyeOff, CheckCircle, XCircle } from "lucide-react";
import { cambiarPassword } from "../../api/authApi";

const schema = z.object({
  nuevaPassword: z.string()
    .min(8, "Mínimo 8 caracteres")
    .regex(/[A-Z]/, "Debe tener mayúscula")
    .regex(/[a-z]/, "Debe tener minúscula")
    .regex(/[0-9]/, "Debe tener número")
    .regex(/[*#!@$%^&()]/, "Debe tener símbolo"),
  confirmar: z.string(),
}).refine((data) => data.nuevaPassword === data.confirmar, {
  message: "Las contraseñas no coinciden",
  path: ["confirmar"],
});

const getPasswordStrength = (password) => {
  if (!password) return { score: 0, label: "", color: "" };
  let score = 0;
  if (password.length >= 8)           score++;
  if (/[A-Z]/.test(password))        score++;
  if (/[a-z]/.test(password))        score++;
  if (/[0-9]/.test(password))        score++;
  if (/[*#!@$%^&()]/.test(password)) score++;
  if (score <= 2) return { score, label: "Débil",   color: "bg-red-400"    };
  if (score <= 3) return { score, label: "Regular", color: "bg-yellow-400" };
  if (score <= 4) return { score, label: "Buena",   color: "bg-blue-400"   };
  return           { score, label: "Fuerte",  color: "bg-green-500"  };
};

const CambiarPasswordPage = () => {
  const [showPassword, setShowPassword]     = useState(false);
  const [showConfirmar, setShowConfirmar]   = useState(false);
  const [loading, setLoading]               = useState(false);
  const [cambiado, setCambiado]             = useState(false);
  const [password, setPassword]             = useState("");
  const [searchParams]                      = useSearchParams();
  const navigate                            = useNavigate();
  const token                               = searchParams.get("token");

  const { register, handleSubmit, formState: { errors } } = useForm({
    resolver: zodResolver(schema),
  });

  const strength = getPasswordStrength(password);

  if (!token) {
    return (
      <div className="min-h-screen bg-gradient-to-br from-purple-50 via-white to-green-50 flex items-center justify-center p-4">
        <div className="bg-white rounded-2xl shadow-sm border border-gray-100 p-10 max-w-md w-full text-center">
          <div className="w-16 h-16 bg-red-100 rounded-full flex items-center justify-center mx-auto mb-4">
            <XCircle className="w-8 h-8 text-red-500" />
          </div>
          <h2 className="text-xl font-bold text-gray-900 mb-2">Link inválido</h2>
          <p className="text-gray-500 text-sm mb-6">El link no es válido o ya expiró.</p>
          <Link to="/olvide-password" className="text-purple-600 hover:underline text-sm">
            Solicitar nuevo link
          </Link>
        </div>
      </div>
    );
  }

  const onSubmit = async (data) => {
    setLoading(true);
    try {
      await cambiarPassword({ token, nuevaPassword: data.nuevaPassword });
      setCambiado(true);
      toast.success("¡Contraseña cambiada correctamente!");
      setTimeout(() => navigate("/login"), 3000);
    } catch (err) {
      toast.error(err.response?.data?.message || "Token inválido o expirado");
    } finally {
      setLoading(false);
    }
  };

  if (cambiado) {
    return (
      <div className="min-h-screen bg-gradient-to-br from-purple-50 via-white to-green-50 flex items-center justify-center p-4">
        <div className="bg-white rounded-2xl shadow-sm border border-gray-100 p-10 max-w-md w-full text-center">
          <div className="w-16 h-16 bg-green-100 rounded-full flex items-center justify-center mx-auto mb-4">
            <CheckCircle className="w-8 h-8 text-green-500" />
          </div>
          <h2 className="text-xl font-bold text-gray-900 mb-2">¡Contraseña cambiada!</h2>
          <p className="text-gray-500 text-sm mb-2">Redirigiendo al login en 3 segundos...</p>
          <Link to="/login" className="text-purple-600 hover:underline text-sm">
            Ir al login ahora
          </Link>
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gradient-to-br from-purple-50 via-white to-green-50 flex items-center justify-center p-4">
      <div className="w-full max-w-md">

        <div className="text-center mb-8">
          <Link to="/" className="inline-flex items-center gap-2">
            <div className="w-10 h-10 rounded-xl bg-gradient-to-br from-purple-500 to-green-500 flex items-center justify-center">
              <Heart className="w-5 h-5 text-white" />
            </div>
            <span className="text-2xl font-semibold text-gray-900">
              Paw<span className="bg-gradient-to-r from-purple-600 to-green-500 bg-clip-text text-transparent">Spa</span>
            </span>
          </Link>
        </div>

        <div className="bg-white rounded-2xl shadow-sm border border-gray-100 p-8">
          <div className="text-center mb-6">
            <h1 className="text-xl font-bold text-gray-900">Nueva contraseña</h1>
            <p className="text-gray-500 text-sm mt-1">Ingresa tu nueva contraseña segura.</p>
          </div>

          <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">

            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">
                Nueva contraseña
              </label>
              <div className="relative">
                <input
                  {...register("nuevaPassword")}
                  type={showPassword ? "text" : "password"}
                  placeholder="••••••••"
                  onChange={(e) => setPassword(e.target.value)}
                  className="w-full px-4 py-2.5 rounded-xl border border-gray-200 focus:outline-none focus:ring-2 focus:ring-purple-300 text-sm pr-10"
                />
                <button
                  type="button"
                  onClick={() => setShowPassword(!showPassword)}
                  className="absolute right-3 top-1/2 -translate-y-1/2 text-gray-400 hover:text-gray-600"
                >
                  {showPassword ? <EyeOff className="w-4 h-4" /> : <Eye className="w-4 h-4" />}
                </button>
              </div>

              {password && (
                <div className="mt-2">
                  <div className="flex gap-1 mb-1">
                    {[1,2,3,4,5].map((i) => (
                      <div
                        key={i}
                        className={`h-1.5 flex-1 rounded-full transition-all ${
                          i <= strength.score ? strength.color : "bg-gray-200"
                        }`}
                      />
                    ))}
                  </div>
                  <p className="text-xs text-gray-500">
                    Fuerza: <span className="font-medium">{strength.label}</span>
                  </p>
                </div>
              )}
              {errors.nuevaPassword && (
                <p className="text-red-500 text-xs mt-1">{errors.nuevaPassword.message}</p>
              )}
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">
                Confirmar contraseña
              </label>
              <div className="relative">
                <input
                  {...register("confirmar")}
                  type={showConfirmar ? "text" : "password"}
                  placeholder="••••••••"
                  className="w-full px-4 py-2.5 rounded-xl border border-gray-200 focus:outline-none focus:ring-2 focus:ring-purple-300 text-sm pr-10"
                />
                <button
                  type="button"
                  onClick={() => setShowConfirmar(!showConfirmar)}
                  className="absolute right-3 top-1/2 -translate-y-1/2 text-gray-400 hover:text-gray-600"
                >
                  {showConfirmar ? <EyeOff className="w-4 h-4" /> : <Eye className="w-4 h-4" />}
                </button>
              </div>
              {errors.confirmar && (
                <p className="text-red-500 text-xs mt-1">{errors.confirmar.message}</p>
              )}
            </div>

            <button
              type="submit"
              disabled={loading}
              className="w-full py-3 rounded-xl bg-gradient-to-r from-purple-500 to-purple-600 text-white font-medium hover:from-purple-600 hover:to-purple-700 transition-all disabled:opacity-60 flex items-center justify-center gap-2 mt-2"
            >
              {loading && <Loader2 className="w-4 h-4 animate-spin" />}
              {loading ? "Cambiando..." : "Cambiar contraseña"}
            </button>
          </form>
        </div>
      </div>
    </div>
  );
};

export default CambiarPasswordPage;
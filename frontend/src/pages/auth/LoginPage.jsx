import { useState, useEffect } from "react";
import { Link, useNavigate } from "react-router-dom";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { z } from "zod";
import toast from "react-hot-toast";
import { Heart, Eye, EyeOff, Loader2 } from "lucide-react";
import { login } from "../../api/authApi";
import useAuthStore from "../../store/authStore";

const schema = z.object({
  correo:   z.string().email("Correo inválido"),
  password: z.string().min(1, "La contraseña es obligatoria"),
});

const LoginPage = () => {
  const [showPassword, setShowPassword] = useState(false);
  const [loading, setLoading]           = useState(false);
  const [captchaToken, setCaptchaToken] = useState(null);
  const navigate                        = useNavigate();
  const { login: setAuth }              = useAuthStore();

  const { register, handleSubmit, formState: { errors } } = useForm({
    resolver: zodResolver(schema),
  });

  useEffect(() => {
  window.onCaptchaLoginChange  = (token) => setCaptchaToken(token);
  window.onCaptchaLoginExpired = () => setCaptchaToken(null);

  // Forzar render del captcha si ya cargó el script
  if (window.grecaptcha && window.grecaptcha.render) {
    setTimeout(() => {
      const container = document.querySelector(".g-recaptcha");
      if (container && !container.hasChildNodes()) {
        window.grecaptcha.render(container, {
          sitekey: "6LcvEOssAAAAAI_B_DqnrGo2_s_XpZ99SXdAJox2",
          callback: window.onCaptchaLoginChange,
          "expired-callback": window.onCaptchaLoginExpired,
        });
      }
    }, 100);
  }

  return () => {
    delete window.onCaptchaLoginChange;
    delete window.onCaptchaLoginExpired;
  };
}, []);

  const onSubmit = async (data) => {
   // if (!captchaToken) {
   //   toast.error("Por favor completa el captcha");
   //   return;
   // }
    setLoading(true);
    try {
      const res = await login({ ...data, captchaToken });
      setAuth(res.data.token, res.data);
      toast.success(`Bienvenido, ${res.data.nombre}!`);
      switch (res.data.rol) {
        case "admin":   navigate("/admin");   break;
        case "groomer": navigate("/groomer"); break;
        case "recepcion": navigate("/recepcion"); break;
        default:        navigate("/cliente");
      }
    } catch (err) {
      toast.error(err.response?.data?.message || "Credenciales inválidas");
      if (window.grecaptcha) window.grecaptcha.reset();
      setCaptchaToken(null);
    } finally {
      setLoading(false);
    }
  };

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
          <h1 className="text-2xl font-bold text-gray-900 mt-6">Iniciar sesión</h1>
          <p className="text-gray-500 text-sm mt-1">Bienvenido de vuelta 🐾</p>
        </div>

        <div className="bg-white rounded-2xl shadow-sm border border-gray-100 p-8">
          <form onSubmit={handleSubmit(onSubmit)} className="space-y-5">

            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">
                Correo electrónico
              </label>
              <input
                {...register("correo")}
                type="email"
                placeholder="tu@correo.com"
                className="w-full px-4 py-2.5 rounded-xl border border-gray-200 focus:outline-none focus:ring-2 focus:ring-purple-300 text-sm"
              />
              {errors.correo && <p className="text-red-500 text-xs mt-1">{errors.correo.message}</p>}
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">
                Contraseña
              </label>
              <div className="relative">
                <input
                  {...register("password")}
                  type={showPassword ? "text" : "password"}
                  placeholder="••••••••"
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
              {errors.password && <p className="text-red-500 text-xs mt-1">{errors.password.message}</p>}
              <div className="flex justify-end mt-1">
                <Link to="/olvide-password" className="text-xs text-purple-600 hover:underline">
                  ¿Olvidaste tu contraseña?
                </Link>
              </div>
            </div>

            {/* Captcha */}
            <div className="flex justify-center">
              <div
                className="g-recaptcha"
                data-sitekey="6LcvEOssAAAAAI_B_DqnrGo2_s_XpZ99SXdAJox2"
                data-callback="onCaptchaLoginChange"
                data-expired-callback="onCaptchaLoginExpired"
              />
            </div>

            <button
  type="submit"
  disabled={loading}
  className="w-full py-3 rounded-xl bg-gradient-to-r from-purple-500 to-purple-600 text-white font-medium hover:from-purple-600 hover:to-purple-700 transition-all shadow-sm disabled:opacity-60 flex items-center justify-center gap-2"
>
              {loading && <Loader2 className="w-4 h-4 animate-spin" />}
              {loading ? "Ingresando..." : "Iniciar sesión"}
            </button>

          </form>

          <p className="text-center text-sm text-gray-500 mt-6">
            ¿No tienes cuenta?{" "}
            <Link to="/register" className="text-purple-600 font-medium hover:underline">
              Regístrate
            </Link>
          </p>
        </div>
      </div>
    </div>
  );
};

export default LoginPage;
import { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { z } from "zod";
import toast from "react-hot-toast";
import { Heart, Eye, EyeOff, Loader2, CheckCircle } from "lucide-react";
import { register as registerApi } from "../../api/authApi";

const schema = z.object({
  nombre:    z.string().min(2, "Mínimo 2 caracteres").max(100),
  apellido:  z.string().min(2, "Mínimo 2 caracteres").max(100),
  correo:    z.string().email("Correo inválido"),
  password:  z.string()
    .min(8, "Mínimo 8 caracteres")
    .regex(/[A-Z]/, "Debe tener al menos una mayúscula")
    .regex(/[a-z]/, "Debe tener al menos una minúscula")
    .regex(/[0-9]/, "Debe tener al menos un número")
    .regex(/[*#!@$%^&()]/, "Debe tener al menos un símbolo (*#!@$%^&())"),
  telefono:  z.string().max(20).optional(),
  direccion: z.string().max(500).optional(),
  ciudad:    z.string().max(100).optional(),
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

const RegisterPage = () => {
  const [showPassword, setShowPassword] = useState(false);
  const [loading, setLoading]           = useState(false);
  const [registered, setRegistered]     = useState(false);
  const [password, setPassword]         = useState("");
  const [captchaToken, setCaptchaToken] = useState(null);

  const { register, handleSubmit, formState: { errors } } = useForm({
    resolver: zodResolver(schema),
  });

  const strength = getPasswordStrength(password);

  useEffect(() => {
  window.onCaptchaRegisterChange  = (token) => setCaptchaToken(token);
  window.onCaptchaRegisterExpired = () => setCaptchaToken(null);

  if (window.grecaptcha && window.grecaptcha.render) {
    setTimeout(() => {
      const container = document.querySelector(".g-recaptcha");
      if (container && !container.hasChildNodes()) {
        window.grecaptcha.render(container, {
          sitekey: "6LcvEOssAAAAAI_B_DqnrGo2_s_XpZ99SXdAJox2",
          callback: window.onCaptchaRegisterChange,
          "expired-callback": window.onCaptchaRegisterExpired,
        });
      }
    }, 100);
  }

  return () => {
    delete window.onCaptchaRegisterChange;
    delete window.onCaptchaRegisterExpired;
  };
}, []);

  const onSubmit = async (data) => {
    if (!captchaToken) {
      toast.error("Por favor completa el captcha");
      return;
    }
    setLoading(true);
    try {
      await registerApi({ ...data, captchaToken });
      setRegistered(true);
    } catch (err) {
      toast.error(err.response?.data?.message || "Error al registrarse");
      if (window.grecaptcha) window.grecaptcha.reset();
      setCaptchaToken(null);
    } finally {
      setLoading(false);
    }
  };

  if (registered) {
    return (
      <div className="min-h-screen bg-gradient-to-br from-purple-50 via-white to-green-50 flex items-center justify-center p-4">
        <div className="bg-white rounded-2xl shadow-sm border border-gray-100 p-10 max-w-md w-full text-center">
          <div className="w-16 h-16 bg-green-100 rounded-full flex items-center justify-center mx-auto mb-4">
            <CheckCircle className="w-8 h-8 text-green-500" />
          </div>
          <h2 className="text-2xl font-bold text-gray-900 mb-2">¡Revisa tu correo!</h2>
          <p className="text-gray-500 text-sm mb-6">
            Te enviamos un link de activación. Tienes <strong>15 minutos</strong> para activar tu cuenta.
          </p>
          <Link
            to="/login"
            className="inline-block px-6 py-3 rounded-xl bg-gradient-to-r from-purple-500 to-purple-600 text-white font-medium text-sm"
          >
            Ir al login
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
          <h1 className="text-2xl font-bold text-gray-900 mt-6">Crear cuenta</h1>
          <p className="text-gray-500 text-sm mt-1">Solo para dueños de mascotas 🐾</p>
        </div>

        <div className="bg-white rounded-2xl shadow-sm border border-gray-100 p-8">
          <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">

            <div className="grid grid-cols-2 gap-3">
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">Nombre</label>
                <input
                  {...register("nombre")}
                  placeholder="Juan"
                  className="w-full px-4 py-2.5 rounded-xl border border-gray-200 focus:outline-none focus:ring-2 focus:ring-purple-300 text-sm"
                />
                {errors.nombre && <p className="text-red-500 text-xs mt-1">{errors.nombre.message}</p>}
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">Apellido</label>
                <input
                  {...register("apellido")}
                  placeholder="Pérez"
                  className="w-full px-4 py-2.5 rounded-xl border border-gray-200 focus:outline-none focus:ring-2 focus:ring-purple-300 text-sm"
                />
                {errors.apellido && <p className="text-red-500 text-xs mt-1">{errors.apellido.message}</p>}
              </div>
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">Correo electrónico</label>
              <input
                {...register("correo")}
                type="email"
                placeholder="tu@correo.com"
                className="w-full px-4 py-2.5 rounded-xl border border-gray-200 focus:outline-none focus:ring-2 focus:ring-purple-300 text-sm"
              />
              {errors.correo && <p className="text-red-500 text-xs mt-1">{errors.correo.message}</p>}
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">Contraseña</label>
              <div className="relative">
                <input
                  {...register("password")}
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
              {errors.password && <p className="text-red-500 text-xs mt-1">{errors.password.message}</p>}
              <p className="text-xs text-gray-400 mt-1">
                Mínimo 8 caracteres, mayúsculas, minúsculas, números y símbolos
              </p>
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">
                Teléfono <span className="text-gray-400">(opcional)</span>
              </label>
              <input
                {...register("telefono")}
                placeholder="+591 76543210"
                className="w-full px-4 py-2.5 rounded-xl border border-gray-200 focus:outline-none focus:ring-2 focus:ring-purple-300 text-sm"
              />
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">
                Ciudad <span className="text-gray-400">(opcional)</span>
              </label>
              <input
                {...register("ciudad")}
                placeholder="La Paz"
                className="w-full px-4 py-2.5 rounded-xl border border-gray-200 focus:outline-none focus:ring-2 focus:ring-purple-300 text-sm"
              />
            </div>

            {/* Captcha */}
            <div className="flex justify-center">
              <div
                className="g-recaptcha"
                data-sitekey="6LcvEOssAAAAAI_B_DqnrGo2_s_XpZ99SXdAJox2"
                data-callback="onCaptchaRegisterChange"
                data-expired-callback="onCaptchaRegisterExpired"
              />
            </div>

            <button
              type="submit"
              disabled={loading || !captchaToken}
              className="w-full py-3 rounded-xl bg-gradient-to-r from-purple-500 to-purple-600 text-white font-medium hover:from-purple-600 hover:to-purple-700 transition-all shadow-sm disabled:opacity-60 flex items-center justify-center gap-2 mt-2"
            >
              {loading && <Loader2 className="w-4 h-4 animate-spin" />}
              {loading ? "Creando cuenta..." : "Crear cuenta"}
            </button>

          </form>

          <p className="text-center text-sm text-gray-500 mt-6">
            ¿Ya tienes cuenta?{" "}
            <Link to="/login" className="text-purple-600 font-medium hover:underline">
              Inicia sesión
            </Link>
          </p>
        </div>
      </div>
    </div>
  );
};

export default RegisterPage;
import { useState } from "react";
import { Link } from "react-router-dom";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { z } from "zod";
import toast from "react-hot-toast";
import { Heart, Loader2, Mail, CheckCircle, ArrowLeft } from "lucide-react";
import { solicitarCambioPassword } from "../../api/authApi";

const schema = z.object({
  correo: z.string().email("Correo inválido"),
});

const OlvidePasswordPage = () => {
  const [loading, setLoading]   = useState(false);
  const [enviado, setEnviado]   = useState(false);

  const { register, handleSubmit, formState: { errors } } = useForm({
    resolver: zodResolver(schema),
  });

  const onSubmit = async (data) => {
    setLoading(true);
    try {
      await solicitarCambioPassword(data);
      setEnviado(true);
    } catch (err) {
      toast.error(err.response?.data?.message || "Error al enviar el correo");
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
        </div>

        <div className="bg-white rounded-2xl shadow-sm border border-gray-100 p-8">
          {enviado ? (
            <div className="text-center">
              <div className="w-16 h-16 bg-green-100 rounded-full flex items-center justify-center mx-auto mb-4">
                <CheckCircle className="w-8 h-8 text-green-500" />
              </div>
              <h2 className="text-xl font-bold text-gray-900 mb-2">¡Correo enviado!</h2>
              <p className="text-gray-500 text-sm mb-6">
                Revisa tu bandeja de entrada. El link expira en <strong>15 minutos</strong>.
              </p>
              <Link
                to="/login"
                className="inline-flex items-center gap-2 text-sm text-purple-600 hover:underline"
              >
                <ArrowLeft className="w-4 h-4" />
                Volver al login
              </Link>
            </div>
          ) : (
            <>
              <div className="text-center mb-6">
                <div className="w-12 h-12 bg-purple-50 rounded-xl flex items-center justify-center mx-auto mb-3">
                  <Mail className="w-6 h-6 text-purple-500" />
                </div>
                <h1 className="text-xl font-bold text-gray-900">¿Olvidaste tu contraseña?</h1>
                <p className="text-gray-500 text-sm mt-1">
                  Ingresa tu correo y te enviaremos un link para cambiarla.
                </p>
              </div>

              <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
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
                  {errors.correo && (
                    <p className="text-red-500 text-xs mt-1">{errors.correo.message}</p>
                  )}
                </div>

                <button
                  type="submit"
                  disabled={loading}
                  className="w-full py-3 rounded-xl bg-gradient-to-r from-purple-500 to-purple-600 text-white font-medium hover:from-purple-600 hover:to-purple-700 transition-all disabled:opacity-60 flex items-center justify-center gap-2"
                >
                  {loading && <Loader2 className="w-4 h-4 animate-spin" />}
                  {loading ? "Enviando..." : "Enviar link"}
                </button>
              </form>

              <p className="text-center text-sm text-gray-500 mt-6">
                <Link to="/login" className="inline-flex items-center gap-1 text-purple-600 hover:underline">
                  <ArrowLeft className="w-3.5 h-3.5" />
                  Volver al login
                </Link>
              </p>
            </>
          )}
        </div>
      </div>
    </div>
  );
};

export default OlvidePasswordPage;
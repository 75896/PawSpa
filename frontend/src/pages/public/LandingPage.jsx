import { Link } from "react-router-dom";
import {
  Scissors, Heart, MapPin,
  Phone, Mail, ChevronRight,
  Sparkles, Shield, Clock
} from "lucide-react";
const servicios = [
  {
    icon: <Scissors className="w-7 h-7" />,
    titulo: "Baño & Corte",
    descripcion: "Servicio completo de higiene con productos premium para el cuidado del pelaje.",
    precio: "Desde Bs. 80",
    color: "bg-purple-50 text-purple-600",
  },
  {
    icon: <Sparkles className="w-7 h-7" />,
    titulo: "Spa Completo",
    descripcion: "Baño, corte, uñas, oídos, glándulas y perfume. El lujo que tu mascota merece.",
    precio: "Desde Bs. 150",
    color: "bg-green-50 text-green-600",
  },
  {
    icon: <Heart className="w-7 h-7" />,
    titulo: "Baño Rápido",
    descripcion: "Baño express para mantener a tu mascota fresca y limpia en tiempo récord.",
    precio: "Desde Bs. 50",
    color: "bg-pink-50 text-pink-500",
  },
  {
    icon: <Shield className="w-7 h-7" />,
    titulo: "Cuidado Especial",
    descripcion: "Atención personalizada para mascotas con necesidades especiales.",
    precio: "Desde Bs. 100",
    color: "bg-amber-50 text-amber-600",
  },
];

const galeria = [
  { src: "https://images.unsplash.com/photo-1587300003388-59208cc962cb?w=400&h=300&fit=crop", alt: "Perro después de baño" },
  { src: "https://images.unsplash.com/photo-1583511655857-d19b40a7a54e?w=400&h=300&fit=crop", alt: "Grooming profesional" },
  { src: "https://images.unsplash.com/photo-1548199973-03cce0bbc87b?w=400&h=300&fit=crop", alt: "Mascotas felices" },
  { src: "https://images.unsplash.com/photo-1601758124510-52d02ddb7cbd?w=400&h=300&fit=crop", alt: "Spa para mascotas" },
  { src: "https://images.unsplash.com/photo-1537151608828-ea2b11777ee8?w=400&h=300&fit=crop", alt: "Perro limpio" },
  { src: "https://images.unsplash.com/photo-1534361960057-19f4434a4b49?w=400&h=300&fit=crop", alt: "Mascota feliz" },
];

const LandingPage = () => {
  return (
    <div className="min-h-screen bg-white">

      {/* NAVBAR */}
      <nav className="sticky top-0 z-50 bg-white/80 backdrop-blur-md border-b border-gray-100">
        <div className="max-w-6xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex items-center justify-between h-16">
            <Link to="/" className="flex items-center gap-2">
              <div className="w-8 h-8 rounded-lg bg-gradient-to-br from-purple-500 to-green-500 flex items-center justify-center">
                <Heart className="w-4 h-4 text-white" />
              </div>
              <span className="text-xl font-semibold text-gray-900">Paw<span className="bg-gradient-to-r from-purple-600 to-green-500 bg-clip-text text-transparent">Spa</span></span>
            </Link>
            <div className="hidden md:flex items-center gap-8">
              <a href="#servicios" className="text-sm text-gray-600 hover:text-purple-600 transition-colors">Servicios</a>
              <a href="#galeria"   className="text-sm text-gray-600 hover:text-purple-600 transition-colors">Galería</a>
              <a href="#contacto"  className="text-sm text-gray-600 hover:text-purple-600 transition-colors">Contacto</a>
            </div>
            <div className="flex items-center gap-3">
              <Link to="/login" className="text-sm font-medium text-gray-700 hover:text-purple-600 transition-colors px-4 py-2 rounded-lg hover:bg-purple-50">
                Iniciar sesión
              </Link>
              <Link to="/register" className="text-sm font-medium text-white px-4 py-2 rounded-lg bg-gradient-to-r from-purple-500 to-purple-600 hover:from-purple-600 hover:to-purple-700 transition-all shadow-sm">
                Registrarse
              </Link>
            </div>
          </div>
        </div>
      </nav>

      {/* HERO */}
      <section className="relative overflow-hidden bg-gradient-to-br from-purple-50 via-white to-green-50 py-20 md:py-32">
        <div className="absolute inset-0 opacity-30">
          <div className="absolute top-10 left-10 w-72 h-72 bg-purple-200 rounded-full blur-3xl" />
          <div className="absolute bottom-10 right-10 w-72 h-72 bg-green-200 rounded-full blur-3xl" />
        </div>
        <div className="relative max-w-6xl mx-auto px-4 sm:px-6 lg:px-8 text-center">
          <div className="inline-flex items-center gap-2 bg-white border border-purple-100 rounded-full px-4 py-1.5 mb-6 shadow-sm">
            <Sparkles className="w-4 h-4 text-purple-500" />
            <span className="text-sm text-purple-700 font-medium">El mejor cuidado para tu mascota</span>
          </div>
          <h1 className="text-4xl md:text-6xl font-bold text-gray-900 mb-6 leading-tight">
            Tu mascota merece<br />
            <span className="bg-gradient-to-r from-purple-600 to-green-500 bg-clip-text text-transparent">sentirse especial</span>
          </h1>
          <p className="text-lg md:text-xl text-gray-500 max-w-2xl mx-auto mb-10">
            En PawSpa ofrecemos servicios de grooming profesional con amor y dedicación.
            Agenda una cita y dale el spa que se merece.
          </p>
          <div className="flex flex-col sm:flex-row gap-4 justify-center">
            <Link to="/login" className="inline-flex items-center gap-2 px-8 py-3.5 rounded-xl bg-gradient-to-r from-purple-500 to-purple-600 text-white font-medium shadow-lg hover:shadow-xl transition-all">
  Agendar cita
  <ChevronRight className="w-4 h-4" />
</Link>
            <a href="#servicios" className="inline-flex items-center gap-2 px-8 py-3.5 rounded-xl border border-gray-200 text-gray-700 font-medium hover:bg-gray-50 transition-all">
              Ver servicios
            </a>
          </div>
          <div className="mt-16 grid grid-cols-3 gap-6 max-w-lg mx-auto">
            {[
              { valor: "500+", label: "Mascotas atendidas" },
              { valor: "4.9★", label: "Calificación" },
              { valor: "3 años", label: "De experiencia" },
            ].map((stat, i) => (
              <div key={i} className="text-center">
                <div className="text-2xl font-bold text-gray-900">{stat.valor}</div>
                <div className="text-xs text-gray-500 mt-1">{stat.label}</div>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* SERVICIOS */}
      <section id="servicios" className="py-20 bg-white">
        <div className="max-w-6xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="text-center mb-14">
            <h2 className="text-3xl md:text-4xl font-bold text-gray-900 mb-4">Nuestros servicios</h2>
            <p className="text-gray-500 max-w-xl mx-auto">Cada servicio está diseñado para el bienestar de tu mascota, con productos seguros y personal capacitado.</p>
          </div>
          <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6">
            {servicios.map((s, i) => (
              <div key={i} className="group p-6 rounded-2xl border border-gray-100 hover:border-purple-200 hover:shadow-lg transition-all cursor-pointer">
                <div className={`w-14 h-14 rounded-xl ${s.color} flex items-center justify-center mb-4 group-hover:scale-110 transition-transform`}>
                  {s.icon}
                </div>
                <h3 className="font-semibold text-gray-900 mb-2">{s.titulo}</h3>
                <p className="text-sm text-gray-500 mb-4 leading-relaxed">{s.descripcion}</p>
                <span className="text-sm font-medium text-purple-600">{s.precio}</span>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* GALERÍA */}
      <section id="galeria" className="py-20 bg-gray-50">
        <div className="max-w-6xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="text-center mb-14">
            <h2 className="text-3xl md:text-4xl font-bold text-gray-900 mb-4">Galería</h2>
            <p className="text-gray-500 max-w-xl mx-auto">Momentos que capturan la felicidad de nuestros clientes peludos.</p>
          </div>
          <div className="grid grid-cols-2 md:grid-cols-3 gap-4">
            {galeria.map((img, i) => (
              <div key={i} className="overflow-hidden rounded-2xl aspect-square bg-gray-200 group cursor-pointer">
                <img src={img.src} alt={img.alt} className="w-full h-full object-cover group-hover:scale-105 transition-transform duration-500" />
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* POR QUÉ NOSOTROS */}
      <section className="py-20 bg-white">
        <div className="max-w-6xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="grid md:grid-cols-3 gap-8 text-center">
            {[
              { icon: <Heart className="w-8 h-8" />,  titulo: "Con amor",             desc: "Cada mascota es tratada como si fuera nuestra propia.",          color: "text-pink-500 bg-pink-50"     },
              { icon: <Shield className="w-8 h-8" />, titulo: "Seguridad garantizada", desc: "Productos hipoalergénicos y técnicas sin estrés.",               color: "text-purple-600 bg-purple-50" },
              { icon: <Clock className="w-8 h-8" />,  titulo: "Puntualidad",           desc: "Respetamos tu tiempo con citas organizadas al detalle.",         color: "text-green-600 bg-green-50"   },
            ].map((item, i) => (
              <div key={i} className="flex flex-col items-center gap-4">
                <div className={`w-16 h-16 rounded-2xl ${item.color} flex items-center justify-center`}>{item.icon}</div>
                <h3 className="font-semibold text-gray-900">{item.titulo}</h3>
                <p className="text-sm text-gray-500">{item.desc}</p>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* CONTACTO */}
      <section id="contacto" className="py-20 bg-gradient-to-br from-purple-50 to-green-50">
        <div className="max-w-6xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="text-center mb-14">
            <h2 className="text-3xl md:text-4xl font-bold text-gray-900 mb-4">Contáctanos</h2>
            <p className="text-gray-500 max-w-xl mx-auto">Estamos aquí para responder tus preguntas y ayudarte a agendar.</p>
          </div>
          <div className="grid md:grid-cols-3 gap-6 max-w-3xl mx-auto">
            {[
              { icon: <MapPin className="w-5 h-5" />, titulo: "Ubicación", info: "Av. Las Mascotas 123, La Paz", color: "text-purple-600 bg-purple-50" },
              { icon: <Phone className="w-5 h-5" />,  titulo: "Teléfono",  info: "+591 76543210",               color: "text-green-600 bg-green-50"   },
              { icon: <Mail className="w-5 h-5" />,   titulo: "Correo",    info: "hola@pawspa.bo",              color: "text-pink-500 bg-pink-50"     },
            ].map((item, i) => (
              <div key={i} className="bg-white rounded-2xl p-6 shadow-sm border border-gray-100 text-center">
                <div className={`w-12 h-12 rounded-xl ${item.color} flex items-center justify-center mx-auto mb-4`}>{item.icon}</div>
                <h3 className="font-medium text-gray-900 mb-1">{item.titulo}</h3>
                <p className="text-sm text-gray-500">{item.info}</p>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* FOOTER */}
      <footer className="bg-gray-900 text-white py-10">
        <div className="max-w-6xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex flex-col md:flex-row items-center justify-between gap-4">
            <div className="flex items-center gap-2">
              <div className="w-7 h-7 rounded-lg bg-gradient-to-br from-purple-500 to-green-500 flex items-center justify-center">
                <Heart className="w-3.5 h-3.5 text-white" />
              </div>
              <span className="font-semibold">PawSpa</span>
            </div>
            <p className="text-sm text-gray-400">© 2025 PawSpa. Todos los derechos reservados.</p>
            <div className="flex items-center gap-4">
              <a href="#" className="text-gray-400 hover:text-white transition-colors">
  <span className="text-sm">Instagram</span>
</a>
              <Link to="/login"    className="text-sm text-gray-400 hover:text-white transition-colors">Iniciar sesión</Link>
              <Link to="/register" className="text-sm text-gray-400 hover:text-white transition-colors">Registrarse</Link>
            </div>
          </div>
        </div>
      </footer>

    </div>
  );
};

export default LandingPage;
/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,jsx,ts,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        primary: {
          50:  "#f3f0ff",
          100: "#e9e3ff",
          200: "#d4c9ff",
          300: "#b8a5ff",
          400: "#9878ff",
          500: "#7c4dff",
          600: "#6b35f5",
          700: "#5b23e1",
          800: "#4b1dbd",
          900: "#3d1899",
        },
        mint: {
          50:  "#f0fdf8",
          100: "#ccfbec",
          200: "#99f5d8",
          300: "#5de8c0",
          400: "#2dd4a4",
          500: "#0fb98a",
          600: "#059470",
          700: "#04775a",
          800: "#065f48",
          900: "#054f3c",
        },
      },
    },
  },
  plugins: [],
}
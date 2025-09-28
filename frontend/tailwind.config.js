/** @type {import('tailwindcss').Config} */
module.exports = {
  content: ["./src/**/*.{js,jsx,ts,tsx}"],
  theme: {
    extend: {
      colors: {
        primary: {
          DEFAULT: "#F2EAD7",
        },
        secondary: {
          tint10: "#96be83",
          tint20: "#a1c591",
          tint30: "#adcd9e",
          DEFAULT: "#8AB775",
          dark20: "#6e925e",
          dark30: "#618052",
          dark60: "#37492f",
          dark70: "#293723",
          dark80: "#1c2517",
          dark90: "#0e120c",
        },
        tertiary: {
          tint: "#EC7D38",
          DEFAULT: "#EA6F22",
        },
      },
    },
  },
  plugins: [],
};

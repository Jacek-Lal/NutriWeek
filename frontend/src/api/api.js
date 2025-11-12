import axios from "axios";

export const ENDPOINTS = {
  AUTH: "/auth",
  MENUS: "/menus",
  MEALS: "/meals",
  PRODUCTS: "/products",
};

const api = axios.create({
  baseURL: process.env.REACT_APP_API_URL,
  withCredentials: true,
});

function getCsrfToken() {
  const match = document.cookie.match(new RegExp("(^| )XSRF-TOKEN=([^;]+)"));
  return match ? decodeURIComponent(match[2]) : null;
}

api.interceptors.request.use((config) => {
  const token = getCsrfToken();
  console.log("CSRF token detected:", token);

  if (token) {
    config.headers["X-XSRF-TOKEN"] = token;
  }

  return config;
});

export default api;

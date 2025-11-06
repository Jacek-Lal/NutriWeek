import axios from "axios";

export const ENDPOINTS = {
  AUTH: "/auth",
  MENUS: "/menus",
  MEALS: "/meals",
  PRODUCTS: "/products",
};

const api = axios.create({
  baseURL: "http://localhost:8080",
  withCredentials: true,
});

function getCsrfToken() {
  const match = document.cookie.match(new RegExp("(^| )XSRF-TOKEN=([^;]+)"));
  return match ? decodeURIComponent(match[2]) : null;
}

api.interceptors.request.use((config) => {
  const token = getCsrfToken();
  if (token) {
    config.headers["X-XSRF-TOKEN"] = token;
  }

  return config;
});

export default api;

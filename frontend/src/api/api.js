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


let csrfToken = null;

export function setCsrfToken(token) {
  csrfToken = token;
  api.defaults.headers["X-XSRF-TOKEN"] = token;
}

api.interceptors.request.use((config) => {
  if (csrfToken) {
    config.headers["X-XSRF-TOKEN"] = csrfToken;
  }

  return config;
});

export default api;

import api, { ENDPOINTS } from "./api";

const ENDPOINT = ENDPOINTS.AUTH;

export const getCsrf = () => api.get(`${ENDPOINT}/csrf`);
export const registerUser = (payload) =>
  api.post(`${ENDPOINT}/register`, payload);
export const loginUser = (payload) => api.post(`${ENDPOINT}/login`, payload);
export const logoutUser = () => api.post(`${ENDPOINT}/logout`, {});
export const getUser = () => api.get(`${ENDPOINT}/me`);

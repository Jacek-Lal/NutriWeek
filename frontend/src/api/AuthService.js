import api, { ENDPOINTS, setCsrfToken } from "./api";

const ENDPOINT = ENDPOINTS.AUTH;

export const getCsrf = async () => {
  const { data } = await api.get(`${ENDPOINT}/csrf`);
  setCsrfToken(data.token);
};
export const registerUser = async (payload) => {
  await getCsrf();
  return api.post(`${ENDPOINT}/register`, payload);
};
export const loginUser = (payload) => api.post(`${ENDPOINT}/login`, payload);
export const loginDemoUser = async () => {
  await getCsrf();
  return api.post(`${ENDPOINT}/demo`);
};
export const logoutUser = () => api.post(`${ENDPOINT}/logout`, {});
export const getUser = () => api.get(`${ENDPOINT}/me`);

import axios from "axios";

const API_URL = "http://localhost:8080/auth";

axios.defaults.withCredentials = true;

export async function registerUser(payload) {
  return await axios.post(`${API_URL}/register`, payload);
}

export async function loginUser(payload) {
  return await axios.post(`${API_URL}/login`, payload, {
    withCredentials: true,
  });
}

export async function logoutUser() {
  axios.post(`${API_URL}/logout`, {}, { withCredentials: true });
}

export async function getUser() {
  return axios.get(`${API_URL}/me`, { withCredentials: true });
}

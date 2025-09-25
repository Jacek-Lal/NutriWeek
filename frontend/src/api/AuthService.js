import axios from "axios";

const API_URL = "http://localhost:8080/auth";

export async function registerUser(payload) {
  return await axios.post(`${API_URL}/register`, payload);
}
export async function loginUser(payload) {
  return await axios.post(`${API_URL}/login`, payload);
}

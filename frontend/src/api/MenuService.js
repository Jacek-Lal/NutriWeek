import axios from "axios";

const API_URL = "http://localhost:8080/menus";

export async function addMenu(menu) {
  return await axios.post(API_URL, menu);
}

export async function getMenu(id) {
  return await axios.get(`${API_URL}/${id}`);
}

export async function getMenus() {
  return await axios.get(API_URL);
}

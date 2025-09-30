import axios from "axios";

const API_URL = "http://localhost:8080/menus";
axios.defaults.withCredentials = true;

export async function addMenu(menu) {
  return await axios.post(API_URL, menu);
}

export async function getMenu(id) {
  return await axios.get(`${API_URL}/${id}`);
}

export async function getMenus(page, size) {
  return await axios.get(`${API_URL}?page=${page}&size=${size}`);
}

export async function deleteMenu(id) {
  return await axios.delete(`${API_URL}/${id}`);
}

export async function getMenuMeals(id, page, size) {
  return await axios.get(`${API_URL}/${id}/meals?page=${page}&size=${size}`);
}

export async function addMenuMeal(id, payload) {
  return await axios.post(`${API_URL}/${id}/meals`, payload);
}

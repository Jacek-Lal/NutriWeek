import api, { ENDPOINTS } from "./api";

const ENDPOINT = ENDPOINTS.MENUS;

export const addMenu = (menu) => api.post(`${ENDPOINT}`, menu);
export const getMenu = (id) => api.get(`${ENDPOINT}/${id}`);
export const deleteMenu = (id) => api.delete(`${ENDPOINT}/${id}`);

export const getMenus = (page, size) =>
  api.get(`${ENDPOINT}?page=${page}&size=${size}`);

export const getMenuMeals = (id, page, size) =>
  api.get(`${ENDPOINT}/${id}/meals?page=${page}&size=${size}`);

export const addMenuMeal = (id, payload) =>
  api.post(`${ENDPOINT}/${id}/meals`, payload);

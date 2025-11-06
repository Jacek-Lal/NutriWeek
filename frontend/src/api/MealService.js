import api, { ENDPOINTS } from "./api";

const ENDPOINT = ENDPOINTS.MEALS;

export const getRecentProducts = (limit = 5) =>
  api.get(`${ENDPOINT}/recent`, { params: { limit: limit } });

export const addMealItems = (mealId, mealItems) =>
  api.put(`${ENDPOINT}/${mealId}/items`, mealItems);

export const deleteMeal = (mealId) => api.delete(`${ENDPOINT}/${mealId}`);

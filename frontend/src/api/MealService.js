import axios from "axios";

const API_URL = "http://localhost:8080/meals";

export async function addMeal(meal) {
  return await axios.post(API_URL, meal);
}

export async function getRecentProducts(limit = 5) {
  return await axios.get(`${API_URL}/recent`, { params: { limit: limit } });
}

export async function addMealItems(mealId, mealItems) {
  return await axios.put(`${API_URL}/${mealId}/items`, mealItems);
}

export async function deleteMeal(mealId) {
  return await axios.delete(`${API_URL}/${mealId}`);
}

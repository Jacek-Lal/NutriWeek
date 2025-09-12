import axios from "axios";

const API_URL = "http://localhost:8080/meals";

export async function addMeal(meal) {
  return await axios.post(API_URL, meal);
}

export async function getRecentProducts(limit) {
  return await axios.get(`${API_URL}/recent`, { params: { limit: limit } });
}

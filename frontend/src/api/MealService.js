import axios from "axios";

const API_URL = "http://localhost:8080/meals";

export async function addMeal(meal) {
  return await axios.post(API_URL, meal);
}

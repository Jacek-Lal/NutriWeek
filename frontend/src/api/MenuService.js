import axios from "axios";

const API_URL = "http://localhost:8080/menus";

export async function addMenu(menu) {
  return await axios.post(API_URL, menu);
}

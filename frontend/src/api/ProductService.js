import axios from "axios";

const API_URL = "http://localhost:8080/products";

export async function searchProducts(query, page = 0, size = 10) {
  return await axios.get(
    `${API_URL}?query="${query}"&page=${page}&size=${size}`
  );
}

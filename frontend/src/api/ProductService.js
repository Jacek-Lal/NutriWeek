import api, { ENDPOINTS } from "./api";

export const searchProducts = (query, page = 0, size = 10) =>
  api.get(`${ENDPOINTS.PRODUCTS}?query="${query}"&page=${page}&size=${size}`);

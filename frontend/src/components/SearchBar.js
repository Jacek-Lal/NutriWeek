import React, { useState } from "react";
import { searchProducts } from "../api/ProductService";

const SearchBar = ({ setData }) => {
  const [input, setInput] = useState("");

  const getProducts = async (query, page = 0, size = 10) => {
    try {
      const response = await searchProducts(query, page, size);

      setData(response.data);
      console.log(response.data);
    } catch (error) {
      console.error(error);
    }
  };

  return (
    <div
      role="search"
      className="grow flex justify-center items-center gap-2 p-6"
    >
      <input
        className="w-1/3 rounded-xl border border-gray-300 px-4 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500 shadow-sm"
        value={input}
        onInput={(e) => setInput(e.target.value)}
      ></input>
      <button
        className="flex items-center gap-2 rounded-xl bg-blue-600 px-4 py-2 text-white shadow-md hover:bg-blue-700 active:scale-95 transition"
        onClick={() => getProducts(input)}
      >
        <i className="bi bi-search"></i> Search
      </button>
    </div>
  );
};

export default SearchBar;

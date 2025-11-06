import React from "react";
import { searchProducts } from "api";

const SearchBar = ({ input, setInput, setData }) => {
  const getProducts = async (query, page = 0, size = 50) => {
    try {
      const { data } = await searchProducts(query, page, size);

      setData(data?.foods);
    } catch (error) {
      console.error(error);
    }
  };
  return (
    <div
      role="search"
      className="w-full flex flex-col sm:flex-row justify-center items-center gap-3 sm:gap-4 p-4 bg-white"
    >
      <input
        className="w-full sm:w-2/3 md:w-1/2 rounded-xl border border-gray-300 bg-gray-50 px-4 py-2 text-gray-800 placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-green-400 focus:border-transparent shadow-sm transition-all duration-200"
        value={input}
        placeholder="Search for products..."
        onInput={(e) => setInput(e.target.value)}
      />

      <button
        type="button"
        onClick={() => getProducts(input)}
        className="flex items-center justify-center gap-2 rounded-xl bg-green-500 px-5 py-2.5 text-white font-medium shadow-md hover:bg-green-600 active:scale-95 transition-all duration-200"
      >
        <i className="bi bi-search"></i>
        Search
      </button>
    </div>
  );
};

export default SearchBar;

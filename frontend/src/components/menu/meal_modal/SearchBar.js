import { searchProducts } from "api";
import { useState } from "react";
import { TailSpin } from "react-loader-spinner";

const SearchBar = ({ setProducts }) => {
  const [input, setInput] = useState("");
  const [loading, setLoading] = useState(false);

  const getProducts = async (query, page = 0, size = 50) => {
    try {
      setLoading(true);
      const { data } = await searchProducts(query, page, size);
      setLoading(false);

      setProducts(data?.content);
      setInput("");
    } catch (error) {
      console.error(error);
    }
  };

  const handleInput = (e) => {
    const value = e.target.value;
    const cleaned = value.replace(/[^a-zA-Z\s]/g, "");
    setInput(cleaned);
  };

  return (
    <div
      role="search"
      className="w-full flex flex-col sm:flex-row justify-center items-center gap-3 sm:gap-4 p-4 bg-white"
    >
      <input
        className="ml-20 w-full sm:w-2/3 md:w-1/2 rounded-xl border border-gray-300 bg-gray-50 px-4 py-2 text-gray-800 placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-green-400 focus:border-transparent shadow-sm transition-all duration-200"
        value={input}
        placeholder="Search for products..."
        onInput={handleInput}
      />

      <button
        type="button"
        disabled={!input.trim() || loading}
        onClick={() => getProducts(input)}
        className="flex items-center justify-center gap-2 rounded-xl bg-green-500 px-5 py-2.5 text-white font-medium shadow-md hover:bg-green-600 active:scale-95 transition-all duration-200 disabled:bg-gray-300 disabled:cursor-not-allowed"
      >
        {loading ? 
          <TailSpin
            height="20"
            width="20"
            visible={true}
          /> : 
          <>
            <i className="bi bi-search"></i>
            Search
          </>
        }
       
      </button>
      
    </div>
  );
};

export default SearchBar;

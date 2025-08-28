import { useState } from "react";
import { searchProducts } from "./api/ProductService";
import Product from "./components/Product";

function App() {
  const [data, setData] = useState({});
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
    <>
      <div role="search" className="flex justify-center items-center gap-2 p-6">
        <input
          className="w-1/3 rounded-xl border border-gray-300 px-4 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500 shadow-sm"
          value={input}
          onInput={(e) => setInput(e.target.value)}
        ></input>
        <button
          className="flex items-center gap-2 rounded-xl bg-blue-600 px-4 py-2 text-white shadow-md hover:bg-blue-700 active:scale-95 transition"
          onClick={() => getProducts(input)}
        >
          <i className="magnifier"></i> Search
        </button>
      </div>
      <ul className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-6 p-6">
        {data?.foods?.length > 0 &&
          data.foods.map((product) => (
            <Product product={product} key={product.fdcId} />
          ))}
      </ul>
    </>
  );
}

export default App;

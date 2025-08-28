import { useEffect, useState } from "react";
import { searchProducts } from "./api/ProductService";
import Product from "./components/Product";

function App() {
  const [data, setData] = useState({});

  const getProducts = async (query, page = 0, size = 10) => {
    try {
      const response = await searchProducts(query, page, size);

      setData(response.data);
      console.log(response.data);
    } catch (error) {
      console.error(error);
    }
  };

  // useEffect(() => {
  //   getProducts("cheese");
  // }, []);

  return (
    <>
      <div role="search" className="search__wrapper">
        <input className="search-bar"></input>
        <button className="bg-green-500">
          <i className="magnifier"></i> Search
        </button>
      </div>
      <ul>
        {data?.foods?.length > 0 &&
          data.foods.map((product) => (
            <Product product={product} key={product.fdcId} />
          ))}
      </ul>
    </>
  );
}

export default App;

import { useState } from "react";
import SearchBar from "./components/SearchBar";
import ProductGrid from "./components/ProductGrid";
import ProductList from "./components/ProductList";

function App() {
  const [data, setData] = useState({});
  const [list, setList] = useState([]);

  return (
    <>
      <SearchBar setData={setData} />
      <div className="grid grid-cols-3">
        <ProductGrid data={data} setList={setList} />
        <ProductList list={list} setList={setList} />
      </div>
    </>
  );
}

export default App;

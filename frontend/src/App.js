import { useRef, useState } from "react";
import SearchBar from "./components/SearchBar";
import ProductGrid from "./components/ProductGrid";
import ProductList from "./components/ProductList";
import { flushSync } from "react-dom";

function App() {
  const [data, setData] = useState({});
  const [list, setList] = useState([]);
  const modalRef = useRef();

  const toggleModal = (show) => {
    show ? modalRef.current.showModal() : modalRef.current.close();
  };

  return (
    <>
      <button
        className="flex items-center gap-2 rounded-xl bg-blue-600 px-4 py-2 text-white shadow-md hover:bg-blue-700 active:scale-95 transition"
        onClick={() => toggleModal(true)}
      >
        <i className="bi bi-plus-circle"></i> Add product
      </button>
      <dialog ref={modalRef} className="w-3/4 h-4/5">
        <div className="flex justify-between">
          <SearchBar setData={setData} />
          <button
            className="bg-blue-200 p-3"
            onClick={() => toggleModal(false)}
          >
            <i className="bi bi-x"></i>
          </button>
        </div>
        <div className="grid grid-cols-3">
          <ProductGrid data={data} list={list} setList={setList} />
          <ProductList list={list} setList={setList} />
        </div>
      </dialog>
    </>
  );
}

export default App;

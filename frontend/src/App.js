import { useRef, useState } from "react";
import SearchBar from "./components/SearchBar";
import ProductGrid from "./components/ProductGrid";
import ProductList from "./components/ProductList";
import { addMeal, getRecentProducts } from "./api/MealService.js";

function App() {
  const [data, setData] = useState([]);
  const [list, setList] = useState([]);
  const [input, setInput] = useState("");
  const [recentMeals, setRecentMeals] = useState([]);

  const modalRef = useRef();

  const toggleModal = async (show) => {
    show ? modalRef.current.showModal() : modalRef.current.close();
    const response = await getRecentProducts(6);
    setRecentMeals(response.data);
    console.log(response.data);
  };
  const saveMeal = async () => {
    try {
      const request = {
        name: "Breakfast",
        targetKcal: 700,
        targetCarb: 115,
        targetFat: 20,
        targetProtein: 35,
        mealItems: list,
      };

      const response = await addMeal(request);
      console.log(response.data);

      toggleModal(false);
      setData([]);
      setList([]);
      setInput("");
    } catch (error) {
      console.error(error);
    }
  };

  return (
    <>
      <button
        className="flex items-center gap-2 rounded-xl bg-blue-600 px-4 py-2 text-white shadow-md hover:bg-blue-700 active:scale-95 transition"
        onClick={() => toggleModal(true)}
      >
        <i className="bi bi-plus-circle"></i> Add Meal
      </button>
      <dialog ref={modalRef} className="w-3/4 h-4/5 bg-slate-800">
        <div className="flex justify-between">
          <SearchBar input={input} setInput={setInput} setData={setData} />
          <button
            className="bg-blue-200 p-3"
            onClick={() => toggleModal(false)}
          >
            <i className="bi bi-x"></i>
          </button>
        </div>
        <div className="grid grid-cols-3 grid-rows-2">
          <div className="bg-green-100 col-span-2">
            <h1 className="font-medium text-lg p-4">Recent products</h1>
            <ProductGrid data={recentMeals} list={list} setList={setList} />
          </div>
          <ProductGrid data={data} list={list} setList={setList} />
          <ProductList list={list} setList={setList} saveMeal={saveMeal} />
        </div>
      </dialog>
    </>
  );
}

export default App;

import { useState, useEffect } from "react";
import SearchBar from "./SearchBar";
import ProductGrid from "./ProductGrid";
import ProductList from "./ProductList";
import { addMealItems, getRecentProducts } from "../api/MealService.js";

const MealModal = ({ isOpen, onClose, list, setList, mealId }) => {
  const [input, setInput] = useState("");
  const [data, setData] = useState([]);
  const [recentMeals, setRecentMeals] = useState([]);

  useEffect(() => {
    if (isOpen) {
      getRecentProducts(6).then((res) => setRecentMeals(res.data));
    }
  }, [isOpen]);

  const saveMeal = async () => {
    await addMealItems(mealId, list);
    console.log(list);
    onClose();
  };
  return (
    <dialog
      open={isOpen}
      className="w-3/4 h-4/5 bg-slate-800 p-4 overflow-hidden rounded-xl"
    >
      <div className="flex justify-between mb-4">
        <SearchBar input={input} setInput={setInput} setData={setData} />
        <button className="bg-slate-500 p-5 rounded-full" onClick={onClose}>
          <i className="bi bi-x "></i>
        </button>
      </div>

      <div className="grid grid-cols-3 gap-4 h-[calc(100%-4rem)]">
        <div className="col-span-2 flex flex-col gap-4 overflow-y-auto">
          <div className="rounded p-4">
            <h1 className="font-medium text-lg mb-2 text-white">
              Recent products
            </h1>
            <ProductGrid data={recentMeals} list={list} setList={setList} />
          </div>

          <div className="rounded p-4">
            <h1 className="font-medium text-lg mb-2 text-white">
              Search results
            </h1>
            <ProductGrid data={data} list={list} setList={setList} />
          </div>
        </div>

        <div className="rounded p-4 overflow-y-auto">
          <ProductList list={list} setList={setList} saveMeal={saveMeal} />
        </div>
      </div>
    </dialog>
  );
};

export default MealModal;

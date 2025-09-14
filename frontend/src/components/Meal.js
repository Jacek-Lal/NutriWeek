import React from "react";
import { useRef, useState } from "react";
import SearchBar from "./SearchBar";
import ProductGrid from "./ProductGrid";
import ProductList from "./ProductList";
import { addMeal, getRecentProducts } from "../api/MealService.js";

const Meal = ({ name }) => {
  const [data, setData] = useState([]);
  const [list, setList] = useState([]);
  const [input, setInput] = useState("");
  const [recentMeals, setRecentMeals] = useState([]);

  const modalRef = useRef();

  const toggleModal = async (show) => {
    if (show) {
      modalRef.current.showModal();
      const response = await getRecentProducts(6);
      setRecentMeals(response.data);
      console.log(response.data);
    } else {
      modalRef.current.close();
    }
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

      //   const response = await addMeal(request);
      //   console.log(response.data);

      //   setData([]);
      //   setList([]);
      setInput("");
      toggleModal(false);
    } catch (error) {
      console.error(error);
    }
  };
  return (
    <>
      <div className="bg-slate-50 flex flex-col max-w-60 rounded-xl p-3 shadow gap-6">
        <p className="text-center font-semibold">{name}</p>
        <ul className="flex flex-col gap-3">
          {list?.length > 0 &&
            list.map((item) => (
              <li key={item.product.name}>
                <div className="flex gap-6 items-center">
                  <p className="w-40 truncate">{item.product.name}</p>
                  <span className="whitespace-nowrap">{item.amount} g</span>
                </div>
              </li>
            ))}
        </ul>
        <button
          className="inline-flex items-center gap-2 rounded-xl bg-blue-600 px-4 py-2 text-white shadow-md hover:bg-blue-700 active:scale-95 transition whitespace-nowrap"
          onClick={() => toggleModal(true)}
        >
          <i className="bi bi-plus-circle"></i> Modify
        </button>
      </div>

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
};

export default Meal;

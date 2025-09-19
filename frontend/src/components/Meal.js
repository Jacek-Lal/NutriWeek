import { useState } from "react";
import MealModal from "./MealModal.js";

const Meal = ({ name, calories, mealItems = [], mealId }) => {
  const [list, setList] = useState(mealItems);
  const [isModalOpen, setIsModalOpen] = useState(false);

  return (
    <>
      <div className="bg-slate-50 flex flex-col max-w-60 rounded-xl p-3 shadow gap-6">
        <p className="text-center font-semibold">{name}</p>
        <p className="text-center">0 / {calories} kcal</p>
        <ul className="flex flex-col gap-3">
          {list.map((item) => (
            <li key={item.product.fdcId}>
              <div className="flex gap-6 items-center">
                <p className="w-40 truncate">{item.product.name}</p>
                <span className="whitespace-nowrap">{item.amount} g</span>
              </div>
            </li>
          ))}
        </ul>
        <button
          className="inline-flex items-center gap-2 rounded-xl bg-blue-600 px-4 py-2 text-white shadow-md hover:bg-blue-700 active:scale-95 transition whitespace-nowrap"
          onClick={() => setIsModalOpen(true)}
        >
          <i className="bi bi-plus-circle"></i> Modify
        </button>
      </div>

      <MealModal
        isOpen={isModalOpen}
        onClose={() => setIsModalOpen(false)}
        list={list}
        setList={setList}
        mealId={mealId}
      />
    </>
  );
};

export default Meal;

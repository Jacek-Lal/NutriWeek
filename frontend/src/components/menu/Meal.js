import { useState, useEffect } from "react";
import MealModal from "./meal_modal/MealModal.js";
import DropdownMenu from "components/common/DropdownMenu.js";

const Meal = ({ meal, onUpdateItems, onDelete }) => {
  const [productList, setProductList] = useState(meal.mealItems);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [isUpdated, setIsUpdated] = useState(false);

  const handleListChange = (newList) => {
    setProductList(newList);
    onUpdateItems(meal.id, newList);
    setIsUpdated(true);
  };

  useEffect(() => {
    if (isUpdated) {
      const timer = setTimeout(() => setIsUpdated(false), 700);
      return () => clearTimeout(timer);
    }
  }, [isUpdated]);

  const macros = productList.reduce(
    (acc, mi) => {
      const get = (n, u = "G") =>
        mi.product.nutrients.find((x) => x.name === n && x.unit === u)?.value ??
        0;
      acc.kcal += (get("Energy", "KCAL") * mi.amount) / 100;
      acc.protein += (get("Protein") * mi.amount) / 100;
      acc.fat += (get("Total lipid (fat)") * mi.amount) / 100;
      acc.carbs += (get("Carbohydrate, by difference") * mi.amount) / 100;
      return acc;
    },
    { kcal: 0, protein: 0, fat: 0, carbs: 0 }
  );

  return (
    <>
      <div
        className={`bg-white rounded-2xl shadow-sm border border-gray-200 hover:shadow-md transition-all duration-300 overflow-hidden 
          ${
            isUpdated
              ? "ring-4 ring-green-300 ring-opacity-60 scale-[1.02]"
              : ""
          }`}
      >
        {/* Dropdown */}
        <DropdownMenu deleteMeal={() => onDelete(meal.id)} />

        {/* Content */}
        <div className="flex flex-col gap-5 p-5">
          {/* Title */}
          <p className="text-center font-semibold text-gray-800 text-lg">
            {meal.name}
          </p>

          {/* Kcal summary */}
          <p className="text-center text-sm text-gray-700">
            <span
              className={`font-semibold ${
                macros.kcal > meal.targetKcal * 1.1
                  ? "text-red-600"
                  : macros.kcal >= meal.targetKcal * 0.9 &&
                    macros.kcal <= meal.targetKcal * 1.1
                  ? "text-green-600"
                  : "text-gray-800"
              }`}
            >
              {macros.kcal}
            </span>{" "}
            / {meal.targetKcal} kcal
          </p>

          {/* Product list */}
          <ul className="flex flex-col gap-2 text-sm text-gray-700">
            {productList.map((item, idx) => (
              <li
                key={idx}
                className="flex justify-between items-center bg-gray-50 rounded-lg px-3 py-2 hover:bg-gray-100 transition"
              >
                <p className="truncate w-40">{item.product.name}</p>
                <span className="whitespace-nowrap font-medium">
                  {item.amount} g
                </span>
              </li>
            ))}
          </ul>

          {/* Macros section */}
          <div className="bg-gray-50 rounded-lg p-4 shadow-inner">
            <div className="flex justify-between text-gray-700 font-medium border-b pb-1 mb-2 text-sm">
              <p>Protein</p>
              <p>Fat</p>
              <p>Carbs</p>
            </div>
            <div className="flex justify-between text-gray-800 font-semibold text-sm">
              <p>
                {macros.protein.toFixed(1)}{" "}
                <span className="text-gray-500">g</span>
              </p>
              <p>
                {macros.fat.toFixed(1)} <span className="text-gray-500">g</span>
              </p>
              <p>
                {macros.carbs.toFixed(1)}{" "}
                <span className="text-gray-500">g</span>
              </p>
            </div>
          </div>

          {/* Modify button */}
          <button
            className="inline-flex items-center justify-center gap-2 rounded-xl bg-green-500 px-4 py-2 text-white shadow-sm hover:bg-green-600 active:scale-95 transition-all duration-150 font-medium w-full"
            onClick={() => setIsModalOpen(true)}
          >
            <i className="bi bi-pencil-square"></i> Modify
          </button>
        </div>
      </div>

      {/* Modal */}
      <MealModal
        isOpen={isModalOpen}
        onClose={() => setIsModalOpen(false)}
        list={productList}
        setList={handleListChange}
        mealId={meal.id}
        targetKcal={meal.targetKcal}
      />
    </>
  );
};

export default Meal;

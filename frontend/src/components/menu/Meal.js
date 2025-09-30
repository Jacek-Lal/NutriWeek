import { useState } from "react";
import MealModal from "./meal_modal/MealModal.js";
import DropdownMenu from "components/common/DropdownMenu.js";

const Meal = ({ meal, targetKcal, onUpdateItems, onDelete }) => {
  const [productList, setProductList] = useState(meal.mealItems);
  const [isModalOpen, setIsModalOpen] = useState(false);

  const handleListChange = (newList) => {
    setProductList(newList);
    onUpdateItems(meal.id, newList);
  };

  const handleDelete = () => {
    onDelete(meal);
  };

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
      <div className="bg-slate-100 max-w-80 rounded-xl shadow">
        <DropdownMenu deleteMeal={handleDelete} />

        <div className="flex flex-col gap-6 pb-4 px-4">
          <p className="text-center font-semibold">{meal.name}</p>

          <p className="text-center">
            <span
              className={`${
                macros.kcal > targetKcal * 1.1
                  ? "text-red-600" // above 110%
                  : macros.kcal >= targetKcal * 0.9 &&
                    macros.kcal <= targetKcal * 1.1
                  ? "text-green-600" // within 90–110%
                  : "text-black" // below 90%
              }`}
            >
              {macros.kcal}
            </span>{" "}
            / {targetKcal} kcal
          </p>
          <ul className="flex flex-col gap-3">
            {productList.map((item, idx) => (
              <li key={idx}>
                <div className="flex gap-6 items-center">
                  <p className="w-40 truncate">{item.product.name}</p>
                  <span className="whitespace-nowrap">{item.amount} g</span>
                </div>
              </li>
            ))}
          </ul>
          <div>
            <div className="pl-2 pr-2 pt-5 flex flex-row justify-between">
              <p>Protein</p>
              <p>Fat</p>
              <p>Carbs</p>
            </div>
            <div className="pl-2 pr-2 flex justify-between">
              <p>
                {macros.protein.toFixed(2)} <span className="text-s">g</span>
              </p>
              <p>{macros.fat.toFixed(2)} g</p>
              <p>{macros.carbs.toFixed(2)} g</p>
            </div>
          </div>
          <button
            className="inline-flex items-center gap-2 rounded-xl bg-blue-600 px-4 py-2 text-white shadow-md hover:bg-blue-700 active:scale-95 transition whitespace-nowrap"
            onClick={() => setIsModalOpen(true)}
          >
            <i className="bi bi-plus-circle"></i> Modify
          </button>
        </div>
      </div>

      <MealModal
        isOpen={isModalOpen}
        onClose={() => setIsModalOpen(false)}
        list={productList}
        setList={handleListChange}
        mealId={meal.id}
        targetKcal={targetKcal}
      />
    </>
  );
};

export default Meal;

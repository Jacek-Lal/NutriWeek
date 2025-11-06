import React, { useState, useEffect } from "react";
import Meal from "./Meal.js";
import { deleteMeal } from "api/MealService.js";
import { addMenuMeal } from "api/MenuService.js";
import { toast } from "react-toastify";
import NewMealDialog from "./NewMealDialog.js";
const Day = ({ menuId, initialMeals, date }) => {
  const [meals, setMeals] = useState(initialMeals ?? []);
  const [showForm, setShowForm] = useState(false);

  useEffect(() => {
    setMeals(initialMeals ?? []);
  }, [initialMeals]);

  const updateMealItems = (mealId, newItems) => {
    setMeals((prev) =>
      prev.map((m) => (m.id === mealId ? { ...m, mealItems: newItems } : m))
    );
  };

  const onDeleteMeal = async (id) => {
    if (meals.length > 1) {
      await deleteMeal(id);
      setMeals((prev) => prev.filter((m) => m.id !== id));
    } else toast.error("You need at least 1 meal per day");
  };

  const onAddMeal = async (data) => {
    const payload = {
      ...data,
      targetKcal: parseInt(data.targetKcal),
      date: date,
    };
    const { data: newMeal } = await addMenuMeal(menuId, payload);
    setMeals([...meals, newMeal]);
    setShowForm(false);
  };

  const macros = meals.reduce(
    (acc, meal) => {
      meal.mealItems.forEach((mi) => {
        const get = (n, u = "G") =>
          mi.product.nutrients.find((x) => x.name === n && x.unit === u)
            ?.value ?? 0;

        acc.kcal += (get("Energy", "KCAL") * mi.amount) / 100;
        acc.protein += (get("Protein") * mi.amount) / 100;
        acc.fat += (get("Total lipid (fat)") * mi.amount) / 100;
        acc.carbs += (get("Carbohydrate, by difference") * mi.amount) / 100;
      });

      return acc;
    },
    { kcal: 0, protein: 0, fat: 0, carbs: 0 }
  );

  const targetKcal = meals.reduce((acc, meal) => {
    return acc + meal.targetKcal;
  }, 0);

  return (
    <div className="min-w-64 flex flex-col gap-5 mt-6 p-6 rounded-2xl">
      {/* Date + macros summary */}
      <div className="flex flex-col items-center">
        <p className="font-semibold text-gray-700">{date}</p>
        <p className="text-sm text-gray-600">
          <span className="text-green-600 font-semibold">{macros.kcal}</span> /{" "}
          {targetKcal} kcal
        </p>
      </div>

      {/* Macros breakdown */}
      <div className="bg-gray-50 rounded-lg p-4 shadow-inner">
        <div className="flex justify-between text-gray-700 font-medium border-b pb-1 mb-2 text-sm">
          <p>Protein</p>
          <p>Fat</p>
          <p>Carbs</p>
        </div>
        <div className="flex justify-between text-gray-800 font-semibold text-sm">
          <p>
            {macros.protein.toFixed(1)} <span className="text-gray-500">g</span>
          </p>
          <p>
            {macros.fat.toFixed(1)} <span className="text-gray-500">g</span>
          </p>
          <p>
            {macros.carbs.toFixed(1)} <span className="text-gray-500">g</span>
          </p>
        </div>
      </div>

      {/* Add meal toggle */}
      <p
        className="text-center text-green-600 font-medium hover:underline cursor-pointer transition"
        onClick={() => setShowForm(!showForm)}
      >
        <i className={`bi bi-${showForm ? "dash" : "plus"}`}></i>{" "}
        {!showForm ? "Add meal" : "Cancel"}
      </p>

      {/* Meal form */}
      <NewMealDialog show={showForm} onSubmit={onAddMeal} />

      {/* Meals list */}
      <div className="flex flex-col gap-5">
        {meals.map((meal) => (
          <Meal
            key={meal.id}
            meal={meal}
            targetKcal={meal.caloriesPercent * targetKcal}
            onUpdateItems={updateMealItems}
            onDelete={onDeleteMeal}
          />
        ))}
      </div>
    </div>
  );
};

export default Day;

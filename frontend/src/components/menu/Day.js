import React, { useState, useEffect, useRef } from "react";
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
    <div className="min-w-64 min-h-100 flex flex-col gap-6 mt-10 p-6 rounded-xl bg-slate-200 text-black">
      <p className=" text-center">{date}</p>
      <p className="text-gray-700 text-center">
        {macros.kcal} / {targetKcal} kcal
      </p>
      <div className="">
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
      <p
        className="text-center text-blue-600 hover:underline cursor-pointer"
        onClick={() => setShowForm(!showForm)}
      >
        <i className={`bi bi-${showForm ? "dash" : "plus"}`}></i>{" "}
        {!showForm ? "Add meal" : "Cancel"}
      </p>
      <NewMealDialog show={showForm} onSubmit={onAddMeal} />

      {meals.map((meal) => {
        return (
          <Meal
            key={meal.id}
            meal={meal}
            targetKcal={meal.caloriesPercent * targetKcal}
            onUpdateItems={updateMealItems}
            onDelete={onDeleteMeal}
          />
        );
      })}
    </div>
  );
};

export default Day;

import React, { useState, useEffect } from "react";
import Meal from "./Meal.js";

const Day = ({ initialMeals, targetKcal, date }) => {
  const [meals, setMeals] = useState(initialMeals ?? []);

  useEffect(() => {
    setMeals(initialMeals ?? []);
  }, [initialMeals]);

  const updateMealItems = (mealId, newItems) => {
    setMeals((prev) =>
      prev.map((m) => (m.id === mealId ? { ...m, mealItems: newItems } : m))
    );
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

  return (
    <div className="min-w-60 min-h-100 flex flex-col gap-6 ml-10 mt-10">
      <p className="text-white text-center">{date}</p>
      <p className="text-gray-300 text-center">
        {macros.kcal} / {targetKcal} kcal
      </p>
      <div className="text-white">
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
      {meals.map((meal) => {
        return (
          <Meal
            key={meal.id}
            mealId={meal.id}
            name={meal.name}
            targetKcal={meal.caloriesPercent * targetKcal}
            mealItems={meal.mealItems}
            onUpdateItems={updateMealItems}
          />
        );
      })}
    </div>
  );
};

export default Day;

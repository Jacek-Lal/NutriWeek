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

  const calories = meals.reduce(
    (acc, m) =>
      acc +
      m.mealItems.reduce((acc2, mi) => {
        const energy = mi.product.nutrients.find(
          (n) => n.name === "Energy" && n.unit === "KCAL"
        ).value;
        return acc2 + (energy * mi.amount) / 100;
      }, 0),
    0
  );

  return (
    <div className="min-w-60 min-h-100 flex flex-col gap-6 ml-10 mt-10">
      <p className="text-white text-center">{date}</p>
      <p className="text-gray-300 text-center">
        {calories} / {targetKcal} kcal
      </p>
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

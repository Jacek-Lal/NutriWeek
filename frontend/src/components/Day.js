import React, { useState } from "react";
import Meal from "./Meal.js";

const Day = ({ meals, menuId, targetMacros, targetKcal, date }) => {
  const [macros, setMacros] = useState({
    protein: 0,
    fat: 0,
    carbs: 0,
  });
  const mealNames = ["Breakfast", "Lunch", "Dinner"];
  console.log(meals);
  const calcCalories = () => {
    return macros.protein * 4 + macros.fat * 9 + macros.carbs * 4;
  };
  return (
    <div className="min-w-60 min-h-100 flex flex-col gap-6 ml-10 mt-10">
      <p className="text-white text-center">{date}</p>
      <p className="text-gray-300 text-center">
        {calcCalories()} / {targetMacros.calories}
      </p>
      {meals.map((meal) => {
        return (
          <Meal
            key={meal.id}
            mealId={meal.id}
            name={meal.name}
            calories={meal.caloriesPercent * targetKcal}
            mealItems={meal.mealItems}
          />
        );
      })}
    </div>
  );
};

export default Day;

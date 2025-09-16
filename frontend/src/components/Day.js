import React, { useState } from "react";
import Meal from "./Meal.js";

const Day = ({ meals, targetMacros, date }) => {
  const [macros, setMacros] = useState({
    protein: 0,
    fat: 0,
    carbs: 0,
  });

  const mealNames = ["Breakfast", "Lunch", "Dinner"];

  const calcCalories = () => {
    return macros.protein * 4 + macros.fat * 9 + macros.carbs * 4;
  };
  return (
    <div className="min-w-60 min-h-100 flex flex-col gap-6 ml-10 mt-10">
      <p className="text-white text-center">{date}</p>
      <p className="text-gray-300 text-center">
        {calcCalories()} / {targetMacros.calories}
      </p>
      {[...Array(meals)].map((_, i) => {
        return (
          <Meal key={i} name={mealNames[i] ? mealNames[i] : `Snack ${i - 2}`} />
        );
      })}
    </div>
  );
};

export default Day;

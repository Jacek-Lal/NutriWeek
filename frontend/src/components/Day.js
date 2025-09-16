import React from "react";

const Day = () => {
  const meals = ["Breakfast", "Lunch", "Dinner"];

  return (
    <>
      <div className="flex flex-col gap-6 ml-10 mt-10">
        {meals.map((name) => (
          <Meal key={name} name={name} />
        ))}
      </div>
    </>
  );
};

export default Day;

import React from "react";
import { useRef, useState } from "react";
import { useNavigate } from "react-router-dom";

import { InputField, MacroField, DaysOrRange } from "../common/Inputs";
import { addDays, daysBetween, formatDate } from "../../utility/Date";
import { addMenu } from "api";

const NewMenuDialog = ({ modalRef, closeModal }) => {
  const navigate = useNavigate();
  const [menuData, setMenuData] = useState({
    name: "Meal Plan",
    days: 7,
    meals: 3,
    calories: 2000,
    startDate: formatDate(new Date()),
    endDate: formatDate(addDays(new Date(), 7)),
    targetFat: 25,
    targetProtein: 25,
    targetCarb: 50,
    caloriesPerMeal: [30, 40, 30],
    rangeType: "days",
  });

  const onChange = (event) => {
    let value = event.target.value;
    const name = event.target.name;

    if (typeof menuData[name] === "number") {
      value = parseInt(value) || 0;
      value = value < 0 ? 0 : value;
    }

    if (name === "meals") {
      const newLength = value;
      let newCaloriesPerMeal = [...menuData.caloriesPerMeal];

      if (newLength > newCaloriesPerMeal.length) {
        newCaloriesPerMeal = [
          ...newCaloriesPerMeal,
          ...Array(newLength - newCaloriesPerMeal.length).fill(0),
        ];
      } else if (newLength < newCaloriesPerMeal.length)
        newCaloriesPerMeal = newCaloriesPerMeal.slice(0, newLength);

      setMenuData({
        ...menuData,
        [name]: value,
        caloriesPerMeal: newCaloriesPerMeal,
      });
      return;
    }

    setMenuData({ ...menuData, [name]: value });
  };

  const onSubmit = async (event) => {
    event.preventDefault();

    let days = menuData.days;
    if (menuData.rangeType == "dates")
      days = daysBetween(menuData.startDate, menuData.endDate);

    let { rangeType, endDate, ...rest } = menuData;
    const payload = { ...rest, days: days };

    const response = await addMenu(payload);

    navigate(`/menus/${response.data?.id}`);
  };

  const macros = [
    { label: "Protein", name: "targetProtein", factor: 4 },
    { label: "Fat", name: "targetFat", factor: 9 },
    { label: "Carbs", name: "targetCarb", factor: 4 },
  ];

  const totalMacro = macros.reduce(
    (sum, m) => sum + parseInt(menuData[m.name] || 0),
    0
  );
  const totalMealCalories = menuData.caloriesPerMeal.reduce(
    (sum, m) => sum + parseInt(m || 0),
    0
  );

  return (
    <dialog
      ref={modalRef}
      className="w-11/12 md:w-3/4 lg:w-2/3 h-[90vh] bg-white text-gray-800 rounded-2xl shadow-2xl p-6 md:p-10 overflow-hidden border border-gray-200"
    >
      {/* Header */}
      <div className="flex justify-between items-center border-b pb-4">
        <h2 className="text-2xl font-extrabold text-gray-800">
          Create <span className="text-green-600">Menu</span>
        </h2>
        <button
          onClick={() => closeModal()}
          className="bg-gray-100 hover:bg-gray-200 text-gray-600 rounded-full p-2 transition"
          title="Close"
        >
          <i className="bi bi-x text-2xl"></i>
        </button>
      </div>

      {/* Menu Form */}
      <form
        onSubmit={onSubmit}
        className="flex flex-col gap-8 overflow-y-auto pr-2 mt-6 h-[calc(90vh-100px)]"
      >
        {/* Name */}
        <InputField
          label="Name"
          name="name"
          value={menuData.name}
          onChange={onChange}
        />

        {/* Duration */}
        <div className="flex flex-col gap-2">
          <label className="font-semibold text-gray-700">Duration</label>
          <DaysOrRange menuData={menuData} onChange={onChange} />
        </div>

        {/* Meals and Calories */}
        <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
          <InputField
            label="Meals per Day"
            type="number"
            name="meals"
            value={menuData.meals}
            onChange={onChange}
          />
          <InputField
            label="Total Calories per Day"
            type="number"
            name="calories"
            value={menuData.calories}
            onChange={onChange}
          />
        </div>

        {/* Macros */}
        <div className="flex flex-col gap-3">
          <p className="font-semibold text-gray-700">Macro Distribution (%)</p>
          <div className="pt-2 grid grid-cols-1 sm:grid-cols-3 gap-4">
            {macros.map((m) => (
              <MacroField
                key={m.name}
                label={m.label}
                name={m.name}
                calories={menuData.calories}
                percent={menuData[m.name]}
                factor={m.factor}
                onChange={onChange}
              />
            ))}
          </div>
          <p className="pt-2 text-end font-semibold text-gray-600">
            Total: {totalMacro}%
          </p>
        </div>

        {/* Calories per meal */}
        <div className="flex flex-col gap-3">
          <p className="font-semibold text-gray-700">
            Calories Distribution (%)
          </p>
          <div className="pt-2 grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-4">
            {[...Array(menuData.meals)].map((_, i) => (
              <div
                key={i}
                className="flex flex-col items-center justify-center bg-gray-50 border border-gray-200 rounded-xl p-4 hover:shadow-md transition"
              >
                <p className="font-semibold text-green-600 mb-1">
                  Meal {i + 1}
                </p>
                <p className="text-sm text-gray-600 mb-1">
                  {(menuData.caloriesPerMeal[i] * menuData.calories) / 100} kcal
                </p>
                <input
                  type="number"
                  className="w-20 text-center rounded-lg border border-gray-300 p-2 text-gray-800 focus:ring-2 focus:ring-green-400 outline-none transition"
                  value={menuData.caloriesPerMeal[i] ?? 0}
                  onChange={(e) => {
                    const newMealCalories = [...menuData.caloriesPerMeal];
                    if (Number(e.target.value) < 0) e.target.value = "";
                    newMealCalories[i] = e.target.value;
                    setMenuData({
                      ...menuData,
                      caloriesPerMeal: newMealCalories,
                    });
                  }}
                />
              </div>
            ))}
          </div>
          <p className="pt-2 text-end font-semibold text-gray-600">
            Total: {totalMealCalories ?? 0}%
          </p>
        </div>

        {/* Submit */}
        <button
          type="submit"
          className="mt-4 bg-green-500 hover:bg-green-600 text-white font-semibold py-3 rounded-xl shadow-md transition-all duration-200"
        >
          Create Menu
        </button>
      </form>
    </dialog>
  );
};

export default NewMenuDialog;

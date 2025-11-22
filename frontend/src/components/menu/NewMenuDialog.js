import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { TailSpin } from "react-loader-spinner";

import { InputField, DaysOrRange } from "../common/Inputs";
import { addDays, daysBetween, formatDate } from "../../utility/Date";
import { addMenu } from "api";

const NUMERIC_FIELDS = [
  "days",
  "calories",
  "targetProtein",
  "targetFat",
  "targetCarb",
];

const VALID_DAYS_OR_PERCENTS_REGEX = /^([1-9]|[1-9][0-9]|100)$/; // 1–100
const VALID_CALORIES_REGEX = /^([1-9][0-9]{0,3}|10000)$/; // 1–10000

const MACROS = [
  { label: "Protein", name: "targetProtein", factor: 4 },
  { label: "Fat", name: "targetFat", factor: 9 },
  { label: "Carbs", name: "targetCarb", factor: 4 },
];

const defaultMenuData = () => ({
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

const isValueInRange = (name, value) => {
  if (
    (name === "days" || name.startsWith("target")) &&
    !VALID_DAYS_OR_PERCENTS_REGEX.test(value)
  ) {
    return false;
  }

  if (name === "calories" && !VALID_CALORIES_REGEX.test(value)) {
    return false;
  }

  return true;
};

const NewMenuDialog = ({ modalRef, closeModal }) => {
  const navigate = useNavigate();
  const [loading, setLoading] = useState(false);
  const [menuData, setMenuData] = useState(defaultMenuData);

  const onChange = (event) => {
    const { name, value } = event.target;

    if (!NUMERIC_FIELDS.includes(name)) {
      setMenuData((prev) => ({ ...prev, [name]: value }));
      return;
    }

    if (value === "") {
      setMenuData((prev) => ({ ...prev, [name]: "" }));
      return;
    }

    if (!isValueInRange(name, value)) return;

    setMenuData((prev) => ({ ...prev, [name]: parseInt(value) }));
  };

  const changeMealsNumber = (event) => {
    const { name, value } = event.target;
    const mealsCount = parseInt(value);

    setMenuData((prev) => {
      const current = [...prev.caloriesPerMeal];
      let newCaloriesPerMeal = current;

      if (mealsCount > current.length) {
        newCaloriesPerMeal = [
          ...current,
          ...Array(mealsCount - current.length).fill(0),
        ];
      } else if (mealsCount < current.length) {
        newCaloriesPerMeal = current.slice(0, mealsCount);
      }

      return {
        ...prev,
        [name]: mealsCount,
        caloriesPerMeal: newCaloriesPerMeal,
      };
    });
  };

  const handleMealCaloriesChange = (index) => (event) => {
    const { value } = event.target;

    setMenuData((prev) => {
      const newMealCalories = [...prev.caloriesPerMeal];

      if (value === "") {
        newMealCalories[index] = "";
        return { ...prev, caloriesPerMeal: newMealCalories };
      }

      if (!VALID_DAYS_OR_PERCENTS_REGEX.test(value)) {
        return prev;
      }

      newMealCalories[index] = value;
      return { ...prev, caloriesPerMeal: newMealCalories };
    });
  };

  const onSubmit = async (event) => {
    event.preventDefault();
    setLoading(true);

    let days = menuData.days;
    if (menuData.rangeType === "dates") {
      days = daysBetween(menuData.startDate, menuData.endDate);
    }

    const { rangeType, endDate, ...rest } = menuData;
    const payload = { ...rest, days };

    const response = await addMenu(payload);

    setLoading(false);
    navigate(`/menus/${response.data?.id}`);
  };

  const totalMacro = MACROS.reduce(
    (sum, macro) => sum + parseInt(menuData[macro.name] || 0),
    0
  );

  const totalMealCalories = menuData.caloriesPerMeal.reduce(
    (sum, m) => sum + parseInt(m || 0),
    0
  );

  return (
    <dialog
      ref={modalRef}
      className="w-11/12 md:w-3/4 lg:w-2/3 h-[95vh] bg-white text-gray-800 rounded-2xl shadow-2xl p-6 md:p-10 overflow-hidden border border-gray-200"
    >
      {/* Header */}
      <div className="flex justify-between items-center border-b pb-4">
        <h2 className="text-2xl font-extrabold text-gray-800">
          Create <span className="text-green-600">Menu</span>
        </h2>
        <button
          onClick={closeModal}
          className="bg-gray-100 hover:bg-gray-200 text-gray-600 rounded-full p-2 transition"
          title="Close"
        >
          <i className="bi bi-x text-2xl" />
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

        <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
          {/* Meals */}
          <div className="flex flex-col gap-2 w-full">
            <label
              htmlFor="meals"
              className="font-semibold text-gray-700 text-sm"
            >
              Meals per day
            </label>
            <select
              id="meals"
              name="meals"
              defaultValue={3}
              className="rounded-xl border border-gray-300 bg-gray-50 px-3 py-2 text-gray-900 placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-green-400 focus:border-transparent transition-all duration-200"
              onChange={changeMealsNumber}
            >
              {[...Array(9)].map((_, i) => (
                <option key={i} value={i + 1}>
                  {i + 1}
                </option>
              ))}
            </select>
          </div>
          {/* Calories */}
          <InputField
            label="Total Calories per Day"
            type="text"
            name="calories"
            value={menuData.calories}
            onChange={onChange}
          />
        </div>

        {/* Macros */}
        <div className="flex flex-col gap-3">
          <p className="font-semibold text-gray-700">Macro Distribution (%)</p>
          <div className="pt-2 grid grid-cols-1 sm:grid-cols-3 gap-4">
            {MACROS.map((macro) => {
              const grams = (
                (menuData.calories * menuData[macro.name]) /
                100 /
                macro.factor
              ).toFixed(1);

              const kcal = (
                (menuData.calories * menuData[macro.name]) /
                100
              ).toFixed(0);

              return (
                <div
                  key={macro.name}
                  className="flex flex-col items-center gap-2 bg-gray-50 border border-gray-200 rounded-xl p-3 shadow-sm hover:shadow-md transition-all duration-200"
                >
                  <label className="font-semibold text-gray-700">
                    {macro.label}
                  </label>
                  <p className="text-sm text-gray-600">{grams} g</p>
                  <p className="text-sm text-gray-600">{kcal} kcal</p>
                  <InputField
                    type="text"
                    name={macro.name}
                    value={menuData[macro.name]}
                    onChange={onChange}
                    className={"w-[60%] mx-auto bg-white text-center"}
                  />
                </div>
              );
            })}
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
                <InputField
                  type="text"
                  name={`meal-${i}`}
                  value={menuData.caloriesPerMeal[i] ?? 0}
                  onChange={handleMealCaloriesChange(i)}
                  className={"w-[60%] mx-auto bg-white text-center"}
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
          className="mt-4 bg-green-500 hover:bg-green-600 text-white font-semibold py-3 rounded-xl shadow-md transition-all duration-200 flex justify-center"
          disabled={loading}
        >
          {loading ? (
            <TailSpin height="20" width="20" visible={true} color="white" />
          ) : (
            "Create Menu"
          )}
        </button>
      </form>
    </dialog>
  );
};

export default NewMenuDialog;

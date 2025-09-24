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

    if (typeof menuData[name] === "number") value = parseInt(value) || 0;

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
      className="w-3/4 h-4/5 bg-slate-800 text-white rounded-xl p-6 shadow-xl"
      ref={modalRef}
    >
      <div className="flex justify-between items-center">
        <h2 className="text-2xl font-bold mb-6 text-center flex-1">
          Create Menu
        </h2>
        <button
          className="bg-blue-600 text-slate-800 pl-4 pr-4 rounded-full hover:bg-blue-700 transition"
          onClick={() => closeModal()}
        >
          <i className="bi bi-x text-3xl text-white"></i>
        </button>
      </div>

      <form
        className="flex flex-col gap-6 overflow-y-auto pr-2 mt-6"
        onSubmit={onSubmit}
      >
        <InputField
          label="Name"
          name="name"
          value={menuData.name}
          onChange={onChange}
        />

        <div className="flex flex-col gap-2">
          <label className="font-semibold">Duration</label>
          <DaysOrRange menuData={menuData} onChange={onChange} />
        </div>

        <div className="grid grid-cols-2 gap-6">
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

        <div className="flex flex-col gap-2">
          <p className="font-semibold">Macro Distribution (%)</p>
          <div className="pt-4 grid grid-cols-3 gap-4">
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
          <p className="pt-4 text-end font-semibold">Total: {totalMacro}%</p>
        </div>

        <div className="flex flex-col gap-2">
          <p className="font-semibold">Calories Distribution (%)</p>
          <div className="pt-4 grid grid-cols-3 gap-4">
            {[...Array(menuData.meals)].map((_, i) => (
              <div key={i} className="flex flex-col text-center gap-2">
                <p className="font-semibold">Meal {i + 1}</p>
                <p>
                  {(menuData.caloriesPerMeal[i] * menuData.calories) / 100} kcal
                </p>
                <input
                  type="number"
                  className="rounded-md p-2 text-slate-800"
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
          <p className="pt-4 text-end font-semibold">
            Total: {totalMealCalories ?? 0}%
          </p>
        </div>
        <button
          type="submit"
          className="mt-6 bg-blue-600 hover:bg-blue-700 px-6 py-3 rounded-lg shadow-md text-white font-semibold transition"
        >
          Submit
        </button>
      </form>
    </dialog>
  );
};

export default NewMenuDialog;

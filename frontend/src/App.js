import { useRef, useState } from "react";

function App() {
  const [menuData, setMenuData] = useState({
    name: "Meal Plan",
    days: 7,
    meals: 3,
    calories: 2000,
    startDate: "",
    endDate: "",
    targetFat: 25,
    targetProtein: 25,
    targetCarb: 50,
  });
  const modalRef = useRef();

  const toggleModal = async (show) => {
    show ? modalRef.current.showModal() : modalRef.current.close();
  };
  const onChange = (event) => {
    setMenuData({ ...menuData, [event.target.name]: event.target.value });
  };
  return (
    <>
      <button
        className="inline-flex items-center gap-2 rounded-xl bg-blue-600 px-4 py-2 text-white shadow-md hover:bg-blue-700 active:scale-95 transition whitespace-nowrap"
        onClick={() => toggleModal(true)}
      >
        <i className="bi bi-plus-circle"></i> New Menu
      </button>

      <dialog
        className="w-3/4 h-4/5 bg-slate-800 text-white rounded-xl p-6 shadow-xl"
        ref={modalRef}
      >
        <div className="flex justify-between">
          <h2 className="text-2xl font-bold mb-6 text-center">Create Menu</h2>
          <button
            className="bg-blue-200 text-slate-800 pl-4 pr-4 rounded-full hover:bg-blue-300 transition"
            onClick={() => toggleModal(false)}
          >
            <i className="bi bi-x text-3xl"></i>
          </button>
        </div>

        <form className="flex flex-col gap-6 overflow-y-auto pr-2 mt-6">
          <div className="flex flex-col gap-2">
            <label className="font-semibold">Name</label>
            <input
              type="text"
              className="rounded-md p-2 text-slate-800"
              name="name"
              value={menuData.name}
              onChange={onChange}
            />
          </div>
          <div className="flex flex-col gap-2">
            <label className="font-semibold">Number of Days / Date Range</label>
            <div className="flex gap-4">
              <input
                type="number"
                placeholder="Days"
                className="w-32 rounded-md p-2 text-slate-800"
                name="days"
                value={menuData.days}
                onChange={onChange}
              />
              <span className="text-sm text-gray-400">or</span>
              <div className="flex gap-2">
                <input
                  type="date"
                  className="rounded-md p-2 text-slate-800"
                  name="startDate"
                  value={menuData.startDate}
                  onChange={onChange}
                />
                <input
                  type="date"
                  className="rounded-md p-2 text-slate-800"
                  name="endDate"
                  value={menuData.endDate}
                  onChange={onChange}
                />
              </div>
            </div>
          </div>

          <div className="grid grid-cols-2 gap-6">
            <div className="flex flex-col gap-2">
              <label className="font-semibold">Meals per Day</label>
              <input
                type="number"
                placeholder="e.g. 3"
                className="rounded-md p-2 text-slate-800"
                name="meals"
                value={menuData.meals}
                onChange={onChange}
              />
            </div>
            <div className="flex flex-col gap-2">
              <label className="font-semibold">Total Calories per Day</label>
              <input
                type="number"
                placeholder="e.g. 2000"
                className="rounded-md p-2 text-slate-800"
                name="calories"
                value={menuData.calories}
                onChange={onChange}
              />
            </div>
          </div>

          {/* 3. Macro split */}
          <div className="flex flex-col gap-2">
            <p className="font-semibold">Macro Distribution (%)</p>
            <div className="pt-4 grid grid-cols-3 gap-4">
              <div className="flex flex-col gap-2">
                <label className="text-center font-semibold">Protein</label>
                <p className="text-center">
                  {(
                    (menuData.calories * menuData.targetProtein) /
                    100 /
                    4
                  ).toFixed(1)}
                  g
                </p>
                <p className="text-center">
                  {(menuData.calories * menuData.targetProtein) / 100} kcal
                </p>
                <input
                  type="number"
                  placeholder="Protein"
                  className="rounded-md p-2 text-slate-800"
                  name="targetProtein"
                  value={menuData.targetProtein}
                  onChange={onChange}
                />
              </div>
              <div className="flex flex-col gap-2">
                <label className="text-center font-semibold">Fat</label>
                <p className="text-center">
                  {((menuData.calories * menuData.targetFat) / 100 / 9).toFixed(
                    1
                  )}
                  g
                </p>
                <p className="text-center">
                  {(menuData.calories * menuData.targetFat) / 100} kcal
                </p>

                <input
                  type="number"
                  placeholder="Fat"
                  className="rounded-md p-2 text-slate-800"
                  name="targetFat"
                  value={menuData.targetFat}
                  onChange={onChange}
                />
              </div>
              <div className="flex flex-col gap-2">
                <label className="text-center font-semibold">Carbs</label>
                <p className="text-center">
                  {(
                    (menuData.calories * menuData.targetCarb) /
                    100 /
                    4
                  ).toFixed(1)}
                  g
                </p>
                <p className="text-center">
                  {(menuData.calories * menuData.targetCarb) / 100} kcal
                </p>
                <input
                  type="number"
                  placeholder="Carbs"
                  className="rounded-md p-2 text-slate-800"
                  name="targetCarb"
                  value={menuData.targetCarb}
                  onChange={onChange}
                />
              </div>
            </div>
            <p className="pt-4 text-end font-semibold">
              Total:{" "}
              {parseInt(menuData.targetProtein) +
                parseInt(menuData.targetFat) +
                parseInt(menuData.targetCarb)}
              %
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
    </>
  );
}
export default App;

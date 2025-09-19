import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { getMenu } from "../api/MenuService";
import Day from "../components/Day.js";
import { addDays } from "../utility/Date.js";
function MenuDetails() {
  const { id } = useParams();
  const [menuData, setMenuData] = useState({});

  useEffect(() => {
    (async function () {
      const response = await getMenu(id);
      if (response.data) setMenuData(response.data);
      console.log(response.data);
    })();
  }, [id]);

  return (
    <div className="flex">
      {[...Array(menuData?.days)].map((__, i) => {
        const macros = {
          calories: menuData.calories,
          protein: (menuData.protein * menuData.calories) / 100,
          fat: (menuData.fat * menuData.calories) / 100,
          carbs: (menuData.carbs * menuData.calories) / 100,
        };
        const meals = (menuData?.mealList ?? []).slice(
          i * menuData.meals,
          (i + 1) * menuData.meals
        );
        return (
          <Day
            key={i}
            menuId={id}
            meals={meals}
            targetMacros={macros}
            targetKcal={menuData.calories}
            date={addDays(new Date(menuData.startDate), i).toDateString()}
          />
        );
      })}
    </div>
  );
}
export default MenuDetails;

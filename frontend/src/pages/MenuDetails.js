import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { getMenu, getMenuMeals } from "api";
import { addDays } from "../utility/Date.js";
import Day from "../components/menu/Day.js";

function MenuDetails() {
  const { id } = useParams();
  const [menu, setMenu] = useState({});
  const [mealsPage, setMealsPage] = useState({});
  const [currentPage, setCurrentPage] = useState(0);
  const getMealsPage = async (menu, page = 0, size = 21) => {
    try {
      setCurrentPage(page);
      if (menu) {
        const days = menu.days < 7 ? menu.days : 7;
        size = menu.meals * days;
      }

      const { data } = await getMenuMeals(menu.id, page, size);
      setMealsPage(data);
      console.log(data);
    } catch (e) {
      console.error(e);
    }
  };

  useEffect(() => {
    (async function () {
      try {
        const { data: menuData } = await getMenu(id);
        setMenu(menuData);
        getMealsPage(menuData);

        console.log(menuData);
      } catch (e) {
        console.error(e);
      }
    })();
  }, [id]);

  return (
    <div>
      {mealsPage.content?.length > 0 && mealsPage.page.totalPages > 1 && (
        <div className="flex justify-center gap-4 text-white">
          <a
            onClick={() => getMealsPage(menu, 0)}
            className={`bg-slate-500 p-2 rounded cursor-pointer ${
              currentPage === 0 ? "pointer-events-none opacity-60" : ""
            }`}
          >
            <i className="bi bi-chevron-double-left text-xs"></i>{" "}
          </a>
          <a
            onClick={() => getMealsPage(menu, currentPage - 1)}
            className={`bg-slate-500 p-2 rounded cursor-pointer ${
              currentPage === 0 ? "pointer-events-none opacity-60" : ""
            }`}
          >
            <i className="bi bi-chevron-left text-xs"></i>
          </a>

          <div className="my-auto">
            <span className="text-blue-500">
              {addDays(
                new Date(menu.startDate),
                currentPage * 7
              ).toDateString()}
            </span>
            <i className="bi bi-dash"></i>
            <span className="text-blue-500">
              {addDays(
                new Date(menu.startDate),
                currentPage * 7 + mealsPage.content.length / menu.meals - 1
              ).toDateString()}
            </span>
          </div>

          <a
            onClick={() => getMealsPage(menu, currentPage + 1)}
            className={`bg-slate-500 p-2 rounded cursor-pointer ${
              currentPage === mealsPage.page.totalPages - 1
                ? "pointer-events-none opacity-60"
                : ""
            }
            `}
          >
            <i className="bi bi-chevron-right text-xs"></i>
          </a>
          <a
            onClick={() => getMealsPage(menu, mealsPage.page.totalPages - 1)}
            className={`bg-slate-500 p-2 rounded cursor-pointer ${
              currentPage === mealsPage.page.totalPages - 1
                ? "pointer-events-none opacity-60"
                : ""
            }
            `}
          >
            <i className="bi bi-chevron-double-right text-xs"></i>{" "}
          </a>
        </div>
      )}
      <div className="flex">
        {mealsPage.content &&
          [...Array(mealsPage.content.length / menu.meals)].map((__, i) => {
            const meals = (mealsPage?.content ?? []).slice(
              i * menu.meals,
              (i + 1) * menu.meals
            );

            return (
              <Day
                key={`${currentPage}-${i}`}
                initialMeals={meals}
                targetKcal={menu.calories}
                date={addDays(
                  new Date(menu.startDate),
                  i + currentPage * 7
                ).toDateString()}
              />
            );
          })}
      </div>
    </div>
  );
}
export default MenuDetails;

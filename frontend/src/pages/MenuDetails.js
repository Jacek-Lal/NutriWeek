import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { getMenu, getMenuMeals } from "api";
import { addDays } from "../utility/Date.js";
import Day from "../components/menu/Day.js";

function MenuDetails() {
  const { id } = useParams();
  const [menu, setMenu] = useState({});
  const [daysPage, setDaysPage] = useState({});
  const [currentPage, setCurrentPage] = useState(0);
  const getMealsPage = async (menu, page = 0, size = 7) => {
    try {
      setCurrentPage(page);

      const { data } = await getMenuMeals(menu.id, page, size);
      setDaysPage(data);
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
      {daysPage.content?.length > 0 && daysPage.page.totalPages > 1 && (
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

          <div className="my-auto flex items-center gap-4">
            <span className="text-blue-500">{daysPage.content.at(0).date}</span>
            <i className="bi bi-dash"></i>
            <span className="text-blue-500">
              {daysPage.content.at(-1).date}
            </span>
          </div>

          <a
            onClick={() => getMealsPage(menu, currentPage + 1)}
            className={`bg-slate-500 p-2 rounded cursor-pointer ${
              currentPage === daysPage.page.totalPages - 1
                ? "pointer-events-none opacity-60"
                : ""
            }
            `}
          >
            <i className="bi bi-chevron-right text-xs"></i>
          </a>
          <a
            onClick={() => getMealsPage(menu, daysPage.page.totalPages - 1)}
            className={`bg-slate-500 p-2 rounded cursor-pointer ${
              currentPage === daysPage.page.totalPages - 1
                ? "pointer-events-none opacity-60"
                : ""
            }
            `}
          >
            <i className="bi bi-chevron-double-right text-xs"></i>{" "}
          </a>
        </div>
      )}
      <div className="flex gap-3 flex-wrap place-content-center">
        {daysPage?.content?.map((day, i) => {
          return (
            <Day
              key={`${currentPage}-${i}`}
              menuId={menu.id}
              initialMeals={day.meals}
              date={day.date}
            />
          );
        })}
      </div>
    </div>
  );
}
export default MenuDetails;

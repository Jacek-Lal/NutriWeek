import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { getMenu, getMenuMeals } from "api";
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
      } catch (e) {
        console.error(e);
      }
    })();
  }, [id]);

  return (
    <div className="flex flex-col gap-8 min-h-screen bg-orange-50 px-4 md:px-10 py-8">
      {/* Pagination */}
      {daysPage.content?.length > 0 && daysPage.page.totalPages > 1 && (
        <div className="flex justify-center items-center gap-3 flex-wrap">
          {/* First page */}
          <a
            onClick={() => getMealsPage(menu, 0)}
            className={`p-2.5 rounded-lg shadow-sm border border-gray-300 bg-white hover:bg-green-50 text-gray-600 transition-all duration-200 cursor-pointer ${
              currentPage === 0 ? "opacity-50 pointer-events-none" : ""
            }`}
            title="First page"
          >
            <i className="bi bi-chevron-double-left text-sm"></i>
          </a>

          {/* Previous page */}
          <a
            onClick={() => getMealsPage(menu, currentPage - 1)}
            className={`p-2.5 rounded-lg shadow-sm border border-gray-300 bg-white hover:bg-green-50 text-gray-600 transition-all duration-200 cursor-pointer ${
              currentPage === 0 ? "opacity-50 pointer-events-none" : ""
            }`}
            title="Previous page"
          >
            <i className="bi bi-chevron-left text-sm"></i>
          </a>

          {/* Current range */}
          <div className="my-auto flex items-center gap-3 px-3 py-2 bg-gray-50 rounded-lg shadow-inner text-sm font-medium">
            <span className="text-green-600 font-semibold">
              {daysPage.content.at(0).date}
            </span>
            <i className="bi bi-dash text-gray-400"></i>
            <span className="text-green-600 font-semibold">
              {daysPage.content.at(-1).date}
            </span>
          </div>

          {/* Next page */}
          <a
            onClick={() => getMealsPage(menu, currentPage + 1)}
            className={`p-2.5 rounded-lg shadow-sm border border-gray-300 bg-white hover:bg-green-50 text-gray-600 transition-all duration-200 cursor-pointer ${
              currentPage === daysPage.page.totalPages - 1
                ? "opacity-50 pointer-events-none"
                : ""
            }`}
            title="Next page"
          >
            <i className="bi bi-chevron-right text-sm"></i>
          </a>

          {/* Last page */}
          <a
            onClick={() => getMealsPage(menu, daysPage.page.totalPages - 1)}
            className={`p-2.5 rounded-lg shadow-sm border border-gray-300 bg-white hover:bg-green-50 text-gray-600 transition-all duration-200 cursor-pointer ${
              currentPage === daysPage.page.totalPages - 1
                ? "opacity-50 pointer-events-none"
                : ""
            }`}
            title="Last page"
          >
            <i className="bi bi-chevron-double-right text-sm"></i>
          </a>
        </div>
      )}

      {/* Days grid */}
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 2xl:grid-cols-5 gap-6 place-content-center">
        {daysPage?.content?.map((day, i) => (
          <div
            key={`${currentPage}-${i}`}
            className="bg-white rounded-2xl border border-gray-200 shadow-sm hover:shadow-md transition-all duration-200 p-4"
          >
            <Day menuId={menu.id} initialMeals={day.meals} date={day.date} />
          </div>
        ))}
      </div>
    </div>
  );
}
export default MenuDetails;

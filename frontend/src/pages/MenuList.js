import { useEffect, useRef, useState } from "react";
import { Link } from "react-router-dom";

import NewMenuDialog from "../components/NewMenuDialog.js";
import { getMenus } from "../api/MenuService.js";

function MenuList() {
  const [menuList, setMenuList] = useState([]);
  const modalRef = useRef();
  const toggleModal = (show) => {
    show ? modalRef.current.showModal() : modalRef.current.close();
  };

  useEffect(() => {
    (async function () {
      const response = await getMenus();
      if (response.data) setMenuList(response.data);
    })();
  }, []);

  return (
    <div>
      <h1 className="text-2xl font-bold mb-4">Menus</h1>

      <button
        onClick={() => toggleModal(true)}
        className="bg-blue-600 text-white px-4 py-2 rounded-lg shadow hover:bg-blue-700 transition"
      >
        + Create New Menu
      </button>

      <NewMenuDialog
        modalRef={modalRef}
        closeModal={() => toggleModal(false)}
      />

      <div className="p-6 flex flex-col gap-4">
        {menuList.map((menu) => {
          return (
            <Link
              to={`/menus/${menu.id}`}
              key={menu.id}
              className="bg-slate-800 p-6 hover:bg-slate-700 transition"
            >
              <div className="grid grid-flow-col text-white justify-between">
                <p className="text-lg">
                  <span className="text-gray-400">#{menu.id}</span> {menu.name}
                </p>
                <p>Start date: {menu.startDate}</p>
                <p>Days: {menu.days}</p>
                <p>Calories: current / {menu.calories}</p>
              </div>
            </Link>
          );
        })}
      </div>
    </div>
  );
}
export default MenuList;

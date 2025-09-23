import { useEffect, useRef, useState } from "react";
import { Link } from "react-router-dom";

import NewMenuDialog from "../components/NewMenuDialog.js";
import { getMenus, deleteMenu } from "../api/MenuService.js";
import ConfirmDialog from "../components/ConfirmDialog.js";

function MenuList() {
  const [menuList, setMenuList] = useState([]);
  const [menuToDelete, setMenuToDelete] = useState(null);
  const menuModalRef = useRef();
  const confirmModalRef = useRef();

  const toggleModal = (show) => {
    show ? menuModalRef.current.showModal() : menuModalRef.current.close();
  };

  const handleDeleteClick = (menu) => {
    setMenuToDelete(menu);
    confirmModalRef.current.showModal();
  };

  const confirmDelete = async () => {
    if (menuToDelete) {
      await deleteMenu(menuToDelete.id); // your API call
      setMenuList((prev) => prev.filter((m) => m.id !== menuToDelete.id));
      setMenuToDelete(null);
    }
    confirmModalRef.current.close();
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
        modalRef={menuModalRef}
        closeModal={() => toggleModal(false)}
      />

      <div className="p-6 flex flex-col gap-4">
        {menuList.map((menu) => {
          return (
            <>
              <div className="flex bg-slate-800 p-6 hover:bg-slate-700 transition">
                <Link
                  to={`/menus/${menu.id}`}
                  key={menu.id}
                  className="flex-1 block mr-4 p-1"
                >
                  <div className="flex justify-between text-white">
                    <p className="text-lg">
                      <span className="text-gray-400">#{menu.id}</span>{" "}
                      {menu.name}
                    </p>
                    <p>Start date: {menu.startDate}</p>
                    <p>Days: {menu.days}</p>
                    <p>Calories: {menu.calories}</p>
                  </div>
                </Link>
                <button
                  className="p-1 rounded opacity-80 hover:bg-red-300 transition"
                  onClick={() => handleDeleteClick(menu)}
                >
                  <i className="bi bi-trash text-red-700"></i>
                </button>
              </div>
            </>
          );
        })}
      </div>
      <ConfirmDialog
        dialogRef={confirmModalRef}
        message={`Are you sure you want to delete "${menuToDelete?.name}"?`}
        onConfirm={confirmDelete}
        onCancel={() => confirmModalRef.current.close()}
      />
    </div>
  );
}
export default MenuList;

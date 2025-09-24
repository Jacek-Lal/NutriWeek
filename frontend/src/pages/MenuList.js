import { useEffect, useRef, useState } from "react";
import { Link } from "react-router-dom";
import { getMenus, deleteMenu } from "api";

import NewMenuDialog from "../components/menu/NewMenuDialog.js";
import ConfirmDialog from "../components/common/ConfirmDialog.js";

function MenuList() {
  const [menusPage, setMenusPage] = useState({});
  const [menuToDelete, setMenuToDelete] = useState(null);
  const [currentPage, setCurrentPage] = useState(0);
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
      await deleteMenu(menuToDelete.id);
      setMenusPage((prev) =>
        prev.content.filter((m) => m.id !== menuToDelete.id)
      );
      setMenuToDelete(null);
    }
    confirmModalRef.current.close();
  };

  const getAllMenus = async (page = 0, size = 5) => {
    try {
      setCurrentPage(page);

      const { data } = await getMenus(page, size);

      setMenusPage(data);
      console.log(data);
    } catch (error) {
      console.error(error);
    }
  };

  useEffect(() => {
    getAllMenus();
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
        {menusPage.content?.map((menu) => {
          return (
            <div
              key={menu.id}
              className="flex bg-slate-800 p-6 hover:bg-slate-700 transition"
            >
              <Link to={`/menus/${menu.id}`} className="flex-1 block mr-4 p-1">
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
          );
        })}
      </div>
      {menusPage.content?.length > 0 && menusPage.page.totalPages > 1 && (
        <div className="flex justify-center gap-4 text-white">
          <a
            onClick={() => getAllMenus(currentPage - 1)}
            className={`bg-slate-500 p-2 rounded cursor-pointer ${
              currentPage === 0 ? "pointer-events-none opacity-60" : ""
            }`}
          >
            &laquo;
          </a>

          {menusPage &&
            [...Array(menusPage.page.totalPages).keys()].map((page, index) => (
              <a
                onClick={() => getAllMenus(page)}
                className={`p-2 rounded cursor-pointer
                  ${
                    currentPage === page
                      ? "bg-blue-600 pointer-events-none"
                      : "bg-slate-500"
                  }`}
                key={page}
              >
                {page + 1}
              </a>
            ))}

          <a
            onClick={() => getAllMenus(currentPage + 1)}
            className={`bg-slate-500 p-2 rounded cursor-pointer ${
              currentPage === menusPage.totalPages - 1
                ? "pointer-events-none opacity-60"
                : ""
            }
            `}
          >
            &raquo;
          </a>
        </div>
      )}
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

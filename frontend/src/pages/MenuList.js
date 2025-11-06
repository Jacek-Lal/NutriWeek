import { useEffect, useRef, useState } from "react";
import { Link } from "react-router-dom";
import { getMenus, deleteMenu } from "api";
import NewMenuDialog from "../components/menu/NewMenuDialog.js";
import ConfirmDialog from "../components/common/ConfirmDialog.js";
import { formatDate } from "utility/Date.js";

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
      setMenusPage((prev) => ({
        ...prev,
        content: prev.content.filter((m) => m.id !== menuToDelete.id),
      }));
      setMenuToDelete(null);
    }
    confirmModalRef.current.close();
  };

  const getAllMenus = async (page = 0, size = 5) => {
    try {
      setCurrentPage(page);

      const { data } = await getMenus(page, size);

      setMenusPage(data);
    } catch (error) {
      console.error(error);
    }
  };

  useEffect(() => {
    getAllMenus();
  }, []);

  return (
    <div className="min-h-screen bg-gradient-to-b from-orange-50 via-white to-gray-50 px-4 md:px-10 py-8">
      {/* Header */}
      <div className="w-3/4 mx-auto ">
        <div className="flex flex-col items-center md:items-start sm:justify-between mb-10 gap-4">
          <h1 className="text-3xl font-bold text-gray-800">
            My <span className="text-green-600">Menus</span>
          </h1>
          <button
            onClick={() => toggleModal(true)}
            className="bg-green-500 text-white font-medium rounded-xl px-5 py-2.5 shadow-sm hover:bg-green-600 transition-all duration-200"
          >
            + New Menu
          </button>
        </div>

        {/* New Menu Modal */}
        <NewMenuDialog
          modalRef={menuModalRef}
          closeModal={() => toggleModal(false)}
        />

        {/* Menus List */}
        <div className="grid grid-cols-1 xl:grid-cols-2 gap-5">
          {menusPage.content?.map((menu) => (
            <div
              key={menu.id}
              className="flex items-center justify-between bg-white border border-gray-200 rounded-2xl p-5 shadow-sm hover:shadow-md hover:bg-green-50 transition-all duration-200"
            >
              <Link
                to={`/menus/${menu.id}`}
                className="flex-1 block mr-4 p-1 rounded-lg"
              >
                <div className="flex flex-col md:flex-row md:items-center md:justify-between gap-2 text-gray-800">
                  <p className="text-lg font-semibold">
                    <span className="text-gray-400 font-normal">
                      {formatDate(new Date(menu.createdAt))}
                    </span>{" "}
                    {menu.name}
                  </p>
                  <p className="text-sm text-gray-500">
                    <span className="font-medium text-gray-600">
                      Start date:
                    </span>{" "}
                    {menu.startDate}
                  </p>
                  <p className="text-sm text-gray-500">
                    <span className="font-medium text-gray-600">Days:</span>{" "}
                    {menu.days}
                  </p>
                </div>
              </Link>
              <button
                className="p-2 rounded-lg bg-red-100 hover:bg-red-200 transition-all duration-200"
                onClick={() => handleDeleteClick(menu)}
                title="Delete menu"
              >
                <i className="bi bi-trash text-red-600 text-lg"></i>
              </button>
            </div>
          ))}
        </div>

        {/* Pagination */}
        {menusPage.content?.length > 0 && menusPage.page.totalPages > 1 && (
          <div className="flex justify-center items-center gap-3 mt-10 flex-wrap">
            {/* Previous */}
            <a
              onClick={() => getAllMenus(currentPage - 1)}
              className={`px-3 py-2 rounded-lg text-sm font-medium transition-colors ${
                currentPage === 0
                  ? "bg-gray-300 text-gray-500 cursor-not-allowed"
                  : "bg-green-500 text-white hover:bg-green-600 cursor-pointer"
              }`}
            >
              &laquo;
            </a>

            {/* Pages */}
            {menusPage &&
              [...Array(menusPage.page.totalPages).keys()].map((page) => (
                <a
                  onClick={() => getAllMenus(page)}
                  className={`px-3 py-2 rounded-lg text-sm font-medium transition-colors ${
                    currentPage === page
                      ? "bg-green-600 text-white pointer-events-none"
                      : "bg-gray-200 text-gray-700 hover:bg-green-100 cursor-pointer"
                  }`}
                  key={page}
                >
                  {page + 1}
                </a>
              ))}

            {/* Next */}
            <a
              onClick={() => getAllMenus(currentPage + 1)}
              className={`px-3 py-2 rounded-lg text-sm font-medium transition-colors ${
                currentPage === menusPage.totalPages - 1
                  ? "bg-gray-300 text-gray-500 cursor-not-allowed"
                  : "bg-green-500 text-white hover:bg-green-600 cursor-pointer"
              }`}
            >
              &raquo;
            </a>
          </div>
        )}

        {/* Confirm Deletion Dialog */}
        <ConfirmDialog
          dialogRef={confirmModalRef}
          message={`Are you sure you want to delete "${menuToDelete?.name}"?`}
          onConfirm={confirmDelete}
          onCancel={() => confirmModalRef.current.close()}
        />
      </div>
    </div>
  );
}
export default MenuList;

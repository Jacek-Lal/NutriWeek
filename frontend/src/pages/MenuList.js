import { useRef, useState } from "react";
import NewMenuDialog from "../components/NewMenuDialog.js";

function MenuList() {
  const modalRef = useRef();
  const toggleModal = (show) => {
    show ? modalRef.current.showModal() : modalRef.current.close();
  };
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
    </div>
  );
}
export default MenuList;

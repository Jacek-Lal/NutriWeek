import React, { useState, useRef, useEffect } from "react";
import ConfirmDialog from "./ConfirmDialog";

const DropdownMenu = ({ deleteMeal }) => {
  const [open, setOpen] = useState(false);
  const menuRef = useRef(null);
  const confirmDeleteRef = useRef();

  const confirmDelete = () => {
    confirmDeleteRef.current.close();
    deleteMeal();
  };

  useEffect(() => {
    const handleClickOutside = (e) => {
      if (menuRef.current && !menuRef.current.contains(e.target)) {
        setOpen(false);
      }
    };
    document.addEventListener("mousedown", handleClickOutside);
    return () => document.removeEventListener("mousedown", handleClickOutside);
  }, []);

  return (
    <div className="relative w-min ml-auto mr-2" ref={menuRef}>
      <button
        onClick={() => setOpen(!open)}
        className="p-2 rounded-full hover:bg-gray-100"
      >
        <i className="bi bi-three-dots"></i>
      </button>

      {open && (
        <div className="absolute left-0 mt-2 w-40 bg-white border rounded-md shadow-lg">
          <button
            className="block w-full text-left px-4 py-2 hover:bg-gray-100"
            onClick={() => {
              confirmDeleteRef.current.showModal();
              setOpen(false);
            }}
          >
            <i className="bi bi-trash text-red-700 mr-2"></i>
            Delete
          </button>
        </div>
      )}
      <ConfirmDialog
        dialogRef={confirmDeleteRef}
        message={`Are you sure you want to delete this meal?`}
        onConfirm={confirmDelete}
        onCancel={() => confirmDeleteRef.current.close()}
        labelCancel="Cancel"
        labelConfirm="Delete"
      />
    </div>
  );
};

export default DropdownMenu;

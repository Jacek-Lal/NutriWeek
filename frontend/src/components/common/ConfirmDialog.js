import React from "react";

const ConfirmDialog = ({ dialogRef, onConfirm, onCancel, message }) => {
  return (
    <dialog ref={dialogRef} className="p-6 rounded-xl bg-white shadow-xl">
      <p className="text-gray-800 mb-4">{message}</p>
      <div className="flex justify-end gap-4">
        <button
          onClick={onCancel}
          className="px-4 py-2 rounded bg-gray-300 hover:bg-gray-400"
        >
          Cancel
        </button>
        <button
          onClick={onConfirm}
          className="px-4 py-2 rounded bg-red-600 text-white hover:bg-red-700"
        >
          Delete
        </button>
      </div>
    </dialog>
  );
};

export default ConfirmDialog;

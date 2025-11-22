import React from "react";
import { TailSpin } from "react-loader-spinner";

const ConfirmDialog = ({
  dialogRef,
  onConfirm,
  onCancel,
  message,
  labelCancel,
  labelConfirm,
  loading,
}) => {
  return (
    <dialog ref={dialogRef} className="p-6 rounded-xl bg-white shadow-xl">
      <p className="text-gray-800 mb-4">{message}</p>
      <div className="flex justify-end gap-4">
        <button
          onClick={onCancel}
          className="px-4 py-2 rounded bg-gray-300 hover:bg-gray-400"
          disabled={loading}
        >
          {labelCancel || "Cancel"}
        </button>
        <button
          onClick={onConfirm}
          className="px-4 py-2 rounded bg-red-600 text-white hover:bg-red-700"
          disabled={loading}
        >
          {loading ? (
            <TailSpin height="20" width="20" visible={true} color="white" />
          ) : (
            labelConfirm || "Confirm"
          )}
        </button>
      </div>
    </dialog>
  );
};

export default ConfirmDialog;

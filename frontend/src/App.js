import { useRef, useState } from "react";
import NewMenuDialog from "./components/NewMenuDialog";

function App() {
  const modalRef = useRef();

  const toggleModal = async (show) => {
    show ? modalRef.current.showModal() : modalRef.current.close();
  };
  return (
    <>
      <button
        className="inline-flex items-center gap-2 rounded-xl bg-blue-600 px-4 py-2 text-white shadow-md hover:bg-blue-700 active:scale-95 transition whitespace-nowrap"
        onClick={() => toggleModal(true)}
      >
        <i className="bi bi-plus-circle"></i> New Menu
      </button>
      <NewMenuDialog modalRef={modalRef} toggleModal={toggleModal} />
    </>
  );
}
export default App;

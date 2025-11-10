import { useState, useEffect } from "react";
import { addMealItems, getRecentProducts } from "api";
import SearchBar from "./SearchBar";
import ProductGrid from "./ProductGrid";
import ProductList from "./ProductList";

const MealModal = ({ isOpen, onClose, list, setList, mealId, targetKcal }) => {
  const [products, setProducts] = useState([]);
  const [recentProducts, setRecentProducts] = useState([]);
  const [modalList, setModalList] = useState(list);
  const [recentVisible, setRecentVisible] = useState(true);

  useEffect(() => {
    if (isOpen) {
      getRecentProducts(10).then((res) => setRecentProducts(res.data));
      setModalList(list);
    }
  }, [isOpen]);

  const saveMeal = async () => {
    await addMealItems(mealId, modalList);
    setList(modalList);
    onClose();
  };

  return (
    <dialog
      open={isOpen}
      className="fixed inset-0 z-[9999] w-11/12 h-[90vh] bg-white text-gray-800 rounded-2xl shadow-2xl p-6 overflow-hidden border border-gray-200"
    >
      {/* Header */}
      <div className="flex justify-between items-center mb-6 border-b pb-3">
        <h2 className="text-2xl font-extrabold text-gray-800">
          Modify <span className="text-green-600">Meal</span>
        </h2>

        <button
          onClick={onClose}
          className="bg-gray-100 hover:bg-gray-200 rounded-full p-3 transition"
          title="Close"
        >
          <i className="bi bi-x text-2xl text-gray-700"></i>
        </button>
      </div>

      {/* Main grid layout */}
      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6 h-[calc(100%-5rem)]">
        {/* Left side: Search + Product grids */}
        <div className="lg:col-span-2 flex flex-col gap-6 overflow-y-auto pr-1">
          {/* Search Bar */}
          <SearchBar setProducts={setProducts} />

          {/* Products */}
          <div className="bg-gray-50 border border-gray-200 rounded-xl p-4 shadow-sm">
            <div className="flex items-center gap-2 ">
              <h1 className="font-semibold text-gray-700 text-lg p-2">
                Recent products
              </h1>

              <button
                className="w-8 h-8 flex items-center justify-center rounded-full hover:bg-gray-200 transition"
                onClick={() => setRecentVisible(!recentVisible)}
              >
                <i
                  className={`bi bi-chevron-${
                    recentVisible ? "up" : "down"
                  } text-gray-700 text-lg`}
                ></i>
              </button>
            </div>

            <div
              className={`overflow-hidden transition-[max-height] duration-500 ease-out ${
                recentVisible ? "max-h-[1000px]" : "max-h-0"
              }`}
            >
              <ProductGrid
                data={recentProducts}
                list={modalList}
                setList={setModalList}
              />
            </div>

            <h1 className="font-semibold text-gray-700 text-lg mb-3 p-2">
              Results
            </h1>
            <ProductGrid
              data={products}
              list={modalList}
              setList={setModalList}
            />
          </div>
        </div>

        {/* Right side: Selected products */}
        <div className="h-min bg-gray-50 border border-gray-200 rounded-xl p-4 shadow-sm overflow-y-auto">
          <h1 className="font-semibold text-gray-700 text-lg mb-3">
            Selected Products
          </h1>
          <ProductList
            list={modalList}
            setList={setModalList}
            saveMeal={saveMeal}
            targetKcal={targetKcal}
          />
        </div>
      </div>
    </dialog>
  );
};

export default MealModal;

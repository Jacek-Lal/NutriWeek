const ProductOnList = ({ mealItem, removeProduct, updateAmount }) => {
  const getNutrient = (n) =>
    mealItem.product.nutrients.find((x) => x.name === n) ?? null;

  const nutKeys = {
    Energy: "Energy",
    Protein: "Protein",
    "Total lipid (fat)": "Fat",
    "Carbohydrate, by difference": "Carbohydrates",
  };

  return (
    <li className="flex flex-col gap-4 rounded-2xl border border-gray-200 bg-white p-5 shadow-sm hover:shadow-md transition-all duration-200">
      {/* Header: product name + delete button */}
      <div className="flex justify-between items-start">
        <h1 className="text-base sm:text-lg font-semibold text-gray-800 leading-tight">
          {mealItem.product.name}
        </h1>
        <button
          onClick={() => removeProduct(mealItem.product.fdcId)}
          className="p-2 text-red-600 hover:bg-red-100 rounded-full transition-all duration-150 active:scale-95"
          title="Remove product"
        >
          <i className="bi bi-trash text-lg"></i>
        </button>
      </div>

      {/* Amount input */}
      <div className="flex items-center gap-2 text-sm text-gray-700">
        <span className="font-medium">Amount:</span>
        <input
          type="number"
          min={0}
          max={9999}
          value={mealItem.amount}
          onChange={(e) =>
            updateAmount(mealItem.product.fdcId, Number(e.target.value))
          }
          className="w-24 rounded-lg border border-gray-300 px-2 py-1 text-gray-800 focus:outline-none focus:ring-2 focus:ring-green-400 focus:border-transparent transition-all duration-200"
        />
        <span className="text-gray-500">g</span>
      </div>

      {/* Nutrients list */}
      <div className="space-y-1.5 text-sm text-gray-700">
        {Object.keys(nutKeys).map((key) => {
          const n = getNutrient(key);
          if (n === null) return null;

          return (
            <p
              key={`${n.name}|${n.unit}`}
              className="flex justify-between items-center border-b border-gray-100 pb-1 last:border-0"
            >
              <span className="truncate">{nutKeys[n.name]}</span>
              <span className="font-medium text-gray-800">
                {n.value} {n.unit}
              </span>
            </p>
          );
        })}
      </div>
    </li>
  );
};

export default ProductOnList;

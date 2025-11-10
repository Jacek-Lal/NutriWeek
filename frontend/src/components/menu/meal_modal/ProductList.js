import ProductOnList from "./ProductOnList";

const ProductList = ({ list, setList, saveMeal, targetKcal }) => {
  const removeProduct = (fdcId) => {
    setList((prevList) =>
      prevList.filter((item) => item.product.fdcId !== fdcId)
    );
  };

  const updateAmount = (fdcId, newAmount) => {
    if (newAmount < 0 || newAmount > 9999) return;
    setList((prevList) =>
      prevList.map((item) =>
        item.product.fdcId === fdcId ? { ...item, amount: newAmount } : item
      )
    );
  };

  const macros = list.reduce(
    (acc, mi) => {
      const get = (n) =>
        mi.product.nutrients.find((x) => x.name === n)?.value ?? 0;
      acc.kcal += (get("Energy") * mi.amount) / 100;
      acc.protein += (get("Protein") * mi.amount) / 100;
      acc.fat += (get("Total lipid (fat)") * mi.amount) / 100;
      acc.carbs += (get("Carbohydrate, by difference") * mi.amount) / 100;
      return acc;
    },
    { kcal: 0, protein: 0, fat: 0, carbs: 0 }
  );

  return (
    <div className="h-auto flex flex-col bg-white border border-gray-200 rounded-2xl shadow-sm overflow-hidden min-w-72">
      {/* Header: Calories & Macros Summary */}
      <div className="bg-green-500 text-white text-center py-4 px-2 rounded-t-2xl">
        <p className="font-semibold text-sm uppercase tracking-wide">
          Calories
        </p>
        <p className="text-lg font-bold">
          {macros.kcal} / {targetKcal} kcal
        </p>
      </div>

      {/* Macros breakdown */}
      <div className="p-4 border-b border-gray-200">
        <div className="flex justify-between text-sm font-medium text-gray-700">
          <p>Protein</p>
          <p>Fat</p>
          <p>Carbs</p>
        </div>
        <div className="flex justify-between text-gray-800 font-semibold mt-1 text-sm">
          <p>
            {macros.protein.toFixed(1)} <span className="text-gray-500">g</span>
          </p>
          <p>
            {macros.fat.toFixed(1)} <span className="text-gray-500">g</span>
          </p>
          <p>
            {macros.carbs.toFixed(1)} <span className="text-gray-500">g</span>
          </p>
        </div>
      </div>

      {/* Product list */}
      <ul className="grid grid-cols-1 2xl:grid-cols-2 gap-4 p-4 overflow-y-auto flex-1 max-h-[50vh]">
        {list.length > 0 ? (
          list.map((mealItem) => (
            <ProductOnList
              key={mealItem.product.fdcId}
              mealItem={mealItem}
              removeProduct={removeProduct}
              updateAmount={updateAmount}
            />
          ))
        ) : (
          <p className="col-span-full text-center text-gray-500 text-sm py-8">
            No products selected yet.
          </p>
        )}
      </ul>

      {/* Save button */}
      <button
        onClick={() => saveMeal()}
        className="mt-auto bg-green-500 hover:bg-green-600 text-white font-semibold py-3 flex items-center justify-center gap-2 transition-all duration-200 rounded-b-2xl"
      >
        <i className="bi bi-floppy text-lg"></i>
        Save
      </button>
    </div>
  );
};

export default ProductList;

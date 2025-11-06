import React from "react";

const Product = ({ product, list, setList }) => {
  const addProduct = () => {
    const el = list.find((item) => item.product.fdcId === product.fdcId);

    if (!el)
      setList((prevList) => [...prevList, { product: product, amount: 100 }]);
  };
  const getNutrient = (n) =>
    product.nutrients.find((x) => x.name === n) ?? null;

  const nutKeys = {
    Energy: "Energy",
    Protein: "Protein",
    "Total lipid (fat)": "Fat",
    "Carbohydrate, by difference": "Carbohydrates",
  };

  return (
    <li className="rounded-2xl border border-gray-200 bg-white p-5 shadow-sm hover:shadow-md hover:border-green-200 transition-all duration-200">
      {/* Header: product name + add button */}
      <div className="flex justify-between items-start mb-3">
        <h1 className="text-lg font-semibold text-gray-800 leading-tight line-clamp-2">
          {product.name}
        </h1>
        <button
          onClick={() => addProduct()}
          className="flex items-center justify-center rounded-full p-2 text-green-600 hover:bg-green-100 active:scale-95 transition-all duration-150"
          title="Add product"
        >
          <i className="bi bi-plus-circle text-xl"></i>
        </button>
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

export default Product;

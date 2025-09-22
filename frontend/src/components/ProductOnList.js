import React from "react";

const ProductOnList = ({ mealItem, setList }) => {
  const removeProduct = () => {
    setList((prevList) =>
      prevList.filter((item) => item.product.fdcId !== mealItem.product.fdcId)
    );
  };

  return (
    <li className="self-start rounded-2xl border border-gray-200 bg-white p-5 shadow hover:shadow-lg transition">
      <div className="flex stretch space-x-4">
        <h1 className="text-lg font-semibold text-gray-800 mb-3">
          {mealItem.product.name}
        </h1>
        <button className="p-3" onClick={() => removeProduct()}>
          <i className="bi bi-trash"></i>
        </button>
      </div>
      <div className="space-y-1 text-sm text-gray-600">
        {mealItem.product.nutrients.slice(1, 5).map((n) => (
          <p key={`${n.name}|${n.unit}`} className="flex justify-between">
            <span>{n.name}</span>
            <span className="font-medium">
              {n.value} {n.unit}
            </span>
          </p>
        ))}
      </div>
    </li>
  );
};

export default ProductOnList;

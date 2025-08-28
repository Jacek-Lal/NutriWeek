import React from "react";

const ProductOnList = ({ product, setList }) => {
  const removeProduct = () => {
    setList((prevList) => prevList.filter((p) => p.fdcId !== product.fdcId));
  };

  return (
    <li className="self-start rounded-2xl border border-gray-200 bg-white p-5 shadow hover:shadow-lg transition">
      <div className="flex stretch space-x-4">
        <h1 className="text-lg font-semibold text-gray-800 mb-3">
          {product.description}
        </h1>
        <button className="p-3" onClick={() => removeProduct()}>
          <i className="bi bi-trash"></i>
        </button>
      </div>
      <div className="space-y-1 text-sm text-gray-600">
        {product.foodNutrients.slice(1, 5).map((n) => (
          <p key={n.nutrientId} className="flex justify-between">
            <span>{n.nutrientName}</span>
            <span className="font-medium">
              {n.value} {n.unitName}
            </span>
          </p>
        ))}
      </div>
    </li>
  );
};

export default ProductOnList;

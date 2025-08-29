import React from "react";

const Product = ({ product, list, setList }) => {
  const addProduct = () => {
    const el = list.find((p) => p.fdcId === product.fdcId);

    if (!el) setList((prevList) => [...prevList, product]);
  };

  return (
    <li className="self-start rounded-2xl border border-gray-200 bg-white p-5 shadow hover:shadow-lg transition">
      <div className="flex stretch space-x-4">
        <h1 className="text-lg font-semibold text-gray-800 mb-3">
          {product.description}
        </h1>
        <button className="p-3" onClick={() => addProduct()}>
          <i className="bi bi-plus-circle"></i>
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

export default Product;

import React from "react";

const Product = ({ product }) => {
  return (
    <li className="rounded-2xl border border-gray-200 bg-white p-5 shadow hover:shadow-lg transition">
      <div className="flex bg-red-200">
        <h1 className="bg-blue-200 text-lg font-semibold text-gray-800 mb-3">
          {product.description}
        </h1>
        <button>
          <i class="bi bi-plus-circle"></i>
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

import React from "react";

const Product = ({ product }) => {
  return (
    <div>
      <h1>{product.description}</h1>
      {product.foodNutrients.slice(1, 5).map((n) => (
        <p key={n.nutrientId}>
          {n.nutrientName}: {n.value} {n.unitName}
        </p>
      ))}
    </div>
  );
};

export default Product;

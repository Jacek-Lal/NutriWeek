import React from "react";
import Product from "./Product";

const ProductGrid = ({ data, list, setList }) => {
  return (
    <ul className="auto-rows-min col-span-2 grid grid-cols-1 md:grid-cols-2 xl:grid-cols-3 gap-6 p-6">
      {data?.foods?.length > 0 &&
        data.foods.map((product) => (
          <Product
            product={product}
            list={list}
            setList={setList}
            key={product.fdcId}
          />
        ))}
    </ul>
  );
};

export default ProductGrid;

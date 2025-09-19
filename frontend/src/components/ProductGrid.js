import React from "react";
import Product from "./Product";

const ProductGrid = ({ data, list, setList }) => {
  return (
    <ul className="auto-rows-min col-span-2 grid grid-cols-1 lg:grid-cols-2 2xl:grid-cols-4 gap-6 p-6">
      {data?.length > 0 &&
        data.map((product) => (
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

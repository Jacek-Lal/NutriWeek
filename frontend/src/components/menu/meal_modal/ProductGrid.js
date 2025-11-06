import React from "react";
import Product from "./Product";

const ProductGrid = ({ data, list, setList }) => {
  return (
    <ul className="grid grid-cols-1 sm:grid-cols-2 xl:grid-cols-3 2xl:grid-cols-4 gap-6 p-4 sm:p-6 bg-gray-50">
      {data?.length > 0 ? (
        data.map((product) => (
          <Product
            key={product.fdcId}
            product={product}
            list={list}
            setList={setList}
          />
        ))
      ) : (
        <p className="col-span-full text-center text-gray-500 py-8">
          No products found.
        </p>
      )}
    </ul>
  );
};

export default ProductGrid;

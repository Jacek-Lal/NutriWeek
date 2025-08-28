import React from "react";
import ProductOnList from "./ProductOnList";

const ProductList = ({ list, setList }) => {
  return (
    <ul className="bg-gray-700 flex flex-col gap-6 p-6">
      {list.length > 0 &&
        list.map((product) => (
          <ProductOnList
            product={product}
            setList={setList}
            key={product.fdcId}
          />
        ))}
    </ul>
  );
};

export default ProductList;

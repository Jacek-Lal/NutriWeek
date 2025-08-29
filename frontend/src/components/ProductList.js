import React from "react";
import ProductOnList from "./ProductOnList";

const ProductList = ({ list, setList }) => {
  const saveMeal = () => {
    console.log(list);
  };

  return (
    <div className="flex flex-col">
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
      <button className="bg-gray-200 p-2" onClick={() => saveMeal()}>
        <i className="bi bi-floppy"></i> Save
      </button>
    </div>
  );
};

export default ProductList;

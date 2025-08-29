import React from "react";
import ProductOnList from "./ProductOnList";
import { addMeal } from "../api/MealService.js";

const ProductList = ({ list, setList, toggleModal }) => {
  const saveMeal = async () => {
    try {
      const { data } = await addMeal(list);
      console.log(data);
      toggleModal(false);
    } catch (error) {
      console.error(error);
    }
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

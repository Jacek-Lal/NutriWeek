import React from "react";
import ProductOnList from "./ProductOnList";
import { addMeal } from "../api/MealService.js";

const ProductList = ({ list, setList, saveMeal }) => {
  return (
    <div className="flex flex-col row-span-full col-start-3">
      <ul className="bg-gray-700 flex flex-col gap-6 p-6">
        {list.length > 0 &&
          list.map((mealItem) => (
            <ProductOnList
              mealItem={mealItem}
              setList={setList}
              key={mealItem.product.fdcId}
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

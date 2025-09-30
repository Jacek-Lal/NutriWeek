import ProductOnList from "./ProductOnList";

const ProductList = ({ list, setList, saveMeal, targetKcal }) => {
  const removeProduct = (fdcId) => {
    setList((prevList) =>
      prevList.filter((item) => item.product.fdcId !== fdcId)
    );
  };

  const updateAmount = (fdcId, newAmount) => {
    setList((prevList) =>
      prevList.map((item) =>
        item.product.fdcId === fdcId ? { ...item, amount: newAmount } : item
      )
    );
  };

  const macros = list.reduce(
    (acc, mi) => {
      const get = (n) =>
        mi.product.nutrients.find((x) => x.name === n)?.value ?? 0;
      acc.kcal += (get("Energy") * mi.amount) / 100;
      acc.protein += (get("Protein") * mi.amount) / 100;
      acc.fat += (get("Total lipid (fat)") * mi.amount) / 100;
      acc.carbs += (get("Carbohydrate, by difference") * mi.amount) / 100;
      return acc;
    },
    { kcal: 0, protein: 0, fat: 0, carbs: 0 }
  );

  return (
    <div className="bg-slate-700 flex flex-col row-span-full col-start-3 min-w-72">
      <div className="p-2 text-center text-white">
        <p>Kcal</p>
        <p>
          {macros.kcal} / {targetKcal}
        </p>
      </div>
      <div className="pl-2 pr-2 flex justify-between text-white">
        <p>Protein</p>
        <p>Fat</p>
        <p>Carbs</p>
      </div>
      <div className="pl-2 pr-2 flex justify-between text-white">
        <p>
          {macros.protein.toFixed(2)} <span className="text-s">g</span>
        </p>
        <p>{macros.fat.toFixed(2)} g</p>
        <p>{macros.carbs.toFixed(2)} g</p>
      </div>
      <ul className="flex flex-col gap-6 p-6">
        {list.length > 0 &&
          list.map((mealItem) => (
            <ProductOnList
              key={mealItem.product.fdcId}
              mealItem={mealItem}
              removeProduct={removeProduct}
              updateAmount={updateAmount}
            />
          ))}
      </ul>
      <button className="bg-slate-200 p-2" onClick={() => saveMeal()}>
        <i className="bi bi-floppy"></i> Save
      </button>
    </div>
  );
};

export default ProductList;

const ProductOnList = ({ mealItem, removeProduct, updateAmount }) => {
  return (
    <li className="flex  flex-col gap-3 rounded-2xl border border-gray-200 bg-white p-5 shadow hover:shadow-lg transition">
      <div className="flex stretch space-x-4">
        <h1 className="text-lg font-semibold text-gray-800">
          {mealItem.product.name}
        </h1>
        <button
          className="p-3"
          onClick={() => removeProduct(mealItem.product.fdcId)}
        >
          <i className="bi bi-trash"></i>
        </button>
      </div>
      <div>
        <span>Amount: </span>
        <input
          className="w-20"
          type="number"
          value={mealItem.amount}
          onChange={(e) =>
            updateAmount(mealItem.product.fdcId, Number(e.target.value))
          }
        ></input>
      </div>

      <div className="space-y-1 text-sm text-gray-600">
        {mealItem.product.nutrients.slice(1, 5).map((n) => (
          <p key={`${n.name}|${n.unit}`} className="flex justify-between">
            <span>{n.name}</span>
            <span className="font-medium">
              {n.value} {n.unit}
            </span>
          </p>
        ))}
      </div>
    </li>
  );
};

export default ProductOnList;

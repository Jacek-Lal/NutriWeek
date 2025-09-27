const InputField = ({
  label,
  name,
  type = "text",
  value,
  onChange,
  placeholder,
  className,
}) => (
  <div className="flex flex-col gap-2">
    {label && <label className="font-semibold">{label}</label>}
    <input
      type={type}
      name={name}
      value={value}
      onChange={onChange}
      placeholder={placeholder}
      className={`rounded-md p-2 text-slate-800 ${className || ""}`}
    />
  </div>
);

const MacroField = ({ label, calories, percent, name, onChange, factor }) => {
  const grams = ((calories * percent) / 100 / factor).toFixed(1);
  const kcal = ((calories * percent) / 100).toFixed(0);

  return (
    <div className="flex flex-col gap-2">
      <label className="text-center font-semibold">{label}</label>
      <p className="text-center">{grams} g</p>
      <p className="text-center">{kcal} kcal</p>
      <input
        type="number"
        name={name}
        value={percent}
        onChange={onChange}
        placeholder={label}
        className="rounded-md p-2 text-slate-800"
      />
    </div>
  );
};

const DaysOrRange = ({ menuData, onChange }) => {
  return (
    <div className="flex flex-col gap-4">
      <div className="flex items-center gap-4">
        <label className="flex items-center gap-2">
          <input
            type="radio"
            name="rangeType"
            value="days"
            checked={menuData.rangeType === "days"}
            onChange={onChange}
            className="accent-blue-500"
          />
          Number of Days
        </label>
        <label className="flex items-center gap-2">
          <input
            type="radio"
            name="rangeType"
            value="dates"
            checked={menuData.rangeType === "dates"}
            onChange={onChange}
            className="accent-blue-500"
          />
          Date Range
        </label>
      </div>

      {menuData.rangeType === "days" ? (
        <InputField
          type="number"
          name="days"
          value={menuData.days}
          onChange={onChange}
          placeholder="Days"
          className="w-32"
        />
      ) : (
        <div className="flex gap-2">
          <InputField
            type="date"
            name="startDate"
            value={menuData.startDate}
            onChange={onChange}
            required
          />
          <InputField
            type="date"
            name="endDate"
            value={menuData.endDate}
            onChange={onChange}
            required
          />
        </div>
      )}
    </div>
  );
};

const FormInput = ({ id, label, type, register, rules, error }) => {
  return (
    <div>
      <label className="mb-4 text-white" htmlFor={id}>
        {label}
      </label>
      <input
        id={id}
        type={type}
        className={`mb-4 py-2 px-4 rounded-lg border ${
          error ? "border-red-500" : ""
        }`}
        onInput={(e) => (e.target.value = e.target.value.replace(/\s/g, ""))}
        {...register(id, rules)}
      />
      {error && <p className="mb-2 text-red-500">{error.message}</p>}
    </div>
  );
};

export { DaysOrRange, MacroField, InputField, FormInput };

import React from "react";

const InputField = ({
  label,
  name,
  type = "text",
  value,
  onChange,
  placeholder,
  className,
}) => (
  <div className="flex flex-col gap-2 w-full">
    {label && (
      <label className="font-semibold text-gray-700 text-sm">{label}</label>
    )}
    <input
      type={type}
      name={name}
      value={value}
      onChange={onChange}
      placeholder={placeholder}
      required
      className={`rounded-xl border border-gray-300 bg-gray-50 px-3 py-2 text-gray-900 placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-green-400 focus:border-transparent transition-all duration-200 ${
        className || ""
      }`}
    />
  </div>
);

const DaysOrRange = ({ menuData, onChange }) => {
  const { rangeType, startDate, endDate } = menuData;

  return (
    <div className="flex flex-col gap-4">
      <div className="flex flex-wrap items-center gap-4 text-sm font-medium text-gray-700">
        <label className="flex items-center gap-2 cursor-pointer">
          <input
            type="radio"
            name="rangeType"
            value="days"
            checked={rangeType === "days"}
            onChange={onChange}
            className="text-green-500 focus:ring-green-400"
          />
          Number of Days
        </label>
        <label className="flex items-center gap-2 cursor-pointer">
          <input
            type="radio"
            name="rangeType"
            value="dates"
            checked={rangeType === "dates"}
            onChange={onChange}
            className="text-green-500 focus:ring-green-400"
          />
          Date Range
        </label>
      </div>

      {rangeType === "days" ? (
        <InputField
          type="text"
          name="days"
          value={menuData.days}
          onChange={onChange}
          placeholder="Days"
          className="w-32"
        />
      ) : (
        <div className="flex flex-col sm:flex-row gap-3">
          <InputField
            type="date"
            name="startDate"
            value={startDate}
            onChange={onChange}
            required
          />
          <InputField
            type="date"
            name="endDate"
            value={endDate}
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
    <div className="flex flex-col gap-1">
      <label htmlFor={id} className="field-label">
        {label}
      </label>
      <input
        id={id}
        type={type}
        className={`rounded-xl border bg-gray-50 px-3 py-2 text-gray-900 placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-green-400 focus:border-transparent transition-all duration-200 ${
          error ? "border-red-500 focus:ring-red-400" : "border-gray-300"
        }`}
        onInput={(e) => (e.target.value = e.target.value.replace(/\s/g, ""))}
        {...register(id, rules)}
      />
      {error && <p className="text-sm text-red-500 mt-1">{error.message}</p>}
    </div>
  );
};

export { DaysOrRange, InputField, FormInput };

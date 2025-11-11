import { FormInput } from "components/common/Inputs";
import React from "react";
import { useForm } from "react-hook-form";

const NewMealDialog = ({ show, onSubmit }) => {
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm();

  const rules = {
    name: {
      required: "Name cannot be empty",
      maxLength: { value: 20, message: "Too long" },
    },
    targetKcal: {
      required: "Calories cannot be empty",
      pattern: {
        value: /^[0-9]*$/i,
        message: "Invalid email address",
      },
    },
    password: {
      required: "Password is required",
      minLength: { value: 8, message: "Minimum 8 characters" },
    },
  };

  return (
    <form
      onSubmit={handleSubmit(onSubmit)}
      className={`p-6 bg-white border border-gray-200 shadow-md flex flex-col gap-4 rounded-2xl w-full sm:w-80 transition-all ${
        !show ? "collapse" : ""
      }`}
    >
      {/* Name */}
      <FormInput
        id="name"
        label="Name"
        type="text"
        register={register}
        rules={rules.name}
        error={errors.name}
      />

      {/* Calories */}
      <FormInput
        id="targetKcal"
        label="Calories"
        type="number"
        register={register}
        rules={rules.email}
        error={errors.email}
      />

      {/* Submit */}
      <input
        type="submit"
        value="Add"
        className="mt-4 py-2.5 px-6 font-semibold text-white bg-green-500 rounded-xl shadow-sm hover:bg-green-600 active:scale-95 cursor-pointer transition-all duration-200"
      />
    </form>
  );
};

export default NewMealDialog;

import { FormInput } from "components/common/Inputs";
import React, { useState } from "react";
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
      className={`p-4 bg-slate-400 shadow flex flex-col w-min h-min rounded-xl ${
        !show ? "collapse" : ""
      }`}
      onSubmit={handleSubmit(onSubmit)}
    >
      <FormInput
        id="name"
        label="Name"
        type="text"
        register={register}
        rules={rules.name}
        error={errors.name}
      />

      <FormInput
        id="targetKcal"
        label="Calories"
        type="number"
        register={register}
        rules={rules.email}
        error={errors.email}
      />

      <input
        className="mt-6 py-2 px-6 text-white bg-blue-600 rounded-full cursor-pointer"
        type="submit"
        value="Add"
      />
    </form>
  );
};

export default NewMealDialog;

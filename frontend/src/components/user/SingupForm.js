import React, { useState } from "react";
import { useForm } from "react-hook-form";
import { FormInput } from "components/common/Inputs";
import { registerUser } from "api";

const SignupForm = () => {
  const {
    register,
    handleSubmit,
    watch,
    formState: { errors },
  } = useForm();

  const [serverError, setServerError] = useState("");

  const rules = {
    login: {
      required: "Login is required",
      minLength: { value: 5, message: "Login must be at least 5 characters" },
      maxLength: { value: 30, message: "Login must be at most 30 characters" },
      pattern: {
        value: /^\S+$/,
        message: "No spaces allowed",
      },
    },
    email: {
      required: "Email is required",
      pattern: {
        value: /^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,}$/i,
        message: "Invalid email address",
      },
    },
    password: {
      required: "Password is required",
      minLength: { value: 8, message: "Minimum 8 characters" },
    },
    confirmPassword: {
      required: "Please confirm your password",
      validate: (value) =>
        value === watch("password") || "Passwords do not match",
    },
  };

  const onSubmit = async (data) => {
    try {
      const { confirmPassword, ...payload } = data;
      const response = await registerUser(payload);
      setServerError("");
    } catch (error) {
      if (error.response.status === 409)
        setServerError(error.response.data.message);
    }
  };

  return (
    <form
      className="p-8 bg-slate-700 flex flex-col w-min h-min m-auto rounded-2xl"
      onSubmit={handleSubmit(onSubmit)}
    >
      <FormInput
        id="login"
        label="Login"
        type="text"
        register={register}
        rules={rules.login}
        error={errors.login}
      />

      <FormInput
        id="email"
        label="Email"
        type="email"
        register={register}
        rules={rules.email}
        error={errors.email}
      />

      <FormInput
        id="password"
        label="Password"
        type="password"
        register={register}
        rules={rules.password}
        error={errors.password}
      />

      <FormInput
        id="confirmPassword"
        label="Confirm password"
        type="password"
        register={register}
        rules={rules.confirmPassword}
        error={errors.confirmPassword}
      />
      {serverError && (
        <p className="mb-4 text-center text-red-500">{serverError}</p>
      )}
      <input
        className="mt-6 py-2 px-6 text-white bg-blue-600 rounded-full cursor-pointer"
        type="submit"
        value="Sign up"
      />
    </form>
  );
};

export default SignupForm;

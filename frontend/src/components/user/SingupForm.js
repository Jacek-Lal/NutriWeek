import React, { useState } from "react";
import { useForm } from "react-hook-form";
import { FormInput } from "components/common/Inputs";
import { getCsrf, registerUser } from "api";

const SignupForm = ({ onSuccess }) => {
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
      await getCsrf();

      const { confirmPassword, ...payload } = data;
      const response = await registerUser(payload);
      onSuccess();
      setServerError("");
    } catch (error) {
      setServerError(error?.response?.data?.message);
    }
  };

  return (
    <form onSubmit={handleSubmit(onSubmit)} className="space-y-5">
      {/* Login */}
      <FormInput
        id="login"
        label="Login"
        type="text"
        register={register}
        rules={rules.login}
        error={errors.login}
      />

      {/* Email */}
      <FormInput
        id="email"
        label="Email"
        type="email"
        register={register}
        rules={rules.email}
        error={errors.email}
      />

      {/* Password */}
      <FormInput
        id="password"
        label="Password"
        type="password"
        register={register}
        rules={rules.password}
        error={errors.password}
      />

      {/* Confirm Password */}
      <FormInput
        id="confirmPassword"
        label="Confirm password"
        type="password"
        register={register}
        rules={rules.confirmPassword}
        error={errors.confirmPassword}
      />

      {serverError && (
        <h1 className="text-center text-red-600">{serverError}</h1>
      )}
      {/* Submit Button */}
      <button
        type="submit"
        className="w-full bg-green-500 text-white font-semibold py-2.5 rounded-xl shadow-sm hover:bg-green-600 transition-all duration-200"
      >
        Sign Up
      </button>
    </form>
  );
};

export default SignupForm;

import React, { useRef, useState } from "react";
import { useForm } from "react-hook-form";
import { TailSpin } from "react-loader-spinner";
import { FormInput } from "components/common/Inputs";
import { registerUser } from "api";

const SignupForm = ({ onSuccess }) => {
  const {
    register,
    handleSubmit,
    watch,
    formState: { errors },
  } = useForm();

  const [serverError, setServerError] = useState("");
  const [loading, setLoading] = useState(false);
  const confirmModalRef = useRef();
  const [warningConfirmed, setWarningConfirmed] = useState(false);

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
    setLoading(true);
    setServerError("");

    try {
      const { confirmPassword, ...payload } = data;
      await registerUser(payload);
      onSuccess();
    } catch (error) {
      setServerError(
        error?.response?.data?.message ?? "Register failed. Please try again."
      );
    }
    setLoading(false);
  };

  return (
    <>
      <form
        onSubmit={(e) => {
          e.preventDefault();
          if (warningConfirmed) {
            handleSubmit(onSubmit)();
          } else {
            confirmModalRef.current?.showModal();
          }
        }}
        className="space-y-5"
      >
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
          {loading ? (
            <TailSpin height="20" width="20" visible={true} color="white" />
          ) : (
            "Sign Up"
          )}
        </button>
      </form>

      {/* Warning dialog window */}
      <dialog
        ref={confirmModalRef}
        className="max-w-[60%] p-6 rounded-xl bg-white shadow-xl"
      >
        <div className="mb-6">
          <i className="bi bi-exclamation-triangle text-yellow-600" />{" "}
          <strong>Important note:</strong> <br />
          This application is hosted on a free tier. The first request after a
          period of inactivity may take up a few minutes to respond. Subsequent
          requests will be significantly faster.
        </div>

        <div className="flex justify-end gap-4">
          <button
            type="button"
            onClick={() => confirmModalRef.current?.close()}
            className="px-4 py-2 rounded bg-gray-300 hover:bg-gray-400"
          >
            Cancel
          </button>
          <button
            type="button"
            onClick={() => {
              setWarningConfirmed(true);
              confirmModalRef.current?.close();
              handleSubmit(onSubmit)();
            }}
            className="inline-flex items-center justify-center w-24 px-4 py-2 rounded bg-red-600 text-white hover:bg-red-700"
          >
            Proceed
          </button>
        </div>
      </dialog>
    </>
  );
};

export default SignupForm;

import React, { useState } from "react";
import { useForm } from "react-hook-form";
import { FormInput } from "components/common/Inputs";
import { useNavigate } from "react-router-dom";
import { useContext } from "react";
import { AuthContext } from "context/AuthContext";
const LoginForm = () => {
  const { login } = useContext(AuthContext);
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm();

  const [serverError, setServerError] = useState("");
  const navigate = useNavigate();

  const onSubmit = async (data) => {
    setServerError("");

    try {
      const response = await login(data);
      console.log(response);
      navigate(response.data.redirect);
    } catch (error) {
      console.log(error);
      if (error.response?.status === 401) {
        setServerError("Invalid username or password");
      } else {
        setServerError("Something went wrong. Please try again.");
      }
    }
  };

  return (
    <form
      className="p-8 bg-slate-700 flex flex-col w-min h-min m-auto rounded-2xl"
      onSubmit={handleSubmit(onSubmit)}
    >
      <FormInput
        id="username"
        label="Username"
        type="text"
        register={register}
        rules={{ required: "Login is required" }}
        error={errors.login}
      />

      <FormInput
        id="password"
        label="Password"
        type="password"
        register={register}
        rules={{ required: "Password is required" }}
        error={errors.password}
      />

      {serverError && (
        <p className="mb-4 text-center text-red-500">{serverError}</p>
      )}
      <input
        className="mt-6 py-2 px-6 text-white bg-blue-600 rounded-full cursor-pointer"
        type="submit"
        value="Log in"
      />
    </form>
  );
};

export default LoginForm;

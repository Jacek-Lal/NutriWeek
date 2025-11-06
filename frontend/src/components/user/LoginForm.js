import React, { useState } from "react";
import { useForm } from "react-hook-form";
import { FormInput } from "components/common/Inputs";
import { useNavigate } from "react-router-dom";
import { useAuth } from "hooks/useAuth";
import { getCsrf } from "api";
const LoginForm = () => {
  const { login } = useAuth();
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
      await getCsrf();
      const response = await login(data);
      navigate(response.data.redirect);
    } catch (error) {
      if (error.response?.status === 401) {
        setServerError("Invalid username or password");
      } else {
        setServerError("Something went wrong. Please try again.");
      }
    }
  };

  return (
    <form className="card mx-auto" onSubmit={handleSubmit(onSubmit)}>
      <div className="space-y-4">
        <div>
          <label htmlFor="username" className="field-label">
            Username
          </label>
          <FormInput
            id="username"
            label="Username"
            type="text"
            register={register}
            rules={{ required: "Login is required" }}
            error={errors.login}
          />
        </div>

        <div>
          <label htmlFor="password" className="field-label">
            Password
          </label>
          <FormInput
            id="password"
            label="Password"
            type="password"
            register={register}
            rules={{ required: "Password is required" }}
            error={errors.password}
          />
        </div>

        {serverError && (
          <p className="text-center text-red-500 text-sm">{serverError}</p>
        )}

        <button type="submit" className="btn-primary w-full">
          Log in
        </button>
      </div>
    </form>
  );
};

export default LoginForm;

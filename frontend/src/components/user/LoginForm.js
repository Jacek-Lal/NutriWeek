import React, { useState } from "react";
import { useForm } from "react-hook-form";
import { FormInput } from "components/common/Inputs";
import { useNavigate } from "react-router-dom";
import { useAuth } from "hooks/useAuth";
import { TailSpin } from "react-loader-spinner";

const LoginForm = () => {
  const { login } = useAuth();
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm();

  const [serverError, setServerError] = useState("");
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  const onSubmit = async (data) => {
    setLoading(true);
    setServerError("");
  
    try {
      const response = await login(data);
      navigate(response.data.redirect);
    } catch (error) {
      setServerError(error.response.data);
    }

    setLoading(false);
  };

  return (
    <form className="card mx-auto" onSubmit={handleSubmit(onSubmit)}>
      <div className="space-y-4">
        <div>
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

        <button type="submit" className="btn-primary w-full flex justify-center" disabled={loading}>
          {loading ? 
          <TailSpin
            height="20"
            width="20"
            visible={true}
            color="white"
          /> : "Log in"}
        </button>
      </div>
    </form>
  );
};

export default LoginForm;

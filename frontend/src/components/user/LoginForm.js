import React, { useRef, useState } from "react";
import { useForm } from "react-hook-form";
import { useNavigate } from "react-router-dom";
import { TailSpin } from "react-loader-spinner";
import { FormInput } from "components/common/Inputs";
import { useAuth } from "hooks/useAuth";

const LoginForm = () => {
  const { login } = useAuth();
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm();

  const [serverError, setServerError] = useState("");
  const navigate = useNavigate();
  const [loading, setLoading] = useState(false);
  const confirmModalRef = useRef();
  const [warningConfirmed, setWarningConfirmed] = useState(false);

  const onSubmit = async (data) => {
    setLoading(true);
    setServerError("");

    try {
      const response = await login(data);
      navigate(response.data.redirect);
    } catch (error) {
      setServerError(
        error?.response?.data ?? "Login failed. Please try again."
      );
    }

    setLoading(false);
  };

  return (
    <>
      <form
        className="card mx-auto"
        onSubmit={(e) => {
          e.preventDefault();
          if (warningConfirmed) {
            handleSubmit(onSubmit)();
          } else {
            confirmModalRef.current?.showModal();
          }
        }}
      >
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

          <button
            type="submit"
            className="btn-primary w-full flex justify-center"
            disabled={loading}
          >
            {loading ? (
              <TailSpin height="20" width="20" visible={true} color="white" />
            ) : (
              "Log in"
            )}
          </button>
        </div>
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

export default LoginForm;

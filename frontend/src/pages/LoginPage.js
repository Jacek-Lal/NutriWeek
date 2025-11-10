import LoginForm from "components/user/LoginForm";
import React, { useEffect, useState } from "react";
import { useLocation } from "react-router-dom";
import { toast } from "react-toastify";

const LoginPage = () => {
  const location = useLocation();

  useEffect(() => {
    const params = new URLSearchParams(location.search);
    const status = params.get("status");

    if (!status) return;

    switch (status) {
      case "success":
        toast.success("✅ Your email has been verified. You can now log in.");
        break;
      case "already":
        toast.info("ℹ️ Your account is already verified.");
        break;
      case "expired":
        toast.warning(
          "⚠️ Your verification link has expired. Please request a new one."
        );
        break;
      case "invalid":
        toast.error("❌ Invalid verification link.");
        break;
      default:
        break;
    }
  }, [location]);

  return (
    <div className="min-h-screen bg-gradient-to-br from-orange-50 via-orange-50 to-white flex items-center justify-center px-4 py-10">
      <div className="w-full max-w-md">
        <div className="text-center mb-6">
          <h1 className="text-3xl font-extrabold text-green-600">
            Nutri<span className="text-orange-500">Week</span>
          </h1>
          <p className="text-gray-500 mt-1">Log in to your account</p>
        </div>

        <LoginForm />

        <p className="text-center text-gray-400 text-xs mt-6">
          © {new Date().getFullYear()} NutriWeek
        </p>
      </div>
    </div>
  );
};

export default LoginPage;

import LoginForm from "components/user/LoginForm";
import React from "react";

const LoginPage = () => {
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

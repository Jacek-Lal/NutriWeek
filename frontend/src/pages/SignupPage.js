import React, { useState } from "react";
import { Link } from "react-router-dom";
import SignupForm from "../components/user/SingupForm";

export default function SignupPage() {
  const [submitted, setSubmitted] = useState(false);

  return (
    <div className="min-h-screen bg-gradient-to-br from-orange-50 via-orange-50 to-white flex items-center justify-center px-4 py-10">
      <div className="w-full max-w-md">
        {/* Header */}
        <div className="text-center mb-8">
          <h1 className="text-4xl font-extrabold text-green-600">
            Nutri<span className="text-orange-400">Week</span>
          </h1>
          <p className="text-gray-600 mt-2">
            Create your free account and start planning your meals
          </p>
        </div>

        {/* Card */}
        <div className="bg-white shadow-lg rounded-2xl p-6 md:p-8">
          {submitted ? (
            <p className="text-center text-green-600 font-medium">
              We've sent a verification link to your email. Please check your
              inbox.
            </p>
          ) : (
            <SignupForm onSuccess={() => setSubmitted(true)} />
          )}
        </div>

        {/* Footer note */}
        <div className="text-center mt-6 text-sm text-gray-600">
          Already have an account?{" "}
          <Link
            to="/login"
            className="text-green-600 font-semibold hover:underline"
          >
            Log in
          </Link>
        </div>

        <p className="text-center text-gray-400 text-xs mt-8">
          © {new Date().getFullYear()} NutriWeek
        </p>
      </div>
    </div>
  );
}

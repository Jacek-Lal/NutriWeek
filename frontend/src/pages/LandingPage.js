import React, { useRef, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { TailSpin } from "react-loader-spinner";
import { useAuth } from "../hooks/useAuth";

export default function LandingPage() {
  const { isLoggedIn, loginDemo } = useAuth();
  const navigate = useNavigate();
  const [loading, setLoading] = useState(false);
  const confirmModalRef = useRef();

  const onLoginDemo = async () => {
    setLoading(true);
    try {
      const response = await loginDemo();
      navigate(response.data.redirect);
    } catch (error) {
      console.error(error.response.data);
    }
    setLoading(false);
  };

  return (
    <div className="min-h-screen bg-gradient-to-b from-orange-50 via-white to-orange-50 text-gray-800 flex flex-col">
      {/* Hero Section */}
      <section className="w-3/4 m-auto flex flex-col md:flex-row items-center justify-between flex-1 px-6 md:px-16 py-10 md:py-20 gap-10">
        {/* Text content */}
        <div className="flex-1 space-y-6 text-center md:text-left">
          <h1 className="text-4xl md:text-5xl font-extrabold text-gray-900 leading-tight">
            Plan your meals.
            <br />
            Eat smarter. Feel better.
          </h1>
          <p className="text-gray-600 text-lg max-w-md mx-auto md:mx-0">
            NutriWeek helps you create balanced meal plans, track ingredients,
            and reach your nutrition goals with ease — all in one place.
          </p>

          {!isLoggedIn ? (
            <div className="flex justify-center md:justify-start gap-4">
              <Link to="/register" className="btn-primary text-lg px-6 py-3">
                Get Started
              </Link>
              <button
                onClick={() => confirmModalRef.current.showModal()}
                className="btn-secondary text-lg px-6 py-3"
              >
                Explore Demo
              </button>
            </div>
          ) : (
            <div className="flex justify-center md:justify-start gap-4">
              <Link to="/menus" className="btn-primary text-lg px-6 py-3">
                Go to Dashboard
              </Link>
            </div>
          )}
        </div>

        {/* Hero Illustration */}
        <div className="flex-1 flex justify-center">
          <div className="relative w-72 h-72 md:w-96 md:h-96">
            <div className="absolute inset-0 bg-green-100 rounded-full blur-3xl opacity-60"></div>
            <img
              src="https://cdn-icons-png.flaticon.com/512/706/706195.png"
              alt="Healthy food illustration"
              className="relative w-full h-full object-contain drop-shadow-md"
            />
          </div>
        </div>
      </section>

      {/* Feature Cards */}
      <section className="px-6 md:px-16 py-10 grid md:grid-cols-3 gap-8">
        <div className="card text-center space-y-4">
          <div className="flex justify-center">
            <div className="w-12 h-12 bg-green-100 text-green-600 rounded-xl flex items-center justify-center text-2xl">
              🥗
            </div>
          </div>
          <h3 className="heading">Personalized Meal Plans</h3>
          <p className="subheading">
            Build weekly meal plans tailored to your goals and preferences.
          </p>
        </div>

        <div className="card text-center space-y-4">
          <div className="flex justify-center">
            <div className="w-12 h-12 bg-orange-100 text-orange-500 rounded-xl flex items-center justify-center text-2xl">
              🍎
            </div>
          </div>
          <h3 className="heading">Track Ingredients</h3>
          <p className="subheading">
            Add products to your meals and easily monitor nutritional values.
          </p>
        </div>

        <div className="card text-center space-y-4">
          <div className="flex justify-center">
            <div className="w-12 h-12 bg-green-100 text-green-600 rounded-xl flex items-center justify-center text-2xl">
              ⏱️
            </div>
          </div>
          <h3 className="heading">Save Time</h3>
          <p className="subheading">
            Stop guessing. Start planning smarter with intuitive tools.
          </p>
        </div>
      </section>

      {/* Footer */}
      <footer className="bg-white border-t border-gray-200 py-6 text-center text-gray-500 text-sm">
        © {new Date().getFullYear()} NutriWeek. Built with ❤️ for better living.
      </footer>

      {/* Warning dialog window */}
      <dialog
        ref={confirmModalRef}
        className="max-w-[60%] p-6 rounded-xl bg-white shadow-xl"
      >
        <div className="mb-6">
          <p className="text-gray-800 mb-4">
            You are about to sign in with a demo account. Any data you create
            will be temporary and removed when you log out.
          </p>

          <p>
            <i className="bi bi-exclamation-triangle text-yellow-600" />{" "}
            <strong>Important note: </strong>
            <br />
            This application is hosted on a free tier. The first request after a
            period of inactivity may take up to a minute to respond. Subsequent
            requests will be significantly faster.
          </p>
        </div>

        <div className="flex justify-end gap-4">
          <button
            onClick={() => confirmModalRef.current.close()}
            className="px-4 py-2 rounded bg-gray-300 hover:bg-gray-400"
            disabled={loading}
          >
            Cancel
          </button>
          <button
            onClick={onLoginDemo}
            className="inline-flex items-center justify-center w-24 px-4 py-2 rounded bg-red-600 text-white hover:bg-red-700"
            disabled={loading}
          >
            {loading ? (
              <TailSpin height="20" width="20" visible={true} color="white" />
            ) : (
              "Proceed"
            )}
          </button>
        </div>
      </dialog>
    </div>
  );
}

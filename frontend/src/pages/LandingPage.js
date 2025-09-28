import React from "react";
import { Link } from "react-router-dom";

const LandingPage = () => {
  return (
    <div className="bg-primary text-secondary-dark30 min-h-screen flex flex-col">
      {/* Hero section */}
      <section className="flex flex-col items-center justify-center text-center flex-1 px-6 py-16">
        <h1 className="text-4xl md:text-5xl font-bold mb-4">
          Welcome to <span className="text-tertiary">NutriWeek</span>
        </h1>
        <p className="max-w-2xl text-lg mb-8">
          Plan, track and optimise your meals with ease. Create weekly menus,
          calculate macros and keep your nutrition on track.
        </p>
        <div className="flex gap-4">
          <Link
            to="/register"
            className="bg-tertiary hover:bg-tertiary/80 px-6 py-3 rounded-lg shadow font-semibold text-white transition"
          >
            Get Started
          </Link>
          <Link
            to="/login"
            className="border border-tertiary hover:bg-tertiary hover:text-white px-6 py-3 rounded-lg shadow font-semibold transition"
          >
            Log In
          </Link>
        </div>
      </section>

      {/* Features section */}
      <section className="bg-secondary-dark60 py-16 px-6 text-primary">
        <h2 className="text-3xl font-bold text-center mb-12">Why NutriWeek?</h2>
        <div className="grid md:grid-cols-3 gap-8 max-w-5xl mx-auto text-secondary-dark80">
          <div className="bg-secondary-tint30 p-6 rounded-xl shadow hover:shadow-lg transition">
            <h3 className="text-xl font-semibold mb-2">Plan Weekly Menus</h3>
            <p>
              Create and manage weekly menus tailored to your goals and track
              your progress day by day.
            </p>
          </div>
          <div className="bg-secondary-tint30 p-6 rounded-xl shadow hover:shadow-lg transition">
            <h3 className="text-xl font-semibold mb-2">Track Macros</h3>
            <p>
              Automatically calculate calories, protein, fat and carbs based on
              your meals and portion sizes.
            </p>
          </div>
          <div className="bg-secondary-tint30 p-6 rounded-xl shadow hover:shadow-lg transition">
            <h3 className="text-xl font-semibold mb-2">
              Personalised Experience
            </h3>
            <p>
              Your data stays secure. Access your menus from any device after
              logging in.
            </p>
          </div>
        </div>
      </section>

      {/* Footer */}
      <footer className="bg-secondary-dark80 py-6 text-center text-primary text-sm">
        © {new Date().getFullYear()} NutriWeek. All rights reserved.
      </footer>
    </div>
  );
};

export default LandingPage;

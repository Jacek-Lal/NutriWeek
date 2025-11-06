import React from "react";
import { Link } from "react-router-dom";
import { useAuth } from "../../hooks/useAuth";

const NavBar = () => {
  const { user, isLoggedIn, logout } = useAuth();

  return (
    <nav className="flex justify-between items-center px-6 md:px-12 py-4 shadow-sm bg-white sticky top-0 z-10">
      <Link
        to="/"
        className="text-2xl font-bold text-green-600 hover:opacity-80 transition-opacity"
      >
        Nutri<span className="text-orange-400">Week</span>
      </Link>

      {!isLoggedIn ? (
        <div className="space-x-4">
          <Link
            to="/login"
            className="btn-ghost text-sm md:text-base font-medium"
          >
            Log in
          </Link>
          <Link
            to="/register"
            className="btn-primary text-sm md:text-base font-medium"
          >
            Sign up
          </Link>
        </div>
      ) : (
        <div className="flex items-center gap-3">
          <span className="text-gray-700 text-sm md:text-base">
            Hi, <span className="font-semibold">{user?.username}</span>
          </span>
          <Link
            to="/menus"
            className="btn-primary text-sm md:text-base font-medium"
          >
            My Menus
          </Link>
          <button
            onClick={logout}
            className="btn-ghost text-sm md:text-base font-medium"
          >
            Logout
          </button>
        </div>
      )}
    </nav>
  );
};

export default NavBar;

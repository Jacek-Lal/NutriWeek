import { BrowserRouter, Routes, Route, Link } from "react-router-dom";
import MenuList from "pages/MenuList.js";
import MenuDetails from "pages/MenuDetails.js";
import LoginForm from "pages/LoginPage.js";
import RegisterForm from "pages/SignupPage.js";
import LandingPage from "pages/LandingPage";
import { useContext } from "react";
import { AuthContext } from "context/AuthContext";
import { ProtectedRoute } from "components/common/ProtectedRoute";

function App() {
  const { user, logout } = useContext(AuthContext);
  return (
    <BrowserRouter>
      <div>
        <nav className="bg-slate-800 flex gap-4 mb-6 p-6">
          <Link to="/" className="text-blue-600 hover:underline">
            Home
          </Link>
          {user ? (
            <>
              <Link to="/menus" className="text-blue-600 hover:underline">
                Menus
              </Link>

              <span className="ml-auto text-white">Hi {user.username}</span>
              <Link
                to="/"
                onClick={logout}
                className="text-blue-600 hover:underline"
              >
                Logout
              </Link>
            </>
          ) : (
            <>
              <Link
                to="/login"
                className="ml-auto text-blue-600 hover:underline"
              >
                Log in
              </Link>
              <Link to="/register" className="text-blue-600 hover:underline">
                Sign up
              </Link>
            </>
          )}
        </nav>
        <div className="p-6">
          <Routes>
            <Route path="/" element={<LandingPage />} />
            <Route
              path="/menus"
              element={
                <ProtectedRoute>
                  <MenuList />
                </ProtectedRoute>
              }
            />
            <Route
              path="/menus/:id"
              element={
                <ProtectedRoute>
                  <MenuDetails />
                </ProtectedRoute>
              }
            />
            <Route path="/login" element={<LoginForm />} />
            <Route path="/register" element={<RegisterForm />} />
          </Routes>
        </div>
      </div>
    </BrowserRouter>
  );
}
export default App;

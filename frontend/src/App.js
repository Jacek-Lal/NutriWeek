import { BrowserRouter, Routes, Route, Link } from "react-router-dom";
import MenuList from "pages/MenuList.js";
import MenuDetails from "pages/MenuDetails.js";
import LoginForm from "pages/LoginPage.js";
import RegisterForm from "pages/SignupPage.js";
import LandingPage from "pages/LandingPage";
import { ProtectedRoute } from "components/common/ProtectedRoute";
import { useAuth } from "hooks/useAuth";

function App() {
  const { user, logout } = useAuth();
  return (
    <BrowserRouter>
      <div>
        <nav className="bg-secondary-dark60 flex gap-4 p-6 text-primary">
          <Link to="/" className=" hover:underline">
            Home
          </Link>
          {user ? (
            <>
              <Link to="/menus" className="hover:underline">
                Menus
              </Link>

              <span className="ml-auto py-2 px-4 text-white">
                Hi {user.username}
              </span>
              <Link
                to="/"
                onClick={logout}
                className="py-2 px-4 hover:underline"
              >
                Logout
              </Link>
            </>
          ) : (
            <>
              <Link
                to="/login"
                className="py-2 px-4 bg-primary text-tertiary rounded-lg ml-auto hover:underline"
              >
                Log in
              </Link>
              <Link
                to="/register"
                className="py-2 px-4 bg-tertiary hover:underline rounded-lg"
              >
                Sign up
              </Link>
            </>
          )}
        </nav>
        <div className="">
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

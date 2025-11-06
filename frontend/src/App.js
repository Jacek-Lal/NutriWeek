import { BrowserRouter, Routes, Route, Link } from "react-router-dom";
import MenuList from "pages/MenuList.js";
import MenuDetails from "pages/MenuDetails.js";
import LoginForm from "pages/LoginPage.js";
import RegisterForm from "pages/SignupPage.js";
import LandingPage from "pages/LandingPage";
import { ProtectedRoute } from "components/common/ProtectedRoute";
import NavBar from "components/common/NavBar";

function App() {
  return (
    <BrowserRouter>
      <NavBar />

      <div>
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

import { BrowserRouter, Routes, Route, Link } from "react-router-dom";
import MenuList from "./pages/MenuList.js";
import MenuDetails from "./pages/MenuDetails.js";

function App() {
  return (
    <BrowserRouter>
      <div className="p-6">
        <nav className="flex gap-4 mb-6">
          <Link to="/menus" className="text-blue-600 hover:underline">
            Menus
          </Link>
        </nav>
        <Routes>
          <Route path="/menus" element={<MenuList />} />
          <Route path="/menus/:id" element={<MenuDetails />} />
        </Routes>
      </div>
    </BrowserRouter>
  );
}
export default App;

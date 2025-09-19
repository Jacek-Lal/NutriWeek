import { BrowserRouter, Routes, Route, Link } from "react-router-dom";
import MenuList from "./pages/MenuList.js";
import MenuDetails from "./pages/MenuDetails.js";

function App() {
  return (
    <BrowserRouter>
      <div>
        <nav className="bg-slate-800 flex gap-4 mb-6 p-6">
          <Link to="/menus" className="text-blue-600 hover:underline">
            Menus
          </Link>
        </nav>
        <div className="p-6">
          <Routes>
            <Route path="/menus" element={<MenuList />} />
            <Route path="/menus/:id" element={<MenuDetails />} />
          </Routes>
        </div>
      </div>
    </BrowserRouter>
  );
}
export default App;

import { createContext, useState, useEffect, useContext } from "react";
import { loginUser, logoutUser, getUser } from "api";
export const AuthContext = createContext(null);

export function AuthProvider({ children }) {
  const [user, setUser] = useState(null);

  useEffect(() => {
    getUser()
      .then((res) => setUser(res.data))
      .catch(() => setUser(null));
  }, []);

  const login = async (credentials) => {
    const response = await loginUser(credentials);
    const { data } = await getUser();
    setUser(data);

    return response;
  };

  const logout = async () => {
    await logoutUser();
    setUser(null);
  };

  return (
    <AuthContext.Provider value={{ user, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
}

export const useAuthContext = () => {
  return useContext(AuthContext);
};

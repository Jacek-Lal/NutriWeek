import { useAuthContext } from "context/AuthContext";

export const useAuth = () => {
  const { user, login, loginDemo, logout } = useAuthContext();

  const isLoggedIn = !!user;

  return { user, isLoggedIn, login, loginDemo, logout };
};

import { useAuthContext } from "context/AuthContext";

export const useAuth = () => {
  const { user, login, logout } = useAuthContext();

  const isLoggedIn = !!user;

  return { user, isLoggedIn, login, logout };
};

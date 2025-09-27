import { AuthContext } from "context/AuthContext";
import React, { useContext } from "react";
import { Navigate } from "react-router-dom";

export const ProtectedRoute = ({ children }) => {
  const { user } = useContext(AuthContext);

  if (!user) {
    return <Navigate to="/login" />;
  }

  return children;
};

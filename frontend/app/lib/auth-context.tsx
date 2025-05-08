"use client";

import { createContext, useContext, useEffect, useState, ReactNode } from "react";
import { login as apiLogin, logout as apiLogout, getCurrentUser } from "./api-services";
import { AuthRequest, UsuarioResponse } from "./types";
import { useRouter } from "next/navigation";

interface AuthContextType {
  user: UsuarioResponse | null;
  isLoading: boolean;
  isAuthenticated: boolean;
  login: (credentials: AuthRequest) => Promise<void>;
  logout: () => void;
  checkAuth: () => Promise<boolean>;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export function AuthProvider({ children }: { children: ReactNode }) {
  const [user, setUser] = useState<UsuarioResponse | null>(null);
  const [isLoading, setIsLoading] = useState(true);
  const router = useRouter();

  useEffect(() => {
    const initAuth = async () => {
      await checkAuth();
      setIsLoading(false);
    };

    initAuth();
  }, []);

  const checkAuth = async (): Promise<boolean> => {
    try {
      if (typeof window !== "undefined") {
        const token = localStorage.getItem("token");
        if (!token) {
          setUser(null);
          return false;
        }

        const userData = await getCurrentUser();
        setUser(userData);
        return true;
      }
      return false;
    } catch (error) {
      console.error("Erro ao verificar autenticação:", error);
      setUser(null);
      return false;
    }
  };

  const login = async (credentials: AuthRequest) => {
    setIsLoading(true);
    try {
      await apiLogin(credentials);
      const userData = await getCurrentUser();
      setUser(userData);
    } catch (error) {
      console.error("Erro ao fazer login:", error);
      throw error;
    } finally {
      setIsLoading(false);
    }
  };

  const logout = () => {
    apiLogout();
    setUser(null);
    router.push("/login");
  };

  return (
    <AuthContext.Provider
      value={{
        user,
        isLoading,
        isAuthenticated: !!user,
        login,
        logout,
        checkAuth,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  const context = useContext(AuthContext);
  if (context === undefined) {
    throw new Error("useAuth deve ser usado dentro de um AuthProvider");
  }
  return context;
}

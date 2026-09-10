import { createContext, useContext, useState } from 'react';
import { post, login as apiLogin } from '../api/client';

const AuthContext = createContext(null);

export function AuthProvider({ children }) {
  const [user, setUser] = useState(null);

  async function login(email, password) {
    const userData = await apiLogin(email, password);
    setUser(userData);
    return userData;
  }

  async function register(name, email, password, role) {
    const userData = await post('/auth/register', { name, email, password, role });
    return userData;
  }

  async function logout() {
    await fetch('http://localhost:8080/api/auth/logout', {
      method: 'POST',
      credentials: 'include',
    });
    setUser(null);
  }

  const value = { user, login, register, logout };

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export function useAuth() {
  return useContext(AuthContext);
}
import React, { createContext, useState, useContext, useEffect } from 'react';
import { authService } from '../services/api';

const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    // Check if user is already logged in
    const token = localStorage.getItem('token');
    if (token) {
      setIsAuthenticated(true);
      const role = localStorage.getItem('role');
      // minimal user reconstruction to satisfy role-based routing
      if (role) {
        setUser({ role });
      }
    }
    setLoading(false);
  }, []);

  const login = async (credentials) => {
    // Dummy credentials for local testing without backend
    const dummyUsers = [
      { email: 'admin@example.com', password: 'admin123', role: 'admin', name: 'Admin User' },
      { email: 'doctor@example.com', password: 'doctor123', role: 'doctor', name: 'Doctor User' },
      { email: 'patient@example.com', password: 'patient123', role: 'patient', name: 'Patient User' },
    ];
    // include locally created demo doctors
    let demoDoctors = [];
    try { demoDoctors = JSON.parse(localStorage.getItem('demoDoctors') || '[]'); } catch {}
    const combined = [...dummyUsers, ...demoDoctors.map(d => ({ email: d.email, password: d.password, role: 'doctor', name: d.name }))];
    const matched = combined.find(u => u.email === credentials?.email && u.password === credentials?.password && (!credentials?.role || credentials.role === u.role));
    if (matched) {
      localStorage.setItem('token', 'dummy-token');
      localStorage.setItem('role', matched.role);
      setUser({ email: matched.email, role: matched.role, name: matched.name });
      setIsAuthenticated(true);
      return { success: true };
    }

    try {
      const response = await authService.login(credentials);
      const { token, user } = response.data;
      
      localStorage.setItem('token', token);
      // persist role for routing
      if (user?.role) {
        localStorage.setItem('role', user.role);
      }
      setUser(user);
      setIsAuthenticated(true);
      return { success: true };
    } catch (error) {
      // Dev fallback: allow fake login when backend auth is not ready
      if (process.env.REACT_APP_ALLOW_FAKE_LOGIN === 'true') {
        const fallbackUser = { email: credentials?.email, role: credentials?.role || 'patient', name: 'Dev User' };
        localStorage.setItem('token', 'dev-fake-token');
        localStorage.setItem('role', fallbackUser.role);
        setUser(fallbackUser);
        setIsAuthenticated(true);
        return { success: true, dev: true };
      }
      return {
        success: false,
        error: error.response?.data?.message || 'Login failed'
      };
    }
  };

  const logout = async () => {
    try {
      await authService.logout();
    } catch (error) {
      console.error('Logout error:', error);
    } finally {
      localStorage.removeItem('token');
      localStorage.removeItem('role');
      setUser(null);
      setIsAuthenticated(false);
    }
  };

  return (
    <AuthContext.Provider value={{ isAuthenticated, user, login, logout, loading }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => useContext(AuthContext);
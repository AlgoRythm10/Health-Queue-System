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
    console.log('Login attempt with credentials:', credentials);
    
    // Dummy credentials for local testing without backend (NO DUMMY DOCTORS)
    const dummyUsers = [
      { email: 'admin@example.com', password: 'admin123', role: 'admin', name: 'Admin User' },
      { email: 'patient@example.com', password: 'patient123', role: 'patient', name: 'Patient User' },
    ];
    // Only include admin-created doctors (no dummy doctors)
    let adminCreatedDoctors = [];
    try { adminCreatedDoctors = JSON.parse(localStorage.getItem('demoDoctors') || '[]'); } catch {}
    const combined = [...dummyUsers, ...adminCreatedDoctors.map(d => ({ email: d.email, password: d.password, role: 'doctor', name: d.name, doctorId: d.doctorId }))];
    console.log('Available users:', combined);
    
    const matched = combined.find(u => u.email === credentials?.email && u.password === credentials?.password && (!credentials?.role || credentials.role === u.role));
    console.log('Matched user:', matched);
    
    if (matched) {
      localStorage.setItem('token', 'dummy-token');
      localStorage.setItem('role', matched.role);
      setUser({ email: matched.email, role: matched.role, name: matched.name });
      setIsAuthenticated(true);
      console.log('Login successful, user set:', { email: matched.email, role: matched.role, name: matched.name });
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
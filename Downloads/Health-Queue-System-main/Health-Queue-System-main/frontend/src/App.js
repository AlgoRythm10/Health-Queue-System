import React from 'react';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { useAuth } from './context/AuthContext';
import AdminLogin from './pages/auth/AdminLogin';
import DoctorLogin from './pages/auth/DoctorLogin';
import PatientAuth from './pages/auth/PatientAuth';
import AdminDashboard from './pages/admin/AdminDashboard';
import DoctorDashboard from './pages/doctor/DoctorDashboard';

const RequireAuth = ({ children, role }) => {
  const { isAuthenticated, user, loading } = useAuth();
  if (loading) return <div className="flex items-center justify-center h-screen">Loading...</div>;
  if (!isAuthenticated) return <Navigate to={`/${role}/login`} replace />;
  if (role && user?.role !== role) return <Navigate to={`/${user?.role || 'patient'}/login`} replace />;
  return children;
};

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/admin/login" element={<AdminLogin />} />
        <Route path="/doctor/login" element={<DoctorLogin />} />
        <Route path="/patient/auth" element={<PatientAuth />} />

        <Route
          path="/doctor"
          element={
            <RequireAuth role="doctor">
              <DoctorDashboard />
            </RequireAuth>
          }
        />

        <Route
          path="/admin"
          element={
            <RequireAuth role="admin">
              <AdminDashboard />
            </RequireAuth>
          }
        />

        <Route path="*" element={<Navigate to="/patient/auth" replace />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;

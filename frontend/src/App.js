import React from 'react';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { useAuth } from './context/AuthContext';
import AdminLogin from './pages/auth/AdminLogin';
import DoctorLogin from './pages/auth/DoctorLogin';
import PatientAuth from './pages/auth/PatientAuth';
import AdminDashboard from './pages/admin/AdminDashboard';
import DoctorManagement from './pages/admin/DoctorManagement';
import DoctorDashboard from './pages/doctor/DoctorDashboard';
import DashboardPage from './pages/DashboardPage';
import BookAppointment from './pages/BookAppointment';
import ViewAppointments from './pages/ViewAppointments';
import QueueStatus from './pages/QueueStatus';

const RequireAuth = ({ children, role }) => {
  const { isAuthenticated, user, loading } = useAuth();
  
  console.log('RequireAuth check:', { isAuthenticated, user, role, loading });
  
  if (loading) return <div className="flex items-center justify-center h-screen">Loading...</div>;
  if (!isAuthenticated) {
    console.log('Not authenticated, redirecting to login');
    return <Navigate to={`/${role}/login`} replace />;
  }
  if (role && user?.role !== role) {
    console.log('Role mismatch, redirecting to appropriate dashboard');
    // Redirect to the appropriate dashboard based on user's role
    if (user?.role === 'admin') return <Navigate to="/admin" replace />;
    if (user?.role === 'doctor') return <Navigate to="/doctor" replace />;
    if (user?.role === 'patient') return <Navigate to="/patient" replace />;
    // If no valid role, redirect to login
    return <Navigate to={`/${role}/login`} replace />;
  }
  console.log('Access granted');
  return children;
};

function App() {
  return (
    <BrowserRouter>
      <Routes>
        {/* Auth Routes - Must come before protected routes */}
        <Route path="/admin/login" element={<AdminLogin />} />
        <Route path="/doctor/login" element={<DoctorLogin />} />
        <Route path="/patient/auth" element={<PatientAuth />} />

        {/* Protected Admin Routes - Must come after login routes */}
        <Route
          path="/admin/doctors"
          element={
            <RequireAuth role="admin">
              <DoctorManagement />
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

        {/* Protected Doctor Routes */}
        <Route
          path="/doctor"
          element={
            <RequireAuth role="doctor">
              <DoctorDashboard />
            </RequireAuth>
          }
        />

        {/* Protected Patient Routes */}
        <Route
          path="/patient/book-appointment"
          element={
            <RequireAuth role="patient">
              <BookAppointment />
            </RequireAuth>
          }
        />
        <Route
          path="/patient/view-appointments"
          element={
            <RequireAuth role="patient">
              <ViewAppointments />
            </RequireAuth>
          }
        />
        <Route
          path="/patient/queue-status"
          element={
            <RequireAuth role="patient">
              <QueueStatus />
            </RequireAuth>
          }
        />
        <Route
          path="/patient"
          element={
            <RequireAuth role="patient">
              <DashboardPage />
            </RequireAuth>
          }
        />

        {/* Default Redirect */}
        <Route path="*" element={<Navigate to="/patient/auth" replace />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;

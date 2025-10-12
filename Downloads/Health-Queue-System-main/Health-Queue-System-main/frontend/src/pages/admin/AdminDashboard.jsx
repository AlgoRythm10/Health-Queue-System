import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { dashboardService } from '../../services/api';
import { useAuth } from '../../context/AuthContext';

const StatCard = ({ label, value }) => (
  <div className="bg-white p-6 rounded-lg shadow">
    <div className="text-sm text-gray-500">{label}</div>
    <div className="text-3xl font-semibold mt-2">{value}</div>
  </div>
);

const AdminDashboard = () => {
  const [stats, setStats] = useState({ patients: 0, doctors: 0, appointments: 0 });
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const { logout } = useAuth();
  const navigate = useNavigate();

  const loadStats = async () => {
    try {
      // Try to get stats from API
      try {
        const res = await dashboardService.getStats();
        setStats({
          patients: res.data?.patients ?? 0,
          doctors: res.data?.doctors ?? 0,
          appointments: res.data?.appointments ?? 0
        });
      } catch (apiError) {
        // Fallback to localStorage data
        console.log('API not available, using localStorage data');
        const doctors = JSON.parse(localStorage.getItem('doctors') || '[]');
        const appointments = JSON.parse(localStorage.getItem('appointments') || '[]');
        const patients = JSON.parse(localStorage.getItem('patients') || '[]');
        
        setStats({
          patients: patients.length,
          doctors: doctors.length,
          appointments: appointments.length
        });
      }
    } catch (e) {
      setError('Failed to load stats');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadStats();
  }, []);

  // Refresh stats when component becomes visible (e.g., returning from doctor management)
  useEffect(() => {
    const handleVisibilityChange = () => {
      if (!document.hidden) {
        loadStats();
      }
    };
    
    document.addEventListener('visibilitychange', handleVisibilityChange);
    return () => document.removeEventListener('visibilitychange', handleVisibilityChange);
  }, []);


  if (loading) return <div className="flex items-center justify-center h-screen">Loading...</div>;

  return (
    <div className="min-h-screen bg-background">
      <div className="max-w-6xl mx-auto p-6">
        <div className="flex items-center justify-between mb-6">
          <h1 className="text-2xl font-semibold">Admin Dashboard</h1>
          <div className="flex space-x-4">
            <button 
              onClick={() => navigate('/admin/doctors')}
              className="text-sm text-white bg-blue-600 px-4 py-2 rounded hover:bg-blue-700"
            >
              Manage Doctors
            </button>
            <button onClick={logout} className="text-sm text-white bg-gray-800 px-3 py-1 rounded">Logout</button>
          </div>
        </div>
        {error && <div className="text-red-600 text-sm mb-4">{error}</div>}
        <div className="grid grid-cols-1 md:grid-cols-3 gap-4 mb-8">
          <StatCard label="Patients" value={stats.patients} />
          <StatCard label="Doctors" value={stats.doctors} />
          <StatCard label="Appointments" value={stats.appointments} />
        </div>

        <div className="bg-white p-6 rounded-lg shadow">
          <h2 className="text-lg font-medium mb-4">Quick Actions</h2>
          <div className="space-y-4">
            <p className="text-gray-600">Use the "Manage Doctors" button above to add, edit, or remove doctors from the system.</p>
            <div className="flex space-x-4">
              <button 
                onClick={() => navigate('/admin/doctors')}
                className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700 transition-colors"
              >
                Manage Doctors
              </button>
              <button 
                onClick={() => { setLoading(true); loadStats(); }}
                className="bg-gray-600 text-white px-4 py-2 rounded hover:bg-gray-700 transition-colors"
              >
                Refresh Stats
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default AdminDashboard;



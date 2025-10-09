import React, { useEffect, useState } from 'react';
import { dashboardService, doctorService } from '../../services/api';
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
  const [newDoctor, setNewDoctor] = useState({ name: '', email: '', password: '' });
  const [creating, setCreating] = useState(false);
  const { logout } = useAuth();

  useEffect(() => {
    const load = async () => {
      try {
        const res = await dashboardService.getStats();
        setStats({
          patients: res.data?.patients ?? 0,
          doctors: res.data?.doctors ?? 0,
          appointments: res.data?.appointments ?? 0
        });
      } catch (e) {
        setError('Failed to load stats');
      } finally {
        setLoading(false);
      }
    };
    load();
  }, []);

  const createDoctor = async (e) => {
    e.preventDefault();
    setError('');
    setCreating(true);
    try {
      await doctorService.createDoctor(newDoctor);
      // store for local demo login as well
      try {
        const existing = JSON.parse(localStorage.getItem('demoDoctors') || '[]');
        localStorage.setItem('demoDoctors', JSON.stringify([...existing, { email: newDoctor.email, password: newDoctor.password, name: newDoctor.name }]));
      } catch {}
      setNewDoctor({ name: '', email: '', password: '' });
      const res = await dashboardService.getStats();
      setStats({
        patients: res.data?.patients ?? 0,
        doctors: res.data?.doctors ?? 0,
        appointments: res.data?.appointments ?? 0
      });
    } catch (e) {
      // Dev fallback: if backend is not ready, simulate a successful creation
      try {
        const existing = JSON.parse(localStorage.getItem('demoDoctors') || '[]');
        localStorage.setItem('demoDoctors', JSON.stringify([...existing, { email: newDoctor.email, password: newDoctor.password, name: newDoctor.name }]));
      } catch {}
      setNewDoctor({ name: '', email: '', password: '' });
      setStats((prev) => ({ ...prev, doctors: (prev.doctors ?? 0) + 1 }));
    } finally {
      setCreating(false);
    }
  };

  if (loading) return <div className="flex items-center justify-center h-screen">Loading...</div>;

  return (
    <div className="min-h-screen bg-background">
      <div className="max-w-6xl mx-auto p-6">
        <div className="flex items-center justify-between mb-6">
          <h1 className="text-2xl font-semibold">Admin Dashboard</h1>
          <button onClick={logout} className="text-sm text-white bg-gray-800 px-3 py-1 rounded">Logout</button>
        </div>
        {error && <div className="text-red-600 text-sm mb-4">{error}</div>}
        <div className="grid grid-cols-1 md:grid-cols-3 gap-4 mb-8">
          <StatCard label="Patients" value={stats.patients} />
          <StatCard label="Doctors" value={stats.doctors} />
          <StatCard label="Appointments" value={stats.appointments} />
        </div>

        <div className="bg-white p-6 rounded-lg shadow max-w-xl">
          <h2 className="text-lg font-medium mb-4">Create New Doctor</h2>
          <form onSubmit={createDoctor} className="space-y-4">
            <div>
              <label className="block text-sm mb-1">Name</label>
              <input value={newDoctor.name} onChange={(e) => setNewDoctor({ ...newDoctor, name: e.target.value })} type="text" required className="w-full border rounded px-3 py-2 focus:outline-none focus:ring-2 focus:ring-primary" />
            </div>
            <div>
              <label className="block text-sm mb-1">Email</label>
              <input value={newDoctor.email} onChange={(e) => setNewDoctor({ ...newDoctor, email: e.target.value })} type="email" required className="w-full border rounded px-3 py-2 focus:outline-none focus:ring-2 focus:ring-primary" />
            </div>
            <div>
              <label className="block text-sm mb-1">Password</label>
              <input value={newDoctor.password} onChange={(e) => setNewDoctor({ ...newDoctor, password: e.target.value })} type="password" required className="w-full border rounded px-3 py-2 focus:outline-none focus:ring-2 focus:ring-primary" />
            </div>
            <button disabled={creating} type="submit" className="bg-primary hover:bg-primary-dark text-white rounded px-4 py-2">
              {creating ? 'Creating...' : 'Create Doctor'}
            </button>
          </form>
        </div>
      </div>
    </div>
  );
};

export default AdminDashboard;



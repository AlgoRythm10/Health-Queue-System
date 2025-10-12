import React, { useEffect, useMemo, useState } from 'react';
import { appointmentService, queueService } from '../../services/api';
import { useAuth } from '../../context/AuthContext';

const StatCard = ({ label, value }) => (
  <div className="bg-white p-5 rounded-lg shadow">
    <div className="text-sm text-gray-500">{label}</div>
    <div className="text-2xl font-semibold mt-1">{value}</div>
  </div>
);

const DoctorDashboard = () => {
  const { user } = useAuth();
  const [appointments, setAppointments] = useState([]);
  const [queue, setQueue] = useState({ waiting: 0, nextPatient: null });
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    const load = async () => {
      try {
        const [apptRes, queueRes] = await Promise.all([
          appointmentService.getAppointments().catch(() => ({ data: [] })),
          queueService.getQueueStatus().catch(() => ({ data: { waiting: 0, nextPatient: null } }))
        ]);

        const appts = Array.isArray(apptRes.data) ? apptRes.data : [];
        setAppointments(appts);

        const qd = queueRes.data || {};
        setQueue({ waiting: qd.waiting ?? 0, nextPatient: qd.nextPatient ?? null });
      } catch (e) {
        setError('Failed to load data');
      } finally {
        setLoading(false);
      }
    };
    load();
  }, []);

  const today = new Date().toISOString().slice(0, 10);
  const todaysAppointments = useMemo(() => {
    if (!Array.isArray(appointments)) return [];
    return appointments.filter((a) => {
      const d = a?.date || a?.appointmentDate || a?.scheduledAt;
      return typeof d === 'string' && d.startsWith(today);
    });
  }, [appointments, today]);

  if (loading) return <div className="flex items-center justify-center h-screen">Loading...</div>;

  return (
    <div className="min-h-screen bg-background">
      <div className="max-w-6xl mx-auto p-6 space-y-6">
        <div className="flex items-center justify-between">
          <div>
            <h1 className="text-2xl font-semibold">Welcome{user?.name ? `, ${user.name}` : ''}</h1>
            <div className="text-sm text-gray-500">Doctor workspace</div>
          </div>
        </div>

        {error && <div className="text-red-600 text-sm">{error}</div>}

        <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
          <StatCard label="Today's Appointments" value={todaysAppointments.length} />
          <StatCard label="Waiting in Queue" value={queue.waiting} />
          <StatCard label="Next Patient" value={queue.nextPatient?.name || '—'} />
        </div>

        <div className="bg-white rounded-lg shadow">
          <div className="p-5 border-b font-medium">Upcoming Appointments</div>
          <div className="divide-y">
            {todaysAppointments.length === 0 && (
              <div className="p-5 text-sm text-gray-500">No appointments for today.</div>
            )}
            {todaysAppointments.map((a, idx) => (
              <div key={idx} className="p-5 flex items-center justify-between">
                <div>
                  <div className="font-medium">{a?.patientName || a?.patient?.name || 'Patient'}</div>
                  <div className="text-xs text-gray-500">{(a?.time || a?.appointmentTime || '').toString()}</div>
                </div>
                <div className="text-xs text-gray-500">{(a?.reason || a?.notes) ? String(a.reason || a.notes) : ''}</div>
              </div>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
};

export default DoctorDashboard;



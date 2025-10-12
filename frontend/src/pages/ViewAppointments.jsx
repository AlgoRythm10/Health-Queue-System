import React, { useState, useEffect } from 'react';
import { useAuth } from '../context/AuthContext';
import api from '../services/api';

const ViewAppointments = () => {
  const { user } = useAuth();
  const [appointments, setAppointments] = useState([]);
  const [loading, setLoading] = useState(true);
  const [message, setMessage] = useState('');
  const [messageType, setMessageType] = useState('');

  useEffect(() => {
    fetchAppointments();
  }, []);

  const fetchAppointments = async () => {
    try {
      setLoading(true);
      const response = await api.get(`/appointments/patient/${user.id}`);
      setAppointments(response.data);
    } catch (error) {
      console.error('Error fetching appointments:', error);
      setMessage('Failed to load appointments');
      setMessageType('error');
    } finally {
      setLoading(false);
    }
  };

  const cancelAppointment = async (appointmentId) => {
    if (!window.confirm('Are you sure you want to cancel this appointment?')) {
      return;
    }

    try {
      await api.patch(`/appointments/${appointmentId}/cancel`);
      setMessage('Appointment cancelled successfully');
      setMessageType('success');
      fetchAppointments(); // Refresh the list
    } catch (error) {
      console.error('Error cancelling appointment:', error);
      setMessage(error.response?.data?.message || 'Failed to cancel appointment');
      setMessageType('error');
    }
  };

  const getStatusColor = (status) => {
    switch (status?.toLowerCase()) {
      case 'scheduled':
        return 'bg-blue-100 text-blue-800';
      case 'confirmed':
        return 'bg-green-100 text-green-800';
      case 'completed':
        return 'bg-gray-100 text-gray-800';
      case 'cancelled':
        return 'bg-red-100 text-red-800';
      case 'no_show':
        return 'bg-orange-100 text-orange-800';
      default:
        return 'bg-gray-100 text-gray-800';
    }
  };

  const formatDate = (dateString) => {
    const date = new Date(dateString);
    return date.toLocaleDateString('en-US', {
      weekday: 'long',
      year: 'numeric',
      month: 'long',
      day: 'numeric'
    });
  };

  const formatTime = (timeString) => {
    if (!timeString) return 'N/A';
    const [hours, minutes] = timeString.split(':');
    const hour = parseInt(hours);
    const ampm = hour >= 12 ? 'PM' : 'AM';
    const displayHour = hour % 12 || 12;
    return `${displayHour}:${minutes} ${ampm}`;
  };

  const canCancelAppointment = (appointment) => {
    const appointmentDateTime = new Date(`${appointment.appointmentDate}T${appointment.appointmentTime}`);
    const now = new Date();
    const hoursDifference = (appointmentDateTime - now) / (1000 * 60 * 60);
    
    return appointment.status === 'SCHEDULED' && hoursDifference > 24;
  };

  if (loading) {
    return (
      <div className="min-h-screen bg-gray-50 py-8">
        <div className="max-w-4xl mx-auto px-4">
          <div className="flex items-center justify-center h-64">
            <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600"></div>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gray-50 py-8">
      <div className="max-w-4xl mx-auto px-4">
        <div className="bg-white rounded-lg shadow-md p-6">
          <h1 className="text-2xl font-bold text-gray-800 mb-6">My Appointments</h1>
          
          {message && (
            <div className={`p-4 rounded-lg mb-6 ${
              messageType === 'success' 
                ? 'bg-green-100 text-green-700 border border-green-200' 
                : 'bg-red-100 text-red-700 border border-red-200'
            }`}>
              {message}
            </div>
          )}

          {appointments.length === 0 ? (
            <div className="text-center py-8">
              <div className="text-gray-500 mb-4">
                <svg className="mx-auto h-12 w-12" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M8 7V3a1 1 0 011-1h6a1 1 0 011 1v4h3a2 2 0 012 2v6a2 2 0 01-2 2H5a2 2 0 01-2-2V9a2 2 0 012-2h3z" />
                </svg>
              </div>
              <p className="text-gray-500">No appointments found</p>
            </div>
          ) : (
            <div className="space-y-4">
              {appointments.map((appointment) => (
                <div key={appointment.id} className="border border-gray-200 rounded-lg p-4 hover:shadow-md transition-shadow">
                  <div className="flex justify-between items-start">
                    <div className="flex-1">
                      <div className="flex items-center gap-3 mb-2">
                        <h3 className="text-lg font-semibold text-gray-800">
                          Dr. {appointment.doctor?.name || 'N/A'}
                        </h3>
                        <span className={`px-2 py-1 rounded-full text-xs font-medium ${getStatusColor(appointment.status)}`}>
                          {appointment.status}
                        </span>
                      </div>
                      
                      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                        <div>
                          <p className="text-sm text-gray-600 mb-1">
                            <span className="font-medium">Specialization:</span> {appointment.doctor?.specialization || 'N/A'}
                          </p>
                          <p className="text-sm text-gray-600 mb-1">
                            <span className="font-medium">Date:</span> {formatDate(appointment.appointmentDate)}
                          </p>
                          <p className="text-sm text-gray-600">
                            <span className="font-medium">Time:</span> {formatTime(appointment.appointmentTime)}
                          </p>
                        </div>
                        
                        <div>
                          <p className="text-sm text-gray-600 mb-1">
                            <span className="font-medium">Reason:</span> {appointment.reason || 'N/A'}
                          </p>
                          {appointment.notes && (
                            <p className="text-sm text-gray-600">
                              <span className="font-medium">Notes:</span> {appointment.notes}
                            </p>
                          )}
                        </div>
                      </div>
                    </div>
                    
                    <div className="ml-4">
                      {canCancelAppointment(appointment) && (
                        <button
                          onClick={() => cancelAppointment(appointment.id)}
                          className="px-4 py-2 bg-red-600 text-white text-sm rounded-lg hover:bg-red-700 focus:outline-none focus:ring-2 focus:ring-red-500 focus:ring-offset-2 transition duration-200"
                        >
                          Cancel
                        </button>
                      )}
                    </div>
                  </div>
                </div>
              ))}
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default ViewAppointments;
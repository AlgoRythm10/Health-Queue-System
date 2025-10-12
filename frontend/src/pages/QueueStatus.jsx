import React, { useState, useEffect } from 'react';
import { useAuth } from '../context/AuthContext';
import api from '../services/api';

const QueueStatus = () => {
  const { user } = useAuth();
  const [queueData, setQueueData] = useState([]);
  const [userPosition, setUserPosition] = useState(null);
  const [loading, setLoading] = useState(true);
  const [message, setMessage] = useState('');
  const [messageType, setMessageType] = useState('');
  const [refreshInterval, setRefreshInterval] = useState(null);

  useEffect(() => {
    fetchQueueStatus();
    
    // Set up auto-refresh every 30 seconds
    const interval = setInterval(fetchQueueStatus, 30000);
    setRefreshInterval(interval);

    // Cleanup interval on component unmount
    return () => {
      if (interval) {
        clearInterval(interval);
      }
    };
  }, []);

  const fetchQueueStatus = async () => {
    try {
      // Fetch general queue status
      const queueResponse = await api.get('/queue');
      setQueueData(queueResponse.data);

      // Fetch user's position in queue
      try {
        const positionResponse = await api.get(`/queue/patient/${user.id}/position`);
        setUserPosition(positionResponse.data);
      } catch (error) {
        // User might not be in queue - that's okay
        setUserPosition(null);
      }
    } catch (error) {
      console.error('Error fetching queue status:', error);
      setMessage('Failed to load queue information');
      setMessageType('error');
    } finally {
      setLoading(false);
    }
  };

  const joinQueue = async (doctorId) => {
    try {
      await api.post('/queue/join', {
        patientId: user.id,
        doctorId: doctorId
      });
      setMessage('Successfully joined the queue!');
      setMessageType('success');
      fetchQueueStatus(); // Refresh data
    } catch (error) {
      console.error('Error joining queue:', error);
      setMessage(error.response?.data?.message || 'Failed to join queue');
      setMessageType('error');
    }
  };

  const leaveQueue = async () => {
    if (!window.confirm('Are you sure you want to leave the queue?')) {
      return;
    }

    try {
      await api.delete(`/queue/patient/${user.id}/leave`);
      setMessage('You have left the queue');
      setMessageType('success');
      setUserPosition(null);
      fetchQueueStatus(); // Refresh data
    } catch (error) {
      console.error('Error leaving queue:', error);
      setMessage(error.response?.data?.message || 'Failed to leave queue');
      setMessageType('error');
    }
  };

  const getEstimatedWaitTime = (position, averageConsultationTime = 15) => {
    if (!position || position <= 0) return '0 minutes';
    const waitTimeMinutes = (position - 1) * averageConsultationTime;
    if (waitTimeMinutes < 60) {
      return `${waitTimeMinutes} minutes`;
    }
    const hours = Math.floor(waitTimeMinutes / 60);
    const minutes = waitTimeMinutes % 60;
    return `${hours}h ${minutes}m`;
  };

  const getQueueStatusColor = (length) => {
    if (length === 0) return 'text-green-600';
    if (length <= 3) return 'text-yellow-600';
    if (length <= 6) return 'text-orange-600';
    return 'text-red-600';
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
          <div className="flex justify-between items-center mb-6">
            <h1 className="text-2xl font-bold text-gray-800">Queue Status</h1>
            <button
              onClick={fetchQueueStatus}
              className="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2 transition duration-200"
            >
              Refresh
            </button>
          </div>
          
          {message && (
            <div className={`p-4 rounded-lg mb-6 ${
              messageType === 'success' 
                ? 'bg-green-100 text-green-700 border border-green-200' 
                : 'bg-red-100 text-red-700 border border-red-200'
            }`}>
              {message}
            </div>
          )}

          {/* User's Current Queue Status */}
          {userPosition && (
            <div className="bg-blue-50 border border-blue-200 rounded-lg p-4 mb-6">
              <h2 className="text-lg font-semibold text-blue-800 mb-2">Your Queue Status</h2>
              <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
                <div>
                  <p className="text-sm text-blue-600">Position in Queue</p>
                  <p className="text-xl font-bold text-blue-800">#{userPosition.position}</p>
                </div>
                <div>
                  <p className="text-sm text-blue-600">Estimated Wait Time</p>
                  <p className="text-xl font-bold text-blue-800">
                    {getEstimatedWaitTime(userPosition.position)}
                  </p>
                </div>
                <div>
                  <p className="text-sm text-blue-600">Doctor</p>
                  <p className="text-lg font-semibold text-blue-800">
                    Dr. {userPosition.doctor?.name || 'N/A'}
                  </p>
                </div>
              </div>
              <div className="mt-4">
                <button
                  onClick={leaveQueue}
                  className="px-4 py-2 bg-red-600 text-white rounded-lg hover:bg-red-700 focus:outline-none focus:ring-2 focus:ring-red-500 focus:ring-offset-2 transition duration-200"
                >
                  Leave Queue
                </button>
              </div>
            </div>
          )}

          {/* Available Queues */}
          <div>
            <h2 className="text-lg font-semibold text-gray-800 mb-4">Available Queues</h2>
            
            {queueData.length === 0 ? (
              <div className="text-center py-8">
                <div className="text-gray-500 mb-4">
                  <svg className="mx-auto h-12 w-12" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0zm6 3a2 2 0 11-4 0 2 2 0 014 0zM7 10a2 2 0 11-4 0 2 2 0 014 0z" />
                  </svg>
                </div>
                <p className="text-gray-500">No queue information available</p>
              </div>
            ) : (
              <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                {queueData.map((queue) => (
                  <div key={queue.doctorId} className="border border-gray-200 rounded-lg p-4 hover:shadow-md transition-shadow">
                    <div className="flex items-center justify-between mb-3">
                      <h3 className="text-lg font-semibold text-gray-800">
                        Dr. {queue.doctorName}
                      </h3>
                      <span className={`text-2xl font-bold ${getQueueStatusColor(queue.queueLength)}`}>
                        {queue.queueLength}
                      </span>
                    </div>
                    
                    <div className="space-y-2 mb-4">
                      <p className="text-sm text-gray-600">
                        <span className="font-medium">Specialization:</span> {queue.specialization}
                      </p>
                      <p className="text-sm text-gray-600">
                        <span className="font-medium">Queue Length:</span> {queue.queueLength} patients
                      </p>
                      <p className="text-sm text-gray-600">
                        <span className="font-medium">Est. Wait Time:</span> {getEstimatedWaitTime(queue.queueLength + 1)}
                      </p>
                    </div>
                    
                    {!userPosition && (
                      <button
                        onClick={() => joinQueue(queue.doctorId)}
                        className="w-full px-4 py-2 bg-green-600 text-white rounded-lg hover:bg-green-700 focus:outline-none focus:ring-2 focus:ring-green-500 focus:ring-offset-2 transition duration-200"
                      >
                        Join Queue
                      </button>
                    )}
                    
                    {userPosition && userPosition.doctorId === queue.doctorId && (
                      <div className="w-full px-4 py-2 bg-blue-100 text-blue-800 rounded-lg text-center font-medium">
                        You are in this queue
                      </div>
                    )}
                  </div>
                ))}
              </div>
            )}
          </div>

          {/* Auto-refresh notice */}
          <div className="mt-6 text-sm text-gray-500 text-center">
            Queue status refreshes automatically every 30 seconds
          </div>
        </div>
      </div>
    </div>
  );
};

export default QueueStatus;
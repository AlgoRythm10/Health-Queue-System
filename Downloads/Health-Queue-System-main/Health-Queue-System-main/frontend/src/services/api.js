import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Add request interceptor to include auth token
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

// API endpoints
export const authService = {
  login: (credentials) => api.post('/auth/login', credentials),
  logout: () => api.post('/auth/logout'),
};

export const queueService = {
  getQueueStatus: async () => {
    try {
      const response = await api.get('/queue/status');
      return response;
    } catch (err) {
      console.log('Queue API not available, using localStorage:', err);
      const localQueue = JSON.parse(localStorage.getItem('queue') || '[]');
      return { data: { activeEntries: localQueue.length, queueEntries: localQueue } };
    }
  },
  addToQueue: (patientData) => {
    // Store in localStorage
    const existingQueue = JSON.parse(localStorage.getItem('queue') || '[]');
    const newQueueEntry = {
      id: Date.now().toString(),
      ...patientData,
      createdAt: new Date().toISOString()
    };
    existingQueue.push(newQueueEntry);
    localStorage.setItem('queue', JSON.stringify(existingQueue));
    
    // Try API call
    return api.post('/queue', patientData).catch(() => ({
      data: newQueueEntry
    }));
  },
  removeFromQueue: (queueId) => {
    // Remove from localStorage
    const existingQueue = JSON.parse(localStorage.getItem('queue') || '[]');
    const filteredQueue = existingQueue.filter(entry => entry.id !== queueId);
    localStorage.setItem('queue', JSON.stringify(filteredQueue));
    
    // Try API call
    return api.delete(`/queue/${queueId}`).catch(() => ({
      data: { success: true }
    }));
  },
};

export const appointmentService = {
  getAppointments: async () => {
    try {
      const response = await api.get('/appointments');
      return response;
    } catch (err) {
      console.log('Appointments API not available, using localStorage:', err);
      const localAppointments = JSON.parse(localStorage.getItem('appointments') || '[]');
      return { data: localAppointments };
    }
  },
  getAppointmentById: (id) => api.get(`/appointments/${id}`),
  createAppointment: (appointmentData) => {
    // Store in localStorage
    const existingAppointments = JSON.parse(localStorage.getItem('appointments') || '[]');
    const newAppointment = {
      id: Date.now().toString(),
      ...appointmentData,
      createdAt: new Date().toISOString()
    };
    existingAppointments.push(newAppointment);
    localStorage.setItem('appointments', JSON.stringify(existingAppointments));
    
    // Try API call
    return api.post('/appointments', appointmentData).catch(() => ({
      data: newAppointment
    }));
  },
  updateAppointment: (id, appointmentData) => {
    // Update in localStorage
    const existingAppointments = JSON.parse(localStorage.getItem('appointments') || '[]');
    const updatedAppointments = existingAppointments.map(appointment => 
      appointment.id === id ? { ...appointment, ...appointmentData, updatedAt: new Date().toISOString() } : appointment
    );
    localStorage.setItem('appointments', JSON.stringify(updatedAppointments));
    
    // Try API call
    return api.put(`/appointments/${id}`, appointmentData).catch(() => ({
      data: { ...appointmentData, id }
    }));
  },
  deleteAppointment: (id) => {
    // Delete from localStorage
    const existingAppointments = JSON.parse(localStorage.getItem('appointments') || '[]');
    const filteredAppointments = existingAppointments.filter(appointment => appointment.id !== id);
    localStorage.setItem('appointments', JSON.stringify(filteredAppointments));
    
    // Try API call
    return api.delete(`/appointments/${id}`).catch(() => ({
      data: { success: true }
    }));
  },
};

export const doctorService = {
  getDoctors: async () => {
    try {
      const response = await api.get('/doctors');
      return response;
    } catch (err) {
      console.log('Doctors API not available, using localStorage:', err);
      // Only return admin-created doctors from localStorage
      const localDoctors = JSON.parse(localStorage.getItem('doctors') || '[]');
      console.log('Admin-created doctors:', localDoctors);
      return { data: localDoctors };
    }
  },
  getDoctorById: (id) => api.get(`/doctors/${id}`),
  createDoctor: (doctorData) => {
    // Store in localStorage as fallback
    const existingDoctors = JSON.parse(localStorage.getItem('doctors') || '[]');
    const newDoctor = {
      ...doctorData,
      id: doctorData.doctorId,
      createdAt: new Date().toISOString()
    };
    existingDoctors.push(newDoctor);
    localStorage.setItem('doctors', JSON.stringify(existingDoctors));
    
    // Try API call
    return api.post('/admin/doctors', doctorData).catch(() => ({
      data: newDoctor
    }));
  },
  updateDoctor: (id, doctorData) => {
    // Update in localStorage
    const existingDoctors = JSON.parse(localStorage.getItem('doctors') || '[]');
    const updatedDoctors = existingDoctors.map(doctor => 
      doctor.id === id ? { ...doctor, ...doctorData, updatedAt: new Date().toISOString() } : doctor
    );
    localStorage.setItem('doctors', JSON.stringify(updatedDoctors));
    
    // Try API call
    return api.put(`/doctors/${id}`, doctorData).catch(() => ({
      data: { ...doctorData, id }
    }));
  },
  deleteDoctor: (id) => {
    // Delete from localStorage
    const existingDoctors = JSON.parse(localStorage.getItem('doctors') || '[]');
    const filteredDoctors = existingDoctors.filter(doctor => doctor.id !== id);
    localStorage.setItem('doctors', JSON.stringify(filteredDoctors));
    
    // Try API call
    return api.delete(`/doctors/${id}`).catch(() => ({
      data: { success: true }
    }));
  }
};

export const patientService = {
  getPatients: () => api.get('/patients'),
  getPatientById: (id) => api.get(`/patients/${id}`),
  createPatient: (patientData) => api.post('/patients', patientData),
  updatePatient: (id, patientData) => api.put(`/patients/${id}`, patientData),
};

export const dashboardService = {
  getStats: () => api.get('/admin/stats'),
};

export default api;
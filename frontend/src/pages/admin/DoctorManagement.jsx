import React, { useState, useEffect } from 'react';
import { doctorService } from '../../services/api';
import { useAuth } from '../../context/AuthContext';
import { useNavigate } from 'react-router-dom';

const DoctorManagement = () => {
  const { logout } = useAuth();
  const navigate = useNavigate();
  const [doctors, setDoctors] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [showAddForm, setShowAddForm] = useState(false);
  const [newDoctor, setNewDoctor] = useState({
    name: '',
    email: '',
    password: '',
    specialization: '',
    department: '',
    phone: '',
    experienceYears: '',
    qualifications: '',
    consultationFee: '',
    availableDays: [],
    availableTimeSlots: ''
  });
  const [editingDoctor, setEditingDoctor] = useState(null);
  const [creating, setCreating] = useState(false);

  const specializations = [
    'Cardiology', 'Dermatology', 'Endocrinology', 'Gastroenterology', 'Neurology',
    'Oncology', 'Pediatrics', 'Psychiatry', 'Pulmonology', 'Rheumatology',
    'Urology', 'General Medicine', 'Orthopedics', 'Ophthalmology', 'ENT', 'Dentistry'
  ];

  useEffect(() => {
    fetchDoctors();
  }, []);

  const fetchDoctors = async () => {
    try {
      setLoading(true);
      console.log('Fetching doctors...');
      const response = await doctorService.getDoctors();
      console.log('Doctors response:', response);
      setDoctors(response.data || []);
    } catch (err) {
      console.error('Error fetching doctors:', err);
      setError('Failed to load doctors.');
    } finally {
      setLoading(false);
    }
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setNewDoctor(prev => ({ ...prev, [name]: value }));
  };

  const handleDayChange = (e) => {
    const { value, checked } = e.target;
    setNewDoctor(prev => ({
      ...prev,
      availableDays: checked
        ? [...prev.availableDays, value]
        : prev.availableDays.filter(day => day !== value)
    }));
  };

  const handleAddDoctor = async (e) => {
    e.preventDefault();
    setError('');
    setCreating(true);
    try {
      const doctorId = `DOC-${Date.now().toString().slice(-8)}`; // Simple ID generation
      const doctorData = { ...newDoctor, doctorId };
      
      console.log('Creating doctor:', doctorData);
      await doctorService.createDoctor(doctorData);
      
      // Also store in AuthContext's demoDoctors for login purposes
      const existingDemoDoctors = JSON.parse(localStorage.getItem('demoDoctors') || '[]');
      localStorage.setItem('demoDoctors', JSON.stringify([...existingDemoDoctors, { 
        email: newDoctor.email, 
        password: newDoctor.password, 
        name: newDoctor.name, 
        doctorId: doctorId 
      }]));

      setNewDoctor({
        name: '', email: '', password: '', specialization: '', department: '', phone: '',
        experienceYears: '', qualifications: '', consultationFee: '', availableDays: [], availableTimeSlots: ''
      });
      setShowAddForm(false);
      fetchDoctors(); // Refresh list
    } catch (err) {
      console.error('Error creating doctor:', err);
      setError('Failed to create doctor. Please try again.');
    } finally {
      setCreating(false);
    }
  };

  const handleEditClick = (doctor) => {
    setEditingDoctor(doctor);
    setNewDoctor({ ...doctor, password: '' }); // Don't pre-fill password for security
    setShowAddForm(true);
  };

  const handleUpdateDoctor = async (e) => {
    e.preventDefault();
    setError('');
    setCreating(true);
    try {
      await doctorService.updateDoctor(editingDoctor.id || editingDoctor.doctorId, newDoctor);
      setEditingDoctor(null);
      setNewDoctor({
        name: '', email: '', password: '', specialization: '', department: '', phone: '',
        experienceYears: '', qualifications: '', consultationFee: '', availableDays: [], availableTimeSlots: ''
      });
      setShowAddForm(false);
      fetchDoctors(); // Refresh list
    } catch (err) {
      console.error('Error updating doctor:', err);
      setError('Failed to update doctor. Please try again.');
    } finally {
      setCreating(false);
    }
  };

  const handleDeleteDoctor = async (id) => {
    if (window.confirm('Are you sure you want to delete this doctor?')) {
      try {
        await doctorService.deleteDoctor(id);
        fetchDoctors(); // Refresh list
      } catch (err) {
        console.error('Error deleting doctor:', err);
        setError('Failed to delete doctor. Please try again.');
      }
    }
  };

  if (loading) {
    return (
      <div className="flex items-center justify-center h-screen">
        <div className="animate-spin rounded-full h-12 w-12 border-t-2 border-b-2 border-blue-600"></div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gray-50">
      {/* Header */}
      <div className="bg-white shadow-sm border-b">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex justify-between items-center py-4">
            <div>
              <h1 className="text-2xl font-bold text-gray-900">Doctor Management</h1>
              <p className="text-sm text-gray-600">Manage all doctors in the system.</p>
            </div>
            <div className="flex space-x-4">
              <button
                onClick={() => navigate('/admin')}
                className="bg-gray-200 text-gray-800 px-4 py-2 rounded hover:bg-gray-300 transition-colors"
              >
                Back to Dashboard
              </button>
              <button
                onClick={() => logout()}
                className="bg-red-600 text-white px-4 py-2 rounded hover:bg-red-700 transition-colors"
              >
                Logout
              </button>
            </div>
          </div>
        </div>
      </div>

      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-6">
        {error && (
          <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded mb-6">
            {error}
          </div>
        )}

        <div className="flex justify-end mb-6">
          <button
            onClick={() => { 
              setShowAddForm(!showAddForm); 
              setEditingDoctor(null); 
              setNewDoctor({
                name: '', email: '', password: '', specialization: '', department: '', phone: '',
                experienceYears: '', qualifications: '', consultationFee: '', availableDays: [], availableTimeSlots: ''
              }); 
            }}
            className="bg-blue-600 text-white px-4 py-2 rounded-md hover:bg-blue-700 transition-colors"
          >
            {showAddForm ? 'Cancel' : 'Add New Doctor'}
          </button>
        </div>

        {showAddForm && (
          <div className="bg-white rounded-lg shadow-md p-6 mb-8">
            <h2 className="text-xl font-semibold text-gray-800 mb-4">
              {editingDoctor ? 'Edit Doctor' : 'Add New Doctor'}
            </h2>
            <form onSubmit={editingDoctor ? handleUpdateDoctor : handleAddDoctor} className="space-y-6">
              <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">Name</label>
                  <input type="text" name="name" value={newDoctor.name} onChange={handleInputChange} required
                    className="w-full border border-gray-300 rounded-md shadow-sm p-2 focus:ring-blue-500 focus:border-blue-500" />
                </div>
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">Email</label>
                  <input type="email" name="email" value={newDoctor.email} onChange={handleInputChange} required
                    className="w-full border border-gray-300 rounded-md shadow-sm p-2 focus:ring-blue-500 focus:border-blue-500" />
                </div>
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">Password {editingDoctor && "(Leave blank to keep current)"}</label>
                  <input type="password" name="password" value={newDoctor.password} onChange={handleInputChange}
                    className="w-full border border-gray-300 rounded-md shadow-sm p-2 focus:ring-blue-500 focus:border-blue-500"
                    required={!editingDoctor} />
                </div>
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">Specialization</label>
                  <select name="specialization" value={newDoctor.specialization} onChange={handleInputChange} required
                    className="w-full border border-gray-300 rounded-md shadow-sm p-2 focus:ring-blue-500 focus:border-blue-500">
                    <option value="">Select Specialization</option>
                    {specializations.map(spec => (
                      <option key={spec} value={spec}>{spec}</option>
                    ))}
                  </select>
                </div>
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">Department</label>
                  <input type="text" name="department" value={newDoctor.department} onChange={handleInputChange}
                    className="w-full border border-gray-300 rounded-md shadow-sm p-2 focus:ring-blue-500 focus:border-blue-500" />
                </div>
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">Phone</label>
                  <input type="tel" name="phone" value={newDoctor.phone} onChange={handleInputChange}
                    className="w-full border border-gray-300 rounded-md shadow-sm p-2 focus:ring-blue-500 focus:border-blue-500" />
                </div>
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">Experience (Years)</label>
                  <input type="number" name="experienceYears" value={newDoctor.experienceYears} onChange={handleInputChange}
                    className="w-full border border-gray-300 rounded-md shadow-sm p-2 focus:ring-blue-500 focus:border-blue-500" />
                </div>
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">Consultation Fee</label>
                  <input type="number" name="consultationFee" value={newDoctor.consultationFee} onChange={handleInputChange}
                    className="w-full border border-gray-300 rounded-md shadow-sm p-2 focus:ring-blue-500 focus:border-blue-500" />
                </div>
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">Qualifications</label>
                <textarea name="qualifications" value={newDoctor.qualifications} onChange={handleInputChange} rows="2"
                  className="w-full border border-gray-300 rounded-md shadow-sm p-2 focus:ring-blue-500 focus:border-blue-500"></textarea>
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">Available Days</label>
                <div className="flex flex-wrap gap-2">
                  {['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday'].map(day => (
                    <label key={day} className="inline-flex items-center">
                      <input type="checkbox" value={day} checked={newDoctor.availableDays.includes(day)} onChange={handleDayChange}
                        className="form-checkbox h-4 w-4 text-blue-600" />
                      <span className="ml-2 text-gray-700">{day}</span>
                    </label>
                  ))}
                </div>
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">Available Time Slots (e.g., 9:00 AM - 5:00 PM)</label>
                <input type="text" name="availableTimeSlots" value={newDoctor.availableTimeSlots} onChange={handleInputChange}
                  className="w-full border border-gray-300 rounded-md shadow-sm p-2 focus:ring-blue-500 focus:border-blue-500" />
              </div>
              <button type="submit" disabled={creating}
                className="bg-blue-600 text-white px-4 py-2 rounded-md hover:bg-blue-700 transition-colors disabled:opacity-50 disabled:cursor-not-allowed">
                {creating ? (editingDoctor ? 'Updating...' : 'Creating...') : (editingDoctor ? 'Update Doctor' : 'Add Doctor')}
              </button>
            </form>
          </div>
        )}

        {/* Doctors List */}
        <div className="bg-white rounded-lg shadow-md">
          <div className="px-6 py-4 border-b border-gray-200">
            <h2 className="text-xl font-semibold text-gray-800">Registered Doctors ({doctors.length})</h2>
          </div>
          {doctors.length === 0 ? (
            <div className="text-center py-12 text-gray-500">No doctors registered yet.</div>
          ) : (
            <div className="divide-y divide-gray-200">
              {doctors.map(doctor => (
                <div key={doctor.id || doctor.doctorId} className="p-6 flex flex-col md:flex-row justify-between items-start md:items-center">
                  <div className="flex-1 mb-4 md:mb-0">
                    <h3 className="text-lg font-semibold text-gray-800">{doctor.name}</h3>
                    <p className="text-sm text-gray-600">{doctor.specialization} - {doctor.department}</p>
                    <div className="flex flex-wrap gap-2 mt-2">
                      <span className="px-2 py-1 bg-blue-100 text-blue-800 text-xs rounded-full">{doctor.specialization}</span>
                      <span className="px-2 py-1 bg-green-100 text-green-800 text-xs rounded-full">{doctor.experienceYears} years exp</span>
                    </div>
                    <div className="mt-2 text-sm text-gray-500">
                      <p><strong>Email:</strong> {doctor.email}</p>
                      <p><strong>Phone:</strong> {doctor.phone}</p>
                      <p><strong>ID:</strong> {doctor.doctorId || doctor.id}</p>
                      <p><strong>Fee:</strong> ₹{doctor.consultationFee || 'N/A'}</p>
                      <p><strong>Department:</strong> {doctor.department}</p>
                      <p><strong>Available:</strong> {doctor.availableTimeSlots || 'N/A'}</p>
                    </div>
                  </div>
                  <div className="flex space-x-2">
                    <button
                      onClick={() => handleEditClick(doctor)}
                      className="bg-blue-500 text-white px-3 py-1 rounded-md text-sm hover:bg-blue-600 transition-colors"
                    >
                      Edit
                    </button>
                    <button
                      onClick={() => handleDeleteDoctor(doctor.id || doctor.doctorId)}
                      className="bg-red-500 text-white px-3 py-1 rounded-md text-sm hover:bg-red-600 transition-colors"
                    >
                      Delete
                    </button>
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

export default DoctorManagement;

import React, { useState, useEffect } from 'react';
import { doctorService } from '../services/api';

const DoctorsPage = () => {
  const [doctors, setDoctors] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    fetchDoctors();
  }, []);

  const fetchDoctors = async () => {
    try {
      setLoading(true);
      const response = await doctorService.getAllDoctors();
      setDoctors(response.data);
      setError('');
    } catch (err) {
      setError('Failed to load doctors');
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="py-6">
      <h1 className="text-3xl font-bold text-primary mb-6">Our Doctors</h1>
      
      {loading ? (
        <div className="flex justify-center items-center h-64">
          <div className="animate-spin rounded-full h-12 w-12 border-t-2 border-b-2 border-primary"></div>
        </div>
      ) : error ? (
        <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded relative" role="alert">
          <strong className="font-bold">Error!</strong>
          <span className="block sm:inline"> {error}</span>
        </div>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {doctors.map(doctor => (
            <div key={doctor.id} className="bg-white rounded-lg shadow-md overflow-hidden hover:shadow-lg transition-shadow duration-300">
              <div className="p-6">
                <h2 className="text-xl font-semibold text-gray-800 mb-2">{doctor.name}</h2>
                <p className="text-primary font-medium mb-2">{doctor.specialization}</p>
                <div className="text-gray-600 mb-4">
                  <p><span className="font-medium">Experience:</span> {doctor.experience} years</p>
                  <p><span className="font-medium">Department:</span> {doctor.department}</p>
                </div>
                <div className="flex justify-between items-center">
                  <span className={`px-3 py-1 inline-flex text-xs leading-5 font-semibold rounded-full ${
                    doctor.available ? 'bg-green-100 text-green-800' : 'bg-red-100 text-red-800'
                  }`}>
                    {doctor.available ? 'Available' : 'Unavailable'}
                  </span>
                  <button className="px-4 py-2 bg-primary text-white rounded hover:bg-primary-dark transition-colors duration-300">
                    Book Appointment
                  </button>
                </div>
              </div>
            </div>
          ))}
          
          {doctors.length === 0 && (
            <div className="col-span-full text-center text-gray-500 py-10">
              No doctors available at the moment
            </div>
          )}
        </div>
      )}
    </div>
  );
};

export default DoctorsPage;
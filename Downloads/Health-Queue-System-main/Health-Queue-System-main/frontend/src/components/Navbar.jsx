import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

const Navbar = () => {
  const [isOpen, setIsOpen] = useState(false);
  const { isAuthenticated, logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  return (
    <nav className="bg-primary p-4 shadow">
      <div className="container">
        <div className="flex justify-between items-center">
          <Link to="/" className="text-white font-bold">Health Queue System</Link>
          
          {/* Mobile menu button */}
          <div className="flex md:hidden">
            <button 
              onClick={() => setIsOpen(!isOpen)}
              className="text-white"
            >
              ☰
            </button>
          </div>
          
          {/* Desktop menu */}
          <div className="flex items-center" style={{display: window.innerWidth > 768 ? 'flex' : isOpen ? 'flex' : 'none'}}>
            <Link to="/" className="text-white px-4 py-2">Home</Link>
            <Link to="/queue" className="text-white px-4 py-2">Queue Status</Link>
            <Link to="/appointments" className="text-white px-4 py-2">Appointments</Link>
            <Link to="/doctors" className="text-white px-4 py-2">Doctors</Link>
            {isAuthenticated ? (
              <>
                <Link to="/dashboard" className="text-white px-4 py-2">Dashboard</Link>
                <button 
                  onClick={handleLogout}
                  className="bg-primary text-white px-4 py-2 rounded"
                  style={{backgroundColor: '#ef4444'}}
                >
                  Logout
                </button>
              </>
            ) : (
              <Link 
                to="/login" 
                className="bg-primary text-white px-4 py-2 rounded"
                style={{backgroundColor: '#3b82f6'}}
              >
                Login
              </Link>
            )}
          </div>
        </div>
        
        {/* Mobile menu */}
        {isOpen && (
          <div className="md:hidden py-2">
            <Link to="/" className="block py-2 text-white hover:text-accent transition-colors">Home</Link>
            <Link to="/queue" className="block py-2 text-white hover:text-accent transition-colors">Queue Status</Link>
            <Link to="/appointments" className="block py-2 text-white hover:text-accent transition-colors">Appointments</Link>
            <Link to="/doctors" className="block py-2 text-white hover:text-accent transition-colors">Doctors</Link>
            {isAuthenticated ? (
              <>
                <Link to="/dashboard" className="block py-2 text-white hover:text-accent transition-colors">Dashboard</Link>
                <button 
                  onClick={logout}
                  className="block w-full text-left py-2 text-white hover:text-red-300 transition-colors"
                >
                  Logout
                </button>
              </>
            ) : (
              <Link 
                to="/login" 
                className="block py-2 text-white hover:text-accent transition-colors"
              >
                Login
              </Link>
            )}
          </div>
        )}
      </div>
    </nav>
  );
};

export default Navbar;
import React, { useState } from 'react';
import { useAuth } from '../../context/AuthContext';
import { useNavigate } from 'react-router-dom';

const DoctorLogin = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const { login } = useAuth();
  const navigate = useNavigate();

  const onSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);
    const result = await login({ email, password, role: 'doctor' });
    setLoading(false);
    if (!result.success) {
      setError(result.error);
    } else {
      navigate('/doctor');
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-background">
      <div className="w-full max-w-md bg-white p-8 rounded-lg shadow">
        <h1 className="text-2xl font-semibold text-center mb-6">Doctor Sign In</h1>
        {error && <div className="text-red-600 text-sm mb-4">{error}</div>}
        <form onSubmit={onSubmit} className="space-y-4">
          <div>
            <label className="block text-sm mb-1">Email</label>
            <input value={email} onChange={(e) => setEmail(e.target.value)} type="email" required className="w-full border rounded px-3 py-2 focus:outline-none focus:ring-2 focus:ring-primary" />
          </div>
          <div>
            <label className="block text-sm mb-1">Password</label>
            <input value={password} onChange={(e) => setPassword(e.target.value)} type="password" required className="w-full border rounded px-3 py-2 focus:outline-none focus:ring-2 focus:ring-primary" />
          </div>
          <button disabled={loading} type="submit" className="w-full bg-primary hover:bg-primary-dark text-white rounded px-4 py-2">
            {loading ? 'Signing in...' : 'Sign In'}
          </button>
        </form>
      </div>
      <button onClick={async () => { const r = await login({ email: 'doctor@example.com', password: 'doctor123', role: 'doctor' }); if (r.success) navigate('/doctor'); }} className="fixed bottom-6 left-1/2 -translate-x-1/2 text-sm text-primary">Use demo doctor</button>
    </div>
  );
};

export default DoctorLogin;



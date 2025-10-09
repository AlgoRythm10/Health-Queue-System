import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';

const AdminLogin = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();
  const { login } = useAuth();

  const onSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);
    const result = await login({ email, password, role: 'admin' });
    setLoading(false);
    if (result.success) {
      navigate('/admin');
    } else {
      setError(result.error);
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-background">
      <div className="w-full max-w-md bg-white p-8 rounded-lg shadow">
        <h1 className="text-2xl font-semibold text-center mb-6">Admin Sign In</h1>
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
        <button onClick={() => onSubmit({ preventDefault: () => {} }) || login({ email: 'admin@example.com', password: 'admin123', role: 'admin' }).then(r => r.success && navigate('/admin'))} className="w-full mt-3 text-sm text-primary">Use demo admin</button>
      </div>
    </div>
  );
};

export default AdminLogin;



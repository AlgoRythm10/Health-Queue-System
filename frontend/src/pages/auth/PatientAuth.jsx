import React, { useState } from 'react';
import { useAuth } from '../../context/AuthContext';

const TabButton = ({ active, children, onClick }) => (
  <button
    onClick={onClick}
    className={`w-1/2 py-2 text-sm font-medium border-b-2 ${active ? 'border-primary text-primary' : 'border-transparent text-gray-500'}`}
  >
    {children}
  </button>
);

const PatientAuth = () => {
  const [tab, setTab] = useState('signin');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [name, setName] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const { login } = useAuth();

  const onSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);
    if (tab === 'signin') {
      const result = await login({ email, password, role: 'patient' });
      if (!result.success) setError(result.error);
    } else {
      // Placeholder for sign up flow - should call backend to create patient
      // For now we mimic successful signup -> sign in
      const result = await login({ email, password, role: 'patient' });
      if (!result.success) setError(result.error);
    }
    setLoading(false);
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-background">
      <div className="w-full max-w-md bg-white p-8 rounded-lg shadow">
        <div className="flex items-center mb-6">
          <TabButton active={tab === 'signin'} onClick={() => setTab('signin')}>Sign In</TabButton>
          <TabButton active={tab === 'signup'} onClick={() => setTab('signup')}>Sign Up</TabButton>
        </div>
        {error && <div className="text-red-600 text-sm mb-4">{error}</div>}
        <form onSubmit={onSubmit} className="space-y-4">
          {tab === 'signup' && (
            <div>
              <label className="block text-sm mb-1">Full Name</label>
              <input value={name} onChange={(e) => setName(e.target.value)} type="text" required className="w-full border rounded px-3 py-2 focus:outline-none focus:ring-2 focus:ring-primary" />
            </div>
          )}
          <div>
            <label className="block text-sm mb-1">Email</label>
            <input value={email} onChange={(e) => setEmail(e.target.value)} type="email" required className="w-full border rounded px-3 py-2 focus:outline-none focus:ring-2 focus:ring-primary" />
          </div>
          <div>
            <label className="block text-sm mb-1">Password</label>
            <input value={password} onChange={(e) => setPassword(e.target.value)} type="password" required className="w-full border rounded px-3 py-2 focus:outline-none focus:ring-2 focus:ring-primary" />
          </div>
          <button disabled={loading} type="submit" className="w-full bg-primary hover:bg-primary-dark text-white rounded px-4 py-2">
            {loading ? (tab === 'signin' ? 'Signing in...' : 'Signing up...') : (tab === 'signin' ? 'Sign In' : 'Sign Up')}
          </button>
        </form>
        <button onClick={() => login({ email: 'patient@example.com', password: 'patient123', role: 'patient' })} className="w-full mt-3 text-sm text-primary">Use demo patient</button>
      </div>
    </div>
  );
};

export default PatientAuth;



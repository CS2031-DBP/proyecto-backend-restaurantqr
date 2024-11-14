import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';

const Login = () => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [rememberMe, setRememberMe] = useState(false);
  const [error, setError] = useState('');
  const [userType, setUserType] = useState('Student');

  const navigate = useNavigate();

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();

    if (username === '' || password === '') {
      setError('Please enter your username and password.');
      return;
    }

    // Redirección directa al dashboard
    navigate('/menu');
  };

  return (
    <div className="flex min-h-screen items-center justify-center bg-base-200">
      <div className="card w-full max-w-md shadow-xl bg-base-100">
        <div className="card-body">
          <h2 className="text-center text-2xl font-bold mb-2">Login to Your Account</h2>
          <p className="text-center text-sm mb-4">Enter your username & password to login</p>

          {error && <div className="alert alert-error mb-4">{error}</div>}

          <form onSubmit={handleSubmit}>
            <div className="form-control mb-4">
              <label htmlFor="username" className="label">
                <span className="label-text">Username</span>
              </label>
              <input
                type="text"
                id="username"
                className="input input-bordered w-full"
                value={username}
                onChange={(e) => setUsername(e.target.value)}
                required
              />
            </div>

            <div className="form-control mb-4">
              <label htmlFor="password" className="label">
                <span className="label-text">Password</span>
              </label>
              <input
                type="password"
                id="password"
                className="input input-bordered w-full"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                required
              />
            </div>

            <div className="form-control mb-4 flex flex-row justify-between items-center">
              <label className="cursor-pointer label">
                <input
                  type="checkbox"
                  className="checkbox checkbox-primary"
                  checked={rememberMe}
                  onChange={(e) => setRememberMe(e.target.checked)}
                />
                <span className="label-text ml-2">Remember me</span>
              </label>
              <a href="/forgot-password" className="text-sm text-primary">Forgot password?</a>
            </div>

            <div className="form-control">
              <button className="btn btn-primary w-full" type="submit">Login</button>
            </div>
          </form>

          <div className="mt-4 text-center text-sm">
            Don’t have an account? <Link to="/register" className="text-primary">Create an account</Link>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Login;

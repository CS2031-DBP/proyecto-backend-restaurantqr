import { useState } from 'react';

const Register = () => {
  const [formData, setFormData] = useState({
    firstName: '',
    lastName: '',
    username: '',
    email: '',
    password: '',
    address: '',
  });

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    console.log('Registered Data:', formData);
  };

  return (
    <div className="flex min-h-screen items-center justify-center bg-base-200">
      <div className="card w-full max-w-lg shadow-xl bg-base-100">
        <div className="card-body">
          <h2 className="text-center text-2xl font-bold">Create an Account</h2>
          <p className="text-center text-sm mb-6">Enter your details to create an account</p>

          <form onSubmit={handleSubmit} className="space-y-4">
            <div className="form-control">
              <label htmlFor="firstName" className="label">
                <span className="label-text">First Name</span>
              </label>
              <input
                type="text"
                id="firstName"
                name="firstName"
                className="input input-bordered w-full"
                value={formData.firstName}
                onChange={handleInputChange}
                required
              />
            </div>

            <div className="form-control">
              <label htmlFor="lastName" className="label">
                <span className="label-text">Last Name</span>
              </label>
              <input
                type="text"
                id="lastName"
                name="lastName"
                className="input input-bordered w-full"
                value={formData.lastName}
                onChange={handleInputChange}
                required
              />
            </div>

            <div className="form-control">
              <label htmlFor="username" className="label">
                <span className="label-text">Username</span>
              </label>
              <input
                type="text"
                id="username"
                name="username"
                className="input input-bordered w-full"
                value={formData.username}
                onChange={handleInputChange}
                required
              />
            </div>

            <div className="form-control">
              <label htmlFor="email" className="label">
                <span className="label-text">Email</span>
              </label>
              <input
                type="email"
                id="email"
                name="email"
                className="input input-bordered w-full"
                value={formData.email}
                onChange={handleInputChange}
                required
              />
            </div>

            <div className="form-control">
              <label htmlFor="password" className="label">
                <span className="label-text">Password</span>
              </label>
              <input
                type="password"
                id="password"
                name="password"
                className="input input-bordered w-full"
                value={formData.password}
                onChange={handleInputChange}
                required
              />
            </div>

            <div className="form-control">
              <label htmlFor="address" className="label">
                <span className="label-text">Address</span>
              </label>
              <input
                type="text"
                id="address"
                name="address"
                className="input input-bordered w-full"
                value={formData.address}
                onChange={handleInputChange}
              />
            </div>

            <div className="form-control mt-4">
              <button className="btn btn-primary w-full" type="submit">Create Account</button>
            </div>
          </form>

          <p className="text-center text-sm mt-4">
            Already have an account? <a href="/login" className="text-primary">Log in</a>
          </p>
        </div>
      </div>
    </div>
  );
};

export default Register;

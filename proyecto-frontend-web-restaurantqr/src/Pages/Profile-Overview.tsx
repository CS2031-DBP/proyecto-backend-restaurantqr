// src/Pages/UserProfile_Overview.jsx
import React from 'react';
import fernandoImage from '../img/team/fernando_munoz.jpeg';  // Asegúrate de que la ruta de la imagen es correcta
import { Link } from 'react-router-dom';
import Navbar from '../Components/navbar';
import UserButtons from '../Components/UserButtons';

const UserProfile_Overview = () => {
  const user = {
    name: "Fernando Muñoz",
    firstName: "Fernando Jose",
    lastName: "Muñoz Paucar",
    email: "fernando.munoz.p@utec.edu.pe",
    username: "Francotirador28",
    role: "Admin", // Cambia entre "Cliente", "Mesero", "Repartidor", "Admin"
  };

  const renderRatingStars = (rating) => {
    const maxStars = 5;
    const stars = [];
    for (let i = 1; i <= maxStars; i++) {
      stars.push(
        <span key={i} className={`text-xl ${i <= rating ? 'text-yellow-500' : 'text-gray-300'}`} >
          ★
        </span>
      );
    }
    return <div className="flex">{stars}</div>;
  };

  const renderUserRoleDetails = () => {
    switch (user.role) {
      case "Cliente":
        return (
          <ul className="text-base space-y-4 ml-4 mt-4">
            <li className="flex mb-2">
              <span className="text-left w-1/4 font-semibold">Puntos de Fidelidad</span>
              <span className="text-left w-3/4">{user.loyaltyPoints}</span>
            </li>
            <li className="flex mb-2">
              <span className="text-left w-1/4 font-semibold">Rango</span>
              <span className="text-left w-3/4">{user.rank}</span>
            </li>
          </ul>
        );
      case "Mesero":
      case "Repartidor":
        return (
          <ul className="text-base space-y-4 ml-4 mt-4">
            <li className="flex items-center mb-2">
              <span className="text-left w-1/4 font-semibold">Calificación</span>
              <span className="text-left w-3/4">{renderRatingStars(user.rating)}</span>
            </li>
          </ul>
        );
      default:
        return null;
    }
  };

  return (
    <div className="bg-[#f5f5ff] flex min-h-screen">
      {/* Main content area */}
      <main className="flex-1 p-4">
        <header className="navbar bg-base-100 shadow-lg mb-4">
          <Navbar />
          <div className="flex justify-between items-center w-full px-4">
            <div className="flex items-center space-x-4">
              <h2 className="text-2xl font-bold mb-1">Perfil</h2>
              <div className="flex space-x-4 mb-1">
                <span className="text-base font-semibold">Inicio / Usuarios / Perfil</span>
              </div>
            </div>
            <div className="flex items-center space-x-4">
              <div className="toggle-sidebar-btn text-3xl cursor-pointer text-blue-800" style={{ paddingLeft: '10px' }}>
                &#9776;
              </div>
              <button className="btn btn-ghost text-blue-800">
                <span className="material-icons">notifications</span>
              </button>
              <span className="badge bg-red-500 text-white">3</span>
            </div>
          </div>
        </header>

        {/* Profile Info Section */}
        <div className="flex space-x-4 mt-4 ml-40">
          {/* User's profile image and basic info */}
          <div className="bg-white p-4 rounded-lg shadow-md flex flex-col items-center w-96 h-60">
            <img src={fernandoImage} alt="Fernando Muñoz" className="w-32 h-32 object-cover rounded-full mb-2" />
            <div className="text-center">
              <h3 className="text-2xl font-semibold">{user.name}</h3>
              <h4 className="text-lg text-gray-500">{user.role}</h4>
            </div>
          </div>

          {/* Profile options and details */}
          <div className="bg-white p-4 rounded-lg shadow-md flex-1 flex flex-col">
            <div className="flex mb-4">
              <Link to="/profile/overview" className="text-lg font-semibold text-blue-500 hover:text-blue-700 mr-10 ml-4 relative">
                General
                <span className="absolute -bottom-3 left-0 w-24 h-0.5 bg-blue-500 transition-all duration-300" style={{ width: '120%', left: '-10%' }}></span>
              </Link>
              <Link to="/profile/edit" className="text-lg font-semibold hover:text-blue-500 mr-10 ml-4">Editar Perfil</Link>
              <Link to="/profile/settings" className="text-lg font-semibold hover:text-blue-500 mr-10 ml-4">Configuración</Link>
              <Link to="/profile/change-password" className="text-lg font-semibold hover:text-blue-500">Cambiar Contraseña</Link>
            </div>
            <hr className="my-6" />

            {/* Profile Details */}
            <h3 className="text-xl font-semibold mb-4 ml-4">Detalles del Perfil</h3>
            <ul className="text-base space-y-4 ml-4 mt-4">
              <li className="flex mb-2">
                <span className="text-left w-1/4 font-semibold">Nombres</span>
                <span className="text-left w-3/4">{user.firstName}</span>
              </li>
              <li className="flex mb-2">
                <span className="text-left w-1/4 font-semibold">Apellidos</span>
                <span className="text-left w-3/4">{user.lastName}</span>
              </li>
              <li className="flex mb-2">
                <span className="text-left w-1/4 font-semibold">Correo Electrónico</span>
                <span className="text-left w-3/4">{user.email}</span>
              </li>
            </ul>

            {/* Role-specific Details */}
            {user.role === "Admin" && (
              <div className="mt-6 ml-auto"> {/* Esto añade más espacio arriba de los botones y los coloca a la derecha */}
                <UserButtons />
              </div>
            )}
          </div>
        </div>
      </main>
    </div>
  );
};

export default UserProfile_Overview;

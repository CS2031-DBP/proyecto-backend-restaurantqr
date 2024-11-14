// src/Components/UserButtons.jsx
import React from 'react';
import { Link } from 'react-router-dom';

const UserButtons = () => {
  return (
    <div className="flex space-x-4">
      {/* Botón de Ver Usuarios */}
      <Link to="/profile/view-user">
        <button className="px-6 py-2 bg-[#3661ff] text-white font-semibold rounded-lg hover:bg-blue-500 transition duration-300">
          Ver Usuarios
        </button>
      </Link>

      {/* Botón de Crear Usuario */}
      <Link to="/profile/create-user">
        <button className="px-6 py-2 bg-[#ff36d3] text-white font-semibold rounded-lg hover:bg-pink-400 transition duration-300">
          Crear Usuario
        </button>
      </Link>
    </div>
  );
};

export default UserButtons;

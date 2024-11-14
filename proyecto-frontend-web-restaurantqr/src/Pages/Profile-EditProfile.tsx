import React, { useState } from 'react';
import fernandoImage from '../img/team/fernando_munoz.jpeg';
import { Link } from 'react-router-dom';
import Navbar from '../Components/navbar';

const UserProfile_EditProfile = () => {
  // Estado para la descripción y rol
  const [description, setDescription] = useState(
    "Soy un estudiante de Ingeniería Electrónica en UTEC que quiere desarrollar sus habilidades como diseñador de software. Actualmente curso el 8vo ciclo de la carrera."
  );
  const [role, setRole] = useState("Cliente"); // Cambia entre "Cliente", "Mesero", "Repartidor", "Admi"

  const handleDescriptionChange = (e) => {
    setDescription(e.target.value);
  };

  return (
    <div className="bg-[#f5f5ff] flex min-h-screen">
      <main className="flex-1 p-4">
        {/* Navbar */}
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
              <div
                className="toggle-sidebar-btn text-3xl cursor-pointer text-blue-800"
                style={{ paddingLeft: '10px' }}
              >
                &#9776;
              </div>
              <button className="btn btn-ghost text-blue-800">
                <span className="material-icons">notifications</span>
              </button>
              <span className="badge bg-red-500 text-white">3</span>
            </div>
          </div>
        </header>

        {/* Perfil Edit */}
        <div className="flex space-x-4 mt-4 ml-40">
          <div className="bg-white p-4 rounded-lg shadow-md flex flex-col items-center w-96 h-60">
            <img
              src={fernandoImage}
              alt="Fernando Muñoz"
              className="w-32 h-32 object-cover rounded-full mb-2"
            />
            <div className="text-center">
              <h3 className="text-2xl font-semibold">Fernando Muñoz</h3>
              <h4 className="text-lg text-gray-500">{role}</h4>
            </div>
          </div>

          <div className="bg-white p-4 rounded-lg shadow-md flex-1 flex flex-col">
            {/* Nav Links */}
            <div className="flex mb-1">
              <Link to="/profile/overview" className="text-lg font-semibold hover:text-blue-500 mr-10 ml-4">
                General
              </Link>
              <Link
                to="/profile/edit"
                className="text-lg font-semibold text-blue-500 hover:text-blue-700 relative mr-10 ml-4">
                Editar Perfil
                <span
                  className="absolute -bottom-2 left-0 w-24 h-0.5 bg-blue-500 transition-all duration-300"
                  style={{ width: '120%', left: '-10%' }}
                ></span>
              </Link>
              <Link to="/profile/settings" className="text-lg font-semibold hover:text-blue-500 mr-10 ml-4">
                Configuración
              </Link>
              <Link to="/profile/change-password" className="text-lg font-semibold hover:text-blue-500 mr-10">
                Cambiar Contraseña
              </Link>
            </div>

            <hr className="my-6" />

            {/* Formulario de edición */}
            <ul className="text-base space-y-4 ml-4">
              {role === "Cliente" || role === "Mesero" || role === "Repartidor" ? (
                <>
                  <li className="flex mb-2">
                    <span className="text-left w-1/4 font-semibold">Nombres</span>
                    <input
                      type="text"
                      className="text-left w-3/4 border rounded p-1 focus:border-blue-500"
                      defaultValue="Fernando José"
                    />
                  </li>

                  <li className="flex mb-2">
                    <span className="text-left w-1/4 font-semibold">Apellidos</span>
                    <input
                      type="text"
                      className="text-left w-3/4 border rounded p-1 focus:border-blue-500"
                      defaultValue="Muñoz Paucar"
                    />
                  </li>
                </>
              ) : role === "Admi" ? (
                <>
                  <li className="flex mb-2">
                    <span className="text-left w-1/4 font-semibold">Nombre</span>
                    <input
                      type="text"
                      className="text-left w-3/4 border rounded p-1 focus:border-blue-500"
                      defaultValue="Fernando"
                    />
                  </li>

                  <li className="flex mb-2">
                    <span className="text-left w-1/4 font-semibold">Apellido</span>
                    <input
                      type="text"
                      className="text-left w-3/4 border rounded p-1 focus:border-blue-500"
                      defaultValue="Muñoz"
                    />
                  </li>

                  <li className="flex mb-2">
                    <span className="text-left w-1/4 font-semibold">Correo Electrónico</span>
                    <input
                      type="email"
                      className="text-left w-3/4 border rounded p-1 focus:border-blue-500"
                      defaultValue="fernando.munoz@utec.edu.pe"
                    />
                  </li>

                  <li className="flex mb-2">
                    <span className="text-left w-1/4 font-semibold">Contraseña</span>
                    <input
                      type="password"
                      className="text-left w-3/4 border rounded p-1 focus:border-blue-500"
                      defaultValue="********"
                    />
                  </li>
                </>
              ) : null}
            </ul>

            <div className="flex justify-center mt-4">
              <button className="px-6 py-2 bg-[#3661ff] text-white font-semibold rounded-lg hover:bg-blue-500 transition duration-300">
                Guardar Cambios
              </button>
            </div>
          </div>
        </div>
      </main>
    </div>
  );
};

export default UserProfile_EditProfile;

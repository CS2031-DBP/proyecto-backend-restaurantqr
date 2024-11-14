import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { FaUser, FaUtensils, FaConciergeBell, FaSignOutAlt, FaTruck, FaCog, FaClipboardList } from 'react-icons/fa';

// Roles: Usuario, Admi, Chef, Mesero, Repartidor

const Navbar = ({ role = "Usuario" }) => {
  const navigate = useNavigate();
  const [isNavbarVisible, setIsNavbarVisible] = useState(false); // Estado para controlar la visibilidad

  const handleLogout = () => {
    // Aquí podrías agregar lógica para limpiar el estado de sesión o token
    navigate('/login');
  };

  const toggleNavbar = () => {
    setIsNavbarVisible(prevState => !prevState); // Alternar el estado de visibilidad del navbar
  };

  const renderMenuOptions = () => {
    switch (role) {
      case 'Usuario':
      case 'Admi':
        return (
          <>
            <li>
              <Link to="/menu" className="btn btn-ghost btn-lg flex items-center gap-2">
                <FaClipboardList className="text-lg" /> Menú
              </Link>
            </li>
            <li>
              <Link to="/profile" className="btn btn-ghost btn-lg flex items-center gap-2">
                <FaUser className="text-lg" /> Perfil
              </Link>
            </li>
            <li>
              <Link to="/reservation" className="btn btn-ghost btn-lg flex items-center gap-2">
                <FaConciergeBell className="text-lg" /> Reserva
              </Link>
            </li>
          </>
        );
      case 'Mesero':
        return (
          <>
            <li>
              <Link to="/orders" className="btn btn-ghost btn-lg flex items-center gap-2">
                <FaClipboardList className="text-lg" /> Pedido
              </Link>
            </li>
            <li>
              <Link to="/profile" className="btn btn-ghost btn-lg flex items-center gap-2">
                <FaUser className="text-lg" /> Perfil
              </Link>
            </li>
            <li>
              <Link to="/reservation" className="btn btn-ghost btn-lg flex items-center gap-2">
                <FaConciergeBell className="text-lg" /> Reserva
              </Link>
            </li>
          </>
        );
      case 'Repartidor':
        return (
          <>
            <li>
              <Link to="/delivery" className="btn btn-ghost btn-lg flex items-center gap-2">
                <FaTruck className="text-lg" /> Delivery
              </Link>
            </li>
            <li>
              <Link to="/profile" className="btn btn-ghost btn-lg flex items-center gap-2">
                <FaUser className="text-lg" /> Perfil
              </Link>
            </li>
          </>
        );
      case 'Chef':
        return (
          <>
            <li>
              <Link to="/orders" className="btn btn-ghost btn-lg flex items-center gap-2">
                <FaClipboardList className="text-lg" /> Pedido
              </Link>
            </li>
            <li>
              <Link to="/profile" className="btn btn-ghost btn-lg flex items-center gap-2">
                <FaUser className="text-lg" /> Perfil
              </Link>
            </li>
          </>
        );
      default:
        return null;
    }
  };

  return (
    <div>
      {/* Navbar lateral centrado en su contenedor con esquinas redondeadas */}
      <nav 
        className={`navbar bg-base-100 shadow-md p-4 fixed left-0 top-1/2 transform -translate-y-1/2 h-auto z-50 transition-transform ${isNavbarVisible ? 'translate-x-0' : '-translate-x-full'} w-40 rounded-lg`} // Clase de esquinas redondeadas añadida
      >
        <div className="navbar-start flex flex-col items-start ml-2">

          {/* Menú de opciones */}
          <ul className="space-y-3"> {/* Aumentar el espacio entre cada opción del menú */}
            {renderMenuOptions()}

            {/* Cerrar sesión */}
            <li>
              <button 
                onClick={handleLogout} 
                className="btn btn-ghost btn-lg flex items-center gap-2"
              >
                <FaSignOutAlt className="text-lg" /> Cerrar Sesión
              </button>
            </li>
          </ul>
        </div>
      </nav>

      {/* Botón de tuerca para abrir/cerrar el Navbar, siempre visible */}
      <button 
        onClick={toggleNavbar} 
        className="btn btn-ghost fixed bottom-4 left-4 z-50 text-2xl"
      >
        <FaCog />
      </button>
    </div>
  );
};

export default Navbar;

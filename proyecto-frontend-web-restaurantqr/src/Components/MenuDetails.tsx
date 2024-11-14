import React from 'react';
import { Link } from 'react-router-dom';

const MenuDetails = ({ item, onClose }) => {
  if (!item) {
    return <p>Plato no encontrado</p>;
  }

  return (
    <div className="relative bg-white rounded-lg shadow-xl p-6 max-w-md w-full">
      {/* Botón de cierre en la esquina superior derecha */}
      <button
        className="absolute top-4 right-4 text-2xl font-bold text-gray-600 hover:text-gray-800"
        onClick={onClose}
      >
        X
      </button>

      <div>
        {/* Imagen representativa */}
        <img
          src="https://via.placeholder.com/400x250"
          alt={item.name}
          className="w-full h-48 object-cover rounded-t-lg"
        />
        <h1 className="text-3xl font-semibold text-center text-gray-800 mt-4">{item.name}</h1>
        <p className="text-lg text-green-600 font-bold text-center mt-2">${item.price}</p>
        <p className="text-md text-gray-600 mt-4">{item.description}</p>

        {/* Botones */}
        <div className="flex justify-between mt-6">
          <Link to="/menu">
            <button
              className="bg-gray-400 text-white font-bold py-2 px-6 rounded-lg shadow-md hover:bg-gray-500 transition duration-300 ease-in-out"
              onClick={onClose}
            >
              Regresar
            </button>
          </Link>
          {/* Botón para añadir al carrito */}
          <button className="bg-green-500 text-white font-bold py-2 px-6 rounded-lg shadow-md hover:bg-green-600 transition duration-300 ease-in-out">
            Añadir al carrito
          </button>
        </div>
      </div>
    </div>
  );
};

export default MenuDetails;

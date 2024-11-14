import React, { useState, useRef } from 'react';
import { Link } from 'react-router-dom';
import { FaCocktail, FaIceCream, FaBreadSlice, FaPizzaSlice, FaUtensils } from 'react-icons/fa';
import Navbar from '../Components/navbar';

const Menu = () => {
  const [description, setDescription] = useState(
    "Soy un estudiante de Ingeniería Electrónica en UTEC que quiere desarrollar sus habilidades como diseñador de software. Actualmente curso el 8vo ciclo de la carrera."
  );

  const [searchTerm, setSearchTerm] = useState("");

  // Control del movimiento del slider de promociones
  const promoRef = useRef(null);

  const handleDescriptionChange = (e) => {
    setDescription(e.target.value);
  };

  const handleSearchChange = (e) => {
    setSearchTerm(e.target.value);
  };

  const handleButtonClick = (category) => {
    console.log(`${category} seleccionado`);
  };

  // Función para mover el slider de las promociones hacia la izquierda (de 3 en 3)
  const handleScrollLeft = () => {
    if (promoRef.current) {
      const promoWidth = promoRef.current.children[0].offsetWidth; // Ancho de cada promoción
      promoRef.current.scrollBy({
        left: -promoWidth * 3, // Mover 3 elementos
        behavior: 'smooth',
      });
    }
  };

  // Función para mover el slider de las promociones hacia la derecha (de 3 en 3)
  const handleScrollRight = () => {
    if (promoRef.current) {
      const promoWidth = promoRef.current.children[0].offsetWidth; // Ancho de cada promoción
      promoRef.current.scrollBy({
        left: promoWidth * 3, // Mover 3 elementos
        behavior: 'smooth',
      });
    }
  };

  return (
    <div className="bg-[#f5f5ff] min-h-screen relative">
      <Navbar /> {/* Integración del componente Navbar */}

      <main className="p-4 flex flex-col">
        <h1 className="text-3xl font-bold mb-4">Tablero</h1>

        <div className="flex space-x-4 mb-4">
          {/* Botones del menú con enlaces internos */}
          <a href="#bebidas" className="flex-1 bg-white p-4 rounded-lg shadow-md flex items-center justify-between hover:bg-blue-200 transition duration-300">
            <div className="flex items-center">
              <FaCocktail className="text-2xl mr-2" />
              <span className="text-lg font-bold">BEBIDAS</span>
            </div>
            <div className="w-8 h-8 bg-blue-500 rounded-full flex items-center justify-center text-white font-bold">10</div>
          </a>

          <a href="#postres" className="flex-1 bg-white p-4 rounded-lg shadow-md flex items-center justify-between hover:bg-green-200 transition duration-300">
            <div className="flex items-center">
              <FaIceCream className="text-2xl mr-2" />
              <span className="text-lg font-bold">POSTRES</span>
            </div>
            <div className="w-8 h-8 bg-green-500 rounded-full flex items-center justify-center text-white font-bold">10</div>
          </a>

          <a href="#entradas" className="flex-1 bg-white p-4 rounded-lg shadow-md flex items-center justify-between hover:bg-orange-200 transition duration-300">
            <div className="flex items-center">
              <FaBreadSlice className="text-2xl mr-2" />
              <span className="text-lg font-bold">ENTRADAS</span>
            </div>
            <div className="w-8 h-8 bg-orange-500 rounded-full flex items-center justify-center text-white font-bold">10</div>
          </a>

          <a href="#aperitivos" className="flex-1 bg-white p-4 rounded-lg shadow-md flex items-center justify-between hover:bg-purple-200 transition duration-300">
            <div className="flex items-center">
              <FaPizzaSlice className="text-2xl mr-2" />
              <span className="text-lg font-bold">APERITIVOS</span>
            </div>
            <div className="w-8 h-8 bg-purple-500 rounded-full flex items-center justify-center text-white font-bold">10</div>
          </a>

          <a href="#menu" className="flex-1 bg-white p-4 rounded-lg shadow-md flex items-center justify-between hover:bg-red-200 transition duration-300">
            <div className="flex items-center">
              <FaUtensils className="text-2xl mr-2" />
              <span className="text-lg font-bold">MENÚ</span>
            </div>
            <div className="w-8 h-8 bg-red-500 rounded-full flex items-center justify-center text-white font-bold">10</div>
          </a>
        </div>

        {/* Recomendaciones */}
        <div className="mt-8 flex space-x-4">
          <div id="bebidas" className="flex-1 bg-blue-50 p-6 rounded-lg shadow-lg border-2 border-blue-300">
            <h3 className="text-xl font-semibold mb-4">Recomendados 1</h3>
            <ul className="space-y-4">
              <Link to="/menu/details"><li className="bg-blue-100 p-4 rounded-lg shadow-md flex items-center"><FaCocktail className="text-2xl mr-2" /> Bebida 1</li></Link>
              <Link to="/menu/details"><li className="bg-blue-100 p-4 rounded-lg shadow-md flex items-center"><FaIceCream className="text-2xl mr-2" /> Postre 1</li></Link>
              <Link to="/menu/details"><li className="bg-blue-100 p-4 rounded-lg shadow-md flex items-center"><FaBreadSlice className="text-2xl mr-2" /> Entrada 1</li></Link>
            </ul>
          </div>

          <div id="postres" className="flex-1 bg-green-50 p-6 rounded-lg shadow-lg border-2 border-green-300">
            <h3 className="text-xl font-semibold mb-4">Recomendados 2</h3>
            <ul className="space-y-4">
              <Link to="/menu/details"><li className="bg-green-100 p-4 rounded-lg shadow-md flex items-center"><FaPizzaSlice className="text-2xl mr-2" /> Aperitivo 1</li></Link>
              <Link to="/menu/details"><li className="bg-green-100 p-4 rounded-lg shadow-md flex items-center"><FaUtensils className="text-2xl mr-2" /> Menú 1</li></Link>
              <Link to="/menu/details"><li className="bg-green-100 p-4 rounded-lg shadow-md flex items-center"><FaCocktail className="text-2xl mr-2" /> Bebida 2</li></Link>
            </ul>
          </div>
        </div>

        {/* Bloque adicional para las promociones */}
        <div id="entradas" className="mt-8 p-4 bg-white rounded-lg shadow-lg relative">
          <h2 className="text-2xl font-bold mb-4">Promociones</h2>
          <div className="absolute top-1/2 left-2 transform -translate-y-1/2 z-10">
            <button className="bg-blue-500 text-white p-2 rounded-full shadow-lg opacity-75 hover:opacity-100 transition duration-300" onClick={handleScrollLeft}>{"<"}</button>
          </div>

          <div ref={promoRef} className="flex space-x-4 pb-4" style={{ scrollBehavior: 'smooth', overflow: 'hidden' }}>
            <Link to="/menu/details"><div className="bg-blue-200 p-16 rounded-lg shadow-lg flex items-center justify-center text-3xl font-bold">Promoción 1</div></Link>
            <Link to="/menu/details"><div className="bg-green-200 p-16 rounded-lg shadow-lg flex items-center justify-center text-3xl font-bold">Promoción 2</div></Link>
            <Link to="/menu/details"><div className="bg-orange-200 p-16 rounded-lg shadow-lg flex items-center justify-center text-3xl font-bold">Promoción 3</div></Link>
            <Link to="/menu/details"><div className="bg-purple-200 p-16 rounded-lg shadow-lg flex items-center justify-center text-3xl font-bold">Promoción 4</div></Link>
          </div>

          <div className="absolute top-1/2 right-2 transform -translate-y-1/2 z-10">
            <button className="bg-blue-500 text-white p-2 rounded-full shadow-lg opacity-75 hover:opacity-100 transition duration-300" onClick={handleScrollRight}>{">"}</button>
          </div>
        </div>
      </main>
    </div>
  );
};

export default Menu;

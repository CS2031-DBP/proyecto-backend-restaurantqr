import React, { useState, useEffect } from 'react';
import incidente from "../img/crearReporte/incidente.png";
import objetoPerdido from "../img/crearReporte/objetoPerdido.png";
import { Link, useNavigate } from 'react-router-dom'; // Importa useNavigate
import Navbar from '../Components/navbar';

const PostReservation = () => {
  const [reportType, setReportType] = useState('Objeto');
  const [currentImage, setCurrentImage] = useState(objetoPerdido); // Estado para la imagen
  const [formData, setFormData] = useState({
    description: '',
    piso: '',
    aula: '',
    detalle: '',
    celular: '',
    email: '',
    hecho: '',
  });

  const navigate = useNavigate(); // Inicializa useNavigate

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  // Cambia la imagen según el tipo de reporte
  useEffect(() => {
    if (reportType === 'Objeto') {
      setCurrentImage(objetoPerdido);
    } else if (reportType === 'Incidente') {
      setCurrentImage(incidente);
    }
  }, [reportType]);

  // Maneja la redirección al hacer clic en "Reservar"
  const handleReservar = () => {
    navigate('/reservation'); // Navega a la ruta /reservation
  };

  return (
    <div className="bg-[#f5f5ff] min-h-screen flex">
      <main className="flex-1 p-4">
        <header className="navbar bg-base-100 shadow-lg mb-4">
          <Navbar />
          <div className="flex justify-between items-center w-full px-4">
            <div className="flex items-center space-x-4">
              <h2 className="text-2xl font-bold mb-1">Nueva Reserva</h2>
              <div className="flex space-x-4 mb-1">
                <span className="text-base font-semibold">Inicio / Reserva / Nueva Reserva</span>
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

        <div className="flex justify-between mt-4 space-x-6 ml-40">
          <div className="bg-[#ffffff] rounded-lg shadow-md p-4 w-2/5 ml-14">
            <h3 className="text-lg font-semibold mb-2 text-center">Reserva</h3>

            {reportType === 'Objeto' && (
              <>
                <div className="mb-2">
                  <label className="block text-sm font-semibold mb-1">Fecha:</label>
                  <input 
                    type="text" 
                    name="description" 
                    value={formData.description} 
                    onChange={handleChange}
                    className="border border-gray-300 rounded p-2 w-full" 
                  />
                </div>
                <div className="mb-2">
                  <label className="block text-sm font-semibold mb-1">Hora:</label>
                  <input 
                    type="text" 
                    name="piso" 
                    value={formData.piso} 
                    onChange={handleChange}
                    className="border border-gray-300 rounded p-2 w-full" 
                  />
                </div>
                <div className="mb-2">
                  <label className="block text-sm font-semibold mb-1">Número de Personas:</label>
                  <input 
                    type="text" 
                    name="aula" 
                    value={formData.aula} 
                    onChange={handleChange}
                    className="border border-gray-300 rounded p-2 w-full" 
                  />
                </div>
                <div className="mb-2">
                  <label className="block text-sm font-semibold mb-1">Detalle:</label>
                  <input 
                    type="text" 
                    name="detalle" 
                    value={formData.detalle} 
                    onChange={handleChange}
                    className="border border-gray-300 rounded p-2 w-full" 
                  />
                </div>
                <div className="mb-2">
                  <label className="block text-sm font-semibold mb-1">Celular:</label>
                  <input 
                    type="text" 
                    name="celular" 
                    value={formData.celular} 
                    onChange={handleChange}
                    className="border border-gray-300 rounded p-2 w-full" 
                  />
                </div>
                <div className="mb-2">
                  <label className="block text-sm font-semibold mb-1">Email:</label>
                  <input 
                    type="email" 
                    name="email" 
                    value={formData.email} 
                    onChange={handleChange}
                    className="border border-gray-300 rounded p-2 w-full" 
                  />
                </div>
              </>
            )}

            <div className="flex justify-center mt-4">
              <button 
                className="bg-blue-500 text-white py-2 px-4 rounded"
                onClick={handleReservar} // Evento onClick para redirigir
              >
                Reservar
              </button>
            </div>
          </div>

          <div className="w-3/5 h-auto">
            <img src={currentImage} alt="Tipo de Reporte" className="w-full h-full object-cover" />
          </div>
        </div>
      </main>
    </div>
  );
};

export default PostReservation;

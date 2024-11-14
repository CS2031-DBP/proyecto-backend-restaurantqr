import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import Navbar from '../Components/navbar';

const Reservation = () => {
  const navigate = useNavigate();
  
  const [description, setDescription] = useState(
    "Soy un estudiante de Ingeniería Electrónica en UTEC que quiere desarrollar sus habilidades como diseñador de software. Actualmente curso el 8vo ciclo de la carrera."
  );

  const [entriesPerPage, setEntriesPerPage] = useState(10);
  const [currentPage, setCurrentPage] = useState(1);

  // Simular roles: admin, student o employee
  const [userRole, setUserRole] = useState('admin'); // Cambia este valor a 'student' o 'employee' para simular otros roles.

  // Estado para mantener los reportes
  const [reports, setReports] = useState([
    { name: "Fernando Muñoz", email: "fernando@example.com", date: "01/11/2024", status: "Pendiente", peopleCount: 5, table: "A1", time: "12:00 PM" },
    { name: "Ana Pérez", email: "ana@example.com", date: "01/11/2024", status: "Rechazado", peopleCount: 3, table: "B2", time: "1:00 PM" },
    { name: "Luis García", email: "luis@example.com", date: "01/11/2024", status: "Aceptado", peopleCount: 2, table: "C3", time: "2:00 PM" },
    // ... (continúa con tus datos)
  ]);

  const totalPages = Math.ceil(reports.length / entriesPerPage);

  const handleDescriptionChange = (e) => {
    setDescription(e.target.value);
  };

  const handleEntriesChange = (e) => {
    setEntriesPerPage(Number(e.target.value));
    setCurrentPage(1); // Reinicia a la primera página al cambiar el número de entradas
  };

  const handlePageChange = (pageNumber) => {
    if (pageNumber > 0 && pageNumber <= totalPages) {
      setCurrentPage(pageNumber);
    }
  };

  const startIndex = (currentPage - 1) * entriesPerPage;
  const currentEntries = reports.slice(startIndex, startIndex + entriesPerPage);

  return (
    <div className="bg-[#f5f5ff] flex min-h-screen">

      <main className="flex-1 p-4">
        <header className="navbar bg-base-100 shadow-lg mb-4">
          <Navbar /> {/* Usar el componente Navbar aquí */}
          <div className="flex justify-between items-center w-full px-4">
            <div className="flex items-center space-x-4">
              <h2 className="text-2xl font-bold mb-1">Tabla de Reservación</h2>
              <div className="flex space-x-4 mb-1">
                <span className="text-base font-semibold">Inicio / Reservación </span>
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

        <div className="flex space-x-4 mt-4 ml-40">
          <div className="bg-white p-4 rounded-lg shadow-md flex-1 flex flex-col">
            <div className="flex justify-between items-center mb-4">
              <a className="text-lg font-semibold hover:text-blue-500">Tus Reservas</a>
              <button onClick={() => navigate('/reservation/post-reservation')} className="px-4 py-2 bg-[#3661ff] text-white font-semibold rounded-lg hover:bg-blue-500 transition duration-300 mr-2">
                + Nueva Reserva
              </button>
            </div>
            <div className="mb-2">
              <span className="text-base">Reservas</span>
            </div>
            <div className="flex items-center mb-4">
              <select 
                value={entriesPerPage} 
                onChange={handleEntriesChange} 
                className="border border-gray-300 rounded p-1 mr-2"
              >
                <option value={1}>1</option>
                <option value={10}>10</option>
                <option value={15}>15</option>
                <option value={20}>20</option>
              </select>
              <span className="text-base">Entradas por página</span>
            </div>
            <hr className="my-4" />

            {/* Tabla de reportes */}
            <table className="min-w-full bg-white">
              <thead>
                <tr className="w-full text-left text-sm text-black uppercase tracking-wider">
                  <th className="py-2 px-4">Cliente</th>
                  <th className="py-2 px-4">Correo</th>
                  <th className="py-2 px-4">Fecha</th>
                  <th className="py-2 px-4">Hora</th>
                  <th className="py-2 px-4">Cantidad de Personas</th>
                  <th className="py-2 px-4">Mesa</th>
                  <th className="py-2 px-4">Acción</th> {/* Columna de acción */}
                </tr>
              </thead>
              <tbody>
                {currentEntries.map((report) => {
                  let statusColor = '';

                  // Definir color según el estado
                  switch (report.status) {
                    case 'Pendiente':
                      statusColor = 'bg-yellow-600'; // Amarillo
                      break;
                    case 'Rechazado':
                      statusColor = 'bg-red-600'; // Rojo
                      break;
                    case 'Aceptado':
                      statusColor = 'bg-green-600'; // Verde
                      break;
                    default:
                      statusColor = '';
                  }

                  return (
                    <tr className="border-t" key={report.email}>
                      <td className="py-2 px-4">{report.name}</td>
                      <td className="py-2 px-4">{report.email}</td>
                      <td className="py-2 px-4">{report.date}</td>
                      <td className="py-2 px-4">{report.time}</td>
                      <td className="py-2 px-4">{report.peopleCount}</td>
                      <td className="py-2 px-4">{report.table}</td>
                      <td className="py-2 px-4">
                        {/* Mostrar estado como etiqueta */}
                        <span className={`py-1 px-2 rounded-full text-white ${statusColor}`}>
                          {report.status}
                        </span>
                      </td>
                    </tr>
                  );
                })}
              </tbody>
            </table>

            {/* Paginación */}
            <div className="flex justify-between items-center mt-4">
              <button 
                onClick={() => handlePageChange(currentPage - 1)} 
                className="btn btn-outline"
                disabled={currentPage === 1}
              >
                Anterior
              </button>
              <div>
                Página {currentPage} de {totalPages}
              </div>
              <button 
                onClick={() => handlePageChange(currentPage + 1)} 
                className="btn btn-outline"
                disabled={currentPage === totalPages}
              >
                Siguiente
              </button>
            </div>
          </div>
        </div>
      </main>
    </div>
  );
};

export default Reservation;

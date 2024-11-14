import React from 'react';
import cesarImage from '../img/team/cesar_tinco.png';
import fernandoImage from '../img/team/fernando_munoz.jpeg';
import jaimeImage from '../img/team/jaime_farfan.jpg'; // Miembro 4
import oscarImage from '../img/team/oscar_ramirez.jpeg'; // Miembro 3
import { Link } from 'react-router-dom';

const Team = () => {
  return (
    <div className="bg-[#f5f5ff] flex min-h-screen">
      {/* Sidebar */}
      <aside className="bg-white w-64 p-4 shadow-lg">
        <h2 className="text-lg font-bold mb-4">Pages</h2>
        <ul className="menu">
        <li className="mb-2 transition duration-300">
            <Link to="/dashboard" className="block text-[#012970] hover:text-[#717ff5] font-bold hover:bg-[#f5f5ff] p-2 rounded">Tablero</Link>
          </li>
          <li className="mb-2 transition duration-300">
            <Link to="/reports" className="block text-[#012970] hover:text-[#717ff5] font-bold hover:bg-[#f5f5ff] p-2 rounded">Reportes</Link>
          </li>
          <li className="mb-2 transition duration-300">
            <Link to="/profile" className="block text-[#012970] hover:text-[#717ff5] font-bold hover:bg-[#f5f5ff] p-2 rounded">Perfil</Link>
          </li>
          <li className="mb-2 transition duration-300">
            <Link to ="/team" className="block text-blue-500 font-bold bg-[#f5f5ff] p-2 rounded">Equipo</Link>
          </li>
          <li className="mb-2 transition duration-300">
            <Link to ="/login" className="block text-[#012970] hover:text-[#717ff5] font-bold hover:bg-[#f5f5ff] p-2 rounded">Cerrar Sesión</Link>
          </li>
        </ul>
      </aside>

      {/* Main Content */}
      <main className="flex-1 p-4">
        <header className="navbar bg-base-100 shadow-lg mb-4">
          <div className="flex items-center">
            <div className="toggle-sidebar-btn mr-4" style={{ fontSize: '32px', paddingLeft: '10px', cursor: 'pointer', color: '#012970' }}>
              &#9776; {/* Icono de menú */}
            </div>
            <input type="text" placeholder="Search..." className="input input-bordered w-full max-w-xs" />
            <button className="btn ml-2">Search</button>
          </div>
          <div className="flex items-center ml-auto">
            <button className="btn btn-ghost">
              <span className="material-icons">notifications</span>
            </button>
            <span className="badge">3</span>
          </div>
        </header>

        <section className="bg-base-100 shadow-lg p-4 rounded-lg">
          <h2 className="text-2xl font-bold">Team</h2>
          <p>
            En nuestro equipo, la combinación de talento joven y la experiencia de nuestro profesor nos impulsa a desarrollar proyectos creativos y efectivos.
          </p>
          <div className="grid grid-cols-2 gap-4 mt-4">
            {/* Team Member 1 */}
            <div className="bg-base-100 p-4 rounded-lg shadow-md flex items-center">
              <img src={cesarImage} alt="César Tinco" className="w-24 h-24 object-cover rounded-full mr-4" />
              <div>
                <h4 className="font-semibold">César Tinco Aliaga</h4>
                <h5 className="text-sm text-gray-500">Estudiante Ing. Electrónica</h5>
                <p className="text-sm">Estudiante del octavo ciclo de Ingeniería Electrónica en UTEC, especializado en circuitos eléctricos, analógicos y digitales, con experiencia en microcontroladores y un enfoque en microelectrónica.</p>
              </div>
            </div>

            {/* Team Member 2 */}
            <div className="bg-base-100 p-4 rounded-lg shadow-md flex items-center">
              <img src={fernandoImage} alt="Fernando Muñoz" className="w-24 h-24 object-cover rounded-full mr-4" />
              <div>
                <h4 className="font-semibold">Fernando Muñoz Paúcar</h4>
                <h5 className="text-sm text-gray-500">Estudiante Ing. Electrónica</h5>
                <p className="text-sm">Estudiante del octavo ciclo de Ingeniería Electrónica en UTEC, con habilidades en el diseño y desarrollo de circuitos analógicos y digitales, así como en proyectos embebidos utilizando microcontroladores como Arduino y ESP32, con un interés particular en la microelectrónica.</p>
              </div>
            </div>

            {/* Team Member 3 */}
            <div className="bg-base-100 p-4 rounded-lg shadow-md flex items-center">
              <img src={oscarImage} alt="Óscar Ramírez" className="w-24 h-24 object-cover rounded-full mr-4" />
              <div>
                <h4 className="font-semibold">Oscar Ramírez Encinas</h4>
                <h5 className="text-sm text-gray-500">Estudiante Ing. Electrónica</h5>
                <p className="text-sm">Estudiante del octavo ciclo de Ingeniería Electrónica en UTEC, especializado en el armado de circuitos eléctricos y digitales, con experiencia en programación de microcontroladores como Arduino y ESP32, buscando contribuir a la electrónica en el país.</p>
              </div>
            </div>

            {/* Team Member 4 */}
            <div className="bg-base-100 p-4 rounded-lg shadow-md flex items-center">
              <img src={jaimeImage} alt="Jaime Farfán" className="w-24 h-24 object-cover rounded-full mr-4" />
              <div>
                <h4 className="font-semibold">Jaime Farfán</h4>
                <h5 className="text-sm text-gray-500">Asesor</h5>
                <p className="text-sm">Con una sólida trayectoria en consultoría y enseñanza, es experto en desarrollo de software, aplicaciones móviles y sistemas de alta disponibilidad.</p>
              </div>
            </div>
          </div>
        </section>
      </main>
    </div>
  );
};

export default Team;


import Login from './Pages/Login.tsx';
import Register from './Pages/Register.tsx';
import Team from './Pages/Team.tsx';
import UserProfile_Overview from './Pages/Profile-Overview.tsx';
import UserProfile_EditProfile from './Pages/Profile-EditProfile.tsx';
import UserProfile_Setting from './Pages/Profile-Setting.tsx';
import UserProfile_ChangePassword from './Pages/Profile-ChangePassword.tsx';
import Reservation from './Pages/Reservation.tsx';
import PostReservation from './Pages/ReservationPost.tsx';
import Menu from './Pages/Menu.tsx';
import MenuDetails from './Components/MenuDetails.tsx';

import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';

const App = () => {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Login />} />
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />
        <Route path="/menu" element={<Menu />} />
        <Route path="/menu/details" element={<MenuDetails />} />
        <Route path="/team" element={<Team />} />
        <Route path="/profile" element={<UserProfile_Overview />} />
        <Route path="/profile/overview" element={<UserProfile_Overview />} />
        <Route path="/profile/edit" element={<UserProfile_EditProfile />} />
        <Route path="/profile/settings" element={<UserProfile_Setting />} />
        <Route path="/profile/change-password" element={<UserProfile_ChangePassword />} />
        <Route path="/reservation" element={<Reservation />} />
        <Route path="/reservation/post-reservation" element={<PostReservation />} />
      </Routes>
    </Router>
  );
};

export default App;

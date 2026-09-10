import { BrowserRouter, Routes, Route, Link } from 'react-router-dom';
import { useAuth } from './context/AuthContext';
import LoginPage from './pages/LoginPage';
import RegisterPage from './pages/RegisterPage';
import BrowseRequestsPage from './pages/BrowseRequestsPage';
import RequestDetailsPage from './pages/RequestDetailsPage';
import CreateRequestPage from './pages/CreateRequestPage';
import ProfessionalProfilePage from './pages/ProfessionalProfilePage';
import MyBookingsPage from './pages/MyBookingsPage';
import CreateReviewPage from './pages/CreateReviewPage';
import ProfessionalReviewsPage from './pages/ProfessionalReviewsPage';

function Navbar() {
  const { user, logout } = useAuth();

  return (
    <nav style={{ background: '#0F8577', color: '#E3FBF6', padding: '1rem 1.5rem', display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
      <Link to="/" style={{ color: 'inherit', fontWeight: 500, textDecoration: 'none', fontSize: '17px' }}>ServiceHub</Link>
      <div style={{ display: 'flex', gap: '1rem', alignItems: 'center' }}>
        {user ? (
          <>
            <Link to="/bookings" style={{ color: 'inherit' }}>My Bookings</Link>
            {user.role === 'PROFESSIONAL' && (
              <Link to="/professionals/me" style={{ color: 'inherit' }}>My Profile</Link>
            )}
            <span style={{ fontSize: '13px' }}>{user.name}</span>
            <button onClick={logout}>Logout</button>
          </>
        ) : (
          <>
            <Link to="/login" style={{ color: 'inherit' }}>Login</Link>
            <Link to="/register" style={{ color: 'inherit' }}>Register</Link>
          </>
        )}
      </div>
    </nav>
  );
}

function App() {
  return (
    <BrowserRouter>
      <Navbar />
      <div style={{ padding: '1.5rem', maxWidth: '900px', margin: '0 auto' }}>
        <Routes>
          <Route path="/" element={<BrowseRequestsPage />} />
          <Route path="/login" element={<LoginPage />} />
          <Route path="/register" element={<RegisterPage />} />
          <Route path="/requests/new" element={<CreateRequestPage />} />
          <Route path="/requests/:id" element={<RequestDetailsPage />} />
          <Route path="/professionals/me" element={<ProfessionalProfilePage />} />
          <Route path="/bookings" element={<MyBookingsPage />} />
          <Route path="/reviews/new/:bookingId" element={<CreateReviewPage />} />
          <Route path="/reviews/professional/:professionalId" element={<ProfessionalReviewsPage />} />
        </Routes>
      </div>
    </BrowserRouter>
  );
}

export default App;
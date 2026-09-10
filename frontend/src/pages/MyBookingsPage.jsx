import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { get, patch } from '../api/client';

const STATUS_COLORS = {
  SCHEDULED: { bg: '#4C9BE8', text: '#04203C' },
  IN_PROGRESS: { bg: '#EF9F27', text: '#412402' },
  COMPLETED: { bg: '#1D9E75', text: '#04342C' },
  CANCELLED: { bg: '#e0e0e0', text: '#666' },
};

export default function MyBookingsPage() {
  const [bookings, setBookings] = useState([]);
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(true);

  function loadBookings() {
    get('/bookings/me')
      .then(setBookings)
      .catch((err) => setError(err.message))
      .finally(() => setLoading(false));
  }

  useEffect(loadBookings, []);

  async function handleComplete(id) {
    setError('');
    try {
      await patch(`/bookings/${id}/complete`);
      loadBookings();
    } catch (err) {
      setError(err.message);
    }
  }

  async function handleCancel(id) {
    setError('');
    try {
      await patch(`/bookings/${id}/cancel`);
      loadBookings();
    } catch (err) {
      setError(err.message);
    }
  }

  if (loading) return <p>Loading...</p>;

  return (
    <div>
      <h2>My Bookings</h2>
      {error && <p style={{ color: '#D4537E' }}>{error}</p>}
      {bookings.length === 0 && <p>No bookings yet.</p>}

      {bookings.map((b) => {
        const color = STATUS_COLORS[b.status] || STATUS_COLORS.SCHEDULED;
        return (
          <div key={b.id} style={{ background: color.bg, color: color.text, borderRadius: '14px', padding: '1.1rem 1.25rem', marginBottom: '10px' }}>
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start' }}>
              <p style={{ margin: 0, fontWeight: 500 }}>{b.professional.businessName}</p>
              <span style={{ background: 'rgba(255,255,255,0.4)', fontSize: '11px', padding: '3px 10px', borderRadius: '999px', fontWeight: 500 }}>
                {b.status}
              </span>
            </div>
            <p style={{ margin: '4px 0 0', fontSize: '13px', opacity: 0.85 }}>Customer: {b.customer.name}</p>

            <div style={{ marginTop: '10px', display: 'flex', gap: '8px' }}>
              {b.status === 'SCHEDULED' && (
                <>
                  <button onClick={() => handleComplete(b.id)}>Complete</button>
                  <button onClick={() => handleCancel(b.id)}>Cancel</button>
                </>
              )}
              {b.status === 'COMPLETED' && (
                <Link to={`/reviews/new/${b.id}`}>
                  <button>Leave review</button>
                </Link>
              )}
            </div>
          </div>
        );
      })}
    </div>
  );
}
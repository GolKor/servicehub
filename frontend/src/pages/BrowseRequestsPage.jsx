import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { get } from '../api/client';

const CATEGORY_COLORS = {
  Plumbing: { bg: '#D4537E', text: '#4B1528' },
  Electrical: { bg: '#8D63D6', text: '#FFFFFF' },
  Cleaning: { bg: '#EF9F27', text: '#412402' },
  Painting: { bg: '#4C9BE8', text: '#04203C' },
};

function getColor(categoryName) {
  return CATEGORY_COLORS[categoryName] || { bg: '#4C9BE8', text: '#04203C' };
}

export default function BrowseRequestsPage() {
  const [requests, setRequests] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    get('/requests')
      .then(setRequests)
      .catch((err) => setError(err.message))
      .finally(() => setLoading(false));
  }, []);

  if (loading) return <p>Loading...</p>;
  if (error) return <p style={{ color: '#D4537E' }}>{error}</p>;

  return (
    <div>
      <div style={{ background: '#0F8577', color: '#E3FBF6', padding: '1.5rem', borderRadius: '16px', marginBottom: '1.5rem' }}>
        <h2 style={{ margin: '0 0 4px', fontWeight: 500 }}>Βρες τον κατάλληλο επαγγελματία</h2>
        <p style={{ margin: 0, fontSize: '13px', opacity: 0.85 }}>{requests.length} ανοιχτά αιτήματα κοντά σου</p>
      </div>

      <div style={{ display: 'flex', justifyContent: 'flex-end', marginBottom: '1rem' }}>
        <Link to="/requests/new">
          <button>+ New request</button>
        </Link>
      </div>

      {requests.length === 0 && <p>No open requests yet.</p>}

      {requests.map((req) => {
        const color = getColor(req.category.name);
        return (
          <Link key={req.id} to={`/requests/${req.id}`} style={{ textDecoration: 'none', color: 'inherit' }}>
            <div style={{ background: color.bg, color: color.text, borderRadius: '14px', padding: '1.1rem 1.25rem', marginBottom: '10px' }}>
              <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start' }}>
                <p style={{ fontWeight: 500, fontSize: '15px', margin: 0 }}>{req.title}</p>
                <span style={{ background: 'rgba(255,255,255,0.4)', fontSize: '11px', padding: '3px 10px', borderRadius: '999px', fontWeight: 500 }}>
                  {req.status}
                </span>
              </div>
              <p style={{ fontSize: '13px', margin: '4px 0 0', opacity: 0.85 }}>
                {req.location} · {req.customer.name}
              </p>
              <p style={{ fontSize: '14px', fontWeight: 500, margin: '10px 0 0' }}>
                {req.offers.length} {req.offers.length === 1 ? 'offer' : 'offers'}
              </p>
            </div>
          </Link>
        );
      })}
    </div>
  );
}
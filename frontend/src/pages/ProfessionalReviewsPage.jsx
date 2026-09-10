import { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { get } from '../api/client';

export default function ProfessionalReviewsPage() {
  const { professionalId } = useParams();
  const [reviews, setReviews] = useState([]);
  const [error, setError] = useState('');

  useEffect(() => {
    get(`/reviews/professional/${professionalId}`)
      .then(setReviews)
      .catch((err) => setError(err.message));
  }, [professionalId]);

  return (
    <div>
      <h2>Reviews</h2>
      {error && <p style={{ color: '#D4537E' }}>{error}</p>}
      {reviews.length === 0 && <p>No reviews yet.</p>}
      {reviews.map((r) => (
        <div key={r.id} style={{ border: '0.5px solid #ddd', borderRadius: '12px', padding: '1rem', marginBottom: '10px' }}>
          <p style={{ margin: 0, fontWeight: 500 }}>{'⭐'.repeat(r.rating)}</p>
          <p style={{ margin: '4px 0', fontSize: '13px' }}>{r.comment}</p>
          <p style={{ margin: 0, fontSize: '12px', color: '#666' }}>— {r.customer.name}</p>
        </div>
      ))}
    </div>
  );
}
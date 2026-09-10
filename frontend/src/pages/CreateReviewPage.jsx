import { useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { post } from '../api/client';

export default function CreateReviewPage() {
  const { bookingId } = useParams();
  const [rating, setRating] = useState(5);
  const [comment, setComment] = useState('');
  const [error, setError] = useState('');
  const navigate = useNavigate();

  async function handleSubmit(e) {
    e.preventDefault();
    setError('');
    try {
      await post('/reviews', { rating: Number(rating), comment, bookingId: Number(bookingId) });
      navigate('/bookings');
    } catch (err) {
      setError(err.message);
    }
  }

  return (
    <div style={{ maxWidth: '360px' }}>
      <h2>Leave a Review</h2>
      <form onSubmit={handleSubmit} style={{ display: 'flex', flexDirection: 'column', gap: '10px' }}>
        <label style={{ fontSize: '13px', fontWeight: 500 }}>Rating</label>
        <select value={rating} onChange={(e) => setRating(e.target.value)}>
          <option value={5}>⭐⭐⭐⭐⭐ (5)</option>
          <option value={4}>⭐⭐⭐⭐ (4)</option>
          <option value={3}>⭐⭐⭐ (3)</option>
          <option value={2}>⭐⭐ (2)</option>
          <option value={1}>⭐ (1)</option>
        </select>
        <textarea placeholder="Your comment" value={comment} onChange={(e) => setComment(e.target.value)} />
        {error && <p style={{ color: '#D4537E' }}>{error}</p>}
        <button type="submit">Submit review</button>
      </form>
    </div>
  );
}
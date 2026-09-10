import { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { get, post, patch } from '../api/client';
import { useAuth } from '../context/AuthContext';

export default function RequestDetailsPage() {
  const { id } = useParams();
  const { user } = useAuth();
  const [request, setRequest] = useState(null);
  const [error, setError] = useState('');
  const [price, setPrice] = useState('');
  const [message, setMessage] = useState('');

  function loadRequest() {
    get(`/requests/${id}`).then(setRequest).catch((err) => setError(err.message));
  }

  useEffect(loadRequest, [id]);

  async function handleMakeOffer(e) {
    e.preventDefault();
    setError('');
    try {
      await post('/offers', { price: Number(price), message, serviceRequestId: Number(id) });
      setPrice('');
      setMessage('');
      loadRequest();
    } catch (err) {
      setError(err.message);
    }
  }

  async function handleAcceptOffer(offerId) {
    setError('');
    try {
      await patch(`/offers/${offerId}/accept`);
      loadRequest();
    } catch (err) {
      setError(err.message);
    }
  }

  if (!request) return <p>Loading...</p>;

  const isOwner = user && user.email && request.customer && user.name === request.customer.name;

  return (
    <div>
      <h2>{request.title}</h2>
      <p style={{ color: '#666' }}>{request.location} · {request.category.name} · {request.status}</p>
      <p>{request.description}</p>

      {error && <p style={{ color: '#D4537E' }}>{error}</p>}

      <h3>Offers ({request.offers.length})</h3>
      {request.offers.length === 0 && <p>No offers yet.</p>}
      {request.offers.map((offer) => (
        <div key={offer.id} style={{ border: '0.5px solid #ddd', borderRadius: '12px', padding: '1rem', marginBottom: '10px' }}>
          <p style={{ margin: 0, fontWeight: 500 }}>{offer.professional.businessName} — €{offer.price}</p>
          <p style={{ margin: '4px 0', fontSize: '13px', color: '#666' }}>{offer.message}</p>
          <p style={{ margin: '4px 0', fontSize: '13px' }}>Status: {offer.status}</p>
          {offer.status === 'PENDING' && (
            <button onClick={() => handleAcceptOffer(offer.id)}>Accept offer</button>
          )}
        </div>
      ))}

      {user && user.role === 'PROFESSIONAL' && request.status === 'OPEN' && (
        <>
          <h3>Make an offer</h3>
          <form onSubmit={handleMakeOffer} style={{ display: 'flex', flexDirection: 'column', gap: '10px', maxWidth: '360px' }}>
            <input type="number" placeholder="Price (€)" value={price} onChange={(e) => setPrice(e.target.value)} required />
            <textarea placeholder="Message" value={message} onChange={(e) => setMessage(e.target.value)} required />
            <button type="submit">Submit offer</button>
          </form>
        </>
      )}
    </div>
  );
}
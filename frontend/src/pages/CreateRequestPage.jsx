import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { get, post } from '../api/client';

export default function CreateRequestPage() {
  const [categories, setCategories] = useState([]);
  const [title, setTitle] = useState('');
  const [description, setDescription] = useState('');
  const [location, setLocation] = useState('');
  const [categoryId, setCategoryId] = useState('');
  const [error, setError] = useState('');
  const navigate = useNavigate();

  useEffect(() => {
    get('/categories').then(setCategories);
  }, []);

  async function handleSubmit(e) {
    e.preventDefault();
    setError('');
    try {
      const created = await post('/requests', {
        title,
        description,
        location,
        categoryId: Number(categoryId),
      });
      navigate(`/requests/${created.id}`);
    } catch (err) {
      setError(err.message);
    }
  }

  return (
    <div style={{ maxWidth: '400px' }}>
      <h2>New Service Request</h2>
      <form onSubmit={handleSubmit} style={{ display: 'flex', flexDirection: 'column', gap: '10px' }}>
        <input type="text" placeholder="Title" value={title} onChange={(e) => setTitle(e.target.value)} required />
        <textarea placeholder="Description" value={description} onChange={(e) => setDescription(e.target.value)} />
        <input type="text" placeholder="Location" value={location} onChange={(e) => setLocation(e.target.value)} required />
        <select value={categoryId} onChange={(e) => setCategoryId(e.target.value)} required>
          <option value="">Select category</option>
          {categories.map((c) => (
            <option key={c.id} value={c.id}>{c.name}</option>
          ))}
        </select>
        {error && <p style={{ color: '#D4537E' }}>{error}</p>}
        <button type="submit">Create request</button>
      </form>
    </div>
  );
}
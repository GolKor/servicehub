import { useEffect, useState } from 'react';
import { get, post, put } from '../api/client';

export default function ProfessionalProfilePage() {
  const [categories, setCategories] = useState([]);
  const [profile, setProfile] = useState(null);
  const [exists, setExists] = useState(false);

  const [businessName, setBusinessName] = useState('');
  const [description, setDescription] = useState('');
  const [location, setLocation] = useState('');
  const [phone, setPhone] = useState('');
  const [experienceYears, setExperienceYears] = useState('');
  const [selectedCategoryIds, setSelectedCategoryIds] = useState([]);

  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');

  useEffect(() => {
    get('/categories').then(setCategories);

    get('/professionals/me')
      .then((data) => {
        setProfile(data);
        setExists(true);
        setBusinessName(data.businessName);
        setDescription(data.description || '');
        setLocation(data.location);
        setPhone(data.phone);
        setExperienceYears(data.experienceYears || '');
        setSelectedCategoryIds(data.categories.map((c) => c.id));
      })
      .catch(() => setExists(false));
  }, []);

  function toggleCategory(id) {
    setSelectedCategoryIds((prev) =>
      prev.includes(id) ? prev.filter((c) => c !== id) : [...prev, id]
    );
  }

  async function handleSubmit(e) {
    e.preventDefault();
    setError('');
    setSuccess('');

    const dto = {
      businessName,
      description,
      location,
      phone,
      experienceYears: experienceYears ? Number(experienceYears) : null,
      categoryIds: selectedCategoryIds,
    };

    try {
      const result = exists ? await put('/professionals/me', dto) : await post('/professionals/me', dto);
      setProfile(result);
      setExists(true);
      setSuccess(exists ? 'Profile updated!' : 'Profile created!');
    } catch (err) {
      setError(err.message);
    }
  }

  return (
    <div style={{ maxWidth: '400px' }}>
      <h2>{exists ? 'Edit Professional Profile' : 'Create Professional Profile'}</h2>

      {profile && (
        <p style={{ fontSize: '13px', color: '#666' }}>
          Rating: {profile.rating.toFixed(1)} ⭐
        </p>
      )}

      <form onSubmit={handleSubmit} style={{ display: 'flex', flexDirection: 'column', gap: '10px' }}>
        <input type="text" placeholder="Business name" value={businessName} onChange={(e) => setBusinessName(e.target.value)} required />
        <textarea placeholder="Description" value={description} onChange={(e) => setDescription(e.target.value)} />
        <input type="text" placeholder="Location" value={location} onChange={(e) => setLocation(e.target.value)} required />
        <input type="text" placeholder="Phone" value={phone} onChange={(e) => setPhone(e.target.value)} required />
        <input type="number" placeholder="Years of experience" value={experienceYears} onChange={(e) => setExperienceYears(e.target.value)} />

        <p style={{ margin: '4px 0 0', fontSize: '13px', fontWeight: 500 }}>Categories</p>
        <div style={{ display: 'flex', flexWrap: 'wrap', gap: '6px' }}>
          {categories.map((c) => (
            <label
              key={c.id}
              style={{
                display: 'flex',
                alignItems: 'center',
                gap: '4px',
                fontSize: '13px',
                border: '0.5px solid #ccc',
                borderRadius: '999px',
                padding: '4px 10px',
                cursor: 'pointer',
              }}
            >
              <input
                type="checkbox"
                checked={selectedCategoryIds.includes(c.id)}
                onChange={() => toggleCategory(c.id)}
              />
              {c.name}
            </label>
          ))}
        </div>

        {error && <p style={{ color: '#D4537E' }}>{error}</p>}
        {success && <p style={{ color: '#1D9E75' }}>{success}</p>}

        <button type="submit">{exists ? 'Update profile' : 'Create profile'}</button>
      </form>
    </div>
  );
}
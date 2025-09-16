export default function Home() {
  return (
    <section style={{ display: 'grid', gap: 12 }}>
      <h1>Welcome to Qare</h1>
      <p>Qare helps you track medical supplies with a simple, fast workflow.</p>
      <img
        src="/home.PNG"
        alt="Qare preview"
        style={{ width: '100%', maxWidth: 500, maxHeight:500, borderRadius: 12, boxShadow: '0 4px 20px rgba(0,0,0,0.08)' }}
      />
    </section>
  )
}

import { NavLink, Outlet } from "react-router-dom"
import {useMemo} from 'react'

export default function APP() {
  const linkStyle = useMemo(
    () => ({ padding: '0.5rem 0.75rem', borderRadius: 8, textDecoration: 'none' }),
    [],
  )

  return (
    <div style={{ fontFamily: 'system-ui, -apple-system, Segoe UI, Roboto, sans-serif'}}>
      <nav style={{ display: 'flex', gap: 8, padding: '12px 16px', borderBottom: '1px solid #eee' }}>
        <NavLink to="/" style={({ isActive }) => ({
          ...linkStyle,
          color: isActive ? 'white' : '#111',
          background: isActive ? '#111' : 'transparent',
        })}>Home</NavLink>

        <NavLink to="/store" style={({ isActive }) => ({
          ...linkStyle,
          color: isActive ? 'white' : '#111',
          background: isActive ? '#111' : 'transparent',
        })}>Store</NavLink>

        <NavLink to="/admin" style={({ isActive }) => ({
          ...linkStyle,
          color: isActive ? 'white' : '#111',
          background: isActive ? '#111' : 'transparent',
        })}>Admin</NavLink>
      </nav>

      <main style={{ maxWidth: 960, margin: '2rem auto', padding: '0 1rem' }}>
        <Outlet />
      </main>
    </div>
  )
}
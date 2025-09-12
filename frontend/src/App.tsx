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

        {/* push the search icon to the other side */}
        <div style={{ marginLeft: 'auto' }} />

        <NavLink to="/search" aria-label="Search" style={({ isActive }) => ({
          padding: 8,
          borderRadius: 999,
          display: 'inline-flex',
          alignItems: 'center',
          justifyContent: 'center',
          background: isActive ? '#777' : 'transparent',
        })}>
          {/* inline magnifying glass icon */}
          <svg width="20" height="20" viewBox="0 0 24 24" fill={ 'currentColor' }
               style={{ color: 'black' }} aria-hidden="true">
            <path d="M21 20.3 16.7 16a7.5 7.5 0 1 0-1.4 1.4L20.3 21 21 20.3zM10.5 16a5.5 5.5 0 1 1 0-11 5.5 5.5 0 0 1 0 11z"/>
          </svg>
        </NavLink>
      </nav>

      <main style={{ maxWidth: 960, margin: '2rem auto', padding: '0 1rem' }}>
        <Outlet />
      </main>
    </div>
  )
}
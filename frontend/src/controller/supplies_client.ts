import type { Supply } from '../model/supply_model'

export async function getAll(): Promise<Supply[]> {
  const r = await fetch('/api/supplies')
  if (!r.ok) throw new Error('Failed to load supplies')
  return r.json()
}

export async function createOne(s: Supply): Promise<Supply> {
  const r = await fetch('/api/supplies', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(s),
  })
  if (!r.ok) {
    const txt = await r.text()
    throw new Error(`Create failed: ${r.status} ${txt}`)
  }
  return r.json()
}

export async function updateOne(s: Supply): Promise<Supply> {
  const r = await fetch(`/api/supplies/${encodeURIComponent(s.name)}`, {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(s),
  })
  if (!r.ok) {
    const txt = await r.text()
    throw new Error(`Update failed: ${r.status} ${txt}`)
  }
  return r.json()
}

export async function removeOne(name: string): Promise<void> {
  const r = await fetch(`/api/supplies/${encodeURIComponent(name)}`, { method: 'DELETE' })
  if (!r.ok && r.status !== 204 && r.status !== 404) {
    const txt = await r.text()
    throw new Error(`Delete failed: ${r.status} ${txt}`)
  }
}

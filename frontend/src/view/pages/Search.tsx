import { useState } from 'react'
import type { Supply } from '../../model/supply_model' // adjust import if needed
import { getOne } from '../../controller/supplies_client'
import SuppliesTable from '../components/supplyTable_view'

export default function Search() {
  const [query, setQuery] = useState('')
  const [result, setResult] = useState<Supply | null>(null)
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)
  const [searched, setSearched] = useState(false)

  const onSearch = async (e: React.FormEvent) => {
    e.preventDefault()
    setError(null)
    setSearched(true)
    const name = query.trim()
    if (!name) { setResult(null); return }
    setLoading(true)
    try {
      const item = await getOne(name)
      setResult(item)
    } catch (err: any) {
      setError(err.message ?? 'Search failed')
      setResult(null)
    } finally {
      setLoading(false)
    }
  }

  return (
    <section style={{ display: 'grid', gap: 12 }}>
      <h1>Search</h1>
      <form onSubmit={onSearch} style={{ display: 'flex', gap: 8 }}>
        <input
          placeholder="Enter supply name…"
          value={query}
          onChange={e => setQuery(e.target.value)}
          style={{ flex: 1, padding: 8 }}
          aria-label="Search by name"
        />
        <button type="submit">Search</button>
      </form>

      {loading && <p>Searching…</p>}
      {error && <p style={{ color: 'crimson' }}>{error}</p>}
      {!loading && searched && result === null && !error && (
        <p>No supply found with name “{query.trim()}”.</p>
      )}
      {!loading && result && (
        <SuppliesTable items={[result]} readonly />
      )}
    </section>
  )
}

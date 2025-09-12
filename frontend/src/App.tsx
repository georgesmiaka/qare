import { useEffect, useState } from 'react'
import type { Supply } from './model/supply_model'
import { getAll, createOne, updateOne, removeOne } from './controller/supplies_client'
import SupplyForm from './view/supplyForm_view'
import SuppliesList from './view/supplyList_view'

export default function App() {
  const [items, setItems] = useState<Supply[]>([])
  const [error, setError] = useState<string | null>(null)
  const [loading, setLoading] = useState(true)

  const load = async () => {
    setLoading(true)
    setError(null)
    try {
      const data = await getAll()
      setItems(data)
    } catch (e: any) {
      setError(e.message ?? 'Failed to load')
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => { load() }, [])

  const upsert = async (s: Supply) => {
    // If it exists, update; else create
    const exists = items.some(i => i.name === s.name)
    if (exists) await updateOne(s)
    else await createOne(s)
    await load()
  }

  const remove = async (name: string) => {
    await removeOne(name)
    await load()
  }

  return (
    <div style={{ maxWidth: 900, margin: '2rem auto', padding: '0 1rem', display: 'grid', gap: 24 }}>
      <h1>Qare Supplies</h1>
      <SupplyForm onSubmit={upsert} />
      {loading ? <p>Loading…</p> : error ? <p style={{ color: 'crimson' }}>{error}</p> : <SuppliesList items={items} onDelete={remove} />}
    </div>
  )
}

import { useEffect, useState } from 'react'
import type { Supply } from '../../model/supply_model'
import { getAll } from '../../controller/supplies_client'
import SuppliesTable from '../components/supplyTable_view'

export default function Store() {
  const [items, setItems] = useState<Supply[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  const load = async () => {
    setLoading(true); setError(null)
    try {
      setItems(await getAll())
    } catch (e: any) {
      setError(e.message ?? 'Failed to load')
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => { load() }, [])

  if (loading) return <p>Loading…</p>
  if (error) return <p style={{ color: 'crimson' }}>{error}</p>
  return (
    <section>
      <h1>Store</h1>
      <SuppliesTable items={items} readonly />
    </section>
  )
}

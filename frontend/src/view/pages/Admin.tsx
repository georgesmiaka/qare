import { useEffect, useState } from 'react'
import type { Supply } from '../../model/supply_model'
import { getAll, createOne, updateOne, removeOne } from '../../controller/supplies_client'
import SupplyForm from '../components/supplyForm_view'
import SuppliesTable from '../components/supplyTable_view'

export default function Admin() {
  const [items, setItems] = useState<Supply[]>([])
  const [selected, setSelected] = useState<Supply | null>(null)
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

  const upsert = async (s: Supply) => {
    const exists = items.some(i => i.name === s.name)
    if (exists) await updateOne(s)
    else await createOne(s)
    setSelected(null)
    await load()
  }

  const remove = async (name: string) => {
    await removeOne(name)
    if (selected?.name === name) setSelected(null)
    await load()
  }

  if (loading) return <p>Loading…</p>
  if (error) return <p style={{ color: 'crimson' }}>{error}</p>
  return (
    <section style={{ display: 'grid', gap: 16 }}>
      <h1>Admin</h1>
      <SupplyForm
        initial={selected}
        onSubmit={upsert}
        onCancel={() => setSelected(null)}
      />
      <SuppliesTable
        items={items}
        onEdit={setSelected}
        onDelete={remove}
      />
    </section>
  )
}

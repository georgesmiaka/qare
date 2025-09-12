import { useState } from 'react'
import type { Supply } from '../model/supply_model'

type Props = { onSubmit: (s: Supply) => Promise<void> }

export default function SupplyForm({ onSubmit }: Props) {
  const [name, setName] = useState('')
  const [amount, setAmount] = useState<string>('')
  const [unitName, setUnitName] = useState('')
  const [error, setError] = useState<string | null>(null)
  const [loading, setLoading] = useState(false)

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    setError(null)
    const amt = amount.trim() === '' ? NaN : Number(amount)
if (!Number.isFinite(amt) || amt < 0) {
  setError('Please provide a non-negative amount.')
  return
}
    setLoading(true)
    try {
      await onSubmit({ name: name.trim(), amount: amt, unitName: unitName.trim() })
      setName(''); setAmount(''); setUnitName('')
    } catch (err: any) {
      setError(err.message ?? 'Failed to save')
    } finally {
      setLoading(false)
    }
  }

  return (
    <form onSubmit={handleSubmit} style={{ display: 'grid', gap: 8, maxWidth: 420 }}>
      <h2>Add / Update Supply</h2>
      <input placeholder="Name" value={name} onChange={e => setName(e.target.value)} />
      <input
        type="number"
  min={0}
  value={amount}
  onChange={(e) => setAmount(e.target.value)}
      />
      <input placeholder="Unit (e.g. kg, pack)" value={unitName} onChange={e => setUnitName(e.target.value)} />
      <button disabled={loading} type="submit">{loading ? 'Saving…' : 'Save'}</button>
      {error && <div style={{ color: 'crimson' }}>{error}</div>}
    </form>
  )
}

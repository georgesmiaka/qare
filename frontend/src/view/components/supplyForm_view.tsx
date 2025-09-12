import { useEffect, useState } from 'react'
import type { Supply } from '../../model/supply_model'

type Props = {
  initial?: Supply | null
  onSubmit: (s: Supply) => Promise<void>
  onCancel?: () => void
}

export default function SupplyForm({ initial, onSubmit, onCancel }: Props) {
  const [name, setName] = useState<string>(initial?.name ?? '')
  const [amount, setAmount] = useState<string>(initial ? String(initial.amount) : '')
  const [unitName, setUnitName] = useState<string>(initial?.unitName ?? '')
  const [error, setError] = useState<string | null>(null)
  const [loading, setLoading] = useState(false)

  useEffect(() => {
    setName(initial?.name ?? '')
    setAmount(initial ? String(initial.amount) : '')
    setUnitName(initial?.unitName ?? '')
  }, [initial])

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    setError(null)
    const amt = amount.trim() === '' ? NaN : Number(amount)
    if (!name.trim() || !unitName.trim() || !Number.isFinite(amt) || amt < 0) {
      setError('Please provide name, non-negative amount, and unit.')
      return
    }
    setLoading(true)
    try {
      await onSubmit({ name: name.trim(), amount: amt, unitName: unitName.trim() })
    } catch (err: any) {
      setError(err.message ?? 'Failed to save')
    } finally {
      setLoading(false)
    }
  }

  return (
    <form onSubmit={handleSubmit} style={{ display: 'grid', gap: 8, maxWidth: 460 }}>
      <h2>{initial ? 'Edit Supply' : 'Add Supply'}</h2>
      <input
        placeholder="Name"
        value={name}
        onChange={e => setName(e.target.value)}
        disabled={Boolean(initial)} // name is the key; immutable in edit mode
      />
      <input
        placeholder="Amount"
        inputMode="numeric"
        value={amount}
        onChange={e => setAmount(e.target.value)}
      />
      <input
        placeholder="Unit (e.g. kg, pack)"
        value={unitName}
        onChange={e => setUnitName(e.target.value)}
      />
      <div style={{ display: 'flex', gap: 8 }}>
        <button disabled={loading} type="submit">{loading ? 'Saving…' : (initial ? 'Update' : 'Create')}</button>
        {initial && <button type="button" onClick={onCancel}>Cancel</button>}
      </div>
      {error && <div style={{ color: 'crimson' }}>{error}</div>}
    </form>
  )
}

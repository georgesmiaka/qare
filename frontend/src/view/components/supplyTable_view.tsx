import type { Supply } from '../../model/supply_model'

type Props = {
  items: Supply[]
  readonly?: boolean
  onEdit?: (s: Supply) => void
  onDelete?: (name: string) => void
}

export default function SuppliesTable({ items, readonly, onEdit, onDelete }: Props) {
  if (!items.length) return <p>No supplies yet.</p>

  return (
    <table style={{ borderCollapse: 'collapse', width: '100%' }}>
      <thead>
        <tr>
          <th style={{ textAlign: 'left', borderBottom: '1px solid #eee', padding: 8 }}>Name</th>
          <th style={{ textAlign: 'right', borderBottom: '1px solid #eee', padding: 8 }}>Amount</th>
          <th style={{ textAlign: 'left', borderBottom: '1px solid #eee', padding: 8 }}>Unit</th>
          {!readonly && <th style={{ borderBottom: '1px solid #eee' }} />}
        </tr>
      </thead>
      <tbody>
        {items.map(s => (
          <tr key={s.name}>
            <td style={{ padding: 8 }}>{s.name}</td>
            <td style={{ padding: 8, textAlign: 'right' }}>{s.amount}</td>
            <td style={{ padding: 8 }}>{s.unitName}</td>
            {!readonly && (
              <td style={{ padding: 8, display: 'flex', gap: 8 }}>
                <button onClick={() => onEdit?.(s)}>Edit</button>
                <button onClick={() => onDelete?.(s.name)}>Delete</button>
              </td>
            )}
          </tr>
        ))}
      </tbody>
    </table>
  )
}

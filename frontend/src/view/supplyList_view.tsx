import type { Supply } from '../model/supply_model'

type Props = {
  items: Supply[]
  onDelete: (name: string) => Promise<void>
}

export default function SuppliesList({ items, onDelete }: Props) {
  if (!items.length) return <p>No supplies yet.</p>
  return (
    <table style={{ borderCollapse: 'collapse', minWidth: 480 }}>
      <thead>
        <tr>
          <th style={{ textAlign: 'left', borderBottom: '1px solid #ddd' }}>Name</th>
          <th style={{ textAlign: 'right', borderBottom: '1px solid #ddd' }}>Amount</th>
          <th style={{ textAlign: 'left', borderBottom: '1px solid #ddd' }}>Unit</th>
          <th />
        </tr>
      </thead>
      <tbody>
        {items.map(s => (
          <tr key={s.name}>
            <td>{s.name}</td>
            <td style={{ textAlign: 'right' }}>{s.amount}</td>
            <td>{s.unitName}</td>
            <td>
              <button onClick={() => onDelete(s.name)}>Delete</button>
            </td>
          </tr>
        ))}
      </tbody>
    </table>
  )
}

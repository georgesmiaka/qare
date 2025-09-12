import type { Supply } from '../../model/supply_model'

type Props = {
    items: Supply[]
    readonly?: boolean
    onEdit?: (s: Supply) => void
    onDelete?: (name: string) => void
}

function getStatus(amount: number) {
    if (amount > 10) return { label: 'OK', bg: '#22c55e' }
    if (amount >= 5) return { label: 'Medium', bg: '#f97316' }
    return { label: 'Low', bg: '#ef4444' }
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
                    <th style={{ textAlign: 'left', borderBottom: '1px solid #eee', padding: 8 }}>Status</th>
                    {!readonly && <th style={{ borderBottom: '1px solid #eee' }} />}
                </tr>
            </thead>
            <tbody>
                {items.map(s => {
                    const status = getStatus(s.amount)
                    return (
                        <tr key={s.name}>
                            <td style={{ padding: 8 }}>{s.name}</td>
                            <td style={{ padding: 8, textAlign: 'right' }}>{s.amount}</td>
                            <td style={{ padding: 8 }}>{s.unitName}</td>
                            <td style={{ padding: 8 }}>
                                <span
                                    style={{
                                        backgroundColor: status.bg,
                                        color: 'white',
                                        borderRadius: 999,
                                        padding: '2px 10px',
                                        fontSize: 12,
                                        display: 'inline-block',
                                        lineHeight: 1.6,
                                    }}
                                >
                                    {status.label}
                                </span>
                            </td>
                            {!readonly && (
                                <td style={{ padding: 8, display: 'flex', gap: 8 }}>
                                    <button onClick={() => onEdit?.(s)}>Edit</button>
                                    <button onClick={() => onDelete?.(s.name)}>Delete</button>
                                </td>
                            )}
                        </tr>
                    )
                })}

            </tbody>
        </table>
    )
}

import { BiPencil, BiShow, BiTrash } from 'react-icons/bi';

interface Props {
    itemLabel: string;
    onPreview?: () => void;
    onEdit?: () => void;
    onDelete?: () => void;
}

const btn = { background: 'none', border: 'none', cursor: 'pointer', padding: '4px', display: 'flex', alignItems: 'center', justifyContent: 'center' } as const;

export default function CastItemActions({ itemLabel, onPreview, onEdit, onDelete }: Props) {
    return (
        <div style={{ display: 'flex', gap: '4px', flexShrink: 0 }}>
            {onPreview && (
                <button type="button" onClick={onPreview} style={{ ...btn, color: 'var(--color-muted-text)' }} title="View traceability">
                    <BiShow size={18} />
                </button>
            )}
            {onEdit && (
                <button type="button" onClick={onEdit} style={{ ...btn, color: '#60a5fa' }} title={`Edit ${itemLabel}`}>
                    <BiPencil size={18} />
                </button>
            )}
            {onDelete && (
                <button type="button" onClick={onDelete} style={{ ...btn, color: '#ef4444' }} title={`Delete ${itemLabel}`}>
                    <BiTrash size={18} />
                </button>
            )}
        </div>
    );
}

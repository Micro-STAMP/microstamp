import { BiLinkExternal } from 'react-icons/bi';
import { ModalContainer, ModalHeader } from '@components/Modal/Templates';
import type { ModalProps } from '@components/Modal/Templates';

export interface TraceabilityRelatedItem {
    id: string;
    code: string;
    name: string;
    description?: string;
    onGoTo?: () => void;
}

export interface TraceabilityRelatedGroup {
    label: string;
    emptyMessage?: string;
    items: TraceabilityRelatedItem[];
}

interface TraceabilityPreviewModalProps extends ModalProps {
    title: string;
    code: string;
    name?: string;
    description?: string;
    relatedGroups: TraceabilityRelatedGroup[];
}

export default function TraceabilityPreviewModal({
    open, onClose, title, code, name, description, relatedGroups
}: TraceabilityPreviewModalProps) {
    const handleGoTo = (item: TraceabilityRelatedItem) => {
        item.onGoTo?.();
        onClose();
    };

    return (
        <ModalContainer open={open} size="normal">
            <ModalHeader title={title} onClose={onClose} />

            <div style={{ display: 'flex', flexDirection: 'column', gap: '6px', marginBottom: '20px' }}>
                <div style={{ display: 'flex', alignItems: 'flex-start', gap: '8px' }}>
                    <span style={{ color: 'var(--color-yellow)', fontWeight: 600, fontSize: '14px', whiteSpace: 'nowrap', marginTop: '1px' }}>{code}</span>
                    {name && <strong style={{ color: '#ffffff', fontSize: '15px' }}>{name}</strong>}
                </div>
                {description && (
                    <p style={{ margin: 0, fontSize: '14px', color: 'var(--color-muted-text)', lineHeight: '1.5' }}>{description}</p>
                )}
            </div>

            {relatedGroups.map(group => (
                <div key={group.label} style={{ marginBottom: '16px' }}>
                    <h4 style={{ margin: '0 0 8px', fontSize: '13px', color: 'var(--color-muted-text)', textTransform: 'uppercase', letterSpacing: '0.05em' }}>
                        {group.label}
                    </h4>
                    {group.items.length === 0 ? (
                        <div style={{ color: 'var(--color-muted-text)', fontSize: '13px' }}>{group.emptyMessage ?? 'None linked.'}</div>
                    ) : (
                        <div style={{ display: 'flex', flexDirection: 'column', gap: '8px' }}>
                            {group.items.map(item => (
                                <div
                                    key={item.id}
                                    style={{
                                        display: 'flex',
                                        justifyContent: 'space-between',
                                        alignItems: 'center',
                                        padding: '10px 12px',
                                        backgroundColor: 'var(--color-dark-gray)',
                                        border: '1px solid var(--color-gray)',
                                        borderRadius: '8px',
                                        gap: '12px'
                                    }}
                                >
                                    <div style={{ flex: 1, minWidth: 0 }}>
                                        <span style={{ color: 'var(--color-yellow)', fontWeight: 600, fontSize: '13px' }}>{item.code}</span>{' '}
                                        <span style={{ color: '#ffffff', fontSize: '14px' }}>{item.name}</span>
                                    </div>
                                    {item.onGoTo && (
                                        <button
                                            onClick={() => handleGoTo(item)}
                                            style={{
                                                display: 'flex',
                                                alignItems: 'center',
                                                gap: '6px',
                                                background: 'none',
                                                border: '1px solid var(--color-yellow)',
                                                color: 'var(--color-yellow)',
                                                borderRadius: '6px',
                                                padding: '4px 10px',
                                                fontSize: '12px',
                                                cursor: 'pointer',
                                                whiteSpace: 'nowrap'
                                            }}
                                            title={`Go to ${item.name}`}
                                        >
                                            <BiLinkExternal size={14} /> Go to
                                        </button>
                                    )}
                                </div>
                            ))}
                        </div>
                    )}
                </div>
            ))}
        </ModalContainer>
    );
}

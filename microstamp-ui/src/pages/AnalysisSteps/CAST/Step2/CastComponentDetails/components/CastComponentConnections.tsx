
import { BiRightArrowAlt, BiTrash } from 'react-icons/bi';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { toast } from 'sonner';
import { getComponentDependencies } from '@http/Step2/Components';
import { deleteConnection } from '@http/Step2/Connections';

import CastSection from '@components/CastSection';
import { useCastConfirm } from '@components/CastConfirm';

interface Props { componentId: string; analysisId: string; }

export default function CastComponentConnections({ componentId }: Props) {
    const queryClient = useQueryClient();
    const { confirmDelete, confirmDialog } = useCastConfirm();

    const { data: deps, isLoading } = useQuery({
        queryKey: ['component-dependencies', componentId],
        queryFn: () => getComponentDependencies(componentId)
    });

    const { mutateAsync: remove } = useMutation({
        mutationFn: (id: string) => deleteConnection(id),
        onSuccess: () => {
            toast.success("Connection removed.");
            queryClient.invalidateQueries({ queryKey: ['component-dependencies', componentId] });
            queryClient.invalidateQueries({ queryKey: ['analysis-connections'] });
        },
        onError: (err: any) => toast.error(err.message || "Error deleting Connection.")
    });

    return (
        <CastSection title="Connections" tooltipInfo="Connections where this component is the source or target." hideAddButton={true}>
            {isLoading ? (
                <div style={{ color: 'var(--color-muted-text)', padding: '10px' }}>Loading interfaces...</div>
            ) : (
                <div style={{ display: 'flex', flexDirection: 'column', gap: '8px' }}>
                    {deps?.connections.map((conn: any) => {
                        const isSource = conn.source.id === componentId;

                        return (
                            <div key={conn.id} style={{ display: 'flex', alignItems: 'center', backgroundColor: 'var(--color-dark-gray)', padding: '12px 16px', borderRadius: '8px', border: '1px solid var(--color-gray)' }}>
                                <div style={{ flex: 1, display: 'flex', alignItems: 'center', gap: '16px' }}>
                                    <span style={{ color: isSource ? '#fb923c' : '#60a5fa', fontWeight: 'bold', fontSize: '12px' }}>
                                        {isSource ? 'OUT' : 'IN'}
                                    </span>

                                    <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
                                        <span style={{ color: '#fff', fontSize: '15px', fontWeight: 'bold' }}>{conn.source.name}</span>
                                        <BiRightArrowAlt color="var(--color-muted-text)" />
                                        <span style={{ color: '#fff', fontSize: '15px', fontWeight: 'bold' }}>{conn.target.name}</span>
                                    </div>
                                    
                                    <span style={{ fontSize: '12px', color: 'var(--color-muted-text)' }}>
                                        ({(conn.interactions ?? []).map((i: any) => i.name).join(', ') || 'Interaction'})
                                    </span>
                                </div>
                                <button onClick={async () => { if (await confirmDelete(`connection ${conn.code}`)) remove(conn.id); }} style={{ background: 'none', border: 'none', color: '#ef4444', cursor: 'pointer' }} title="Delete connection">
                                    <BiTrash />
                                </button>
                            </div>
                        );
                    })}
                    {!deps?.connections.length && <p style={{ color: 'var(--color-muted-text)', fontSize: '14px' }}>No interfaces defined for this component.</p>}
                </div>
            )}
            {confirmDialog}
        </CastSection>
    );
}

import { BiRightArrowAlt, BiTrash } from 'react-icons/bi';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { toast } from 'sonner';
import { getComponentDependencies } from '@http/Step2/Components';
import { deleteConnection } from '@http/Step2/Connections';

import CastSection from '@components/CastSection';

interface Props { componentId: string; analysisId: string; }

export default function CastComponentConnections({ componentId }: Props) {
    const queryClient = useQueryClient();

    const { data: deps, isLoading } = useQuery({
        queryKey: ['component-dependencies', componentId],
        queryFn: () => getComponentDependencies(componentId)
    });

    const { mutateAsync: remove } = useMutation({
        mutationFn: (id: string) => deleteConnection(id),
        onSuccess: () => {
            toast.success("Connection removed.");
            queryClient.invalidateQueries({ queryKey: ['component-dependencies', componentId] });
        }
    });

    return (
        <CastSection title="Connections" tooltipInfo="Connections where this component is the source or target." hideAddButton={true}>
            {isLoading ? (
                <div style={{ color: '#9ca3af', padding: '10px' }}>Loading interfaces...</div>
            ) : (
                <div style={{ display: 'flex', flexDirection: 'column', gap: '8px' }}>
                    {deps?.connections.map((conn: any) => {
                        const interaction = conn.interactions?.[0];
                        const isSource = conn.source.id === componentId;

                        return (
                            <div key={conn.id} style={{ display: 'flex', alignItems: 'center', backgroundColor: '#374151', padding: '12px 16px', borderRadius: '8px', border: '1px solid #4b5563' }}>
                                <div style={{ flex: 1, display: 'flex', alignItems: 'center', gap: '16px' }}>
                                    <span style={{ color: isSource ? '#fb923c' : '#60a5fa', fontWeight: 'bold' }}>
                                        {isSource ? 'OUT' : 'IN'}
                                    </span>
                                    
                                    <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
                                        <span>{conn.source.name}</span>
                                        <BiRightArrowAlt color="#9ca3af" />
                                        <span>{conn.target.name}</span>
                                    </div>
                                    
                                    <span style={{ fontSize: '12px', color: '#9ca3af' }}>
                                        ({interaction?.name || 'Interaction'})
                                    </span>
                                </div>
                                <button onClick={() => remove(conn.id)} style={{ background: 'none', border: 'none', color: '#ef4444', cursor: 'pointer' }}>
                                    <BiTrash />
                                </button>
                            </div>
                        );
                    })}
                    {!deps?.connections.length && <p style={{ color: '#6b7280', fontSize: '14px' }}>No interfaces defined for this component.</p>}
                </div>
            )}
        </CastSection>
    );
}
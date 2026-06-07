import { useState } from 'react';
import { BiSave, BiTrash, BiTransferAlt, BiRightArrowAlt } from 'react-icons/bi';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { toast } from 'sonner';
import { getComponents } from '@http/Step2/Components';
import { getConnections, createConnection, deleteConnection } from '@http/Step2/Connections';
import { createInteraction } from '@http/Step2/Interactions';
import { IInteractionType } from '@interfaces/IStep2';

import CastSection from '@components/CastSection';
import styles from '../../Step1/CastStepOne.module.css';

interface Props { analysisId: string; }

export default function CastConnectionsForm({ analysisId }: Props) {
    const queryClient = useQueryClient();

    const { data: availableComponents, isLoading: loadingComps } = useQuery({
        queryKey: ['analysis-components', analysisId],
        queryFn: () => getComponents(analysisId)
    });

    const { data: connections, isLoading: loadingConns } = useQuery({
        queryKey: ['analysis-connections', analysisId],
        queryFn: () => getConnections(analysisId)
    });

    const [form, setForm] = useState({ 
        code: '', 
        sourceId: '', 
        targetId: '', 
        type: IInteractionType.CONTROL_ACTION 
    });

    const { mutateAsync: save, isPending } = useMutation({
        mutationFn: async () => {
            const newConnection = await createConnection({
                code: form.code,
                style: "SOLID" as any,
                sourceId: form.sourceId,
                targetId: form.targetId,
                analysisId: analysisId
            });

            await createInteraction({
                name: form.code, 
                code: form.code,
                interactionType: form.type,
                connectionId: newConnection.id
            });

            return newConnection;
        },
        onSuccess: () => {
            toast.success("Connection and Interaction added!");
            setForm({ code: '', sourceId: '', targetId: '', type: IInteractionType.CONTROL_ACTION });
            queryClient.invalidateQueries({ queryKey: ['analysis-connections', analysisId] });
        },
        onError: (err: any) => toast.error(err.message || "Error saving Connection.")
    });

    const { mutateAsync: remove } = useMutation({
        mutationFn: async (id: string) => {
            if (window.confirm("Are you sure you want to delete this connection?")) {
                return await deleteConnection(id);
            }
            return Promise.reject(new Error("Cancelled"));
        },
        onSuccess: () => {
            toast.success("Connection removed.");
            queryClient.invalidateQueries({ queryKey: ['analysis-connections', analysisId] });
        },
        onError: (err: any) => {
            if (err.message !== "Cancelled") toast.error(err.message);
        }
    });

    const getComponentName = (id: string) => {
        if (!availableComponents) return 'Unknown';
        const comp = availableComponents.find((c: any) => c.id === id);
        return comp ? comp.name : 'Unknown';
    };

    return (
        <CastSection title="Connections & Interactions" tooltipInfo="Define how components interact through control actions and feedback." hideAddButton={true}>
            {loadingComps || loadingConns ? (
                <div style={{ color: '#9ca3af', padding: '10px' }}>Loading data...</div>
            ) : (
                <div style={{ display: 'flex', flexDirection: 'column', gap: '24px' }}>
                    
                    {connections && connections.length > 0 && (
                        <div style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
                            {connections.map((conn: any) => {
                                const interactions = conn.interactions || [];
                                const interaction = interactions.length > 0 ? interactions[0] : null;
                                
                                const isFeedback = interaction?.interactionType === 'FEEDBACK';
                                const displayCode = interaction ? interaction.code : conn.code;
                                const displayType = interaction ? interaction.interactionType.replace('_', ' ') : 'CONNECTION';

                                return (
                                    <div key={conn.id} style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', backgroundColor: '#374151', padding: '16px', borderRadius: '8px', border: '1px solid #4b5563' }}>
                                        
                                        <div style={{ display: 'flex', alignItems: 'center', gap: '16px', flex: 1 }}>
                                            <div style={{ backgroundColor: '#1f2937', color: '#fff', padding: '8px 16px', borderRadius: '4px', border: '1px solid #4b5563', flex: 1, textAlign: 'center', fontWeight: 'bold' }}>
                                                {getComponentName(conn.source.id || conn.sourceId)}
                                            </div>
                                            <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', color: isFeedback ? '#60a5fa' : '#f97316', width: '120px' }}>
                                                <span style={{ fontSize: '10px', fontWeight: 'bold', letterSpacing: '1px' }}>{displayCode}</span>
                                                <div style={{ display: 'flex', alignItems: 'center' }}>
                                                    {isFeedback && <BiRightArrowAlt size={24} style={{ transform: 'rotate(180deg)' }} />}
                                                    <div style={{ height: '2px', width: '60px', backgroundColor: isFeedback ? '#60a5fa' : '#f97316' }}></div>
                                                    {!isFeedback && <BiRightArrowAlt size={24} />}
                                                </div>
                                                <span style={{ fontSize: '10px', color: '#9ca3af' }}>{displayType}</span>
                                            </div>

2                                            <div style={{ backgroundColor: '#1f2937', color: '#fff', padding: '8px 16px', borderRadius: '4px', border: '1px solid #4b5563', flex: 1, textAlign: 'center', fontWeight: 'bold' }}>
                                                {getComponentName(conn.target.id || conn.targetId)}
                                            </div>
                                        </div>

                                        <button onClick={() => remove(conn.id)} style={{ background: 'transparent', border: 'none', color: '#ef4444', cursor: 'pointer', marginLeft: '16px' }} title="Delete Connection">
                                            <BiTrash size={18} />
                                        </button>
                                    </div>
                                );
                            })}
                        </div>
                    )}

                    <div style={{ backgroundColor: '#1f2937', padding: '16px', borderRadius: '8px', border: '1px dashed #4b5563', display: 'flex', flexDirection: 'column', gap: '16px' }}>
                        <div style={{ display: 'flex', alignItems: 'center', gap: '8px', color: '#d1d5db' }}>
                            <BiTransferAlt size={20} />
                            <h4 style={{ margin: 0, fontSize: '14px' }}>Add New Interaction</h4>
                        </div>
                        
                        <div style={{ display: 'grid', gridTemplateColumns: '80px 1fr 1fr 1fr', gap: '16px', alignItems: 'end' }}>
                            <div>
                                <label className={styles.inputLabel}>Code</label>
                                <input type="text" placeholder="CA-1" className={styles.inputField} value={form.code} onChange={e => setForm({ ...form, code: e.target.value })} style={{ marginBottom: 0 }} />
                            </div>
                            <div>
                                <label className={styles.inputLabel}>Source</label>
                                <select className={styles.inputField} value={form.sourceId} onChange={e => setForm({ ...form, sourceId: e.target.value })} style={{ marginBottom: 0 }}>
                                    <option value="" disabled>Select Source...</option>
                                    {availableComponents?.map((c: any) => <option key={c.id} value={c.id}>{c.name}</option>)}
                                </select>
                            </div>
                            <div>
                                <label className={styles.inputLabel}>Type</label>
                                <select className={styles.inputField} value={form.type} onChange={e => setForm({ ...form, type: e.target.value as IInteractionType })} style={{ marginBottom: 0 }}>
                                    <option value={IInteractionType.CONTROL_ACTION}>Control Action (➔)</option>
                                    <option value={IInteractionType.FEEDBACK}>Feedback (🡐)</option>
                                </select>
                            </div>
                            <div>
                                <label className={styles.inputLabel}>Target</label>
                                <select className={styles.inputField} value={form.targetId} onChange={e => setForm({ ...form, targetId: e.target.value })} style={{ marginBottom: 0 }}>
                                    <option value="" disabled>Select Target...</option>
                                    {availableComponents?.map((c: any) => <option key={c.id} value={c.id}>{c.name}</option>)}
                                </select>
                            </div>
                        </div>

                        <div style={{ display: 'flex', justifyContent: 'flex-end', marginTop: '8px' }}>
                            <button onClick={() => save()} disabled={isPending || !form.sourceId || !form.targetId || !form.code} className={styles.btnSaveForm}>
                                <BiSave size={18} /> {isPending ? 'Saving...' : 'Add Connection'}
                            </button>
                        </div>
                    </div>

                </div>
            )}
        </CastSection>
    );
}
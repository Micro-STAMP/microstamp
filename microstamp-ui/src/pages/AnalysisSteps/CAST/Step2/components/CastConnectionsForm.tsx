import { useState } from 'react';
import { BiSave, BiTrash, BiTransferAlt, BiRightArrowAlt, BiShow } from 'react-icons/bi';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { useNavigate } from 'react-router-dom';
import { toast } from 'sonner';
import { getComponents } from '@http/Step2/Components';
import { getConnections, createConnection, deleteConnection } from '@http/Step2/Connections';
import { createInteraction } from '@http/Step2/Interactions';
import { IInteractionType } from '@interfaces/IStep2';
import { SelectOption } from '@components/FormField/Templates';

import CastSection from '@components/CastSection';
import Select from '@components/FormField/Select';
import TraceabilityPreviewModal from '@components/Modal/ModalTraceability/TraceabilityPreviewModal';
import styles from '../../Step1/CastStepOne.module.css';

interface Props { analysisId: string; }

export default function CastConnectionsForm({ analysisId }: Props) {
    const queryClient = useQueryClient();
    const navigate = useNavigate();
    const [previewConnection, setPreviewConnection] = useState<any>(null);

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

    const getComponent = (id: string) => {
        return availableComponents?.find((c: any) => c.id === id) || null;
    };

    const getComponentName = (id: string) => {
        return getComponent(id)?.name || 'Unknown';
    };

    const componentSelectOptions: SelectOption[] = (availableComponents ?? []).map((c: any) => ({ label: c.name, value: c.id }));
    const interactionTypeOptions: SelectOption[] = [
        { label: 'Control Action (➔)', value: IInteractionType.CONTROL_ACTION },
        { label: 'Feedback (🡐)', value: IInteractionType.FEEDBACK }
    ];

    return (
        <>
        <CastSection title="Connections & Interactions" tooltipInfo="Define how components interact through control actions and feedback." hideAddButton={true}>
            {loadingComps || loadingConns ? (
                <div style={{ color: 'var(--color-muted-text)', padding: '10px' }}>Loading data...</div>
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
                                    <div key={conn.id} style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', backgroundColor: 'var(--color-dark-gray)', padding: '16px', borderRadius: '8px', border: '1px solid var(--color-gray)' }}>
                                        
                                        <div style={{ display: 'flex', alignItems: 'center', gap: '16px', flex: 1 }}>
                                            <div style={{ backgroundColor: 'var(--color-dark)', color: '#fff', padding: '8px 16px', borderRadius: '4px', border: '1px solid var(--color-gray)', flex: 1, textAlign: 'center', fontWeight: 'bold', fontSize: '15px' }}>
                                                {getComponentName(conn.source.id || conn.sourceId)}
                                            </div>
                                            <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', color: isFeedback ? '#60a5fa' : '#f97316', width: '120px' }}>
                                                <span style={{ fontSize: '10px', fontWeight: 'bold', letterSpacing: '1px' }}>{displayCode}</span>
                                                <div style={{ display: 'flex', alignItems: 'center' }}>
                                                    {isFeedback && <BiRightArrowAlt size={24} style={{ transform: 'rotate(180deg)' }} />}
                                                    <div style={{ height: '2px', width: '60px', backgroundColor: isFeedback ? '#60a5fa' : '#f97316' }}></div>
                                                    {!isFeedback && <BiRightArrowAlt size={24} />}
                                                </div>
                                                <span style={{ fontSize: '10px', color: 'var(--color-muted-text)' }}>{displayType}</span>
                                            </div>

                                            <div style={{ backgroundColor: 'var(--color-dark)', color: '#fff', padding: '8px 16px', borderRadius: '4px', border: '1px solid var(--color-gray)', flex: 1, textAlign: 'center', fontWeight: 'bold', fontSize: '15px' }}>
                                                {getComponentName(conn.target.id || conn.targetId)}
                                            </div>
                                        </div>

                                        <div style={{ display: 'flex', gap: '4px', marginLeft: '16px' }}>
                                            <button onClick={() => setPreviewConnection({ conn, displayCode, sourceId: conn.source.id || conn.sourceId, targetId: conn.target.id || conn.targetId })} style={{ background: 'transparent', border: 'none', color: 'var(--color-muted-text)', cursor: 'pointer' }} title="View traceability">
                                                <BiShow size={18} />
                                            </button>
                                            <button onClick={() => remove(conn.id)} style={{ background: 'transparent', border: 'none', color: '#ef4444', cursor: 'pointer' }} title="Delete Connection">
                                                <BiTrash size={18} />
                                            </button>
                                        </div>
                                    </div>
                                );
                            })}
                        </div>
                    )}

                    <div style={{ backgroundColor: 'var(--color-dark)', padding: '16px', borderRadius: '8px', border: '1px dashed var(--color-gray)', display: 'flex', flexDirection: 'column', gap: '16px' }}>
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
                                <Select
                                    label="Source"
                                    options={componentSelectOptions}
                                    value={componentSelectOptions.find(o => o.value === form.sourceId) || null}
                                    onChange={opt => setForm({ ...form, sourceId: opt?.value || '' })}
                                />
                            </div>
                            <div>
                                <Select
                                    label="Type"
                                    options={interactionTypeOptions}
                                    value={interactionTypeOptions.find(o => o.value === form.type) || null}
                                    onChange={opt => setForm({ ...form, type: (opt?.value as IInteractionType) ?? IInteractionType.CONTROL_ACTION })}
                                    required
                                />
                            </div>
                            <div>
                                <Select
                                    label="Target"
                                    options={componentSelectOptions}
                                    value={componentSelectOptions.find(o => o.value === form.targetId) || null}
                                    onChange={opt => setForm({ ...form, targetId: opt?.value || '' })}
                                />
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

        {previewConnection && (
            <TraceabilityPreviewModal
                open={!!previewConnection}
                onClose={() => setPreviewConnection(null)}
                title="Interaction Details"
                code={previewConnection.displayCode}
                relatedGroups={[
                    {
                        label: 'Linked Components',
                        emptyMessage: 'No components found.',
                        items: [
                            getComponent(previewConnection.sourceId) && {
                                id: previewConnection.sourceId,
                                code: getComponent(previewConnection.sourceId)?.code,
                                name: `${getComponent(previewConnection.sourceId)?.name} (Source)`,
                                onGoTo: () => navigate(`/analyses/${analysisId}/cast/step2/component/${previewConnection.sourceId}`)
                            },
                            getComponent(previewConnection.targetId) && {
                                id: previewConnection.targetId,
                                code: getComponent(previewConnection.targetId)?.code,
                                name: `${getComponent(previewConnection.targetId)?.name} (Target)`,
                                onGoTo: () => navigate(`/analyses/${analysisId}/cast/step2/component/${previewConnection.targetId}`)
                            }
                        ].filter(Boolean) as any
                    }
                ]}
            />
        )}
        </>
    );
}
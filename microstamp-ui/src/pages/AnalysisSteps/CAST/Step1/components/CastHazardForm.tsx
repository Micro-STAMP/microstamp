import { useState } from 'react';
import { BiSave, BiTrash } from 'react-icons/bi';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { toast } from 'sonner';
import { getByAnalysisId as getHazards, createHazard, deleteHazard } from '@http/CAST/Step1/Hazards';
import { getByAnalysisId as getLossEvents } from '@http/CAST/Step1/AccidentLossEvents';
import CastSection from '@components/CastSection';
import styles from '../CastStepOne.module.css';

interface Props { analysisId: string; }

export default function CastHazardForm({ analysisId }: Props) {
    const queryClient = useQueryClient();

    const { data: hazards, isLoading: isLoadingHazards } = useQuery({
        queryKey: ['hazards', analysisId],
        queryFn: () => getHazards(analysisId)
    });

    const { data: losses } = useQuery({
        queryKey: ['loss-events', analysisId],
        queryFn: () => getLossEvents(analysisId)
    });

    const [form, setForm] = useState({ 
        code: 'H-1', 
        name: '', 
        description: '', 
        accidentLossEventIds: [] as string[] 
    });

    const { mutateAsync: save, isPending } = useMutation({
        mutationFn: async () => {
            return await createHazard({
                code: form.code,
                name: form.name,
                description: form.description,
                accidentLossEventIds: form.accidentLossEventIds,
                analysisId
            });
        },
        onSuccess: () => {
            toast.success("Hazard added successfully!");
            setForm({ code: `H-${(hazards?.length || 0) + 2}`, name: '', description: '', accidentLossEventIds: [] });
            queryClient.invalidateQueries({ queryKey: ['hazards', analysisId] });
        },
        onError: (err: any) => toast.error(err.message || "Error saving Hazard.")
    });

    const { mutateAsync: remove } = useMutation({
        mutationFn: async (id: string) => await deleteHazard(id),
        onSuccess: () => {
            toast.success("Hazard removed.");
            queryClient.invalidateQueries({ queryKey: ['hazards', analysisId] });
        }
    });

    const handleLossToggle = (lossId: string) => {
        setForm(prev => ({
            ...prev,
            accidentLossEventIds: prev.accidentLossEventIds.includes(lossId)
                ? prev.accidentLossEventIds.filter(id => id !== lossId)
                : [...prev.accidentLossEventIds, lossId]
        }));
    };

    return (
        <CastSection title="Hazards Involved" tooltipInfo="What unsafe states materialized?" defaultOpen={false} hideAddButton={true}>
            {isLoadingHazards ? (
                <div style={{ color: '#9ca3af', fontSize: '14px', padding: '10px' }}>Loading hazards...</div>
            ) : (
                <div style={{ display: 'flex', flexDirection: 'column', gap: '20px' }}>
                    
                    {hazards && hazards.length > 0 ? (
                        <div style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
                            {hazards.map(h => (
                                <div 
                                    key={h.id} 
                                    style={{ 
                                        display: 'flex', 
                                        justifyContent: 'space-between', 
                                        alignItems: 'flex-start',
                                        padding: '16px', 
                                        backgroundColor: '#303642', 
                                        border: '1px solid #4b5563', 
                                        borderRadius: '8px',
                                        gap: '16px'
                                    }}
                                >
                                    <div style={{ flex: 1 }}>
                                        <div style={{ display: 'flex', alignItems: 'center', gap: '8px', marginBottom: '6px' }}>
                                            <span style={{ 
                                                color: 'var(--color-yellow)', 
                                                fontWeight: 600, 
                                                fontSize: '14px'
                                            }}>
                                                {h.code}
                                            </span>
                                            <strong style={{ color: '#ffffff', fontSize: '15px' }}>{h.name}</strong>
                                        </div>
                                        
                                        {h.description && (
                                            <p style={{ 
                                                margin: 0, 
                                                fontSize: '14px', 
                                                color: '#9ca3af', 
                                                lineHeight: '1.5' 
                                            }}>
                                                {h.description}
                                            </p>
                                        )}
                                    </div>
                                    
                                    <button 
                                        onClick={() => remove(h.id)} 
                                        style={{ 
                                            background: 'none', 
                                            border: 'none', 
                                            color: '#ef4444', 
                                            cursor: 'pointer',
                                            padding: '4px',
                                            display: 'flex',
                                            alignItems: 'center',
                                            justifyContent: 'center',
                                            transition: 'opacity 0.2s',
                                            marginTop: '-4px' 
                                        }}
                                        title="Remove Hazard"
                                    >
                                        <BiTrash size={18} />
                                    </button>
                                </div>
                            ))}
                        </div>
                    ) : (
                        <div className={styles.emptyState}>No hazards added yet.</div>
                    )}

                    <hr style={{ borderTop: '1px solid #e5e7eb', margin: '10px 0' }} />

                    <div style={{ display: 'flex', flexDirection: 'column', gap: '16px' }}>
                        <h4 style={{ margin: 0, fontSize: '14px', color: '#374151' }}>Add New Hazard</h4>
                        <div style={{ display: 'grid', gridTemplateColumns: '100px 1fr', gap: '16px' }}>
                            <div>
                                <label className={styles.inputLabel}>Code</label>
                                <input type="text" className={styles.inputField} value={form.code} onChange={e => setForm({ ...form, code: e.target.value })} style={{ marginBottom: 0 }} />
                            </div>
                            <div>
                                <label className={styles.inputLabel}>Hazard Name</label>
                                <input type="text" placeholder="E.g., High Pressure" className={styles.inputField} value={form.name} onChange={e => setForm({ ...form, name: e.target.value })} style={{ marginBottom: 0 }} />
                            </div>
                        </div>
                        <div>
                            <label className={styles.inputLabel}>Description</label>
                            <textarea rows={2} className={styles.inputField} value={form.description} onChange={e => setForm({ ...form, description: e.target.value })} style={{ marginBottom: 0 }} />
                        </div>

                        {losses && losses.length > 0 && (
                            <div>
                                <label className={styles.inputLabel}>Linked Accident / Loss Events</label>
                                <div style={{ display: 'flex', flexDirection: 'column', gap: '8px', marginTop: '4px' }}>
                                    {losses.map(loss => (
                                        <label key={loss.id} style={{ display: 'flex', alignItems: 'center', gap: '8px', fontSize: '14px', cursor: 'pointer' }}>
                                            <input 
                                                type="checkbox" 
                                                checked={form.accidentLossEventIds.includes(loss.id)}
                                                onChange={() => handleLossToggle(loss.id)}
                                            />
                                            {loss.code}: {loss.name}
                                        </label>
                                    ))}
                                </div>
                            </div>
                        )}

                        <div style={{ display: 'flex', justifyContent: 'flex-end' }}>
                            <button onClick={() => save()} disabled={isPending || !form.name} className={styles.btnSaveForm}>
                                <BiSave size={18} /> {isPending ? 'Adding...' : 'Add Hazard'}
                            </button>
                        </div>
                    </div>
                </div>
            )}
        </CastSection>
    );
}
import { useState } from 'react';
import { BiSave, BiTrash } from 'react-icons/bi';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { toast } from 'sonner';
import { getByAnalysisId as getConstraints, createConstraint, deleteConstraint } from '@http/CAST/Step1/Constraints';
import { getByAnalysisId as getHazards } from '@http/CAST/Step1/Hazards';
import CastSection from '@components/CastSection';
import styles from '../CastStepOne.module.css';

interface Props { analysisId: string; }

export default function ViolatedSystemSafetyConstraintForm({ analysisId }: Props) {
    const queryClient = useQueryClient();

    const { data: constraints, isLoading: isLoadingConstraints } = useQuery({
        queryKey: ['constraints', analysisId],
        queryFn: () => getConstraints(analysisId)
    });

    const { data: hazards } = useQuery({
        queryKey: ['hazards', analysisId],
        queryFn: () => getHazards(analysisId)
    });

    const [form, setForm] = useState({ 
        code: 'SC-1', 
        name: '', 
        description: '', 
        hazardIds: [] as string[] 
    });

    const { mutateAsync: save, isPending } = useMutation({
        mutationFn: async () => {
            return await createConstraint({
                code: form.code,
                name: form.name,
                description: form.description,
                hazardIds: form.hazardIds,
                analysisId
            });
        },
        onSuccess: () => {
            toast.success("Constraint added successfully!");
            setForm({ code: `SC-${(constraints?.length || 0) + 2}`, name: '', description: '', hazardIds: [] });
            queryClient.invalidateQueries({ queryKey: ['constraints', analysisId] });
        },
        onError: (err: any) => toast.error(err.message || "Error saving Constraint.")
    });

    const { mutateAsync: remove } = useMutation({
        mutationFn: async (id: string) => await deleteConstraint(id),
        onSuccess: () => {
            toast.success("Constraint removed.");
            queryClient.invalidateQueries({ queryKey: ['constraints', analysisId] });
        }
    });

    const handleHazardToggle = (hazardId: string) => {
        setForm(prev => ({
            ...prev,
            hazardIds: prev.hazardIds.includes(hazardId)
                ? prev.hazardIds.filter(id => id !== hazardId)
                : [...prev.hazardIds, hazardId]
        }));
    };

    return (
        <CastSection title="Violated System Safety Constraints" tooltipInfo="What rules were violated?" defaultOpen={false} hideAddButton={true}>
            {isLoadingConstraints ? (
                <div style={{ color: '#9ca3af', fontSize: '14px', padding: '10px' }}>Loading constraints...</div>
            ) : (
                <div style={{ display: 'flex', flexDirection: 'column', gap: '20px' }}>
                    
                    {constraints && constraints.length > 0 ? (
                        <div style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
                            {constraints.map(c => (
                                <div key={c.id} style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', padding: '16px', backgroundColor: '#303642', border: '1px solid #4b5563', borderRadius: '8px', gap: '16px' }}>
                                    <div style={{ flex: 1 }}>
                                        <div style={{ display: 'flex', alignItems: 'center', gap: '8px', marginBottom: '6px' }}>
                                            <span style={{ color: 'var(--color-yellow)', fontWeight: 600, fontSize: '14px' }}>
                                                {c.code}
                                            </span>
                                            <strong style={{ color: '#ffffff', fontSize: '15px' }}>{c.name}</strong>
                                        </div>
                                        {c.description && (
                                            <p style={{ margin: 0, fontSize: '14px', color: '#9ca3af', lineHeight: '1.5' }}>
                                                {c.description}
                                            </p>
                                        )}
                                    </div>
                                    <button onClick={() => remove(c.id)} style={{ background: 'none', border: 'none', color: '#ef4444', cursor: 'pointer', padding: '4px', display: 'flex', alignItems: 'center', justifyContent: 'center', transition: 'opacity 0.2s', marginTop: '-4px' }} title="Remove Constraint">
                                        <BiTrash size={18} />
                                    </button>
                                </div>
                            ))}
                        </div>
                    ) : (
                        <div className={styles.emptyState}>No constraints added yet.</div>
                    )}

                    <hr style={{ borderTop: '1px solid #e5e7eb', margin: '10px 0' }} />

                    <div style={{ display: 'flex', flexDirection: 'column', gap: '16px' }}>
                        <h4 style={{ margin: 0, fontSize: '14px', color: '#374151' }}>Add New Safety Constraint</h4>
                        <div style={{ display: 'grid', gridTemplateColumns: '100px 1fr', gap: '16px' }}>
                            <div>
                                <label className={styles.inputLabel}>Code</label>
                                <input type="text" className={styles.inputField} value={form.code} onChange={e => setForm({ ...form, code: e.target.value })} style={{ marginBottom: 0 }} />
                            </div>
                            <div>
                                <label className={styles.inputLabel}>Constraint Name</label>
                                <input type="text" placeholder="E.g., Medication must be given..." className={styles.inputField} value={form.name} onChange={e => setForm({ ...form, name: e.target.value })} style={{ marginBottom: 0 }} />
                            </div>
                        </div>
                        <div>
                            <label className={styles.inputLabel}>Description</label>
                            <textarea rows={2} className={styles.inputField} value={form.description} onChange={e => setForm({ ...form, description: e.target.value })} style={{ marginBottom: 0 }} />
                        </div>

                        {hazards && hazards.length > 0 && (
                            <div>
                                <label className={styles.inputLabel}>Linked Hazards</label>
                                <div style={{ display: 'flex', flexDirection: 'column', gap: '8px', marginTop: '4px' }}>
                                    {hazards.map(hazard => (
                                        <label key={hazard.id} style={{ display: 'flex', alignItems: 'center', gap: '8px', fontSize: '14px', cursor: 'pointer' }}>
                                            <input 
                                                type="checkbox" 
                                                checked={form.hazardIds.includes(hazard.id)}
                                                onChange={() => handleHazardToggle(hazard.id)}
                                            />
                                            {hazard.code}: {hazard.name}
                                        </label>
                                    ))}
                                </div>
                            </div>
                        )}

                        <div style={{ display: 'flex', justifyContent: 'flex-end' }}>
                            <button onClick={() => save()} disabled={isPending || !form.name} className={styles.btnSaveForm}>
                                <BiSave size={18} /> {isPending ? 'Adding...' : 'Add Constraint'}
                            </button>
                        </div>
                    </div>
                </div>
            )}
        </CastSection>
    );
}
import { useState } from 'react';
import { BiSave, BiTrash } from 'react-icons/bi';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { toast } from 'sonner';
import { getByAnalysisId as getPhysicalLosses, createPhysicalLoss, deletePhysicalLoss } from '@http/CAST/Step1/PhysicalLoss';
import CastSection from '@components/CastSection';
import styles from '../CastStepOne.module.css';

interface Props { analysisId: string; }

export default function PhysicalLossAnalysisForm({ analysisId }: Props) {
    const queryClient = useQueryClient();

    const { data: physicalLosses, isLoading } = useQuery({
        queryKey: ['physical-loss', analysisId],
        queryFn: () => getPhysicalLosses(analysisId)
    });

    const [form, setForm] = useState({ 
        code: 'PLA-1', 
        affectedEquipment: '',
        physicalLossDescription: '', 
        physicalDesignRequirements: '',
        physicalControls: '',
        failuresAndUnsafeInteractions: '',
        missingOrInadequateControls: '',
        contextualFactors: ''
    });

    const { mutateAsync: save, isPending } = useMutation({
        mutationFn: async () => {
            return await createPhysicalLoss({
                ...form,
                analysisId
            });
        },
        onSuccess: () => {
            toast.success("Physical Loss Analysis added!");
            setForm({ 
                code: `PLA-${(physicalLosses?.length || 0) + 2}`, 
                affectedEquipment: '', physicalLossDescription: '', physicalDesignRequirements: '', 
                physicalControls: '', failuresAndUnsafeInteractions: '', missingOrInadequateControls: '', contextualFactors: '' 
            });
            queryClient.invalidateQueries({ queryKey: ['physical-loss', analysisId] });
        },
        onError: (err: any) => toast.error(err.message || "Error saving Physical Loss Analysis.")
    });

    const { mutateAsync: remove } = useMutation({
        mutationFn: async (id: string) => await deletePhysicalLoss(id),
        onSuccess: () => {
            toast.success("Physical Loss Analysis removed.");
            queryClient.invalidateQueries({ queryKey: ['physical-loss', analysisId] });
        }
    });

    return (
        <CastSection title="Physical Loss Analysis" tooltipInfo="Analyze the physical components and controls involved in the loss." defaultOpen={false} hideAddButton={true}>
            {isLoading ? (
                <div style={{ color: '#9ca3af', fontSize: '14px', padding: '10px' }}>Loading analysis...</div>
            ) : (
                <div style={{ display: 'flex', flexDirection: 'column', gap: '24px' }}>
                    
                    {physicalLosses && physicalLosses.length > 0 ? (
                        <div style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
                            {physicalLosses.map(pla => (
                                <div key={pla.id} style={{ backgroundColor: '#303642', border: '1px solid #4b5563', borderRadius: '8px', padding: '16px', display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start' }}>
                                    <div style={{ flex: 1, display: 'flex', flexDirection: 'column', gap: '8px' }}>
                                        <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
                                            <span style={{ color: 'var(--color-yellow)', fontWeight: 600, fontSize: '14px' }}>{pla.code}</span>
                                            <strong style={{ color: '#ffffff', fontSize: '15px' }}>{pla.affectedEquipment}</strong>
                                        </div>
                                        {pla.physicalLossDescription && <p style={{ margin: 0, fontSize: '14px', color: '#9ca3af' }}><strong>Loss:</strong> {pla.physicalLossDescription}</p>}
                                        {pla.failuresAndUnsafeInteractions && <p style={{ margin: 0, fontSize: '14px', color: '#9ca3af' }}><strong>Failures:</strong> {pla.failuresAndUnsafeInteractions}</p>}
                                        {pla.missingOrInadequateControls && <p style={{ margin: 0, fontSize: '14px', color: '#9ca3af' }}><strong>Missing Controls:</strong> {pla.missingOrInadequateControls}</p>}
                                    </div>
                                    <button onClick={() => remove(pla.id)} style={{ background: 'none', border: 'none', color: '#ef4444', cursor: 'pointer', padding: '4px' }} title="Remove Analysis">
                                        <BiTrash size={18} />
                                    </button>
                                </div>
                            ))}
                        </div>
                    ) : (
                        <div className={styles.emptyState}>No physical loss analyses added yet.</div>
                    )}

                    <hr style={{ borderTop: '1px solid #e5e7eb', margin: '0' }} />

                    <div style={{ display: 'flex', flexDirection: 'column', gap: '16px' }}>
                        <h4 style={{ margin: 0, fontSize: '14px', color: '#374151' }}>Add New Physical Loss Analysis</h4>
                        
                        <div style={{ display: 'grid', gridTemplateColumns: '100px 1fr', gap: '16px' }}>
                            <div>
                                <label className={styles.inputLabel}>Code</label>
                                <input type="text" className={styles.inputField} value={form.code} onChange={e => setForm({ ...form, code: e.target.value })} style={{ marginBottom: 0 }} />
                            </div>
                            <div>
                                <label className={styles.inputLabel}>Affected Equipment / Component</label>
                                <input type="text" placeholder="E.g., Valve, Printer, Terminal..." className={styles.inputField} value={form.affectedEquipment} onChange={e => setForm({ ...form, affectedEquipment: e.target.value })} style={{ marginBottom: 0 }} />
                            </div>
                        </div>

                        <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '16px' }}>
                            <div>
                                <label className={styles.inputLabel}>Physical Loss Description</label>
                                <textarea rows={2} className={styles.inputField} value={form.physicalLossDescription} onChange={e => setForm({ ...form, physicalLossDescription: e.target.value })} style={{ marginBottom: 0 }} />
                            </div>
                            <div>
                                <label className={styles.inputLabel}>Physical Design Requirements</label>
                                <textarea rows={2} className={styles.inputField} value={form.physicalDesignRequirements} onChange={e => setForm({ ...form, physicalDesignRequirements: e.target.value })} style={{ marginBottom: 0 }} />
                            </div>
                            <div>
                                <label className={styles.inputLabel}>Physical Controls (Existing)</label>
                                <textarea rows={2} className={styles.inputField} value={form.physicalControls} onChange={e => setForm({ ...form, physicalControls: e.target.value })} style={{ marginBottom: 0 }} />
                            </div>
                            <div>
                                <label className={styles.inputLabel}>Failures & Unsafe Interactions</label>
                                <textarea rows={2} className={styles.inputField} value={form.failuresAndUnsafeInteractions} onChange={e => setForm({ ...form, failuresAndUnsafeInteractions: e.target.value })} style={{ marginBottom: 0 }} />
                            </div>
                        </div>

                        <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '16px' }}>
                            <div>
                                <label className={styles.inputLabel}>Missing or Inadequate Controls</label>
                                <textarea rows={2} className={styles.inputField} value={form.missingOrInadequateControls} onChange={e => setForm({ ...form, missingOrInadequateControls: e.target.value })} style={{ marginBottom: 0 }} />
                            </div>
                            <div>
                                <label className={styles.inputLabel}>Contextual Factors</label>
                                <textarea rows={2} className={styles.inputField} value={form.contextualFactors} onChange={e => setForm({ ...form, contextualFactors: e.target.value })} style={{ marginBottom: 0 }} />
                            </div>
                        </div>

                        <div style={{ display: 'flex', justifyContent: 'flex-end', marginTop: '8px' }}>
                            <button onClick={() => save()} disabled={isPending || !form.affectedEquipment} className={styles.btnSaveForm}>
                                <BiSave size={18} /> {isPending ? 'Adding...' : 'Add Physical Loss'}
                            </button>
                        </div>
                    </div>
                </div>
            )}
        </CastSection>
    );
}
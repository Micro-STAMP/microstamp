import { useState } from 'react';
import { BiSave, BiX } from 'react-icons/bi';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { toast } from 'sonner';
import { getByAnalysisId as getPhysicalLosses, createPhysicalLoss, updatePhysicalLoss, deletePhysicalLoss } from '@http/CAST/Step1/PhysicalLoss';
import { IPhysicalLossAnalysisReadDto } from '@interfaces/CAST/IPhysicalLossAnalysis';
import CastSection from '@components/CastSection';
import CastTextarea from '@components/CastTextarea';
import CastItemActions from '@components/CastItemActions';
import { useCastConfirm } from '@components/CastConfirm';
import { nextCode } from '@utils/castCodes';
import styles from '../CastStepOne.module.css';

interface Props { analysisId: string; }

const EMPTY_FORM = {
    code: '',
    affectedEquipment: '',
    physicalLossDescription: '',
    physicalDesignRequirements: '',
    physicalControls: '',
    failuresAndUnsafeInteractions: '',
    missingOrInadequateControls: '',
    contextualFactors: ''
};

export default function PhysicalLossAnalysisForm({ analysisId }: Props) {
    const queryClient = useQueryClient();
    const { confirmDelete, confirmDialog } = useCastConfirm();

    const { data: physicalLosses, isLoading } = useQuery({
        queryKey: ['physical-loss', analysisId],
        queryFn: () => getPhysicalLosses(analysisId)
    });

    const [form, setForm] = useState(EMPTY_FORM);
    const [editingId, setEditingId] = useState<string | null>(null);
    const code = form.code || nextCode('PLA', (physicalLosses ?? []).map(p => p.code));

    const resetForm = () => {
        setForm(EMPTY_FORM);
        setEditingId(null);
    };

    const { mutateAsync: save, isPending } = useMutation({
        mutationFn: async () => {
            const data = { ...form, code: code.trim() };
            return editingId ? await updatePhysicalLoss(editingId, data) : await createPhysicalLoss({ ...data, analysisId });
        },
        onSuccess: () => {
            toast.success(editingId ? "Physical Loss Analysis updated!" : "Physical Loss Analysis added!");
            resetForm();
            queryClient.invalidateQueries({ queryKey: ['physical-loss', analysisId] });
        },
        onError: (err: any) => toast.error(err.message || "Error saving Physical Loss Analysis.")
    });

    const { mutateAsync: remove } = useMutation({
        mutationFn: async (id: string) => await deletePhysicalLoss(id),
        onSuccess: (_, id) => {
            toast.success("Physical Loss Analysis removed.");
            if (id === editingId) resetForm();
            queryClient.invalidateQueries({ queryKey: ['physical-loss', analysisId] });
        },
        onError: (err: any) => toast.error(err.message || "Error deleting Physical Loss Analysis.")
    });

    const startEdit = (pla: IPhysicalLossAnalysisReadDto) => {
        setEditingId(pla.id);
        setForm({
            code: pla.code,
            affectedEquipment: pla.affectedEquipment || '',
            physicalLossDescription: pla.physicalLossDescription || '',
            physicalDesignRequirements: pla.physicalDesignRequirements || '',
            physicalControls: pla.physicalControls || '',
            failuresAndUnsafeInteractions: pla.failuresAndUnsafeInteractions || '',
            missingOrInadequateControls: pla.missingOrInadequateControls || '',
            contextualFactors: pla.contextualFactors || ''
        });
    };

    const handleDelete = async (pla: IPhysicalLossAnalysisReadDto) => {
        if (await confirmDelete(`Physical Loss Analysis ${pla.code}`)) await remove(pla.id);
    };

    return (
        <CastSection title="Physical Loss Analysis" tooltipInfo="Analyze the physical components and controls involved in the loss." defaultOpen={false} hideAddButton={true}>
            {isLoading ? (
                <div style={{ color: 'var(--color-muted-text)', fontSize: '14px', padding: '10px' }}>Loading analysis...</div>
            ) : (
                <div style={{ display: 'flex', flexDirection: 'column', gap: '24px' }}>
                    
                    {physicalLosses && physicalLosses.length > 0 ? (
                        <div style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
                            {physicalLosses.map(pla => (
                                <div key={pla.id} style={{ backgroundColor: 'var(--color-dark-gray)', border: `1px solid ${pla.id === editingId ? 'var(--color-yellow)' : 'var(--color-gray)'}`, borderRadius: '8px', padding: '16px', display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start' }}>
                                    <div style={{ flex: 1, display: 'flex', flexDirection: 'column', gap: '8px' }}>
                                        <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
                                            <span style={{ color: 'var(--color-yellow)', fontWeight: 600, fontSize: '14px' }}>{pla.code}</span>
                                            <strong style={{ color: '#ffffff', fontSize: '15px' }}>{pla.affectedEquipment}</strong>
                                        </div>
                                        {pla.physicalLossDescription && <p style={{ margin: 0, fontSize: '14px', color: 'var(--color-muted-text)' }}><strong>Loss:</strong> {pla.physicalLossDescription}</p>}
                                        {pla.failuresAndUnsafeInteractions && <p style={{ margin: 0, fontSize: '14px', color: 'var(--color-muted-text)' }}><strong>Failures:</strong> {pla.failuresAndUnsafeInteractions}</p>}
                                        {pla.missingOrInadequateControls && <p style={{ margin: 0, fontSize: '14px', color: 'var(--color-muted-text)' }}><strong>Missing Controls:</strong> {pla.missingOrInadequateControls}</p>}
                                    </div>
                                    <CastItemActions itemLabel="Physical Loss Analysis" onEdit={() => startEdit(pla)} onDelete={() => handleDelete(pla)} />
                                </div>
                            ))}
                        </div>
                    ) : (
                        <div className={styles.emptyState}>No physical loss analyses added yet.</div>
                    )}

                    <hr style={{ borderTop: '1px solid #e5e7eb', margin: '0' }} />

                    <div style={{ display: 'flex', flexDirection: 'column', gap: '16px' }}>
                        <h4 style={{ margin: 0, fontSize: '14px', color: 'var(--color-white)' }}>{editingId ? `Edit Physical Loss Analysis ${code}` : 'Add New Physical Loss Analysis'}</h4>
                        
                        <div style={{ display: 'grid', gridTemplateColumns: '100px 1fr', gap: '16px' }}>
                            <div>
                                <label className={styles.inputLabel}>Code</label>
                                <input type="text" className={styles.inputField} value={code} onChange={e => setForm({ ...form, code: e.target.value })} style={{ marginBottom: 0 }} />
                            </div>
                            <div>
                                <label className={styles.inputLabel}>Affected Equipment / Component</label>
                                <input type="text" placeholder="E.g., Valve, Printer, Terminal..." className={styles.inputField} value={form.affectedEquipment} onChange={e => setForm({ ...form, affectedEquipment: e.target.value })} style={{ marginBottom: 0 }} />
                            </div>
                        </div>

                        <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '16px' }}>
                            <div>
                                <label className={styles.inputLabel}>Physical Loss Description</label>
                                <CastTextarea rows={2} className={styles.inputField} value={form.physicalLossDescription} onChange={e => setForm({ ...form, physicalLossDescription: e.target.value })} style={{ marginBottom: 0 }} />
                            </div>
                            <div>
                                <label className={styles.inputLabel}>Physical Design Requirements</label>
                                <CastTextarea rows={4} className={styles.inputField} value={form.physicalDesignRequirements} onChange={e => setForm({ ...form, physicalDesignRequirements: e.target.value })} style={{ marginBottom: 0 }} />
                            </div>
                            <div>
                                <label className={styles.inputLabel}>Physical Controls (Existing)</label>
                                <CastTextarea rows={4} className={styles.inputField} value={form.physicalControls} onChange={e => setForm({ ...form, physicalControls: e.target.value })} style={{ marginBottom: 0 }} />
                            </div>
                            <div>
                                <label className={styles.inputLabel}>Failures & Unsafe Interactions</label>
                                <CastTextarea rows={4} className={styles.inputField} value={form.failuresAndUnsafeInteractions} onChange={e => setForm({ ...form, failuresAndUnsafeInteractions: e.target.value })} style={{ marginBottom: 0 }} />
                            </div>
                        </div>

                        <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '16px' }}>
                            <div>
                                <label className={styles.inputLabel}>Missing or Inadequate Controls</label>
                                <CastTextarea rows={4} className={styles.inputField} value={form.missingOrInadequateControls} onChange={e => setForm({ ...form, missingOrInadequateControls: e.target.value })} style={{ marginBottom: 0 }} />
                            </div>
                            <div>
                                <label className={styles.inputLabel}>Contextual Factors</label>
                                <CastTextarea rows={4} className={styles.inputField} value={form.contextualFactors} onChange={e => setForm({ ...form, contextualFactors: e.target.value })} style={{ marginBottom: 0 }} />
                            </div>
                        </div>

                        <div style={{ display: 'flex', justifyContent: 'flex-end', marginTop: '8px', gap: '8px' }}>
                            {editingId && (
                                <button onClick={resetForm} className={styles.btnBack} type="button">
                                    <BiX size={18} /> Cancel
                                </button>
                            )}
                            <button onClick={() => save()} disabled={isPending || !form.affectedEquipment.trim() || !code.trim()} className={styles.btnSaveForm}>
                                <BiSave size={18} /> {isPending ? 'Saving...' : editingId ? 'Save Changes' : 'Add Physical Loss'}
                            </button>
                        </div>
                    </div>
                </div>
            )}
            {confirmDialog}
        </CastSection>
    );
}
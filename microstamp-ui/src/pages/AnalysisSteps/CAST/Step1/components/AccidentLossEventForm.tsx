import { useState } from 'react';
import { BiSave, BiX } from 'react-icons/bi';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { toast } from 'sonner';
import { getByAnalysisId, createLossEvent, updateLossEvent, deleteLossEvent } from '@http/CAST/Step1/AccidentLossEvents';
import { IAccidentLossEventReadDto } from '@interfaces/CAST/IAccidentLossEvent';
import CastSection from '@components/CastSection';
import CastTextarea from '@components/CastTextarea';
import CastItemActions from '@components/CastItemActions';
import { useCastConfirm } from '@components/CastConfirm';
import { useRegisterCastSection } from '@components/CastSection/CastSectionNavigator';
import { nextCode } from '@utils/castCodes';
import styles from '../CastStepOne.module.css';

interface Props { analysisId: string; }

const EMPTY_FORM = { code: '', name: '', description: '' };

export default function AccidentLossEventForm({ analysisId }: Props) {
    const queryClient = useQueryClient();
    const registerLossSection = useRegisterCastSection('loss-events');
    const { confirmDelete, confirmDialog } = useCastConfirm();

    const { data: losses, isLoading } = useQuery({
        queryKey: ['loss-events', analysisId],
        queryFn: () => getByAnalysisId(analysisId)
    });

    const [form, setForm] = useState(EMPTY_FORM);
    const [editingId, setEditingId] = useState<string | null>(null);
    const code = form.code || nextCode('L', (losses ?? []).map(l => l.code));

    const resetForm = () => {
        setForm(EMPTY_FORM);
        setEditingId(null);
    };

    const invalidate = () => {
        queryClient.invalidateQueries({ queryKey: ['loss-events', analysisId] });
        queryClient.invalidateQueries({ queryKey: ['hazards', analysisId] });
    };

    const { mutateAsync: save, isPending } = useMutation({
        mutationFn: async () => {
            const data = { code: code.trim(), name: form.name.trim(), description: form.description };
            return editingId
                ? await updateLossEvent(editingId, data)
                : await createLossEvent({ ...data, analysisId });
        },
        onSuccess: () => {
            toast.success(editingId ? "System Loss updated!" : "System Loss added!");
            resetForm();
            invalidate();
        },
        onError: (err: any) => toast.error(err.message || "Error saving System Loss.")
    });

    const { mutateAsync: remove } = useMutation({
        mutationFn: (id: string) => deleteLossEvent(id),
        onSuccess: (_, id) => {
            toast.success("System Loss removed.");
            if (id === editingId) resetForm();
            invalidate();
        },
        onError: (err: any) => toast.error(err.message || "Error deleting System Loss.")
    });

    const startEdit = (loss: IAccidentLossEventReadDto) => {
        setEditingId(loss.id);
        setForm({ code: loss.code, name: loss.name, description: loss.description || '' });
    };

    const handleDelete = async (loss: IAccidentLossEventReadDto) => {
        if (await confirmDelete(`System Loss ${loss.code}`, 'Hazards linked to it will lose this link.')) await remove(loss.id);
    };

    return (
        <CastSection ref={registerLossSection} title="System Losses" tooltipInfo="What losses occurred? List every loss (L-1, L-2, ...) the accident caused — e.g., loss of life or injury, damage to equipment or the environment, loss of mission." hideAddButton={true}>
            {isLoading ? (
                <div style={{ color: 'var(--color-muted-text)', fontSize: '14px', padding: '10px' }}>Loading data...</div>
            ) : (
                <div style={{ display: 'flex', flexDirection: 'column', gap: '20px' }}>
                    {losses && losses.length > 0 ? (
                        <div style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
                            {losses.map(loss => (
                                <div key={loss.id} style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', padding: '16px', backgroundColor: 'var(--color-dark-gray)', border: `1px solid ${loss.id === editingId ? 'var(--color-yellow)' : 'var(--color-gray)'}`, borderRadius: '8px', gap: '16px' }}>
                                    <div style={{ flex: 1 }}>
                                        <div style={{ display: 'flex', alignItems: 'center', gap: '8px', marginBottom: '6px' }}>
                                            <span style={{ color: 'var(--color-yellow)', fontWeight: 600, fontSize: '14px' }}>{loss.code}</span>
                                            <strong style={{ color: '#ffffff', fontSize: '15px' }}>{loss.name}</strong>
                                        </div>
                                        {loss.description && (
                                            <p style={{ margin: 0, fontSize: '14px', color: 'var(--color-muted-text)', lineHeight: '1.5', whiteSpace: 'pre-wrap' }}>{loss.description}</p>
                                        )}
                                    </div>
                                    <CastItemActions itemLabel="System Loss" onEdit={() => startEdit(loss)} onDelete={() => handleDelete(loss)} />
                                </div>
                            ))}
                        </div>
                    ) : (
                        <div className={styles.emptyState}>No system losses added yet.</div>
                    )}

                    <hr style={{ borderTop: '1px solid #e5e7eb', margin: '10px 0' }} />

                    <div style={{ display: 'flex', flexDirection: 'column', gap: '16px' }}>
                        <h4 style={{ margin: 0, fontSize: '14px', color: 'var(--color-white)' }}>
                            {editingId ? `Edit System Loss ${code}` : 'Add New System Loss'}
                        </h4>
                        <div style={{ display: 'grid', gridTemplateColumns: '100px 1fr', gap: '16px' }}>
                            <div>
                                <label className={styles.inputLabel}>Code</label>
                                <input type="text" className={styles.inputField} value={code} onChange={e => setForm({ ...form, code: e.target.value })} style={{ marginBottom: 0 }} />
                            </div>
                            <div>
                                <label className={styles.inputLabel}>Short Title</label>
                                <input type="text" placeholder="E.g., Loss of life or serious injury to patients" className={styles.inputField} value={form.name} onChange={e => setForm({ ...form, name: e.target.value })} style={{ marginBottom: 0 }} />
                            </div>
                        </div>
                        <div>
                            <label className={styles.inputLabel}>Detailed Description</label>
                            <CastTextarea rows={3} placeholder="Detailed description..." className={styles.inputField} value={form.description} onChange={e => setForm({ ...form, description: e.target.value })} style={{ marginBottom: 0 }} />
                        </div>
                        <div style={{ display: 'flex', justifyContent: 'flex-end', gap: '8px' }}>
                            {editingId && (
                                <button onClick={resetForm} className={styles.btnBack} type="button">
                                    <BiX size={18} /> Cancel
                                </button>
                            )}
                            <button onClick={() => save()} disabled={isPending || !form.name.trim() || !code.trim()} className={styles.btnSaveForm}>
                                <BiSave size={18} /> {isPending ? 'Saving...' : editingId ? 'Save Changes' : 'Add System Loss'}
                            </button>
                        </div>
                    </div>
                </div>
            )}
            {confirmDialog}
        </CastSection>
    );
}

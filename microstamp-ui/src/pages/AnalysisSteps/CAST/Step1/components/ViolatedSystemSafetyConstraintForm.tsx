import { useState } from 'react';
import { BiSave, BiX } from 'react-icons/bi';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { toast } from 'sonner';
import { getByAnalysisId as getConstraints, createConstraint, updateConstraint, deleteConstraint } from '@http/CAST/Step1/Constraints';
import { getByAnalysisId as getHazards } from '@http/CAST/Step1/Hazards';
import CastSection from '@components/CastSection';
import CastCheckbox from '@components/CastCheckbox';
import LinkBadge from '@components/LinkBadge';
import CastTextarea from '@components/CastTextarea';
import CastItemActions from '@components/CastItemActions';
import { useCastConfirm } from '@components/CastConfirm';
import { nextCode } from '@utils/castCodes';
import { useGoToCastSection, useRegisterCastSection } from '@components/CastSection/CastSectionNavigator';
import TraceabilityPreviewModal from '@components/Modal/ModalTraceability/TraceabilityPreviewModal';
import { IViolatedSystemSafetyConstraintReadDto } from '@interfaces/CAST/IViolatedSystemSafetyConstraint';
import styles from '../CastStepOne.module.css';

interface Props { analysisId: string; }

export default function ViolatedSystemSafetyConstraintForm({ analysisId }: Props) {
    const queryClient = useQueryClient();
    const goToSection = useGoToCastSection();
    const registerConstraintsSection = useRegisterCastSection('constraints');
    const [previewConstraint, setPreviewConstraint] = useState<IViolatedSystemSafetyConstraintReadDto | null>(null);
    const { confirmDelete, confirmDialog } = useCastConfirm();

    const { data: constraints, isLoading: isLoadingConstraints } = useQuery({
        queryKey: ['constraints', analysisId],
        queryFn: () => getConstraints(analysisId)
    });

    const { data: hazards } = useQuery({
        queryKey: ['hazards', analysisId],
        queryFn: () => getHazards(analysisId)
    });

    const EMPTY_FORM = { code: '', name: '', description: '', hazardIds: [] as string[] };
    const [form, setForm] = useState(EMPTY_FORM);
    const [editingId, setEditingId] = useState<string | null>(null);
    const code = form.code || nextCode('SC', (constraints ?? []).map(c => c.code));

    const resetForm = () => {
        setForm(EMPTY_FORM);
        setEditingId(null);
    };

    const invalidate = () => {
        queryClient.invalidateQueries({ queryKey: ['constraints', analysisId] });
        queryClient.invalidateQueries({ queryKey: ['violated-ssc-select-options', analysisId] });
    };

    const { mutateAsync: save, isPending } = useMutation({
        mutationFn: async () => {
            const data = {
                code: code.trim(),
                name: form.name.trim(),
                description: form.description,
                hazardIds: form.hazardIds
            };
            return editingId ? await updateConstraint(editingId, data) : await createConstraint({ ...data, analysisId });
        },
        onSuccess: () => {
            toast.success(editingId ? "Constraint updated!" : "Constraint added successfully!");
            resetForm();
            invalidate();
        },
        onError: (err: any) => toast.error(err.message || "Error saving Constraint.")
    });

    const { mutateAsync: remove } = useMutation({
        mutationFn: async (id: string) => await deleteConstraint(id),
        onSuccess: (_, id) => {
            toast.success("Constraint removed.");
            if (id === editingId) resetForm();
            invalidate();
        },
        onError: (err: any) => toast.error(err.message || "Error deleting Constraint.")
    });

    const startEdit = (constraint: IViolatedSystemSafetyConstraintReadDto) => {
        setEditingId(constraint.id);
        setForm({
            code: constraint.code,
            name: constraint.name,
            description: constraint.description || '',
            hazardIds: (constraint.hazards ?? []).map(h => h.id)
        });
    };

    const handleDelete = async (constraint: IViolatedSystemSafetyConstraintReadDto) => {
        if (await confirmDelete(`Safety Constraint ${constraint.code}`)) await remove(constraint.id);
    };

    const handleHazardToggle = (hazardId: string) => {
        setForm(prev => ({
            ...prev,
            hazardIds: prev.hazardIds.includes(hazardId)
                ? prev.hazardIds.filter(id => id !== hazardId)
                : [...prev.hazardIds, hazardId]
        }));
    };

    return (
        <>
        <CastSection ref={registerConstraintsSection} title="Violated System Safety Constraints" tooltipInfo="What rules were violated?" defaultOpen={false} hideAddButton={true}>
            {isLoadingConstraints ? (
                <div style={{ color: 'var(--color-muted-text)', fontSize: '14px', padding: '10px' }}>Loading constraints...</div>
            ) : (
                <div style={{ display: 'flex', flexDirection: 'column', gap: '20px' }}>
                    
                    {constraints && constraints.length > 0 ? (
                        <div style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
                            {constraints.map(c => (
                                <div key={c.id} style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', padding: '16px', backgroundColor: 'var(--color-dark-gray)', border: `1px solid ${c.id === editingId ? 'var(--color-yellow)' : 'var(--color-gray)'}`, borderRadius: '8px', gap: '16px' }}>
                                    <div style={{ flex: 1 }}>
                                        <div style={{ display: 'flex', alignItems: 'center', gap: '8px', marginBottom: '6px', flexWrap: 'wrap' }}>
                                            <span style={{ color: 'var(--color-yellow)', fontWeight: 600, fontSize: '14px' }}>
                                                {c.code}
                                            </span>
                                            <strong style={{ color: '#ffffff', fontSize: '15px' }}>{c.name}</strong>
                                            {(c.hazards ?? []).map(hazard => (
                                                <LinkBadge key={hazard.id} code={hazard.code} tooltip={hazard.name} />
                                            ))}
                                        </div>
                                        {c.description && (
                                            <p style={{ margin: 0, fontSize: '14px', color: 'var(--color-muted-text)', lineHeight: '1.5' }}>
                                                {c.description}
                                            </p>
                                        )}
                                    </div>
                                    <CastItemActions itemLabel="Constraint" onPreview={() => setPreviewConstraint(c)} onEdit={() => startEdit(c)} onDelete={() => handleDelete(c)} />
                                </div>
                            ))}
                        </div>
                    ) : (
                        <div className={styles.emptyState}>No constraints added yet.</div>
                    )}

                    <hr style={{ borderTop: '1px solid #e5e7eb', margin: '10px 0' }} />

                    <div style={{ display: 'flex', flexDirection: 'column', gap: '16px' }}>
                        <h4 style={{ margin: 0, fontSize: '14px', color: 'var(--color-white)' }}>{editingId ? `Edit Safety Constraint ${code}` : 'Add New Safety Constraint'}</h4>
                        <div style={{ display: 'grid', gridTemplateColumns: '100px 1fr', gap: '16px' }}>
                            <div>
                                <label className={styles.inputLabel}>Code</label>
                                <input type="text" className={styles.inputField} value={code} onChange={e => setForm({ ...form, code: e.target.value })} style={{ marginBottom: 0 }} />
                            </div>
                            <div>
                                <label className={styles.inputLabel}>Constraint Name</label>
                                <input type="text" placeholder="E.g., Medication must be given..." className={styles.inputField} value={form.name} onChange={e => setForm({ ...form, name: e.target.value })} style={{ marginBottom: 0 }} />
                            </div>
                        </div>
                        <div>
                            <label className={styles.inputLabel}>Description</label>
                            <CastTextarea rows={2} className={styles.inputField} value={form.description} onChange={e => setForm({ ...form, description: e.target.value })} style={{ marginBottom: 0 }} />
                        </div>

                        {hazards && hazards.length > 0 && (
                            <div>
                                <label className={styles.inputLabel}>Linked Hazards</label>
                                <div style={{ display: 'flex', flexDirection: 'column', gap: '8px', marginTop: '4px' }}>
                                    {hazards.map(hazard => (
                                        <label key={hazard.id} style={{ display: 'flex', alignItems: 'center', gap: '8px', fontSize: '14px', cursor: 'pointer' }}>
                                            <CastCheckbox
                                                checked={form.hazardIds.includes(hazard.id)}
                                                onChange={() => handleHazardToggle(hazard.id)}
                                            />
                                            {hazard.code}: {hazard.name}
                                        </label>
                                    ))}
                                </div>
                            </div>
                        )}

                        <div style={{ display: 'flex', justifyContent: 'flex-end', gap: '8px' }}>
                            {editingId && (
                                <button onClick={resetForm} className={styles.btnBack} type="button">
                                    <BiX size={18} /> Cancel
                                </button>
                            )}
                            <button onClick={() => save()} disabled={isPending || !form.name.trim() || !code.trim()} className={styles.btnSaveForm}>
                                <BiSave size={18} /> {isPending ? 'Saving...' : editingId ? 'Save Changes' : 'Add Constraint'}
                            </button>
                        </div>
                    </div>
                </div>
            )}
            {confirmDialog}
        </CastSection>

        {previewConstraint && (
            <TraceabilityPreviewModal
                open={!!previewConstraint}
                onClose={() => setPreviewConstraint(null)}
                title="Constraint Details"
                code={previewConstraint.code}
                name={previewConstraint.name}
                description={previewConstraint.description}
                relatedGroups={[
                    {
                        label: 'Linked Hazards',
                        emptyMessage: 'No linked hazards.',
                        items: (previewConstraint.hazards ?? []).map(hazard => ({
                            id: hazard.id,
                            code: hazard.code,
                            name: hazard.name,
                            onGoTo: () => goToSection('hazards')
                        }))
                    }
                ]}
            />
        )}
        </>
    );
}
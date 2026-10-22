import { useState } from 'react';
import { BiSave, BiCheckShield, BiSearchAlt, BiX } from 'react-icons/bi';
import { toast } from 'sonner';
import { useNavigate } from 'react-router-dom';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { getResponsibilities, createResponsibility, updateResponsibility, deleteResponsibility } from '@http/Step2/Responsibilities';
import { getComponents } from '@http/Step2/Components';
import { getByAnalysisId as getConstraints } from '@http/CAST/Step1/Constraints';
import CastCheckbox from '@components/CastCheckbox';
import CastSection from '@components/CastSection';
import CastTextarea from '@components/CastTextarea';
import CastItemActions from '@components/CastItemActions';
import { useCastConfirm } from '@components/CastConfirm';
import LinkBadge from '@components/LinkBadge';
import TraceabilityPreviewModal from '@components/Modal/ModalTraceability/TraceabilityPreviewModal';
import { IResponsibilityReadDto } from '@interfaces/IStep2';
import { IViolatedSystemSafetyConstraintReadDto } from '@interfaces/CAST/IViolatedSystemSafetyConstraint';
import { nextCode } from '@utils/castCodes';
import styles from '../../../Step1/CastStepOne.module.css';

interface Props { componentId: string; analysisId: string; }

const EMPTY_FORM = { code: '', responsibility: '', constraintIds: [] as string[] };

export default function CastResponsibilitiesForm({ componentId, analysisId }: Props) {
    const queryClient = useQueryClient();
    const navigate = useNavigate();
    const { confirmDelete, confirmDialog } = useCastConfirm();
    const [form, setForm] = useState(EMPTY_FORM);
    const [editingId, setEditingId] = useState<string | null>(null);
    const [previewResponsibility, setPreviewResponsibility] = useState<IResponsibilityReadDto | null>(null);
    const [inspectedConstraintId, setInspectedConstraintId] = useState<string | null>(null);

    const { data: responsibilities } = useQuery({
        queryKey: ['component-responsibilities', componentId],
        queryFn: () => getResponsibilities(componentId)
    });

    const { data: components } = useQuery({
        queryKey: ['analysis-components', analysisId],
        queryFn: () => getComponents(analysisId)
    });

    const { data: constraints } = useQuery({
        queryKey: ['violated-ssc-select-options', analysisId],
        queryFn: () => getConstraints(analysisId)
    });

    const allResponsibilityCodes = [
        ...(components ?? []).flatMap(c => (c.responsibilities ?? []).map(r => r.code)),
        ...(responsibilities ?? []).map(r => r.code)
    ];
    const code = form.code || nextCode('R', allResponsibilityCodes);

    const inspectedConstraint = constraints?.find(c => c.id === inspectedConstraintId) ?? null;

    const toggleConstraint = (id: string) => {
        setForm(prev => ({
            ...prev,
            constraintIds: prev.constraintIds.includes(id)
                ? prev.constraintIds.filter(cId => cId !== id)
                : [...prev.constraintIds, id]
        }));
    };

    const resetForm = () => {
        setForm(EMPTY_FORM);
        setEditingId(null);
    };

    const invalidate = () => {
        queryClient.invalidateQueries({ queryKey: ['component-responsibilities', componentId] });
        queryClient.invalidateQueries({ queryKey: ['analysis-components', analysisId] });
    };

    const { mutateAsync: save, isPending } = useMutation({
        mutationFn: async () => {
            const data = {
                responsibility: form.responsibility.trim(),
                code: code.trim(),
                violatedSystemSafetyConstraintIds: form.constraintIds
            };
            return editingId
                ? await updateResponsibility(editingId, data)
                : await createResponsibility({ ...data, componentId });
        },
        onSuccess: () => {
            toast.success(editingId ? "Responsibility updated!" : "Responsibility added!");
            resetForm();
            invalidate();
        },
        onError: (err: any) => toast.error(err.message || "Error saving Responsibility.")
    });

    const { mutateAsync: remove } = useMutation({
        mutationFn: (id: string) => deleteResponsibility(id),
        onSuccess: (_, id) => {
            toast.success("Responsibility removed.");
            if (id === editingId) resetForm();
            invalidate();
        },
        onError: (err: any) => toast.error(err.message || "Error deleting Responsibility.")
    });

    const startEdit = (resp: IResponsibilityReadDto) => {
        setEditingId(resp.id);
        setForm({
            code: resp.code,
            responsibility: resp.responsibility,
            constraintIds: (resp.violatedSystemSafetyConstraints ?? []).map(c => c.id)
        });
    };

    const handleDelete = async (resp: IResponsibilityReadDto) => {
        if (await confirmDelete(`Responsibility ${resp.code}`)) await remove(resp.id);
    };

    const constraintTooltip = (constraint: Pick<IViolatedSystemSafetyConstraintReadDto, 'id' | 'name'>) => {
        const full = constraints?.find(c => c.id === constraint.id);
        return `${full?.name ?? constraint.name} — click to inspect`;
    };

    return (
        <>
        <CastSection title="Responsibilities" tooltipInfo="What was this component supposed to do? Each safety-related responsibility should trace back to the system safety constraints it enforces — click a constraint badge to inspect it without leaving Step 2." hideAddButton={true}>
            <div style={{ display: 'flex', flexDirection: 'column', gap: '24px' }}>

                {responsibilities && responsibilities.map((resp: IResponsibilityReadDto) => (
                    <div key={resp.id} style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', gap: '16px', backgroundColor: 'var(--color-dark-gray)', padding: '16px', borderRadius: '8px', border: `1px solid ${resp.id === editingId ? 'var(--color-yellow)' : 'var(--color-gray)'}` }}>
                        <div>
                            <div style={{ display: 'flex', alignItems: 'center', gap: '8px', marginBottom: '4px', flexWrap: 'wrap' }}>
                                <span style={{ color: 'var(--color-yellow)', fontWeight: 'bold', fontSize: '14px' }}>{resp.code}</span>
                                {(resp.violatedSystemSafetyConstraints ?? []).map(constraint => (
                                    <LinkBadge
                                        key={constraint.id}
                                        code={constraint.code}
                                        tooltip={constraintTooltip(constraint)}
                                        onClick={() => setInspectedConstraintId(constraint.id)}
                                    />
                                ))}
                            </div>
                            <span style={{ color: '#fff', fontSize: '15px', whiteSpace: 'pre-wrap' }}>{resp.responsibility}</span>
                        </div>
                        <CastItemActions itemLabel="Responsibility" onPreview={() => setPreviewResponsibility(resp)} onEdit={() => startEdit(resp)} onDelete={() => handleDelete(resp)} />
                    </div>
                ))}

                <div style={{ backgroundColor: 'var(--color-dark)', padding: '16px', borderRadius: '8px', border: '1px dashed var(--color-gray)' }}>
                    <div style={{ display: 'flex', alignItems: 'center', gap: '8px', marginBottom: '16px', color: 'var(--color-white)' }}>
                        <BiCheckShield size={20} /> <h4 style={{ margin: 0, fontSize: '14px' }}>{editingId ? `Edit Responsibility ${code}` : 'Add Responsibility'}</h4>
                    </div>

                    <div style={{ display: 'grid', gridTemplateColumns: '100px 1fr', gap: '16px' }}>
                        <div>
                            <label className={styles.inputLabel}>Code</label>
                            <input className={styles.inputField} style={{ marginBottom: 0 }} value={code} onChange={e => setForm({ ...form, code: e.target.value })} />
                        </div>
                        <div>
                            <label className={styles.inputLabel}>Responsibility</label>
                            <CastTextarea rows={1} className={styles.inputField} style={{ marginBottom: 0 }} placeholder="Description..." value={form.responsibility} onChange={e => setForm({ ...form, responsibility: e.target.value })} />
                        </div>
                    </div>

                    {constraints && constraints.length > 0 && (
                        <div style={{ marginTop: '16px' }}>
                            <label className={styles.inputLabel}>Violated System Safety Constraints</label>
                            <div style={{ display: 'flex', flexDirection: 'column', gap: '8px', marginTop: '4px' }}>
                                {constraints.map(constraint => (
                                    <div key={constraint.id} style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
                                        <label style={{ display: 'flex', alignItems: 'center', gap: '8px', fontSize: '14px', cursor: 'pointer' }}>
                                            <CastCheckbox
                                                checked={form.constraintIds.includes(constraint.id)}
                                                onChange={() => toggleConstraint(constraint.id)}
                                            />
                                            {constraint.code}: {constraint.name}
                                        </label>
                                        <button
                                            type="button"
                                            onClick={() => setInspectedConstraintId(constraint.id)}
                                            style={{ background: 'none', border: 'none', color: 'var(--color-muted-text)', cursor: 'pointer', padding: '2px', display: 'flex' }}
                                            title="Inspect constraint"
                                        >
                                            <BiSearchAlt size={16} />
                                        </button>
                                    </div>
                                ))}
                            </div>
                        </div>
                    )}

                    <div style={{ display: 'flex', justifyContent: 'flex-end', gap: '8px', marginTop: '16px' }}>
                        {editingId && (
                            <button onClick={resetForm} className={styles.btnBack} type="button">
                                <BiX size={18} /> Cancel
                            </button>
                        )}
                        <button onClick={() => save()} disabled={isPending || !code.trim() || !form.responsibility.trim() || form.constraintIds.length === 0} className={styles.btnSaveForm}>
                            <BiSave size={18} /> {isPending ? 'Saving...' : editingId ? 'Save Changes' : 'Add Responsibility'}
                        </button>
                    </div>
                </div>
            </div>
            {confirmDialog}
        </CastSection>

        {previewResponsibility && (
            <TraceabilityPreviewModal
                open={!!previewResponsibility}
                onClose={() => setPreviewResponsibility(null)}
                title="Responsibility Details"
                code={previewResponsibility.code}
                description={previewResponsibility.responsibility}
                relatedGroups={[
                    {
                        label: 'Violated Safety Constraints',
                        emptyMessage: 'No constraint linked.',
                        items: (previewResponsibility.violatedSystemSafetyConstraints ?? []).map(constraint => ({
                            id: constraint.id,
                            code: constraint.code,
                            name: constraint.name,
                            onGoTo: () => navigate(`/analyses/${analysisId}/cast/step1`, { state: { scrollToSection: 'constraints' } })
                        }))
                    }
                ]}
            />
        )}

        {inspectedConstraint && (
            <TraceabilityPreviewModal
                open={!!inspectedConstraint}
                onClose={() => setInspectedConstraintId(null)}
                title="Safety Constraint"
                code={inspectedConstraint.code}
                name={inspectedConstraint.name}
                description={inspectedConstraint.description || 'No description provided.'}
                relatedGroups={[
                    {
                        label: 'Linked Hazards (Step 1)',
                        emptyMessage: 'No linked hazards.',
                        items: (inspectedConstraint.hazards ?? []).map(hazard => ({
                            id: hazard.id,
                            code: hazard.code,
                            name: hazard.name
                        }))
                    }
                ]}
            />
        )}
        </>
    );
}

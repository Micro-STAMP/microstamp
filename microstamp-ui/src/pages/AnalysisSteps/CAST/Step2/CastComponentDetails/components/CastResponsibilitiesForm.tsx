import { useState } from 'react';
import { BiSave, BiTrash, BiCheckShield, BiShow } from 'react-icons/bi';
import { toast } from 'sonner';
import { useNavigate } from 'react-router-dom';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { getResponsibilities, createResponsibility, deleteResponsibility } from '@http/Step2/Responsibilities';
import { getByAnalysisId as getConstraints } from '@http/CAST/Step1/Constraints';
import CastCheckbox from '@components/CastCheckbox';
import CastSection from '@components/CastSection';
import TraceabilityPreviewModal from '@components/Modal/ModalTraceability/TraceabilityPreviewModal';
import { IResponsibilityReadDto } from '@interfaces/IStep2';
import styles from '../../../Step1/CastStepOne.module.css';

interface Props { componentId: string; analysisId: string; }

export default function CastResponsibilitiesForm({ componentId, analysisId }: Props) {
    const queryClient = useQueryClient();
    const navigate = useNavigate();
    const [form, setForm] = useState({ code: '', responsibility: '', constraintIds: [] as string[] });
    const [previewResponsibility, setPreviewResponsibility] = useState<IResponsibilityReadDto | null>(null);

    const { data: responsibilities } = useQuery({
        queryKey: ['component-responsibilities', componentId],
        queryFn: () => getResponsibilities(componentId)
    });

    const { data: constraints } = useQuery({
        queryKey: ['violated-ssc-select-options', analysisId],
        queryFn: () => getConstraints(analysisId)
    });

    const toggleConstraint = (id: string) => {
        setForm(prev => ({
            ...prev,
            constraintIds: prev.constraintIds.includes(id)
                ? prev.constraintIds.filter(cId => cId !== id)
                : [...prev.constraintIds, id]
        }));
    };

    const { mutateAsync: save, isPending } = useMutation({
        mutationFn: async () => {
            return await createResponsibility({
                responsibility: form.responsibility,
                code: form.code,
                violatedSystemSafetyConstraintIds: form.constraintIds,
                componentId: componentId
            });
        },
        onSuccess: () => {
            toast.success("Responsibility added!");
            setForm({ code: '', responsibility: '', constraintIds: [] });
            queryClient.invalidateQueries({ queryKey: ['component-responsibilities', componentId] });
        }
    });

    const { mutateAsync: remove } = useMutation({
        mutationFn: (id: string) => deleteResponsibility(id),
        onSuccess: () => queryClient.invalidateQueries({ queryKey: ['component-responsibilities', componentId] })
    });



    return (
        <>
        <CastSection title="Responsibilities" tooltipInfo="What was this component supposed to do?" hideAddButton={true}>
            <div style={{ display: 'flex', flexDirection: 'column', gap: '24px' }}>

                {responsibilities && responsibilities.map((resp: IResponsibilityReadDto) => (
                    <div key={resp.id} style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', backgroundColor: 'var(--color-dark-gray)', padding: '16px', borderRadius: '8px', border: '1px solid var(--color-gray)' }}>
                        <div>
                            <div style={{ display: 'flex', alignItems: 'center', gap: '8px', marginBottom: '4px', flexWrap: 'wrap' }}>
                                <span style={{ color: 'var(--color-yellow)', fontWeight: 'bold', fontSize: '14px' }}>{resp.code}</span>
                                {(resp.violatedSystemSafetyConstraints ?? []).map(constraint => (
                                    <span key={constraint.id} style={{ backgroundColor: 'var(--color-dark)', color: 'var(--color-muted-text)', padding: '2px 6px', borderRadius: '4px', fontSize: '10px', border: '1px solid var(--color-gray)' }}>
                                        {constraint.code}
                                    </span>
                                ))}
                            </div>
                            <span style={{ color: '#fff', fontSize: '15px' }}>{resp.responsibility}</span>
                        </div>
                        <div style={{ display: 'flex', gap: '4px' }}>
                            <button onClick={() => setPreviewResponsibility(resp)} style={{ background: 'transparent', border: 'none', color: 'var(--color-muted-text)', cursor: 'pointer' }} title="View traceability"><BiShow size={18} /></button>
                            <button onClick={() => remove(resp.id)} style={{ background: 'transparent', border: 'none', color: '#ef4444', cursor: 'pointer' }} title="Remove Responsibility"><BiTrash size={18} /></button>
                        </div>
                    </div>
                ))}

                <div style={{ backgroundColor: 'var(--color-dark)', padding: '16px', borderRadius: '8px', border: '1px dashed var(--color-gray)' }}>
                    <div style={{ display: 'flex', alignItems: 'center', gap: '8px', marginBottom: '16px', color: 'var(--color-white)' }}>
                        <BiCheckShield size={20} /> <h4 style={{ margin: 0, fontSize: '14px' }}>Add Responsibility</h4>
                    </div>

                    <div style={{ display: 'grid', gridTemplateColumns: '100px 1fr', gap: '16px' }}>
                        <input className={styles.inputField} placeholder="Code" value={form.code} onChange={e => setForm({...form, code: e.target.value})} />
                        <input className={styles.inputField} placeholder="Description..." value={form.responsibility} onChange={e => setForm({...form, responsibility: e.target.value})} />
                    </div>

                    {constraints && constraints.length > 0 && (
                        <div style={{ marginTop: '16px' }}>
                            <label className={styles.inputLabel}>Violated System Safety Constraints</label>
                            <div style={{ display: 'flex', flexDirection: 'column', gap: '8px', marginTop: '4px' }}>
                                {constraints.map(constraint => (
                                    <label key={constraint.id} style={{ display: 'flex', alignItems: 'center', gap: '8px', fontSize: '14px', cursor: 'pointer' }}>
                                        <CastCheckbox
                                            checked={form.constraintIds.includes(constraint.id)}
                                            onChange={() => toggleConstraint(constraint.id)}
                                        />
                                        {constraint.code}: {constraint.name}
                                    </label>
                                ))}
                            </div>
                        </div>
                    )}

                    <div style={{ display: 'flex', justifyContent: 'flex-end', marginTop: '16px' }}>
                        <button onClick={() => save()} disabled={isPending || !form.code || !form.responsibility || form.constraintIds.length === 0} className={styles.btnSaveForm}>
                            <BiSave size={18} /> {isPending ? 'Saving...' : 'Add Responsibility'}
                        </button>
                    </div>
                </div>
            </div>
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
        </>
    );
}

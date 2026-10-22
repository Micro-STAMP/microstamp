import { useState } from 'react';
import { BiSave, BiX } from 'react-icons/bi';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { toast } from 'sonner';
import { getByAnalysisId as getHazards, createHazard, updateHazard, deleteHazard } from '@http/CAST/Step1/Hazards';
import { getByAnalysisId as getLossEvents } from '@http/CAST/Step1/AccidentLossEvents';
import CastSection from '@components/CastSection';
import CastCheckbox from '@components/CastCheckbox';
import LinkBadge from '@components/LinkBadge';
import CastTextarea from '@components/CastTextarea';
import CastItemActions from '@components/CastItemActions';
import { useCastConfirm } from '@components/CastConfirm';
import { nextCode } from '@utils/castCodes';
import { useRegisterCastSection, useGoToCastSection } from '@components/CastSection/CastSectionNavigator';
import TraceabilityPreviewModal from '@components/Modal/ModalTraceability/TraceabilityPreviewModal';
import { ICastHazardReadDto } from '@interfaces/CAST/ICastHazard';
import styles from '../CastStepOne.module.css';

interface Props { analysisId: string; }

export default function CastHazardForm({ analysisId }: Props) {
    const queryClient = useQueryClient();
    const registerHazardsSection = useRegisterCastSection('hazards');
    const goToSection = useGoToCastSection();
    const [previewHazard, setPreviewHazard] = useState<ICastHazardReadDto | null>(null);
    const { confirmDelete, confirmDialog } = useCastConfirm();

    const { data: hazards, isLoading: isLoadingHazards } = useQuery({
        queryKey: ['hazards', analysisId],
        queryFn: () => getHazards(analysisId)
    });

    const { data: losses } = useQuery({
        queryKey: ['loss-events', analysisId],
        queryFn: () => getLossEvents(analysisId)
    });

    const EMPTY_FORM = { code: '', name: '', description: '', accidentLossEventIds: [] as string[] };
    const [form, setForm] = useState(EMPTY_FORM);
    const [editingId, setEditingId] = useState<string | null>(null);
    const code = form.code || nextCode('H', (hazards ?? []).map(h => h.code));

    const resetForm = () => {
        setForm(EMPTY_FORM);
        setEditingId(null);
    };

    const invalidate = () => {
        queryClient.invalidateQueries({ queryKey: ['hazards', analysisId] });
        queryClient.invalidateQueries({ queryKey: ['constraints', analysisId] });
    };

    const { mutateAsync: save, isPending } = useMutation({
        mutationFn: async () => {
            const data = {
                code: code.trim(),
                name: form.name.trim(),
                description: form.description,
                accidentLossEventIds: form.accidentLossEventIds
            };
            return editingId ? await updateHazard(editingId, data) : await createHazard({ ...data, analysisId });
        },
        onSuccess: () => {
            toast.success(editingId ? "Hazard updated!" : "Hazard added successfully!");
            resetForm();
            invalidate();
        },
        onError: (err: any) => toast.error(err.message || "Error saving Hazard.")
    });

    const { mutateAsync: remove } = useMutation({
        mutationFn: async (id: string) => await deleteHazard(id),
        onSuccess: (_, id) => {
            toast.success("Hazard removed.");
            if (id === editingId) resetForm();
            invalidate();
        },
        onError: (err: any) => toast.error(err.message || "Error deleting Hazard.")
    });

    const startEdit = (hazard: ICastHazardReadDto) => {
        setEditingId(hazard.id);
        setForm({
            code: hazard.code,
            name: hazard.name,
            description: hazard.description || '',
            accidentLossEventIds: (hazard.accidentLossEvents ?? []).map(loss => loss.id)
        });
    };

    const handleDelete = async (hazard: ICastHazardReadDto) => {
        if (await confirmDelete(`Hazard ${hazard.code}`)) await remove(hazard.id);
    };

    const handleLossToggle = (lossId: string) => {
        setForm(prev => ({
            ...prev,
            accidentLossEventIds: prev.accidentLossEventIds.includes(lossId)
                ? prev.accidentLossEventIds.filter(id => id !== lossId)
                : [...prev.accidentLossEventIds, lossId]
        }));
    };

    return (
        <>
        <CastSection ref={registerHazardsSection} title="System Hazards" tooltipInfo="What unsafe states materialized?" defaultOpen={false} hideAddButton={true}>
            {isLoadingHazards ? (
                <div style={{ color: 'var(--color-muted-text)', fontSize: '14px', padding: '10px' }}>Loading hazards...</div>
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
                                        backgroundColor: 'var(--color-dark-gray)', 
                                        border: `1px solid ${h.id === editingId ? 'var(--color-yellow)' : 'var(--color-gray)'}`, 
                                        borderRadius: '8px',
                                        gap: '16px'
                                    }}
                                >
                                    <div style={{ flex: 1 }}>
                                        <div style={{ display: 'flex', alignItems: 'center', gap: '8px', marginBottom: '6px', flexWrap: 'wrap' }}>
                                            <span style={{
                                                color: 'var(--color-yellow)',
                                                fontWeight: 600,
                                                fontSize: '14px'
                                            }}>
                                                {h.code}
                                            </span>
                                            <strong style={{ color: '#ffffff', fontSize: '15px' }}>{h.name}</strong>
                                            {(h.accidentLossEvents ?? []).map(loss => (
                                                <LinkBadge key={loss.id} code={loss.code} tooltip={loss.name} />
                                            ))}
                                        </div>

                                        {h.description && (
                                            <p style={{ 
                                                margin: 0, 
                                                fontSize: '14px', 
                                                color: 'var(--color-muted-text)', 
                                                lineHeight: '1.5' 
                                            }}>
                                                {h.description}
                                            </p>
                                        )}
                                    </div>
                                    
                                    <CastItemActions itemLabel="Hazard" onPreview={() => setPreviewHazard(h)} onEdit={() => startEdit(h)} onDelete={() => handleDelete(h)} />
                                </div>
                            ))}
                        </div>
                    ) : (
                        <div className={styles.emptyState}>No hazards added yet.</div>
                    )}

                    <hr style={{ borderTop: '1px solid #e5e7eb', margin: '10px 0' }} />

                    <div style={{ display: 'flex', flexDirection: 'column', gap: '16px' }}>
                        <h4 style={{ margin: 0, fontSize: '14px', color: 'var(--color-white)' }}>{editingId ? `Edit Hazard ${code}` : 'Add New Hazard'}</h4>
                        <div style={{ display: 'grid', gridTemplateColumns: '100px 1fr', gap: '16px' }}>
                            <div>
                                <label className={styles.inputLabel}>Code</label>
                                <input type="text" className={styles.inputField} value={code} onChange={e => setForm({ ...form, code: e.target.value })} style={{ marginBottom: 0 }} />
                            </div>
                            <div>
                                <label className={styles.inputLabel}>Hazard Title</label>
                                <input type="text" placeholder="E.g., High Pressure" className={styles.inputField} value={form.name} onChange={e => setForm({ ...form, name: e.target.value })} style={{ marginBottom: 0 }} />
                            </div>
                        </div>
                        <div>
                            <label className={styles.inputLabel}>Description</label>
                            <CastTextarea rows={2} className={styles.inputField} value={form.description} onChange={e => setForm({ ...form, description: e.target.value })} style={{ marginBottom: 0 }} />
                        </div>

                        {losses && losses.length > 0 && (
                            <div>
                                <label className={styles.inputLabel}>Linked Losses</label>
                                <div style={{ display: 'flex', flexDirection: 'column', gap: '8px', marginTop: '4px' }}>
                                    {losses.map(loss => (
                                        <label key={loss.id} style={{ display: 'flex', alignItems: 'center', gap: '8px', fontSize: '14px', cursor: 'pointer' }}>
                                            <CastCheckbox
                                                checked={form.accidentLossEventIds.includes(loss.id)}
                                                onChange={() => handleLossToggle(loss.id)}
                                            />
                                            {loss.code}: {loss.name}
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
                                <BiSave size={18} /> {isPending ? 'Saving...' : editingId ? 'Save Changes' : 'Add Hazard'}
                            </button>
                        </div>
                    </div>
                </div>
            )}
            {confirmDialog}
        </CastSection>

        {previewHazard && (
            <TraceabilityPreviewModal
                open={!!previewHazard}
                onClose={() => setPreviewHazard(null)}
                title="Hazard Details"
                code={previewHazard.code}
                name={previewHazard.name}
                description={previewHazard.description}
                relatedGroups={[
                    {
                        label: 'Linked Losses',
                        emptyMessage: 'No linked loss events.',
                        items: (previewHazard.accidentLossEvents ?? []).map(loss => ({
                            id: loss.id,
                            code: loss.code,
                            name: loss.name,
                            onGoTo: () => goToSection('loss-events')
                        }))
                    }
                ]}
            />
        )}
        </>
    );
}
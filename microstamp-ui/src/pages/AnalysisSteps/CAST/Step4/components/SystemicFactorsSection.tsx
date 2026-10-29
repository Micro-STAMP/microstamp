import { useState } from 'react';
import { BiTrash, BiPencil, BiLinkAlt, BiShow } from 'react-icons/bi';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { useNavigate } from 'react-router-dom';
import { toast } from 'sonner';
import {
    getByAnalysisId,
    createSystemicFactor,
    updateSystemicFactor,
    deleteSystemicFactor
} from '@http/CAST/Step4/SystemicFactors';
import { getByAnalysisId as getCacsByAnalysisId } from '@http/CAST/Step3/ControlActionContributions';
import {
    systemicFactorCategoryLabel,
    ISystemicFactorReadDto
} from '@interfaces/CAST/IStep4/ISystemicFactor';
import CastSection from '@components/CastSection';
import { useCastConfirm } from '@components/CastConfirm';
import LinkBadge from '@components/LinkBadge';
import ModalSystemicFactor from '@components/Modal/ModalEntity/ModalStep4/ModalSystemicFactor';
import TraceabilityPreviewModal from '@components/Modal/ModalTraceability/TraceabilityPreviewModal';
import styles from '../../Step1/CastStepOne.module.css';

interface Props { analysisId: string; }

export default function SystemicFactorsSection({ analysisId }: Props) {
    const queryClient = useQueryClient();
    const { confirmDelete, confirmDialog } = useCastConfirm();
    const navigate = useNavigate();
    const [previewFactor, setPreviewFactor] = useState<ISystemicFactorReadDto | null>(null);

    const { data: systemicFactors, isLoading: isLoadingFactors } = useQuery({
        queryKey: ['systemic-factors', analysisId],
        queryFn: () => getByAnalysisId(analysisId)
    });

    const { data: cacs, isLoading: isLoadingCacs } = useQuery({
        queryKey: ['control-action-contributions', analysisId],
        queryFn: () => getCacsByAnalysisId(analysisId)
    });

    const cacOptions = cacs ? cacs.map(cac => ({ id: cac.id, code: cac.code, controlActionName: cac.controlActionName })) : [];
    const getCac = (cacId: string) => cacOptions.find(i => i.id === cacId) ?? null;

    const [modalOpen, setModalOpen] = useState(false);
    const [editingFactor, setEditingFactor] = useState<ISystemicFactorReadDto | null>(null);

    const openCreateModal = () => {
        setEditingFactor(null);
        setModalOpen(true);
    };
    const openEditModal = (factor: ISystemicFactorReadDto) => {
        setEditingFactor(factor);
        setModalOpen(true);
    };
    const closeModal = () => setModalOpen(false);

    const { mutateAsync: create, isPending: isCreating } = useMutation({
        mutationFn: createSystemicFactor,
        onSuccess: () => {
            toast.success("Systemic Factor added successfully!");
            queryClient.invalidateQueries({ queryKey: ['systemic-factors', analysisId] });
        },
        onError: (err: any) => toast.error(err.message || "Error saving Systemic Factor.")
    });

    const { mutateAsync: update, isPending: isUpdating } = useMutation({
        mutationFn: ({ id, data }: { id: string; data: Parameters<typeof updateSystemicFactor>[1] }) =>
            updateSystemicFactor(id, data),
        onSuccess: () => {
            toast.success("Systemic Factor updated successfully!");
            queryClient.invalidateQueries({ queryKey: ['systemic-factors', analysisId] });
        },
        onError: (err: any) => toast.error(err.message || "Error updating Systemic Factor.")
    });

    const { mutateAsync: remove } = useMutation({
        mutationFn: async (id: string) => {
            return await deleteSystemicFactor(id);
        },
        onSuccess: () => {
            toast.success("Systemic Factor removed.");
            queryClient.invalidateQueries({ queryKey: ['systemic-factors', analysisId] });
        },
        onError: (err: any) => toast.error(err.message)
    });

    return (
        <>
        <CastSection
            title="Systemic Factors"
            tooltipInfo="Beyond analyzing each individual component (Step 3), CAST requires identifying flaws in the control structure as a whole: general systemic factors — such as communication and coordination, the safety information system, the design of the safety management system, safety culture, changes and dynamics over time, and economic/environmental factors — that span multiple components and help explain why the safety controls, taken together, were ineffective."
            onAddClick={openCreateModal}
            addButtonLabel="Add Systemic Factor"
        >
            {isLoadingFactors || isLoadingCacs ? (
                <div style={{ color: 'var(--color-muted-text)', fontSize: '14px', padding: '10px' }}>Loading Systemic Factors...</div>
            ) : systemicFactors && systemicFactors.length > 0 ? (
                <div style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
                    {systemicFactors.map(factor => (
                        <div
                            key={factor.id}
                            style={{
                                display: 'flex',
                                justifyContent: 'space-between',
                                alignItems: 'flex-start',
                                padding: '16px',
                                backgroundColor: 'var(--color-dark-gray)',
                                border: '1px solid var(--color-gray)',
                                borderRadius: '8px',
                                gap: '16px'
                            }}
                        >
                            <div style={{ flex: 1 }}>
                                <div style={{ display: 'flex', alignItems: 'center', gap: '8px', marginBottom: '6px', flexWrap: 'wrap' }}>
                                    <span style={{ backgroundColor: 'var(--color-dark)', color: 'var(--color-yellow)', padding: '2px 8px', borderRadius: '4px', fontSize: '12px', border: '1px solid var(--color-gray)', fontWeight: 600 }}>
                                        {systemicFactorCategoryLabel(factor)}
                                    </span>
                                </div>

                                {factor.description && (
                                    <p style={{ margin: '0 0 8px 0', fontSize: '14px', color: '#d1d5db', lineHeight: '1.5' }}>
                                        {factor.description}
                                    </p>
                                )}

                                {factor.controlActionContributionIds && factor.controlActionContributionIds.length > 0 && (
                                    <div style={{ display: 'flex', flexWrap: 'wrap', gap: '6px' }}>
                                        {factor.controlActionContributionIds.map(cacId => (
                                            <LinkBadge
                                                key={cacId}
                                                icon={<BiLinkAlt size={12} />}
                                                code={getCac(cacId)?.code ?? 'Removed'}
                                                tooltip={getCac(cacId)?.controlActionName ?? 'This CAC was deleted in Step 3. The link will be cleared when this factor is saved.'}
                                            />
                                        ))}
                                    </div>
                                )}
                            </div>

                            <div style={{ display: 'flex', alignItems: 'center', gap: '12px' }}>
                                <button
                                    onClick={() => setPreviewFactor(factor)}
                                    style={{ background: 'none', border: 'none', color: 'var(--color-muted-text)', cursor: 'pointer', padding: '4px' }}
                                    title="View traceability"
                                >
                                    <BiShow size={18} />
                                </button>
                                <button
                                    onClick={() => openEditModal(factor)}
                                    style={{ background: 'none', border: 'none', color: '#60a5fa', cursor: 'pointer', padding: '4px' }}
                                    title="Edit Systemic Factor"
                                >
                                    <BiPencil size={18} />
                                </button>
                                <button
                                    onClick={async () => { if (await confirmDelete('this Systemic Factor', 'Recommendations linked to it will lose this link.')) remove(factor.id); }}
                                    style={{ background: 'none', border: 'none', color: '#ef4444', cursor: 'pointer', padding: '4px' }}
                                    title="Delete Systemic Factor"
                                >
                                    <BiTrash size={18} />
                                </button>
                            </div>
                        </div>
                    ))}
                </div>
            ) : (
                <div className={styles.emptyState}>No Systemic Factors added yet.</div>
            )}

            <ModalSystemicFactor
                open={modalOpen}
                onClose={closeModal}
                analysisId={analysisId}
                cacOptions={cacOptions}
                customCategories={Array.from(new Set((systemicFactors ?? []).map(f => f.customCategory).filter((c): c is string => !!c)))}
                systemicFactor={editingFactor}
                isLoading={isCreating || isUpdating}
                onCreate={async data => { await create(data); }}
                onUpdate={async (id, data) => { await update({ id, data }); }}
            />
            {confirmDialog}
        </CastSection>

        {previewFactor && (
            <TraceabilityPreviewModal
                open={!!previewFactor}
                onClose={() => setPreviewFactor(null)}
                title="Systemic Factor Details"
                code={systemicFactorCategoryLabel(previewFactor)}
                description={previewFactor.description}
                relatedGroups={[
                    {
                        label: 'Linked CACs (Step 3)',
                        emptyMessage: 'No CACs linked.',
                        items: (previewFactor.controlActionContributionIds ?? []).map(cacId => {
                            const cac = cacOptions.find(i => i.id === cacId);
                            return {
                                id: cacId,
                                code: cac?.code ?? 'Removed',
                                name: cac?.controlActionName ?? 'CAC deleted in Step 3',
                                onGoTo: () => navigate(`/analyses/${analysisId}/cast/step3`)
                            };
                        })
                    }
                ]}
            />
        )}
        </>
    );
}

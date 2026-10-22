import { useState } from 'react';
import { BiTrash, BiPencil, BiLinkAlt, BiUserCheck, BiShow, BiCalendar } from 'react-icons/bi';
import { useNavigate } from 'react-router-dom';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { toast } from 'sonner';
import {
    getByAnalysisId,
    createRecommendation,
    updateRecommendation,
    deleteRecommendation
} from '@http/CAST/Step5/Recommendations';
import { getByAnalysisId as getCacsByAnalysisId } from '@http/CAST/Step3/ControlActionContributions';
import { getByAnalysisId as getSystemicFactorsByAnalysisId } from '@http/CAST/Step4/SystemicFactors';
import { getComponents } from '@http/Step2/Components';
import { componentsToSelectOptions } from '@interfaces/IStep2/IComponent';
import {
    IRecommendationReadDto,
    recommendationPriorityToSelectOption,
    recommendationStatusToSelectOption,
    recommendationTypeToSelectOption,
    recommendationScopeCategoryToSelectOption
} from '@interfaces/CAST/IStep5/IRecommendation';
import { systemicFactorCategoryToSelectOption } from '@interfaces/CAST/IStep4/ISystemicFactor';
import { formatDateBR } from '@services/date';
import CastSection from '@components/CastSection';
import LinkBadge from '@components/LinkBadge';
import ModalRecommendation from '@components/Modal/ModalEntity/ModalStep5/ModalRecommendation';
import TraceabilityPreviewModal from '@components/Modal/ModalTraceability/TraceabilityPreviewModal';
import styles from '../../Step1/CastStepOne.module.css';

interface Props { analysisId: string; }

export default function RecommendationsSection({ analysisId }: Props) {
    const queryClient = useQueryClient();
    const navigate = useNavigate();
    const [previewRecommendation, setPreviewRecommendation] = useState<IRecommendationReadDto | null>(null);

    const { data: recommendations, isLoading: isLoadingRecommendations } = useQuery({
        queryKey: ['recommendations', analysisId],
        queryFn: () => getByAnalysisId(analysisId)
    });

    const { data: cacs, isLoading: isLoadingCacs } = useQuery({
        queryKey: ['control-action-contributions', analysisId],
        queryFn: () => getCacsByAnalysisId(analysisId)
    });

    const { data: systemicFactors, isLoading: isLoadingSystemicFactors } = useQuery({
        queryKey: ['systemic-factors', analysisId],
        queryFn: () => getSystemicFactorsByAnalysisId(analysisId)
    });

    const { data: components, isLoading: isLoadingComponents } = useQuery({
        queryKey: ['analysis-components', analysisId],
        queryFn: () => getComponents(analysisId)
    });

    const componentOptions = components ? componentsToSelectOptions(components) : [];
    const getComponentLabel = (componentId: string) => {
        const option = componentOptions.find(c => c.value === componentId);
        return option ? option.label : 'Unknown component';
    };
    const getComponent = (componentId: string) => components?.find(c => c.id === componentId) || null;

    const cacOptions = cacs ? cacs.map(cac => ({ id: cac.id, code: cac.code, controlActionName: cac.controlActionName })) : [];
    const getCac = (cacId: string) => cacOptions.find(i => i.id === cacId) ?? null;

    const systemicFactorOptions = systemicFactors
        ? systemicFactors.map(factor => ({
            id: factor.id,
            categoryLabel: systemicFactorCategoryToSelectOption(factor.category).label,
            description: factor.description
        }))
        : [];
    const getSystemicFactor = (systemicFactorId: string) => systemicFactorOptions.find(f => f.id === systemicFactorId) ?? null;

    const [modalOpen, setModalOpen] = useState(false);
    const [editingRecommendation, setEditingRecommendation] = useState<IRecommendationReadDto | null>(null);

    const openCreateModal = () => {
        setEditingRecommendation(null);
        setModalOpen(true);
    };
    const openEditModal = (recommendation: IRecommendationReadDto) => {
        setEditingRecommendation(recommendation);
        setModalOpen(true);
    };
    const closeModal = () => setModalOpen(false);

    const { mutateAsync: create, isPending: isCreating } = useMutation({
        mutationFn: createRecommendation,
        onSuccess: () => {
            toast.success("Recommendation added successfully!");
            queryClient.invalidateQueries({ queryKey: ['recommendations', analysisId] });
        },
        onError: (err: any) => toast.error(err.message || "Error saving Recommendation.")
    });

    const { mutateAsync: update, isPending: isUpdating } = useMutation({
        mutationFn: ({ id, data }: { id: string; data: Parameters<typeof updateRecommendation>[1] }) =>
            updateRecommendation(id, data),
        onSuccess: () => {
            toast.success("Recommendation updated successfully!");
            queryClient.invalidateQueries({ queryKey: ['recommendations', analysisId] });
        },
        onError: (err: any) => toast.error(err.message || "Error updating Recommendation.")
    });

    const { mutateAsync: remove } = useMutation({
        mutationFn: async (id: string) => {
            if (window.confirm("Are you sure you want to delete this Recommendation?")) {
                return await deleteRecommendation(id);
            }
            return Promise.reject(new Error("Cancelled"));
        },
        onSuccess: () => {
            toast.success("Recommendation removed.");
            queryClient.invalidateQueries({ queryKey: ['recommendations', analysisId] });
        },
        onError: (err: any) => {
            if (err.message !== "Cancelled") toast.error(err.message);
        }
    });

    const isLoading = isLoadingRecommendations || isLoadingCacs || isLoadingSystemicFactors || isLoadingComponents;

    return (
        <>
        <CastSection
            title="Recommendations"
            tooltipInfo="The final step of CAST is to create an improvement program: generate recommendations that address the identified Control Action Contributions (Step 3) and Systemic Factors (Step 4), improving the safety control structure as a whole rather than simply assigning blame (CAST Handbook, Leveson 2019)."
            onAddClick={openCreateModal}
            addButtonLabel="Add Recommendation"
        >
            {isLoading ? (
                <div style={{ color: 'var(--color-muted-text)', fontSize: '14px', padding: '10px' }}>Loading Recommendations...</div>
            ) : recommendations && recommendations.length > 0 ? (
                <div style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
                    {recommendations.map(recommendation => (
                        <div
                            key={recommendation.id}
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
                                {(recommendation.status || recommendation.priority || recommendation.recommendationType || recommendation.scopeCategory || recommendation.componentId || recommendation.assignee || recommendation.dueDate) && (
                                    <div style={{ display: 'flex', alignItems: 'center', gap: '8px', marginBottom: '6px', flexWrap: 'wrap' }}>
                                        {recommendation.status && (
                                            <span style={{ backgroundColor: 'var(--color-dark)', color: '#4ade80', padding: '2px 8px', borderRadius: '4px', fontSize: '12px', border: '1px solid var(--color-gray)', fontWeight: 600 }}>
                                                {recommendationStatusToSelectOption(recommendation.status).label}
                                            </span>
                                        )}
                                        {recommendation.priority && (
                                            <span style={{ backgroundColor: 'var(--color-dark)', color: 'var(--color-yellow)', padding: '2px 8px', borderRadius: '4px', fontSize: '12px', border: '1px solid var(--color-gray)', fontWeight: 600 }}>
                                                {recommendationPriorityToSelectOption(recommendation.priority).label}
                                            </span>
                                        )}
                                        {recommendation.recommendationType && (
                                            <span style={{ backgroundColor: 'var(--color-dark)', color: '#d1d5db', padding: '2px 8px', borderRadius: '4px', fontSize: '12px', border: '1px solid var(--color-gray)' }}>
                                                {recommendationTypeToSelectOption(recommendation.recommendationType).label}
                                            </span>
                                        )}
                                        {recommendation.scopeCategory && (
                                            <span style={{ backgroundColor: 'var(--color-dark)', color: '#d1d5db', padding: '2px 8px', borderRadius: '4px', fontSize: '12px', border: '1px solid var(--color-gray)' }}>
                                                {recommendationScopeCategoryToSelectOption(recommendation.scopeCategory).label}
                                            </span>
                                        )}
                                        {recommendation.componentId && (
                                            <span style={{ display: 'flex', alignItems: 'center', gap: '4px', color: 'var(--color-muted-text)', fontSize: '12px' }}>
                                                <BiUserCheck size={14} color="#60a5fa" /> {getComponentLabel(recommendation.componentId)}
                                            </span>
                                        )}
                                        {recommendation.assignee && (
                                            <span style={{ display: 'flex', alignItems: 'center', gap: '4px', color: 'var(--color-muted-text)', fontSize: '12px' }}>
                                                <BiUserCheck size={14} color="#60a5fa" /> {recommendation.assignee}
                                            </span>
                                        )}
                                        {recommendation.dueDate && (
                                            <span style={{ display: 'flex', alignItems: 'center', gap: '4px', color: 'var(--color-muted-text)', fontSize: '12px' }}>
                                                <BiCalendar size={14} /> Due {formatDateBR(recommendation.dueDate)}
                                            </span>
                                        )}
                                    </div>
                                )}

                                {recommendation.description && (
                                    <p style={{ margin: '0 0 8px 0', fontSize: '14px', color: '#d1d5db', lineHeight: '1.5' }}>
                                        {recommendation.description}
                                    </p>
                                )}

                                {recommendation.auditMechanism && (
                                    <p style={{ margin: '0 0 8px 0', fontSize: '13px', color: 'var(--color-muted-text)', lineHeight: '1.5' }}>
                                        <strong style={{ color: '#d1d5db' }}>Feedback / Audit Mechanism: </strong>{recommendation.auditMechanism}
                                    </p>
                                )}

                                {recommendation.effectivenessEvidence && (
                                    <p style={{ margin: '0 0 8px 0', fontSize: '13px', color: 'var(--color-muted-text)', lineHeight: '1.5' }}>
                                        <strong style={{ color: '#d1d5db' }}>Audit Findings / Effectiveness Evidence: </strong>{recommendation.effectivenessEvidence}
                                    </p>
                                )}

                                {(recommendation.controlActionContributionIds?.length > 0 || recommendation.systemicFactorIds?.length > 0) && (
                                    <div style={{ display: 'flex', flexWrap: 'wrap', gap: '6px' }}>
                                        {recommendation.controlActionContributionIds?.map(cacId => (
                                            <LinkBadge
                                                key={cacId}
                                                icon={<BiLinkAlt size={12} />}
                                                code={getCac(cacId)?.code ?? '—'}
                                                tooltip={getCac(cacId)?.controlActionName}
                                            />
                                        ))}
                                        {recommendation.systemicFactorIds?.map(systemicFactorId => (
                                            <LinkBadge
                                                key={systemicFactorId}
                                                icon={<BiLinkAlt size={12} />}
                                                code={getSystemicFactor(systemicFactorId)?.categoryLabel ?? '—'}
                                                tooltip={getSystemicFactor(systemicFactorId)?.description}
                                            />
                                        ))}
                                    </div>
                                )}
                            </div>

                            <div style={{ display: 'flex', alignItems: 'center', gap: '12px' }}>
                                <button
                                    onClick={() => setPreviewRecommendation(recommendation)}
                                    style={{ background: 'none', border: 'none', color: 'var(--color-muted-text)', cursor: 'pointer', padding: '4px' }}
                                    title="View traceability"
                                >
                                    <BiShow size={18} />
                                </button>
                                <button
                                    onClick={() => openEditModal(recommendation)}
                                    style={{ background: 'none', border: 'none', color: '#60a5fa', cursor: 'pointer', padding: '4px' }}
                                    title="Edit Recommendation"
                                >
                                    <BiPencil size={18} />
                                </button>
                                <button
                                    onClick={() => remove(recommendation.id)}
                                    style={{ background: 'none', border: 'none', color: '#ef4444', cursor: 'pointer', padding: '4px' }}
                                    title="Delete Recommendation"
                                >
                                    <BiTrash size={18} />
                                </button>
                            </div>
                        </div>
                    ))}
                </div>
            ) : (
                <div className={styles.emptyState}>No Recommendations added yet.</div>
            )}

            <ModalRecommendation
                open={modalOpen}
                onClose={closeModal}
                analysisId={analysisId}
                cacOptions={cacOptions}
                systemicFactorOptions={systemicFactorOptions}
                componentOptions={componentOptions}
                recommendation={editingRecommendation}
                isLoading={isCreating || isUpdating}
                onCreate={async data => { await create(data); }}
                onUpdate={async (id, data) => { await update({ id, data }); }}
            />
        </CastSection>

        {previewRecommendation && (
            <TraceabilityPreviewModal
                open={!!previewRecommendation}
                onClose={() => setPreviewRecommendation(null)}
                title="Recommendation Details"
                code={previewRecommendation.priority ? recommendationPriorityToSelectOption(previewRecommendation.priority).label : 'Recommendation'}
                description={previewRecommendation.description}
                relatedGroups={[
                    {
                        label: 'Linked CACs (Step 3)',
                        emptyMessage: 'No CACs linked.',
                        items: (previewRecommendation.controlActionContributionIds ?? []).map(cacId => {
                            const cac = cacOptions.find(i => i.id === cacId);
                            return {
                                id: cacId,
                                code: cac?.code ?? '—',
                                name: cac?.controlActionName ?? 'CAC not found',
                                onGoTo: () => navigate(`/analyses/${analysisId}/cast/step3`)
                            };
                        })
                    },
                    {
                        label: 'Linked Systemic Factors (Step 4)',
                        emptyMessage: 'No Systemic Factor linked.',
                        items: (previewRecommendation.systemicFactorIds ?? []).map(systemicFactorId => {
                            const factor = systemicFactorOptions.find(f => f.id === systemicFactorId);
                            return {
                                id: systemicFactorId,
                                code: factor?.categoryLabel ?? '—',
                                name: factor?.description ?? 'Systemic Factor not found',
                                onGoTo: () => navigate(`/analyses/${analysisId}/cast/step4`)
                            };
                        })
                    },
                    {
                        label: 'Target Controller (Step 2)',
                        emptyMessage: 'No controller assigned (structural / organizational recommendation).',
                        items: previewRecommendation.componentId && getComponent(previewRecommendation.componentId)
                            ? [{
                                id: previewRecommendation.componentId,
                                code: getComponent(previewRecommendation.componentId)!.code,
                                name: getComponent(previewRecommendation.componentId)!.name,
                                onGoTo: () => navigate(`/analyses/${analysisId}/cast/step2/component/${previewRecommendation.componentId}`)
                            }]
                            : []
                    }
                ]}
            />
        )}
        </>
    );
}

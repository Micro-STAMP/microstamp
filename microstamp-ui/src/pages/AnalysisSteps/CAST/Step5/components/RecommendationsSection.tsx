import { useState } from 'react';
import { BiTrash, BiPencil, BiLinkAlt } from 'react-icons/bi';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { toast } from 'sonner';
import {
    getByAnalysisId,
    createRecommendation,
    updateRecommendation,
    deleteRecommendation
} from '@http/CAST/Step5/Recommendations';
import { getByAnalysisId as getIcasByAnalysisId } from '@http/CAST/Step3/InadequateControlActions';
import { getByAnalysisId as getSystemicFactorsByAnalysisId } from '@http/CAST/Step4/SystemicFactors';
import { IRecommendationReadDto } from '@interfaces/CAST/IStep5/IRecommendation';
import { systemicFactorCategoryToSelectOption } from '@interfaces/CAST/IStep4/ISystemicFactor';
import CastSection from '@components/CastSection';
import ModalRecommendation from '@components/Modal/ModalEntity/ModalStep5/ModalRecommendation';
import styles from '../../Step1/CastStepOne.module.css';

interface Props { analysisId: string; }

export default function RecommendationsSection({ analysisId }: Props) {
    const queryClient = useQueryClient();

    const { data: recommendations, isLoading: isLoadingRecommendations } = useQuery({
        queryKey: ['recommendations', analysisId],
        queryFn: () => getByAnalysisId(analysisId)
    });

    const { data: icas, isLoading: isLoadingIcas } = useQuery({
        queryKey: ['inadequate-control-actions', analysisId],
        queryFn: () => getIcasByAnalysisId(analysisId)
    });

    const { data: systemicFactors, isLoading: isLoadingSystemicFactors } = useQuery({
        queryKey: ['systemic-factors', analysisId],
        queryFn: () => getSystemicFactorsByAnalysisId(analysisId)
    });

    const icaOptions = icas ? icas.map(ica => ({ id: ica.id, code: ica.code, controlActionName: ica.controlActionName })) : [];
    const getIcaLabel = (icaId: string) => {
        const ica = icaOptions.find(i => i.id === icaId);
        return ica ? `${ica.code} — ${ica.controlActionName}` : 'Inadequate Control Action not found';
    };

    const systemicFactorOptions = systemicFactors
        ? systemicFactors.map(factor => ({
            id: factor.id,
            categoryLabel: systemicFactorCategoryToSelectOption(factor.category).label,
            description: factor.description
        }))
        : [];
    const getSystemicFactorLabel = (systemicFactorId: string) => {
        const factor = systemicFactorOptions.find(f => f.id === systemicFactorId);
        return factor ? `${factor.categoryLabel} — ${factor.description}` : 'Systemic Factor not found';
    };

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

    const isLoading = isLoadingRecommendations || isLoadingIcas || isLoadingSystemicFactors;

    return (
        <CastSection
            title="Recommendations"
            tooltipInfo="The final step of CAST is to create an improvement program: generate recommendations that address the identified Inadequate Control Actions (Step 3) and Systemic Factors (Step 4), improving the safety control structure as a whole rather than simply assigning blame (CAST Handbook, Leveson 2019)."
            onAddClick={openCreateModal}
            addButtonLabel="Add Recommendation"
        >
            {isLoading ? (
                <div style={{ color: '#9ca3af', fontSize: '14px', padding: '10px' }}>Loading Recommendations...</div>
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
                                backgroundColor: '#303642',
                                border: '1px solid #4b5563',
                                borderRadius: '8px',
                                gap: '16px'
                            }}
                        >
                            <div style={{ flex: 1 }}>
                                {recommendation.description && (
                                    <p style={{ margin: '0 0 8px 0', fontSize: '14px', color: '#d1d5db', lineHeight: '1.5' }}>
                                        {recommendation.description}
                                    </p>
                                )}

                                {(recommendation.inadequateControlActionIds?.length > 0 || recommendation.systemicFactorIds?.length > 0) && (
                                    <div style={{ display: 'flex', flexWrap: 'wrap', gap: '6px' }}>
                                        {recommendation.inadequateControlActionIds?.map(icaId => (
                                            <span
                                                key={icaId}
                                                style={{ display: 'flex', alignItems: 'center', gap: '4px', backgroundColor: '#1f2937', color: '#9ca3af', padding: '2px 8px', borderRadius: '4px', fontSize: '12px', border: '1px solid #4b5563' }}
                                            >
                                                <BiLinkAlt size={12} /> {getIcaLabel(icaId)}
                                            </span>
                                        ))}
                                        {recommendation.systemicFactorIds?.map(systemicFactorId => (
                                            <span
                                                key={systemicFactorId}
                                                style={{ display: 'flex', alignItems: 'center', gap: '4px', backgroundColor: '#1f2937', color: 'var(--color-yellow)', padding: '2px 8px', borderRadius: '4px', fontSize: '12px', border: '1px solid #4b5563' }}
                                            >
                                                <BiLinkAlt size={12} /> {getSystemicFactorLabel(systemicFactorId)}
                                            </span>
                                        ))}
                                    </div>
                                )}
                            </div>

                            <div style={{ display: 'flex', alignItems: 'center', gap: '12px' }}>
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
                icaOptions={icaOptions}
                systemicFactorOptions={systemicFactorOptions}
                recommendation={editingRecommendation}
                isLoading={isCreating || isUpdating}
                onCreate={async data => { await create(data); }}
                onUpdate={async (id, data) => { await update({ id, data }); }}
            />
        </CastSection>
    );
}

import { useState } from 'react';
import { BiTrash, BiPencil, BiMicrochip, BiQuestionMark, BiShow } from 'react-icons/bi';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { useNavigate } from 'react-router-dom';
import { toast } from 'sonner';
import { getComponents } from '@http/Step2/Components';
import { getResponsibilities } from '@http/Step2/Responsibilities';
import { componentsToSelectOptions } from '@interfaces/IStep2/IComponent';
import {
    getByAnalysisId,
    createControlActionContribution,
    updateControlActionContribution,
    deleteControlActionContribution
} from '@http/CAST/Step3/ControlActionContributions';
import {
    icaTypeToSelectOption,
    IControlActionContributionReadDto
} from '@interfaces/CAST/IStep3/IControlActionContribution';
import CastSection from '@components/CastSection';
import ModalControlActionContribution from '@components/Modal/ModalEntity/ModalStep3/ModalControlActionContribution';
import TraceabilityPreviewModal from '@components/Modal/ModalTraceability/TraceabilityPreviewModal';
import styles from '../../Step1/CastStepOne.module.css';

interface Props { analysisId: string; }

export default function ControlActionContributionsSection({ analysisId }: Props) {
    const queryClient = useQueryClient();
    const navigate = useNavigate();
    const [previewCac, setPreviewCac] = useState<IControlActionContributionReadDto | null>(null);

    const { data: cacs, isLoading: isLoadingCacs } = useQuery({
        queryKey: ['control-action-contributions', analysisId],
        queryFn: () => getByAnalysisId(analysisId)
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

    const { data: previewResponsibilities } = useQuery({
        queryKey: ['component-responsibilities', previewCac?.componentId],
        queryFn: () => getResponsibilities(previewCac!.componentId),
        enabled: !!previewCac?.componentId
    });

    const [modalOpen, setModalOpen] = useState(false);
    const [editingCac, setEditingCac] = useState<IControlActionContributionReadDto | null>(null);

    const openCreateModal = () => {
        setEditingCac(null);
        setModalOpen(true);
    };
    const openEditModal = (cac: IControlActionContributionReadDto) => {
        setEditingCac(cac);
        setModalOpen(true);
    };
    const closeModal = () => setModalOpen(false);

    const { mutateAsync: create, isPending: isCreating } = useMutation({
        mutationFn: createControlActionContribution,
        onSuccess: () => {
            toast.success("CAC added successfully!");
            queryClient.invalidateQueries({ queryKey: ['control-action-contributions', analysisId] });
        },
        onError: (err: any) => toast.error(err.message || "Error saving CAC.")
    });

    const { mutateAsync: update, isPending: isUpdating } = useMutation({
        mutationFn: ({ id, data }: { id: string; data: Parameters<typeof updateControlActionContribution>[1] }) =>
            updateControlActionContribution(id, data),
        onSuccess: () => {
            toast.success("CAC updated successfully!");
            queryClient.invalidateQueries({ queryKey: ['control-action-contributions', analysisId] });
        },
        onError: (err: any) => toast.error(err.message || "Error updating CAC.")
    });

    const { mutateAsync: remove } = useMutation({
        mutationFn: async (id: string) => {
            if (window.confirm("Are you sure you want to delete this CAC?")) {
                return await deleteControlActionContribution(id);
            }
            return Promise.reject(new Error("Cancelled"));
        },
        onSuccess: () => {
            toast.success("CAC removed.");
            queryClient.invalidateQueries({ queryKey: ['control-action-contributions', analysisId] });
        },
        onError: (err: any) => {
            if (err.message !== "Cancelled") toast.error(err.message);
        }
    });

    return (
        <>
        <CastSection
            title="Control Action Contribution (CAC)"
            tooltipInfo="For each control action taken during the accident, identify how it was inadequate: not provided, provided unsafely, provided at the wrong time or in the wrong sequence, or stopped too soon / applied too long."
            onAddClick={openCreateModal}
            addButtonLabel="Add CAC"
        >
            {isLoadingCacs || isLoadingComponents ? (
                <div style={{ color: 'var(--color-muted-text)', fontSize: '14px', padding: '10px' }}>Loading CACs...</div>
            ) : cacs && cacs.length > 0 ? (
                <div style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
                    {cacs.map(cac => (
                        <div
                            key={cac.id}
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
                                    <span style={{ color: 'var(--color-yellow)', fontWeight: 600, fontSize: '14px' }}>
                                        {cac.code}
                                    </span>
                                    <strong style={{ color: '#ffffff', fontSize: '15px' }}>{cac.controlActionName}</strong>
                                    <span style={{ backgroundColor: 'var(--color-dark)', color: 'var(--color-muted-text)', padding: '2px 8px', borderRadius: '4px', fontSize: '12px', border: '1px solid var(--color-gray)' }}>
                                        {icaTypeToSelectOption(cac.type).label}
                                    </span>
                                    <span style={{ display: 'flex', alignItems: 'center', gap: '4px', color: 'var(--color-muted-text)', fontSize: '12px' }}>
                                        <BiMicrochip size={14} color="#fb923c" /> {getComponentLabel(cac.componentId)}
                                    </span>
                                </div>

                                {cac.description && (
                                    <p style={{ margin: '0 0 6px 0', fontSize: '14px', color: '#d1d5db', lineHeight: '1.5' }}>
                                        {cac.description}
                                    </p>
                                )}
                                {cac.context && (
                                    <p style={{ margin: '0 0 4px 0', fontSize: '13px', color: 'var(--color-muted-text)', lineHeight: '1.5' }}>
                                        <strong style={{ color: '#d1d5db' }}>Context / Contextual Factors: </strong>{cac.context}
                                    </p>
                                )}
                                {cac.processModelFlaw && (
                                    <p style={{ margin: '0 0 4px 0', fontSize: '13px', color: 'var(--color-muted-text)', lineHeight: '1.5' }}>
                                        <strong style={{ color: '#d1d5db' }}>Process Model Flaw: </strong>{cac.processModelFlaw}
                                    </p>
                                )}
                                {cac.questions && (
                                    <div style={{ marginTop: '8px', backgroundColor: 'var(--color-dark-gray)', padding: '10px 12px', borderRadius: '6px', borderLeft: '3px solid var(--color-yellow)', display: 'flex', gap: '8px', alignItems: 'flex-start' }}>
                                        <BiQuestionMark size={18} color="var(--color-yellow)" style={{ marginTop: '2px', flexShrink: 0 }} />
                                        <p style={{ margin: 0, fontSize: '13px', color: '#d1d5db', fontStyle: 'italic' }}>
                                            {cac.questions}
                                        </p>
                                    </div>
                                )}
                            </div>

                            <div style={{ display: 'flex', alignItems: 'center', gap: '12px' }}>
                                <button
                                    onClick={() => setPreviewCac(cac)}
                                    style={{ background: 'none', border: 'none', color: 'var(--color-muted-text)', cursor: 'pointer', padding: '4px' }}
                                    title="View traceability"
                                >
                                    <BiShow size={18} />
                                </button>
                                <button
                                    onClick={() => openEditModal(cac)}
                                    style={{ background: 'none', border: 'none', color: '#60a5fa', cursor: 'pointer', padding: '4px' }}
                                    title="Edit CAC"
                                >
                                    <BiPencil size={18} />
                                </button>
                                <button
                                    onClick={() => remove(cac.id)}
                                    style={{ background: 'none', border: 'none', color: '#ef4444', cursor: 'pointer', padding: '4px' }}
                                    title="Delete CAC"
                                >
                                    <BiTrash size={18} />
                                </button>
                            </div>
                        </div>
                    ))}
                </div>
            ) : (
                <div className={styles.emptyState}>No CACs added yet.</div>
            )}

            <ModalControlActionContribution
                open={modalOpen}
                onClose={closeModal}
                analysisId={analysisId}
                componentOptions={componentOptions}
                cac={editingCac}
                isLoading={isCreating || isUpdating}
                onCreate={async data => { await create(data); }}
                onUpdate={async (id, data) => { await update({ id, data }); }}
            />
        </CastSection>

        {previewCac && (
            <TraceabilityPreviewModal
                open={!!previewCac}
                onClose={() => setPreviewCac(null)}
                title="CAC Details"
                code={previewCac.code}
                name={previewCac.controlActionName}
                description={previewCac.description}
                relatedGroups={[
                    {
                        label: 'Target Component',
                        emptyMessage: 'Component not found.',
                        items: getComponent(previewCac.componentId)
                            ? [{
                                id: previewCac.componentId,
                                code: getComponent(previewCac.componentId)!.code,
                                name: getComponent(previewCac.componentId)!.name,
                                onGoTo: () => navigate(`/analyses/${analysisId}/cast/step2/component/${previewCac.componentId}`)
                            }]
                            : []
                    },
                    {
                        label: 'Safety-Related Responsibilities (Step 2)',
                        emptyMessage: 'No Safety-Related Responsibilities registered for this component in Step 2 yet.',
                        items: (previewResponsibilities ?? []).map(resp => ({
                            id: resp.id,
                            code: resp.code,
                            name: resp.responsibility,
                            onGoTo: () => navigate(`/analyses/${analysisId}/cast/step2/component/${previewCac.componentId}`)
                        }))
                    }
                ]}
            />
        )}
        </>
    );
}

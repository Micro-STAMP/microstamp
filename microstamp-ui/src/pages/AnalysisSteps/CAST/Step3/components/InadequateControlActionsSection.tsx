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
    createInadequateControlAction,
    updateInadequateControlAction,
    deleteInadequateControlAction
} from '@http/CAST/Step3/InadequateControlActions';
import {
    icaTypeToSelectOption,
    IInadequateControlActionReadDto
} from '@interfaces/CAST/IStep3/IInadequateControlAction';
import CastSection from '@components/CastSection';
import ModalInadequateControlAction from '@components/Modal/ModalEntity/ModalStep3/ModalInadequateControlAction';
import TraceabilityPreviewModal from '@components/Modal/ModalTraceability/TraceabilityPreviewModal';
import styles from '../../Step1/CastStepOne.module.css';

interface Props { analysisId: string; }

export default function InadequateControlActionsSection({ analysisId }: Props) {
    const queryClient = useQueryClient();
    const navigate = useNavigate();
    const [previewIca, setPreviewIca] = useState<IInadequateControlActionReadDto | null>(null);

    const { data: icas, isLoading: isLoadingIcas } = useQuery({
        queryKey: ['inadequate-control-actions', analysisId],
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
        queryKey: ['component-responsibilities', previewIca?.componentId],
        queryFn: () => getResponsibilities(previewIca!.componentId),
        enabled: !!previewIca?.componentId
    });

    const [modalOpen, setModalOpen] = useState(false);
    const [editingIca, setEditingIca] = useState<IInadequateControlActionReadDto | null>(null);

    const openCreateModal = () => {
        setEditingIca(null);
        setModalOpen(true);
    };
    const openEditModal = (ica: IInadequateControlActionReadDto) => {
        setEditingIca(ica);
        setModalOpen(true);
    };
    const closeModal = () => setModalOpen(false);

    const { mutateAsync: create, isPending: isCreating } = useMutation({
        mutationFn: createInadequateControlAction,
        onSuccess: () => {
            toast.success("Component Analysis added successfully!");
            queryClient.invalidateQueries({ queryKey: ['inadequate-control-actions', analysisId] });
        },
        onError: (err: any) => toast.error(err.message || "Error saving Component Analysis.")
    });

    const { mutateAsync: update, isPending: isUpdating } = useMutation({
        mutationFn: ({ id, data }: { id: string; data: Parameters<typeof updateInadequateControlAction>[1] }) =>
            updateInadequateControlAction(id, data),
        onSuccess: () => {
            toast.success("Component Analysis updated successfully!");
            queryClient.invalidateQueries({ queryKey: ['inadequate-control-actions', analysisId] });
        },
        onError: (err: any) => toast.error(err.message || "Error updating Component Analysis.")
    });

    const { mutateAsync: remove } = useMutation({
        mutationFn: async (id: string) => {
            if (window.confirm("Are you sure you want to delete this Component Analysis?")) {
                return await deleteInadequateControlAction(id);
            }
            return Promise.reject(new Error("Cancelled"));
        },
        onSuccess: () => {
            toast.success("Component Analysis removed.");
            queryClient.invalidateQueries({ queryKey: ['inadequate-control-actions', analysisId] });
        },
        onError: (err: any) => {
            if (err.message !== "Cancelled") toast.error(err.message);
        }
    });

    return (
        <>
        <CastSection
            title="Component Analysis"
            tooltipInfo="For each control action taken during the accident, identify how it was inadequate: not provided, provided unsafely, provided at the wrong time or in the wrong sequence, or stopped too soon / applied too long."
            onAddClick={openCreateModal}
            addButtonLabel="Add Component Analysis"
        >
            {isLoadingIcas || isLoadingComponents ? (
                <div style={{ color: 'var(--color-muted-text)', fontSize: '14px', padding: '10px' }}>Loading Component Analysis...</div>
            ) : icas && icas.length > 0 ? (
                <div style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
                    {icas.map(ica => (
                        <div
                            key={ica.id}
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
                                        {ica.code}
                                    </span>
                                    <strong style={{ color: '#ffffff', fontSize: '15px' }}>{ica.controlActionName}</strong>
                                    <span style={{ backgroundColor: 'var(--color-dark)', color: 'var(--color-muted-text)', padding: '2px 8px', borderRadius: '4px', fontSize: '12px', border: '1px solid var(--color-gray)' }}>
                                        {icaTypeToSelectOption(ica.type).label}
                                    </span>
                                    <span style={{ display: 'flex', alignItems: 'center', gap: '4px', color: 'var(--color-muted-text)', fontSize: '12px' }}>
                                        <BiMicrochip size={14} color="#fb923c" /> {getComponentLabel(ica.componentId)}
                                    </span>
                                </div>

                                {ica.description && (
                                    <p style={{ margin: '0 0 6px 0', fontSize: '14px', color: '#d1d5db', lineHeight: '1.5' }}>
                                        {ica.description}
                                    </p>
                                )}
                                {ica.context && (
                                    <p style={{ margin: '0 0 4px 0', fontSize: '13px', color: 'var(--color-muted-text)', lineHeight: '1.5' }}>
                                        <strong style={{ color: '#d1d5db' }}>Context / Contextual Factors: </strong>{ica.context}
                                    </p>
                                )}
                                {ica.processModelFlaw && (
                                    <p style={{ margin: '0 0 4px 0', fontSize: '13px', color: 'var(--color-muted-text)', lineHeight: '1.5' }}>
                                        <strong style={{ color: '#d1d5db' }}>Process Model Flaw: </strong>{ica.processModelFlaw}
                                    </p>
                                )}
                                {ica.questions && (
                                    <div style={{ marginTop: '8px', backgroundColor: 'var(--color-dark-gray)', padding: '10px 12px', borderRadius: '6px', borderLeft: '3px solid var(--color-yellow)', display: 'flex', gap: '8px', alignItems: 'flex-start' }}>
                                        <BiQuestionMark size={18} color="var(--color-yellow)" style={{ marginTop: '2px', flexShrink: 0 }} />
                                        <p style={{ margin: 0, fontSize: '13px', color: '#d1d5db', fontStyle: 'italic' }}>
                                            {ica.questions}
                                        </p>
                                    </div>
                                )}
                            </div>

                            <div style={{ display: 'flex', alignItems: 'center', gap: '12px' }}>
                                <button
                                    onClick={() => setPreviewIca(ica)}
                                    style={{ background: 'none', border: 'none', color: 'var(--color-muted-text)', cursor: 'pointer', padding: '4px' }}
                                    title="View traceability"
                                >
                                    <BiShow size={18} />
                                </button>
                                <button
                                    onClick={() => openEditModal(ica)}
                                    style={{ background: 'none', border: 'none', color: '#60a5fa', cursor: 'pointer', padding: '4px' }}
                                    title="Edit Component Analysis"
                                >
                                    <BiPencil size={18} />
                                </button>
                                <button
                                    onClick={() => remove(ica.id)}
                                    style={{ background: 'none', border: 'none', color: '#ef4444', cursor: 'pointer', padding: '4px' }}
                                    title="Delete Component Analysis"
                                >
                                    <BiTrash size={18} />
                                </button>
                            </div>
                        </div>
                    ))}
                </div>
            ) : (
                <div className={styles.emptyState}>No Component Analysis added yet.</div>
            )}

            <ModalInadequateControlAction
                open={modalOpen}
                onClose={closeModal}
                analysisId={analysisId}
                componentOptions={componentOptions}
                ica={editingIca}
                isLoading={isCreating || isUpdating}
                onCreate={async data => { await create(data); }}
                onUpdate={async (id, data) => { await update({ id, data }); }}
            />
        </CastSection>

        {previewIca && (
            <TraceabilityPreviewModal
                open={!!previewIca}
                onClose={() => setPreviewIca(null)}
                title="Component Analysis Details"
                code={previewIca.code}
                name={previewIca.controlActionName}
                description={previewIca.description}
                relatedGroups={[
                    {
                        label: 'Target Component',
                        emptyMessage: 'Component not found.',
                        items: getComponent(previewIca.componentId)
                            ? [{
                                id: previewIca.componentId,
                                code: getComponent(previewIca.componentId)!.code,
                                name: getComponent(previewIca.componentId)!.name,
                                onGoTo: () => navigate(`/analyses/${analysisId}/cast/step2/component/${previewIca.componentId}`)
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
                            onGoTo: () => navigate(`/analyses/${analysisId}/cast/step2/component/${previewIca.componentId}`)
                        }))
                    }
                ]}
            />
        )}
        </>
    );
}

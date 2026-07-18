import { useState } from 'react';
import { BiTrash, BiPencil, BiMicrochip } from 'react-icons/bi';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { toast } from 'sonner';
import { getComponents } from '@http/Step2/Components';
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
import styles from '../../Step1/CastStepOne.module.css';

interface Props { analysisId: string; }

export default function InadequateControlActionsSection({ analysisId }: Props) {
    const queryClient = useQueryClient();

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
            toast.success("Inadequate Control Action added successfully!");
            queryClient.invalidateQueries({ queryKey: ['inadequate-control-actions', analysisId] });
        },
        onError: (err: any) => toast.error(err.message || "Error saving Inadequate Control Action.")
    });

    const { mutateAsync: update, isPending: isUpdating } = useMutation({
        mutationFn: ({ id, data }: { id: string; data: Parameters<typeof updateInadequateControlAction>[1] }) =>
            updateInadequateControlAction(id, data),
        onSuccess: () => {
            toast.success("Inadequate Control Action updated successfully!");
            queryClient.invalidateQueries({ queryKey: ['inadequate-control-actions', analysisId] });
        },
        onError: (err: any) => toast.error(err.message || "Error updating Inadequate Control Action.")
    });

    const { mutateAsync: remove } = useMutation({
        mutationFn: async (id: string) => {
            if (window.confirm("Are you sure you want to delete this Inadequate Control Action?")) {
                return await deleteInadequateControlAction(id);
            }
            return Promise.reject(new Error("Cancelled"));
        },
        onSuccess: () => {
            toast.success("Inadequate Control Action removed.");
            queryClient.invalidateQueries({ queryKey: ['inadequate-control-actions', analysisId] });
        },
        onError: (err: any) => {
            if (err.message !== "Cancelled") toast.error(err.message);
        }
    });

    return (
        <CastSection
            title="Inadequate Control Actions"
            tooltipInfo="For each control action taken during the accident, identify how it was inadequate: not provided, provided unsafely, provided at the wrong time or in the wrong sequence, or stopped too soon / applied too long."
            onAddClick={openCreateModal}
            addButtonLabel="Add ICA"
        >
            {isLoadingIcas || isLoadingComponents ? (
                <div style={{ color: '#9ca3af', fontSize: '14px', padding: '10px' }}>Loading Inadequate Control Actions...</div>
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
                                backgroundColor: '#303642',
                                border: '1px solid #4b5563',
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
                                    <span style={{ backgroundColor: '#1f2937', color: '#9ca3af', padding: '2px 8px', borderRadius: '4px', fontSize: '12px', border: '1px solid #4b5563' }}>
                                        {icaTypeToSelectOption(ica.type).label}
                                    </span>
                                    <span style={{ display: 'flex', alignItems: 'center', gap: '4px', color: '#9ca3af', fontSize: '12px' }}>
                                        <BiMicrochip size={14} color="#fb923c" /> {getComponentLabel(ica.componentId)}
                                    </span>
                                </div>

                                {ica.description && (
                                    <p style={{ margin: '0 0 6px 0', fontSize: '14px', color: '#d1d5db', lineHeight: '1.5' }}>
                                        {ica.description}
                                    </p>
                                )}
                                {ica.context && (
                                    <p style={{ margin: '0 0 4px 0', fontSize: '13px', color: '#9ca3af', lineHeight: '1.5' }}>
                                        <strong style={{ color: '#d1d5db' }}>Context: </strong>{ica.context}
                                    </p>
                                )}
                                {ica.processModelFlaw && (
                                    <p style={{ margin: 0, fontSize: '13px', color: '#9ca3af', lineHeight: '1.5' }}>
                                        <strong style={{ color: '#d1d5db' }}>Process Model Flaw: </strong>{ica.processModelFlaw}
                                    </p>
                                )}
                            </div>

                            <div style={{ display: 'flex', alignItems: 'center', gap: '12px' }}>
                                <button
                                    onClick={() => openEditModal(ica)}
                                    style={{ background: 'none', border: 'none', color: '#60a5fa', cursor: 'pointer', padding: '4px' }}
                                    title="Edit Inadequate Control Action"
                                >
                                    <BiPencil size={18} />
                                </button>
                                <button
                                    onClick={() => remove(ica.id)}
                                    style={{ background: 'none', border: 'none', color: '#ef4444', cursor: 'pointer', padding: '4px' }}
                                    title="Delete Inadequate Control Action"
                                >
                                    <BiTrash size={18} />
                                </button>
                            </div>
                        </div>
                    ))}
                </div>
            ) : (
                <div className={styles.emptyState}>No Inadequate Control Actions added yet.</div>
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
    );
}

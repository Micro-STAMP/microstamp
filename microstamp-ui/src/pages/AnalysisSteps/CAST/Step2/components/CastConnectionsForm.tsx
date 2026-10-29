import { useState } from 'react';
import { BiSave, BiTransferAlt, BiRightArrowAlt, BiX } from 'react-icons/bi';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { useNavigate } from 'react-router-dom';
import { toast } from 'sonner';
import { getComponents } from '@http/Step2/Components';
import { getConnections, createConnection, updateConnection, deleteConnection } from '@http/Step2/Connections';
import { createInteraction, updateInteraction, deleteInteraction } from '@http/Step2/Interactions';
import { IConnectionReadDto, IInteractionType } from '@interfaces/IStep2';
import { IConnectionStyle } from '@interfaces/IStep2/IConnection';
import { SelectOption } from '@components/FormField/Templates';

import CastSection from '@components/CastSection';
import CastListInput from '@components/CastListInput';
import CastItemActions from '@components/CastItemActions';
import { useCastConfirm } from '@components/CastConfirm';
import Select from '@components/FormField/Select';
import TraceabilityPreviewModal from '@components/Modal/ModalTraceability/TraceabilityPreviewModal';
import { nextCode } from '@utils/castCodes';
import { cleanList } from '@utils/castList';
import styles from '../../Step1/CastStepOne.module.css';

interface Props { analysisId: string; }

const EMPTY_FORM = {
    code: '',
    sourceId: '',
    targetId: '',
    type: IInteractionType.CONTROL_ACTION,
    actions: [] as string[]
};

const codePrefix = (type: IInteractionType) => (type === IInteractionType.FEEDBACK ? 'FB' : 'CA');

const interactionCode = (connectionCode: string, index: number) => `${connectionCode}.${index + 1}`;

export default function CastConnectionsForm({ analysisId }: Props) {
    const queryClient = useQueryClient();
    const navigate = useNavigate();
    const { confirmDelete, confirmDialog } = useCastConfirm();
    const [previewConnection, setPreviewConnection] = useState<IConnectionReadDto | null>(null);

    const { data: availableComponents, isLoading: loadingComps } = useQuery({
        queryKey: ['analysis-components', analysisId],
        queryFn: () => getComponents(analysisId)
    });

    const { data: connections, isLoading: loadingConns } = useQuery({
        queryKey: ['analysis-connections', analysisId],
        queryFn: () => getConnections(analysisId)
    });

    const [form, setForm] = useState(EMPTY_FORM);
    const [editingConnection, setEditingConnection] = useState<IConnectionReadDto | null>(null);
    const code = form.code || nextCode(codePrefix(form.type), (connections ?? []).map(c => c.code));

    const resetForm = () => {
        setForm(EMPTY_FORM);
        setEditingConnection(null);
    };

    const invalidate = () => {
        queryClient.invalidateQueries({ queryKey: ['analysis-connections', analysisId] });
        queryClient.invalidateQueries({ queryKey: ['component-dependencies'] });
    };

    const { mutateAsync: save, isPending } = useMutation({
        mutationFn: async () => {
            const connectionCode = code.trim();
            const actions = cleanList(form.actions);
            const connectionData = {
                code: connectionCode,
                sourceId: form.sourceId,
                targetId: form.targetId,
                style: editingConnection?.style ?? IConnectionStyle.SOLID
            };

            const connection = editingConnection
                ? (await updateConnection(editingConnection.id, connectionData), editingConnection)
                : await createConnection({ ...connectionData, analysisId });

            const existing = editingConnection?.interactions ?? [];
            for (let i = 0; i < actions.length; i++) {
                const interaction = { name: actions[i], code: interactionCode(connectionCode, i), interactionType: form.type };
                if (existing[i]) await updateInteraction(existing[i].id, interaction);
                else await createInteraction({ ...interaction, connectionId: connection.id });
            }
            for (const extra of existing.slice(actions.length)) {
                await deleteInteraction(extra.id);
            }
        },
        onSuccess: () => {
            toast.success(editingConnection ? "Connection updated!" : "Connection and Interactions added!");
            resetForm();
            invalidate();
        },
        onError: (err: any) => toast.error(err.message || "Error saving Connection.")
    });

    const { mutateAsync: remove } = useMutation({
        mutationFn: (id: string) => deleteConnection(id),
        onSuccess: (_, id) => {
            toast.success("Connection removed.");
            if (editingConnection?.id === id) resetForm();
            invalidate();
        },
        onError: (err: any) => toast.error(err.message || "Error deleting Connection.")
    });

    const getComponent = (id: string) => availableComponents?.find(c => c.id === id) || null;
    const getComponentName = (id: string) => getComponent(id)?.name || 'Unknown';
    const connectionType = (conn: IConnectionReadDto) => conn.interactions?.[0]?.interactionType ?? IInteractionType.CONTROL_ACTION;

    const startEdit = (conn: IConnectionReadDto) => {
        setEditingConnection(conn);
        setForm({
            code: conn.code,
            sourceId: conn.source.id,
            targetId: conn.target.id,
            type: connectionType(conn),
            actions: (conn.interactions ?? []).map(i => i.name)
        });
    };

    const handleDelete = async (conn: IConnectionReadDto) => {
        if (await confirmDelete(`connection ${conn.code}`, 'All actions/feedback of this connection will also be removed.')) await remove(conn.id);
    };

    const componentSelectOptions: SelectOption[] = (availableComponents ?? []).map(c => ({ label: `${c.code} - ${c.name}`, value: c.id }));
    const interactionTypeOptions: SelectOption[] = [
        { label: 'Control Action (➔)', value: IInteractionType.CONTROL_ACTION },
        { label: 'Feedback (🡐)', value: IInteractionType.FEEDBACK }
    ];
    const isFeedbackForm = form.type === IInteractionType.FEEDBACK;
    const hasActions = cleanList(form.actions).length > 0;

    return (
        <>
        <CastSection title="Connections & Interactions" tooltipInfo="Define how components interact: the control actions a controller sends down to a controlled process and the feedback that flows back up. For each connection, list every specific command or piece of information that flows between the two components." hideAddButton={true}>
            {loadingComps || loadingConns ? (
                <div style={{ color: 'var(--color-muted-text)', padding: '10px' }}>Loading data...</div>
            ) : (
                <div style={{ display: 'flex', flexDirection: 'column', gap: '24px' }}>

                    {connections && connections.length > 0 && (
                        <div style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
                            {connections.map(conn => {
                                const interactions = conn.interactions || [];
                                const isFeedback = connectionType(conn) === IInteractionType.FEEDBACK;
                                const color = isFeedback ? '#60a5fa' : '#f97316';

                                return (
                                    <div key={conn.id} style={{ display: 'flex', flexDirection: 'column', gap: '12px', backgroundColor: 'var(--color-dark-gray)', padding: '16px', borderRadius: '8px', border: `1px solid ${conn.id === editingConnection?.id ? 'var(--color-yellow)' : 'var(--color-gray)'}` }}>
                                        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                                            <div style={{ display: 'flex', alignItems: 'center', gap: '16px', flex: 1 }}>
                                                <div style={{ backgroundColor: 'var(--color-dark)', color: '#fff', padding: '8px 16px', borderRadius: '4px', border: '1px solid var(--color-gray)', flex: 1, textAlign: 'center', fontWeight: 'bold', fontSize: '15px' }}>
                                                    {getComponentName(conn.source.id)}
                                                </div>
                                                <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', color, width: '120px' }}>
                                                    <span style={{ fontSize: '10px', fontWeight: 'bold', letterSpacing: '1px' }}>{conn.code}</span>
                                                    <div style={{ display: 'flex', alignItems: 'center' }}>
                                                        {isFeedback && <BiRightArrowAlt size={24} style={{ transform: 'rotate(180deg)' }} />}
                                                        <div style={{ height: '2px', width: '60px', backgroundColor: color }}></div>
                                                        {!isFeedback && <BiRightArrowAlt size={24} />}
                                                    </div>
                                                    <span style={{ fontSize: '10px', color: 'var(--color-muted-text)' }}>{isFeedback ? 'FEEDBACK' : 'CONTROL ACTION'}</span>
                                                </div>
                                                <div style={{ backgroundColor: 'var(--color-dark)', color: '#fff', padding: '8px 16px', borderRadius: '4px', border: '1px solid var(--color-gray)', flex: 1, textAlign: 'center', fontWeight: 'bold', fontSize: '15px' }}>
                                                    {getComponentName(conn.target.id)}
                                                </div>
                                            </div>

                                            <div style={{ marginLeft: '16px' }}>
                                                <CastItemActions itemLabel="Connection" onPreview={() => setPreviewConnection(conn)} onEdit={() => startEdit(conn)} onDelete={() => handleDelete(conn)} />
                                            </div>
                                        </div>

                                        {interactions.length > 0 && (
                                            <div style={{ display: 'flex', alignItems: 'center', gap: '6px', flexWrap: 'wrap' }}>
                                                <span style={{ fontSize: '12px', color: 'var(--color-muted-text)', marginRight: '4px' }}>Actions / Feedback:</span>
                                                {interactions.map(interaction => (
                                                    <span key={interaction.id} title={interaction.code} style={{ fontSize: '12px', color: '#e5e7eb', backgroundColor: 'var(--color-dark)', border: `1px solid ${color}`, borderRadius: '12px', padding: '2px 10px' }}>
                                                        {interaction.name}
                                                    </span>
                                                ))}
                                            </div>
                                        )}
                                    </div>
                                );
                            })}
                        </div>
                    )}

                    <div style={{ backgroundColor: 'var(--color-dark)', padding: '16px', borderRadius: '8px', border: '1px dashed var(--color-gray)', display: 'flex', flexDirection: 'column', gap: '16px' }}>
                        <div style={{ display: 'flex', alignItems: 'center', gap: '8px', color: '#d1d5db' }}>
                            <BiTransferAlt size={20} />
                            <h4 style={{ margin: 0, fontSize: '14px' }}>{editingConnection ? `Edit Connection ${code}` : 'Add New Interaction'}</h4>
                        </div>

                        <div style={{ display: 'grid', gridTemplateColumns: '80px 1fr 1fr 1fr', gap: '16px', alignItems: 'end' }}>
                            <div>
                                <label className={styles.inputLabel}>Code</label>
                                <input type="text" className={styles.inputField} value={code} onChange={e => setForm({ ...form, code: e.target.value })} style={{ marginBottom: 0 }} />
                            </div>
                            <div>
                                <Select
                                    label="Source"
                                    options={componentSelectOptions}
                                    value={componentSelectOptions.find(o => o.value === form.sourceId) || null}
                                    onChange={opt => setForm({ ...form, sourceId: opt?.value || '' })}
                                />
                            </div>
                            <div>
                                <Select
                                    label="Type"
                                    options={interactionTypeOptions}
                                    value={interactionTypeOptions.find(o => o.value === form.type) || null}
                                    onChange={opt => setForm({ ...form, type: (opt?.value as IInteractionType) ?? IInteractionType.CONTROL_ACTION })}
                                    required
                                />
                            </div>
                            <div>
                                <Select
                                    label="Target"
                                    options={componentSelectOptions}
                                    value={componentSelectOptions.find(o => o.value === form.targetId) || null}
                                    onChange={opt => setForm({ ...form, targetId: opt?.value || '' })}
                                />
                            </div>
                        </div>

                        <CastListInput
                            label="Actions / Feedback"
                            items={form.actions}
                            onChange={actions => setForm({ ...form, actions })}
                            placeholder={isFeedbackForm ? 'E.g., Infusion pump alarm, Lab results' : 'E.g., Prescribe dosage, Release brakes'}
                            addLabel={isFeedbackForm ? 'Add feedback' : 'Add control action'}
                            helperText={isFeedbackForm
                                ? 'Each piece of information the target receives back from the source.'
                                : 'Each command the source (controller) can send to the target.'}
                            helperClassName={styles.inputLabel}
                        />

                        <div style={{ display: 'flex', justifyContent: 'flex-end', gap: '8px', marginTop: '8px' }}>
                            {editingConnection && (
                                <button onClick={resetForm} className={styles.btnBack} type="button">
                                    <BiX size={18} /> Cancel
                                </button>
                            )}
                            <button onClick={() => save()} disabled={isPending || !form.sourceId || !form.targetId || !code.trim() || !hasActions} className={styles.btnSaveForm}>
                                <BiSave size={18} /> {isPending ? 'Saving...' : editingConnection ? 'Save Changes' : 'Add Connection'}
                            </button>
                        </div>
                    </div>

                </div>
            )}
            {confirmDialog}
        </CastSection>

        {previewConnection && (
            <TraceabilityPreviewModal
                open={!!previewConnection}
                onClose={() => setPreviewConnection(null)}
                title="Interaction Details"
                code={previewConnection.code}
                description={(previewConnection.interactions ?? []).map(i => `• ${i.name}`).join('\n') || undefined}
                relatedGroups={[
                    {
                        label: 'Linked Components',
                        emptyMessage: 'No components found.',
                        items: [previewConnection.source.id, previewConnection.target.id]
                            .map((componentId, index) => {
                                const comp = getComponent(componentId);
                                return comp && {
                                    id: comp.id,
                                    code: comp.code,
                                    name: `${comp.name} (${index === 0 ? 'Source' : 'Target'})`,
                                    onGoTo: () => navigate(`/analyses/${analysisId}/cast/step2/component/${comp.id}`)
                                };
                            })
                            .filter((item): item is NonNullable<typeof item> => !!item)
                    }
                ]}
            />
        )}
        </>
    );
}

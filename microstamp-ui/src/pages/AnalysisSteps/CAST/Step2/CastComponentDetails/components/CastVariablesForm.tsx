import { useState } from 'react';
import { BiData, BiPlus, BiSave, BiX } from 'react-icons/bi';
import { toast } from 'sonner';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { getVariablesByComponentId, createVariable, updateVariable, deleteVariable } from '@http/Step2/Variables';
import { createState, updateState, deleteState } from '@http/Step2/States';
import { getComponents } from '@http/Step2/Components';
import { IVariableReadDto } from '@interfaces/IStep2';

import CastSection from '@components/CastSection';
import CastListInput from '@components/CastListInput';
import CastItemActions from '@components/CastItemActions';
import { useCastConfirm } from '@components/CastConfirm';
import { nextCode } from '@utils/castCodes';
import { cleanList } from '@utils/castList';
import styles from '../../../Step1/CastStepOne.module.css';

interface Props { componentId: string; analysisId: string; }

const EMPTY_FORM = { code: '', name: '', states: [] as string[] };

const stateCode = (variableCode: string, index: number) => `${variableCode}.${index + 1}`;

export default function CastVariablesForm({ componentId, analysisId }: Props) {
    const queryClient = useQueryClient();
    const { confirmDelete, confirmDialog } = useCastConfirm();
    const [form, setForm] = useState(EMPTY_FORM);
    const [editingVariable, setEditingVariable] = useState<IVariableReadDto | null>(null);

    const { data: variables } = useQuery({
        queryKey: ['component-variables', componentId],
        queryFn: () => getVariablesByComponentId(componentId)
    });

    const { data: components } = useQuery({
        queryKey: ['analysis-components', analysisId],
        queryFn: () => getComponents(analysisId)
    });

    const allVariableCodes = [
        ...(components ?? []).flatMap(c => (c.variables ?? []).map(v => v.code)),
        ...(variables ?? []).map(v => v.code)
    ];
    const code = form.code || nextCode('PM', allVariableCodes);

    const resetForm = () => {
        setForm(EMPTY_FORM);
        setEditingVariable(null);
    };

    const invalidate = () => {
        queryClient.invalidateQueries({ queryKey: ['component-variables', componentId] });
        queryClient.invalidateQueries({ queryKey: ['analysis-components', analysisId] });
    };

    const { mutateAsync: save, isPending } = useMutation({
        mutationFn: async () => {
            const variableCode = code.trim();
            const states = cleanList(form.states);
            const variableData = { name: form.name.trim(), code: variableCode };

            const variable = editingVariable
                ? (await updateVariable(editingVariable.id, variableData), editingVariable)
                : await createVariable({ ...variableData, componentId });

            const existing = editingVariable?.states ?? [];
            for (let i = 0; i < states.length; i++) {
                const state = { name: states[i], code: stateCode(variableCode, i) };
                if (existing[i]) await updateState(existing[i].id, state);
                else await createState({ ...state, variableId: variable.id });
            }
            for (const extra of existing.slice(states.length)) {
                await deleteState(extra.id);
            }
        },
        onSuccess: () => {
            toast.success(editingVariable ? "Process Model variable updated!" : "Process Model updated!");
            resetForm();
            invalidate();
        },
        onError: (err: any) => toast.error(err.message || "Error saving Process Model variable.")
    });

    const { mutateAsync: removeVar } = useMutation({
        mutationFn: (id: string) => deleteVariable(id),
        onSuccess: (_, id) => {
            toast.success("Variable removed.");
            if (editingVariable?.id === id) resetForm();
            invalidate();
        },
        onError: (err: any) => toast.error(err.message || "Error deleting variable.")
    });

    const { mutateAsync: removeState } = useMutation({
        mutationFn: (id: string) => deleteState(id),
        onSuccess: () => {
            toast.success("State removed.");
            invalidate();
        },
        onError: (err: any) => toast.error(err.message || "Error deleting state.")
    });

    const startEdit = (variable: IVariableReadDto) => {
        setEditingVariable(variable);
        setForm({ code: variable.code, name: variable.name, states: (variable.states ?? []).map(s => s.name) });
    };

    const handleDelete = async (variable: IVariableReadDto) => {
        if (await confirmDelete(`variable ${variable.code}`, 'All its states will also be removed.')) await removeVar(variable.id);
    };

    return (
        <CastSection title="Process Model (Variables & States)" tooltipInfo="What variables did this controller believe to be true? Each variable lists the possible states the controller could believe the controlled process was in." hideAddButton={true}>
            <div style={{ display: 'flex', flexDirection: 'column', gap: '24px' }}>

                {variables?.map(v => (
                    <div key={v.id} style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', gap: '16px', backgroundColor: 'var(--color-dark-gray)', padding: '16px', borderRadius: '8px', border: `1px solid ${v.id === editingVariable?.id ? 'var(--color-yellow)' : 'var(--color-gray)'}` }}>
                        <div style={{ display: 'flex', flexDirection: 'column', gap: '8px' }}>
                            <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
                                <span style={{ color: 'var(--color-yellow)', fontWeight: 'bold', fontSize: '14px' }}>{v.code}</span>
                                <span style={{ color: '#fff', fontSize: '16px' }}>{v.name}</span>
                            </div>
                            <div style={{ display: 'flex', gap: '8px', flexWrap: 'wrap' }}>
                                {v.states?.map(state => (
                                    <div key={state.id} title={state.code} style={{ backgroundColor: 'var(--color-dark)', border: '1px solid var(--color-gray)', color: '#d1d5db', padding: '2px 8px', borderRadius: '12px', fontSize: '12px', display: 'flex', alignItems: 'center', gap: '4px' }}>
                                        {state.name}
                                        <span onClick={async () => { if (await confirmDelete(`state "${state.name}"`)) removeState(state.id); }} style={{ cursor: 'pointer', color: '#ef4444' }} title="Delete state">×</span>
                                    </div>
                                ))}
                            </div>
                        </div>
                        <CastItemActions itemLabel="Variable" onEdit={() => startEdit(v)} onDelete={() => handleDelete(v)} />
                    </div>
                ))}

                <div style={{ backgroundColor: 'var(--color-dark)', padding: '16px', borderRadius: '8px', border: '1px dashed var(--color-gray)', display: 'flex', flexDirection: 'column', gap: '16px' }}>
                    <div style={{ display: 'flex', alignItems: 'center', gap: '8px', color: '#d1d5db' }}>
                        <BiData size={20} /> <h4 style={{ margin: 0, fontSize: '14px' }}>{editingVariable ? `Edit Variable ${code}` : 'Add Process Model Variable'}</h4>
                    </div>

                    <div style={{ display: 'grid', gridTemplateColumns: '100px 1fr', gap: '16px' }}>
                        <div>
                            <label className={styles.inputLabel}>Code</label>
                            <input className={styles.inputField} style={{ marginBottom: 0 }} value={code} onChange={e => setForm({ ...form, code: e.target.value })} />
                        </div>
                        <div>
                            <label className={styles.inputLabel}>Variable Name</label>
                            <input className={styles.inputField} style={{ marginBottom: 0 }} placeholder="E.g., Patient weight" value={form.name} onChange={e => setForm({ ...form, name: e.target.value })} />
                        </div>
                    </div>

                    <CastListInput
                        label="States"
                        items={form.states}
                        onChange={states => setForm({ ...form, states })}
                        placeholder="E.g., Known, Unknown, Outdated"
                        addLabel="Add state"
                    />

                    <div style={{ display: 'flex', justifyContent: 'flex-end', gap: '8px' }}>
                        {editingVariable && (
                            <button onClick={resetForm} className={styles.btnBack} type="button">
                                <BiX size={18} /> Cancel
                            </button>
                        )}
                        <button onClick={() => save()} disabled={isPending || !code.trim() || !form.name.trim()} className={styles.btnSaveForm}>
                            {editingVariable ? <BiSave size={18} /> : <BiPlus size={18} />} {isPending ? 'Saving...' : editingVariable ? 'Save Changes' : 'Add Variable'}
                        </button>
                    </div>
                </div>
            </div>
            {confirmDialog}
        </CastSection>
    );
}

import { useState } from 'react';
import { BiTrash, BiData, BiPlus } from 'react-icons/bi';
import { toast } from 'sonner';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { getVariablesByComponentId, createVariable, deleteVariable } from '@http/Step2/Variables';
import { createState, deleteState } from '@http/Step2/States';

import CastSection from '@components/CastSection';
import styles from '../../../Step1/CastStepOne.module.css';

interface Props { componentId: string; analysisId: string; }

export default function CastVariablesForm({ componentId }: Props) {
    const queryClient = useQueryClient();
    const [form, setForm] = useState({ code: '', name: '', statesText: '' });

    const { data: variables } = useQuery({
        queryKey: ['component-variables', componentId],
        queryFn: () => getVariablesByComponentId(componentId)
    });

    const { mutateAsync: save, isPending } = useMutation({
        mutationFn: async () => {
            const variable = await createVariable({
                name: form.name,
                code: form.code,
                componentId: componentId
            });

            const states = form.statesText.split(',').map(s => s.trim()).filter(s => s !== "");
            for (const stateName of states) {
                await createState({
                    name: stateName,
                    code: `${variable.code}-${stateName.substring(0, 3).toUpperCase()}`,
                    variableId: variable.id
                });
            }
        },
        onSuccess: () => {
            toast.success("Process Model updated!");
            setForm({ code: '', name: '', statesText: '' });
            queryClient.invalidateQueries({ queryKey: ['component-variables', componentId] });
        }
    });

    const { mutateAsync: removeVar } = useMutation({
        mutationFn: (id: string) => deleteVariable(id),
        onSuccess: () => queryClient.invalidateQueries({ queryKey: ['component-variables', componentId] })
    });

    const { mutateAsync: removeState } = useMutation({
        mutationFn: (id: string) => deleteState(id),
        onSuccess: () => queryClient.invalidateQueries({ queryKey: ['component-variables', componentId] })
    });

    return (
        <CastSection title="Process Model (Variables & States)" tooltipInfo="What variables did this controller believe to be true?" hideAddButton={true}>
            <div style={{ display: 'flex', flexDirection: 'column', gap: '24px' }}>
                
                {variables?.map((v: any) => (
                    <div key={v.id} style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', backgroundColor: '#374151', padding: '16px', borderRadius: '8px', border: '1px solid #4b5563' }}>
                        <div style={{ display: 'flex', flexDirection: 'column', gap: '8px' }}>
                            <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
                                <span style={{ color: 'var(--color-yellow)', fontWeight: 'bold' }}>{v.code}</span>
                                <span style={{ color: '#fff', fontSize: '16px' }}>{v.name}</span>
                            </div>
                            <div style={{ display: 'flex', gap: '8px', flexWrap: 'wrap' }}>
                                {v.states?.map((state: any) => (
                                    <div key={state.id} style={{ backgroundColor: '#1f2937', border: '1px solid #6b7280', color: '#d1d5db', padding: '2px 8px', borderRadius: '12px', fontSize: '12px', display: 'flex', alignItems: 'center', gap: '4px' }}>
                                        {state.name} 
                                        <span onClick={() => removeState(state.id)} style={{ cursor: 'pointer', color: '#ef4444' }}>×</span>
                                    </div>
                                ))}
                            </div>
                        </div>
                        <button onClick={() => removeVar(v.id)} style={{ background: 'transparent', border: 'none', color: '#ef4444', cursor: 'pointer' }}><BiTrash size={18} /></button>
                    </div>
                ))}

                <div style={{ backgroundColor: '#1f2937', padding: '16px', borderRadius: '8px', border: '1px dashed #4b5563', display: 'flex', flexDirection: 'column', gap: '16px' }}>
                    <div style={{ display: 'flex', alignItems: 'center', gap: '8px', color: '#d1d5db' }}>
                        <BiData size={20} /> <h4 style={{ margin: 0, fontSize: '14px' }}>Add Process Model Variable</h4>
                    </div>
                    
                    <div style={{ display: 'grid', gridTemplateColumns: '100px 1fr 1fr', gap: '16px' }}>
                        <input className={styles.inputField} placeholder="PM-1" value={form.code} onChange={e => setForm({ ...form, code: e.target.value })} />
                        <input className={styles.inputField} placeholder="Variable Name" value={form.name} onChange={e => setForm({ ...form, name: e.target.value })} />
                        <input className={styles.inputField} placeholder="States (comma separated)" value={form.statesText} onChange={e => setForm({ ...form, statesText: e.target.value })} />
                    </div>

                    <div style={{ display: 'flex', justifyContent: 'flex-end' }}>
                        <button onClick={() => save()} disabled={isPending || !form.code || !form.name} className={styles.btnSaveForm}>
                            <BiPlus size={18} /> {isPending ? 'Saving...' : 'Add Variable'}
                        </button>
                    </div>
                </div>
            </div>
        </CastSection>
    );
}
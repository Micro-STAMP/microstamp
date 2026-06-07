import { useState } from 'react';
import { BiSave, BiTrash, BiCheckShield } from 'react-icons/bi';
import { toast } from 'sonner';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { useMemo } from 'react';
import { getResponsibilities, createResponsibility, deleteResponsibility } from '@http/Step2/Responsibilities';
import { getSystemSafetyConstraints } from '@http/Step1/SystemSafetyConstraints';
import SystemSafetyConstraintSelect from '@components/FormField/Select/SystemSafetyConstraintSelect';
import { systemSafetyConstraintsToSelectOptions } from '@components/FormField/Select/SystemSafetyConstraintSelect/util';
import CastSection from '@components/CastSection';
import styles from '../../../Step1/CastStepOne.module.css';

interface Props { componentId: string; analysisId: string; }

export default function CastResponsibilitiesForm({ componentId, analysisId }: Props) {
    const queryClient = useQueryClient();
    const [form, setForm] = useState({ code: '', responsibility: '', ssc: null as any });

    const { data: responsibilities } = useQuery({
        queryKey: ['component-responsibilities', componentId],
        queryFn: () => getResponsibilities(componentId)
    });

    const { data: sscData } = useQuery({
        queryKey: ['ssc-select-options', analysisId],
        queryFn: () => getSystemSafetyConstraints(analysisId)
    });

    const sscOptions = useMemo(() => {
        return sscData ? systemSafetyConstraintsToSelectOptions(sscData) : [];
    }, [sscData]); 

    const { mutateAsync: save, isPending } = useMutation({
        mutationFn: async () => {
            return await createResponsibility({
                responsibility: form.responsibility,
                code: form.code,
                systemSafetyConstraintId: form.ssc?.value,
                componentId: componentId
            });
        },
        onSuccess: () => {
            toast.success("Responsibility added!");
            setForm({ code: '', responsibility: '', ssc: null });
            queryClient.invalidateQueries({ queryKey: ['component-responsibilities', componentId] });
        }
    });

    const { mutateAsync: remove } = useMutation({
        mutationFn: (id: string) => deleteResponsibility(id),
        onSuccess: () => queryClient.invalidateQueries({ queryKey: ['component-responsibilities', componentId] })
    });



    return (
        <CastSection title="Responsibilities" tooltipInfo="What was this component supposed to do?" hideAddButton={true}>
            <div style={{ display: 'flex', flexDirection: 'column', gap: '24px' }}>
                
                {responsibilities && responsibilities.map((resp: any) => (
                    <div key={resp.id} style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', backgroundColor: '#374151', padding: '16px', borderRadius: '8px', border: '1px solid #4b5563' }}>
                        <div>
                            <div style={{ display: 'flex', alignItems: 'center', gap: '8px', marginBottom: '4px' }}>
                                <span style={{ color: 'var(--color-yellow)', fontWeight: 'bold' }}>{resp.code}</span>
                                <span style={{ backgroundColor: '#1f2937', color: '#9ca3af', padding: '2px 6px', borderRadius: '4px', fontSize: '10px', border: '1px solid #4b5563' }}>
                                    {resp.systemSafetyConstraint?.code}
                                </span>
                            </div>
                            <span style={{ color: '#fff' }}>{resp.responsibility}</span>
                        </div>
                        <button onClick={() => remove(resp.id)} style={{ background: 'transparent', border: 'none', color: '#ef4444', cursor: 'pointer' }}><BiTrash size={18} /></button>
                    </div>
                ))}

                <div style={{ backgroundColor: '#1f2937', padding: '16px', borderRadius: '8px', border: '1px dashed #4b5563' }}>
                    <div style={{ display: 'flex', alignItems: 'center', gap: '8px', marginBottom: '16px', color: '#d1d5db' }}>
                        <BiCheckShield size={20} /> <h4 style={{ margin: 0, fontSize: '14px' }}>Add Responsibility</h4>
                    </div>
                    
                    <div style={{ display: 'grid', gridTemplateColumns: '100px 1fr 1fr', gap: '16px' }}>
                        <input className={styles.inputField} placeholder="Code" value={form.code} onChange={e => setForm({...form, code: e.target.value})} />
                        <input className={styles.inputField} placeholder="Description..." value={form.responsibility} onChange={e => setForm({...form, responsibility: e.target.value})} />
                        <SystemSafetyConstraintSelect 
                            systemSafetyConstraints={sscOptions} 
                            value={form.ssc} 
                            onChange={(val: any) => {
                                setForm(prev => {
                                    if (prev.ssc === val) return prev; 
                                    return { ...prev, ssc: val };
                                });
                            }} 
                        />
                    </div>

                    <div style={{ display: 'flex', justifyContent: 'flex-end', marginTop: '16px' }}>
                        <button onClick={() => save()} disabled={isPending || !form.code || !form.responsibility || !form.ssc} className={styles.btnSaveForm}>
                            <BiSave size={18} /> {isPending ? 'Saving...' : 'Add Responsibility'}
                        </button>
                    </div>
                </div>
            </div>
        </CastSection>
    );
}
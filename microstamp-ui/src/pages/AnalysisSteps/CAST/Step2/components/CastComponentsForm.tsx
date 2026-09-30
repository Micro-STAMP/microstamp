import { useState } from 'react';
import { BiSave, BiTrash, BiMicrochip, BiRightArrowAlt } from 'react-icons/bi';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { toast } from 'sonner';
import { useNavigate } from 'react-router-dom'; 
import { createComponent, deleteComponent, getComponents } from '@http/Step2/Components';
import { IComponentInsertDto, IComponentType } from '@interfaces/IStep2';
import { componentTypeSelectOptions } from '@interfaces/IStep2/IComponent/Enums';

import CastSection from '@components/CastSection';
import Select from '@components/FormField/Select';
import styles from '../../Step1/CastStepOne.module.css';

interface Props { analysisId: string; }

export default function CastComponentsForm({ analysisId }: Props) {
    const queryClient = useQueryClient();
    const navigate = useNavigate(); 

    const { data: components, isLoading } = useQuery({
        queryKey: ['analysis-components', analysisId],
        queryFn: () => getComponents(analysisId)
    });

    const [form, setForm] = useState({ code: '', name: '', componentType: IComponentType.CONTROLLER });

    const { mutateAsync: save, isPending } = useMutation({
        mutationFn: async () => {
            const dto: IComponentInsertDto = {
                name: form.name,
                type: form.componentType,
                analysisId: analysisId,
                code: form.code, 
                fatherId: null, 
                border: "SOLID" as any,
                isVisible: true
            };
            return await createComponent(dto);
        },
        onSuccess: () => {
            toast.success("Component added successfully!");
            setForm({ code: '', name: '', componentType: IComponentType.CONTROLLER }); 
            queryClient.invalidateQueries({ queryKey: ['analysis-components', analysisId] });
        },
        onError: (err: any) => toast.error(err.message || "Error saving Component.")
    });

    const { mutateAsync: remove } = useMutation({
        mutationFn: async (id: string) => {
            if (window.confirm("Are you sure you want to delete this component?")) {
                return await deleteComponent(id);
            }
            return Promise.reject(new Error("Cancelled"));
        },
        onSuccess: () => {
            toast.success("Component removed.");
            queryClient.invalidateQueries({ queryKey: ['analysis-components', analysisId] });
            queryClient.invalidateQueries({ queryKey: ['analysis-connections', analysisId] }); 
        },
        onError: (err: any) => {
            if (err.message !== "Cancelled") toast.error(err.message);
        }
    });

    return (
        <CastSection title="Components" tooltipInfo="List all the physical and human components involved in the control structure. Click on 'Analyze Details' to configure their process models and responsibilities." hideAddButton={true}>
            {isLoading ? (
                <div style={{ color: 'var(--color-muted-text)', fontSize: '14px', padding: '10px' }}>Loading components...</div>
            ) : (
                <div style={{ display: 'flex', flexDirection: 'column', gap: '24px' }}>
                    
                    {components && components.length > 0 && (
                        <div style={{ display: 'flex', flexDirection: 'column', gap: '8px' }}>
                            {components.map((comp) => (
                                <div key={comp.id} style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', backgroundColor: 'var(--color-dark-gray)', padding: '12px 16px', borderRadius: '8px', border: '1px solid var(--color-gray)' }}>
                                    
                                    {/* BLOCO DA ESQUERDA: INFORMAÇÕES DO COMPONENTE */}
                                    <div style={{ display: 'flex', alignItems: 'center', gap: '12px' }}>
                                        <BiMicrochip size={20} color="#fb923c" />
                                        
                                        {/* O NOME AGORA É APENAS TEXTO, SEM SUBINHA OU ESTILO DE LINK */}
                                        <span style={{ color: '#f3f4f6', fontWeight: 'bold', fontSize: '15px' }}>
                                            {comp.code} - {comp.name}
                                        </span>
                                        
                                        <span style={{ backgroundColor: 'var(--color-dark)', color: 'var(--color-muted-text)', padding: '2px 8px', borderRadius: '4px', fontSize: '12px', border: '1px solid var(--color-gray)' }}>
                                            {comp.type.replace('_', ' ')}
                                        </span>
                                    </div>

                                    {/* BLOCO DA DIREITA: AÇÕES (BOTÃO EXPLÍCITO) */}
                                    <div style={{ display: 'flex', alignItems: 'center', gap: '16px' }}>
                                        <button
                                            onClick={() => navigate(`/analyses/${analysisId}/cast/step2/component/${comp.id}`)}
                                            style={{
                                                backgroundColor: 'transparent',
                                                color: 'var(--color-info)',
                                                border: '1px solid var(--color-info)',
                                                padding: '6px 12px',
                                                borderRadius: 'var(--radius1)',
                                                cursor: 'pointer',
                                                fontSize: '12px',
                                                fontWeight: 'bold',
                                                display: 'flex',
                                                alignItems: 'center',
                                                gap: '4px'
                                            }}
                                            title="Analyze responsibilities and process model"
                                        >
                                            Analyze Details <BiRightArrowAlt size={16} />
                                        </button>

                                        <button onClick={() => remove(comp.id)} style={{ background: 'transparent', border: 'none', color: 'var(--color-danger)', cursor: 'pointer' }} title="Delete Component">
                                            <BiTrash size={18} />
                                        </button>
                                    </div>

                                </div>
                            ))}
                        </div>
                    )}

                    <div style={{ backgroundColor: 'var(--color-dark)', padding: '16px', borderRadius: '8px', border: '1px dashed var(--color-gray)', display: 'flex', flexDirection: 'column', gap: '16px' }}>
                        <h4 style={{ margin: 0, color: 'var(--color-white)', fontSize: '14px' }}>Add New Component</h4>
                        
                        <div style={{ display: 'grid', gridTemplateColumns: '80px 1fr 1fr', gap: '16px' }}>
                            <div>
                                <label className={styles.inputLabel}>Code</label>
                                <input 
                                    type="text" 
                                    placeholder="C-2" 
                                    className={styles.inputField} 
                                    value={form.code} 
                                    onChange={e => setForm({ ...form, code: e.target.value })} 
                                    style={{ marginBottom: 0 }} 
                                />
                            </div>
                            <div>
                                <label className={styles.inputLabel}>Name</label>
                                <input 
                                    type="text" 
                                    placeholder="e.g. Doctor, Auto-pilot..." 
                                    className={styles.inputField} 
                                    value={form.name} 
                                    onChange={e => setForm({ ...form, name: e.target.value })} 
                                    style={{ marginBottom: 0 }} 
                                />
                            </div>
                            <div>
                                <Select
                                    label="Type"
                                    options={componentTypeSelectOptions}
                                    value={componentTypeSelectOptions.find(o => o.value === form.componentType) || null}
                                    onChange={opt => setForm({ ...form, componentType: (opt?.value as IComponentType) ?? IComponentType.CONTROLLER })}
                                    required
                                />
                            </div>
                        </div>

                        <div style={{ display: 'flex', justifyContent: 'flex-end' }}>
                            <button 
                                onClick={() => save()} 
                                disabled={isPending || !form.name.trim() || !form.code.trim()} 
                                className={styles.btnSaveForm}
                            >
                                <BiSave size={18} /> {isPending ? 'Saving...' : 'Add Component'}
                            </button>
                        </div>
                    </div>

                </div>
            )}
        </CastSection>
    );
}
import { useState } from 'react';
import { BiSave, BiTrash, BiMicrochip, BiRightArrowAlt } from 'react-icons/bi';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { toast } from 'sonner';
import { useNavigate } from 'react-router-dom'; 
import { createComponent, deleteComponent, getComponents } from '@http/Step2/Components';
import { IComponentInsertDto, IComponentType } from '@interfaces/IStep2';

import CastSection from '@components/CastSection';
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
                <div style={{ color: '#9ca3af', fontSize: '14px', padding: '10px' }}>Loading components...</div>
            ) : (
                <div style={{ display: 'flex', flexDirection: 'column', gap: '24px' }}>
                    
                    {components && components.length > 0 && (
                        <div style={{ display: 'flex', flexDirection: 'column', gap: '8px' }}>
                            {components.map((comp) => (
                                <div key={comp.id} style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', backgroundColor: '#374151', padding: '12px 16px', borderRadius: '8px', border: '1px solid #4b5563' }}>
                                    
                                    {/* BLOCO DA ESQUERDA: INFORMAÇÕES DO COMPONENTE */}
                                    <div style={{ display: 'flex', alignItems: 'center', gap: '12px' }}>
                                        <BiMicrochip size={20} color="#fb923c" />
                                        
                                        {/* O NOME AGORA É APENAS TEXTO, SEM SUBINHA OU ESTILO DE LINK */}
                                        <span style={{ color: '#f3f4f6', fontWeight: 'bold' }}>
                                            {comp.code} - {comp.name}
                                        </span>
                                        
                                        <span style={{ backgroundColor: '#1f2937', color: '#9ca3af', padding: '2px 8px', borderRadius: '4px', fontSize: '12px', border: '1px solid #4b5563' }}>
                                            {comp.type.replace('_', ' ')}
                                        </span>
                                    </div>

                                    {/* BLOCO DA DIREITA: AÇÕES (BOTÃO EXPLÍCITO) */}
                                    <div style={{ display: 'flex', alignItems: 'center', gap: '16px' }}>
                                        <button 
                                            onClick={() => navigate(`/analyses/${analysisId}/cast/step2/component/${comp.id}`)}
                                            style={{ 
                                                backgroundColor: '#3b82f6', // Azul para destaque
                                                color: 'white', 
                                                border: 'none', 
                                                padding: '6px 12px', 
                                                borderRadius: '4px', 
                                                cursor: 'pointer',
                                                fontSize: '12px',
                                                fontWeight: 'bold',
                                                display: 'flex',
                                                alignItems: 'center',
                                                gap: '4px',
                                                boxShadow: '0 2px 4px rgba(0,0,0,0.1)'
                                            }}
                                            title="Analyze responsibilities and process model"
                                        >
                                            Analyze Details <BiRightArrowAlt size={16} />
                                        </button>

                                        <button onClick={() => remove(comp.id)} style={{ background: 'transparent', border: 'none', color: '#ef4444', cursor: 'pointer' }} title="Delete Component">
                                            <BiTrash size={18} />
                                        </button>
                                    </div>

                                </div>
                            ))}
                        </div>
                    )}

                    <div style={{ backgroundColor: '#1f2937', padding: '16px', borderRadius: '8px', border: '1px dashed #4b5563', display: 'flex', flexDirection: 'column', gap: '16px' }}>
                        <h4 style={{ margin: 0, color: '#d1d5db', fontSize: '14px' }}>Add New Component</h4>
                        
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
                                <label className={styles.inputLabel}>Type</label>
                                <select 
                                    className={styles.inputField} 
                                    value={form.componentType} 
                                    onChange={e => setForm({ ...form, componentType: e.target.value as IComponentType })} 
                                    style={{ marginBottom: 0, cursor: 'pointer' }}
                                >
                                    <option value={IComponentType.CONTROLLER}>Controller</option>
                                    <option value={IComponentType.CONTROLLED_PROCESS}>Controlled Process</option>
                                    <option value={IComponentType.SENSOR}>Sensor</option>
                                    <option value={IComponentType.ACTUATOR}>Actuator</option>
                                </select>
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
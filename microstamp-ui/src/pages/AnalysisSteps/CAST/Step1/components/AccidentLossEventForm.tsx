import { useState, useEffect } from 'react';
import { BiSave } from 'react-icons/bi';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { toast } from 'sonner';
import { getByAnalysisId, createLossEvent, updateLossEvent } from '@http/CAST/Step1/AccidentLossEvents';
import CastSection from '@components/CastSection';
import styles from '../CastStepOne.module.css';

interface Props { analysisId: string; }

export default function AccidentLossEventForm({ analysisId }: Props) {
    const queryClient = useQueryClient();

    const { data, isLoading } = useQuery({
        queryKey: ['loss-events', analysisId],
        queryFn: () => getByAnalysisId(analysisId)
    });
    const currentLoss = data?.[0]; 

    const [form, setForm] = useState({ code: 'A-1', name: '', description: '' });

    useEffect(() => {
        if (currentLoss) {
            setForm({
                code: currentLoss.code,
                name: currentLoss.name,
                description: currentLoss.description || ''
            });
        }
    }, [currentLoss]);

    const { mutateAsync: save, isPending } = useMutation({
        mutationFn: async () => {
            if (currentLoss) {
                return await updateLossEvent(currentLoss.id, {
                    code: form.code, name: form.name, description: form.description
                });
            } else {
                return await createLossEvent({
                    code: form.code, name: form.name, description: form.description, analysisId
                });
            }
        },
        onSuccess: () => {
            toast.success("Accident / Loss Event saved successfully!");
            queryClient.invalidateQueries({ queryKey: ['loss-events', analysisId] });
        },
        onError: (err: any) => toast.error(err.message || "Error saving Loss Event.")
    });

    return (
        <CastSection title="Accident / Loss Event" tooltipInfo="What was the specific loss event?" hideAddButton={true}>
            {isLoading ? (
                <div style={{ color: '#9ca3af', fontSize: '14px', padding: '10px' }}>Loading data...</div>
            ) : (
                <div style={{ display: 'flex', flexDirection: 'column', gap: '16px' }}>
                    <div style={{ display: 'grid', gridTemplateColumns: '100px 1fr', gap: '16px' }}>
                        <div>
                            <label className={styles.inputLabel}>Code</label>
                            <input type="text" className={styles.inputField} value={form.code} onChange={e => setForm({ ...form, code: e.target.value })} style={{ marginBottom: 0 }} />
                        </div>
                        <div>
                            <label className={styles.inputLabel}>Short Title</label>
                            <input type="text" placeholder="Short title..." className={styles.inputField} value={form.name} onChange={e => setForm({ ...form, name: e.target.value })} style={{ marginBottom: 0 }} />
                        </div>
                    </div>
                    <div>
                        <label className={styles.inputLabel}>Detailed Description</label>
                        <textarea rows={3} placeholder="Detailed description..." className={styles.inputField} value={form.description} onChange={e => setForm({ ...form, description: e.target.value })} style={{ marginBottom: 0 }} />
                    </div>
                    <div style={{ display: 'flex', justifyContent: 'flex-end' }}>
                        <button onClick={() => save()} disabled={isPending} className={styles.btnSaveForm}>
                            <BiSave size={18} /> {isPending ? 'Saving...' : 'Save Loss Event'}
                        </button>
                    </div>
                </div>
            )}
        </CastSection>
    );
}
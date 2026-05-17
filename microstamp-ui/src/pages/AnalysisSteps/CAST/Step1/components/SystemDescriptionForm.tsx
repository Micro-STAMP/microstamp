import { useState, useEffect } from 'react';
import { BiSave } from 'react-icons/bi';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { toast } from 'sonner';
import { getByAnalysisId, createSystemDescription, updateSystemDescription } from '@http/CAST/Step1/SystemDescription';
import CastSection from '@components/CastSection';
import styles from '../CastStepOne.module.css';

interface Props { analysisId: string; }

export default function SystemDescriptionForm({ analysisId }: Props) {
    const queryClient = useQueryClient();

    const { data, isLoading } = useQuery({
        queryKey: ['system-description', analysisId],
        queryFn: () => getByAnalysisId(analysisId)
    });
    const currentSD = data?.[0];

    const [form, setForm] = useState({ code: 'SD-1', description: '', analysisBoundary: '' });

    useEffect(() => {
        if (currentSD) {
            setForm({
                code: currentSD.code,
                description: currentSD.description,
                analysisBoundary: currentSD.analysisBoundary || ''
            });
        }
    }, [currentSD]);

    const { mutateAsync: save, isPending } = useMutation({
        mutationFn: async () => {
            if (currentSD) {
                return await updateSystemDescription(currentSD.id, {
                    code: form.code, description: form.description, analysisBoundary: form.analysisBoundary
                });
            } else {
                return await createSystemDescription({
                    code: form.code, description: form.description, analysisBoundary: form.analysisBoundary, analysisId
                });
            }
        },
        onSuccess: () => {
            toast.success("System Description saved successfully!");
            queryClient.invalidateQueries({ queryKey: ['system-description', analysisId] });
        },
        onError: (err: any) => toast.error(err.message || "Error saving System Description.")
    });

    return (
        <CastSection title="System Description" tooltipInfo="Describe the components and boundaries." hideAddButton={true}>
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
                            <label className={styles.inputLabel}>Analysis Boundary</label>
                            <input type="text" placeholder="What is inside and outside the scope?" className={styles.inputField} value={form.analysisBoundary} onChange={e => setForm({ ...form, analysisBoundary: e.target.value })} style={{ marginBottom: 0 }} />
                        </div>
                    </div>
                    <div>
                        <label className={styles.inputLabel}>System Description</label>
                        <textarea rows={4} placeholder="Describe normal operations..." className={styles.inputField} value={form.description} onChange={e => setForm({ ...form, description: e.target.value })} style={{ marginBottom: 0 }} />
                    </div>
                    <div style={{ display: 'flex', justifyContent: 'flex-end' }}>
                        <button onClick={() => save()} disabled={isPending} className={styles.btnSaveForm}>
                            <BiSave size={18} /> {isPending ? 'Saving...' : 'Save Description'}
                        </button>
                    </div>
                </div>
            )}
        </CastSection>
    );
}
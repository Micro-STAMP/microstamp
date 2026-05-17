import { useState } from 'react';
import { BiSave, BiTrash, BiQuestionMark } from 'react-icons/bi';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { toast } from 'sonner';
import { getByAnalysisId as getEvents, createTimelineEvent, deleteTimelineEvent } from '@http/CAST/Step1/Timeline';
import CastSection from '@components/CastSection';
import styles from '../CastStepOne.module.css';

interface Props { analysisId: string; }

export default function TimelineEventForm({ analysisId }: Props) {
    const queryClient = useQueryClient();

    const { data: events, isLoading } = useQuery({
        queryKey: ['timeline-events', analysisId],
        queryFn: () => getEvents(analysisId)
    });

    const sortedEvents = events?.slice().sort((a, b) => (a.eventOrder || 0) - (b.eventOrder || 0)) || [];

    const [form, setForm] = useState({ 
        code: 'T-1', 
        eventDescription: '', 
        questions: '' 
    });

    const { mutateAsync: save, isPending } = useMutation({
        mutationFn: async () => {
            return await createTimelineEvent({
                code: form.code,
                eventDescription: form.eventDescription,
                questions: form.questions,
                eventOrder: sortedEvents.length + 1, 
                analysisId
            });
        },
        onSuccess: () => {
            toast.success("Event added to timeline!");
            setForm({ code: `T-${sortedEvents.length + 2}`, eventDescription: '', questions: '' });
            queryClient.invalidateQueries({ queryKey: ['timeline-events', analysisId] });
        },
        onError: (err: any) => toast.error(err.message || "Error saving Timeline Event.")
    });

    const { mutateAsync: remove } = useMutation({
        mutationFn: async (id: string) => await deleteTimelineEvent(id),
        onSuccess: () => {
            toast.success("Event removed from timeline.");
            queryClient.invalidateQueries({ queryKey: ['timeline-events', analysisId] });
        }
    });

    return (
        <CastSection title="Timeline of Events & Questions" tooltipInfo="Chronological summary of proximate events." defaultOpen={false} hideAddButton={true}>
            {isLoading ? (
                <div style={{ color: '#9ca3af', fontSize: '14px', padding: '10px' }}>Loading timeline...</div>
            ) : (
                <div style={{ display: 'flex', flexDirection: 'column', gap: '24px' }}>
                    
                    {sortedEvents.length > 0 ? (
                        <div style={{ 
                            display: 'flex', 
                            flexDirection: 'column', 
                            gap: '16px',
                            paddingLeft: '24px', 
                            borderLeft: '2px solid #4b5563', 
                            marginLeft: '12px',
                            position: 'relative'
                        }}>
                            {sortedEvents.map((ev, index) => (
                                <div key={ev.id} style={{ position: 'relative' }}>
                                    
                                    <div style={{
                                        position: 'absolute',
                                        left: '-34px',
                                        top: '16px',
                                        width: '18px',
                                        height: '18px',
                                        borderRadius: '50%',
                                        backgroundColor: 'var(--color-yellow)',
                                        border: '4px solid #1f2937' 
                                    }} />

                                    <div style={{ backgroundColor: '#303642', border: '1px solid #4b5563', borderRadius: '8px', padding: '16px', display: 'flex', justifyContent: 'space-between' }}>
                                        <div style={{ flex: 1 }}>
                                            <div style={{ display: 'flex', alignItems: 'center', gap: '8px', marginBottom: '8px' }}>
                                                <span style={{ color: 'var(--color-yellow)', fontWeight: 600, fontSize: '14px' }}>
                                                    {ev.code}
                                                </span>
                                                <span style={{ fontSize: '12px', color: '#9ca3af', backgroundColor: '#1f2937', padding: '2px 8px', borderRadius: '12px' }}>
                                                    Event {index + 1}
                                                </span>
                                            </div>
                                            
                                            <p style={{ margin: '0 0 12px 0', fontSize: '15px', color: '#ffffff', lineHeight: '1.5' }}>
                                                {ev.eventDescription}
                                            </p>

                                            {ev.questions && (
                                                <div style={{ backgroundColor: '#374151', padding: '10px 12px', borderRadius: '6px', borderLeft: '3px solid var(--color-yellow)', display: 'flex', gap: '8px', alignItems: 'flex-start' }}>
                                                    <BiQuestionMark size={18} color="var(--color-yellow)" style={{ marginTop: '2px', flexShrink: 0 }} />
                                                    <p style={{ margin: 0, fontSize: '14px', color: '#d1d5db', fontStyle: 'italic' }}>
                                                        {ev.questions}
                                                    </p>
                                                </div>
                                            )}
                                        </div>
                                        <button onClick={() => remove(ev.id)} style={{ background: 'none', border: 'none', color: '#ef4444', cursor: 'pointer', padding: '4px', height: 'fit-content' }} title="Remove Event">
                                            <BiTrash size={18} />
                                        </button>
                                    </div>
                                </div>
                            ))}
                        </div>
                    ) : (
                        <div className={styles.emptyState}>No events added to timeline yet.</div>
                    )}

                    <hr style={{ borderTop: '1px solid #e5e7eb', margin: '0' }} />

                    <div style={{ display: 'flex', flexDirection: 'column', gap: '16px' }}>
                        <h4 style={{ margin: 0, fontSize: '14px', color: '#374151' }}>Add Next Event</h4>
                        <div style={{ display: 'grid', gridTemplateColumns: '100px 1fr', gap: '16px' }}>
                            <div>
                                <label className={styles.inputLabel}>Code</label>
                                <input type="text" className={styles.inputField} value={form.code} onChange={e => setForm({ ...form, code: e.target.value })} style={{ marginBottom: 0 }} />
                            </div>
                            <div>
                                <label className={styles.inputLabel}>Proximate Event</label>
                                <textarea rows={2} placeholder="What happened?" className={styles.inputField} value={form.eventDescription} onChange={e => setForm({ ...form, eventDescription: e.target.value })} style={{ marginBottom: 0 }} />
                            </div>
                        </div>
                        <div>
                            <label className={styles.inputLabel}>Questions Generated</label>
                            <textarea rows={2} placeholder="Why did this happen? (e.g., Why was the alert ignored?)" className={styles.inputField} value={form.questions} onChange={e => setForm({ ...form, questions: e.target.value })} style={{ marginBottom: 0 }} />
                        </div>

                        <div style={{ display: 'flex', justifyContent: 'flex-end' }}>
                            <button onClick={() => save()} disabled={isPending || !form.eventDescription} className={styles.btnSaveForm}>
                                <BiSave size={18} /> {isPending ? 'Adding...' : 'Add to Timeline'}
                            </button>
                        </div>
                    </div>
                </div>
            )}
        </CastSection>
    );
}
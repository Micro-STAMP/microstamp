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

    const [expandedEventId, setExpandedEventId] = useState<string | null>(null);
    const expandedEvent = sortedEvents.find(ev => ev.id === expandedEventId) || null;

    const [form, setForm] = useState({
        code: 'T-1',
        eventDescription: '',
        questions: '',
        timeLabel: ''
    });

    const { mutateAsync: save, isPending } = useMutation({
        mutationFn: async () => {
            return await createTimelineEvent({
                code: form.code,
                eventDescription: form.eventDescription,
                questions: form.questions,
                timeLabel: form.timeLabel,
                eventOrder: sortedEvents.length + 1,
                analysisId
            });
        },
        onSuccess: () => {
            toast.success("Event added to timeline!");
            setForm({ code: `T-${sortedEvents.length + 2}`, eventDescription: '', questions: '', timeLabel: '' });
            queryClient.invalidateQueries({ queryKey: ['timeline-events', analysisId] });
        },
        onError: (err: any) => toast.error(err.message || "Error saving Timeline Event.")
    });

    const { mutateAsync: remove } = useMutation({
        mutationFn: async (id: string) => await deleteTimelineEvent(id),
        onSuccess: () => {
            toast.success("Event removed from timeline.");
            setExpandedEventId(null);
            queryClient.invalidateQueries({ queryKey: ['timeline-events', analysisId] });
        }
    });

    return (
        <CastSection title="Proximate Events and Questions" tooltipInfo="Chronological summary of proximate events." defaultOpen={false} hideAddButton={true}>
            {isLoading ? (
                <div style={{ color: 'var(--color-muted-text)', fontSize: '14px', padding: '10px' }}>Loading timeline...</div>
            ) : (
                <div style={{ display: 'flex', flexDirection: 'column', gap: '24px' }}>

                    {sortedEvents.length > 0 ? (
                        <div style={{ display: 'flex', flexDirection: 'column', gap: '16px' }}>
                            <div style={{ overflowX: 'auto', paddingBottom: '8px' }}>
                                <div style={{ display: 'flex', alignItems: 'flex-start', gap: '48px', position: 'relative', paddingTop: '8px', minWidth: 'fit-content' }}>
                                    <div style={{ position: 'absolute', top: '17px', left: '9px', right: '9px', height: '2px', backgroundColor: 'var(--color-gray)', zIndex: 0 }} />

                                    {sortedEvents.map(ev => {
                                        const isExpanded = ev.id === expandedEventId;
                                        return (
                                            <button
                                                key={ev.id}
                                                onClick={() => setExpandedEventId(isExpanded ? null : ev.id)}
                                                style={{ position: 'relative', zIndex: 1, display: 'flex', flexDirection: 'column', alignItems: 'center', gap: '8px', background: 'none', border: 'none', cursor: 'pointer', padding: 0, minWidth: '90px' }}
                                            >
                                                <div style={{
                                                    width: '18px',
                                                    height: '18px',
                                                    borderRadius: '50%',
                                                    backgroundColor: isExpanded ? 'var(--color-yellow)' : 'var(--color-dark-gray)',
                                                    border: `4px solid ${isExpanded ? 'var(--color-dark)' : 'var(--color-yellow)'}`
                                                }} />
                                                <div style={{ textAlign: 'center' }}>
                                                    <div style={{ color: 'var(--color-yellow)', fontWeight: 600, fontSize: '13px' }}>{ev.code}</div>
                                                    {ev.timeLabel && (
                                                        <div style={{ color: 'var(--color-muted-text)', fontSize: '11px' }}>{ev.timeLabel}</div>
                                                    )}
                                                </div>
                                            </button>
                                        );
                                    })}
                                </div>
                            </div>

                            {expandedEvent && (
                                <div style={{ backgroundColor: 'var(--color-dark-gray)', border: '1px solid var(--color-gray)', borderRadius: '8px', padding: '16px', display: 'flex', justifyContent: 'space-between' }}>
                                    <div style={{ flex: 1 }}>
                                        <div style={{ display: 'flex', alignItems: 'center', gap: '8px', marginBottom: '8px' }}>
                                            <span style={{ color: 'var(--color-yellow)', fontWeight: 600, fontSize: '14px' }}>
                                                {expandedEvent.code}
                                            </span>
                                            {expandedEvent.timeLabel && (
                                                <span style={{ fontSize: '12px', color: 'var(--color-muted-text)', backgroundColor: 'var(--color-dark)', padding: '2px 8px', borderRadius: '12px' }}>
                                                    {expandedEvent.timeLabel}
                                                </span>
                                            )}
                                        </div>

                                        <p style={{ margin: '0 0 12px 0', fontSize: '15px', color: '#ffffff', lineHeight: '1.5' }}>
                                            {expandedEvent.eventDescription}
                                        </p>

                                        {expandedEvent.questions && (
                                            <div style={{ backgroundColor: 'var(--color-dark-gray)', padding: '10px 12px', borderRadius: '6px', borderLeft: '3px solid var(--color-yellow)', display: 'flex', gap: '8px', alignItems: 'flex-start' }}>
                                                <BiQuestionMark size={18} color="var(--color-yellow)" style={{ marginTop: '2px', flexShrink: 0 }} />
                                                <p style={{ margin: 0, fontSize: '14px', color: '#d1d5db', fontStyle: 'italic' }}>
                                                    {expandedEvent.questions}
                                                </p>
                                            </div>
                                        )}
                                    </div>
                                    <button onClick={() => remove(expandedEvent.id)} style={{ background: 'none', border: 'none', color: '#ef4444', cursor: 'pointer', padding: '4px', height: 'fit-content' }} title="Remove Event">
                                        <BiTrash size={18} />
                                    </button>
                                </div>
                            )}
                        </div>
                    ) : (
                        <div className={styles.emptyState}>No events added to timeline yet.</div>
                    )}

                    <hr style={{ borderTop: '1px solid #e5e7eb', margin: '0' }} />

                    <div style={{ display: 'flex', flexDirection: 'column', gap: '16px' }}>
                        <h4 style={{ margin: 0, fontSize: '14px', color: 'var(--color-white)' }}>Add Next Event</h4>
                        <div style={{ display: 'grid', gridTemplateColumns: '100px 1fr', gap: '16px' }}>
                            <div>
                                <label className={styles.inputLabel}>Code</label>
                                <input type="text" className={styles.inputField} value={form.code} onChange={e => setForm({ ...form, code: e.target.value })} style={{ marginBottom: 0 }} />
                            </div>
                            <div>
                                <label className={styles.inputLabel}>Time Label (optional)</label>
                                <input type="text" placeholder="e.g. 21:00, T+10min, the next morning" className={styles.inputField} value={form.timeLabel} onChange={e => setForm({ ...form, timeLabel: e.target.value })} style={{ marginBottom: 0 }} />
                            </div>
                        </div>
                        <div>
                            <label className={styles.inputLabel}>Proximate Event</label>
                            <textarea rows={2} placeholder="What happened?" className={styles.inputField} value={form.eventDescription} onChange={e => setForm({ ...form, eventDescription: e.target.value })} style={{ marginBottom: 0 }} />
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

import { useState } from 'react';
import { BiSave, BiTrash, BiQuestionMark, BiImageAdd, BiUpload, BiX, BiChevronLeft, BiChevronRight } from 'react-icons/bi';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { toast } from 'sonner';
import {
    getByAnalysisId as getEvents,
    createTimelineEvent,
    updateTimelineEvent,
    deleteTimelineEvent,
    reorderTimelineEvents,
    uploadTimelineEventImage,
    deleteTimelineEventImage
} from '@http/CAST/Step1/Timeline';
import { ITimelineEventReadDto } from '@interfaces/CAST/ITimelineEvent';
import CastSection from '@components/CastSection';
import CastCheckbox from '@components/CastCheckbox';
import CastTextarea from '@components/CastTextarea';
import CastListInput from '@components/CastListInput';
import CastItemActions from '@components/CastItemActions';
import { useCastConfirm } from '@components/CastConfirm';
import { formatDateTimeBR } from '@services/date';
import { nextCode } from '@utils/castCodes';
import { cleanList } from '@utils/castList';
import styles from '../CastStepOne.module.css';

function toIsoDateTime(date: string, time: string): string | undefined {
    if (!date) return undefined;
    return `${date}T${time || '00:00'}:00.000Z`;
}

function eventTimeDisplay(ev: { exactDateTime?: string; timeLabel?: string }): string | null {
    if (ev.exactDateTime) return formatDateTimeBR(ev.exactDateTime);
    return ev.timeLabel || null;
}

interface Props { analysisId: string; }

const EMPTY_FORM = {
    code: '',
    eventDescription: '',
    questions: [] as string[],
    timeLabel: '',
    useExactDate: false,
    exactDate: '',
    exactTime: ''
};

const arrowBtn = { background: 'none', border: '1px solid var(--color-gray)', borderRadius: '6px', color: 'var(--color-muted-text)', cursor: 'pointer', padding: '4px 8px', display: 'flex', alignItems: 'center', gap: '2px', fontSize: '12px' } as const;

export default function TimelineEventForm({ analysisId }: Props) {
    const queryClient = useQueryClient();
    const queryKey = ['timeline-events', analysisId];
    const { confirmDelete, confirmDialog } = useCastConfirm();

    const { data: events, isLoading } = useQuery({
        queryKey,
        queryFn: () => getEvents(analysisId)
    });

    const sortedEvents = events?.slice().sort((a, b) => (a.eventOrder || 0) - (b.eventOrder || 0)) || [];

    const [expandedEventId, setExpandedEventId] = useState<string | null>(null);
    const expandedEvent = sortedEvents.find(ev => ev.id === expandedEventId) || null;
    const expandedIndex = expandedEvent ? sortedEvents.indexOf(expandedEvent) : -1;

    const [form, setForm] = useState(EMPTY_FORM);
    const [editingEvent, setEditingEvent] = useState<ITimelineEventReadDto | null>(null);
    const code = form.code || nextCode('T', sortedEvents.map(ev => ev.code));

    const [draggedId, setDraggedId] = useState<string | null>(null);

    const resetForm = () => {
        setForm(EMPTY_FORM);
        setEditingEvent(null);
    };

    const { mutateAsync: save, isPending } = useMutation({
        mutationFn: async () => {
            const data = {
                code: code.trim(),
                eventDescription: form.eventDescription,
                questions: cleanList(form.questions),
                timeLabel: form.useExactDate ? undefined : form.timeLabel,
                exactDateTime: form.useExactDate ? toIsoDateTime(form.exactDate, form.exactTime) : undefined
            };
            return editingEvent
                ? await updateTimelineEvent(editingEvent.id, { ...data, eventOrder: editingEvent.eventOrder })
                : await createTimelineEvent({ ...data, eventOrder: sortedEvents.length + 1, analysisId });
        },
        onSuccess: () => {
            toast.success(editingEvent ? "Event updated!" : "Event added to timeline!");
            resetForm();
            queryClient.invalidateQueries({ queryKey });
        },
        onError: (err: any) => toast.error(err.message || "Error saving Timeline Event.")
    });

    const { mutateAsync: remove } = useMutation({
        mutationFn: async (id: string) => await deleteTimelineEvent(id),
        onSuccess: (_, id) => {
            toast.success("Event removed from timeline.");
            setExpandedEventId(null);
            if (editingEvent?.id === id) resetForm();
            queryClient.invalidateQueries({ queryKey });
        },
        onError: (err: any) => toast.error(err.message || "Error deleting Timeline Event.")
    });

    const { mutate: reorder } = useMutation({
        mutationFn: (orderedIds: string[]) => reorderTimelineEvents(analysisId, orderedIds),
        onMutate: async (orderedIds: string[]) => {
            await queryClient.cancelQueries({ queryKey });
            const previous = queryClient.getQueryData<ITimelineEventReadDto[]>(queryKey);
            queryClient.setQueryData<ITimelineEventReadDto[]>(queryKey, old =>
                old?.map(ev => ({ ...ev, eventOrder: orderedIds.indexOf(ev.id) + 1 }))
            );
            return { previous };
        },
        onError: (err: any, _ids, context) => {
            if (context?.previous) queryClient.setQueryData(queryKey, context.previous);
            toast.error(err.message || "Error reordering the timeline.");
        },
        onSettled: () => queryClient.invalidateQueries({ queryKey })
    });

    const moveEvent = (fromIndex: number, toIndex: number) => {
        if (toIndex < 0 || toIndex >= sortedEvents.length || fromIndex === toIndex) return;
        const ids = sortedEvents.map(ev => ev.id);
        const [moved] = ids.splice(fromIndex, 1);
        ids.splice(toIndex, 0, moved);
        reorder(ids);
    };

    const handleDrop = (targetId: string) => {
        if (!draggedId) return;
        moveEvent(sortedEvents.findIndex(ev => ev.id === draggedId), sortedEvents.findIndex(ev => ev.id === targetId));
        setDraggedId(null);
    };

    const { mutateAsync: uploadImage, isPending: isUploadingImage } = useMutation({
        mutationFn: async ({ id, file }: { id: string; file: File }) => await uploadTimelineEventImage(id, file),
        onSuccess: () => {
            toast.success("Event image uploaded!");
            queryClient.invalidateQueries({ queryKey });
        },
        onError: (err: any) => toast.error(err.message || "Error uploading event image.")
    });

    const { mutateAsync: removeImage, isPending: isRemovingImage } = useMutation({
        mutationFn: async (id: string) => await deleteTimelineEventImage(id),
        onSuccess: () => {
            toast.success("Event image removed.");
            queryClient.invalidateQueries({ queryKey });
        },
        onError: (err: any) => toast.error(err.message || "Error removing event image.")
    });

    const handleImageChange = async (e: React.ChangeEvent<HTMLInputElement>, eventId: string) => {
        const file = e.target.files?.[0];
        if (!file) return;

        if (!file.type.startsWith('image/')) {
            toast.error("Please upload a valid image file (PNG, JPG).");
            return;
        }

        await uploadImage({ id: eventId, file });
        e.target.value = '';
    };

    const startEdit = (ev: ITimelineEventReadDto) => {
        setEditingEvent(ev);
        setForm({
            code: ev.code,
            eventDescription: ev.eventDescription,
            questions: ev.questions ?? [],
            timeLabel: ev.timeLabel || '',
            useExactDate: !!ev.exactDateTime,
            exactDate: ev.exactDateTime ? ev.exactDateTime.slice(0, 10) : '',
            exactTime: ev.exactDateTime ? ev.exactDateTime.slice(11, 16) : ''
        });
    };

    const handleDelete = async (ev: ITimelineEventReadDto) => {
        if (await confirmDelete(`event ${ev.code}`)) await remove(ev.id);
    };

    const handleRemoveImage = async (ev: ITimelineEventReadDto) => {
        if (await confirmDelete(`the image of event ${ev.code}`)) await removeImage(ev.id);
    };

    return (
        <CastSection title="Proximate Events and Questions Raised" tooltipInfo="Chronological summary of the proximate events. For each event, record the questions it raises — they guide the rest of the analysis. Drag the events (or use the arrows) to fix their sequence." defaultOpen={false} hideAddButton={true}>
            {isLoading ? (
                <div style={{ color: 'var(--color-muted-text)', fontSize: '14px', padding: '10px' }}>Loading timeline...</div>
            ) : (
                <div style={{ display: 'flex', flexDirection: 'column', gap: '24px' }}>

                    {sortedEvents.length > 0 ? (
                        <div style={{ display: 'flex', flexDirection: 'column', gap: '16px' }}>
                            <div style={{ overflowX: 'auto', paddingBottom: '8px' }}>
                                <div style={{ display: 'flex', alignItems: 'flex-start', gap: '48px', position: 'relative', paddingTop: '8px', minWidth: 'fit-content' }}>
                                    <div style={{ position: 'absolute', top: '17px', left: '9px', right: '9px', height: '2px', backgroundColor: 'var(--color-gray)', zIndex: 0 }} />

                                    {sortedEvents.map((ev, index) => {
                                        const isExpanded = ev.id === expandedEventId;
                                        const isDragTarget = draggedId !== null && draggedId !== ev.id;
                                        return (
                                            <button
                                                key={ev.id}
                                                draggable
                                                onDragStart={() => setDraggedId(ev.id)}
                                                onDragEnd={() => setDraggedId(null)}
                                                onDragOver={e => { if (isDragTarget) e.preventDefault(); }}
                                                onDrop={() => handleDrop(ev.id)}
                                                onClick={() => setExpandedEventId(isExpanded ? null : ev.id)}
                                                title="Click to see details · drag to reorder"
                                                style={{ position: 'relative', zIndex: 1, display: 'flex', flexDirection: 'column', alignItems: 'center', gap: '8px', background: 'none', border: 'none', cursor: 'grab', padding: 0, minWidth: '90px', opacity: draggedId === ev.id ? 0.4 : 1 }}
                                            >
                                                <div style={{
                                                    width: '18px',
                                                    height: '18px',
                                                    borderRadius: '50%',
                                                    backgroundColor: isExpanded ? 'var(--color-yellow)' : 'var(--color-dark-gray)',
                                                    border: `4px solid ${isExpanded ? 'var(--color-dark)' : 'var(--color-yellow)'}`
                                                }} />
                                                <div style={{ textAlign: 'center' }}>
                                                    <div style={{ color: 'var(--color-muted-text)', fontSize: '10px' }}>#{index + 1}</div>
                                                    <div style={{ color: 'var(--color-yellow)', fontWeight: 600, fontSize: '13px' }}>{ev.code}</div>
                                                    {eventTimeDisplay(ev) && (
                                                        <div style={{ color: 'var(--color-muted-text)', fontSize: '11px' }}>{eventTimeDisplay(ev)}</div>
                                                    )}
                                                </div>
                                            </button>
                                        );
                                    })}
                                </div>
                            </div>

                            {expandedEvent && (
                                <div style={{ backgroundColor: 'var(--color-dark-gray)', border: '1px solid var(--color-gray)', borderRadius: '8px', padding: '16px', display: 'flex', justifyContent: 'space-between', gap: '16px' }}>
                                    <div style={{ flex: 1 }}>
                                        <div style={{ display: 'flex', alignItems: 'center', gap: '8px', marginBottom: '8px', flexWrap: 'wrap' }}>
                                            <span style={{ color: 'var(--color-yellow)', fontWeight: 600, fontSize: '14px' }}>
                                                {expandedEvent.code}
                                            </span>
                                            {eventTimeDisplay(expandedEvent) && (
                                                <span style={{ fontSize: '12px', color: 'var(--color-muted-text)', backgroundColor: 'var(--color-dark)', padding: '2px 8px', borderRadius: '12px' }}>
                                                    {eventTimeDisplay(expandedEvent)}
                                                </span>
                                            )}
                                            <div style={{ display: 'flex', gap: '6px', marginLeft: 'auto' }}>
                                                <button type="button" style={{ ...arrowBtn, opacity: expandedIndex <= 0 ? 0.4 : 1 }} onClick={() => moveEvent(expandedIndex, expandedIndex - 1)} disabled={expandedIndex <= 0} title="Move earlier in the sequence">
                                                    <BiChevronLeft size={16} /> Earlier
                                                </button>
                                                <button type="button" style={{ ...arrowBtn, opacity: expandedIndex >= sortedEvents.length - 1 ? 0.4 : 1 }} onClick={() => moveEvent(expandedIndex, expandedIndex + 1)} disabled={expandedIndex >= sortedEvents.length - 1} title="Move later in the sequence">
                                                    Later <BiChevronRight size={16} />
                                                </button>
                                            </div>
                                        </div>

                                        <p style={{ margin: '0 0 12px 0', fontSize: '15px', color: '#ffffff', lineHeight: '1.5', whiteSpace: 'pre-wrap' }}>
                                            {expandedEvent.eventDescription}
                                        </p>

                                        {(expandedEvent.questions ?? []).length > 0 && (
                                            <div style={{ backgroundColor: 'var(--color-dark-gray)', padding: '10px 12px', borderRadius: '6px', borderLeft: '3px solid var(--color-yellow)', display: 'flex', gap: '8px', alignItems: 'flex-start', marginBottom: '12px' }}>
                                                <BiQuestionMark size={18} color="var(--color-yellow)" style={{ marginTop: '2px', flexShrink: 0 }} />
                                                <div>
                                                    <div style={{ fontSize: '12px', fontWeight: 600, color: 'var(--color-yellow)', marginBottom: '4px' }}>Questions Raised</div>
                                                    <ol style={{ margin: 0, paddingLeft: '18px', fontSize: '14px', color: '#d1d5db', fontStyle: 'italic', display: 'flex', flexDirection: 'column', gap: '4px' }}>
                                                        {expandedEvent.questions!.map((q, i) => <li key={i}>{q}</li>)}
                                                    </ol>
                                                </div>
                                            </div>
                                        )}

                                        {expandedEvent.imageBase64 ? (
                                            <div style={{ border: '1px solid var(--color-gray)', borderRadius: '8px', overflow: 'hidden', backgroundColor: 'var(--color-dark)' }}>
                                                <div style={{ display: 'flex', justifyContent: 'space-between', padding: '8px 12px', borderBottom: '1px solid var(--color-gray)' }}>
                                                    <span style={{ color: '#d1d5db', fontSize: '12px', fontWeight: 'bold' }}>Event Image</span>
                                                    <button
                                                        onClick={() => handleRemoveImage(expandedEvent)}
                                                        disabled={isRemovingImage}
                                                        style={{ background: 'transparent', border: 'none', color: '#ef4444', cursor: isRemovingImage ? 'not-allowed' : 'pointer', display: 'flex', alignItems: 'center', gap: '4px', fontSize: '12px' }}
                                                    >
                                                        <BiTrash size={14} /> {isRemovingImage ? 'Removing...' : 'Remove'}
                                                    </button>
                                                </div>
                                                <div style={{ padding: '12px', display: 'flex', justifyContent: 'center' }}>
                                                    <img
                                                        src={expandedEvent.imageBase64.startsWith('data:image') ? expandedEvent.imageBase64 : `data:image/png;base64,${expandedEvent.imageBase64}`}
                                                        alt={`Event ${expandedEvent.code}`}
                                                        style={{ maxWidth: '100%', maxHeight: '300px', objectFit: 'contain', borderRadius: '4px' }}
                                                    />
                                                </div>
                                            </div>
                                        ) : (
                                            <label style={{ display: 'inline-flex', alignItems: 'center', gap: '6px', fontSize: '12px', color: 'var(--color-muted-text)', border: '1px dashed var(--color-gray)', borderRadius: '6px', padding: '6px 10px', cursor: isUploadingImage ? 'not-allowed' : 'pointer', width: 'fit-content' }}>
                                                <BiImageAdd size={16} />
                                                {isUploadingImage ? 'Uploading...' : 'Add Event Image (optional)'}
                                                <input
                                                    type="file"
                                                    accept="image/png, image/jpeg"
                                                    style={{ display: 'none' }}
                                                    onChange={e => handleImageChange(e, expandedEvent.id)}
                                                    disabled={isUploadingImage}
                                                />
                                                <BiUpload size={14} />
                                            </label>
                                        )}
                                    </div>
                                    <CastItemActions itemLabel="Event" onEdit={() => startEdit(expandedEvent)} onDelete={() => handleDelete(expandedEvent)} />
                                </div>
                            )}
                        </div>
                    ) : (
                        <div className={styles.emptyState}>No events added to timeline yet.</div>
                    )}

                    <hr style={{ borderTop: '1px solid #e5e7eb', margin: '0' }} />

                    <div style={{ display: 'flex', flexDirection: 'column', gap: '16px' }}>
                        <h4 style={{ margin: 0, fontSize: '14px', color: 'var(--color-white)' }}>
                            {editingEvent ? `Edit Event ${code}` : 'Add Next Event'}
                        </h4>
                        <div style={{ display: 'grid', gridTemplateColumns: '100px 1fr', gap: '16px' }}>
                            <div>
                                <label className={styles.inputLabel}>Code</label>
                                <input type="text" className={styles.inputField} value={code} onChange={e => setForm({ ...form, code: e.target.value })} style={{ marginBottom: 0 }} />
                            </div>
                            <div>
                                <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
                                    <label className={styles.inputLabel}>
                                        {form.useExactDate ? 'Exact Date & Time' : 'Time Label (optional)'}
                                    </label>
                                    <label style={{ display: 'flex', alignItems: 'center', gap: '6px', fontSize: '12px', color: 'var(--color-muted-text)', cursor: 'pointer', marginBottom: '6px' }}>
                                        <CastCheckbox
                                            checked={form.useExactDate}
                                            onChange={() => setForm({ ...form, useExactDate: !form.useExactDate })}
                                        />
                                        I have the exact date
                                    </label>
                                </div>
                                {form.useExactDate ? (
                                    <div style={{ display: 'flex', gap: '8px' }}>
                                        <input
                                            type="date"
                                            className={styles.inputField}
                                            value={form.exactDate}
                                            onChange={e => setForm({ ...form, exactDate: e.target.value })}
                                            style={{ marginBottom: 0, flex: 1 }}
                                        />
                                        <input
                                            type="time"
                                            className={styles.inputField}
                                            value={form.exactTime}
                                            onChange={e => setForm({ ...form, exactTime: e.target.value })}
                                            style={{ marginBottom: 0, width: '110px' }}
                                        />
                                    </div>
                                ) : (
                                    <input type="text" placeholder="e.g. 21:00, T+10min, the next morning" className={styles.inputField} value={form.timeLabel} onChange={e => setForm({ ...form, timeLabel: e.target.value })} style={{ marginBottom: 0 }} />
                                )}
                            </div>
                        </div>
                        <div>
                            <label className={styles.inputLabel}>Proximate Event</label>
                            <CastTextarea rows={2} placeholder="What happened?" className={styles.inputField} value={form.eventDescription} onChange={e => setForm({ ...form, eventDescription: e.target.value })} style={{ marginBottom: 0 }} />
                        </div>
                        <CastListInput
                            label="Questions Raised"
                            items={form.questions}
                            onChange={questions => setForm({ ...form, questions })}
                            placeholder="E.g., Why was the alert ignored?"
                            addLabel="Add question"
                        />

                        <div style={{ display: 'flex', justifyContent: 'flex-end', gap: '8px' }}>
                            {editingEvent && (
                                <button onClick={resetForm} className={styles.btnBack} type="button">
                                    <BiX size={18} /> Cancel
                                </button>
                            )}
                            <button onClick={() => save()} disabled={isPending || !form.eventDescription.trim() || !code.trim()} className={styles.btnSaveForm}>
                                <BiSave size={18} /> {isPending ? 'Saving...' : editingEvent ? 'Save Changes' : 'Add to Timeline'}
                            </button>
                        </div>
                    </div>
                </div>
            )}
            {confirmDialog}
        </CastSection>
    );
}

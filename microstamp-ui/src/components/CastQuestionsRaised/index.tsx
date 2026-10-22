import { useState } from 'react';
import { BiSave, BiTrash, BiPencil, BiX, BiQuestionMark } from 'react-icons/bi';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { toast } from 'sonner';
import {
    getByAnalysisId,
    createQuestionRaised,
    updateQuestionRaised,
    deleteQuestionRaised
} from '@http/CAST/QuestionsRaised';
import { CastStepNumber, IQuestionRaisedReadDto } from '@interfaces/CAST/IQuestionRaised';
import CastSection from '@components/CastSection';
import CastTextarea from '@components/CastTextarea';
import { useCastConfirm } from '@components/CastConfirm';
import { nextCode } from '@utils/castCodes';
import styles from '@pages/AnalysisSteps/CAST/Step1/CastStepOne.module.css';

interface Props {
    analysisId: string;
    step: CastStepNumber;
}

const QUESTION_PREFIX = 'Q';

export default function CastQuestionsRaised({ analysisId, step }: Props) {
    const queryClient = useQueryClient();
    const { confirmDelete, confirmDialog } = useCastConfirm();
    const queryKey = ['questions-raised', analysisId];

    const { data: allQuestions, isLoading } = useQuery({
        queryKey,
        queryFn: () => getByAnalysisId(analysisId)
    });

    const questions = (allQuestions ?? []).filter(q => q.castStep === step);
    const suggestedCode = nextCode(QUESTION_PREFIX, (allQuestions ?? []).map(q => q.code));

    const [form, setForm] = useState({ code: '', question: '' });
    const [editing, setEditing] = useState<{ id: string; code: string; question: string } | null>(null);
    const code = form.code || suggestedCode;

    const invalidate = () => queryClient.invalidateQueries({ queryKey });

    const { mutateAsync: create, isPending: isCreating } = useMutation({
        mutationFn: () => createQuestionRaised({ analysisId, castStep: step, code: code.trim(), question: form.question.trim() }),
        onSuccess: () => {
            toast.success("Question added!");
            setForm({ code: '', question: '' });
            invalidate();
        },
        onError: (err: any) => toast.error(err.message || "Error saving Question Raised.")
    });

    const { mutateAsync: update, isPending: isUpdating } = useMutation({
        mutationFn: (q: { id: string; code: string; question: string }) =>
            updateQuestionRaised(q.id, { code: q.code.trim(), question: q.question.trim() }),
        onSuccess: () => {
            toast.success("Question updated!");
            setEditing(null);
            invalidate();
        },
        onError: (err: any) => toast.error(err.message || "Error updating Question Raised.")
    });

    const { mutateAsync: remove } = useMutation({
        mutationFn: (id: string) => deleteQuestionRaised(id),
        onSuccess: () => {
            toast.success("Question removed.");
            invalidate();
        },
        onError: (err: any) => toast.error(err.message || "Error deleting Question Raised.")
    });

    const handleDelete = async (q: IQuestionRaisedReadDto) => {
        if (await confirmDelete(`question ${q.code}`)) await remove(q.id);
    };

    return (
        <CastSection
            title="Questions Raised"
            tooltipInfo="Questions raised during this step that cannot be answered with the information currently available. In CAST, these open questions are recorded explicitly and guide the rest of the investigation (CAST Handbook, Leveson 2019)."
            defaultOpen={false}
            hideAddButton={true}
        >
            {isLoading ? (
                <div style={{ color: 'var(--color-muted-text)', fontSize: '14px', padding: '10px' }}>Loading questions...</div>
            ) : (
                <div style={{ display: 'flex', flexDirection: 'column', gap: '20px' }}>
                    {questions.length > 0 ? (
                        <div style={{ display: 'flex', flexDirection: 'column', gap: '10px' }}>
                            {questions.map(q => editing?.id === q.id ? (
                                <div key={q.id} style={{ display: 'grid', gridTemplateColumns: '100px 1fr auto', gap: '12px', alignItems: 'flex-start', padding: '12px', backgroundColor: 'var(--color-dark-gray)', border: '1px solid var(--color-yellow)', borderRadius: '8px' }}>
                                    <input className={styles.inputField} style={{ marginBottom: 0 }} value={editing.code} onChange={e => setEditing({ ...editing, code: e.target.value })} />
                                    <CastTextarea rows={1} className={styles.inputField} style={{ marginBottom: 0 }} value={editing.question} onChange={e => setEditing({ ...editing, question: e.target.value })} />
                                    <div style={{ display: 'flex', gap: '4px', paddingTop: '6px' }}>
                                        <button onClick={() => update(editing)} disabled={isUpdating || !editing.code.trim() || !editing.question.trim()} style={{ background: 'none', border: 'none', color: 'var(--color-yellow)', cursor: 'pointer', padding: '4px' }} title="Save changes">
                                            <BiSave size={18} />
                                        </button>
                                        <button onClick={() => setEditing(null)} style={{ background: 'none', border: 'none', color: 'var(--color-muted-text)', cursor: 'pointer', padding: '4px' }} title="Cancel">
                                            <BiX size={18} />
                                        </button>
                                    </div>
                                </div>
                            ) : (
                                <div key={q.id} style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', gap: '12px', padding: '12px 16px', backgroundColor: 'var(--color-dark-gray)', borderRadius: '8px', borderLeft: '3px solid var(--color-yellow)' }}>
                                    <div style={{ display: 'flex', gap: '8px', alignItems: 'flex-start' }}>
                                        <BiQuestionMark size={18} color="var(--color-yellow)" style={{ marginTop: '2px', flexShrink: 0 }} />
                                        <span style={{ color: 'var(--color-yellow)', fontWeight: 600, fontSize: '14px', whiteSpace: 'nowrap' }}>{q.code}</span>
                                        <p style={{ margin: 0, fontSize: '14px', color: '#d1d5db', lineHeight: '1.5', whiteSpace: 'pre-wrap' }}>{q.question}</p>
                                    </div>
                                    <div style={{ display: 'flex', gap: '4px' }}>
                                        <button onClick={() => setEditing({ id: q.id, code: q.code, question: q.question })} style={{ background: 'none', border: 'none', color: '#60a5fa', cursor: 'pointer', padding: '4px' }} title="Edit question">
                                            <BiPencil size={18} />
                                        </button>
                                        <button onClick={() => handleDelete(q)} style={{ background: 'none', border: 'none', color: '#ef4444', cursor: 'pointer', padding: '4px' }} title="Delete question">
                                            <BiTrash size={18} />
                                        </button>
                                    </div>
                                </div>
                            ))}
                        </div>
                    ) : (
                        <div className={styles.emptyState}>No questions raised in this step yet.</div>
                    )}

                    <div style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
                        <h4 style={{ margin: 0, fontSize: '14px', color: 'var(--color-white)' }}>Add Question</h4>
                        <div style={{ display: 'grid', gridTemplateColumns: '100px 1fr', gap: '16px' }}>
                            <div>
                                <label className={styles.inputLabel}>Code</label>
                                <input type="text" className={styles.inputField} style={{ marginBottom: 0 }} value={code} onChange={e => setForm({ ...form, code: e.target.value })} />
                            </div>
                            <div>
                                <label className={styles.inputLabel}>Question</label>
                                <CastTextarea rows={1} placeholder="E.g., Why was the alarm not acknowledged by the operator?" className={styles.inputField} style={{ marginBottom: 0 }} value={form.question} onChange={e => setForm({ ...form, question: e.target.value })} />
                            </div>
                        </div>
                        <div style={{ display: 'flex', justifyContent: 'flex-end' }}>
                            <button onClick={() => create()} disabled={isCreating || !form.question.trim() || !code.trim()} className={styles.btnSaveForm}>
                                <BiSave size={18} /> {isCreating ? 'Adding...' : 'Add Question'}
                            </button>
                        </div>
                    </div>
                </div>
            )}
            {confirmDialog}
        </CastSection>
    );
}

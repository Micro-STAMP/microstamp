import {
    icaTypeSelectOptions,
    IIcaType,
    IInadequateControlActionInsertDto,
    IInadequateControlActionReadDto,
    IInadequateControlActionUpdateDto
} from "@interfaces/CAST/IStep3/IInadequateControlAction";
import { SelectOption } from "@components/FormField/Templates";
import Select from "@components/FormField/Select";
import castStyles from "@pages/AnalysisSteps/CAST/Step1/CastStepOne.module.css";
import { useEffect, useState } from "react";
import { useQuery } from "@tanstack/react-query";
import { BiSave, BiX, BiCheckShield } from "react-icons/bi";
import { toast } from "sonner";
import { getResponsibilities } from "@http/Step2/Responsibilities";
import styles from "./ModalInadequateControlAction.module.css";

interface ModalInadequateControlActionProps {
    open: boolean;
    onClose: () => void;
    analysisId: string;
    componentOptions: SelectOption[];
    ica?: IInadequateControlActionReadDto | null;
    isLoading?: boolean;
    onCreate: (data: IInadequateControlActionInsertDto) => Promise<void>;
    onUpdate: (id: string, data: IInadequateControlActionUpdateDto) => Promise<void>;
}

const EMPTY_FORM = {
    componentId: "",
    code: "",
    controlActionName: "",
    type: IIcaType.NOT_PROVIDED,
    description: "",
    context: "",
    processModelFlaw: "",
    questions: ""
};

export default function ModalInadequateControlAction({
    open,
    onClose,
    analysisId,
    componentOptions,
    ica,
    isLoading = false,
    onCreate,
    onUpdate
}: ModalInadequateControlActionProps) {
    const isEditMode = !!ica;
    const [form, setForm] = useState(EMPTY_FORM);

    const { data: responsibilities, isLoading: isLoadingResponsibilities } = useQuery({
        queryKey: ['component-responsibilities', form.componentId],
        queryFn: () => getResponsibilities(form.componentId),
        enabled: !!form.componentId
    });

    useEffect(() => {
        if (!open) return;

        if (ica) {
            setForm({
                componentId: ica.componentId,
                code: ica.code,
                controlActionName: ica.controlActionName,
                type: ica.type,
                description: ica.description || "",
                context: ica.context || "",
                processModelFlaw: ica.processModelFlaw || "",
                questions: ica.questions || ""
            });
        } else {
            setForm({ ...EMPTY_FORM, componentId: componentOptions[0]?.value || "" });
        }
    }, [open, ica, componentOptions]);

    if (!open) return null;

    const handleSubmit = async () => {
        if (!form.controlActionName.trim()) {
            toast.warning("Fill in the required fields.");
            return;
        }

        if (isEditMode && ica) {
            await onUpdate(ica.id, {
                controlActionName: form.controlActionName,
                type: form.type,
                description: form.description,
                context: form.context,
                processModelFlaw: form.processModelFlaw,
                questions: form.questions
            });
        } else {
            if (!form.componentId || !form.code.trim()) {
                toast.warning("Select a component and fill in the code.");
                return;
            }
            await onCreate({
                analysisId,
                componentId: form.componentId,
                code: form.code,
                controlActionName: form.controlActionName,
                type: form.type,
                description: form.description,
                context: form.context,
                processModelFlaw: form.processModelFlaw,
                questions: form.questions
            });
        }

        onClose();
    };

    return (
        <div className={styles.overlay} onClick={onClose}>
            <div className={styles.dialog} onClick={e => e.stopPropagation()}>
                <div className={styles.header}>
                    <h3 className={styles.title}>
                        {isEditMode ? "Edit Component Analysis" : "New Component Analysis"}
                    </h3>
                    <button className={styles.closeBtn} onClick={onClose} type="button">
                        <BiX size={22} />
                    </button>
                </div>

                <div className={styles.body}>
                    <div className={`${styles.row} ${styles.double}`}>
                        <div>
                            <Select
                                label="Component"
                                options={componentOptions}
                                value={componentOptions.find(o => o.value === form.componentId) || null}
                                onChange={opt => setForm({ ...form, componentId: opt?.value || "" })}
                                disabled={isEditMode}
                                required
                            />
                        </div>
                        <div>
                            <label className={castStyles.inputLabel}>Code</label>
                            <input
                                type="text"
                                placeholder="CA-1"
                                className={castStyles.inputField}
                                style={{ marginBottom: 0 }}
                                value={form.code}
                                disabled={isEditMode}
                                onChange={e => setForm({ ...form, code: e.target.value })}
                            />
                        </div>
                    </div>

                    {form.componentId && (
                        <div>
                            <label className={castStyles.inputLabel}>Safety-Related Responsibilities</label>
                            <div style={{ display: 'flex', flexDirection: 'column', gap: '8px', backgroundColor: 'var(--color-dark)', border: '1px solid var(--color-gray)', borderRadius: '8px', padding: '12px' }}>
                                {isLoadingResponsibilities ? (
                                    <span style={{ color: 'var(--color-muted-text)', fontSize: '13px' }}>Loading responsibilities...</span>
                                ) : responsibilities && responsibilities.length > 0 ? (
                                    responsibilities.map(resp => (
                                        <div key={resp.id} style={{ display: 'flex', alignItems: 'flex-start', gap: '8px' }}>
                                            <BiCheckShield size={16} color="var(--color-yellow)" style={{ marginTop: '2px', flexShrink: 0 }} />
                                            <span style={{ fontSize: '13px', color: '#d1d5db', lineHeight: '1.5' }}>
                                                <strong style={{ color: 'var(--color-yellow)' }}>{resp.code}: </strong>
                                                {resp.responsibility}
                                            </span>
                                        </div>
                                    ))
                                ) : (
                                    <span style={{ color: 'var(--color-muted-text)', fontSize: '13px' }}>No Safety-Related Responsibilities registered for this component in Step 2 yet.</span>
                                )}
                            </div>
                            <p className={styles.helperText}>
                                What this component was supposed to do, as registered in Step 2 — use it to compare against its actual contribution below.
                            </p>
                        </div>
                    )}

                    <div className={`${styles.row} ${styles.double}`}>
                        <div>
                            <label className={castStyles.inputLabel}>Control Action Name</label>
                            <input
                                type="text"
                                placeholder="E.g., Release Brakes"
                                className={castStyles.inputField}
                                style={{ marginBottom: 0 }}
                                value={form.controlActionName}
                                onChange={e => setForm({ ...form, controlActionName: e.target.value })}
                            />
                        </div>
                        <div>
                            <Select
                                label="Control Action Category"
                                options={icaTypeSelectOptions}
                                value={icaTypeSelectOptions.find(o => o.value === form.type) || null}
                                onChange={opt => setForm({ ...form, type: (opt?.value as IIcaType) ?? IIcaType.NOT_PROVIDED })}
                                required
                            />
                        </div>
                    </div>

                    <div>
                        <label className={castStyles.inputLabel}>Contribution / Description</label>
                        <textarea
                            rows={2}
                            className={castStyles.inputField}
                            style={{ marginBottom: 0 }}
                            value={form.description}
                            onChange={e => setForm({ ...form, description: e.target.value })}
                        />
                        <p className={styles.helperText}>
                            Describe how this component's behavior contributed to the hazardous state.
                        </p>
                    </div>

                    <div>
                        <label className={castStyles.inputLabel}>Context / Contextual Factors</label>
                        <textarea
                            rows={3}
                            className={castStyles.inputField}
                            style={{ marginBottom: 0 }}
                            value={form.context}
                            onChange={e => setForm({ ...form, context: e.target.value })}
                        />
                        <p className={styles.helperText}>
                            Why did it make sense for the controller to act this way at the time of the accident?
                        </p>
                    </div>

                    <div>
                        <label className={castStyles.inputLabel}>Process Model Flaw</label>
                        <textarea
                            rows={3}
                            className={castStyles.inputField}
                            style={{ marginBottom: 0 }}
                            value={form.processModelFlaw}
                            onChange={e => setForm({ ...form, processModelFlaw: e.target.value })}
                        />
                        <p className={styles.helperText}>
                            What was wrong or incomplete in the controller's process/mental model that led to this control action?
                        </p>
                    </div>

                    <div>
                        <label className={castStyles.inputLabel}>Investigative / Unanswered Questions</label>
                        <textarea
                            rows={3}
                            className={castStyles.inputField}
                            style={{ marginBottom: 0 }}
                            value={form.questions}
                            onChange={e => setForm({ ...form, questions: e.target.value })}
                        />
                        <p className={styles.helperText}>
                            Open questions this component-level analysis couldn't resolve — e.g., organizational, managerial, or regulatory causes to pick up in Step 4 (Systemic Factors).
                        </p>
                    </div>
                </div>

                <div className={styles.footer}>
                    <button className={castStyles.btnBack} onClick={onClose} type="button">
                        <BiX size={18} /> Cancel
                    </button>
                    <button
                        className={castStyles.btnSaveForm}
                        onClick={handleSubmit}
                        disabled={isLoading || !form.controlActionName.trim()}
                        type="button"
                    >
                        <BiSave size={18} /> {isLoading ? "Saving..." : isEditMode ? "Save Changes" : "Add Component Analysis"}
                    </button>
                </div>
            </div>
        </div>
    );
}

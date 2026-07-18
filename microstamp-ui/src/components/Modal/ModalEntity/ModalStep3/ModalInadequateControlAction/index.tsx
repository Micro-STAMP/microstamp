import {
    icaTypeSelectOptions,
    IIcaType,
    IInadequateControlActionInsertDto,
    IInadequateControlActionReadDto,
    IInadequateControlActionUpdateDto
} from "@interfaces/CAST/IStep3/IInadequateControlAction";
import { SelectOption } from "@components/FormField/Templates";
import castStyles from "@pages/AnalysisSteps/CAST/Step1/CastStepOne.module.css";
import { useEffect, useState } from "react";
import { BiSave, BiX } from "react-icons/bi";
import { toast } from "sonner";
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
    processModelFlaw: ""
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
                processModelFlaw: ica.processModelFlaw || ""
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
                processModelFlaw: form.processModelFlaw
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
                processModelFlaw: form.processModelFlaw
            });
        }

        onClose();
    };

    return (
        <div className={styles.overlay} onClick={onClose}>
            <div className={styles.dialog} onClick={e => e.stopPropagation()}>
                <div className={styles.header}>
                    <h3 className={styles.title}>
                        {isEditMode ? "Edit Inadequate Control Action" : "New Inadequate Control Action"}
                    </h3>
                    <button className={styles.closeBtn} onClick={onClose} type="button">
                        <BiX size={22} />
                    </button>
                </div>

                <div className={styles.body}>
                    <div className={`${styles.row} ${styles.double}`}>
                        <div>
                            <label className={castStyles.inputLabel}>Component</label>
                            <select
                                className={castStyles.inputField}
                                style={{ marginBottom: 0, cursor: isEditMode ? "default" : "pointer" }}
                                value={form.componentId}
                                disabled={isEditMode}
                                onChange={e => setForm({ ...form, componentId: e.target.value })}
                            >
                                {componentOptions.length === 0 && <option value="">No components available</option>}
                                {componentOptions.map(option => (
                                    <option key={option.value} value={option.value}>
                                        {option.label}
                                    </option>
                                ))}
                            </select>
                        </div>
                        <div>
                            <label className={castStyles.inputLabel}>Code</label>
                            <input
                                type="text"
                                placeholder="ICA-1"
                                className={castStyles.inputField}
                                style={{ marginBottom: 0 }}
                                value={form.code}
                                disabled={isEditMode}
                                onChange={e => setForm({ ...form, code: e.target.value })}
                            />
                        </div>
                    </div>

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
                            <label className={castStyles.inputLabel}>ICA Type</label>
                            <select
                                className={castStyles.inputField}
                                style={{ marginBottom: 0, cursor: "pointer" }}
                                value={form.type}
                                onChange={e => setForm({ ...form, type: e.target.value as IIcaType })}
                            >
                                {icaTypeSelectOptions.map(option => (
                                    <option key={option.value} value={option.value}>
                                        {option.label}
                                    </option>
                                ))}
                            </select>
                        </div>
                    </div>

                    <div>
                        <label className={castStyles.inputLabel}>Description</label>
                        <textarea
                            rows={2}
                            className={castStyles.inputField}
                            style={{ marginBottom: 0 }}
                            value={form.description}
                            onChange={e => setForm({ ...form, description: e.target.value })}
                        />
                    </div>

                    <div>
                        <label className={castStyles.inputLabel}>Context</label>
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
                        <BiSave size={18} /> {isLoading ? "Saving..." : isEditMode ? "Save Changes" : "Add ICA"}
                    </button>
                </div>
            </div>
        </div>
    );
}

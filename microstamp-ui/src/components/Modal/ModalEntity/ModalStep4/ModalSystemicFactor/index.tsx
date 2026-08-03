import {
    systemicFactorCategorySelectOptions,
    ISystemicFactorCategory,
    ISystemicFactorInsertDto,
    ISystemicFactorReadDto,
    ISystemicFactorUpdateDto
} from "@interfaces/CAST/IStep4/ISystemicFactor";
import castStyles from "@pages/AnalysisSteps/CAST/Step1/CastStepOne.module.css";
import { useEffect, useState } from "react";
import { BiSave, BiX } from "react-icons/bi";
import { toast } from "sonner";
import styles from "./ModalSystemicFactor.module.css";

interface IcaOption {
    id: string;
    code: string;
    controlActionName: string;
}

interface ModalSystemicFactorProps {
    open: boolean;
    onClose: () => void;
    analysisId: string;
    icaOptions: IcaOption[];
    systemicFactor?: ISystemicFactorReadDto | null;
    isLoading?: boolean;
    onCreate: (data: ISystemicFactorInsertDto) => Promise<void>;
    onUpdate: (id: string, data: ISystemicFactorUpdateDto) => Promise<void>;
}

const EMPTY_FORM = {
    category: ISystemicFactorCategory.COMMUNICATION_AND_COORDINATION,
    description: "",
    inadequateControlActionIds: [] as string[]
};

export default function ModalSystemicFactor({
    open,
    onClose,
    analysisId,
    icaOptions,
    systemicFactor,
    isLoading = false,
    onCreate,
    onUpdate
}: ModalSystemicFactorProps) {
    const isEditMode = !!systemicFactor;
    const [form, setForm] = useState(EMPTY_FORM);

    useEffect(() => {
        if (!open) return;

        if (systemicFactor) {
            setForm({
                category: systemicFactor.category,
                description: systemicFactor.description || "",
                inadequateControlActionIds: systemicFactor.inadequateControlActionIds || []
            });
        } else {
            setForm(EMPTY_FORM);
        }
    }, [open, systemicFactor]);

    if (!open) return null;

    const toggleIca = (icaId: string) => {
        setForm(prev => ({
            ...prev,
            inadequateControlActionIds: prev.inadequateControlActionIds.includes(icaId)
                ? prev.inadequateControlActionIds.filter(id => id !== icaId)
                : [...prev.inadequateControlActionIds, icaId]
        }));
    };

    const handleSubmit = async () => {
        if (!form.description.trim()) {
            toast.warning("Fill in the required fields.");
            return;
        }

        if (isEditMode && systemicFactor) {
            await onUpdate(systemicFactor.id, {
                category: form.category,
                description: form.description,
                inadequateControlActionIds: form.inadequateControlActionIds
            });
        } else {
            await onCreate({
                analysisId,
                category: form.category,
                description: form.description,
                inadequateControlActionIds: form.inadequateControlActionIds
            });
        }

        onClose();
    };

    return (
        <div className={styles.overlay} onClick={onClose}>
            <div className={styles.dialog} onClick={e => e.stopPropagation()}>
                <div className={styles.header}>
                    <h3 className={styles.title}>
                        {isEditMode ? "Edit Systemic Factor" : "New Systemic Factor"}
                    </h3>
                    <button className={styles.closeBtn} onClick={onClose} type="button">
                        <BiX size={22} />
                    </button>
                </div>

                <div className={styles.body}>
                    <div>
                        <label className={castStyles.inputLabel}>Category</label>
                        <select
                            className={castStyles.inputField}
                            style={{ marginBottom: 0, cursor: "pointer" }}
                            value={form.category}
                            onChange={e => setForm({ ...form, category: e.target.value as ISystemicFactorCategory })}
                        >
                            {systemicFactorCategorySelectOptions.map(option => (
                                <option key={option.value} value={option.value}>
                                    {option.label}
                                </option>
                            ))}
                        </select>
                        <p className={styles.helperText}>
                            Systemic factors span the individual components of the safety control structure (CAST Handbook, Leveson 2019).
                        </p>
                    </div>

                    <div>
                        <label className={castStyles.inputLabel}>Description</label>
                        <textarea
                            rows={4}
                            className={castStyles.inputField}
                            style={{ marginBottom: 0 }}
                            value={form.description}
                            onChange={e => setForm({ ...form, description: e.target.value })}
                        />
                    </div>

                    <div>
                        <label className={castStyles.inputLabel}>Linked Inadequate Control Actions</label>
                        <div className={styles.icaCheckboxList}>
                            {icaOptions.length === 0 ? (
                                <p className={styles.icaEmptyState}>No Inadequate Control Actions registered in Step 3 yet.</p>
                            ) : (
                                icaOptions.map(ica => {
                                    const checked = form.inadequateControlActionIds.includes(ica.id);
                                    return (
                                        <label
                                            key={ica.id}
                                            className={`${styles.icaCheckboxRow} ${checked ? styles.icaCheckboxRowActive : ""}`}
                                        >
                                            <input
                                                type="checkbox"
                                                checked={checked}
                                                onChange={() => toggleIca(ica.id)}
                                            />
                                            <span className={styles.icaCode}>{ica.code}</span>
                                            <span className={styles.icaName}>{ica.controlActionName}</span>
                                        </label>
                                    );
                                })
                            )}
                        </div>
                        <p className={styles.helperText}>
                            Select every Inadequate Control Action (Step 3) that this systemic factor helps explain.
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
                        disabled={isLoading || !form.description.trim()}
                        type="button"
                    >
                        <BiSave size={18} /> {isLoading ? "Saving..." : isEditMode ? "Save Changes" : "Add Systemic Factor"}
                    </button>
                </div>
            </div>
        </div>
    );
}

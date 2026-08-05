import {
    IRecommendationInsertDto,
    IRecommendationReadDto,
    IRecommendationUpdateDto
} from "@interfaces/CAST/IStep5/IRecommendation";
import castStyles from "@pages/AnalysisSteps/CAST/Step1/CastStepOne.module.css";
import { useEffect, useState } from "react";
import { BiSave, BiX } from "react-icons/bi";
import { toast } from "sonner";
import styles from "./ModalRecommendation.module.css";

interface IcaOption {
    id: string;
    code: string;
    controlActionName: string;
}

interface SystemicFactorOption {
    id: string;
    categoryLabel: string;
    description: string;
}

interface ModalRecommendationProps {
    open: boolean;
    onClose: () => void;
    analysisId: string;
    icaOptions: IcaOption[];
    systemicFactorOptions: SystemicFactorOption[];
    recommendation?: IRecommendationReadDto | null;
    isLoading?: boolean;
    onCreate: (data: IRecommendationInsertDto) => Promise<void>;
    onUpdate: (id: string, data: IRecommendationUpdateDto) => Promise<void>;
}

const EMPTY_FORM = {
    description: "",
    inadequateControlActionIds: [] as string[],
    systemicFactorIds: [] as string[]
};

export default function ModalRecommendation({
    open,
    onClose,
    analysisId,
    icaOptions,
    systemicFactorOptions,
    recommendation,
    isLoading = false,
    onCreate,
    onUpdate
}: ModalRecommendationProps) {
    const isEditMode = !!recommendation;
    const [form, setForm] = useState(EMPTY_FORM);

    useEffect(() => {
        if (!open) return;

        if (recommendation) {
            setForm({
                description: recommendation.description || "",
                inadequateControlActionIds: recommendation.inadequateControlActionIds || [],
                systemicFactorIds: recommendation.systemicFactorIds || []
            });
        } else {
            setForm(EMPTY_FORM);
        }
    }, [open, recommendation]);

    if (!open) return null;

    const toggleIca = (icaId: string) => {
        setForm(prev => ({
            ...prev,
            inadequateControlActionIds: prev.inadequateControlActionIds.includes(icaId)
                ? prev.inadequateControlActionIds.filter(id => id !== icaId)
                : [...prev.inadequateControlActionIds, icaId]
        }));
    };

    const toggleSystemicFactor = (systemicFactorId: string) => {
        setForm(prev => ({
            ...prev,
            systemicFactorIds: prev.systemicFactorIds.includes(systemicFactorId)
                ? prev.systemicFactorIds.filter(id => id !== systemicFactorId)
                : [...prev.systemicFactorIds, systemicFactorId]
        }));
    };

    const handleSubmit = async () => {
        if (!form.description.trim()) {
            toast.warning("Fill in the required fields.");
            return;
        }

        if (isEditMode && recommendation) {
            await onUpdate(recommendation.id, {
                description: form.description,
                inadequateControlActionIds: form.inadequateControlActionIds,
                systemicFactorIds: form.systemicFactorIds
            });
        } else {
            await onCreate({
                analysisId,
                description: form.description,
                inadequateControlActionIds: form.inadequateControlActionIds,
                systemicFactorIds: form.systemicFactorIds
            });
        }

        onClose();
    };

    return (
        <div className={styles.overlay} onClick={onClose}>
            <div className={styles.dialog} onClick={e => e.stopPropagation()}>
                <div className={styles.header}>
                    <h3 className={styles.title}>
                        {isEditMode ? "Edit Recommendation" : "New Recommendation"}
                    </h3>
                    <button className={styles.closeBtn} onClick={onClose} type="button">
                        <BiX size={22} />
                    </button>
                </div>

                <div className={styles.body}>
                    <div>
                        <label className={castStyles.inputLabel}>Description</label>
                        <textarea
                            rows={4}
                            className={castStyles.inputField}
                            style={{ marginBottom: 0 }}
                            value={form.description}
                            onChange={e => setForm({ ...form, description: e.target.value })}
                        />
                        <p className={styles.helperText}>
                            Describe the corrective action or systemic change recommended to prevent recurrence (CAST Handbook, Leveson 2019 — Step 5: Create an Improvement Program).
                        </p>
                    </div>

                    <div className={styles.selectPanelsRow}>
                        <div className={styles.selectPanelColumn}>
                            <label className={castStyles.inputLabel}>Linked Inadequate Control Actions</label>
                            <div className={styles.checkboxList}>
                                {icaOptions.length === 0 ? (
                                    <p className={styles.emptyState}>No Inadequate Control Actions registered in Step 3 yet.</p>
                                ) : (
                                    icaOptions.map(ica => {
                                        const checked = form.inadequateControlActionIds.includes(ica.id);
                                        return (
                                            <label
                                                key={ica.id}
                                                className={`${styles.checkboxRow} ${checked ? styles.checkboxRowActive : ""}`}
                                            >
                                                <input
                                                    type="checkbox"
                                                    checked={checked}
                                                    onChange={() => toggleIca(ica.id)}
                                                />
                                                <span className={styles.itemText}>
                                                    <span className={styles.itemCode}>{ica.code}</span>
                                                    <span className={styles.itemName}>{ica.controlActionName}</span>
                                                </span>
                                            </label>
                                        );
                                    })
                                )}
                            </div>
                            <p className={styles.helperText}>
                                Select every Inadequate Control Action (Step 3) this recommendation addresses.
                            </p>
                        </div>

                        <div className={styles.selectPanelColumn}>
                            <label className={castStyles.inputLabel}>Linked Systemic Factors</label>
                            <div className={styles.checkboxList}>
                                {systemicFactorOptions.length === 0 ? (
                                    <p className={styles.emptyState}>No Systemic Factors registered in Step 4 yet.</p>
                                ) : (
                                    systemicFactorOptions.map(factor => {
                                        const checked = form.systemicFactorIds.includes(factor.id);
                                        return (
                                            <label
                                                key={factor.id}
                                                className={`${styles.checkboxRow} ${checked ? styles.checkboxRowActive : ""}`}
                                            >
                                                <input
                                                    type="checkbox"
                                                    checked={checked}
                                                    onChange={() => toggleSystemicFactor(factor.id)}
                                                />
                                                <span className={styles.itemText}>
                                                    <span className={styles.itemCode}>{factor.categoryLabel}</span>
                                                    <span className={styles.itemName}>{factor.description}</span>
                                                </span>
                                            </label>
                                        );
                                    })
                                )}
                            </div>
                            <p className={styles.helperText}>
                                Select every Systemic Factor (Step 4) this recommendation addresses.
                            </p>
                        </div>
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
                        <BiSave size={18} /> {isLoading ? "Saving..." : isEditMode ? "Save Changes" : "Add Recommendation"}
                    </button>
                </div>
            </div>
        </div>
    );
}

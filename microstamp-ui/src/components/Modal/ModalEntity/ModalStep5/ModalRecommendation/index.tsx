import {
    IRecommendationInsertDto,
    IRecommendationReadDto,
    IRecommendationUpdateDto,
    IRecommendationPriority,
    recommendationPrioritySelectOptions
} from "@interfaces/CAST/IStep5/IRecommendation";
import { SelectOption } from "@components/FormField/Templates";
import Select from "@components/FormField/Select";
import castStyles from "@pages/AnalysisSteps/CAST/Step1/CastStepOne.module.css";
import { useEffect, useState } from "react";
import { BiSave, BiX } from "react-icons/bi";
import { toast } from "sonner";
import CastCheckbox from "@components/CastCheckbox";
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
    componentOptions: SelectOption[];
    recommendation?: IRecommendationReadDto | null;
    isLoading?: boolean;
    onCreate: (data: IRecommendationInsertDto) => Promise<void>;
    onUpdate: (id: string, data: IRecommendationUpdateDto) => Promise<void>;
}

const EMPTY_FORM = {
    description: "",
    inadequateControlActionIds: [] as string[],
    systemicFactorIds: [] as string[],
    componentId: "",
    priority: IRecommendationPriority.IMMEDIATE,
    auditMechanism: ""
};

export default function ModalRecommendation({
    open,
    onClose,
    analysisId,
    icaOptions,
    systemicFactorOptions,
    componentOptions,
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
                systemicFactorIds: recommendation.systemicFactorIds || [],
                componentId: recommendation.componentId || "",
                priority: recommendation.priority || IRecommendationPriority.IMMEDIATE,
                auditMechanism: recommendation.auditMechanism || ""
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
                systemicFactorIds: form.systemicFactorIds,
                componentId: form.componentId || undefined,
                priority: form.priority,
                auditMechanism: form.auditMechanism
            });
        } else {
            await onCreate({
                analysisId,
                description: form.description,
                inadequateControlActionIds: form.inadequateControlActionIds,
                systemicFactorIds: form.systemicFactorIds,
                componentId: form.componentId || undefined,
                priority: form.priority,
                auditMechanism: form.auditMechanism
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
                            Describe the corrective action or systemic change recommended to prevent recurrence (CAST Handbook, Leveson 2019 — Step 5: Create Improvement Program).
                        </p>
                    </div>

                    <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '16px' }}>
                        <div>
                            <Select
                                label="Target Controller"
                                options={componentOptions}
                                value={componentOptions.find(o => o.value === form.componentId) || null}
                                onChange={opt => setForm({ ...form, componentId: opt?.value || "" })}
                            />
                            <p className={styles.helperText}>
                                Who is responsible for implementing this recommendation? (CAST Handbook — recommendations must be assigned to a responsible controller.) Leave as None for structural / organizational recommendations.
                            </p>
                        </div>
                        <div>
                            <Select
                                label="Priority / Timeframe"
                                options={recommendationPrioritySelectOptions}
                                value={recommendationPrioritySelectOptions.find(o => o.value === form.priority) || null}
                                onChange={opt => setForm({ ...form, priority: (opt?.value as IRecommendationPriority) ?? IRecommendationPriority.IMMEDIATE })}
                                required
                            />
                            <p className={styles.helperText}>
                                Immediate containment fix, or a deeper long-term structural change?
                            </p>
                        </div>
                    </div>

                    <div>
                        <label className={castStyles.inputLabel}>Feedback / Audit Mechanism</label>
                        <textarea
                            rows={2}
                            className={castStyles.inputField}
                            style={{ marginBottom: 0 }}
                            value={form.auditMechanism}
                            onChange={e => setForm({ ...form, auditMechanism: e.target.value })}
                        />
                        <p className={styles.helperText}>
                            How will the organization verify this recommendation was implemented and is actually effective (e.g., audits, inspections, leading indicators)?
                        </p>
                    </div>

                    <div className={styles.selectPanelsRow}>
                        <div className={styles.selectPanelColumn}>
                            <label className={castStyles.inputLabel}>Linked Component Analyses</label>
                            <div className={styles.checkboxList}>
                                {icaOptions.length === 0 ? (
                                    <p className={styles.emptyState}>No Component Analysis registered in Step 3 yet.</p>
                                ) : (
                                    icaOptions.map(ica => {
                                        const checked = form.inadequateControlActionIds.includes(ica.id);
                                        return (
                                            <label
                                                key={ica.id}
                                                className={`${styles.checkboxRow} ${checked ? styles.checkboxRowActive : ""}`}
                                            >
                                                <CastCheckbox
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
                                Select every Component Analysis (Step 3) this recommendation addresses.
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
                                                <CastCheckbox
                                                    checked={checked}
                                                    onChange={() => toggleSystemicFactor(factor.id)}
                                                />
                                                <span className={styles.itemText}>
                                                    <span className={styles.itemCategory}>{factor.categoryLabel}</span>
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

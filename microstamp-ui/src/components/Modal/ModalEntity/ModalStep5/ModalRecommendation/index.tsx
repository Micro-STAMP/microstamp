import {
    IRecommendationInsertDto,
    IRecommendationReadDto,
    IRecommendationUpdateDto,
    IRecommendationPriority,
    recommendationPrioritySelectOptions,
    IRecommendationStatus,
    recommendationStatusSelectOptions,
    IRecommendationType,
    recommendationTypeSelectOptions,
    IRecommendationScopeCategory,
    recommendationScopeCategorySelectOptions
} from "@interfaces/CAST/IStep5/IRecommendation";
import { SelectOption } from "@components/FormField/Templates";
import Select from "@components/FormField/Select";
import castStyles from "@pages/AnalysisSteps/CAST/Step1/CastStepOne.module.css";
import { useEffect, useState } from "react";
import { BiSave, BiX } from "react-icons/bi";
import { toast } from "sonner";
import CastCheckbox from "@components/CastCheckbox";
import CastTextarea from "@components/CastTextarea";
import styles from "./ModalRecommendation.module.css";

interface CacOption {
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
    cacOptions: CacOption[];
    systemicFactorOptions: SystemicFactorOption[];
    componentOptions: SelectOption[];
    recommendation?: IRecommendationReadDto | null;
    isLoading?: boolean;
    onCreate: (data: IRecommendationInsertDto) => Promise<void>;
    onUpdate: (id: string, data: IRecommendationUpdateDto) => Promise<void>;
}

const EMPTY_FORM = {
    description: "",
    controlActionContributionIds: [] as string[],
    systemicFactorIds: [] as string[],
    componentId: "",
    priority: IRecommendationPriority.IMMEDIATE,
    auditMechanism: "",
    assignee: "",
    dueDate: "",
    status: IRecommendationStatus.PROPOSED,
    recommendationType: "" as IRecommendationType | "",
    scopeCategory: "" as IRecommendationScopeCategory | "",
    effectivenessEvidence: ""
};

export default function ModalRecommendation({
    open,
    onClose,
    analysisId,
    cacOptions,
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
                controlActionContributionIds: recommendation.controlActionContributionIds || [],
                systemicFactorIds: recommendation.systemicFactorIds || [],
                componentId: recommendation.componentId || "",
                priority: recommendation.priority || IRecommendationPriority.IMMEDIATE,
                auditMechanism: recommendation.auditMechanism || "",
                assignee: recommendation.assignee || "",
                dueDate: recommendation.dueDate || "",
                status: recommendation.status || IRecommendationStatus.PROPOSED,
                recommendationType: recommendation.recommendationType || "",
                scopeCategory: recommendation.scopeCategory || "",
                effectivenessEvidence: recommendation.effectivenessEvidence || ""
            });
        } else {
            setForm(EMPTY_FORM);
        }
    }, [open, recommendation]);

    if (!open) return null;

    const toggleCac = (cacId: string) => {
        setForm(prev => ({
            ...prev,
            controlActionContributionIds: prev.controlActionContributionIds.includes(cacId)
                ? prev.controlActionContributionIds.filter(id => id !== cacId)
                : [...prev.controlActionContributionIds, cacId]
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

        if (form.status === IRecommendationStatus.EFFECTIVE && !form.effectivenessEvidence.trim()) {
            toast.warning("Describe the audit findings / effectiveness evidence before marking this Recommendation as Effective.");
            return;
        }

        const payload = {
            description: form.description,
            controlActionContributionIds: form.controlActionContributionIds,
            systemicFactorIds: form.systemicFactorIds,
            componentId: form.componentId || undefined,
            priority: form.priority,
            auditMechanism: form.auditMechanism,
            assignee: form.assignee || undefined,
            dueDate: form.dueDate || undefined,
            status: form.status,
            recommendationType: form.recommendationType || undefined,
            scopeCategory: form.scopeCategory || undefined,
            effectivenessEvidence: form.effectivenessEvidence || undefined
        };

        if (isEditMode && recommendation) {
            await onUpdate(recommendation.id, payload);
        } else {
            await onCreate({ analysisId, ...payload });
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
                        <CastTextarea
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

                    <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '16px' }}>
                        <div>
                            <label className={castStyles.inputLabel}>Assignee</label>
                            <input
                                type="text"
                                className={castStyles.inputField}
                                placeholder="e.g. Maintenance Management / Eng. Silva"
                                value={form.assignee}
                                onChange={e => setForm({ ...form, assignee: e.target.value })}
                            />
                            <p className={styles.helperText}>
                                Who (person/role) is accountable for carrying out this recommendation within the deadline below.
                            </p>
                        </div>
                        <div>
                            <label className={castStyles.inputLabel}>Due Date</label>
                            <input
                                type="date"
                                className={castStyles.inputField}
                                value={form.dueDate}
                                onChange={e => setForm({ ...form, dueDate: e.target.value })}
                            />
                            <p className={styles.helperText}>
                                Established deadline for implementation (CAST Handbook — Assigning Responsibility for Implementation).
                            </p>
                        </div>
                    </div>

                    <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '16px' }}>
                        <div>
                            <Select
                                label="Implementation Status"
                                options={recommendationStatusSelectOptions}
                                value={recommendationStatusSelectOptions.find(o => o.value === form.status) || null}
                                onChange={opt => setForm({ ...form, status: (opt?.value as IRecommendationStatus) ?? IRecommendationStatus.PROPOSED })}
                                required
                            />
                            <p className={styles.helperText}>
                                Track whether the recommendation is still on paper or has been verified as effective.
                            </p>
                        </div>
                        <div>
                            <Select
                                label="Recommendation Type"
                                options={recommendationTypeSelectOptions}
                                value={recommendationTypeSelectOptions.find(o => o.value === form.recommendationType) || null}
                                onChange={opt => setForm({ ...form, recommendationType: (opt?.value as IRecommendationType) ?? "" })}
                            />
                            <p className={styles.helperText}>
                                Nature of the change, independent of its urgency (short-term fix, long-term structural change, or re-evaluating a failed design assumption).
                            </p>
                        </div>
                    </div>

                    <div>
                        <Select
                            label="Systemic Scope"
                            options={recommendationScopeCategorySelectOptions}
                            value={recommendationScopeCategorySelectOptions.find(o => o.value === form.scopeCategory) || null}
                            onChange={opt => setForm({ ...form, scopeCategory: (opt?.value as IRecommendationScopeCategory) ?? "" })}
                        />
                        <p className={styles.helperText}>
                            Where in the sociotechnical control structure this recommendation acts (CAST Handbook — Systemic Scope).
                        </p>
                    </div>

                    <div>
                        <label className={castStyles.inputLabel}>Feedback / Audit Mechanism</label>
                        <CastTextarea
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

                    <div>
                        <label className={castStyles.inputLabel}>
                            Audit Findings / Effectiveness Evidence
                            {form.status === IRecommendationStatus.EFFECTIVE && " *"}
                        </label>
                        <CastTextarea
                            rows={2}
                            className={castStyles.inputField}
                            style={{ marginBottom: 0 }}
                            value={form.effectivenessEvidence}
                            onChange={e => setForm({ ...form, effectivenessEvidence: e.target.value })}
                        />
                        <p className={styles.helperText}>
                            Evidence (audit findings, leading indicators, inspections) proving the change actually strengthened the safety control structure. Required once the status is set to "Effective".
                        </p>
                    </div>

                    <div className={styles.selectPanelsRow}>
                        <div className={styles.selectPanelColumn}>
                            <label className={castStyles.inputLabel}>Linked CACs</label>
                            <div className={styles.checkboxList}>
                                {cacOptions.length === 0 ? (
                                    <p className={styles.emptyState}>No CACs registered in Step 3 yet.</p>
                                ) : (
                                    cacOptions.map(cac => {
                                        const checked = form.controlActionContributionIds.includes(cac.id);
                                        return (
                                            <label
                                                key={cac.id}
                                                className={`${styles.checkboxRow} ${checked ? styles.checkboxRowActive : ""}`}
                                            >
                                                <CastCheckbox
                                                    checked={checked}
                                                    onChange={() => toggleCac(cac.id)}
                                                />
                                                <span className={styles.itemText}>
                                                    <span className={styles.itemCode}>{cac.code}</span>
                                                    <span className={styles.itemName}>{cac.controlActionName}</span>
                                                </span>
                                            </label>
                                        );
                                    })
                                )}
                            </div>
                            <p className={styles.helperText}>
                                Select every CAC (Step 3) this recommendation addresses.
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

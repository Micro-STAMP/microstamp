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
import CastCheckbox from "@components/CastCheckbox";
import Select from "@components/FormField/Select";
import styles from "./ModalSystemicFactor.module.css";

interface CacOption {
    id: string;
    code: string;
    controlActionName: string;
}

interface ModalSystemicFactorProps {
    open: boolean;
    onClose: () => void;
    analysisId: string;
    cacOptions: CacOption[];
    systemicFactor?: ISystemicFactorReadDto | null;
    isLoading?: boolean;
    onCreate: (data: ISystemicFactorInsertDto) => Promise<void>;
    onUpdate: (id: string, data: ISystemicFactorUpdateDto) => Promise<void>;
}

const EMPTY_FORM = {
    category: ISystemicFactorCategory.COMMUNICATION_AND_COORDINATION,
    description: "",
    controlActionContributionIds: [] as string[]
};

export default function ModalSystemicFactor({
    open,
    onClose,
    analysisId,
    cacOptions,
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
                controlActionContributionIds: systemicFactor.controlActionContributionIds || []
            });
        } else {
            setForm(EMPTY_FORM);
        }
    }, [open, systemicFactor]);

    if (!open) return null;

    const toggleCac = (cacId: string) => {
        setForm(prev => ({
            ...prev,
            controlActionContributionIds: prev.controlActionContributionIds.includes(cacId)
                ? prev.controlActionContributionIds.filter(id => id !== cacId)
                : [...prev.controlActionContributionIds, cacId]
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
                controlActionContributionIds: form.controlActionContributionIds
            });
        } else {
            await onCreate({
                analysisId,
                category: form.category,
                description: form.description,
                controlActionContributionIds: form.controlActionContributionIds
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
                        <Select
                            label="Category"
                            options={systemicFactorCategorySelectOptions}
                            value={systemicFactorCategorySelectOptions.find(o => o.value === form.category) || null}
                            onChange={opt => setForm({ ...form, category: (opt?.value as ISystemicFactorCategory) ?? ISystemicFactorCategory.COMMUNICATION_AND_COORDINATION })}
                            required
                        />
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
                        <label className={castStyles.inputLabel}>Linked CACs</label>
                        <div className={styles.cacCheckboxList}>
                            {cacOptions.length === 0 ? (
                                <p className={styles.cacEmptyState}>No CACs registered in Step 3 yet.</p>
                            ) : (
                                cacOptions.map(cac => {
                                    const checked = form.controlActionContributionIds.includes(cac.id);
                                    return (
                                        <label
                                            key={cac.id}
                                            className={`${styles.cacCheckboxRow} ${checked ? styles.cacCheckboxRowActive : ""}`}
                                        >
                                            <CastCheckbox
                                                checked={checked}
                                                onChange={() => toggleCac(cac.id)}
                                            />
                                            <span className={styles.cacCode}>{cac.code}</span>
                                            <span className={styles.cacName}>{cac.controlActionName}</span>
                                        </label>
                                    );
                                })
                            )}
                        </div>
                        <p className={styles.helperText}>
                            Select every CAC (Step 3) that this systemic factor helps explain.
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

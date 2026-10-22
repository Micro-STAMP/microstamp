import {
    systemicFactorCategorySelectOptions,
    ISystemicFactorCategory,
    ISystemicFactorInsertDto,
    ISystemicFactorReadDto,
    ISystemicFactorUpdateDto
} from "@interfaces/CAST/IStep4/ISystemicFactor";
import castStyles from "@pages/AnalysisSteps/CAST/Step1/CastStepOne.module.css";
import { useEffect, useState } from "react";
import { BiPlus, BiSave, BiX } from "react-icons/bi";
import { toast } from "sonner";
import CastCheckbox from "@components/CastCheckbox";
import Select from "@components/FormField/Select";
import CastTextarea from "@components/CastTextarea";
import { SelectOption } from "@components/FormField/Templates";
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
    customCategories: string[];
    systemicFactor?: ISystemicFactorReadDto | null;
    isLoading?: boolean;
    onCreate: (data: ISystemicFactorInsertDto) => Promise<void>;
    onUpdate: (id: string, data: ISystemicFactorUpdateDto) => Promise<void>;
}

const CUSTOM_PREFIX = "custom:";
const toCustomValue = (name: string) => `${CUSTOM_PREFIX}${name}`;

const EMPTY_FORM = {
    categoryValue: ISystemicFactorCategory.COMMUNICATION_AND_COORDINATION as string,
    description: "",
    controlActionContributionIds: [] as string[]
};

export default function ModalSystemicFactor({
    open,
    onClose,
    analysisId,
    cacOptions,
    customCategories,
    systemicFactor,
    isLoading = false,
    onCreate,
    onUpdate
}: ModalSystemicFactorProps) {
    const isEditMode = !!systemicFactor;
    const [form, setForm] = useState(EMPTY_FORM);
    const [sessionCategories, setSessionCategories] = useState<string[]>([]);
    const [newCategory, setNewCategory] = useState<string | null>(null);

    const allCustomCategories = Array.from(new Set([...customCategories, ...sessionCategories]));
    const categoryOptions: SelectOption[] = [
        ...systemicFactorCategorySelectOptions,
        ...allCustomCategories.map(name => ({ label: `${name} (custom)`, value: toCustomValue(name) }))
    ];

    useEffect(() => {
        if (!open) return;

        if (systemicFactor) {
            setForm({
                categoryValue: systemicFactor.customCategory
                    ? toCustomValue(systemicFactor.customCategory)
                    : systemicFactor.category ?? ISystemicFactorCategory.COMMUNICATION_AND_COORDINATION,
                description: systemicFactor.description || "",
                controlActionContributionIds: systemicFactor.controlActionContributionIds || []
            });
        } else {
            setForm(EMPTY_FORM);
        }
        setNewCategory(null);
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

    const addNewCategory = () => {
        const name = newCategory?.trim();
        if (!name) return;
        const standard = systemicFactorCategorySelectOptions.find(o => o.label.toLowerCase() === name.toLowerCase());
        if (standard) {
            setForm({ ...form, categoryValue: standard.value });
        } else {
            setSessionCategories(prev => (prev.includes(name) ? prev : [...prev, name]));
            setForm({ ...form, categoryValue: toCustomValue(name) });
        }
        setNewCategory(null);
    };

    const handleSubmit = async () => {
        if (!form.description.trim()) {
            toast.warning("Fill in the required fields.");
            return;
        }

        const isCustom = form.categoryValue.startsWith(CUSTOM_PREFIX);
        const categoryData = {
            category: isCustom ? null : (form.categoryValue as ISystemicFactorCategory),
            customCategory: isCustom ? form.categoryValue.slice(CUSTOM_PREFIX.length) : null
        };

        if (isEditMode && systemicFactor) {
            await onUpdate(systemicFactor.id, {
                ...categoryData,
                description: form.description,
                controlActionContributionIds: form.controlActionContributionIds
            });
        } else {
            await onCreate({
                analysisId,
                ...categoryData,
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
                            options={categoryOptions}
                            value={categoryOptions.find(o => o.value === form.categoryValue) || null}
                            onChange={opt => setForm({ ...form, categoryValue: opt?.value ?? ISystemicFactorCategory.COMMUNICATION_AND_COORDINATION })}
                            required
                        />
                        {newCategory === null ? (
                            <button
                                type="button"
                                onClick={() => setNewCategory("")}
                                style={{ marginTop: "8px", display: "inline-flex", alignItems: "center", gap: "4px", background: "none", border: "1px dashed var(--color-gray)", borderRadius: "6px", color: "var(--color-muted-text)", fontSize: "12px", padding: "6px 10px", cursor: "pointer" }}
                            >
                                <BiPlus size={14} /> New category
                            </button>
                        ) : (
                            <div style={{ display: "flex", gap: "8px", marginTop: "8px" }}>
                                <input
                                    autoFocus
                                    type="text"
                                    maxLength={120}
                                    placeholder="E.g., Regulatory oversight"
                                    className={castStyles.inputField}
                                    style={{ marginBottom: 0 }}
                                    value={newCategory}
                                    onChange={e => setNewCategory(e.target.value)}
                                    onKeyDown={e => { if (e.key === "Enter") addNewCategory(); if (e.key === "Escape") setNewCategory(null); }}
                                />
                                <button type="button" className={castStyles.btnSaveForm} onClick={addNewCategory} disabled={!newCategory.trim()}>
                                    <BiPlus size={16} /> Add
                                </button>
                                <button type="button" className={castStyles.btnBack} onClick={() => setNewCategory(null)}>
                                    <BiX size={16} />
                                </button>
                            </div>
                        )}
                        <p className={styles.helperText}>
                            Systemic factors span the individual components of the safety control structure (CAST Handbook, Leveson 2019). The six standard categories are not exhaustive — add a new one when none of them fits.
                        </p>
                    </div>

                    <div>
                        <label className={castStyles.inputLabel}>Description</label>
                        <CastTextarea
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

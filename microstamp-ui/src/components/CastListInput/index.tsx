import { BiPlus, BiX } from 'react-icons/bi';
import CastTextarea from '@components/CastTextarea';
import castStyles from '@pages/AnalysisSteps/CAST/Step1/CastStepOne.module.css';
import styles from './CastListInput.module.css';

interface Props {
    label: string;
    items: string[];
    onChange: (items: string[]) => void;
    placeholder?: string;
    addLabel?: string;
    helperText?: string;
    helperClassName?: string;
}

export default function CastListInput({ label, items, onChange, placeholder, addLabel = 'Add item', helperText, helperClassName }: Props) {
    const rows = items.length > 0 ? items : [''];

    const updateAt = (index: number, value: string) => onChange(rows.map((item, i) => (i === index ? value : item)));
    const removeAt = (index: number) => onChange(rows.filter((_, i) => i !== index));
    const add = () => onChange([...rows, '']);

    return (
        <div>
            <label className={castStyles.inputLabel}>{label}</label>
            <div className={styles.list}>
                {rows.map((item, index) => (
                    <div key={index} className={styles.row}>
                        <span className={styles.index}>{index + 1}.</span>
                        <CastTextarea
                            rows={1}
                            className={castStyles.inputField}
                            style={{ marginBottom: 0 }}
                            placeholder={placeholder}
                            value={item}
                            onChange={e => updateAt(index, e.target.value)}
                        />
                        <button
                            type="button"
                            className={styles.removeBtn}
                            onClick={() => removeAt(index)}
                            disabled={rows.length === 1 && !item}
                            title="Remove item"
                        >
                            <BiX size={18} />
                        </button>
                    </div>
                ))}
                <button type="button" className={styles.addBtn} onClick={add}>
                    <BiPlus size={14} /> {addLabel}
                </button>
            </div>
            {helperText && <p className={helperClassName}>{helperText}</p>}
        </div>
    );
}

import { BiCheck } from 'react-icons/bi';
import styles from './CastCheckbox.module.css';

interface Props {
    checked: boolean;
    onChange: () => void;
}

export default function CastCheckbox({ checked, onChange }: Props) {
    return (
        <span className={styles.wrapper}>
            <input
                type="checkbox"
                checked={checked}
                onChange={onChange}
                className={styles.nativeInput}
            />
            <span className={`${styles.box} ${checked ? styles.boxChecked : ''}`}>
                {checked && <BiCheck size={14} color="var(--color-dark)" />}
            </span>
        </span>
    );
}

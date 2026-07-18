import { useState } from 'react';
import { BiPlus, BiChevronDown, BiChevronRight, BiHelpCircle, BiInfoCircle } from 'react-icons/bi';
import styles from './CastSection.module.css';

interface CastSectionProps {
    title: string;
    tooltipInfo: string;
    children: React.ReactNode;
    defaultOpen?: boolean;
    hideAddButton?: boolean;
    addButtonLabel?: string;
    onAddClick?: () => void;
}

export default function CastSection({ title, tooltipInfo, children, defaultOpen = true, hideAddButton = false, addButtonLabel = "Add Item", onAddClick }: CastSectionProps) {
    const [isOpen, setIsOpen] = useState(defaultOpen);
    const [showInfo, setShowInfo] = useState(false);

    return (
        <div className={styles.section}>
            <div className={styles.header}>
                <div className={styles.titleGroup}>
                    <button onClick={() => setIsOpen(!isOpen)} className={styles.toggleBtn}>
                        {isOpen ? <BiChevronDown size={20} /> : <BiChevronRight size={20} />}
                    </button>

                    <h3 className={styles.title}>{title}</h3>

                    <button
                        onClick={() => setShowInfo(!showInfo)}
                        className={`${styles.helpBtn} ${showInfo ? styles.helpBtnActive : styles.helpBtnInactive}`}
                        title="What is this?"
                    >
                        <BiHelpCircle size={16} />
                    </button>
                </div>

                {!hideAddButton && (
                    <button className={styles.addBtn} onClick={onAddClick} type="button">
                        <BiPlus size={14} /> {addButtonLabel}
                    </button>
                )}
            </div>

            {showInfo && (
                <div className={styles.infoBar}>
                    <BiInfoCircle size={18} className={styles.infoIcon} />
                    <p className={styles.infoText}>{tooltipInfo}</p>
                </div>
            )}

            {isOpen && <div className={styles.content}>{children}</div>}
        </div>
    );
}
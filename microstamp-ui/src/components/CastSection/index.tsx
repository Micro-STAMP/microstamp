import { useState } from 'react';
import { BiPlus, BiChevronDown, BiChevronRight, BiHelpCircle, BiInfoCircle } from 'react-icons/bi';
import styles from './CastSection.module.css';

interface CastSectionProps {
    title: string;
    tooltipInfo: string;
    children: React.ReactNode;
    defaultOpen?: boolean;
}

export default function CastSection({ title, tooltipInfo, children, defaultOpen = true }: CastSectionProps) {
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

                <button className={styles.addBtn}>
                    <BiPlus size={14} /> Add Item
                </button>
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
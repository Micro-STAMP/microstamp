import { IAnalysisReadDto } from "@interfaces/IAnalysis";
import { BiCalendar, BiEdit, BiTrash, BiImageAlt } from "react-icons/bi";
import styles from "./AnalysisCard.module.css";

interface AnalysisCardProps {
    analysis: IAnalysisReadDto & { type?: "STPA" | "CAST" };
    onOpen: () => void;
    onEdit: () => void; 
    onDelete: () => void;
}

export default function AnalysisCard({ analysis, onOpen, onEdit, onDelete }: AnalysisCardProps) {
    const isSTPA = analysis.type !== "CAST";

    return (
        <div className={styles.card} onClick={onOpen}>
            <div className={styles.imageContainer}>
                <div className={styles.imagePlaceholder}>
                    <BiImageAlt size={40} />
                </div>
                <span className={`${styles.badge} ${isSTPA ? styles.badgeSTPA : styles.badgeCAST}`}>
                    {isSTPA ? "STPA" : "CAST"}
                </span>
            </div>

            <div className={styles.content}>
                <h3 className={styles.title}>{analysis.name}</h3>
                <p className={styles.description}>{analysis.description}</p>

                <div className={styles.footer}>
                    <div className={styles.date}>
                        <BiCalendar size={14} />
                        <span>Just now</span>
                    </div>
                    <div className={styles.actions}>
                        <button 
                            className={styles.actionBtn}
                            onClick={(e) => {
                                e.stopPropagation();
                                onEdit();
                            }}
                        >
                            <BiEdit size={14} /> Edit
                        </button>
                        <button 
                            className={styles.actionBtn}
                            onClick={(e) => {
                                e.stopPropagation(); 
                                onDelete();
                            }}
                        >
                            <BiTrash size={14} /> Delete
                        </button>
                    </div>
                </div>
            </div>
        </div>
    );
}
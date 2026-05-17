import { useState } from 'react';
import { BiGridAlt, BiArrowBack, BiPlus, BiTrash } from 'react-icons/bi';
import CastSection from '@components/CastSection';
import styles from './CastStepOne.module.css';
import SystemDescriptionForm from './components/SystemDescriptionForm';
import AccidentLossEventForm from './components/AccidentLossEventForm';
import CastHazardForm from './components/CastHazardForm';
import ViolatedSystemSafetyConstraintForm from './components/ViolatedSystemSafetyConstraintForm';
import TimelineEventForm from './components/TimelineEventForm';
import PhysicalLossAnalysisForm from './components/PhysicalLossAnalysisForm';

interface CastStepOneProps {
    onBack: () => void;
    analysisName: string;
    analysisId: string; 
}

export default function CastStepOne({ onBack, analysisName, analysisId }: CastStepOneProps) {
    const [timelineEvents, setTimelineEvents] = useState([
        { id: 1, description: '', questions: '' }
    ]);
    const handleAddEvent = () => setTimelineEvents([...timelineEvents, { id: Date.now(), description: '', questions: '' }]);
    const handleRemoveEvent = (id: number) => setTimelineEvents(timelineEvents.filter(e => e.id !== id));

    return (
        <div className={styles.pageContainer}>
            <div className={styles.wrapper}>
                
                <div className={styles.header}>
                    <div>
                        <div className={styles.breadcrumbGroup}>
                            <span className={styles.badgeCAST}>CAST</span>
                            <span className={styles.breadcrumbText}>
                                <BiGridAlt size={16} /> Analyses / {analysisName}
                            </span>
                        </div>
                        <h1 className={styles.pageTitle}>Step 1: Assemble Basic Information</h1>
                    </div>

                    <div className={styles.actions}>
                        <button onClick={onBack} className={styles.btnBack}>
                            <BiArrowBack size={18} /> Go Back
                        </button>
                        <button className={styles.btnChangeStep}>Change Step</button>
                    </div>
                </div>

                <SystemDescriptionForm analysisId={analysisId} />
                <AccidentLossEventForm analysisId={analysisId} />
                <CastHazardForm analysisId={analysisId} />
                <ViolatedSystemSafetyConstraintForm analysisId={analysisId} />
                <TimelineEventForm analysisId={analysisId} />
                <PhysicalLossAnalysisForm analysisId={analysisId} />

            </div>
        </div>
    );
}
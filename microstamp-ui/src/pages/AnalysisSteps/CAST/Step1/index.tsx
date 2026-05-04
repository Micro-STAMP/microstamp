import { useState } from 'react';
import { BiGridAlt, BiArrowBack, BiPlus, BiTrash } from 'react-icons/bi';
import CastSection from '@components/CastSection';
import styles from './CastStepOne.module.css';

interface CastStepOneProps {
    onBack: () => void;
    analysisName: string;
}

export default function CastStepOne({ onBack, analysisName }: CastStepOneProps) {
    const [timelineEvents, setTimelineEvents] = useState([
        { id: 1, description: '', questions: '' }
    ]);

    const handleAddEvent = () => {
        setTimelineEvents([
            ...timelineEvents,
            { id: Date.now(), description: '', questions: '' }
        ]);
    };

    const handleRemoveEvent = (id: number) => {
        setTimelineEvents(timelineEvents.filter(event => event.id !== id));
    };

    return (
        <div className={styles.pageContainer}>
            <div className={styles.wrapper}>
                
                {/* Cabeçalho */}
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

                <CastSection
                    title="System Description"
                    tooltipInfo="Describe the physical and organizational system components and their state right before the incident."
                >
                    <textarea
                        rows={4}
                        placeholder="Describe how the system was operating normally..."
                        className={styles.inputField}
                    />
                </CastSection>

                <CastSection
                    title="Accident / Loss Event"
                    tooltipInfo="What was the specific loss event or unacceptable outcome that actually occurred?"
                >
                    <input
                        type="text"
                        placeholder="Short title of the accident..."
                        className={styles.inputField}
                    />
                    <textarea
                        rows={3}
                        placeholder="Detailed description of the loss event..."
                        className={styles.inputField}
                        style={{ marginBottom: 0 }}
                    />
                </CastSection>

                <CastSection
                    title="Hazards Involved"
                    tooltipInfo="What unsafe system states or conditions materialized to allow the accident to happen?"
                    defaultOpen={false}
                >
                    <div className={styles.emptyState}>No hazards added yet. Click "Add Item" to start.</div>
                </CastSection>

                <CastSection
                    title="Violated System Safety Constraints"
                    tooltipInfo="What safety rules, constraints, or protocols were violated or proved inadequate?"
                    defaultOpen={false}
                >
                    <div className={styles.emptyState}>No constraints added yet. Click "Add Item" to start.</div>
                </CastSection>

                <CastSection
                    title="Timeline of Events & Questions"
                    tooltipInfo="A high-level chronological summary of the proximate events leading up to the loss, and questions generated for the investigation."
                >
                    <div>
                        {timelineEvents.map((event, index) => (
                            <div key={event.id} className={styles.timelineEvent}>
                                <div className={styles.timelineNumberCol}>
                                    <div className={styles.timelineNumber}>{index + 1}</div>
                                    {index !== timelineEvents.length - 1 && <div className={styles.timelineLine}></div>}
                                </div>

                                <div className={styles.timelineContent}>
                                    <div>
                                        <label className={styles.inputLabel}>Proximate Event</label>
                                        <textarea
                                            rows={2}
                                            placeholder="What happened? (e.g., Valve A opened unexpectedly)"
                                            className={styles.inputField}
                                        />
                                    </div>
                                    <div>
                                        <label className={styles.inputLabel}>Questions Generated</label>
                                        <textarea
                                            rows={2}
                                            placeholder="Why did this happen? (e.g., Why was the open command sent?)"
                                            className={styles.inputField}
                                        />
                                    </div>
                                </div>

                                {timelineEvents.length > 1 && (
                                    <button onClick={() => handleRemoveEvent(event.id)} className={styles.btnRemove} title="Remove event">
                                        <BiTrash size={16} />
                                    </button>
                                )}
                            </div>
                        ))}

                        <button onClick={handleAddEvent} className={styles.btnAddEvent}>
                            <BiPlus size={18} /> Add Next Event
                        </button>
                    </div>
                </CastSection>

                <CastSection
                    title="Physical Loss Analysis"
                    tooltipInfo="Analysis of the physical system, equipment failures, and physical controls that were missing or inadequate."
                    defaultOpen={false}
                >
                    <div>
                        <label className={styles.inputLabel}>Physical Controls / Equipment <span style={{ fontWeight: "normal", display: "block" }}>What physical protections were designed to prevent this?</span></label>
                        <textarea rows={2} placeholder="e.g., Pressure relief valves..." className={styles.inputField} />
                        
                        <label className={styles.inputLabel} style={{ marginTop: "10px" }}>Failures and Unsafe Interactions <span style={{ fontWeight: "normal", display: "block" }}>What equipment failed or interacted unsafely?</span></label>
                        <textarea rows={2} placeholder="e.g., Valve stuck closed..." className={styles.inputField} />
                    </div>
                </CastSection>

            </div>
        </div>
    );
}
import Button from "@components/Button";
import { ModalSelectStep3, ModalSelectStep4 } from "@components/Modal";
import { ModalButtons, ModalContainer, ModalHeader, ModalProps } from "@components/Modal/Templates";
import StepIcon from "@components/StepIcon";
import { getStepLabel, ISteps } from "@interfaces/ISteps";
import { useState } from "react";
import { BiUndo as ReturnIcon } from "react-icons/bi";
import { useNavigate } from "react-router-dom";
import styles from "./ModalStepsMenu.module.css";

interface ModalStepsMenuProps extends ModalProps {
    analysisId: string;
    analysisType?: "STPA" | "CAST"; 
}

function ModalStepsMenu({ open, onClose, analysisId, analysisType = "STPA" }: ModalStepsMenuProps) {
    const navigate = useNavigate();

    /* - - - - - - - - - - - - - - - - - - - - - - */
    // * Handle Modals

    const [modalSelectStep3Open, setModalSelectStep3Open] = useState(false);
    const toggleModalSelectStep3 = () => setModalSelectStep3Open(!modalSelectStep3Open);

    const [modalSelectStep4Open, setModalSelectStep4Open] = useState(false);
    const toggleModalSelectStep4 = () => setModalSelectStep4Open(!modalSelectStep4Open);

    /* - - - - - - - - - - - - - - - - - - - - - - */
    // * Handle Icons

    const Step1Icon = () => <StepIcon step={ISteps.STEP_1} />;
    const Step2Icon = () => <StepIcon step={ISteps.STEP_2} />;
    const Step3Icon = () => <StepIcon step={ISteps.STEP_3} />;
    const Step4Icon = () => <StepIcon step={ISteps.STEP_4} />;
    const Step5Icon = () => <StepIcon step={ISteps.STEP_5} />;

    /* - - - - - - - - - - - - - - - - - - - - - - */

    return (
        <>
            <ModalContainer open={open} size="small">
                <ModalHeader title={"Change Analysis Step"} onClose={onClose} />
                <div className={styles.steps_menu}>
                    
                    {/* STEPS DO STPA */}
                    {analysisType === "STPA" && (
                        <>
                            <button
                                type="button"
                                className={styles.step_button}
                                onClick={() => {
                                    navigate(`/analyses/${analysisId}/purpose`);
                                    onClose();
                                }}
                            >
                                <span className={styles.step_icon}><Step1Icon /></span>
                                <span className={styles.step_label}>{getStepLabel(ISteps.STEP_1)}</span>
                            </button>

                            <button
                                type="button"
                                className={styles.step_button}
                                onClick={() => {
                                    navigate(`/analyses/${analysisId}/control-structure`);
                                    onClose();
                                }}
                            >
                                <span className={styles.step_icon}><Step2Icon /></span>
                                <span className={styles.step_label}>{getStepLabel(ISteps.STEP_2)}</span>
                            </button>

                            <button type="button" className={styles.step_button} onClick={toggleModalSelectStep3}>
                                <span className={styles.step_icon}><Step3Icon /></span>
                                <span className={styles.step_label}>{getStepLabel(ISteps.STEP_3)}</span>
                            </button>

                            <button type="button" className={styles.step_button} onClick={toggleModalSelectStep4}>
                                <span className={styles.step_icon}><Step4Icon /></span>
                                <span className={styles.step_label}>{getStepLabel(ISteps.STEP_4)}</span>
                            </button>
                        </>
                    )}

                    {/* STEPS DO CAST */}
                    {analysisType === "CAST" && (
                        <>
							{/* step 1  */}
                            <button
                                type="button"
                                className={styles.step_button}
                                onClick={() => {
                                    navigate(`/analyses/${analysisId}/cast/step1`);
                                    onClose();
                                }}
                            >
                                <span className={styles.step_icon}><Step1Icon /></span>
                                <span className={styles.step_label}>Step 1: Assemble Basic Info</span>
                            </button>
							
							{/*  step 2 */}
                            <button
                                type="button"
                                className={styles.step_button}
                                onClick={() => {
                                    navigate(`/analyses/${analysisId}/cast/step2`);
                                    onClose();
                                }}
                            >
                                <span className={styles.step_icon}><Step2Icon /></span>
                                <span className={styles.step_label}>Step 2: Model the Control Structure</span>
                            </button>

							{/*  step 3 */}
                            <button
                                type="button"
                                className={styles.step_button}
                                onClick={() => {
                                    navigate(`/analyses/${analysisId}/cast/step3`);
                                    onClose();
                                }}
                            >
                                <span className={styles.step_icon}><Step3Icon /></span>
                                <span className={styles.step_label}>Step 3: Analyze the Inadequate Control</span>
                            </button>

							{/*  step 4 */}
                            <button
                                type="button"
                                className={styles.step_button}
                                onClick={() => {
                                    navigate(`/analyses/${analysisId}/cast/step4`);
                                    onClose();
                                }}
                            >
                                <span className={styles.step_icon}><Step4Icon /></span>
                                <span className={styles.step_label}>Step 4: Identify Systemic Factors</span>
                            </button>

							{/*  step 5 */}
                            <button
                                type="button"
                                className={styles.step_button}
                                onClick={() => {
                                    navigate(`/analyses/${analysisId}/cast/step5`);
                                    onClose();
                                }}
                            >
                                <span className={styles.step_icon}><Step5Icon /></span>
                                <span className={styles.step_label}>Step 5: Create an Improvement Program</span>
                            </button>
                        </>
                    )}
                </div>

                <ModalButtons>
                    <Button variant="dark" onClick={onClose} size="small" icon={ReturnIcon}>
                        Cancel
                    </Button>
                </ModalButtons>
            </ModalContainer>

            {open && analysisType === "STPA" && (
                <>
                    <ModalSelectStep3
                        open={modalSelectStep3Open}
                        onClose={toggleModalSelectStep3}
                        analysisId={analysisId}
                        onSelect={onClose}
                    />
                    <ModalSelectStep4
                        open={modalSelectStep4Open}
                        onClose={toggleModalSelectStep4}
                        analysisId={analysisId}
                        onSelect={onClose}
                    />
                </>
            )}
        </>
    );
}

export default ModalStepsMenu;
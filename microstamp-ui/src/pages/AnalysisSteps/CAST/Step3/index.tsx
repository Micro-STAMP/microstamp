import { useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { BiGridAlt, BiArrowBack } from 'react-icons/bi';
import styles from '../Step1/CastStepOne.module.css';

import InadequateControlActionsSection from './components/InadequateControlActionsSection';
import ModalStepsMenu from '@components/Modal/ModalStepsMenu';

export default function CastStepThree() {
    const { id } = useParams();
    const navigate = useNavigate();
    const analysisId = id || '';

    const [modalStepsMenuOpen, setModalStepsMenuOpen] = useState(false);
    const toggleModalStepsMenu = () => setModalStepsMenuOpen(!modalStepsMenuOpen);

    return (
        <div className={styles.pageContainer}>
            <div className={styles.wrapper}>

                <div className={styles.header}>
                    <div>
                        <div className={styles.breadcrumbGroup}>
                            <span className={styles.badgeCAST}>CAST</span>
                            <span className={styles.breadcrumbText}>
                                <BiGridAlt size={16} /> Analyses / {analysisId}
                            </span>
                        </div>
                        <h1 className={styles.pageTitle}>Step 3: Analyze the Inadequate Control</h1>
                    </div>

                    <div className={styles.actions}>
                        <button onClick={() => navigate(-1)} className={styles.btnBack}>
                            <BiArrowBack size={18} /> Go Back
                        </button>
                        <button onClick={toggleModalStepsMenu} className={styles.btnChangeStep}>
                            Change Step
                        </button>
                    </div>
                </div>

                <InadequateControlActionsSection analysisId={analysisId} />

            </div>

            <ModalStepsMenu
                open={modalStepsMenuOpen}
                onClose={toggleModalStepsMenu}
                analysisId={analysisId}
                analysisType="CAST"
            />
        </div>
    );
}

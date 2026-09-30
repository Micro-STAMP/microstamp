import { useEffect, useState } from 'react';
import { useParams, useNavigate, useLocation } from 'react-router-dom';
import { BiGridAlt, BiArrowBack } from 'react-icons/bi';
import styles from './CastStepOne.module.css';

// forms imports
import SystemDescriptionForm from './components/SystemDescriptionForm';
import AccidentLossEventForm from './components/AccidentLossEventForm';
import CastHazardForm from './components/CastHazardForm';
import ViolatedSystemSafetyConstraintForm from './components/ViolatedSystemSafetyConstraintForm';
import TimelineEventForm from './components/TimelineEventForm';
import PhysicalLossAnalysisForm from './components/PhysicalLossAnalysisForm';
import ModalStepsMenu from '@components/Modal/ModalStepsMenu';
import CastNextStepButton from '@components/CastNextStepButton';
import { CastSectionNavigatorProvider, useGoToCastSection } from '@components/CastSection/CastSectionNavigator';

function ScrollToSectionOnMount() {
    const location = useLocation();
    const navigate = useNavigate();
    const goToSection = useGoToCastSection();

    useEffect(() => {
        const state = location.state as { scrollToSection?: string } | null;
        if (state?.scrollToSection) {
            goToSection(state.scrollToSection);
            navigate(location.pathname, { replace: true, state: null });
        }
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, []);

    return null;
}

export default function CastStepOne() {
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
                        <h1 className={styles.pageTitle}>Step 1: Assemble Basic Information</h1>
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

                <CastSectionNavigatorProvider>
                    <SystemDescriptionForm analysisId={analysisId} />
                    <AccidentLossEventForm analysisId={analysisId} />
                    <CastHazardForm analysisId={analysisId} />
                    <ViolatedSystemSafetyConstraintForm analysisId={analysisId} />
                    <TimelineEventForm analysisId={analysisId} />
                    <PhysicalLossAnalysisForm analysisId={analysisId} />
                    <ScrollToSectionOnMount />
                </CastSectionNavigatorProvider>

                <CastNextStepButton analysisId={analysisId} to="step2" label="Step 2: Model the Safety Control Structure" />

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
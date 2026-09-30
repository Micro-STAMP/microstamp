import { useNavigate } from 'react-router-dom';
import { BiRightArrowAlt } from 'react-icons/bi';
import styles from '@pages/AnalysisSteps/CAST/Step1/CastStepOne.module.css';

interface Props {
    analysisId: string;
    to: string;
    label: string;
}

export default function CastNextStepButton({ analysisId, to, label }: Props) {
    const navigate = useNavigate();

    return (
        <div className={styles.nextStepBar}>
            <button
                onClick={() => navigate(`/analyses/${analysisId}/cast/${to}`)}
                className={styles.btnNextStep}
            >
                Proceed to {label} <BiRightArrowAlt size={18} />
            </button>
        </div>
    );
}

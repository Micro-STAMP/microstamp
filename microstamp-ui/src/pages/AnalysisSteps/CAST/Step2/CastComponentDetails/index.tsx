import { useParams, useNavigate } from 'react-router-dom'; 
import { BiGridAlt, BiArrowBack, BiMicrochip } from 'react-icons/bi';
import { useQuery } from '@tanstack/react-query';
import styles from '../../Step1/CastStepOne.module.css';

import { getComponent } from '@http/Step2/Components';

import CastResponsibilitiesForm from './components/CastResponsibilitiesForm';
import CastVariablesForm from './components/CastVariablesForm';
import CastComponentConnections from './components/CastComponentConnections';

export default function CastComponentDetails() { 
    const { id, componentId } = useParams(); 
    const navigate = useNavigate();
    const analysisId = id || ''; 

    const { data: component, isLoading } = useQuery({
        queryKey: ['component', componentId],
        queryFn: () => getComponent(componentId!),
        enabled: !!componentId
    });

    const componentName = isLoading ? "Loading..." : component?.name || "Unknown Component";

    return (
        <div className={styles.pageContainer}>
            <div className={styles.wrapper}>
                
                <div className={styles.header}>
                    <div>
                        <div className={styles.breadcrumbGroup}>
                            <span className={styles.badgeCAST}>CAST</span>
                            <span className={styles.breadcrumbText}>
                                <BiGridAlt size={16} /> Analyses / {analysisId} / Components / {componentId}
                            </span>
                        </div>
                        <h1 className={styles.pageTitle} style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
                            <BiMicrochip color="#fb923c" /> {componentName}
                        </h1>
                        <p style={{ margin: '8px 0 0 0', fontSize: '14px', color: 'var(--color-muted-text)' }}>
                            Analyze the specific responsibilities and the process model (variables and states) for this controller.
                        </p>
                    </div>

                    <div className={styles.actions}>
                        <button onClick={() => navigate(-1)} className={styles.btnBack}>
                            <BiArrowBack size={18} /> Go Back
                        </button>
                    </div>
                </div>

                <CastResponsibilitiesForm componentId={componentId!} analysisId={analysisId} />
                <CastVariablesForm componentId={componentId!} analysisId={analysisId} />
                <CastComponentConnections componentId={componentId!} analysisId={analysisId} />

            </div>
        </div>
    );
}
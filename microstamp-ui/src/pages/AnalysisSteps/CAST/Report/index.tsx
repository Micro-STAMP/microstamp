import { useState } from 'react';
import { useParams, useNavigate, Link } from 'react-router-dom';
import { useQuery } from '@tanstack/react-query';
import { toast } from 'sonner';
import { BiGridAlt, BiArrowBack, BiDownload, BiLinkAlt } from 'react-icons/bi';

import { generateCastReportPdf } from '@utils/castReportPdf';

import { getByAnalysisId as getSystemDescriptions } from '@http/CAST/Step1/SystemDescription';
import { getByAnalysisId as getAccidentLossEvents } from '@http/CAST/Step1/AccidentLossEvents';
import { getByAnalysisId as getCastHazards } from '@http/CAST/Step1/Hazards';
import { getByAnalysisId as getViolatedConstraints } from '@http/CAST/Step1/Constraints';
import { getByAnalysisId as getTimelineEvents } from '@http/CAST/Step1/Timeline';
import { getByAnalysisId as getPhysicalLossAnalyses } from '@http/CAST/Step1/PhysicalLoss';
import { getComponents } from '@http/Step2/Components';
import { getByAnalysisId as getIcas } from '@http/CAST/Step3/InadequateControlActions';
import { getByAnalysisId as getSystemicFactors } from '@http/CAST/Step4/SystemicFactors';
import { getByAnalysisId as getRecommendations } from '@http/CAST/Step5/Recommendations';

import { icaTypeToSelectOption } from '@interfaces/CAST/IStep3/IInadequateControlAction';
import { systemicFactorCategoryToSelectOption } from '@interfaces/CAST/IStep4/ISystemicFactor';

import castStyles from '../Step1/CastStepOne.module.css';
import styles from './CastReport.module.css';

interface Props {}

export default function CastReport(_: Props) {
    const { id } = useParams();
    const navigate = useNavigate();
    const analysisId = id || '';

    const [isGeneratingPdf, setIsGeneratingPdf] = useState(false);
    const [executiveSummary, setExecutiveSummary] = useState('');

    const systemDescriptionsQuery = useQuery({
        queryKey: ['cast-system-descriptions', analysisId],
        queryFn: () => getSystemDescriptions(analysisId)
    });
    const accidentLossEventsQuery = useQuery({
        queryKey: ['cast-accident-loss-events', analysisId],
        queryFn: () => getAccidentLossEvents(analysisId)
    });
    const castHazardsQuery = useQuery({
        queryKey: ['cast-hazards', analysisId],
        queryFn: () => getCastHazards(analysisId)
    });
    const violatedConstraintsQuery = useQuery({
        queryKey: ['cast-violated-constraints', analysisId],
        queryFn: () => getViolatedConstraints(analysisId)
    });
    const timelineEventsQuery = useQuery({
        queryKey: ['cast-timeline-events', analysisId],
        queryFn: () => getTimelineEvents(analysisId)
    });
    const physicalLossAnalysesQuery = useQuery({
        queryKey: ['cast-physical-loss-analyses', analysisId],
        queryFn: () => getPhysicalLossAnalyses(analysisId)
    });
    const componentsQuery = useQuery({
        queryKey: ['cast-report-components', analysisId],
        queryFn: () => getComponents(analysisId)
    });
    const icasQuery = useQuery({
        queryKey: ['inadequate-control-actions', analysisId],
        queryFn: () => getIcas(analysisId)
    });
    const systemicFactorsQuery = useQuery({
        queryKey: ['systemic-factors', analysisId],
        queryFn: () => getSystemicFactors(analysisId)
    });
    const recommendationsQuery = useQuery({
        queryKey: ['recommendations', analysisId],
        queryFn: () => getRecommendations(analysisId)
    });

    const isLoading = systemDescriptionsQuery.isLoading
        || accidentLossEventsQuery.isLoading
        || castHazardsQuery.isLoading
        || violatedConstraintsQuery.isLoading
        || timelineEventsQuery.isLoading
        || physicalLossAnalysesQuery.isLoading
        || componentsQuery.isLoading
        || icasQuery.isLoading
        || systemicFactorsQuery.isLoading
        || recommendationsQuery.isLoading;

    const icas = icasQuery.data || [];
    const systemicFactors = systemicFactorsQuery.data || [];

    const getIcaLabel = (icaId: string) => {
        const ica = icas.find(i => i.id === icaId);
        return ica ? `${ica.code} — ${ica.controlActionName}` : 'Inadequate Control Action not found';
    };
    const getSystemicFactorLabel = (systemicFactorId: string) => {
        const factor = systemicFactors.find(f => f.id === systemicFactorId);
        return factor
            ? `${systemicFactorCategoryToSelectOption(factor.category).label} — ${factor.description}`
            : 'Systemic Factor not found';
    };

    const editUrl = (path: string) => `${window.location.origin}${path}`;

    const handleDownloadPdf = async () => {
        setIsGeneratingPdf(true);
        try {
            generateCastReportPdf({
                analysisId,
                executiveSummary,
                sections: [
                    {
                        title: 'System Description',
                        editUrl: editUrl(`/analyses/${analysisId}/cast/step1`),
                        emptyLabel: 'No system description registered yet.',
                        items: (systemDescriptionsQuery.data || []).map(item => ({
                            code: item.code,
                            paragraphs: [item.description],
                            fields: item.analysisBoundary
                                ? [{ label: 'Analysis Boundary', value: item.analysisBoundary }]
                                : []
                        }))
                    },
                    {
                        title: 'Accident / Loss Events',
                        editUrl: editUrl(`/analyses/${analysisId}/cast/step1`),
                        emptyLabel: 'No accident/loss events registered yet.',
                        items: (accidentLossEventsQuery.data || []).map(item => ({
                            code: item.code,
                            title: item.name,
                            paragraphs: item.description ? [item.description] : []
                        }))
                    },
                    {
                        title: 'Hazards',
                        editUrl: editUrl(`/analyses/${analysisId}/cast/step1`),
                        emptyLabel: 'No hazards registered yet.',
                        items: (castHazardsQuery.data || []).map(item => ({
                            code: item.code,
                            title: item.name,
                            paragraphs: item.description ? [item.description] : [],
                            chips: (item.accidentLossEvents || []).map(event => `${event.code} — ${event.name}`)
                        }))
                    },
                    {
                        title: 'Violated System Safety Constraints',
                        editUrl: editUrl(`/analyses/${analysisId}/cast/step1`),
                        emptyLabel: 'No violated system safety constraints registered yet.',
                        items: (violatedConstraintsQuery.data || []).map(item => ({
                            code: item.code,
                            title: item.name,
                            paragraphs: item.description ? [item.description] : [],
                            chips: (item.hazards || []).map(hazard => `${hazard.code} — ${hazard.name}`)
                        }))
                    },
                    {
                        title: 'Timeline of Events',
                        editUrl: editUrl(`/analyses/${analysisId}/cast/step1`),
                        emptyLabel: 'No timeline events registered yet.',
                        items: (timelineEventsQuery.data || []).map(item => ({
                            code: item.code,
                            paragraphs: [item.eventDescription],
                            fields: item.questions ? [{ label: 'Questions', value: item.questions }] : []
                        }))
                    },
                    {
                        title: 'Physical Loss Analysis',
                        editUrl: editUrl(`/analyses/${analysisId}/cast/step1`),
                        emptyLabel: 'No physical loss analysis registered yet.',
                        items: (physicalLossAnalysesQuery.data || []).map(item => ({
                            code: item.code,
                            paragraphs: item.physicalLossDescription ? [item.physicalLossDescription] : [],
                            fields: [
                                item.affectedEquipment
                                    ? { label: 'Affected Equipment', value: item.affectedEquipment }
                                    : null,
                                item.failuresAndUnsafeInteractions
                                    ? {
                                          label: 'Failures and Unsafe Interactions',
                                          value: item.failuresAndUnsafeInteractions
                                      }
                                    : null,
                                item.missingOrInadequateControls
                                    ? { label: 'Missing or Inadequate Controls', value: item.missingOrInadequateControls }
                                    : null
                            ].filter((field): field is { label: string; value: string } => field !== null)
                        }))
                    },
                    {
                        title: 'Safety Control Structure',
                        editUrl: editUrl(`/analyses/${analysisId}/cast/step2`),
                        emptyLabel: 'No components modeled yet.',
                        items: (componentsQuery.data || []).map(item => ({
                            code: item.code,
                            title: item.name,
                            fields: [{ label: 'Type', value: item.type }]
                        }))
                    },
                    {
                        title: 'Inadequate Control Actions',
                        editUrl: editUrl(`/analyses/${analysisId}/cast/step3`),
                        emptyLabel: 'No Inadequate Control Actions registered yet.',
                        items: icas.map(item => ({
                            code: item.code,
                            title: `${item.controlActionName} — ${icaTypeToSelectOption(item.type).label}`,
                            paragraphs: item.description ? [item.description] : [],
                            fields: [
                                item.context ? { label: 'Context', value: item.context } : null,
                                item.processModelFlaw
                                    ? { label: 'Process Model Flaw', value: item.processModelFlaw }
                                    : null
                            ].filter((field): field is { label: string; value: string } => field !== null)
                        }))
                    },
                    {
                        title: 'Systemic Factors',
                        editUrl: editUrl(`/analyses/${analysisId}/cast/step4`),
                        emptyLabel: 'No Systemic Factors registered yet.',
                        items: systemicFactors.map(item => ({
                            code: systemicFactorCategoryToSelectOption(item.category).label,
                            paragraphs: [item.description],
                            chips: (item.inadequateControlActionIds || []).map(getIcaLabel)
                        }))
                    },
                    {
                        title: 'Recommendations',
                        editUrl: editUrl(`/analyses/${analysisId}/cast/step5`),
                        emptyLabel: 'No Recommendations registered yet.',
                        items: (recommendationsQuery.data || []).map(item => ({
                            paragraphs: [item.description],
                            chips: [
                                ...(item.inadequateControlActionIds || []).map(getIcaLabel),
                                ...(item.systemicFactorIds || []).map(getSystemicFactorLabel)
                            ]
                        }))
                    }
                ]
            });
        } catch (err) {
            console.error(err);
            toast.error('Error generating PDF report.');
        } finally {
            setIsGeneratingPdf(false);
        }
    };

    return (
        <div className={castStyles.pageContainer}>
            <div className={castStyles.wrapper}>

                <div className={castStyles.header}>
                    <div>
                        <div className={castStyles.breadcrumbGroup}>
                            <span className={castStyles.badgeCAST}>CAST</span>
                            <span className={castStyles.breadcrumbText}>
                                <BiGridAlt size={16} /> Analyses / {analysisId}
                            </span>
                        </div>
                        <h1 className={castStyles.pageTitle}>Accident Analysis Report</h1>
                    </div>

                    <div className={castStyles.actions}>
                        <button onClick={() => navigate(-1)} className={castStyles.btnBack}>
                            <BiArrowBack size={18} /> Go Back
                        </button>
                        <button
                            onClick={handleDownloadPdf}
                            className={styles.btnDownload}
                            disabled={isLoading || isGeneratingPdf}
                        >
                            <BiDownload size={18} /> {isGeneratingPdf ? 'Generating PDF...' : 'Download PDF'}
                        </button>
                    </div>
                </div>

                {isLoading ? (
                    <div style={{ color: '#9ca3af', fontSize: '14px', padding: '10px' }}>Loading report data...</div>
                ) : (
                    <div className={styles.reportContainer}>

                        <div className={`${styles.reportBlock} ${styles.titleBlock}`}>
                            <h2 className={styles.reportMainTitle}>CAST Accident Analysis Report</h2>
                            <p className={styles.reportMeta}>Analysis ID: {analysisId}</p>
                            <p className={styles.reportMeta}>Generated on {new Date().toLocaleString()}</p>
                        </div>

                        <div className={styles.reportBlock}>
                            <h3 className={styles.groupTitle}>Executive Summary</h3>
                            <textarea
                                className={styles.summaryTextarea}
                                placeholder="Optional free-text synthesis of the analysis, written by the analyst for this report."
                                value={executiveSummary}
                                onChange={e => setExecutiveSummary(e.target.value)}
                            />
                        </div>

                        {/* 1. Purpose of the Analysis (CAST Handbook, Leveson 2019) */}

                        <ReportGroup
                            title="System Description"
                            editHref={`/analyses/${analysisId}/cast/step1`}
                            isEmpty={(systemDescriptionsQuery.data || []).length === 0}
                            emptyLabel="No system description registered yet."
                        >
                            {(systemDescriptionsQuery.data || []).map(item => (
                                <div key={item.id} className={styles.reportBlock}>
                                    <span className={styles.itemCode}>{item.code}</span>
                                    <p className={styles.itemField}>{item.description}</p>
                                    {item.analysisBoundary && (
                                        <p className={styles.itemField}>
                                            <span className={styles.itemFieldLabel}>Analysis Boundary: </span>
                                            {item.analysisBoundary}
                                        </p>
                                    )}
                                </div>
                            ))}
                        </ReportGroup>

                        <ReportGroup
                            title="Accident / Loss Events"
                            editHref={`/analyses/${analysisId}/cast/step1`}
                            isEmpty={(accidentLossEventsQuery.data || []).length === 0}
                            emptyLabel="No accident/loss events registered yet."
                        >
                            {(accidentLossEventsQuery.data || []).map(item => (
                                <div key={item.id} className={styles.reportBlock}>
                                    <span className={styles.itemCode}>{item.code}</span>
                                    <h4 className={styles.itemTitle}>{item.name}</h4>
                                    {item.description && <p className={styles.itemField}>{item.description}</p>}
                                </div>
                            ))}
                        </ReportGroup>

                        <ReportGroup
                            title="Hazards"
                            editHref={`/analyses/${analysisId}/cast/step1`}
                            isEmpty={(castHazardsQuery.data || []).length === 0}
                            emptyLabel="No hazards registered yet."
                        >
                            {(castHazardsQuery.data || []).map(item => (
                                <div key={item.id} className={styles.reportBlock}>
                                    <span className={styles.itemCode}>{item.code}</span>
                                    <h4 className={styles.itemTitle}>{item.name}</h4>
                                    {item.description && <p className={styles.itemField}>{item.description}</p>}
                                    {item.accidentLossEvents && item.accidentLossEvents.length > 0 && (
                                        <div className={styles.chipsRow}>
                                            {item.accidentLossEvents.map(event => (
                                                <span key={event.id} className={styles.chip}>
                                                    <BiLinkAlt size={12} /> {event.code} — {event.name}
                                                </span>
                                            ))}
                                        </div>
                                    )}
                                </div>
                            ))}
                        </ReportGroup>

                        <ReportGroup
                            title="Violated System Safety Constraints"
                            editHref={`/analyses/${analysisId}/cast/step1`}
                            isEmpty={(violatedConstraintsQuery.data || []).length === 0}
                            emptyLabel="No violated system safety constraints registered yet."
                        >
                            {(violatedConstraintsQuery.data || []).map(item => (
                                <div key={item.id} className={styles.reportBlock}>
                                    <span className={styles.itemCode}>{item.code}</span>
                                    <h4 className={styles.itemTitle}>{item.name}</h4>
                                    {item.description && <p className={styles.itemField}>{item.description}</p>}
                                    {item.hazards && item.hazards.length > 0 && (
                                        <div className={styles.chipsRow}>
                                            {item.hazards.map(hazard => (
                                                <span key={hazard.id} className={styles.chip}>
                                                    <BiLinkAlt size={12} /> {hazard.code} — {hazard.name}
                                                </span>
                                            ))}
                                        </div>
                                    )}
                                </div>
                            ))}
                        </ReportGroup>

                        <ReportGroup
                            title="Timeline of Events"
                            editHref={`/analyses/${analysisId}/cast/step1`}
                            isEmpty={(timelineEventsQuery.data || []).length === 0}
                            emptyLabel="No timeline events registered yet."
                        >
                            {(timelineEventsQuery.data || []).map(item => (
                                <div key={item.id} className={styles.reportBlock}>
                                    <span className={styles.itemCode}>{item.code}</span>
                                    <p className={styles.itemField}>{item.eventDescription}</p>
                                    {item.questions && (
                                        <p className={styles.itemField}>
                                            <span className={styles.itemFieldLabel}>Questions: </span>
                                            {item.questions}
                                        </p>
                                    )}
                                </div>
                            ))}
                        </ReportGroup>

                        <ReportGroup
                            title="Physical Loss Analysis"
                            editHref={`/analyses/${analysisId}/cast/step1`}
                            isEmpty={(physicalLossAnalysesQuery.data || []).length === 0}
                            emptyLabel="No physical loss analysis registered yet."
                        >
                            {(physicalLossAnalysesQuery.data || []).map(item => (
                                <div key={item.id} className={styles.reportBlock}>
                                    <span className={styles.itemCode}>{item.code}</span>
                                    {item.physicalLossDescription && (
                                        <p className={styles.itemField}>{item.physicalLossDescription}</p>
                                    )}
                                    {item.affectedEquipment && (
                                        <p className={styles.itemField}>
                                            <span className={styles.itemFieldLabel}>Affected Equipment: </span>
                                            {item.affectedEquipment}
                                        </p>
                                    )}
                                    {item.failuresAndUnsafeInteractions && (
                                        <p className={styles.itemField}>
                                            <span className={styles.itemFieldLabel}>Failures and Unsafe Interactions: </span>
                                            {item.failuresAndUnsafeInteractions}
                                        </p>
                                    )}
                                    {item.missingOrInadequateControls && (
                                        <p className={styles.itemField}>
                                            <span className={styles.itemFieldLabel}>Missing or Inadequate Controls: </span>
                                            {item.missingOrInadequateControls}
                                        </p>
                                    )}
                                </div>
                            ))}
                        </ReportGroup>

                        {/* 2. Safety Control Structure */}

                        <ReportGroup
                            title="Safety Control Structure"
                            editHref={`/analyses/${analysisId}/cast/step2`}
                            isEmpty={(componentsQuery.data || []).length === 0}
                            emptyLabel="No components modeled yet."
                        >
                            {(componentsQuery.data || []).map(item => (
                                <div key={item.id} className={styles.reportBlock}>
                                    <span className={styles.itemCode}>{item.code}</span>
                                    <h4 className={styles.itemTitle}>{item.name}</h4>
                                    <p className={styles.itemField}>
                                        <span className={styles.itemFieldLabel}>Type: </span>
                                        {item.type}
                                    </p>
                                </div>
                            ))}
                        </ReportGroup>

                        {/* 3. Inadequate Control Actions */}

                        <ReportGroup
                            title="Inadequate Control Actions"
                            editHref={`/analyses/${analysisId}/cast/step3`}
                            isEmpty={icas.length === 0}
                            emptyLabel="No Inadequate Control Actions registered yet."
                        >
                            {icas.map(item => (
                                <div key={item.id} className={styles.reportBlock}>
                                    <span className={styles.itemCode}>{item.code}</span>
                                    <h4 className={styles.itemTitle}>
                                        {item.controlActionName} — {icaTypeToSelectOption(item.type).label}
                                    </h4>
                                    {item.description && <p className={styles.itemField}>{item.description}</p>}
                                    {item.context && (
                                        <p className={styles.itemField}>
                                            <span className={styles.itemFieldLabel}>Context: </span>
                                            {item.context}
                                        </p>
                                    )}
                                    {item.processModelFlaw && (
                                        <p className={styles.itemField}>
                                            <span className={styles.itemFieldLabel}>Process Model Flaw: </span>
                                            {item.processModelFlaw}
                                        </p>
                                    )}
                                </div>
                            ))}
                        </ReportGroup>

                        {/* 4. Systemic Factors */}

                        <ReportGroup
                            title="Systemic Factors"
                            editHref={`/analyses/${analysisId}/cast/step4`}
                            isEmpty={systemicFactors.length === 0}
                            emptyLabel="No Systemic Factors registered yet."
                        >
                            {systemicFactors.map(item => (
                                <div key={item.id} className={styles.reportBlock}>
                                    <span className={styles.itemCode}>{systemicFactorCategoryToSelectOption(item.category).label}</span>
                                    <p className={styles.itemField}>{item.description}</p>
                                    {item.inadequateControlActionIds && item.inadequateControlActionIds.length > 0 && (
                                        <div className={styles.chipsRow}>
                                            {item.inadequateControlActionIds.map(icaId => (
                                                <span key={icaId} className={styles.chip}>
                                                    <BiLinkAlt size={12} /> {getIcaLabel(icaId)}
                                                </span>
                                            ))}
                                        </div>
                                    )}
                                </div>
                            ))}
                        </ReportGroup>

                        {/* 5. Recommendations / Improvement Program */}

                        <ReportGroup
                            title="Recommendations"
                            editHref={`/analyses/${analysisId}/cast/step5`}
                            isEmpty={(recommendationsQuery.data || []).length === 0}
                            emptyLabel="No Recommendations registered yet."
                        >
                            {(recommendationsQuery.data || []).map(item => (
                                <div key={item.id} className={styles.reportBlock}>
                                    <p className={styles.itemField}>{item.description}</p>
                                    {(item.inadequateControlActionIds?.length > 0 || item.systemicFactorIds?.length > 0) && (
                                        <div className={styles.chipsRow}>
                                            {item.inadequateControlActionIds?.map(icaId => (
                                                <span key={icaId} className={styles.chip}>
                                                    <BiLinkAlt size={12} /> {getIcaLabel(icaId)}
                                                </span>
                                            ))}
                                            {item.systemicFactorIds?.map(systemicFactorId => (
                                                <span key={systemicFactorId} className={styles.chip}>
                                                    <BiLinkAlt size={12} /> {getSystemicFactorLabel(systemicFactorId)}
                                                </span>
                                            ))}
                                        </div>
                                    )}
                                </div>
                            ))}
                        </ReportGroup>

                    </div>
                )}

            </div>
        </div>
    );
}

interface ReportGroupProps {
    title: string;
    editHref: string;
    isEmpty: boolean;
    emptyLabel: string;
    children: React.ReactNode;
}

function ReportGroup({ title, editHref, isEmpty, emptyLabel, children }: ReportGroupProps) {
    return (
        <div className={styles.itemsList}>
            <div className={styles.reportBlock}>
                <h3 className={styles.groupTitle} style={{ margin: 0, padding: 0, border: 'none' }}>{title}</h3>
            </div>

            {isEmpty ? (
                <div className={styles.emptyGroupState}>
                    <span>{emptyLabel}</span>
                    <Link to={editHref} className={styles.editLink}>Edit →</Link>
                </div>
            ) : (
                children
            )}
        </div>
    );
}

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
import { getByAnalysisId as getCacs } from '@http/CAST/Step3/ControlActionContributions';
import { getByAnalysisId as getSystemicFactors } from '@http/CAST/Step4/SystemicFactors';
import { getByAnalysisId as getRecommendations } from '@http/CAST/Step5/Recommendations';
import { getByAnalysisId as getQuestionsRaised } from '@http/CAST/QuestionsRaised';

import { icaTypeToSelectOption } from '@interfaces/CAST/IStep3/IControlActionContribution';
import { systemicFactorCategoryLabel } from '@interfaces/CAST/IStep4/ISystemicFactor';
import {
    recommendationPriorityToSelectOption,
    recommendationStatusToSelectOption,
    recommendationTypeToSelectOption,
    recommendationScopeCategoryToSelectOption
} from '@interfaces/CAST/IStep5/IRecommendation';
import { formatDateBR } from '@services/date';
import CastTextarea from '@components/CastTextarea';

import castStyles from '../Step1/CastStepOne.module.css';
import styles from './CastReport.module.css';

interface Props {}

const CAST_STEP_TITLES: Record<number, string> = {
    1: 'Step 1 — Assemble Basic Information',
    2: 'Step 2 — Model the Safety Control Structure',
    3: 'Step 3 — Analyze Each Component in the Loss',
    4: 'Step 4 — Identify Control Structure Flaws',
    5: 'Step 5 — Create an Improvement Program'
};

const numberedText = (items?: string[]) => (items ?? []).map((item, i) => `${i + 1}. ${item}`).join('\n');

function ReportList({ label, items }: { label: string; items?: string[] }) {
    if (!items || items.length === 0) return null;
    return (
        <div className={styles.itemField}>
            <span className={styles.itemFieldLabel}>{label}: </span>
            <ol style={{ margin: '4px 0 0 0', paddingLeft: '20px' }}>
                {items.map((item, i) => <li key={i}>{item}</li>)}
            </ol>
        </div>
    );
}

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
    const cacsQuery = useQuery({
        queryKey: ['control-action-contributions', analysisId],
        queryFn: () => getCacs(analysisId)
    });
    const systemicFactorsQuery = useQuery({
        queryKey: ['systemic-factors', analysisId],
        queryFn: () => getSystemicFactors(analysisId)
    });
    const recommendationsQuery = useQuery({
        queryKey: ['recommendations', analysisId],
        queryFn: () => getRecommendations(analysisId)
    });
    const questionsRaisedQuery = useQuery({
        queryKey: ['questions-raised', analysisId],
        queryFn: () => getQuestionsRaised(analysisId)
    });

    const isLoading = systemDescriptionsQuery.isLoading
        || accidentLossEventsQuery.isLoading
        || castHazardsQuery.isLoading
        || violatedConstraintsQuery.isLoading
        || timelineEventsQuery.isLoading
        || physicalLossAnalysesQuery.isLoading
        || componentsQuery.isLoading
        || cacsQuery.isLoading
        || systemicFactorsQuery.isLoading
        || recommendationsQuery.isLoading
        || questionsRaisedQuery.isLoading;

    const cacs = cacsQuery.data || [];
    const systemicFactors = systemicFactorsQuery.data || [];
    const questionsRaised = questionsRaisedQuery.data || [];
    const questionSteps = Array.from(new Set(questionsRaised.map(q => q.castStep))).sort();

    const getCacLabel = (cacId: string) => {
        const cac = cacs.find(i => i.id === cacId);
        return cac ? `${cac.code} — ${cac.controlActionName}` : 'CAC not found';
    };
    const getSystemicFactorLabel = (systemicFactorId: string) => {
        const factor = systemicFactors.find(f => f.id === systemicFactorId);
        return factor
            ? `${systemicFactorCategoryLabel(factor)} — ${factor.description}`
            : 'Systemic Factor not found';
    };
    const getComponentLabel = (componentId: string) => {
        const component = (componentsQuery.data || []).find(c => c.id === componentId);
        return component ? `${component.code}: ${component.name}` : 'Unknown component';
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
                        title: 'System & System Boundary',
                        editUrl: editUrl(`/analyses/${analysisId}/cast/step1`),
                        emptyLabel: 'No system description registered yet.',
                        items: (systemDescriptionsQuery.data || []).map(item => ({
                            paragraphs: item.generalDescription ? [item.generalDescription] : [],
                            fields: [
                                item.analysisBoundary ? { label: 'System Name', value: item.analysisBoundary } : null,
                                item.description ? { label: 'System Boundary', value: item.description } : null
                            ].filter((field): field is { label: string; value: string } => field !== null)
                        }))
                    },
                    {
                        title: 'System Losses',
                        editUrl: editUrl(`/analyses/${analysisId}/cast/step1`),
                        emptyLabel: 'No accident/loss events registered yet.',
                        items: (accidentLossEventsQuery.data || []).map(item => ({
                            code: item.code,
                            title: item.name,
                            paragraphs: item.description ? [item.description] : []
                        }))
                    },
                    {
                        title: 'System Hazards',
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
                        title: 'Proximate Events and Questions Raised',
                        editUrl: editUrl(`/analyses/${analysisId}/cast/step1`),
                        emptyLabel: 'No timeline events registered yet.',
                        items: (timelineEventsQuery.data || []).map(item => ({
                            code: item.code,
                            paragraphs: [item.eventDescription],
                            fields: (item.questions ?? []).length > 0 ? [{ label: 'Questions Raised', value: numberedText(item.questions) }] : []
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
                        title: 'Control Action Contribution (CAC)',
                        editUrl: editUrl(`/analyses/${analysisId}/cast/step3`),
                        emptyLabel: 'No CACs registered yet.',
                        items: cacs.map(item => ({
                            code: item.code,
                            title: `${item.controlActionName} — ${icaTypeToSelectOption(item.type).label}`,
                            paragraphs: item.description ? [item.description] : [],
                            fields: [
                                (item.context ?? []).length > 0
                                    ? { label: 'Context / Contextual Factors', value: numberedText(item.context) }
                                    : null,
                                (item.processModelFlaw ?? []).length > 0
                                    ? { label: 'Process / Mental Model Flaws', value: numberedText(item.processModelFlaw) }
                                    : null,
                                (item.questions ?? []).length > 0
                                    ? { label: 'Questions Raised', value: numberedText(item.questions) }
                                    : null
                            ].filter((field): field is { label: string; value: string } => field !== null)
                        }))
                    },
                    {
                        title: 'Control Structure Flaws (Systemic Factors)',
                        editUrl: editUrl(`/analyses/${analysisId}/cast/step4`),
                        emptyLabel: 'No Systemic Factors registered yet.',
                        items: systemicFactors.map(item => ({
                            code: systemicFactorCategoryLabel(item),
                            paragraphs: [item.description],
                            chips: (item.controlActionContributionIds || []).map(getCacLabel)
                        }))
                    },
                    {
                        title: 'Recommendations',
                        editUrl: editUrl(`/analyses/${analysisId}/cast/step5`),
                        emptyLabel: 'No Recommendations registered yet.',
                        items: (recommendationsQuery.data || []).map(item => ({
                            paragraphs: [item.description],
                            fields: [
                                item.status
                                    ? { label: 'Implementation Status', value: recommendationStatusToSelectOption(item.status).label }
                                    : null,
                                item.priority
                                    ? { label: 'Priority / Timeframe', value: recommendationPriorityToSelectOption(item.priority).label }
                                    : null,
                                item.recommendationType
                                    ? { label: 'Recommendation Type', value: recommendationTypeToSelectOption(item.recommendationType).label }
                                    : null,
                                item.scopeCategory
                                    ? { label: 'Systemic Scope', value: recommendationScopeCategoryToSelectOption(item.scopeCategory).label }
                                    : null,
                                item.componentId
                                    ? { label: 'Target Controller', value: getComponentLabel(item.componentId) }
                                    : null,
                                item.assignee
                                    ? { label: 'Assignee', value: item.assignee }
                                    : null,
                                item.dueDate
                                    ? { label: 'Due Date', value: formatDateBR(item.dueDate) }
                                    : null,
                                item.auditMechanism
                                    ? { label: 'Feedback / Audit Mechanism', value: item.auditMechanism }
                                    : null,
                                item.effectivenessEvidence
                                    ? { label: 'Audit Findings / Effectiveness Evidence', value: item.effectivenessEvidence }
                                    : null
                            ].filter((field): field is { label: string; value: string } => field !== null),
                            chips: [
                                ...(item.controlActionContributionIds || []).map(getCacLabel),
                                ...(item.systemicFactorIds || []).map(getSystemicFactorLabel)
                            ]
                        }))
                    },
                    {
                        title: 'Questions Raised',
                        editUrl: editUrl(`/analyses/${analysisId}/cast/step1`),
                        emptyLabel: 'No Questions Raised registered yet.',
                        items: questionSteps.map(step => ({
                            title: CAST_STEP_TITLES[step],
                            paragraphs: questionsRaised
                                .filter(q => q.castStep === step)
                                .map(q => `${q.code}: ${q.question}`)
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
                    <div style={{ color: 'var(--color-muted-text)', fontSize: '14px', padding: '10px' }}>Loading report data...</div>
                ) : (
                    <div className={styles.reportContainer}>

                        <div className={`${styles.reportBlock} ${styles.titleBlock}`}>
                            <h2 className={styles.reportMainTitle}>CAST Accident Analysis Report</h2>
                            <p className={styles.reportMeta}>Analysis ID: {analysisId}</p>
                            <p className={styles.reportMeta}>Generated on {new Date().toLocaleString()}</p>
                        </div>

                        <div className={styles.reportBlock}>
                            <h3 className={styles.groupTitle}>Executive Summary</h3>
                            <CastTextarea
                                className={styles.summaryTextarea}
                                placeholder="Optional free-text synthesis of the analysis, written by the analyst for this report."
                                value={executiveSummary}
                                onChange={e => setExecutiveSummary(e.target.value)}
                            />
                        </div>

                        {/* 1. Purpose of the Analysis (CAST Handbook, Leveson 2019) */}

                        <ReportGroup
                            title="System & System Boundary"
                            editHref={`/analyses/${analysisId}/cast/step1`}
                            isEmpty={(systemDescriptionsQuery.data || []).length === 0}
                            emptyLabel="No system description registered yet."
                        >
                            {(systemDescriptionsQuery.data || []).map(item => (
                                <div key={item.id} className={styles.reportBlock}>
                                    {item.generalDescription && <p className={styles.itemField}>{item.generalDescription}</p>}
                                    {item.analysisBoundary && (
                                        <p className={styles.itemField}>
                                            <span className={styles.itemFieldLabel}>System Name: </span>
                                            {item.analysisBoundary}
                                        </p>
                                    )}
                                    {item.description && (
                                        <p className={styles.itemField}>
                                            <span className={styles.itemFieldLabel}>System Boundary: </span>
                                            {item.description}
                                        </p>
                                    )}
                                </div>
                            ))}
                        </ReportGroup>

                        <ReportGroup
                            title="System Losses"
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
                            title="System Hazards"
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
                            title="Proximate Events and Questions Raised"
                            editHref={`/analyses/${analysisId}/cast/step1`}
                            isEmpty={(timelineEventsQuery.data || []).length === 0}
                            emptyLabel="No timeline events registered yet."
                        >
                            {(timelineEventsQuery.data || []).map(item => (
                                <div key={item.id} className={styles.reportBlock}>
                                    <span className={styles.itemCode}>{item.code}</span>
                                    <p className={styles.itemField}>{item.eventDescription}</p>
                                    <ReportList label="Questions Raised" items={item.questions} />
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

                        {/* 3. Control Action Contribution (CAC) */}

                        <ReportGroup
                            title="Control Action Contribution (CAC)"
                            editHref={`/analyses/${analysisId}/cast/step3`}
                            isEmpty={cacs.length === 0}
                            emptyLabel="No CACs registered yet."
                        >
                            {cacs.map(item => (
                                <div key={item.id} className={styles.reportBlock}>
                                    <span className={styles.itemCode}>{item.code}</span>
                                    <h4 className={styles.itemTitle}>
                                        {item.controlActionName} — {icaTypeToSelectOption(item.type).label}
                                    </h4>
                                    {item.description && <p className={styles.itemField}>{item.description}</p>}
                                    <ReportList label="Context / Contextual Factors" items={item.context} />
                                    <ReportList label="Process / Mental Model Flaws" items={item.processModelFlaw} />
                                    <ReportList label="Questions Raised" items={item.questions} />
                                </div>
                            ))}
                        </ReportGroup>

                        {/* 4. Systemic Factors */}

                        <ReportGroup
                            title="Control Structure Flaws (Systemic Factors)"
                            editHref={`/analyses/${analysisId}/cast/step4`}
                            isEmpty={systemicFactors.length === 0}
                            emptyLabel="No Systemic Factors registered yet."
                        >
                            {systemicFactors.map(item => (
                                <div key={item.id} className={styles.reportBlock}>
                                    <span className={styles.itemCode}>{systemicFactorCategoryLabel(item)}</span>
                                    <p className={styles.itemField}>{item.description}</p>
                                    {item.controlActionContributionIds && item.controlActionContributionIds.length > 0 && (
                                        <div className={styles.chipsRow}>
                                            {item.controlActionContributionIds.map(cacId => (
                                                <span key={cacId} className={styles.chip}>
                                                    <BiLinkAlt size={12} /> {getCacLabel(cacId)}
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
                                    {item.status && (
                                        <p className={styles.itemField}>
                                            <span className={styles.itemFieldLabel}>Implementation Status: </span>
                                            {recommendationStatusToSelectOption(item.status).label}
                                        </p>
                                    )}
                                    {item.priority && (
                                        <p className={styles.itemField}>
                                            <span className={styles.itemFieldLabel}>Priority / Timeframe: </span>
                                            {recommendationPriorityToSelectOption(item.priority).label}
                                        </p>
                                    )}
                                    {item.recommendationType && (
                                        <p className={styles.itemField}>
                                            <span className={styles.itemFieldLabel}>Recommendation Type: </span>
                                            {recommendationTypeToSelectOption(item.recommendationType).label}
                                        </p>
                                    )}
                                    {item.scopeCategory && (
                                        <p className={styles.itemField}>
                                            <span className={styles.itemFieldLabel}>Systemic Scope: </span>
                                            {recommendationScopeCategoryToSelectOption(item.scopeCategory).label}
                                        </p>
                                    )}
                                    {item.componentId && (
                                        <p className={styles.itemField}>
                                            <span className={styles.itemFieldLabel}>Target Controller: </span>
                                            {getComponentLabel(item.componentId)}
                                        </p>
                                    )}
                                    {item.assignee && (
                                        <p className={styles.itemField}>
                                            <span className={styles.itemFieldLabel}>Assignee: </span>
                                            {item.assignee}
                                        </p>
                                    )}
                                    {item.dueDate && (
                                        <p className={styles.itemField}>
                                            <span className={styles.itemFieldLabel}>Due Date: </span>
                                            {formatDateBR(item.dueDate)}
                                        </p>
                                    )}
                                    {item.auditMechanism && (
                                        <p className={styles.itemField}>
                                            <span className={styles.itemFieldLabel}>Feedback / Audit Mechanism: </span>
                                            {item.auditMechanism}
                                        </p>
                                    )}
                                    {item.effectivenessEvidence && (
                                        <p className={styles.itemField}>
                                            <span className={styles.itemFieldLabel}>Audit Findings / Effectiveness Evidence: </span>
                                            {item.effectivenessEvidence}
                                        </p>
                                    )}
                                    {(item.controlActionContributionIds?.length > 0 || item.systemicFactorIds?.length > 0) && (
                                        <div className={styles.chipsRow}>
                                            {item.controlActionContributionIds?.map(cacId => (
                                                <span key={cacId} className={styles.chip}>
                                                    <BiLinkAlt size={12} /> {getCacLabel(cacId)}
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


                        <ReportGroup
                            title="Questions Raised"
                            editHref={`/analyses/${analysisId}/cast/step1`}
                            isEmpty={questionsRaised.length === 0}
                            emptyLabel="No Questions Raised registered yet."
                        >
                            {questionSteps.map(step => (
                                <div key={step} className={styles.reportBlock}>
                                    <h4 className={styles.itemTitle}>{CAST_STEP_TITLES[step]}</h4>
                                    {questionsRaised.filter(q => q.castStep === step).map(q => (
                                        <p key={q.id} className={styles.itemField}>
                                            <span className={styles.itemCode}>{q.code}</span> {q.question}
                                        </p>
                                    ))}
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

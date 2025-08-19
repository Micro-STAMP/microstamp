import Button from "@components/Button";
import { Textarea } from "@components/FormField";
import Loader from "@components/Loader";
import {
	ModalButtons,
	ModalContainer,
	ModalHeader,
	ModalInputs,
	ModalProps
} from "@components/Modal/Templates";
import NoResultsMessage from "@components/NoResultsMessage";
import { getRefinedScenariosCommonCausesByUCA } from "@http/Step4New/RefinedScenarios";
import { IUnsafeControlActionReadDto } from "@interfaces/IStep3";
import {
	IRefinedScenarioFormData,
	IRefinedScenarioReadDto,
	IRefinedScenariosCommonCausesDto
} from "@interfaces/IStep4New/IRefinedScenarios";
import { useQuery } from "@tanstack/react-query";
import { useEffect, useState } from "react";
import { BiCheckDouble as CheckIcon, BiUndo as ReturnIcon } from "react-icons/bi";
import { toast } from "sonner";
import styles from "./ModalRefinedScenarios.module.css";

interface ModalRefinedScenariosProps extends ModalProps {
	onSubmit: (refinedScenario: IRefinedScenarioFormData) => Promise<void>;
	isLoading?: boolean;
	uca: IUnsafeControlActionReadDto;
	refinedScenario?: IRefinedScenarioReadDto;
	formalScenarioClassId: string;
}
function ModalRefinedScenarios({
	open,
	onClose,
	onSubmit,
	uca,
	isLoading = false,
	formalScenarioClassId,
	refinedScenario
}: ModalRefinedScenariosProps) {
	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Refined Scenario Data

	const [refinedScenarioData, setRefinedScenarioData] = useState<IRefinedScenarioFormData>({
		refinedScenario: refinedScenario ? refinedScenario.refinedScenario : "",
		commonCauseId: refinedScenario ? refinedScenario.commonCauseId : "",
		formalScenarioClassId: refinedScenario
			? refinedScenario.formalScenarioClassId
			: formalScenarioClassId
	});

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Get Common Causes

	const {
		data: commonCauses,
		isLoading: isLoadingCommonCauses,
		isError: isErrorCommonCauses
	} = useQuery({
		queryKey: ["common-causes", uca.id],
		queryFn: () => getRefinedScenariosCommonCausesByUCA(uca.id)
	});

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Selected Common Cause & Template

	const [selectedCause, setSelectedCause] = useState<IRefinedScenariosCommonCausesDto | null>(
		null
	);

	useEffect(() => {
		if (!open || !commonCauses) return;
		if (refinedScenario) {
			const cause =
				commonCauses.find(cc => cc.id === refinedScenario.commonCauseId) || commonCauses[0];
			setSelectedCause(cause);
			setRefinedScenarioData({
				refinedScenario: refinedScenario.refinedScenario,
				commonCauseId: cause.id,
				formalScenarioClassId: refinedScenario.formalScenarioClassId
			});
		} else {
			const cause = commonCauses[0];
			setSelectedCause(cause);
			setRefinedScenarioData({
				refinedScenario: "",
				commonCauseId: cause.id,
				formalScenarioClassId: formalScenarioClassId
			});
		}
	}, [open, commonCauses, refinedScenario, formalScenarioClassId]);

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Select Cause & Template

	const handleCommonCauseSelect = (cause: IRefinedScenariosCommonCausesDto) => {
		setSelectedCause(cause);
		setRefinedScenarioData({
			...refinedScenarioData,
			commonCauseId: cause.id,
			refinedScenario: ""
		});
	};
	const handleTemplateSelect = (template: string) => {
		setRefinedScenarioData({
			...refinedScenarioData,
			refinedScenario: template
		});
	};

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Submit Refined Scenario

	const handleSubmitRefinedScenario = async () => {
		if (!refinedScenarioData.refinedScenario || !refinedScenarioData.commonCauseId) {
			toast.warning("A required field is empty.");
			return;
		}
		await onSubmit(refinedScenarioData);
		if (!refinedScenario) {
			setRefinedScenarioData({
				refinedScenario: "",
				commonCauseId: "",
				formalScenarioClassId: formalScenarioClassId
			});
		}
		setSelectedCause(
			commonCauses
				? refinedScenario
					? commonCauses.find(cc => cc.id === refinedScenario.commonCauseId)!
					: commonCauses[0]
				: null
		);
		onClose();
	};

	/* - - - - - - - - - - - - - - - - - - - - - - */

	if (isLoadingCommonCauses)
		return (
			<ModalContainer open={open} size="small">
				<Loader />;
			</ModalContainer>
		);
	if (isErrorCommonCauses || commonCauses === undefined)
		return (
			<ModalContainer open={open} size="small">
				<ModalHeader onClose={onClose} title="Error" />
				<NoResultsMessage message="Error loading modal." />
			</ModalContainer>
		);

	/* - - - - - - - - - - - - - - - - - - - - - - */

	const filteredTemplates =
		selectedCause?.templates.filter(
			template =>
				!template.unsafeControlActionType || template.unsafeControlActionType === uca.type
		) || [];
	return (
		<ModalContainer open={open} size="big">
			<ModalHeader
				onClose={onClose}
				title={refinedScenario ? "Update Refined Scenario" : "Create Refined Scenario"}
			/>
			{selectedCause ? (
				<>
					<div className={styles.common_cause_container}>
						<div className={styles.common_causes_select}>
							<div className={styles.causes_list}>
								{commonCauses.map(cause => (
									<button
										key={cause.id}
										className={`${styles.cause_button} ${
											selectedCause.id === cause.id ? styles.active : ""
										}`}
										onClick={() => handleCommonCauseSelect(cause)}
									>
										{cause.code}
									</button>
								))}
							</div>
							<div className={styles.cause_description}>
								<span>Common Cause:</span> {selectedCause.cause}
							</div>
						</div>
						<div className={styles.templates}>
							<span className={styles.title}>Templates:</span>
							<div className={styles.templates_list}>
								{filteredTemplates.map(template => (
									<div
										key={template.id}
										className={styles.template_item}
										onClick={() => handleTemplateSelect(template.template)}
										title="Apply Template"
									>
										{template.template}
									</div>
								))}
							</div>
						</div>
					</div>
					<ModalInputs>
						<Textarea
							label="Refined Scenario"
							value={refinedScenarioData.refinedScenario}
							onChange={(value: string) =>
								setRefinedScenarioData({
									...refinedScenarioData,
									refinedScenario: value
								})
							}
							required
							rows={6}
						/>
					</ModalInputs>
					<ModalButtons>
						<Button variant="dark" onClick={onClose} size="small" icon={ReturnIcon}>
							Cancel
						</Button>
						<Button
							onClick={handleSubmitRefinedScenario}
							isLoading={isLoading}
							size="small"
							icon={CheckIcon}
						>
							{refinedScenario
								? "Update Refined Scenario"
								: "Create Refined Scenario"}
						</Button>
					</ModalButtons>
				</>
			) : (
				<NoResultsMessage message="Error loading modal." />
			)}
		</ModalContainer>
	);
}

export default ModalRefinedScenarios;

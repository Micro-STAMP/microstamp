import Button from "@components/Button";
import { Input } from "@components/FormField";
import SystemSafetyConstraintSelect from "@components/FormField/Select/SystemSafetyConstraintSelect";
import {
	systemSafetyConstraintsToSelectOptions,
	systemSafetyConstraintToSelectOption
} from "@components/FormField/Select/SystemSafetyConstraintSelect/util";
import { SelectOption } from "@components/FormField/Templates";
import Loader from "@components/Loader";
import {
	ModalButtons,
	ModalContainer,
	ModalHeader,
	ModalInputs,
	ModalProps
} from "@components/Modal/Templates";
import { getSystemSafetyConstraints } from "@http/Step1/SystemSafetyConstraints";
import { IResponsibilityFormData, IResponsibilityReadDto } from "@interfaces/IStep2";
import { useQuery } from "@tanstack/react-query";
import { useEffect, useState } from "react";
import { BiCheckDouble as CheckIcon, BiUndo as ReturnIcon } from "react-icons/bi";
import { toast } from "sonner";

interface ModalResponsibilityProps extends ModalProps {
	onSubmit: (responsibility: IResponsibilityFormData) => Promise<void>;
	title: string;
	analysisId: string;
	isLoading?: boolean;
	responsibility?: IResponsibilityReadDto;
	btnText?: string;
}
function ModalResponsibility({
	open,
	onClose,
	onSubmit,
	title,
	analysisId,
	responsibility,
	isLoading = false,
	btnText = "Confirm"
}: ModalResponsibilityProps) {
	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * System Safety Constraints Options

	const [sscOptions, setSscOptions] = useState<SelectOption[]>([]);
	const {
		data: systemSafetyConstraints,
		isLoading: isLoadingSSCs,
		isError
	} = useQuery({
		queryKey: ["ssc-select-options", analysisId],
		queryFn: () => getSystemSafetyConstraints(analysisId)
	});

	useEffect(() => {
		if (systemSafetyConstraints) {
			setSscOptions(systemSafetyConstraintsToSelectOptions(systemSafetyConstraints));
		}
	}, [systemSafetyConstraints]);

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Responsibility Data

	const [responsibilityData, setResponsibilityData] = useState<IResponsibilityFormData>({
		responsibility: responsibility?.responsibility || "",
		code: responsibility?.code || "",
		systemSafetyConstraint: responsibility
			? systemSafetyConstraintToSelectOption(responsibility.systemSafetyConstraint)
			: sscOptions[0]
	});

	useEffect(() => {
		if (responsibility) {
			setResponsibilityData({
				responsibility: responsibility.responsibility,
				code: responsibility.code,
				systemSafetyConstraint: systemSafetyConstraintToSelectOption(
					responsibility.systemSafetyConstraint
				)
			});
		} else {
			setResponsibilityData({
				responsibility: "",
				code: "",
				systemSafetyConstraint: sscOptions[0]
			});
		}
	}, [responsibility, sscOptions, systemSafetyConstraints]);

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Submit Responsibility

	const handleSubmitResponsibility = async () => {
		if (
			!responsibilityData.responsibility ||
			!responsibilityData.code ||
			!responsibilityData.systemSafetyConstraint
		) {
			toast.warning("A required field is empty.");
			return;
		}
		await onSubmit(responsibilityData);
		setResponsibilityData({
			responsibility: responsibility?.responsibility || "",
			code: responsibility?.code || "",
			systemSafetyConstraint: responsibility
				? systemSafetyConstraintToSelectOption(responsibility.systemSafetyConstraint)
				: sscOptions[0]
		});
		onClose();
	};

	/* - - - - - - - - - - - - - - - - - - - - - - */

	if (isLoadingSSCs) return <Loader />;
	if (isError || systemSafetyConstraints === undefined) return <h1>Error</h1>;
	return (
		<ModalContainer open={open} size="big">
			<ModalHeader onClose={onClose} title={title} />
			<ModalInputs column="double">
				<Input
					label="Name"
					value={responsibilityData.responsibility}
					onChange={(value: string) =>
						setResponsibilityData({ ...responsibilityData, responsibility: value })
					}
					required
				/>
				<Input
					label="Code"
					value={responsibilityData.code}
					onChange={(value: string) =>
						setResponsibilityData({ ...responsibilityData, code: value })
					}
					required
				/>
				<SystemSafetyConstraintSelect
					systemSafetyConstraints={sscOptions}
					onChange={(value: SelectOption | null) =>
						setResponsibilityData({
							...responsibilityData,
							systemSafetyConstraint: value
						})
					}
					value={responsibilityData.systemSafetyConstraint}
				/>
			</ModalInputs>
			<ModalButtons>
				<Button variant="dark" onClick={onClose} size="small" icon={ReturnIcon}>
					Cancel
				</Button>
				<Button
					onClick={handleSubmitResponsibility}
					isLoading={isLoading}
					size="small"
					icon={CheckIcon}
				>
					{btnText}
				</Button>
			</ModalButtons>
		</ModalContainer>
	);
}

export default ModalResponsibility;

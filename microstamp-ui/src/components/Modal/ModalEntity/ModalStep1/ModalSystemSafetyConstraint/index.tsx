import Button from "@components/Button";
import { Input } from "@components/FormField";
import HazardsMultiSelect from "@components/FormField/MultiSelect/HazardsMultiSelect";
import { hazardsToSelectOptions } from "@components/FormField/MultiSelect/HazardsMultiSelect/util";
import { SelectOption } from "@components/FormField/Templates";
import {
	ModalButtons,
	ModalContainer,
	ModalHeader,
	ModalInputs,
	ModalProps
} from "@components/Modal/Templates";
import {
	ISystemSafetyConstraintFormData,
	ISystemSafetyConstraintReadDto
} from "@interfaces/IStep1";
import { useEffect, useState } from "react";
import { BiCheckDouble as CheckIcon, BiUndo as ReturnIcon } from "react-icons/bi";
import { toast } from "sonner";

interface ModalSystemSafetyConstraintProps extends ModalProps {
	onSubmit: (systemSafetyConstraint: ISystemSafetyConstraintFormData) => Promise<void>;
	title: string;
	analysisId: string;
	isLoading?: boolean;
	systemSafetyConstraint?: ISystemSafetyConstraintReadDto;
	btnText?: string;
}
function ModalSystemSafetyConstraint({
	open,
	onClose,
	onSubmit,
	analysisId,
	title,
	isLoading = false,
	systemSafetyConstraint,
	btnText = "Confirm"
}: ModalSystemSafetyConstraintProps) {
	const [systemSafetyConstraintData, setSystemSafetyConstraintData] =
		useState<ISystemSafetyConstraintFormData>({
			name: systemSafetyConstraint?.name || "",
			code: systemSafetyConstraint?.code || "",
			hazards: systemSafetyConstraint
				? hazardsToSelectOptions(systemSafetyConstraint.hazards)
				: []
		});

	useEffect(() => {
		if (systemSafetyConstraint) {
			setSystemSafetyConstraintData({
				name: systemSafetyConstraint.name,
				code: systemSafetyConstraint.code,
				hazards: hazardsToSelectOptions(systemSafetyConstraint.hazards)
			});
		} else {
			setSystemSafetyConstraintData({
				name: "",
				code: "",
				hazards: []
			});
		}
	}, [systemSafetyConstraint]);

	const handleSubmitSystemSafetyConstraint = async () => {
		if (
			!systemSafetyConstraintData.name ||
			!systemSafetyConstraintData.code ||
			systemSafetyConstraintData.hazards.length === 0
		) {
			toast.warning("A required field is empty.");
			return;
		}
		await onSubmit(systemSafetyConstraintData);
		setSystemSafetyConstraintData({
			name: systemSafetyConstraint ? systemSafetyConstraintData.name : "",
			code: systemSafetyConstraint ? systemSafetyConstraintData.code : "",
			hazards: systemSafetyConstraint
				? hazardsToSelectOptions(systemSafetyConstraint.hazards)
				: []
		});
		onClose();
	};

	return (
		<ModalContainer open={open}>
			<ModalHeader onClose={onClose} title={title} />
			<ModalInputs>
				<Input
					label="Name"
					value={systemSafetyConstraintData.name}
					onChange={(value: string) =>
						setSystemSafetyConstraintData({
							...systemSafetyConstraintData,
							name: value
						})
					}
					required
				/>
				<Input
					label="Code"
					value={systemSafetyConstraintData.code}
					onChange={(value: string) =>
						setSystemSafetyConstraintData({
							...systemSafetyConstraintData,
							code: value
						})
					}
					required
				/>
				<HazardsMultiSelect
					analysisId={analysisId}
					hazards={systemSafetyConstraintData.hazards}
					onChange={(hazards: SelectOption[]) =>
						setSystemSafetyConstraintData({
							...systemSafetyConstraintData,
							hazards: hazards
						})
					}
				/>
			</ModalInputs>
			<ModalButtons>
				<Button variant="dark" onClick={onClose} size="small" icon={ReturnIcon}>
					Cancel
				</Button>
				<Button
					onClick={handleSubmitSystemSafetyConstraint}
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

export default ModalSystemSafetyConstraint;

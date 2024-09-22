import Button from "@components/Button";
import { Input } from "@components/FormField";
import {
	ModalButtons,
	ModalContainer,
	ModalHeader,
	ModalInputs,
	ModalProps
} from "@components/Modal/Templates";
import { ISystemGoalFormData, ISystemGoalReadDto } from "@interfaces/IStep1";
import { useEffect, useState } from "react";
import { BiCheckDouble as CheckIcon, BiUndo as ReturnIcon } from "react-icons/bi";
import { toast } from "sonner";

interface ModalSystemGoalProps extends ModalProps {
	onSubmit: (systemGoal: ISystemGoalFormData) => Promise<void>;
	title: string;
	isLoading?: boolean;
	systemGoal?: ISystemGoalReadDto;
	btnText?: string;
}
function ModalSystemGoal({
	open,
	onClose,
	onSubmit,
	title,
	isLoading = false,
	systemGoal,
	btnText = "Confirm"
}: ModalSystemGoalProps) {
	const [systemGoalData, setSystemGoalData] = useState<ISystemGoalFormData>({
		name: systemGoal?.name || "",
		code: systemGoal?.code || ""
	});

	useEffect(() => {
		if (systemGoal) {
			setSystemGoalData({
				name: systemGoal.name,
				code: systemGoal.code
			});
		} else {
			setSystemGoalData({
				name: "",
				code: ""
			});
		}
	}, [systemGoal]);

	const handleSubmitSystemGoal = async () => {
		if (!systemGoalData.name || !systemGoalData.code) {
			toast.warning("A required field is empty.");
			return;
		}
		await onSubmit(systemGoalData);
		setSystemGoalData({
			name: systemGoal ? systemGoalData.name : "",
			code: systemGoal ? systemGoalData.code : ""
		});
		onClose();
	};

	return (
		<ModalContainer open={open}>
			<ModalHeader onClose={onClose} title={title} />
			<ModalInputs>
				<Input
					label="Name"
					value={systemGoalData.name}
					onChange={(value: string) =>
						setSystemGoalData({ ...systemGoalData, name: value })
					}
					required
				/>
				<Input
					label="Code"
					value={systemGoalData.code}
					onChange={(value: string) =>
						setSystemGoalData({ ...systemGoalData, code: value })
					}
					required
				/>
			</ModalInputs>
			<ModalButtons>
				<Button variant="dark" onClick={onClose} size="small" icon={ReturnIcon}>
					Cancel
				</Button>
				<Button
					onClick={handleSubmitSystemGoal}
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

export default ModalSystemGoal;

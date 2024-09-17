import Button from "@components/Button";
import { Input } from "@components/FormField";
import {
	ModalButtons,
	ModalContainer,
	ModalHeader,
	ModalInputs,
	ModalProps
} from "@components/Modal/Templates";
import { IAssumptionFormData, IAssumptionReadDto } from "@interfaces/IStep1";
import { useEffect, useState } from "react";
import { BiCheckDouble as CheckIcon, BiUndo as ReturnIcon } from "react-icons/bi";
import { toast } from "sonner";

interface ModalAssumptionProps extends ModalProps {
	onSubmit: (assumption: IAssumptionFormData) => Promise<void>;
	title: string;
	isLoading?: boolean;
	assumption?: IAssumptionReadDto;
	btnText?: string;
}
function ModalAssumption({
	open,
	onClose,
	onSubmit,
	title,
	isLoading = false,
	assumption,
	btnText = "Confirm"
}: ModalAssumptionProps) {
	const [assumptionData, setAssumptionData] = useState<IAssumptionFormData>({
		name: assumption?.name || "",
		code: assumption?.code || ""
	});

	useEffect(() => {
		if (assumption) {
			setAssumptionData({
				name: assumption.name,
				code: assumption.code
			});
		} else {
			setAssumptionData({
				name: "",
				code: ""
			});
		}
	}, [assumption]);

	const handleSubmitAssumption = async () => {
		if (!assumptionData.name || !assumptionData.code) {
			toast.warning("A required field is empty.");
			return;
		}
		await onSubmit(assumptionData);
		setAssumptionData({
			name: assumption ? assumptionData.name : "",
			code: assumption ? assumptionData.code : ""
		});
		onClose();
	};

	return (
		<ModalContainer open={open}>
			<ModalHeader onClose={onClose} title={title} />
			<ModalInputs>
				<Input
					label="Name"
					value={assumptionData.name}
					onChange={(value: string) =>
						setAssumptionData({ ...assumptionData, name: value })
					}
					required
				/>
				<Input
					label="Code"
					value={assumptionData.code}
					onChange={(value: string) =>
						setAssumptionData({ ...assumptionData, code: value })
					}
					required
				/>
			</ModalInputs>
			<ModalButtons>
				<Button variant="dark" onClick={onClose} size="small" icon={ReturnIcon}>
					Cancel
				</Button>
				<Button
					onClick={handleSubmitAssumption}
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

export default ModalAssumption;

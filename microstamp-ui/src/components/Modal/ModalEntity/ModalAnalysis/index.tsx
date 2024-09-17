import Button from "@components/Button";
import { Input } from "@components/FormField";
import {
	ModalButtons,
	ModalContainer,
	ModalHeader,
	ModalInputs,
	ModalProps
} from "@components/Modal/Templates";
import { IAnalysisFormData, IAnalysisReadDto } from "@interfaces/IAnalysis";
import { useState } from "react";
import { BiCheckDouble as CheckIcon, BiUndo as ReturnIcon } from "react-icons/bi";
import { toast } from "sonner";

interface ModalAnalysisProps extends ModalProps {
	onSubmit: (analysis: IAnalysisFormData) => Promise<void>;
	title: string;
	isLoading?: boolean;
	analysis?: IAnalysisReadDto;
	btnText?: string;
}
function ModalAnalysis({
	open,
	onClose,
	onSubmit,
	title,
	isLoading = false,
	analysis,
	btnText = "Confirm"
}: ModalAnalysisProps) {
	const [analysisData, setAnalysisData] = useState<IAnalysisFormData>({
		name: analysis?.name || "",
		description: analysis?.description || ""
	});

	const handleSubmitAnalysis = async () => {
		if (!analysisData.name || !analysisData.description) {
			toast.warning("A required field is empty.");
			return;
		}
		await onSubmit(analysisData);
		setAnalysisData({
			name: analysis ? analysisData.name : "",
			description: analysis ? analysisData.description : ""
		});
		onClose();
	};

	return (
		<ModalContainer open={open}>
			<ModalHeader onClose={onClose} title={title} />
			<ModalInputs>
				<Input
					label="Name"
					value={analysisData.name}
					onChange={(value: string) => setAnalysisData({ ...analysisData, name: value })}
					required
				/>
				<Input
					label="Description"
					value={analysisData.description}
					onChange={(value: string) =>
						setAnalysisData({ ...analysisData, description: value })
					}
					required
				/>
			</ModalInputs>
			<ModalButtons>
				<Button variant="dark" onClick={onClose} size="small" icon={ReturnIcon}>
					Cancel
				</Button>
				<Button
					onClick={handleSubmitAnalysis}
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

export default ModalAnalysis;

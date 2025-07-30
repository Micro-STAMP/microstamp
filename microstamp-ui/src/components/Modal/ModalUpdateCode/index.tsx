import Button from "@components/Button";
import { Input } from "@components/FormField";
import {
	ModalButtons,
	ModalContainer,
	ModalHeader,
	ModalInputs,
	ModalProps
} from "@components/Modal/Templates";
import { useState } from "react";
import { BiCheckDouble as CheckIcon, BiUndo as ReturnIcon } from "react-icons/bi";
import { toast } from "sonner";

interface ModalUpdateCodeProps extends ModalProps {
	title: string;
	code: string;
	onSubmit: (code: string) => Promise<void>;
	isLoading?: boolean;
}
function ModalUpdateCode({
	title,
	code,
	open,
	onClose,
	onSubmit,
	isLoading = false
}: ModalUpdateCodeProps) {
	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Code Data

	const [codeData, setCodeData] = useState(code);

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Submit Code

	const handleSubmitCode = async () => {
		if (!codeData) {
			toast.error("Code cannot be empty.");
			return;
		}
		await onSubmit(codeData);
		onClose();
	};

	/* - - - - - - - - - - - - - - - - - - - - - - */

	return (
		<ModalContainer open={open} size="normal">
			<ModalHeader onClose={onClose} title={title} />
			<ModalInputs column="single">
				<Input
					label="Code"
					value={codeData}
					onChange={(value: string) => setCodeData(value)}
					required
				/>
			</ModalInputs>
			<ModalButtons>
				<Button variant="dark" onClick={onClose} size="small" icon={ReturnIcon}>
					Cancel
				</Button>
				<Button
					onClick={handleSubmitCode}
					isLoading={isLoading}
					size="small"
					icon={CheckIcon}
				>
					Update
				</Button>
			</ModalButtons>
		</ModalContainer>
	);
}

export default ModalUpdateCode;

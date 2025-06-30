import Button from "@components/Button";
import { Input, MultiSelect } from "@components/FormField";
import { SelectOption } from "@components/FormField/Templates";
import {
	ModalButtons,
	ModalContainer,
	ModalHeader,
	ModalInputs,
	ModalProps
} from "@components/Modal/Templates";
import { IFourTupleFormData } from "@interfaces/IStep4";
import { useState } from "react";
import { BiCheckDouble as CheckIcon, BiUndo as ReturnIcon } from "react-icons/bi";
import { toast } from "sonner";

interface ModalFourTupleProps extends ModalProps {
	onSubmit: (tuple: IFourTupleFormData) => Promise<void>;
	analysisId: string;
	isLoading?: boolean;
}
function ModalFourTuple({
	analysisId,
	open,
	onClose,
	onSubmit,
	isLoading = false
}: ModalFourTupleProps) {
	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Four Tuple Data

	const tupleInitialData: IFourTupleFormData = {
		analysisId: analysisId,
		associatedCausalFactor: "",
		rationale: "",
		recommendation: "",
		scenario: "",
		code: "",
		unsafeControlActions: []
	};
	const [tupleData, setTupleData] = useState<IFourTupleFormData>(tupleInitialData);

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Submit Four Tuple

	const handleSubmitFourTuple = async () => {
		if (
			!tupleData.associatedCausalFactor ||
			!tupleData.rationale ||
			!tupleData.recommendation ||
			!tupleData.scenario ||
			!tupleData.code ||
			!tupleData.unsafeControlActions.length
		) {
			toast.warning("A required field is empty.");
			return;
		}
		await onSubmit(tupleData);
		setTupleData(tupleInitialData);
		onClose();
	};

	/* - - - - - - - - - - - - - - - - - - - - - - */

	return (
		<ModalContainer open={open} size="big">
			<ModalHeader onClose={onClose} title="Causal Scenario" />
			<ModalInputs column="double">
				<Input
					label="Code"
					value={tupleData.code}
					onChange={(value: string) => setTupleData({ ...tupleData, code: value })}
					required
				/>
				<Input
					label="Scenario"
					value={tupleData.scenario}
					onChange={(value: string) => setTupleData({ ...tupleData, scenario: value })}
					required
				/>
				<Input
					label="Associated Causal Factor"
					value={tupleData.associatedCausalFactor}
					onChange={(value: string) =>
						setTupleData({ ...tupleData, associatedCausalFactor: value })
					}
					required
				/>
				<Input
					label="Recommendation"
					value={tupleData.recommendation}
					onChange={(value: string) =>
						setTupleData({ ...tupleData, recommendation: value })
					}
					required
				/>
				<Input
					label="Rationale"
					value={tupleData.rationale}
					onChange={(value: string) => setTupleData({ ...tupleData, rationale: value })}
					required
				/>
				<MultiSelect
					label="Unsafe Control Actions"
					values={tupleData.unsafeControlActions}
					options={[]}
					optionsPosition="top"
					onChange={(options: SelectOption[]) =>
						setTupleData({ ...tupleData, unsafeControlActions: options })
					}
				/>
			</ModalInputs>
			<ModalButtons>
				<Button variant="dark" onClick={onClose} size="small" icon={ReturnIcon}>
					Cancel
				</Button>
				<Button
					onClick={handleSubmitFourTuple}
					isLoading={isLoading}
					size="small"
					icon={CheckIcon}
				>
					Create
				</Button>
			</ModalButtons>
		</ModalContainer>
	);
}

export default ModalFourTuple;

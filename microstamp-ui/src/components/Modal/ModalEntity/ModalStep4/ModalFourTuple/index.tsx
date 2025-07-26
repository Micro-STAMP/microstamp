import Button from "@components/Button";
import { Input } from "@components/FormField";
import UCAsMultiSelect from "@components/FormField/MultiSelect/UCAsMultiSelect";
import { SelectOption } from "@components/FormField/Templates";
import {
	ModalButtons,
	ModalContainer,
	ModalHeader,
	ModalInputs,
	ModalProps
} from "@components/Modal/Templates";
import { IFourTupleFormData, IFourTupleReadDto } from "@interfaces/IStep4";
import { useEffect, useState } from "react";
import { BiCheckDouble as CheckIcon, BiUndo as ReturnIcon } from "react-icons/bi";
import { toast } from "sonner";

interface ModalFourTupleProps extends ModalProps {
	title: string;
	onSubmit: (tuple: IFourTupleFormData) => Promise<void>;
	fourTuple?: IFourTupleReadDto;
	analysisId: string;
	isLoading?: boolean;
	btnText?: string;
}
function ModalFourTuple({
	analysisId,
	title,
	fourTuple,
	open,
	onClose,
	onSubmit,
	isLoading = false,
	btnText = "Confirm"
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
	const [tupleData, setTupleData] = useState<IFourTupleFormData>({
		analysisId: analysisId,
		associatedCausalFactor: fourTuple?.associatedCausalFactor || "",
		rationale: fourTuple?.rationale || "",
		recommendation: fourTuple?.recommendation || "",
		scenario: fourTuple?.scenario || "",
		code: fourTuple?.code || "",
		unsafeControlActions: []
	});

	useEffect(() => {
		if (fourTuple) {
			setTupleData({
				analysisId: analysisId,
				associatedCausalFactor: fourTuple.associatedCausalFactor,
				rationale: fourTuple.rationale,
				recommendation: fourTuple.recommendation,
				scenario: fourTuple.scenario,
				code: fourTuple.code,
				unsafeControlActions: []
			});
		} else {
			setTupleData(tupleInitialData);
		}
	}, [fourTuple]);

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
		<ModalContainer open={open} size="large">
			<ModalHeader onClose={onClose} title={title} />
			<ModalInputs column="single">
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
				<UCAsMultiSelect
					analysisId={analysisId}
					ucas={tupleData.unsafeControlActions}
					onChange={(ucas: SelectOption[]) =>
						setTupleData({ ...tupleData, unsafeControlActions: ucas })
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
					{btnText}
				</Button>
			</ModalButtons>
		</ModalContainer>
	);
}

export default ModalFourTuple;

import Button from "@components/Button";
import { SelectOption } from "@components/FormField/Templates";
import { OptionsList } from "@components/Modal/ModalSelectOptions/Template";
import { ModalButtons, ModalContainer, ModalHeader, ModalProps } from "@components/Modal/Templates";
import { BiX } from "react-icons/bi";

interface ModalSelectRefinedScenariosProps extends ModalProps {
	refinedScenarios: SelectOption[];
	onChange: (refinedScenarios: SelectOption[]) => void;
	options: SelectOption[];
	multiple?: boolean;
}
function ModalSelectRefinedScenarios({
	open,
	onClose,
	refinedScenarios,
	onChange,
	options,
	multiple = false
}: ModalSelectRefinedScenariosProps) {
	return (
		<>
			<ModalContainer open={open} size="big">
				<ModalHeader
					title={
						multiple ? "Select the Refined Scenarios" : "Select the Refined Scenario"
					}
					onClose={onClose}
				/>
				<OptionsList
					options={options}
					selectedOptions={refinedScenarios}
					onChange={onChange}
					multiple={multiple}
					onSelect={onClose}
				/>
				<ModalButtons>
					<Button variant="dark" onClick={onClose} size="small" icon={BiX}>
						Close
					</Button>
				</ModalButtons>
			</ModalContainer>
		</>
	);
}

export default ModalSelectRefinedScenarios;

import Button from "@components/Button";
import { SelectOption } from "@components/FormField/Templates";
import { OptionsList } from "@components/Modal/ModalSelectOptions/Template";
import { ModalButtons, ModalContainer, ModalHeader, ModalProps } from "@components/Modal/Templates";
import { BiX } from "react-icons/bi";

interface ModalSelectControlActionProps extends ModalProps {
	controlActions: SelectOption[];
	onChange: (controlActions: SelectOption[]) => void;
	options: SelectOption[];
	multiple?: boolean;
}
function ModalSelectControlAction({
	open,
	onClose,
	controlActions,
	onChange,
	options,
	multiple = false
}: ModalSelectControlActionProps) {
	return (
		<>
			<ModalContainer open={open} size="big">
				<ModalHeader
					title={multiple ? "Select the Control Actions" : "Select the Control Action"}
					onClose={onClose}
				/>
				<OptionsList
					options={options}
					selectedOptions={controlActions}
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

export default ModalSelectControlAction;

import Button from "@components/Button";
import { SelectOption } from "@components/FormField/Templates";
import { ModalButtons, ModalContainer, ModalHeader, ModalProps } from "@components/Modal/Templates";
import { BiCheck as CheckIcon, BiX as CloseIcon } from "react-icons/bi";
import styles from "./ModalSelectOptions.module.css";

interface ModalSelectOptionsProps extends ModalProps {
	options: SelectOption[];
	title: string;
	onChange: (value: SelectOption[]) => void;
	selectedOptions: SelectOption[];
	multiple?: boolean;
}
function ModalSelectOptions({
	title,
	options,
	selectedOptions,
	onChange,
	multiple = false,
	open,
	onClose
}: ModalSelectOptionsProps) {
	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Select Option

	const handleSelectOption = (option: SelectOption) => {
		if (multiple) {
			const isSelected = selectedOptions.some(selected => selected.value === option.value);
			if (isSelected) {
				onChange(selectedOptions.filter(selected => selected.value !== option.value));
			} else {
				onChange([...selectedOptions, option]);
			}
		} else {
			onChange([option]);
			onClose();
		}
	};

	/* - - - - - - - - - - - - - - - - - - - - - - */

	return (
		<>
			<ModalContainer open={open} size="big">
				<ModalHeader title={title} onClose={onClose} />
				<div className={styles.select_options}>
					{options.map(option => {
						const isSelected = selectedOptions.some(
							selected => selected.value === option.value
						);
						return (
							<div
								key={option.value}
								className={`${styles.option_item} ${
									isSelected ? styles.selected : ""
								}`}
								onClick={() => handleSelectOption(option)}
							>
								<div className={styles.option_label}>{option.label}</div>
								{isSelected && <CheckIcon className={styles.checkmark} />}
							</div>
						);
					})}
				</div>
				<ModalButtons>
					<Button variant="dark" onClick={onClose} size="small" icon={CloseIcon}>
						Close
					</Button>
				</ModalButtons>
			</ModalContainer>
		</>
	);
}

export default ModalSelectOptions;

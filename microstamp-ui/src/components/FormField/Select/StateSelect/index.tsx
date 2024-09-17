import { Select } from "@components/FormField";
import { SelectOption } from "@components/FormField/Templates";

interface StateSelectProps {
	label?: string;
	value: SelectOption | null;
	onChange: (value: SelectOption | null) => void;
	states: SelectOption[];
	disabled?: boolean;
	required?: boolean;
	optionsPosition?: "top" | "bottom";
}
function StateSelect({
	label = "State",
	value,
	states,
	onChange,
	disabled = false,
	required = false,
	optionsPosition = "top"
}: StateSelectProps) {
	return (
		<>
			<Select
				label={label}
				value={value}
				onChange={onChange}
				options={states}
				required={required}
				disabled={disabled}
				optionsPosition={optionsPosition}
			/>
		</>
	);
}

export default StateSelect;

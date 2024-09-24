import { SelectOption } from "@components/FormField/Templates";
import ContextSelect from "./ContextSelect";

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
			<ContextSelect
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

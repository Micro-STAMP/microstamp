import { Select } from "@components/FormField";
import { SelectOption } from "@components/FormField/Templates";
import { ucaTypeSelectOptions } from "@interfaces/IStep3/IUnsafeControlAction/Enums";

interface TypeSelectProps {
	type: SelectOption | null;
	onChange: (value: SelectOption | null) => void;
	disabled?: boolean;
	optionsPosition?: "top" | "bottom";
}
function TypeSelect({ type, onChange, disabled, optionsPosition = "top" }: TypeSelectProps) {
	return (
		<>
			<Select
				label="Type"
				value={type}
				onChange={onChange}
				options={ucaTypeSelectOptions}
				disabled={disabled}
				required
				optionsPosition={optionsPosition}
			/>
		</>
	);
}

export default TypeSelect;

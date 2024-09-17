import { MultiSelect } from "@components/FormField";
import { SelectOption } from "@components/FormField/Templates";
import { ucaTypeSelectOptions } from "@interfaces/IStep3/IUnsafeControlAction/Enums";

interface TypesMultiSelectProps {
	types: SelectOption[];
	onChange: (types: SelectOption[]) => void;
	optionsPosition?: "top" | "bottom";
	disabled?: boolean;
}
function TypesMultiSelect({
	types,
	onChange,
	disabled,
	optionsPosition = "top"
}: TypesMultiSelectProps) {
	return (
		<>
			<MultiSelect
				label={"Types"}
				values={types}
				onChange={onChange}
				options={ucaTypeSelectOptions}
				disabled={disabled}
				optionsPosition={optionsPosition}
			/>
		</>
	);
}

export default TypesMultiSelect;

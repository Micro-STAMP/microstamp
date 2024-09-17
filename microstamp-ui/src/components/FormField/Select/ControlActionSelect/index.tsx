import { Select } from "@components/FormField";
import { SelectOption } from "@components/FormField/Templates";

interface ControlActionSelectProps {
	value: SelectOption | null;
	onChange: (value: SelectOption | null) => void;
	controlActions: SelectOption[];
	disabled?: boolean;
}
function ControlActionSelect({
	value,
	controlActions,
	onChange,
	disabled = false
}: ControlActionSelectProps) {
	return (
		<>
			<Select
				label="Control Action"
				value={value}
				onChange={onChange}
				options={controlActions}
				required={true}
				disabled={disabled}
				optionsPosition="top"
			/>
		</>
	);
}

export default ControlActionSelect;

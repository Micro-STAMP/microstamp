import { MultiSelect } from "@components/FormField";
import { SelectOption } from "@components/FormField/Templates";
import { getUnsafeControlActionsByAnalysis } from "@http/Step3/UnsafeControlActions";
import { truncateText } from "@services/text";
import { useQuery } from "@tanstack/react-query";
import { useEffect, useState } from "react";
import { ucasToSelectOptions } from "./util";

interface UCAsSelectProps {
	analysisId: string;
	ucas: SelectOption[];
	onChange: (ucas: SelectOption[]) => void;
	disabled?: boolean;
}
function UCAsMultiSelect({ analysisId, ucas, onChange, disabled }: UCAsSelectProps) {
	const [ucasOptions, setucasOptions] = useState<SelectOption[]>([]);

	const {
		data: ucasList,
		isLoading,
		isError
	} = useQuery({
		queryKey: ["ucas-multi-select", analysisId],
		queryFn: () => getUnsafeControlActionsByAnalysis(analysisId)
	});

	useEffect(() => {
		if (ucasList) {
			setucasOptions(ucasToSelectOptions(ucasList));
		}
	}, [ucasList]);

	return (
		<>
			<MultiSelect
				label={isLoading ? "Loading" : isError ? "Error" : "Unsafe Control Actions"}
				values={ucas.map(uca => ({ label: truncateText(uca.label, 30), value: uca.value }))}
				options={ucasOptions}
				disabled={isLoading || isError || disabled}
				optionsPosition="top"
				onChange={onChange}
			/>
		</>
	);
}

export default UCAsMultiSelect;

import { MultiSelect } from "@components/FormField";
import { SelectOption } from "@components/FormField/Templates";
import { getHazards } from "@http/Step1/Hazards";
import { useQuery } from "@tanstack/react-query";
import { useEffect, useState } from "react";
import { hazardsToSelectOptions } from "./util";

interface HazardsSelectProps {
	analysisId: string;
	hazards: SelectOption[];
	onChange: (hazards: SelectOption[]) => void;
	disabled?: boolean;
}
function HazardsMultiSelect({ analysisId, hazards, onChange, disabled }: HazardsSelectProps) {
	const [hazardsOptions, sethazardsOptions] = useState<SelectOption[]>([]);

	const {
		data: hazardsList,
		isLoading,
		isError
	} = useQuery({
		queryKey: ["hazards-multi-select", analysisId],
		queryFn: () => getHazards(analysisId)
	});

	useEffect(() => {
		if (hazardsList) {
			sethazardsOptions(hazardsToSelectOptions(hazardsList));
		}
	}, [hazardsList]);

	return (
		<>
			<MultiSelect
				label={isLoading ? "Loading" : isError ? "Error" : "Hazards"}
				values={hazards}
				options={hazardsOptions}
				disabled={isLoading || isError || disabled}
				optionsPosition="top"
				onChange={onChange}
			/>
		</>
	);
}

export default HazardsMultiSelect;

import { MultiSelect } from "@components/FormField";
import { SelectOption } from "@components/FormField/Templates";
import { getLosses } from "@http/Step1/Losses";
import { useQuery } from "@tanstack/react-query";
import { useEffect, useState } from "react";
import { lossesToSelectOptions } from "./util";

interface LossesSelectProps {
	analysisId: string;
	losses: SelectOption[];
	onChange: (losses: SelectOption[]) => void;
	disabled?: boolean;
}
function LossesMultiSelect({ analysisId, losses, onChange, disabled }: LossesSelectProps) {
	const [lossesOptions, setlossesOptions] = useState<SelectOption[]>([]);

	const {
		data: lossesList,
		isLoading,
		isError
	} = useQuery({
		queryKey: ["losses-multi-select", analysisId],
		queryFn: () => getLosses(analysisId)
	});

	useEffect(() => {
		if (lossesList) {
			setlossesOptions(lossesToSelectOptions(lossesList));
		}
	}, [lossesList]);

	return (
		<>
			<MultiSelect
				label={isLoading ? "Loading" : isError ? "Error" : "Losses"}
				values={losses}
				options={lossesOptions}
				disabled={isLoading || isError || disabled}
				optionsPosition="top"
				onChange={onChange}
			/>
		</>
	);
}

export default LossesMultiSelect;

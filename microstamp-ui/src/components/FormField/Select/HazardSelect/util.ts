import { SelectOption } from "@components/FormField/Templates";
import { IHazardReadDto } from "@interfaces/IStep1";
import { truncateText } from "@services/text";

const hazardToSelectOption = (hazard: IHazardReadDto): SelectOption => {
	return {
		label: `${hazard.code}: ${truncateText(hazard.name, 30)}`,
		value: hazard.id
	};
};
const hazardsToSelectOptions = (hazards: IHazardReadDto[]): SelectOption[] => {
	return hazards.map(hazard => hazardToSelectOption(hazard));
};

export { hazardsToSelectOptions, hazardToSelectOption };

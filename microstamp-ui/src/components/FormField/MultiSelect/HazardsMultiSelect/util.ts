import { SelectOption } from "@components/FormField/Templates";
import { IHazardReadDto } from "@interfaces/IStep1";
import { truncateText } from "@services/text";

const hazardsToSelectOptions = (hazards: IHazardReadDto[]): SelectOption[] => {
	return hazards.map(hazard => ({
		label: `${hazard.code}: ${truncateText(hazard.name, 30)}`,
		value: hazard.id
	}));
};

export { hazardsToSelectOptions };

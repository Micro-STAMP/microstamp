import { SelectOption } from "@components/FormField/Templates";

/* - - - - - - - - - - - - - - - - - - - - - - */

// Component Border Enum

enum IComponentBorder {
	DASHED = "DASHED",
	SOLID = "SOLID",
	ETCHED = "ETCHED"
}

export { IComponentBorder };

/* - - - - - - - - - - - - - - - - - - - - - - */

// Component Border Select Options

const borderSelectOptions: SelectOption[] = [
	{ label: "Solid", value: IComponentBorder.SOLID },
	{ label: "Dashed", value: IComponentBorder.DASHED },
	{ label: "Etched", value: IComponentBorder.ETCHED }
];

export { borderSelectOptions };

/* - - - - - - - - - - - - - - - - - - - - - - */

// Border to Select Option

const borderToSelectOptionMap: Record<IComponentBorder, SelectOption> = {
	[IComponentBorder.SOLID]: borderSelectOptions[0],
	[IComponentBorder.DASHED]: borderSelectOptions[1],
	[IComponentBorder.ETCHED]: borderSelectOptions[2]
};
const borderToSelectOption = (border: IComponentBorder): SelectOption => {
	return borderToSelectOptionMap[border];
};

export { borderToSelectOption };

/* - - - - - - - - - - - - - - - - - - - - - - */

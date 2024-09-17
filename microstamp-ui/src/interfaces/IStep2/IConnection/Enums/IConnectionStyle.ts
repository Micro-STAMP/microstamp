import { SelectOption } from "@components/FormField/Templates";

/* - - - - - - - - - - - - - - - - - - - - - - */

// Connection Style Enum

enum IConnectionStyle {
	DASHED = "DASHED",
	SOLID = "SOLID",
	ETCHED = "ETCHED"
}

export { IConnectionStyle };

/* - - - - - - - - - - - - - - - - - - - - - - */

// Connection Style Select Options

const connectionStyleSelectOptions: SelectOption[] = [
	{ label: "Solid", value: IConnectionStyle.SOLID },
	{ label: "Dashed", value: IConnectionStyle.DASHED },
	{ label: "Etched", value: IConnectionStyle.ETCHED }
];

export { connectionStyleSelectOptions };

/* - - - - - - - - - - - - - - - - - - - - - - */

// Connection Style to Select Option

const connectionStyleToSelectOptionMap: Record<IConnectionStyle, SelectOption> = {
	[IConnectionStyle.SOLID]: connectionStyleSelectOptions[0],
	[IConnectionStyle.DASHED]: connectionStyleSelectOptions[1],
	[IConnectionStyle.ETCHED]: connectionStyleSelectOptions[2]
};
const connectionStyleToSelectOption = (style: IConnectionStyle): SelectOption => {
	return connectionStyleToSelectOptionMap[style];
};

export { connectionStyleToSelectOption };

/* - - - - - - - - - - - - - - - - - - - - - - */

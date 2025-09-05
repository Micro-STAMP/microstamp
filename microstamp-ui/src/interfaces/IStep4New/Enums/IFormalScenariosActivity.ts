enum IFormalScenariosActivity {
	HIGH_LEVEL_SCENARIOS = "HIGH_LEVEL_SCENARIOS",
	HIGH_LEVEL_SOLUTIONS = "HIGH_LEVEL_SOLUTIONS",
	REFINED_SCENARIOS = "REFINED_SCENARIOS",
	REFINED_SOLUTIONS = "REFINED_SOLUTIONS"
}

const getFormalActivityTitle = (activity: IFormalScenariosActivity): string => {
	switch (activity) {
		case IFormalScenariosActivity.HIGH_LEVEL_SCENARIOS:
			return "4.1 Identify High-Level Scenarios";
		case IFormalScenariosActivity.HIGH_LEVEL_SOLUTIONS:
			return "4.2 Identify High-Level Solutions";
		case IFormalScenariosActivity.REFINED_SCENARIOS:
			return "4.3 Identify Refined Scenarios";
		case IFormalScenariosActivity.REFINED_SOLUTIONS:
			return "4.4 Identify Refined Solutions";
	}
};

export { getFormalActivityTitle, IFormalScenariosActivity };

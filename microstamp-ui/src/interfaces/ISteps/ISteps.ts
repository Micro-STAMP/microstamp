enum ISteps {
	STEP_1 = "STEP_1",
	STEP_2 = "STEP_2",
	STEP_3 = "STEP_3",
	STEP_4 = "STEP_4"
}
export { ISteps };

const getStepLabel = (step: ISteps) => {
	switch (step) {
		case ISteps.STEP_1:
			return "Define the Purpose of the Analysis";
		case ISteps.STEP_2:
			return "Model the Control Structure";
		case ISteps.STEP_3:
			return "Identify Unsafe Control Actions";
		case ISteps.STEP_4:
			return "Identify Loss Scenarios";
	}
};
export { getStepLabel };

const getStepFullLabel = (step: ISteps) => {
	switch (step) {
		case ISteps.STEP_1:
			return "Step 1: Define Purpose of the Analysis";
		case ISteps.STEP_2:
			return "Step 2: Model the Control Structure";
		case ISteps.STEP_3:
			return "Step 3: Identify Unsafe Control Actions";
		case ISteps.STEP_4:
			return "Step 4: Identify Loss Scenarios";
	}
};
export { getStepFullLabel };

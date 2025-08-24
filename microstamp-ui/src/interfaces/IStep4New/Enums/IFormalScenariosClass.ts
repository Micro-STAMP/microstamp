enum IFormalScenariosClass {
	CLASS_1 = "CLASS_1",
	CLASS_2 = "CLASS_2",
	CLASS_3 = "CLASS_3",
	CLASS_4 = "CLASS_4"
}

const getFormalClassTitle = (classEnum: IFormalScenariosClass) => {
	switch (classEnum) {
		case IFormalScenariosClass.CLASS_1:
			return "Class 1";
		case IFormalScenariosClass.CLASS_2:
			return "Class 2";
		case IFormalScenariosClass.CLASS_3:
			return "Class 3";
		case IFormalScenariosClass.CLASS_4:
			return "Class 4";
	}
};
const getFormalClassKey = (classEnum: IFormalScenariosClass) => {
	switch (classEnum) {
		case IFormalScenariosClass.CLASS_1:
			return "class1";
		case IFormalScenariosClass.CLASS_2:
			return "class2";
		case IFormalScenariosClass.CLASS_3:
			return "class3";
		case IFormalScenariosClass.CLASS_4:
			return "class4";
	}
};

export { getFormalClassKey, getFormalClassTitle, IFormalScenariosClass };

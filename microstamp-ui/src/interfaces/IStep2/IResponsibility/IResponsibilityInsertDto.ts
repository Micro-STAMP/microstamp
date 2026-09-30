interface IResponsibilityInsertDto {
	responsibility: string;
	code: string;
	componentId: string;
	systemSafetyConstraintId?: string;
	violatedSystemSafetyConstraintIds?: string[];
}

export type { IResponsibilityInsertDto };

interface IResponsibilityUpdateDto {
	responsibility: string;
	code: string;
	systemSafetyConstraintId?: string;
	violatedSystemSafetyConstraintIds?: string[];
}

export type { IResponsibilityUpdateDto };

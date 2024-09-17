import { ISystemSafetyConstraintReadDto } from "@interfaces/IStep1";

interface IResponsibilityReadDto {
	id: string;
	responsibility: string;
	code: string;
	systemSafetyConstraint: ISystemSafetyConstraintReadDto;
}

export type { IResponsibilityReadDto };

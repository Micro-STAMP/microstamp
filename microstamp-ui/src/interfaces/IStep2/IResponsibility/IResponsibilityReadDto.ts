import { ISystemSafetyConstraintReadDto } from "@interfaces/IStep1";
import { IViolatedSystemSafetyConstraintReadDto } from "@interfaces/CAST/IViolatedSystemSafetyConstraint";

interface IResponsibilityReadDto {
	id: string;
	responsibility: string;
	code: string;
	systemSafetyConstraint?: ISystemSafetyConstraintReadDto;
	violatedSystemSafetyConstraints?: IViolatedSystemSafetyConstraintReadDto[];
}

export type { IResponsibilityReadDto };

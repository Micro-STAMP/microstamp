import { IHazardReadDto } from "@interfaces/IStep1";

interface ISystemSafetyConstraintReadDto {
	id: string;
	name: string;
	code: string;
	hazards: IHazardReadDto[];
}

export type { ISystemSafetyConstraintReadDto };

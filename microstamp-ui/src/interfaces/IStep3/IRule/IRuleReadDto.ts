import { IHazardReadDto } from "@interfaces/IStep1";
import { IStateReadDto } from "@interfaces/IStep2";
import { IUCAType } from "@interfaces/IStep3";

interface IRuleReadDto {
	id: string;
	name: string;
	code: string;
	control_action_name: string;
	states: IStateReadDto[];
	types: IUCAType[];
	hazard: IHazardReadDto;
}

export type { IRuleReadDto };

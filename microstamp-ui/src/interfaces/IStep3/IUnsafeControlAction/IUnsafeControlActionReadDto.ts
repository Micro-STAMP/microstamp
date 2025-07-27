import { IStateReadDto } from "@interfaces/IStep2";
import { IUCAType } from "./Enums";

interface IUnsafeControlActionReadDto {
	id: string;
	name: string;
	analysis_id: string;
	hazard_code: string;
	uca_code: string;
	rule_code: string;
	states: IStateReadDto[];
	type: IUCAType;
}

export type { IUnsafeControlActionReadDto };

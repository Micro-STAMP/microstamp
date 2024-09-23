import { IUCAType } from "@interfaces/IStep3";

interface IRuleInsertDto {
	name: string;
	code: string;
	analysis_id: string;
	control_action_id: string;
	states_ids: string[];
	types: IUCAType[];
	hazard_id: string;
}

export type { IRuleInsertDto };

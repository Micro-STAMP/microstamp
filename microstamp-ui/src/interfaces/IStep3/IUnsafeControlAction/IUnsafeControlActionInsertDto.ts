import { IUCAType } from "./Enums";

interface IUnsafeControlActionInsertDto {
	control_action_id: string;
	states_ids: string[];
	hazard_id: string;
	type: IUCAType;
	analysis_id: string;
	rule_code?: string;
}

export type { IUnsafeControlActionInsertDto };

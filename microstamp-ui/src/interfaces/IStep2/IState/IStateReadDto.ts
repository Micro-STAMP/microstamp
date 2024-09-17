interface IStateReadDto {
	id: string;
	name: string;
	code: string;
	variable: {
		id: string;
		name: string;
		code: string;
	};
}

export type { IStateReadDto };

interface IToken {
	access_token: string;
	refresh_token: string;
	scope: string;
	id_token: string;
	token_type: string;
	expires_in: number;
}

export type { IToken };

interface ITokenPayload {
	sub: string;
	aud: string;
	nbf: number;
	scope: string[];
	iss: string;
	exp: number;
	iat: number;
	userId: string;
	jti: string;
}

export type { ITokenPayload };

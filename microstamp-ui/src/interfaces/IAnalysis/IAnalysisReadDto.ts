import { IImageReadDto } from "@interfaces/IImage";

interface IAnalysisReadDto {
	id: string;
	name: string;
	description: string;
	creationDate: string;
	userId: string;
	image: IImageReadDto | null;
}

export type { IAnalysisReadDto };

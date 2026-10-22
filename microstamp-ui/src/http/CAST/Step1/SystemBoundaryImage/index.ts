import { http } from "@http/AxiosConfig";
import { IImageReadDto } from "@interfaces/IImage";

const IMAGES_ENDPOINT = "cast-step1/cast/system-boundary-images";

const getSystemBoundaryImages = async (analysisId: string) => {
	try {
		const res = await http.get<IImageReadDto[]>(`${IMAGES_ENDPOINT}/analysis/${analysisId}`);
		return res.data;
	} catch (err) {
		console.error(err);
		throw err;
	}
};

const saveSystemBoundaryImage = async (image: File, analysisId: string) => {
	const data = new FormData();
	data.append("file", image);
	try {
		const res = await http.request({
			url: `${IMAGES_ENDPOINT}/analysis/${analysisId}`,
			method: "POST",
			headers: {
				"Content-Type": "multipart/form-data"
			},
			data: data
		});
		return res.data;
	} catch (err) {
		console.error(err);
		throw new Error("Error saving image.");
	}
};

const deleteSystemBoundaryImage = async (id: string) => {
	try {
		await http.delete(`${IMAGES_ENDPOINT}/${id}`);
	} catch (err) {
		console.error(err);
		throw new Error("Error deleting image.");
	}
};

export { deleteSystemBoundaryImage, getSystemBoundaryImages, saveSystemBoundaryImage };

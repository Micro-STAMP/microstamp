import { http } from "@http/AxiosConfig";
import { IImageReadDto } from "@interfaces/IImage";

/* - - - - - - - - - - - - - - - - - - - - - - */

const IMAGES_ENDPOINT = "step2/images";

const getControlStructureImages = async (analysisId: string) => {
	try {
		const res = await http.get<IImageReadDto[]>(`${IMAGES_ENDPOINT}/analysis/${analysisId}`);
		return res.data;
	} catch (err) {
		console.error(err);
		throw err;
	}
};
const saveControlStructureImage = async (image: File, analysisId: string) => {
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
const deleteControlStructureImage = async (id: string) => {
	try {
		await http.delete(`${IMAGES_ENDPOINT}/${id}`);
	} catch (err) {
		console.error(err);
		throw new Error("Error deleting image.");
	}
};

export { deleteControlStructureImage, getControlStructureImages, saveControlStructureImage };

/* - - - - - - - - - - - - - - - - - - - - - - */

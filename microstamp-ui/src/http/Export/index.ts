import { http } from "@http/AxiosConfig";

/* - - - - - - - - - - - - - - - - - - - - - - */

// Get Analysis PDF

const ANALYSIS_EXPORT_ENDPOINT = "auth/export/analysis";

const getAnalysisPDF = async (analysisId: string) => {
	try {
		const res = await http.get<Blob>(`${ANALYSIS_EXPORT_ENDPOINT}/${analysisId}/pdf`, {
			headers: {
				Accept: "application/pdf"
			},
			responseType: "blob"
		});
		return res.data;
	} catch (err) {
		console.error("Error fetching PDF:", err);
		throw new Error("Failed to fetch analysis PDF.");
	}
};

export { getAnalysisPDF };

/* - - - - - - - - - - - - - - - - - - - - - - */

// Get Step 1 PDF

const STEP1_EXPORT_ENDPOINT = "step1/export/analysis";

const getStep1PDF = async (analysisId: string) => {
	try {
		const res = await http.get<Blob>(`${STEP1_EXPORT_ENDPOINT}/${analysisId}/pdf`, {
			headers: {
				Accept: "application/pdf"
			},
			responseType: "blob"
		});
		return res.data;
	} catch (err) {
		console.error("Error fetching Step 1 PDF:", err);
		throw new Error("Failed to fetch Step 1 PDF.");
	}
};

export { getStep1PDF };

/* - - - - - - - - - - - - - - - - - - - - - - */

// Get Step 2 PDF

const STEP2_EXPORT_ENDPOINT = "step2/export/analysis";

const getStep2PDF = async (analysisId: string) => {
	try {
		const res = await http.get<Blob>(`${STEP2_EXPORT_ENDPOINT}/${analysisId}/pdf`, {
			headers: {
				Accept: "application/pdf"
			},
			responseType: "blob"
		});
		return res.data;
	} catch (err) {
		console.error("Error fetching Step 2 PDF:", err);
		throw new Error("Failed to fetch Step 2 PDF.");
	}
};

export { getStep2PDF };

/* - - - - - - - - - - - - - - - - - - - - - - */

// Get Step 3 PDF

const STEP3_EXPORT_ENDPOINT = "step3/export/analysis";

const getStep3PDF = async (analysisId: string) => {
	try {
		const res = await http.get<Blob>(`${STEP3_EXPORT_ENDPOINT}/${analysisId}/pdf`, {
			headers: {
				Accept: "application/pdf"
			},
			responseType: "blob"
		});
		return res.data;
	} catch (err) {
		console.error("Error fetching Step 3 PDF:", err);
		throw new Error("Failed to fetch Step 3 PDF.");
	}
};

export { getStep3PDF };

/* - - - - - - - - - - - - - - - - - - - - - - */

// Get Step 4 PDF

const STEP4_EXPORT_ENDPOINT = "step4/export/analysis";

const getStep4PDF = async (analysisId: string) => {
	try {
		const res = await http.get<Blob>(`${STEP4_EXPORT_ENDPOINT}/${analysisId}/pdf`, {
			headers: {
				Accept: "application/pdf"
			},
			responseType: "blob"
		});
		return res.data;
	} catch (err) {
		console.error("Error fetching Step 4 PDF:", err);
		throw new Error("Failed to fetch Step 4 PDF.");
	}
};

export { getStep4PDF };

/* - - - - - - - - - - - - - - - - - - - - - - */

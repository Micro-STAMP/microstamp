import { http } from "@http/AxiosConfig";
import { IAuditLogEntryReadDto } from "@interfaces/CAST/IAuditLog";

const ENDPOINTS = [
	"cast-step1/cast/audit-log",
	"step2/audit-log",
	"cast-step3/cast/audit-log",
	"cast-step4/cast/audit-log",
	"cast-step5/cast/audit-log"
];

const getAggregatedAuditLog = async (analysisId: string, limitPerService = 10) => {
	const results = await Promise.allSettled(
		ENDPOINTS.map(endpoint =>
			http.get<IAuditLogEntryReadDto[]>(`${endpoint}/analysis/${analysisId}`, {
				params: { limit: limitPerService }
			})
		)
	);

	const entries = results.flatMap(result =>
		result.status === "fulfilled" ? result.value.data : []
	);

	return entries.sort(
		(a, b) => new Date(b.timestamp).getTime() - new Date(a.timestamp).getTime()
	);
};

export { getAggregatedAuditLog };

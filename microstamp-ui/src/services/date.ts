/* - - - - - - - - - - - - - - - - - - - - - - */

// Format Date to mm/dd/yyyy

function formatDate(isoDate: string): string {
	const date = new Date(isoDate);
	const month = (date.getUTCMonth() + 1).toString().padStart(2, "0"); // Mês começa em 0, por isso adicionamos 1
	const day = date.getUTCDate().toString().padStart(2, "0");
	const year = date.getUTCFullYear();
	return `${month}/${day}/${year}`;
}

/* - - - - - - - - - - - - - - - - - - - - - - */

// Format Date to dd/mm/yyyy

function formatDateBR(isoDate: string): string {
	const date = new Date(isoDate);
	const day = date.getUTCDate().toString().padStart(2, "0");
	const month = (date.getUTCMonth() + 1).toString().padStart(2, "0");
	const year = date.getUTCFullYear();
	return `${day}/${month}/${year}`;
}

// Format Date to dd/mm/yyyy HH:mm

function formatDateTimeBR(isoDate: string): string {
	const date = new Date(isoDate);
	const hours = date.getUTCHours().toString().padStart(2, "0");
	const minutes = date.getUTCMinutes().toString().padStart(2, "0");
	return `${formatDateBR(isoDate)} ${hours}:${minutes}`;
}

export { formatDate, formatDateBR, formatDateTimeBR };

/* - - - - - - - - - - - - - - - - - - - - - - */

/* - - - - - - - - - - - - - - - - - - - - - - */

// Format Date to mm/dd/yyyy

function formatDate(isoDate: string): string {
	const date = new Date(isoDate);
	const month = (date.getUTCMonth() + 1).toString().padStart(2, "0"); // Mês começa em 0, por isso adicionamos 1
	const day = date.getUTCDate().toString().padStart(2, "0");
	const year = date.getUTCFullYear();
	return `${month}/${day}/${year}`;
}

export { formatDate };

/* - - - - - - - - - - - - - - - - - - - - - - */

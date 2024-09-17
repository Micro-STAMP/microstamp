import { useEffect } from "react";
import { BiSolidLeftArrow as LeftIcon, BiSolidRightArrow as RightIcon } from "react-icons/bi";
import PagesDisplay from "./PageDisplay";
import styles from "./Pagination.module.css";

interface PaginationProps {
	page: number;
	pages: number;
	changePage: (newPage: number) => void;
}
function Pagination({ changePage, page, pages }: PaginationProps) {
	useEffect(() => {
		if (page < 0 || page >= pages) {
			changePage(0);
		}
	}, [page, pages]);

	const nextPage = () => {
		if (page + 1 < pages) changePage(page + 1);
	};
	const previousPage = () => {
		if (page - 1 >= 0) changePage(page - 1);
	};

	return (
		<div className={styles.pagination_container}>
			<button type="button" className={styles.button} onClick={previousPage}>
				<LeftIcon />
			</button>
			<PagesDisplay current={page} totalPages={pages} onPageChange={changePage} />
			<button type="button" className={styles.button} onClick={nextPage}>
				<RightIcon />
			</button>
		</div>
	);
}

export default Pagination;

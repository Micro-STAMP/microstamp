import { IPageTag } from "@components/PageDisplay/IPageTag";
import { BiSolidLeftArrowCircle as GoBackIcon } from "react-icons/bi";
import { useNavigate } from "react-router-dom";
import styles from "./PageDisplay.module.css";

interface PageDisplayProps {
	tags: IPageTag[];
}
function PageDisplay({ tags }: PageDisplayProps) {
	const navigate = useNavigate();

	return (
		<header className={styles.page_display}>
			<div className={styles.tags}>
				{tags.map(tag => (
					<div key={tag.tag}>
						<strong>{tag.tag}: </strong>
						<span>{tag.value}</span>
					</div>
				))}
			</div>
			<button type="button" className={styles.back_button} onClick={() => navigate(-1)}>
				<GoBackIcon className={styles.icon} />
				<span>Go Back</span>
			</button>
		</header>
	);
}

export default PageDisplay;

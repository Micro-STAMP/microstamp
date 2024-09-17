import { IAnalysisReadDto } from "@interfaces/IAnalysis";
import { BiImageAlt as ImageIcon } from "react-icons/bi";
import { Link } from "react-router-dom";
import styles from "./AnalysisCard.module.css";

interface AnalysisCardProps {
	analysis: IAnalysisReadDto;
}
function AnalysisCard({ analysis }: AnalysisCardProps) {
	return (
		<div className={styles.analysis_card}>
			<Link to={`/analyses/${analysis.id}`}>
				<div className={styles.name}>{analysis.name}</div>
				{analysis.image ? (
					<div
						className={styles.image}
						style={{
							backgroundImage: `url(data:image/png;base64,${analysis.image.base64})`
						}}
					></div>
				) : (
					<div className={styles.image_placeholder}>
						<ImageIcon />
					</div>
				)}

				<div className={styles.description}>{analysis.description}</div>
			</Link>
		</div>
	);
}

export default AnalysisCard;

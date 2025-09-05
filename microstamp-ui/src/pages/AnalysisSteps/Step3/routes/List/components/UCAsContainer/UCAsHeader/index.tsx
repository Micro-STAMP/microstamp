import Button from "@components/Button";
import { BiPlusCircle } from "react-icons/bi";
import { useNavigate } from "react-router-dom";
import styles from "./UCAsHeader.module.css";

interface UCAsHeaderProps {
	controlActionName: string;
	ucaCount: number;
}
function UCAsHeader({ controlActionName, ucaCount }: UCAsHeaderProps) {
	const navigate = useNavigate();

	const handleIdentifyClick = () => {
		navigate("identify");
	};

	return (
		<div className={styles.header_container}>
			<div className={styles.header_content}>
				<div className={styles.header_info}>
					<div className={styles.title_section}>
						<span className={styles.title}>Identified Unsafe Control Actions</span>
					</div>
					<div className={styles.count_section}>
						<span className={styles.count}>
							{ucaCount} <em>UCA{ucaCount === 1 ? "" : "s"}</em> identified for{" "}
							<span className={styles.highlight}>{controlActionName}</span>
						</span>
						<span className={styles.note}>
							Associated controller's constraints are generated from the identified
							UCAs.
						</span>
					</div>
				</div>
				<Button
					size="small"
					icon={BiPlusCircle}
					variant="dark"
					onClick={handleIdentifyClick}
				>
					Identify New Unsafe Control Actions
				</Button>
			</div>
		</div>
	);
}

export default UCAsHeader;

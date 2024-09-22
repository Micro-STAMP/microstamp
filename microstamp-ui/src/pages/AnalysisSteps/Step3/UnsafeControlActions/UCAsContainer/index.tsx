import Loader from "@components/Loader";
import { getUnsafeControlActions } from "@http/Step3/UnsafeControlActions";
import { IControlAction } from "@interfaces/IStep2";
import { useQuery } from "@tanstack/react-query";
import UCARow from "./UCARow";
import styles from "./UCAsContainer.module.css";

interface UCAsContainerProps {
	controlAction: IControlAction;
}
function UCAsContainer({ controlAction }: UCAsContainerProps) {
	const {
		data: ucas,
		isLoading,
		isError
	} = useQuery({
		queryKey: ["unsafe-control-actions", controlAction.id],
		queryFn: () => getUnsafeControlActions(controlAction.id)
	});

	if (isLoading) return <Loader />;
	if (isError || !ucas) return <h1>Error</h1>;
	return (
		<div className={styles.ucas_container}>
			<header className={styles.container_header}>
				<span className={styles.column_tag}>Unsafe Control Actions</span>
				<span className={styles.column_tag}>Associated Controller's Constraints</span>
			</header>
			<div className={styles.ucas_list}>
				{ucas.map(uca => (
					<UCARow unsafeControlAction={uca} key={uca.id} />
				))}
			</div>
		</div>
	);
}

export default UCAsContainer;

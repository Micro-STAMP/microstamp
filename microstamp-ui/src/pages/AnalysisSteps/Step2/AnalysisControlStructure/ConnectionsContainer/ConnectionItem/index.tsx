import DualButton from "@components/Button/DualButton";
import IconButton from "@components/Button/IconButton";
import { ListItem as Connection } from "@components/Container/ListItem";
import { ModalConnectionActions } from "@components/Modal";
import { IConnectionReadDto } from "@interfaces/IStep2";
import { useState } from "react";
import { BiInfoCircle as InfoIcon } from "react-icons/bi";
import { FaArrowRightLong as SolidArrowIcon } from "react-icons/fa6";
import styles from "./ConnectionItem.module.css";

interface ConnectionItemProps {
	connection: IConnectionReadDto;
	selectConnection: (connection: IConnectionReadDto) => void;
	modalUpdateConnection: () => void;
	modalDeleteConnection: () => void;
}
function ConnectionItem({
	connection,
	selectConnection,
	modalDeleteConnection,
	modalUpdateConnection
}: ConnectionItemProps) {
	const [modalConnectionActionsOpen, setModalConnectionActionsOpen] = useState(false);
	const toggleModalConnectionActions = () =>
		setModalConnectionActionsOpen(!modalConnectionActionsOpen);

	const connectionName = (
		<span className={styles.connection_name}>
			{connection.source.name} <SolidArrowIcon className={styles.arrow} />{" "}
			{connection.target.name}
		</span>
	);

	return (
		<>
			<Connection.Root key={connection.id}>
				<Connection.Name code={connection.code} name={connectionName} />
				<Connection.Actions>
					<IconButton icon={InfoIcon} onClick={toggleModalConnectionActions} />
					<DualButton
						onEdit={() => {
							selectConnection(connection);
							modalUpdateConnection();
						}}
						onDelete={() => {
							selectConnection(connection);
							modalDeleteConnection();
						}}
					/>
				</Connection.Actions>
			</Connection.Root>
			<ModalConnectionActions
				open={modalConnectionActionsOpen}
				onClose={toggleModalConnectionActions}
				connection={connection}
			/>
		</>
	);
}

export default ConnectionItem;

import Loader from "@components/Loader";
import { ReactNode, useState } from "react";
import {
	BiErrorAlt,
	BiExpand,
	BiHide,
	BiShowAlt,
	BiDotsHorizontalRounded as OptionsIcon,
	BiPlusMedical as PlusIcon
} from "react-icons/bi";
import styles from "./Container.module.css";

interface ContainerProps {
	title: string;
	children: ReactNode;
	justTitle?: boolean;
	onClick?: () => void;
	onOptions?: () => void;
	isLoading?: boolean;
	isError?: boolean;
	collapsible?: boolean;
	defaultCollapsed?: boolean;
}
function Container({
	title,
	justTitle = false,
	onClick,
	onOptions,
	children,
	isLoading = false,
	isError = false,
	collapsible = false,
	defaultCollapsed = false
}: ContainerProps) {
	const [collapsed, setCollapsed] = useState(defaultCollapsed);

	return (
		<div className={styles.container}>
			<header className={styles.header}>
				<div className={styles.header_container}>
					<span className={styles.title}>{title}</span>
					{!justTitle && !isError && !isLoading && (
						<button type="button" className={styles.button} onClick={onClick}>
							<PlusIcon className={styles.icon} />
						</button>
					)}
				</div>
				{!isError && !isLoading && (
					<div className={styles.header_options}>
						{onOptions && (
							<button type="button" className={styles.option} onClick={onOptions}>
								<OptionsIcon />
							</button>
						)}
						{collapsible && (
							<button
								type="button"
								className={styles.option}
								onClick={() => setCollapsed(!collapsed)}
								title={collapsed ? "Expand content" : "Hide content"}
							>
								{collapsed ? <BiShowAlt /> : <BiHide />}
							</button>
						)}
					</div>
				)}
			</header>
			<div className={styles.content}>
				{isLoading ? (
					<Loader />
				) : isError ? (
					<div className={styles.error}>
						<BiErrorAlt />
						<span>An unexpected error occurred. Try refreshing the page.</span>
					</div>
				) : collapsible && collapsed ? (
					<div
						className={styles.collapsed}
						onClick={() => setCollapsed(!collapsed)}
						title={"Expand content"}
					>
						<span>Content hidden. Click to expand.</span>
						<BiExpand />
					</div>
				) : (
					children
				)}
			</div>
		</div>
	);
}

export default Container;

import { ReactNode, useState } from "react";
import {
	BiErrorAlt,
	BiDotsHorizontalRounded as OptionsIcon,
	BiPlusMedical as PlusIcon
} from "react-icons/bi";
import { MdOutlineExpandCircleDown as ChevronDown } from "react-icons/md";
import Loader from "../Loader";
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
								<OptionsIcon className={styles.icon} />
							</button>
						)}
						{collapsible && (
							<button
								type="button"
								className={styles.option}
								onClick={() => setCollapsed(!collapsed)}
							>
								{collapsed ? (
									<ChevronDown className={styles.icon} />
								) : (
									<ChevronDown
										style={{
											transform: "rotate(180deg)"
										}}
										className={styles.icon}
									/>
								)}
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
					<div className={styles.collapsed} onClick={() => setCollapsed(!collapsed)}>
						<div className={styles.collapsed_dots}>
							<span></span>
							<span></span>
							<span></span>
						</div>
					</div>
				) : (
					children
				)}
			</div>
		</div>
	);
}

export default Container;

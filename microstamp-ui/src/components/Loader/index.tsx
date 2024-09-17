import styles from "./Loader.module.css";

function Loader() {
	return (
		<div className={styles.loader_wrapper}>
			<div className={styles.spinner}></div>
			<span className={styles.message}>Loading</span>
		</div>
	);
}

export default Loader;

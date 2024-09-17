import { Toaster as ToastComponent } from "sonner";
import styles from "./Toaster.module.css";

function Toaster() {
	return (
		<ToastComponent
			theme="dark"
			position="top-center"
			toastOptions={{
				duration: 4 * 1000,
				className: styles.toast
			}}
			richColors
		/>
	);
}

export default Toaster;

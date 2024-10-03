import { ModalConfirm } from "@components/Modal";
import { useAuth } from "@hooks/useAuth";
import { authRegisterRequest, authServerRequest } from "@http/Auth";
import { useEffect, useState } from "react";
import { AiOutlineSecurityScan as LogoIcon } from "react-icons/ai";
import {
	BiAnalyse as AnalysesIcon,
	BiX as CloseIcon,
	BiLogInCircle as LoginIcon,
	BiMenu as MenuIcon,
	BiUserPlus as RegisterIcon,
	BiUserCircle as UserIcon
} from "react-icons/bi";
import { Link, useNavigate } from "react-router-dom";
import styles from "./Header.module.css";

function Header() {
	const { user, authenticated, deauthenticateUser } = useAuth();

	const [modalLogoutOpen, setModalLoagoutOpen] = useState(false);
	const toggleModalLogout = () => {
		closeMenu();
		setModalLoagoutOpen(!modalLogoutOpen);
	};

	const [menuOpen, setMenuOpen] = useState(false);
	const toggleMenu = () => setMenuOpen(!menuOpen);
	const closeMenu = () => setMenuOpen(false);

	useEffect(() => {
		const handleResize = () => {
			if (window.innerWidth > 425) closeMenu();
		};
		window.addEventListener("resize", handleResize);
		return () => window.removeEventListener("resize", handleResize);
	}, []);

	const navigate = useNavigate();
	const handleLogin = () => {
		closeMenu();
		authServerRequest();
	};
	const handleLogout = () => {
		deauthenticateUser();
		toggleModalLogout();
		navigate("/");
	};
	const handleRegister = () => {
		closeMenu();
		authRegisterRequest();
	};

	const navbar = () => {
		const navClass = `${styles.nav} ${styles.mobile} ${menuOpen ? styles.active : ""}`;
		if (authenticated && user) {
			return (
				<nav className={navClass}>
					<Link to="/analyses" className={styles.link} onClick={closeMenu}>
						<AnalysesIcon className={styles.icon} />
						Analyses
					</Link>
					<div className={styles.link} onClick={toggleModalLogout}>
						<UserIcon className={styles.icon} />
						<span>{user.username}</span>
					</div>
				</nav>
			);
		} else {
			return (
				<nav className={navClass}>
					<div className={styles.link} onClick={handleRegister}>
						<RegisterIcon className={styles.register_icon} />
						Register
					</div>
					<div className={styles.link} onClick={handleLogin}>
						<LoginIcon className={styles.icon} />
						Login
					</div>
				</nav>
			);
		}
	};

	return (
		<>
			<header className={styles.header}>
				<div className={styles.container}>
					<span className={styles.logo}>
						<LogoIcon className={styles.icon} />
						<Link to="/" className={styles.link}>
							MicroSTAMP
						</Link>
					</span>
					{menuOpen ? (
						<CloseIcon className={styles.menu_icon} onClick={toggleMenu} />
					) : (
						<MenuIcon className={styles.menu_icon} onClick={toggleMenu} />
					)}
					{navbar()}
				</div>
			</header>
			<ModalConfirm
				open={modalLogoutOpen}
				onClose={toggleModalLogout}
				message="Do you want to logout?"
				onConfirm={handleLogout}
				title="Logout"
				btnText="Logout"
			/>
		</>
	);
}

export default Header;

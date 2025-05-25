import { AiOutlineSecurityScan as LogoIcon } from "react-icons/ai";
import { BiLogoGithub as GithubIcon } from "react-icons/bi";
import { MdMailOutline as MailIcon } from "react-icons/md";
import styles from "./Home.module.css";

function Home() {
	return (
		<>
			<section className={`${styles.home_section} ${styles.description} `}>
				<div className={styles.section_text}>
					<h1 className={styles.title}>
						<LogoIcon className={styles.icon} />
						MicroSTAMP
					</h1>
					<span>
						MicroSTAMP is a free and <strong>open-source</strong> web application built
						on a microservices architecture to support STPA. While the project is{" "}
						<strong>still under active development,</strong> MicroSTAMP already offers
						support for <strong>Steps 1, 2, and 3 of the STPA.</strong> We estimate
						releasing the microservice for Step 4 by September 2025. Please note that
						all steps are subject to ongoing changes and improvements.
					</span>
				</div>
				<div className={styles.image}></div>
			</section>

			<section className={`${styles.home_section} ${styles.github_contact} `}>
				<div className={styles.github_section}>
					<h2 className={styles.title}>
						<GithubIcon className={styles.icon} />
						Source Code
					</h2>
					<span>
						MicroSTAMP is open-source, and we welcome contributions! Visit our{" "}
						<a
							href="https://github.com/Micro-STAMP/microstamp"
							target="_blank"
							rel="noopener noreferrer"
						>
							GitHub Repository.
						</a>
					</span>
				</div>
				<div className={styles.contact_section}>
					<h2 className={styles.title}>
						<MailIcon className={styles.icon} />
						Contact Us
					</h2>
					<span>If you have any questions or suggestions, feel free to reach out:</span>
					<span>
						Email:{" "}
						<a href="mailto:rodrigo.pagliares@unifal-mg.edu.br">
							rodrigo.pagliares@unifal-mg.edu.br
						</a>
					</span>
				</div>
			</section>

			<footer className={styles.footer}>
				<h2 className={styles.title}>MicroSTAMP Development Team</h2>
				<div className={styles.contributors}>
					<a
						href="https://github.com/felliperey"
						target="_blank"
						rel="noopener noreferrer"
					>
						<GithubIcon />
						Fellipe Guilherme Rey de Souza
					</a>
					<a
						href="https://github.com/gabriel-francelino"
						target="_blank"
						rel="noopener noreferrer"
					>
						<GithubIcon />
						Gabriel Francelino Nascimento
					</a>
					<a
						href="https://github.com/gabriel-nadalin"
						target="_blank"
						rel="noopener noreferrer"
					>
						<GithubIcon />
						Gabriel Kusumota Nadalin
					</a>
					<a
						href="https://github.com/gabriel-piva"
						target="_blank"
						rel="noopener noreferrer"
					>
						<GithubIcon />
						Gabriel Piva Pereira
					</a>
					<a href="https://github.com/JoaoHugo" target="_blank" rel="noopener noreferrer">
						<GithubIcon />
						João Hugo Marinho Maimone
					</a>
					<a
						href="https://github.com/pagliares"
						target="_blank"
						rel="noopener noreferrer"
					>
						<GithubIcon />
						Rodrigo Martins Pagliares
					</a>
					<a
						href="https://github.com/ThiagoFranco0202"
						target="_blank"
						rel="noopener noreferrer"
					>
						<GithubIcon />
						Thiago Franco de Carvalho Dias
					</a>
				</div>
			</footer>
		</>
	);
}

export default Home;

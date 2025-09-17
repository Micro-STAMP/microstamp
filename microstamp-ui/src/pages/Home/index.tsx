import { AiOutlineSecurityScan as LogoIcon } from "react-icons/ai";
import { BiSolidAnalyse as AnalyseIcon, BiLogoGithub as GithubIcon } from "react-icons/bi";
import { BsDiagram3Fill as ArchitectureIcon } from "react-icons/bs";
import { IoShieldCheckmarkSharp as FullSupportIcon } from "react-icons/io5";
import { MdMail as MailIcon, MdHandshake as PartnershipIcon } from "react-icons/md";
import { RiFilePdf2Fill as PDFIcon } from "react-icons/ri";
import styles from "./Home.module.css";

function Home() {
	const MAIL_TO = "rodrigo.pagliares@unifal-mg.edu.br";

	return (
		<>
			<div className={styles.home_layout}>
				{/* Hero Section */}
				<section className={styles.hero}>
					<div className={styles.hero_text}>
						<h1 className={styles.hero_title}>
							<LogoIcon className={styles.hero_icon} />
							MicroSTAMP
						</h1>
						<p className={styles.hero_subtitle}>
							Microservices for System-Theoretic Process Analysis
						</p>
						<p className={styles.hero_description}>
							A <strong>free</strong> and{" "}
							<strong>open-source STPA compliant tool</strong> based on a{" "}
							<strong>microservices</strong> architecture. You can learn more about
							the project on our GitHub repository or by contacting us via email.
						</p>
						<div className={styles.hero_buttons}>
							<a
								href="https://github.com/Micro-STAMP/microstamp"
								target="_blank"
								rel="noopener noreferrer"
								className={styles.cta_button_outline}
							>
								<GithubIcon /> View on GitHub
							</a>
							<a href={`mailto:${MAIL_TO}`} className={styles.cta_button_outline}>
								<MailIcon /> Contact Us
							</a>
						</div>
					</div>
					<div className={styles.hero_image}>
						<img src="/assets/microstamp_logo.png" alt="MicroSTAMP Logo" />
					</div>
				</section>

				{/* Features Section */}
				<section className={styles.features}>
					<h2 className={styles.section_title}>MicroSTAMP Features</h2>
					<div className={styles.features_grid}>
						<div className={styles.feature_card}>
							<FullSupportIcon className={styles.feature_icon} />
							<h3>Full STPA Support</h3>
							<p>
								Support for all four STPA steps, including two approaches for
								Identify Loss Scenarios: the standard Handbook method and the Formal
								Approach based on{" "}
								<a
									href="https://youtu.be/hp-KBjIBmrI"
									target="_blank"
									rel="noopener noreferrer"
								>
									Formally Developing Loss Scenarios
								</a>
								.
							</p>
						</div>
						<div className={styles.feature_card}>
							<PDFIcon className={styles.feature_icon} />
							<h3>Export Analysis (PDF/JSON)</h3>
							<p>
								Export complete analyses or specific steps as PDF with embedded
								navigation hyperlinks, or as JSON for data portability and
								integration with other tools.
							</p>
						</div>
						<div className={styles.feature_card}>
							<AnalyseIcon className={styles.feature_icon} />
							<h3>Example Analyses Included</h3>
							<p>
								Pre-loaded with example analyses including the Insulin Pump case
								study and control structure examples from the STPA Handbook,
								accessible through the guest account.
							</p>
						</div>
					</div>
				</section>

				{/* Architecture Section */}
				<section className={styles.architecture}>
					<h2 className={styles.section_title}>
						<ArchitectureIcon className={styles.section_title_icon} />
						Microservices Architecture
					</h2>
					<div className={styles.arch_content}>
						<div className={styles.arch_text}>
							<p>
								The MicroSTAMP architecture is based on independent{" "}
								<strong>microservices</strong>, each responsible for a specific part
								of the STPA technique or for supporting the system infrastructure.
								This design promotes{" "}
								<strong>scalability, reusability, and easy integration</strong> with
								other STPA tools.
							</p>
							<p>
								The key components of the architecture include the unified
								MicroSTAMP <strong>user interface</strong>, an{" "}
								<strong>API Gateway</strong> for routing requests, an{" "}
								<strong>Authorization Server</strong> for authentication, a{" "}
								<strong>Service Registry</strong> for discovery, and the dedicated{" "}
								<strong>STPA Microservices</strong>.
							</p>
						</div>
						<div className={styles.arch_image}>
							<img
								src="/assets/microstamp_architecture.png"
								alt="MicroSTAMP Architecture"
							/>
						</div>
					</div>
				</section>

				{/* GitHub and Contact Section */}
				<section className={styles.github_contact}>
					<div className={styles.github_section}>
						<h2 className={styles.subsection_title}>
							<GithubIcon className={styles.subsection_icon} />
							Open Source
						</h2>
						<p>
							MicroSTAMP is open-source, and we welcome contributions! Visit our
							repository to explore the code, report issues, or contribute.
						</p>
						<a
							href="https://github.com/Micro-STAMP/microstamp"
							target="_blank"
							rel="noopener noreferrer"
							className={styles.cta_button}
						>
							View on GitHub
						</a>
					</div>
					<div className={styles.contact_section}>
						<h2 className={styles.subsection_title}>
							<MailIcon className={styles.subsection_icon} />
							Contact Us
						</h2>
						<p>
							Have questions or want to learn more about MicroSTAMP? Reach out to our
							team.
						</p>
						<a href={`mailto:${MAIL_TO}`} className={styles.cta_button}>
							Send Email
						</a>
					</div>
				</section>

				{/* Supporters Section */}
				<section className={styles.supporters}>
					<h2 className={styles.section_title}>Supported By</h2>
					<div className={styles.supporter_logos}>
						<a
							href="https://www.unifal-mg.edu.br/"
							target="_blank"
							rel="noopener noreferrer"
							className={styles.supporter_card}
						>
							<img
								src="https://www.unifal-mg.edu.br/portal/wp-content/uploads/sites/52/2022/06/unifal-logo-transparente.png"
								alt="UNIFAL-MG"
							/>
							<span>Universidade Federal de Alfenas, UNIFAL-MG</span>
						</a>
						<a
							href="https://www.unifal-mg.edu.br/nti/"
							target="_blank"
							rel="noopener noreferrer"
							className={styles.supporter_card}
						>
							<img
								src="https://sistemas.unifal-mg.edu.br/portal/Imagens/icones/nti.png"
								alt="NTI UNIFAL"
							/>
							<span>Núcleo de Tecnologia de Informação (NTI), UNIFAL-MG</span>
						</a>
					</div>
				</section>

				{/* Partnership Section */}
				<section className={styles.partnership}>
					<h2 className={styles.section_title}>
						<PartnershipIcon className={styles.section_title_icon} />
						Partnership Opportunities
					</h2>
					<p>
						We welcome partnerships with organizations interested in advancing
						open-source safety analysis tools. Possible contributions include funding,
						technical expertise, collaborative research, or sponsorship of new features.
					</p>
					<a
						href="mailto:rodrigo.pagliares@unifal-mg.edu.br"
						className={styles.cta_button}
					>
						Explore Partnership
					</a>
				</section>

				{/* Team Section */}
				<footer className={styles.footer}>
					<h2 className={styles.section_title}>Development Team</h2>
					<div className={styles.contributors}>
						{[
							{
								name: "João Hugo Marinho Maimone",
								url: "https://github.com/JoaoHugo"
							},
							{
								name: "Gabriel Francelino Nascimento",
								url: "https://github.com/gabriel-francelino"
							},
							{
								name: "Gabriel Piva Pereira",
								url: "https://github.com/gabriel-piva"
							},
							{
								name: "Rodrigo Martins Pagliares",
								url: "https://github.com/pagliares"
							},
							{
								name: "Fellipe Guilherme Rey de Souza",
								url: "https://github.com/felliperey"
							},
							{
								name: "Gabriel Kusumota Nadalin",
								url: "https://github.com/gabriel-nadalin"
							},
							{
								name: "Thiago Franco de Carvalho Dias",
								url: "https://github.com/ThiagoFranco0202"
							}
						].map((dev, index) => (
							<a key={index} href={dev.url} target="_blank" rel="noopener noreferrer">
								<GithubIcon />
								{dev.name}
							</a>
						))}
					</div>
				</footer>
			</div>
		</>
	);
}

export default Home;

import Pagination from "@components/Pagination";
import { useState } from "react";

function Home() {
	const [page, setPage] = useState(1);
	return (
		<>
			<h1>MicroSTAMP Home</h1>
			<Pagination page={page} pages={10} changePage={setPage} />
		</>
	);
}

export default Home;

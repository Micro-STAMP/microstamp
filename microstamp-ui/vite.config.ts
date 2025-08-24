import react from "@vitejs/plugin-react";
import * as path from "path";
import { defineConfig } from "vite";

// https://vitejs.dev/config/
export default defineConfig({
	plugins: [react()],

	server: {
		host: "127.0.0.1",
		port: 3000
	},

	resolve: {
		alias: {
			"@": path.resolve(__dirname, "./src"),
			"@components": path.resolve(__dirname, "./src/components"),
			"@interfaces": path.resolve(__dirname, "./src/interfaces"),
			"@services": path.resolve(__dirname, "./src/services"),
			"@context": path.resolve(__dirname, "./src/context"),
			"@utils": path.resolve(__dirname, "./src/utils"),
			"@pages": path.resolve(__dirname, "./src/pages"),
			"@hooks": path.resolve(__dirname, "./src/hooks"),
			"@http": path.resolve(__dirname, "./src/http")
		}
	}
});

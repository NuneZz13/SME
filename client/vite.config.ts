import * as path from 'node:path';
import react from '@vitejs/plugin-react-swc';
import { defineConfig } from 'vite';

export default defineConfig({
  plugins: [react()],
  build: {
    outDir: "build/dist",
  },
  server: {
    proxy: {
      "/api": "http://127.0.0.1:8081", //mockoon port
    },
  },
  resolve: {
    alias: [
      // XXX: This is only needed because Kotlin is currently generating a
      // `package.json` with a wrong `main` field (`.js` extension instead of
      // `.mjs`). If/when this is fixed, this entry can be removed.
      {
        find: "SMEFrontEndKform-common",
        replacement: path.resolve(
          __dirname,
          "node_modules/SMEFrontEndKform-common/SMEFrontEndKform-common.mjs"
        ),
      },
      // Due to how Kotlin "bundles" dependencies, we need to use the
      // `@kform/core` produced by the common module.
      {
        find: "@kform/core",
        replacement: path.resolve(
          __dirname,
          "node_modules/SMEFrontEndKform-common/kform-core.mjs"
        ),
      },
    ],
  },
});

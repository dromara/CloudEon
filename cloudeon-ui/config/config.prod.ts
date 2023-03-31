import { defineConfig } from 'umi';
export default defineConfig({
  define: {
    "process.env.UMI_ENV": process.env.UMI_ENV,
  },
});
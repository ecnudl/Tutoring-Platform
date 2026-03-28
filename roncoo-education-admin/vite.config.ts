import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import path from 'path'
export default defineConfig({
  base: '/admin/',
  plugins: [vue()],
  resolve: { alias: { '@': path.resolve(__dirname, 'src') } },
  server: { port: 3100, proxy: { '/api': { target: 'http://localhost:7700', changeOrigin: true, rewrite: (p) => p.replace(/^\/api/, '') } } }
})

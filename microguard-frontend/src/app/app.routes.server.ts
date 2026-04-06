import { RenderMode, ServerRoute } from '@angular/ssr';

export const serverRoutes: ServerRoute[] = [
  { path: '', renderMode: RenderMode.Prerender },
  { path: 'login', renderMode: RenderMode.Prerender },

  // 🔥 IMPORTANT (ADD THIS)
  { path: 'dashboard', renderMode: RenderMode.Server },

  { path: 'analysis/:projectId', renderMode: RenderMode.Server },

  { path: '**', renderMode: RenderMode.Prerender }
];
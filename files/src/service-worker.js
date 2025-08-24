// Basic Vite PWA service worker template (expand for full offline support)
self.addEventListener("install", event => {
  self.skipWaiting();
});
self.addEventListener("activate", event => {
  self.clients.claim();
});
self.addEventListener("fetch", event => {
  // Cache-first for static assets, network-first for API calls
});
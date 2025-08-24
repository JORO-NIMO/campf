# CampusFundi MVP

A campus micro-challenge and barter PWA for Ugandan universities.

## How to run

1. `npm install`
2. `npm run dev` for local dev
3. `npm run build` for production
4. Deploy `/dist` to Netlify, Vercel, or your university server

## Features

- Offline-first (Dexie.js + PWA)
- Micro-challenge creation
- Campus memory map (stub)
- Admin dashboard (stub)
- Theming & branding

## Folder structure

- `/src/db/` — Dexie schema, offline sync helpers
- `/src/modules/` — App modules (challenges, skills, map, admin)
- `/src/pages/` — Main pages
- `/branding/` — Colors, logo

## Contributing

- Keep bundle size < 300KB initial
- Use only local or self-hosted assets
- See [branding/colors.md](branding/colors.md) for color usage

---

Built for MUST Kihumuro & Ugandan campuses."# campf" 

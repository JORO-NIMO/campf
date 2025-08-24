# CampusFundi — Feature-Complete MVP Checklist

This document outlines all remaining tasks to deliver a ready-to-launch, robust MVP for CampusFundi.

---

## 1. Feature Implementation

### a. Module Completion

- [ ] **Skill Swap Market**
  - [ ] Skill listing form (micro-service title, description, token/free, availability, contact)
  - [ ] Skill listing feed (browsable, filterable, sorted by proximity/time)
  - [ ] Claim/request flow
  - [ ] Token/free exchange logic and UI

- [ ] **Memory Map**
  - [ ] Pin creation form (location, caption, 20s audio/image, offline queue)
  - [ ] Map view (use Leaflet/static map; supports offline)
  - [ ] List view for all pins (sorted by distance/time)
  - [ ] Media upload (compress, preview, queue for sync)

- [ ] **Peer Messaging**
  - [ ] Ephemeral chat for challenge/skill arrangement (local-only MVP)
  - [ ] Notification badge for new messages
  - [ ] Delete/conversation expiry logic

- [ ] **Token & Badge System**
  - [ ] Award tokens on challenge/skill completion
  - [ ] Redeem tokens for priority listings/votes
  - [ ] Badge assignment and display on profile
  - [ ] Token ledger UI

- [ ] **Moderation**
  - [ ] Report/flag content flow for users
  - [ ] Ambassador approval dashboard (approve, hide, escalate)
  - [ ] Trust model: restrict new users until verified/approved

- [ ] **Admin Dashboard**
  - [ ] View and approve/reject micro-grant/project applications
  - [ ] Seed events and campus-wide challenges
  - [ ] Safety metrics (flagged content, user reports, etc.)

### b. Core Flows

- [ ] **Offline-First Everything**
  - [ ] All forms and content creation (challenges, skills, pins, messages) work offline and sync later
  - [ ] Home/feed view caches and displays recent data offline

- [ ] **Sync Logic**
  - [ ] Offline queue covers all models
  - [ ] Automatic background sync when online
  - [ ] Conflict handling (edit vs. sync)
  - [ ] User feedback on sync status/errors

---

## 2. Basic UI/UX Polish

- [ ] Home feed: unified, sorted by recency/distance; tabs for challenges, skills, pins
- [ ] Map/list toggle for memory pins
- [ ] Gamified micro-animations (CSS/Lottie) for task completion
- [ ] Responsive mobile UI; large buttons, readable fonts
- [ ] Color scheme and branding assets applied

---

## 3. Essential MVP Integrations

- [ ] **Authentication**
  - [ ] Simple login (email/phone OTP, local mock for MVP)
  - [ ] User onboarding (choose handle, year/course, avatar)

- [ ] **Media**
  - [ ] Image/audio compression before upload
  - [ ] Local preview and storage
  - [ ] (Optional) mock S3/university storage for MVP

- [ ] **QRCode/Quick Share**
  - [ ] Generate QR codes for event/challenge signups
  - [ ] Scan QR codes to join events (offline; syncs when online)

---

## 4. Branding & Content

- [ ] Add actual logo and school colors
- [ ] Onboarding/tutorial screens
- [ ] Seed with demo content:
  - [ ] 20+ curated challenges
  - [ ] 50+ skill listings
  - [ ] 20+ memory pins

---

## 5. Testing & Quality

- [ ] Automated tests for:
  - [ ] Offline queue and sync
  - [ ] Challenge/skill/pin creation
  - [ ] Authentication and onboarding
- [ ] Manual QA:
  - [ ] Install and use offline
  - [ ] Reconnect and test sync
  - [ ] Multiple mobile browsers/older Android
- [ ] Accessibility: color contrast, font size, keyboard nav
- [ ] Low-bandwidth simulation

---

## 6. Deployment

- [ ] Finalize PWA manifest (name, icons, splash, theme color)
- [ ] Add favicon and mobile icons
- [ ] Deploy to Netlify, Vercel, or university server
- [ ] Add clear deployment steps to README

---

## 7. Documentation

- [ ] Update README:
  - [ ] Feature list
  - [ ] Usage instructions
  - [ ] Troubleshooting (offline, sync, login)
- [ ] Add privacy/T&Cs, reporting flow, and safety guidance

---

## 8. Launch Prep

- [ ] Partner with campus groups for onboarding
- [ ] Prepare and print QR codes, posters, WhatsApp invite links
- [ ] Ambassadors briefed and ready for moderation

---

## Progress Tracking

- [ ] [ ] = Not started
- [x] [x] = Complete
- [-] [-] = In progress

---

**Tip:**  
You can break this list into GitHub Issues or project cards for your team!

---

### Questions, feedback, or want to generate code for any of the modules above?  
**Just ask!**

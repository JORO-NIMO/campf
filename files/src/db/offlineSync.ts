import { db } from "./campusfundi-db";

// Example: Call this periodically or on connection regain
export async function syncOfflineQueue() {
  if (!navigator.onLine) return;
  const queue = await db.offlineQueue.toArray();
  for (const item of queue) {
    // Replace with real sync logic (e.g., POST to server or cloud function)
    // For MVP, just log:
    console.log("Syncing offline item:", item);
    await db.offlineQueue.delete(item.id);
  }
}
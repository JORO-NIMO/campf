import Dexie, { Table } from "dexie";

export interface User {
  id: string;
  name: string;
  handle: string;
  year: string;
  course: string;
  reputation: number;
  avatarURL?: string;
}

export interface Challenge {
  id: string;
  creatorId: string;
  title: string;
  desc: string;
  timeEstimate: string;
  tags: string[];
  location?: { lat: number; long: number };
  rewardType: "token" | "badge" | "other";
  status: "open" | "in-progress" | "completed";
  mediaRefs?: string[];
  createdAt: number;
}

export interface SkillListing {
  id: string;
  userId: string;
  title: string;
  priceOrToken: number | "free";
  availability: string;
  contactMethod: string;
}

export interface MemoryPin {
  id: string;
  userId: string;
  lat: number;
  long: number;
  caption: string;
  audioRef?: string;
  imageRef?: string;
  createdAt: number;
}

export interface TokenLedger {
  userId: string;
  balance: number;
  transactions: Array<{ amount: number; type: string; ref: string; date: number }>;
}

class CampusFundiDB extends Dexie {
  users!: Table<User>;
  challenges!: Table<Challenge>;
  skillListings!: Table<SkillListing>;
  memoryPins!: Table<MemoryPin>;
  tokenLedger!: Table<TokenLedger>;
  offlineQueue!: Table<any>;

  constructor() {
    super("CampusFundiDB");
    this.version(1).stores({
      users: "id",
      challenges: "id,creatorId,status,createdAt",
      skillListings: "id,userId",
      memoryPins: "id,userId,lat,long,createdAt",
      tokenLedger: "userId",
      offlineQueue: "++id,type"
    });
  }
}

export const db = new CampusFundiDB();
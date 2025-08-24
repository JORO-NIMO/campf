import React, { useState } from "react";
import { db } from "../../db/campusfundi-db";
import { useNavigate } from "react-router-dom";

export default function ChallengeCreate() {
  const [title, setTitle] = useState("");
  const [desc, setDesc] = useState("");
  const [timeEstimate, setTimeEstimate] = useState("");
  const [rewardType, setRewardType] = useState("token");
  const [submitting, setSubmitting] = useState(false);
  const navigate = useNavigate();

  async function handleSubmit(e: React.FormEvent) {
    e.preventDefault();
    setSubmitting(true);

    const newChallenge = {
      id: crypto.randomUUID(),
      creatorId: "demo-user", // Replace with real user ID
      title,
      desc,
      timeEstimate,
      tags: [],
      rewardType,
      status: "open",
      createdAt: Date.now()
    };

    try {
      await db.challenges.add(newChallenge);
      // Add to offline queue if offline, else sync immediately
      if (!navigator.onLine) {
        await db.offlineQueue.add({ type: "challenge", data: newChallenge });
      }
      navigate("/");
    } catch (e) {
      alert("Failed to create challenge. Try again.");
    }
    setSubmitting(false);
  }

  return (
    <form onSubmit={handleSubmit} className="p-4 flex flex-col gap-2">
      <h2 className="text-xl font-bold">Create Micro-Challenge</h2>
      <input
        className="input"
        placeholder="Title"
        required
        value={title}
        onChange={e => setTitle(e.target.value)}
      />
      <textarea
        className="input"
        placeholder="Description"
        required
        value={desc}
        onChange={e => setDesc(e.target.value)}
      />
      <input
        className="input"
        placeholder="Time Estimate"
        required
        value={timeEstimate}
        onChange={e => setTimeEstimate(e.target.value)}
      />
      <select
        className="input"
        value={rewardType}
        onChange={e => setRewardType(e.target.value)}
      >
        <option value="token">Token</option>
        <option value="badge">Badge</option>
        <option value="other">Other</option>
      </select>
      <button className="btn-primary" type="submit" disabled={submitting}>
        {submitting ? "Creating..." : "Create Challenge"}
      </button>
    </form>
  );
}
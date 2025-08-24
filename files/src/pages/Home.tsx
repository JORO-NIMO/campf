import React from "react";
import { Link } from "react-router-dom";

export default function Home() {
  return (
    <main className="p-4">
      <h1 className="text-2xl font-bold">CampusFundi</h1>
      <p>Fun, local, and free — micro-challenges, peer-skill swaps, campus stories, and micro-rewards.</p>
      <div className="flex flex-col gap-2 mt-4">
        <Link to="/challenge/create" className="btn-primary">Create Challenge</Link>
        <Link to="/memory-map" className="btn-secondary">Campus Memory Map</Link>
        <Link to="/admin" className="btn-tertiary">Admin Dashboard</Link>
      </div>
      {/* Add feeds here */}
    </main>
  );
}
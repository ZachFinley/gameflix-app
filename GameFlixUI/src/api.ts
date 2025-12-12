const API_BASE = "http://localhost:8080";
import type { User } from "./types";

export async function getAllGames() {
  const res = await fetch(`${API_BASE}/api/games`);
  if (!res.ok) throw new Error("Failed to fetch games");
  return res.json();
}

export async function getGamesByStatus(status: string) {
  const res = await fetch(`${API_BASE}/api/games/status/${encodeURIComponent(status)}`);
  if (!res.ok) throw new Error("Failed to fetch games by status");
  return res.json();
}

export async function getGameById(id: number) {
  const res = await fetch(`${API_BASE}/api/games/${id}`);
  if (!res.ok) throw new Error("Failed to fetch game");
  return res.json();
}

export async function searchGames(title: string) {
  const res = await fetch(`${API_BASE}/api/games/search?title=${encodeURIComponent(title)}`);
  if (!res.ok) throw new Error("Failed to search games");
  return res.json();
}

export async function register(username: string, password: string) {
  const res = await fetch(`${API_BASE}/register`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ username, password }),
  });
  const text = await res.text();
  return { ok: res.ok, status: res.status, message: text };
}

export async function login(username: string, password: string) {
  const res = await fetch(`${API_BASE}/login`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ username, password }),
  });
  const text = await res.text();
  return { ok: res.ok, status: res.status, message: text };
}

export async function findUserByEmail(email: string): Promise<User[]>{
  const res = await fetch(`${API_BASE}/api/users/search/email?email=${encodeURIComponent(email)}`);
  if (!res.ok) throw new Error("Failed to find user");
  return res.json();
}

export async function getUserLibrary(userId: number) {
  const res = await fetch(`${API_BASE}/api/users/${userId}/library`);
  if (!res.ok) throw new Error("Failed to fetch user library");
  return res.json();
}

export async function isUserAdmin(userId: number) {
  const res = await fetch(`${API_BASE}/api/users/${userId}/is-admin`);
  if (!res.ok) throw new Error("Failed to check admin");
  return res.json();
}

export async function addToLibrary(userId: number, gameId: number) {
  const res = await fetch(`${API_BASE}/api/users/${userId}/library`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ gameId }),
  });
  if (!res.ok) {
    const text = await res.text();
    throw new Error(text || "Failed to add to library");
  }
  return res.json();
}

export async function removeFromLibrary(userId: number, gameId: number) {
  const res = await fetch(`${API_BASE}/api/users/${userId}/library/${gameId}`, {
    method: "DELETE",
  });
  if (!res.ok) {
    const text = await res.text();
    throw new Error(text || "Failed to remove from library");
  }
  return true;
}

export default {
  getAllGames,
  getGamesByStatus,
  getGameById,
  searchGames,
  register,
  login,
  findUserByEmail,
  getUserLibrary,
  isUserAdmin,
};

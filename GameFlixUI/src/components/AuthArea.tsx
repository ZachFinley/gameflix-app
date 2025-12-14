import { useState } from "react";
import * as api from "../api";
import type { User } from "../types";

type Props = {
  onSignedIn: (user: User) => void;
};

export default function AuthArea({ onSignedIn }: Props) {
  const [mode, setMode] = useState<"login" | "register">("login");
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [displayName, setDisplayName] = useState("");
  const [message, setMessage] = useState<string | null>(null);

  async function doLogin(e?: React.FormEvent) {
    e?.preventDefault();
    setMessage(null);
    try {
      const r = await api.login(username, password);
      setMessage(r.message);
      if (r.ok) {
        const users = await api.findUserByEmail(username);
        if (users && users.length) {
          const u = users[0];
          onSignedIn({ id: u.id, displayName: String(u.displayName || u.email || username), email: u.email, admin: u.admin });
        } else {
          onSignedIn({ id: -1, displayName: username, email: username, admin: false });
        }
      }
    } catch (err: unknown) {
      const msg = typeof err === "object" && err !== null && "message" in err ? String((err as { message?: unknown }).message) : String(err);
      setMessage(msg);
    }
  }

  async function doRegister(e?: React.FormEvent) {
    e?.preventDefault();
    setMessage(null);
    try {
      const r = await api.register(username, password);
      setMessage(r.message);
      if (r.ok) {
        const users = await api.findUserByEmail(username);
        if (users && users.length) {
          const u = users[0];
          const finalDisplayName = displayName.trim() || String(u.displayName || u.email || username);
          onSignedIn({ id: u.id, displayName: finalDisplayName, email: u.email, admin: u.admin });
        }
      }
    } catch (err: unknown) {
      const msg = typeof err === "object" && err !== null && "message" in err ? String((err as { message?: unknown }).message) : String(err);
      setMessage(msg);
    }
  }

  return (
    <div className="p-3">
      <h2>{mode === "login" ? "Sign In" : "Register"}</h2>
      {message && <div className="mb-2 alert alert-info">{message}</div>}
      <form onSubmit={mode === "login" ? doLogin : doRegister} className="d-grid gap-2 gf-form-max-sm">
        <label className="form-label">
          Email
          <input className="form-control" value={username} onChange={e => setUsername(e.target.value)} />
        </label>
        {mode === "register" && (
          <label className="form-label">
            Display Name
            <input className="form-control" value={displayName} onChange={e => setDisplayName(e.target.value)}/>
          </label>
        )}
        <label className="form-label">
          Password
          <input className="form-control" type="password" value={password} onChange={e => setPassword(e.target.value)} />
        </label>
        <div className="d-flex gap-2">
          <button className="btn btn-primary" type="submit">{mode === "login" ? "Sign In" : "Register"}</button>
          <button className="btn btn-outline-secondary" type="button" onClick={() => { setMode(mode === "login" ? "register" : "login"); setMessage(null); }}>
            {mode === "login" ? "Switch to Register" : "Switch to Sign In"}
          </button>
        </div>
      </form>
    </div>
  );
}

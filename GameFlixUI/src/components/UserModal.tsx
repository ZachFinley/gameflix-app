import type { User } from "../types";

type Props = { user: User; onClose: () => void; onSignOut?: () => void };

export default function UserModal({ user, onClose, onSignOut }: Props) {
  return (
    <div className="gf-modal-backdrop">
      <div className="gf-modal">
        <h3>User Settings</h3>
        <p><strong>Name:</strong> {user.displayName}</p>
        <p><strong>Email:</strong> {user.email || "(not set)"}</p>
        <p><strong>Admin:</strong> {user.admin ? "Yes" : "No"}</p>
        <div className="text-end mt-2">
          {onSignOut && (
            <button className="btn btn-outline-secondary me-2" onClick={() => { onSignOut(); onClose(); }}>Sign Out</button>
          )}
          <button className="btn btn-primary" onClick={onClose}>Close</button>
        </div>
      </div>
    </div>
  );
}

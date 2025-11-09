type User = {
  id: number;
  displayName: string;
  admin?: boolean;
};

type Props = {
  currentUser: User | null;
  onNavigate: (page: string) => void;
  onOpenUserModal: () => void;
};

export default function Navbar({ currentUser, onNavigate, onOpenUserModal }: Props) {
  return (
    <header className="gf-navbar d-flex justify-content-between align-items-center">
      <nav className="d-flex align-items-center gap-2">
        <button className="btn btn-link gf-nav-link" onClick={() => onNavigate("home")}>Home</button>
        <button className="btn btn-link gf-nav-link" onClick={() => onNavigate("library")}>Library</button>
        {currentUser?.admin && (
          <button className="btn btn-link gf-nav-link" onClick={() => onNavigate("admin")}>
            Admin
          </button>
        )}
      </nav>

      <div className="d-flex align-items-center gap-2">
        {currentUser ? (
          <>
            <span className="me-2">Hello, {currentUser.displayName}</span>
            <button className="btn btn-outline-secondary btn-sm" onClick={onOpenUserModal}>User</button>
          </>
        ) : (
          <button className="btn btn-primary btn-sm" onClick={() => onNavigate("signin")}>Sign In</button>
        )}
      </div>
    </header>
  );
}

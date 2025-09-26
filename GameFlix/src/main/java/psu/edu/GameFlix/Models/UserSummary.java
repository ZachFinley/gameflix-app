package psu.edu.GameFlix.Models;

public class UserSummary {
    private Long id;
    private String email;
    private String displayName;
    private boolean isEmailVerified;
    private boolean isAdmin;
    private String createdAt;

    public UserSummary() {}

    public UserSummary(Long id, String email, String displayName, boolean isEmailVerified, boolean isAdmin, String createdAt) {
        this.id = id;
        this.email = email;
        this.displayName = displayName;
        this.isEmailVerified = isEmailVerified;
        this.isAdmin = isAdmin;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public boolean isEmailVerified() {
        return isEmailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        isEmailVerified = emailVerified;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}

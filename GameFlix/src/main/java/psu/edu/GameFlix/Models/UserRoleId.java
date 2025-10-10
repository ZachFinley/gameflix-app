package psu.edu.GameFlix.Models;

import java.io.Serializable;
import java.util.Objects;

public class UserRoleId implements Serializable {
    private int userId;
    private int roleId;

    public UserRoleId() {
    }

    public UserRoleId(int userId, int roleId) {
        this.userId = userId;
        this.roleId = roleId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }
}

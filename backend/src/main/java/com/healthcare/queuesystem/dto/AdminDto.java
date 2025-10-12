package com.healthcare.queuesystem.dto;

import java.time.LocalDateTime;
import java.util.Map;

public class AdminDto {

    private String adminId;
    private String username;
    private String email;
    private String role;
    private LocalDateTime lastLogin;
    private boolean isActive;
    private Map<String, Object> permissions;

    // Default constructor
    public AdminDto() {}

    // Constructor with essential fields
    public AdminDto(String username, String email, String role) {
        this.username = username;
        this.email = email;
        this.role = role;
        this.isActive = true;
    }

    // Getters and Setters
    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Map<String, Object> getPermissions() {
        return permissions;
    }

    public void setPermissions(Map<String, Object> permissions) {
        this.permissions = permissions;
    }
}
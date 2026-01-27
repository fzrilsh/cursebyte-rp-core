package com.cursebyte.plugin.modules.state.authority;

public class AuthorityContext {
    public RoleType role;
    public String institution;
    public boolean isCoordinator;

    public AuthorityContext(RoleType role, String institution, boolean isCoordinator){
        this.role = role;
        this.institution = institution;
        this.isCoordinator = isCoordinator;
    }
}
package com.cursebyte.plugin.modules.governance.player;

import java.util.UUID;

public class GovernanceProfile {
    private UUID uuid;
    private String roleId; // ex: PRESIDENT, BEN_COORDINATOR
    private String institutionId; // ex: BEN

    public GovernanceProfile(UUID uuid, String roleId, String institutionId) {
        this.uuid = uuid;
        this.roleId = roleId;
        this.institutionId = institutionId;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getRoleId() {
        return roleId;
    }

    public String getInstitutionId() {
        return institutionId;
    }
}
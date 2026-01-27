package com.cursebyte.plugin.modules.governance.model;

public class Role {
    private String id;
    private String institutionId;
    private int authorityLevel;
    private boolean canMakeDecision;
    private boolean canBroadcast;
    private boolean canCallMeeting;

    public Role(String id, String institutionId, int authorityLevel, boolean canMakeDecision, boolean canBroadcast,
            boolean canCallMeeting) {
        this.id = id;
        this.institutionId = institutionId;
        this.authorityLevel = authorityLevel;
        this.canMakeDecision = canMakeDecision;
        this.canBroadcast = canBroadcast;
        this.canCallMeeting = canCallMeeting;
    }

    public String getId() {
        return id;
    }

    public String getInstitutionId() {
        return institutionId;
    }

    public int getAuthorityLevel() {
        return authorityLevel;
    }

    public boolean canMakeDecision() {
        return canMakeDecision;
    }

    public boolean canBroadcast() {
        return canBroadcast;
    }

    public boolean canCallMeeting() {
        return canCallMeeting;
    }
}

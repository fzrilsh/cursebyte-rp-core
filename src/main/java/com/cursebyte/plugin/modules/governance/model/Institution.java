package com.cursebyte.plugin.modules.governance.model;

import java.util.*;

public class Institution {
    private String id;
    private String name;
    private boolean requiresPresidentApproval;
    private List<String> permissions;

    public Institution(String id, String name, boolean requiresPresidentApproval, List<String> permissions) {
        this.id = id;
        this.name = name;
        this.requiresPresidentApproval = requiresPresidentApproval;
        this.permissions = permissions;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean requiresApproval() {
        return requiresPresidentApproval;
    }

    public List<String> getPermissions() {
        return permissions;
    }
}

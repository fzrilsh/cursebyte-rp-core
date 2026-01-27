package com.cursebyte.plugin.modules.governance.model;

import java.util.List;
import java.util.Map;

public class StateStructure {

    public President president;
    public VicePresident vicePresident;
    public Map<String, InstitutionConfig> institutions;
    public CitizenRules citizenRules;

    public static class President {
        public boolean enabled;
        public List<String> roles;
        public List<String> approvalRequiredFor;
    }

    public static class VicePresident {
        public boolean enabled;
        public List<String> roles;
        public List<String> restrictions;
    }

    public static class InstitutionConfig {
        public String name;
        public String coordinator;
        public List<String> members;
        public List<String> permissions;
        public boolean requirePresidentApproval;
    }

    public static class CitizenRules {
        public double votingReputationMin;
        public double businessReputationMin;
        public double officeReputationMin;
    }
}
package com.cursebyte.plugin.modules.governance.engine;

import java.util.*;

import com.cursebyte.plugin.modules.governance.model.Institution;
import com.cursebyte.plugin.modules.governance.model.Role;

public class GovernanceRegistry {

    private static final Map<String, Institution> institutions = new HashMap<>();
    private static final Map<String, Role> roles = new HashMap<>();

    public static void registerInstitution(Institution i){
        institutions.put(i.getId(), i);
    }

    public static void registerRole(Role r){
        roles.put(r.getId(), r);
    }

    public static Institution getInstitution(String id){
        return institutions.get(id);
    }

    public static Role getRole(String id){
        return roles.get(id);
    }

    public static Collection<Institution> getInstitutions(){
        return institutions.values();
    }

    public static Collection<Role> getRoles(){
        return roles.values();
    }
}
package com.cursebyte.plugin.modules.state.authority;

import java.util.UUID;

import com.cursebyte.plugin.modules.governance.model.StateStructure;
import com.cursebyte.plugin.modules.state.core.StateLoader;

public class AuthorityManager {

    public static AuthorityContext resolve(UUID uuid){
        StateStructure state = StateLoader.get();

        if(uuid.toString().equals(state.president.enabled ? getPresidentUUID() : "")){
            return new AuthorityContext(RoleType.PRESIDENT, "STATE", true);
        }

        if(uuid.toString().equals(getVicePresidentUUID())){
            return new AuthorityContext(RoleType.VICE_PRESIDENT, "STATE", true);
        }

        for(var entry : state.institutions.entrySet()){
            String key = entry.getKey();
            var inst = entry.getValue();

            if(uuid.toString().equals(inst.coordinator)){
                return new AuthorityContext(
                    RoleType.valueOf(key + "_COORDINATOR"),
                    key,
                    true
                );
            }

            if(inst.members.contains(uuid.toString())){
                return new AuthorityContext(
                    RoleType.valueOf(key + "_MEMBER"),
                    key,
                    false
                );
            }
        }

        return new AuthorityContext(RoleType.CITIZEN, "CITIZEN", false);
    }

    public static boolean hasPermission(UUID uuid, String permission){
        AuthorityContext ctx = resolve(uuid);

        if(ctx.role == RoleType.PRESIDENT) return true;
        if(ctx.role == RoleType.VICE_PRESIDENT){
            if(permission.equalsIgnoreCase("final_decision")) return false;
            return true;
        }

        StateStructure state = StateLoader.get();
        var inst = state.institutions.get(ctx.institution);

        if(inst == null) return false;

        return inst.permissions.contains(permission);
    }

    public static AuthorityResult requireApproval(UUID actor, String institution){
        StateStructure state = StateLoader.get();

        var inst = state.institutions.get(institution);
        if(inst == null) return AuthorityResult.deny("Institution not found");

        if(inst.requirePresidentApproval){
            AuthorityContext ctx = resolve(actor);
            if(ctx.role == RoleType.PRESIDENT){
                return AuthorityResult.allow();
            }
            return AuthorityResult.deny("Presidential approval required");
        }

        return AuthorityResult.allow();
    }

    public static boolean canCommand(UUID commander, UUID target){
        AuthorityContext c1 = resolve(commander);
        AuthorityContext c2 = resolve(target);

        if(c1.role == RoleType.PRESIDENT) return true;
        if(c1.role == RoleType.VICE_PRESIDENT && c2.role != RoleType.PRESIDENT) return true;

        if(c1.isCoordinator && c1.institution.equals(c2.institution)) return true;

        return false;
    }

    private static String getPresidentUUID(){
        return StateLoader.get().president.enabled ? 
            StateLoader.get().president.roles.toString() : "";
    }

    private static String getVicePresidentUUID(){
        return "";
    }
}
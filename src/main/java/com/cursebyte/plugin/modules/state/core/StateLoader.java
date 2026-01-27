package com.cursebyte.plugin.modules.state.core;

import org.bukkit.configuration.file.FileConfiguration;

import com.cursebyte.plugin.modules.governance.engine.GovernanceEngine;
import com.cursebyte.plugin.modules.governance.model.StateStructure;

import java.util.*;

public class StateLoader {

    private static StateStructure state;

    public static void load(){
        FileConfiguration cfg = StateConfigManager.get();

        state = new StateStructure();

        // President
        var p = new StateStructure.President();
        p.enabled = cfg.getBoolean("state.structure.president.enabled");
        p.roles = cfg.getStringList("state.structure.president.roles");
        p.approvalRequiredFor = cfg.getStringList("state.structure.president.approval_required_for");
        state.president = p;

        // Vice President
        var vp = new StateStructure.VicePresident();
        vp.enabled = cfg.getBoolean("state.structure.vice_president.enabled");
        vp.roles = cfg.getStringList("state.structure.vice_president.roles");
        vp.restrictions = cfg.getStringList("state.structure.vice_president.restrictions");
        state.vicePresident = vp;

        // Institutions
        Map<String, StateStructure.InstitutionConfig> instMap = new HashMap<>();
        var section = cfg.getConfigurationSection("state.structure.institutions");
        if(section != null){
            for(String key : section.getKeys(false)){
                var i = new StateStructure.InstitutionConfig();
                i.name = cfg.getString("state.structure.institutions."+key+".name");
                i.coordinator = cfg.getString("state.structure.institutions."+key+".coordinator");
                i.members = cfg.getStringList("state.structure.institutions."+key+".members");
                i.permissions = cfg.getStringList("state.structure.institutions."+key+".permissions");
                i.requirePresidentApproval = cfg.getBoolean("state.structure.institutions."+key+".require_president_approval");
                instMap.put(key, i);
            }
        }

        state.institutions = instMap;

        // Citizen rules
        var cr = new StateStructure.CitizenRules();
        cr.votingReputationMin = cfg.getDouble("state.citizen_rules.voting_reputation_min");
        cr.businessReputationMin = cfg.getDouble("state.citizen_rules.business_reputation_min");
        cr.officeReputationMin = cfg.getDouble("state.citizen_rules.office_reputation_min");
        state.citizenRules = cr;

        GovernanceEngine.bootstrap(state);
    }

    public static StateStructure get(){
        return state;
    }
}
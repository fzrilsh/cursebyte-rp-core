package com.cursebyte.plugin.modules.governance.engine;

import java.util.*;

import com.cursebyte.plugin.modules.governance.model.Institution;
import com.cursebyte.plugin.modules.governance.model.Role;
import com.cursebyte.plugin.modules.governance.model.StateStructure;

public class GovernanceEngine {

        public static void bootstrap(StateStructure state) {

                // PRESIDENT
                if (state.president.enabled) {
                        GovernanceRegistry.registerInstitution(
                                        new Institution(
                                                        "PRESIDENCY",
                                                        "Presiden",
                                                        false,
                                                        List.of("broadcast", "approve_decision", "call_meeting")));

                        for (String r : state.president.roles) {
                                GovernanceRegistry.registerRole(
                                                new Role(r, "PRESIDENCY", 100, true, true, true));
                        }
                }

                // VICE PRESIDENT
                if (state.vicePresident.enabled) {
                        GovernanceRegistry.registerInstitution(
                                        new Institution(
                                                        "VICE_PRESIDENCY",
                                                        "Wakil Presiden",
                                                        false,
                                                        List.of("receive_reports", "fine_decision")));

                        for (String r : state.vicePresident.roles) {
                                GovernanceRegistry.registerRole(
                                                new Role(r, "VICE_PRESIDENCY", 90, false, false, false));
                        }
                }

                // INSTITUTIONS
                for (var entry : state.institutions.entrySet()) {
                        var i = entry.getValue();

                        GovernanceRegistry.registerInstitution(
                                        new Institution(
                                                        entry.getKey(),
                                                        i.name,
                                                        i.requirePresidentApproval,
                                                        i.permissions));

                        GovernanceRegistry.registerRole(
                                        new Role(
                                                        i.coordinator,
                                                        entry.getKey(),
                                                        80,
                                                        true,
                                                        false,
                                                        true));

                        for (String m : i.members) {
                                GovernanceRegistry.registerRole(
                                                new Role(
                                                                m,
                                                                entry.getKey(),
                                                                50,
                                                                false,
                                                                false,
                                                                false));
                        }
                }
        }
}
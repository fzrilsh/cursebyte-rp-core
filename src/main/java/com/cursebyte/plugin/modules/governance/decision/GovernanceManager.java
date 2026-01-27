package com.cursebyte.plugin.modules.governance.decision;

import java.util.UUID;

import com.cursebyte.plugin.modules.state.authority.AuthorityManager;
import com.cursebyte.plugin.modules.state.authority.AuthorityResult;

public class GovernanceManager {

    public static int propose(UUID proposer, String institution, String type, String payload){

        if(!AuthorityManager.hasPermission(proposer, "create_decision")){
            return -1;
        }

        AuthorityResult approval = AuthorityManager.requireApproval(proposer, institution);

        int id = GovernanceRepository.createProposal(
            proposer.toString(),
            institution,
            type,
            payload
        );

        if(id == -1) return -1;

        if(!approval.allowed){
            GovernanceRepository.setStatus(id, "WAITING_PRESIDENT");
        }else{
            GovernanceRepository.setStatus(id, "APPROVED");
            execute(id, type, payload);
        }

        GovernanceRepository.audit(
            proposer.toString(),
            "CREATE_PROPOSAL",
            institution + ":" + type
        );

        return id;
    }

    public static void approve(UUID approver, int proposalId, boolean accepted){

        String decision = accepted ? "APPROVED" : "REJECTED";

        GovernanceRepository.addApproval(
            proposalId,
            approver.toString(),
            decision
        );

        GovernanceRepository.setStatus(
            proposalId,
            decision
        );

        if(accepted){
            // TODO: load payload & execute
        }

        GovernanceRepository.audit(
            approver.toString(),
            "APPROVAL",
            "proposal=" + proposalId + " decision=" + decision
        );
    }

    private static void execute(int id, String type, String payload){
        // future executor system
        // TAX_CHANGE
        // JOB_SALARY
        // FUND_TRANSFER
        // LAW_CREATE
        // BUSINESS_LICENSE_REVOKE
    }
}
package com.cursebyte.plugin.modules.state.election;

import java.util.UUID;

public class ElectionManager {

    public static int createElection(UUID creator, String type, String institution){
        return ElectionRepository.createElection(
            type,
            institution,
            creator.toString()
        );
    }

    public static boolean registerCandidate(int electionId, UUID player, String name, double reputation){
        if(reputation < 0.8) return false;
        ElectionRepository.registerCandidate(electionId, player, name, reputation);
        return true;
    }

    public static boolean vote(int electionId, UUID voter, int candidateId){
        if(ElectionRepository.hasVoted(electionId, voter)) return false;
        ElectionRepository.vote(electionId, voter, candidateId);
        return true;
    }

    public static void startVoting(int electionId){
        ElectionRepository.setElectionStatus(electionId, "VOTING");
    }

    public static void startCounting(int electionId){
        ElectionRepository.setElectionStatus(electionId, "COUNTING");
    }

    public static void finishElection(int electionId){
        ElectionRepository.setElectionStatus(electionId, "FINISHED");
    }
}
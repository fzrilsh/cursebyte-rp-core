package com.cursebyte.plugin.modules.state.election;

import com.cursebyte.plugin.database.DatabaseManager;
import java.sql.*;
import java.util.*;

public class ElectionRepository {

    public static void init(){
        try(Statement st = DatabaseManager.getConnection().createStatement()){
            st.execute("""
                CREATE TABLE IF NOT EXISTS elections (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    type TEXT,
                    institution TEXT,
                    status TEXT,
                    created_at INTEGER,
                    created_by TEXT
                )
            """);

            st.execute("""
                CREATE TABLE IF NOT EXISTS election_candidates (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    election_id INTEGER,
                    uuid TEXT,
                    name TEXT,
                    reputation REAL,
                    approved INTEGER
                )
            """);

            st.execute("""
                CREATE TABLE IF NOT EXISTS election_votes (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    election_id INTEGER,
                    voter TEXT,
                    candidate_id INTEGER,
                    time INTEGER
                )
            """);

            st.execute("""
                CREATE TABLE IF NOT EXISTS election_results (
                    election_id INTEGER,
                    winner_uuid TEXT,
                    winner_name TEXT,
                    total_votes INTEGER
                )
            """);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static int createElection(String type, String institution, String creator){
        try(PreparedStatement ps = DatabaseManager.getConnection()
                .prepareStatement("""
                    INSERT INTO elections(type, institution, status, created_at, created_by)
                    VALUES(?,?,?,?,?)
                """, Statement.RETURN_GENERATED_KEYS)){
            ps.setString(1, type);
            ps.setString(2, institution);
            ps.setString(3, "REGISTRATION");
            ps.setLong(4, System.currentTimeMillis());
            ps.setString(5, creator);
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if(rs.next()) return rs.getInt(1);
        }catch(Exception e){
            e.printStackTrace();
        }
        return -1;
    }

    public static void setElectionStatus(int id, String status){
        try(PreparedStatement ps = DatabaseManager.getConnection()
                .prepareStatement("UPDATE elections SET status=? WHERE id=?")){
            ps.setString(1, status);
            ps.setInt(2, id);
            ps.executeUpdate();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    // --- Candidates ---
    public static void registerCandidate(int electionId, UUID uuid, String name, double rep){
        try(PreparedStatement ps = DatabaseManager.getConnection()
                .prepareStatement("""
                    INSERT INTO election_candidates(election_id, uuid, name, reputation, approved)
                    VALUES(?,?,?,?,?)
                """)){
            ps.setInt(1, electionId);
            ps.setString(2, uuid.toString());
            ps.setString(3, name);
            ps.setDouble(4, rep);
            ps.setInt(5, 1);
            ps.executeUpdate();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void vote(int electionId, UUID voter, int candidateId){
        try(PreparedStatement ps = DatabaseManager.getConnection()
                .prepareStatement("""
                    INSERT INTO election_votes(election_id, voter, candidate_id, time)
                    VALUES(?,?,?,?)
                """)){
            ps.setInt(1, electionId);
            ps.setString(2, voter.toString());
            ps.setInt(3, candidateId);
            ps.setLong(4, System.currentTimeMillis());
            ps.executeUpdate();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static boolean hasVoted(int electionId, UUID voter){
        try(PreparedStatement ps = DatabaseManager.getConnection()
                .prepareStatement("""
                    SELECT 1 FROM election_votes WHERE election_id=? AND voter=?
                """)){
            ps.setInt(1, electionId);
            ps.setString(2, voter.toString());
            ResultSet rs = ps.executeQuery();
            return rs.next();
        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }
}
package com.cursebyte.plugin.modules.governance.decision;

import com.cursebyte.plugin.database.DatabaseManager;
import java.sql.*;

public class GovernanceRepository {

    public static void init() {
        try (Statement st = DatabaseManager.getConnection().createStatement()) {
            st.execute("""
                        CREATE TABLE IF NOT EXISTS governance_proposals (
                            id INTEGER PRIMARY KEY AUTOINCREMENT,
                            proposer TEXT,
                            institution TEXT,
                            type TEXT,
                            payload TEXT,
                            status TEXT,
                            created_at INTEGER
                        )
                    """);

            st.execute("""
                        CREATE TABLE IF NOT EXISTS governance_approvals (
                            id INTEGER PRIMARY KEY AUTOINCREMENT,
                            proposal_id INTEGER,
                            approver TEXT,
                            decision TEXT,
                            time INTEGER
                        )
                    """);

            st.execute("""
                        CREATE TABLE IF NOT EXISTS state_audit_log (
                            id INTEGER PRIMARY KEY AUTOINCREMENT,
                            actor TEXT,
                            action TEXT,
                            context TEXT,
                            time INTEGER
                        )
                    """);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int createProposal(String proposer, String institution, String type, String payload) {
        String sql = """
                    INSERT INTO governance_proposals(proposer, institution, type, payload, status, created_at)
                    VALUES(?,?,?,?,?,?)
                """;

        try (PreparedStatement ps = DatabaseManager.getConnection()
                .prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, proposer);
            ps.setString(2, institution);
            ps.setString(3, type);
            ps.setString(4, payload);
            ps.setString(5, "PENDING");
            ps.setLong(6, System.currentTimeMillis());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next())
                return rs.getInt(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static void setStatus(int id, String status) {
        try (PreparedStatement ps = DatabaseManager.getConnection()
                .prepareStatement("UPDATE governance_proposals SET status=? WHERE id=?")) {
            ps.setString(1, status);
            ps.setInt(2, id);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addApproval(int proposalId, String approver, String decision) {
        try (PreparedStatement ps = DatabaseManager.getConnection()
                .prepareStatement("""
                            INSERT INTO governance_approvals(proposal_id, approver, decision, time)
                            VALUES(?,?,?,?)
                        """)) {
            ps.setInt(1, proposalId);
            ps.setString(2, approver);
            ps.setString(3, decision);
            ps.setLong(4, System.currentTimeMillis());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void audit(String actor, String action, String context) {
        try (PreparedStatement ps = DatabaseManager.getConnection()
                .prepareStatement("""
                            INSERT INTO state_audit_log(actor, action, context, time)
                            VALUES(?,?,?,?)
                        """)) {
            ps.setString(1, actor);
            ps.setString(2, action);
            ps.setString(3, context);
            ps.setLong(4, System.currentTimeMillis());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
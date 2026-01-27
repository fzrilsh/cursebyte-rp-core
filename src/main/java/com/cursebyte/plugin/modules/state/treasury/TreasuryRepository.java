package com.cursebyte.plugin.modules.state.treasury;

import com.cursebyte.plugin.database.DatabaseManager;
import java.sql.*;

public class TreasuryRepository {

    public static void init(){
        try(Statement st = DatabaseManager.getConnection().createStatement()){
            st.execute("""
                CREATE TABLE IF NOT EXISTS state_treasury (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    balance REAL
                )
            """);

            st.execute("""
                CREATE TABLE IF NOT EXISTS state_tax_rules (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT,
                    type TEXT,
                    rate REAL,
                    active INTEGER
                )
            """);

            st.execute("""
                CREATE TABLE IF NOT EXISTS state_budget (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    institution TEXT,
                    allocated REAL,
                    used REAL
                )
            """);

            st.execute("""
                CREATE TABLE IF NOT EXISTS state_fund_flow (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    from_entity TEXT,
                    to_entity TEXT,
                    amount REAL,
                    reason TEXT,
                    time INTEGER
                )
            """);

            // init treasury row
            st.execute("""
                INSERT OR IGNORE INTO state_treasury(id, balance) VALUES(1, 0)
            """);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    // Treasury
    public static double getTreasury(){
        try(PreparedStatement ps = DatabaseManager.getConnection()
                .prepareStatement("SELECT balance FROM state_treasury WHERE id=1")){
            ResultSet rs = ps.executeQuery();
            if(rs.next()) return rs.getDouble(1);
        }catch(Exception e){
            e.printStackTrace();
        }

        return 0;
    }

    public static void addTreasury(double val){
        try(PreparedStatement ps = DatabaseManager.getConnection()
                .prepareStatement("UPDATE state_treasury SET balance = balance + ? WHERE id = 1")){
            ps.setDouble(1, val);
            ps.executeUpdate();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void subTreasury(double val){
        try(PreparedStatement ps = DatabaseManager.getConnection()
                .prepareStatement("UPDATE state_treasury SET balance = GREATEST(0, balance - ?) WHERE id = 1")){
            ps.setDouble(1, val);
            ps.executeUpdate();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
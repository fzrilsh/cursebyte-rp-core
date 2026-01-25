package com.cursebyte.plugin.modules.reputation;

import com.cursebyte.plugin.CursebyteCore;
import com.cursebyte.plugin.database.DatabaseManager;

import java.util.UUID;

public class ReputationRepository {

    public static void init(){
        String sql = """
            CREATE TABLE IF NOT EXISTS reputation (
                uuid TEXT PRIMARY KEY,
                score REAL
            )
        """;

        try(var st = DatabaseManager.getConnection().createStatement()){
            st.execute(sql);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void create(UUID uuid, double def){
        String sql = "INSERT OR IGNORE INTO reputation(uuid, score) VALUES(?, ?)";

        try(var ps = DatabaseManager.getConnection().prepareStatement(sql)){
            ps.setString(1, uuid.toString());
            ps.setDouble(2, def);
            ps.executeUpdate();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static double get(UUID uuid){
        String sql = "SELECT score FROM reputation WHERE uuid = ?";

        try(var ps = DatabaseManager.getConnection().prepareStatement(sql)){
            ps.setString(1, uuid.toString());
            var rs = ps.executeQuery();
            if(rs.next()){
                return rs.getDouble("score");
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        return CursebyteCore.getInstance().getConfig().getDouble("reputation.default", 0.5);
    }

    public static void set(UUID uuid, double value){
        String sql = "UPDATE reputation SET score = ? WHERE uuid = ?";

        try(var ps = DatabaseManager.getConnection().prepareStatement(sql)){
            ps.setDouble(1, value);
            ps.setString(2, uuid.toString());
            ps.executeUpdate();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
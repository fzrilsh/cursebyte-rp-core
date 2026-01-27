package com.cursebyte.plugin.modules.state.treasury;

import java.sql.*;
import java.util.*;
import com.cursebyte.plugin.database.DatabaseManager;

public class TaxEngine {

    public static double getTaxRate(String type){
        try(PreparedStatement ps = DatabaseManager.getConnection()
            .prepareStatement("""
                SELECT rate FROM state_tax_rules 
                WHERE type=? AND active=1
            """)){
            ps.setString(1, type);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) return rs.getDouble(1);
        }catch(Exception e){
            e.printStackTrace();
        }
        return 0;
    }

    public static double applyTax(String type, double amount){
        double rate = getTaxRate(type);
        return amount * rate;
    }
}
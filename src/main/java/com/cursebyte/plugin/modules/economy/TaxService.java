package com.cursebyte.plugin.modules.economy;

import org.bukkit.configuration.file.FileConfiguration;
import com.cursebyte.plugin.CursebyteCore;

public class TaxService {

    public static double getTransferTaxPercent(){
        FileConfiguration cfg = CursebyteCore.getInstance().getConfig();
        return cfg.getDouble("economy.tax.transfer-tax", 0.2);
    }

    public static boolean isEnabled(){
        return CursebyteCore.getInstance()
                .getConfig()
                .getBoolean("economy.tax.enabled", true);
    }

    public static double calculateTax(double amount){
        if(!isEnabled()) return 0;
        
        double percent = getTransferTaxPercent();
        return amount * (percent / 100.0);
    }
}
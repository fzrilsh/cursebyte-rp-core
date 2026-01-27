package com.cursebyte.plugin.modules.state.core;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class StateConfigManager {

    private static File file;
    private static FileConfiguration config;

    public static void init(File dataFolder){
        file = new File(dataFolder, "state.yml");

        if(!file.exists()){
            dataFolder.mkdirs();
            try{
                file.createNewFile();
            }catch(Exception e){
                e.printStackTrace();
            }
        }

        config = YamlConfiguration.loadConfiguration(file);
    }

    public static FileConfiguration get(){
        return config;
    }

    public static void save(){
        try{
            config.save(file);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
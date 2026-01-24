package com.cursebyte.plugin;

import org.bukkit.plugin.java.JavaPlugin;

import com.cursebyte.plugin.command.AppCommand;
import com.cursebyte.plugin.command.ImmigrationCommand;
import com.cursebyte.plugin.database.DatabaseManager;
import com.cursebyte.plugin.economy.EconomyManager;
import com.cursebyte.plugin.immigration.CitizenshipNPCManager;
import com.cursebyte.plugin.listener.AppMenuListener;
import com.cursebyte.plugin.listener.ChatInputListener;
import com.cursebyte.plugin.listener.CitizenListener;
import com.cursebyte.plugin.listener.EconomyListener;
import com.cursebyte.plugin.listener.TransactionListener;
import com.cursebyte.plugin.modules.citizen.CitizenService;
import com.cursebyte.plugin.ui.core.MenuRegistry;
import com.cursebyte.plugin.ui.menus.MainMenu;
import com.cursebyte.plugin.ui.menus.TransferMenu;
import com.cursebyte.plugin.ui.menus.WalletMenu;
import com.cursebyte.plugin.ui.menus.wallet.MutationMenu;

public class CursebyteCore extends JavaPlugin {

    private static CursebyteCore instance;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        instance = this;

        if (!getDataFolder().exists()) {
            getDataFolder().mkdirs();
        }

        DatabaseManager.init(getDataFolder().getAbsolutePath());
        EconomyManager.init();
        CitizenService.init();

        registerMenus();
        registerListeners();
        registerCommands();
        registerTask();

        getLogger().info("Cursebyte Core launched!");
    }

    private void registerMenus() {
        MenuRegistry.register(new MainMenu());
        MenuRegistry.register(new WalletMenu());
        MenuRegistry.register(new MutationMenu());
        MenuRegistry.register(new TransferMenu());
    }

    private void registerTask() {
        getServer().getScheduler().runTaskLater(this, () -> {
            new CitizenshipNPCManager().spawnNPC();
        }, 100L);
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new EconomyListener(), this);
        getServer().getPluginManager().registerEvents(new AppMenuListener(), this);
        getServer().getPluginManager().registerEvents(new TransactionListener(), this);
        getServer().getPluginManager().registerEvents(new ChatInputListener(), this);
        getServer().getPluginManager().registerEvents(new CitizenListener(), this);
    }

    private void registerCommands() {
        getCommand("app").setExecutor(new AppCommand());
        getCommand("setupimmigration").setExecutor(new ImmigrationCommand());
    }

    public static CursebyteCore getInstance() {
        return instance;
    }
}
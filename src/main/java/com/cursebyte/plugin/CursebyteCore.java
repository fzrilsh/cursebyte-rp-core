package com.cursebyte.plugin;

import org.bukkit.plugin.java.JavaPlugin;

import com.cursebyte.plugin.command.AppCommand;
import com.cursebyte.plugin.command.ImmigrationCommand;
import com.cursebyte.plugin.database.DatabaseManager;
import com.cursebyte.plugin.immigration.CitizenshipNPCManager;
import com.cursebyte.plugin.listener.AppMenuListener;
import com.cursebyte.plugin.listener.CitizenListener;
import com.cursebyte.plugin.listener.EconomyListener;
import com.cursebyte.plugin.listener.ReportListener;
import com.cursebyte.plugin.listener.TransactionListener;
import com.cursebyte.plugin.listener.TransferListener;
import com.cursebyte.plugin.modules.citizen.CitizenService;
import com.cursebyte.plugin.modules.economy.EconomyRepository;
import com.cursebyte.plugin.modules.report.ReportRepository;
import com.cursebyte.plugin.modules.reputation.ReputationRepository;
import com.cursebyte.plugin.modules.state.core.StateConfigManager;
import com.cursebyte.plugin.modules.state.core.StateLoader;
import com.cursebyte.plugin.ui.core.MenuRegistry;
import com.cursebyte.plugin.ui.menus.MainMenu;
import com.cursebyte.plugin.ui.menus.ReportMenu;
import com.cursebyte.plugin.ui.menus.TransferMenu;
import com.cursebyte.plugin.ui.menus.WalletMenu;
import com.cursebyte.plugin.ui.menus.report.ReportCrimeMenu;
import com.cursebyte.plugin.ui.menus.report.ReportPlayerMenu;
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
        StateConfigManager.init(getDataFolder());
        EconomyRepository.init();
        CitizenService.init();
        ReputationRepository.init();
        ReportRepository.init();

        registerMenus();
        registerListeners();
        registerCommands();
        registerTask();

        getLogger().info("Cursebyte Core launched!");
    }

    @Override
    public void onDisable() {
        DatabaseManager.close();
    }

    private void registerMenus() {
        MenuRegistry.register(new MainMenu());
        MenuRegistry.register(new WalletMenu());
        MenuRegistry.register(new MutationMenu());
        MenuRegistry.register(new TransferMenu());
        MenuRegistry.register(new ReportMenu());
        MenuRegistry.register(new ReportPlayerMenu());
        MenuRegistry.register(new ReportCrimeMenu());
    }

    private void registerTask() {
        getServer().getScheduler().runTaskLater(this, () -> {
            new CitizenshipNPCManager().spawnNPC();
            StateLoader.load();
        }, 100L);
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new EconomyListener(), this);
        getServer().getPluginManager().registerEvents(new AppMenuListener(), this);
        getServer().getPluginManager().registerEvents(new TransactionListener(), this);
        getServer().getPluginManager().registerEvents(new TransferListener(), this);
        getServer().getPluginManager().registerEvents(new CitizenListener(), this);
        getServer().getPluginManager().registerEvents(new ReportListener(), this);
    }

    private void registerCommands() {
        getCommand("app").setExecutor(new AppCommand());
        getCommand("setupimmigration").setExecutor(new ImmigrationCommand());
    }

    public static CursebyteCore getInstance() {
        return instance;
    }
}
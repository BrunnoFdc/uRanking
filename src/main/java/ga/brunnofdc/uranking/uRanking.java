package ga.brunnofdc.uranking;

import ga.brunnofdc.uranking.commands.Admin;
import ga.brunnofdc.uranking.commands.Rankup;
import ga.brunnofdc.uranking.data.DataManager;
import ga.brunnofdc.uranking.economy.EconomicUnit;
import ga.brunnofdc.uranking.economy.units.VaultMoney;
import ga.brunnofdc.uranking.events.JoinQuitEvent;
import ga.brunnofdc.uranking.hooks.Hook;
import ga.brunnofdc.uranking.hooks.Legendchat;
import ga.brunnofdc.uranking.hooks.UltimateChat;
import ga.brunnofdc.uranking.lib.Metrics;
import ga.brunnofdc.uranking.ranking.RankCacheManager;
import ga.brunnofdc.uranking.ranking.RankUtils;
import ga.brunnofdc.uranking.utils.Language;
import ga.brunnofdc.uranking.utils.SystemDefs;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class uRanking extends JavaPlugin {

    private static final String[] tags = new String[] { "§b[uRanking]§r", "§4[uRanking]§r" };
    private static uRanking instance;
    private EconomicUnit unity;

    public void onEnable() {

        instance = this;

        if(!getDataFolder().exists())
            getDataFolder().mkdir();

        //If has no config, save the default one
        saveDefaultConfig();

        //If config exists, try to copy new defaults that aren't present on actual config
        getConfig().options().copyDefaults(true);
        saveConfig();

        //TODO: Make dynamic
        unity = new VaultMoney();

        SystemDefs.loadProps(getConfig());
        RankUtils.loadRanks(getConfig());
        getLogger().info(RankUtils.getTotalRanks() + " ranks were found and loaded!");

        //Setup data (MySQL, Flatfile, and any other, will be set up and loaded here)
        DataManager.setup(this);
        Language.setup(this);

        //Command registering
        getCommand("rankup").setExecutor(new Rankup());
        getCommand("uranking").setExecutor(new Admin());

        //Hooks and other events
        setupHooks();
        Bukkit.getPluginManager().registerEvents(new JoinQuitEvent(), this);
        Bukkit.getPluginManager().registerEvents(new Rankup(), this);

        Metrics metrics = new Metrics(this, 2122);

    }

    public void onDisable() {

        getLogger().info("Saving data...");
        RankCacheManager.saveAllCachedData();
        getLogger().info("Data has been successfully saved.");
        DataManager.getDataSource().onDisable();

    }

    public void setupHooks() {

        List<Hook> hooks = new ArrayList<>();
        hooks.add(new Legendchat());
        hooks.add(new UltimateChat());

        for(Hook hook : hooks) {
            String relativePlugin = hook.getRelativePlugin();
            if(Bukkit.getPluginManager().isPluginEnabled(relativePlugin)) {
                hook.setupHook(this);
                getLogger().info("Hooked with " + relativePlugin + "!");
            }
        }

    }

    public EconomicUnit getEconomicUnit() {
        return this.unity;
    }

    public static uRanking getInstance() {
        return instance;
    }

    public static String getChatTag(String option) {
        if(option.equalsIgnoreCase("success")) {
            return tags[0];
        } else if(option.equalsIgnoreCase("error")) {
            return tags[1];
        } else {
            return tags[0];
        }
    }

}

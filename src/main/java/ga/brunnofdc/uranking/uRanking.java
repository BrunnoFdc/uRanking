package ga.brunnofdc.uranking;

import ga.brunnofdc.uranking.commands.Admin;
import ga.brunnofdc.uranking.commands.Rankup;
import ga.brunnofdc.uranking.data.DataManager;
import ga.brunnofdc.uranking.economy.EconomicUnit;
import ga.brunnofdc.uranking.economy.units.VaultMoney;
import ga.brunnofdc.uranking.events.JoinQuitEvent;
import ga.brunnofdc.uranking.hooks.*;
import ga.brunnofdc.uranking.hooks.placeholderapi.PlaceholderAPIHook;
import ga.brunnofdc.uranking.lib.Metrics;
import ga.brunnofdc.uranking.ranking.Rank;
import ga.brunnofdc.uranking.ranking.RankCacheManager;
import ga.brunnofdc.uranking.ranking.RankUtils;
import ga.brunnofdc.uranking.utils.Language;
import ga.brunnofdc.uranking.utils.RankMapper;
import ga.brunnofdc.uranking.utils.SystemDefs;
import ga.brunnofdc.uranking.utils.UpdateChecker;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class uRanking extends JavaPlugin {

    public static final String GITHUB_REPO_NAME = "BrunnoFdc/uRanking";
    private static final String[] tags = new String[] { "§b[uRanking]§r", "§4[uRanking]§r" };
    private static uRanking instance;
    private EconomicUnit unity;

    public void onEnable() {

        instance = this;

        setupConfig();

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

        Bukkit.getScheduler().runTaskAsynchronously(this, new UpdateChecker());

    }

    public void onDisable() {

        getLogger().info("Saving data...");
        RankCacheManager.saveAllCachedData();
        getLogger().info("Data has been successfully saved.");
        DataManager.getDataSource().onDisable();

    }

    public void setupConfig() {
        if(!getDataFolder().exists())
            getDataFolder().mkdir();

        //If has no config, save the default one
        saveDefaultConfig();

        //If config exists, try to copy new defaults that aren't present on actual config
        getConfig().options().copyDefaults(true);


        //If there are no ranks set up, then copy the example ones
        if(!getConfig().isSet("Ranks")) {
            ConfigurationSection ranksSection = getConfig().createSection("Ranks");
            List<Rank> exampleRanks = Arrays.asList(
                    new Rank(
                            "Noob",
                            "&f&lNoob&r",
                            "&7[&f&lNoob&r&7]",
                            0,
                            0,
                            Collections.singletonList("pex user @player group add noob")
                    ),
                    new Rank(
                            "Regular",
                            "&a&lRegular&r",
                            "&7[&a&lRegular&r&7]",
                            1,
                            500,
                            Arrays.asList(
                                    "pex user @player group remove noob",
                                    "pex user @player group add regular",
                                    "give @player diamond 5"
                            )
                    ),
                    new Rank(
                            "Pro",
                            "&b&lPro&r",
                            "&7[&b&lPro&r&7]",
                            2,
                            2500,
                            Arrays.asList(
                                    "pex user @player group remove regular",
                                    "pex user @player group add pro",
                                    "give @player diamond 10"
                            )
                    )
            );

            exampleRanks.forEach(rank -> {
                RankMapper.toConfiguraionSection(rank, ranksSection);
            });

        }
        saveConfig();
    }

    public void setupHooks() {

        List<Hook> hooks = new ArrayList<>();
        hooks.add(new Legendchat());
        hooks.add(new UltimateChat());
        hooks.add(new MVdWPlugins());
        hooks.add(new PlaceholderAPIHook());

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

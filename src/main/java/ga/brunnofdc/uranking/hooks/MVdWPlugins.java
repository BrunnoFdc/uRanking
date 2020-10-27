package ga.brunnofdc.uranking.hooks;

import be.maximvdw.placeholderapi.PlaceholderAPI;
import ga.brunnofdc.uranking.ranking.RankCacheManager;
import org.bukkit.plugin.java.JavaPlugin;

public class MVdWPlugins implements Hook {

    public static final String PLACEHOLDER_PREFIX = "uranking_";

    @Override
    public String getRelativePlugin() {
        return "MVdWPlaceholderAPI";
    }

    @Override
    public void setupHook(JavaPlugin plugin) {

        PlaceholderMapper.PLACEHOLDERS.forEach((placeholder, mapper) ->
                PlaceholderAPI.registerPlaceholder(
                    plugin,
                    PLACEHOLDER_PREFIX + placeholder,
                    event -> (
                            mapper.apply(RankCacheManager.getRankedPlayer(event.getPlayer()))
                    )
                )
        );

    }
}

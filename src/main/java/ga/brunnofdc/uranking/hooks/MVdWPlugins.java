package ga.brunnofdc.uranking.hooks;

import be.maximvdw.placeholderapi.PlaceholderAPI;
import ga.brunnofdc.uranking.ranking.Rank;
import ga.brunnofdc.uranking.ranking.RankCacheManager;
import ga.brunnofdc.uranking.ranking.RankUtils;
import ga.brunnofdc.uranking.utils.Language;
import ga.brunnofdc.uranking.utils.enums.SingleLineMessage;
import ga.brunnofdc.uranking.utils.exceptions.MaxRankException;
import ga.brunnofdc.uranking.utils.exceptions.MinRankException;
import org.bukkit.plugin.java.JavaPlugin;

public class MVdWPlugins implements Hook {
    @Override
    public String getRelativePlugin() {
        return "MVdWPlaceholderAPI";
    }

    @Override
    public void setupHook(JavaPlugin plugin) {

        PlaceholderConsumer.DEFAULT_PLACEHOLDERS.forEach((placeholder, consumer) ->
                PlaceholderAPI.registerPlaceholder(
                    plugin,
                    "uranking_" + placeholder,
                    event -> (
                            consumer.getValue(RankCacheManager.getRankedPlayer(event.getPlayer()))
                    )
                )
        );

    }
}

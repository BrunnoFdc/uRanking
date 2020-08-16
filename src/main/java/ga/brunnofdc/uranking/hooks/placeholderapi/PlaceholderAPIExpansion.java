package ga.brunnofdc.uranking.hooks.placeholderapi;

import ga.brunnofdc.uranking.hooks.PlaceholderConsumer;
import ga.brunnofdc.uranking.ranking.RankCacheManager;
import ga.brunnofdc.uranking.ranking.RankedPlayer;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class PlaceholderAPIExpansion extends PlaceholderExpansion {

    private JavaPlugin pluginInstance;

    public void register(JavaPlugin pluginInstance) {
        this.pluginInstance = pluginInstance;
        super.register();
    }

    /**
     * Because this is a internal class, this check is not needed
     * and we can simply return {@code true} to PAPI
     *
     * @return Always true since it's an internal class.
     */
    @Override
    public boolean canRegister() {
        return true;
    }

    /**
     * Because this is an internal class, we need to override this method to
     * let PlaceholderAPI know to not unregister this hook class when PAPI's reloaded
     *
     * @return true to persist through reloads
     */
    @Override
    public boolean persist(){
        return true;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "uranking";
    }

    @Override
    public @NotNull String getAuthor() {
        return pluginInstance.getDescription().getAuthors().get(0);
    }

    @Override
    public @NotNull String getVersion() {
        return pluginInstance.getDescription().getVersion();
    }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String identifier){

        if(player == null){
            return "";
        }

        if(PlaceholderConsumer.DEFAULT_PLACEHOLDERS.containsKey(identifier)) {
            PlaceholderConsumer consumer = PlaceholderConsumer.DEFAULT_PLACEHOLDERS.get(identifier);
            RankedPlayer rankedPlayer = RankCacheManager.getRankedPlayer(player);
            return consumer.getValue(rankedPlayer);
        } else {
            return null;
        }
    }
}

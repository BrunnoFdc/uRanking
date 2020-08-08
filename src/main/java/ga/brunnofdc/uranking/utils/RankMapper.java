package ga.brunnofdc.uranking.utils;

import ga.brunnofdc.uranking.ranking.Rank;
import org.bukkit.configuration.ConfigurationSection;

import java.util.LinkedHashMap;
import java.util.Map;

public class RankMapper {

    public static void toConfiguraionSection(Rank rank, ConfigurationSection ranksSection) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("Display-Name", rank.getName());
        data.put("Chat-Prefix", rank.getPrefix());
        data.put("Price", rank.getPrice());
        data.put("Commands", rank.getCommands());
        ranksSection.createSection(rank.getID(), data);
    }

}

package ga.brunnofdc.uranking.ranking;

import ga.brunnofdc.uranking.utils.exceptions.MaxRankException;
import ga.brunnofdc.uranking.utils.exceptions.MinRankException;
import org.bukkit.configuration.file.FileConfiguration;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class RankUtils {

    private static Map<Integer, Rank> ranksByOrder = new HashMap<>();

    public static void loadRanks(FileConfiguration config) {

        int position = 1;
        for (String rankID : config.getConfigurationSection("Ranks").getKeys(false)) {
            String actualRankPath = "Ranks." + rankID;
            Rank rank = new Rank(
                    rankID,
                    config.getString(actualRankPath + ".Display-Name"),
                    config.getString(actualRankPath + ".Chat-Prefix"),
                    position,
                    config.getDouble(actualRankPath + ".Price"),
                    config.getStringList(actualRankPath + ".Commands")
            );
            ranksByOrder.put(position, rank);

            position++;

        }

    }

    public static int getTotalRanks() {
        return ranksByOrder.size();
    }

    public static Rank getRankByPosition(int position) {
        return ranksByOrder.get(position);
    }

    public static int getPositionByRank(String id) {
        return ranksByOrder.values().stream().filter(
                rank -> rank.getID().equals(id)
        ).findFirst().get().getPosition();
    }

    public static Rank getRankByID(String id) {
        return ranksByOrder.values().stream().filter(
                rank -> rank.getID().equals(id)
        ).findFirst().orElse(null);
    }

    @Nullable
    public static Rank getNextRank(Rank rank) throws MaxRankException {
        if(rank.getPosition() < ranksByOrder.size()) {
            return ranksByOrder.get(rank.getPosition() + 1);
        } else {
            throw new MaxRankException();
        }
    }

    public static Rank getOldRank(Rank rank) throws MinRankException {
        if(rank.getPosition() > 1) {
            return ranksByOrder.get(rank.getPosition() - 1);
        } else {
            throw new MinRankException();
        }
    }

    public static boolean isMaxRank(Rank rank) {
        try {
            return getNextRank(rank) == null;
        } catch (MaxRankException e) {
            return true;
        }
    }

}
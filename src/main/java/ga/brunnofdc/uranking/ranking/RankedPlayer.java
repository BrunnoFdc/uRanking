package ga.brunnofdc.uranking.ranking;

import ga.brunnofdc.uranking.api.PlayerRankupEvent;
import ga.brunnofdc.uranking.data.DataManager;
import ga.brunnofdc.uranking.economy.EconomicUnit;
import ga.brunnofdc.uranking.uRanking;
import ga.brunnofdc.uranking.utils.Language;
import ga.brunnofdc.uranking.utils.StringList;
import ga.brunnofdc.uranking.utils.SystemDefs;
import ga.brunnofdc.uranking.utils.enums.Message;
import ga.brunnofdc.uranking.utils.enums.Property;
import ga.brunnofdc.uranking.utils.enums.SingleLineMessage;
import ga.brunnofdc.uranking.utils.exceptions.MaxRankException;
import ga.brunnofdc.uranking.utils.exceptions.MinRankException;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.UUID;

public class RankedPlayer {

    private Player player;
    private Rank rank;
    private UUID playerUUID;

    public RankedPlayer(Player player) {
        this.player = player;
        this.playerUUID = player.getUniqueId();
        DataManager.getDataSource().exists(player, (result) -> {
            if(result) {
                DataManager.getDataSource().read(player, (rank) -> {
                    this.rank = rank;
                });
            } else {
                rankUp(RankUtils.getRankByPosition(1), false, false);
            }
        });

    }

    public Player getPlayer() {
        return player;
    }

    public Rank getRank() {
        return rank;
    }

    public void setRank(Rank rank) {
        this.rank = rank;
    }

    /**
     * Run all the routines related to a rank up, and save the data.
     * @param sendMessages If messages (broadcast and PM) should be sent.
     * @param takeMoney If the price of new rank should be taken of player's money.
     */
    public void rankUp(boolean sendMessages, boolean takeMoney) {
        Rank nextRank;
        try {
            nextRank = RankUtils.getNextRank(rank);
        } catch (MaxRankException e) {
            player.sendMessage(translateRankupVariables(Language.getMessage(Message.ALREADY_MAX_RANK), this).toArray());
            return;
        }
        rankUp(nextRank, sendMessages, takeMoney);

    }

    /**
     * Same as the another one, but a specific rank will be set, instead of the next rank of player.
     * @param sendMessages If messages (broadcast and PM) should be sent.
     * @param takeMoney If the price of new rank should be taken of player's money.
     */
    public void rankUp(Rank specific, boolean sendMessages, boolean takeMoney) {

        EconomicUnit unit = uRanking.getInstance().getEconomicUnit();
       
        if(takeMoney) {
            if(unit.has(player, specific.getPrice())) {
                unit.withdraw(player, specific.getPrice());
            } else {
                player.sendMessage(translateRankupVariables(Language.getMessage(Message.NO_MONEY), this).toArray());
                return;
            }
        }

        if(sendMessages) {
            Property[] messageRoutine;
            Message[] messageFormat;

            if(RankUtils.isMaxRank(specific)) {
                messageRoutine = new Property[] {Property.PM_MAXRANK, Property.BROADCAST_MAXRANK};
                messageFormat = new Message[] {Message.RANKUP_MAX_PM, Message.RANKUP_MAX_ANNOUNCE};
            } else {
                messageRoutine = new Property[] {Property.PM_RANKUP, Property.BROADCAST_RANKUP};
                messageFormat = new Message[] {Message.RANKUP_PM, Message.RANKUP_ANNOUNCE};
            }

            if(SystemDefs.getProp(messageRoutine[0])) {
                player.sendMessage(
                        translateRankupVariables(Language.getMessage(messageFormat[0]), this).toArray()
                );
            }

            if(SystemDefs.getProp(messageRoutine[1])) {
                translateRankupVariables(Language.getMessage(messageFormat[1]), this)
                        .forEach(Bukkit::broadcastMessage);
            }

            if(SystemDefs.getProp(Property.SEND_TITLE_ENABLE)) {
                ConfigurationSection titleSec = uRanking.getInstance().getConfig().getConfigurationSection("System-Behaviour.Send-Title");
                String title = ChatColor.translateAlternateColorCodes('&', titleSec.getString("Title-Line"))
                        .replace("@rank", specific.getName())
                        .replace("@player", player.getName());
                String subtitle = ChatColor.translateAlternateColorCodes('&', titleSec.getString("Subtitle-Line"))
                        .replace("@rank", specific.getName())
                        .replace("@player", player.getName());
                if(SystemDefs.getProp(Property.SEND_TITLE_GLOBAL)) {
                    Bukkit.getOnlinePlayers().forEach(p -> p.sendTitle(title, subtitle));
                } else {
                    player.sendTitle(title, subtitle);
                }
            }


        }
        this.rank = specific;
        this.rank.getCommands().forEach(cmd -> {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd
                    .replace("@player", this.player.getName())
                    .replace("@rank", this.rank.getID())
                    .replace("@price", String.valueOf(this.rank.getPrice()))
            );
        });

        DataManager.getDataSource().set(player.getUniqueId(), rank.getID());
        Bukkit.getPluginManager().callEvent(new PlayerRankupEvent(this));
    }

    public static StringList translateRankupVariables(StringList message, RankedPlayer player) {
        Rank nextRank;
        Rank oldRank;
        try {
            nextRank = RankUtils.getNextRank(player.getRank());
        } catch (MaxRankException e) {
            nextRank = null;
        }

        try {
            oldRank = RankUtils.getOldRank(player.getRank());
        } catch (MinRankException e) {
            oldRank = null;
        }

        EconomicUnit unit = uRanking.getInstance().getEconomicUnit();
        double playerBalance = unit.getBalance(player.getPlayer());

        String oldRankName = (oldRank != null) ? oldRank.getName() : Language.getSingleLineMessage(SingleLineMessage.NO_OLD_RANK);
        String nextPrice = Language.getSingleLineMessage(SingleLineMessage.NO_NEXT_RANK),
                nextRankName = Language.getSingleLineMessage(SingleLineMessage.NO_NEXT_RANK),
                remaining = String.valueOf(playerBalance);

        if(nextRank != null) {
            if(unit.has(player.getPlayer(), nextRank.getPrice())) {
                remaining = String.valueOf(unit.getBalance(player.getPlayer()) - nextRank.getPrice());
            }
            nextPrice = String.valueOf(nextRank.getPrice());
            nextRankName = nextRank.getName(); ;
        }

        return message
                .replace("@price", nextPrice)
                .replace("@remaining", remaining)
                .replace("@balance", String.valueOf(uRanking.getInstance().getEconomicUnit().getBalance(player.getPlayer())))
                .replace("@player", player.getPlayer().getName())
                .replace("@rank", player.getRank().getName())
                .replace("@nextrank", nextRankName)
                .replace("@oldrank", oldRankName);

    }

    public static String translateRankupVariables(String message, RankedPlayer player) {
        Rank nextRank;
        Rank oldRank;
        try {
            nextRank = RankUtils.getNextRank(player.getRank());
        } catch (MaxRankException e) {
            nextRank = null;
        }

        try {
            oldRank = RankUtils.getOldRank(player.getRank());
        } catch (MinRankException e) {
            oldRank = null;
        }

        String nextPrice = (nextRank != null) ? String.valueOf(nextRank.getPrice()) : Language.getSingleLineMessage(SingleLineMessage.NO_NEXT_RANK);
        String nextRankName = (nextRank != null) ? nextRank.getName() : Language.getSingleLineMessage(SingleLineMessage.NO_NEXT_RANK);
        String oldRankName = (oldRank != null) ? oldRank.getName() : Language.getSingleLineMessage(SingleLineMessage.NO_OLD_RANK);

        return message
                .replace("@price", nextPrice)
                .replace("@balance", String.valueOf(uRanking.getInstance().getEconomicUnit().getBalance(player.getPlayer())))
                .replace("@player", player.getPlayer().getName())
                .replace("@rank", player.getRank().getName())
                .replace("@nextrank", nextRankName)
                .replace("@oldrank", oldRankName);

    }

    public UUID getPlayerUUID() {
        return playerUUID;
    }
}

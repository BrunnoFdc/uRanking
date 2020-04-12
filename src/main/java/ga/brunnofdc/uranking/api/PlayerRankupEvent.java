package ga.brunnofdc.uranking.api;

import ga.brunnofdc.uranking.ranking.Rank;
import ga.brunnofdc.uranking.ranking.RankedPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerRankupEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private Player player;
    private Rank rank;

    public PlayerRankupEvent(RankedPlayer rankedPlayer) {

        this.player = rankedPlayer.getPlayer();
        this.rank = rankedPlayer.getRank();

    }

    public Player getPlayer() {
        return player;
    }

    public String getRankID() {
        return rank.getID();
    }

    public String getRankDisplayname() {
        return rank.getName();
    }

    public String getRankTag() {
        return rank.getPrefix();
    }

    public int getRankPosition() {
        return rank.getPosition();
    }

    public double getRankPrice() {
        return rank.getPrice();
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

}

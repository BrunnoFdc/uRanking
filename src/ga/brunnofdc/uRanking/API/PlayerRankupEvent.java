package ga.brunnofdc.uRanking.API;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public final class PlayerRankupEvent extends Event {
	
  private Player p;
  private String rank;
  private String oldrank;
  private String tag;
  private double preco;
  private static final HandlerList handlers = new HandlerList();
  
  public PlayerRankupEvent(Player player, String r, String or, String t, double p) {
	  
    this.p = player;
    this.rank = r;
    this.oldrank = or;
    this.tag = t;
    this.preco = p;
    
  }
  
  public HandlerList getHandlers() {
  
	  return handlers;
  
  }
  
  public static HandlerList getHandlerList() {
  
	  return handlers;
  
  }
  
  public Player getPlayer() {
  
	  return this.p;
  
  }
  
  public String getRank() {
    return this.rank;
  
  }
  
  public String getRankName() {
	
	  return oldrank;
  
  }
  
  public String oldRank() {
	  
    return this.oldrank;
  
  }
  
  public String getTag() {
  
	  return this.tag;
  
  }
  
  public double getPreco() {
	  
    return this.preco;
  
  }

}

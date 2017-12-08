package ga.brunnofdc.uRanking.Comandos;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import ga.brunnofdc.uRanking.Main;
import ga.brunnofdc.uRanking.Core.LocaleManager;
import ga.brunnofdc.uRanking.Core.RankManager;
import ga.brunnofdc.uRanking.Core.Player.PlayerInfo;

public class Ranks extends Command {

	public Ranks(String name) {
		super(name);
		try {
			
		    final Field commandMapField = Bukkit.getServer().getClass().getDeclaredField("commandMap");
		    commandMapField.setAccessible(true);
		    CommandMap commandMap = (CommandMap) commandMapField.get(Bukkit.getServer());
		    commandMap.register("ranks", this);
		    
		} catch (NoSuchFieldException  | IllegalArgumentException | IllegalAccessException exception){
		   
			exception.printStackTrace();
		
		}
	}
	

	 @SuppressWarnings("deprecation")
	private ItemStack parseString(String itemId) {
	    ItemStack item = null;
	    if(itemId.contains(":")) {
	      String[] parts = itemId.split(":");
	      int matId = Integer.parseInt(parts[0]);
	      
	      if (parts.length == 2) {
	        short data = Short.parseShort(parts[1]);
	        item = new ItemStack(Material.getMaterial(matId), 1, data);
	      }
	      
	    } else {
	      
	    	int matId = Integer.parseInt(itemId);
	    	item = new ItemStack(Material.getMaterial(matId));
	    }
	    
	    return item;
	  
	 }

	@Override
	public boolean execute(CommandSender sender, String command, String[] args) {

		if(command.equalsIgnoreCase("ranks")) {
			
			if(sender instanceof Player) {
				
				Player p = (Player)sender;
				
				if(!p.hasPermission("uranking.ranks")) {
					
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', LocaleManager.getMensagens().getString("Sem-Permissao")));
					return true;
					
				}
				
				if(!RankManager.PLAYER_RANKS.containsKey(p.getUniqueId())) {
					
					p.sendMessage("§cNão foi possível encontrar seu rank, por favor, relogue!");
					return true;
					
				}
				
				PlayerInfo pInfo = new PlayerInfo(p.getUniqueId());
				FileConfiguration config = Main.plugin.getConfig(); 
				Inventory inv = Bukkit.createInventory(p, 54, config.getString("GUI.Nome")
						.replace("&", "§")
						.replace("@player", p.getName())
						.replace("@playerrank", pInfo.getPlayerRank().getRankName())
						.replace("@dinheiro", String.valueOf(Main.economy.getBalance(p))));
				
				 Set<String> s = config.getConfigurationSection("Ranks").getKeys(false);
			     String[] ranks = s.toArray(new String[s.size()]);
				
				for(int i = 0; i < ranks.length; i++) {
					
					ItemStack item = parseString(config.getString("Ranks." + ranks[i] + ".ItemID"));
					ItemMeta meta = item.getItemMeta();
					List<String> lore = new ArrayList<String>();
					meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', config.getString("GUI.ItemNome")
							.replace("@player", p.getName())
							.replace("@rank", config.getString("Ranks." + ranks[i] + ".Nome"))
							.replace("@playerrank", pInfo.getPlayerRank().getRankName())
							.replace("@preco", String.valueOf(config.getDouble("Ranks." + ranks[i] + ".Preco")))
							.replace("@dinheiro", String.valueOf(Main.economy.getBalance(p)))));
					String rankNome = ranks[i];
					config.getStringList("GUI.ItemLore").forEach(r -> lore.add(ChatColor.translateAlternateColorCodes('&', r
							.replace("@player", p.getName())
							.replace("@rank", config.getString("Ranks." + rankNome + ".Nome"))
							.replace("@playerrank", pInfo.getPlayerRank().getRankName())
							.replace("@preco", String.valueOf(config.getDouble("Ranks." + rankNome + ".Preco")))
							.replace("@dinheiro", String.valueOf(Main.economy.getBalance(p))))));
					meta.setLore(lore);
					item.setItemMeta(meta);
				    inv.setItem(i, item);
				}
				
				p.openInventory(inv);
				
			} else {
				
				sender.sendMessage("§cEsse comando só pode ser executado por players!");
				
			}
		}
		
		return false;
	
	}
}

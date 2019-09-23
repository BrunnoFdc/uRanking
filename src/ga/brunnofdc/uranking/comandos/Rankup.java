package ga.brunnofdc.uranking.comandos;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import ga.brunnofdc.uranking.Main;
import ga.brunnofdc.uranking.api.PlayerRankupEvent;
import ga.brunnofdc.uranking.core.LocaleManager;
import ga.brunnofdc.uranking.core.Rank;
import ga.brunnofdc.uranking.core.RankManager;
import ga.brunnofdc.uranking.core.player.PlayerInfo;

@SuppressWarnings("deprecation")
public class Rankup implements CommandExecutor {

	static FileConfiguration msg = LocaleManager.getMensagens();
	static Main jp = Main.plugin;
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
	
		if(command.getName().equalsIgnoreCase("rankup")) {
			
			if(!(sender instanceof Player)) {
				
				sender.sendMessage("§cEsse comando só pode ser executado por players!");
				return true;
				
			}
			
			Player p = (Player)sender;
			
			if(!RankManager.PLAYER_RANKS.containsKey(p.getUniqueId())) {
				
				p.sendMessage("§cNão foi possível encontrar seu rank, por favor, relogue!");
				return true;
			}
			
			//Verifica se o player tem permissão
			if(!(p.hasPermission("uranking.rankup"))) {
				
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', msg.getString("Sem-Permissao")));
				return true;
			
			}
			
			
			PlayerInfo pInfo = new PlayerInfo(p.getUniqueId());
			Rank pRank = pInfo.getPlayerRank();
			Rank pNewRank = pInfo.getNextRank();

			if(pRank == null) {
				
				p.sendMessage("§cNão foi possível encontrar seu rank, por favor, relogue!");
				return true;
				
			}
			
			//Verifica se existe um proximo rank
			if(pNewRank == null) {
				
				msg.getStringList("Rank-Maximo").stream().forEach(r -> p.sendMessage(ChatColor.translateAlternateColorCodes('&', 
						r.replace("@rank", pRank.getRankName())
						.replace("@player", p.getName()))));
				return true;
							
			}
			
			if(Main.USE_UPGUI) {
				
				openGUI(p, pInfo, pRank, pNewRank);
				
				
			} else {
				
				if(upPlayer(p, pInfo, pRank, pNewRank)) { 
					
					return true;
					
				}
				
			}
			
			
			
		}
		
		return false;
		
	}
	
	public void openGUI(Player p, PlayerInfo pInfo, Rank pRank, Rank pNewRank) {
		
		Inventory inv = Bukkit.createInventory(p, 27, Main.plugin.getConfig().getString("Rankup-GUI.Nome")
				.replace("&", "§"));
		
		ItemStack itemCancelar = stackByString(jp.getConfig().getString("Rankup-GUI.Item_Cancelar.ID"));
		ItemMeta cancelMeta = itemCancelar.getItemMeta();
		cancelMeta.setDisplayName(jp.getConfig().getString("Rankup-GUI.Item_Cancelar.Nome")
				.replace("&", "§")
				.replace("@rank", pNewRank.getRankName())
				.replace("@atual", pRank.getRankName())
				.replace("@preco", String.valueOf(pNewRank.getPreco()))
				.replace("@restante", String.valueOf(Main.economy.getBalance(p.getName()) - pNewRank.getPreco()))
				.replace("@dinheiro", String.valueOf(Main.economy.getBalance(p.getName()))));
		List<String> cancelLore = new ArrayList<>();
		jp.getConfig().getStringList("Rankup-GUI.Item_Cancelar.Lore").forEach(r -> cancelLore.add(r
				.replace("&", "§")
				.replace("@rank", pNewRank.getRankName())
				.replace("@atual", pRank.getRankName())
				.replace("@preco", String.valueOf(pNewRank.getPreco()))
				.replace("@restante", String.valueOf(Main.economy.getBalance(p.getName()) - pNewRank.getPreco()))
				.replace("@dinheiro", String.valueOf(Main.economy.getBalance(p.getName())))));
		cancelMeta.setLore(cancelLore);
		itemCancelar.setItemMeta(cancelMeta);
		
		
		ItemStack itemInfo;
		if(jp.getConfig().getBoolean("Rankup-GUI.Item_Rank.Cabeca.Modo-Cabeca")) {
			
			itemInfo = new ItemStack(Material.SKULL_ITEM, 1 , (short) 3);
			SkullMeta infoMeta = (SkullMeta)itemInfo.getItemMeta();
			
			String nome = jp.getConfig().getString("Rankup-GUI.Item_Rank.Cabeca.Cabeca"); 
			
			if(nome.equalsIgnoreCase("Jogador")) {
				
				infoMeta.setOwner(p.getName());
			
			} else {
				
				 infoMeta.setOwner(nome);
				
			}
			
			
			infoMeta.setDisplayName(jp.getConfig().getString("Rankup-GUI.Item_Rank.Nome")
					.replace("&", "§")
					.replace("@rank", pNewRank.getRankName())
					.replace("@atual", pRank.getRankName())
					.replace("@preco", String.valueOf(pNewRank.getPreco()))
					.replace("@restante", String.valueOf(Main.economy.getBalance(p.getName()) - pNewRank.getPreco()))
					.replace("@dinheiro", String.valueOf(Main.economy.getBalance(p.getName()))));
			List<String> infoLore = new ArrayList<>();
			jp.getConfig().getStringList("Rankup-GUI.Item_Rank.Lore").forEach(r -> infoLore.add(r
					.replace("&", "§")
					.replace("@rank", pNewRank.getRankName())
					.replace("@atual", pRank.getRankName())
					.replace("@preco", String.valueOf(pNewRank.getPreco()))
					.replace("@restante", String.valueOf(Main.economy.getBalance(p.getName()) - pNewRank.getPreco()))
					.replace("@dinheiro", String.valueOf(Main.economy.getBalance(p.getName())))));
			infoMeta.setLore(infoLore);
			itemInfo.setItemMeta(infoMeta);
			
		} else {
			
			itemInfo = stackByString(jp.getConfig().getString("Rankup-GUI.Item_Rank.ID"));
			ItemMeta infoMeta = itemInfo.getItemMeta();
			
			infoMeta.setDisplayName(jp.getConfig().getString("Rankup-GUI.Item_Rank.Nome")
					.replace("&", "§")
					.replace("@rank", pNewRank.getRankName())
					.replace("@atual", pRank.getRankName())
					.replace("@preco", String.valueOf(pNewRank.getPreco()))
					.replace("@restante", String.valueOf(Main.economy.getBalance(p.getName()) - pNewRank.getPreco()))
					.replace("@dinheiro", String.valueOf(Main.economy.getBalance(p.getName()))));
			List<String> infoLore = new ArrayList<>();
			jp.getConfig().getStringList("Rankup-GUI.Item_Rank.Lore").forEach(r -> infoLore.add(r
					.replace("&", "§")
					.replace("@rank", pNewRank.getRankName())
					.replace("@atual", pRank.getRankName())
					.replace("@preco", String.valueOf(pNewRank.getPreco()))
					.replace("@restante", String.valueOf(Main.economy.getBalance(p.getName()) - pNewRank.getPreco()))
					.replace("@dinheiro", String.valueOf(Main.economy.getBalance(p.getName())))));
			infoMeta.setLore(infoLore);
			itemInfo.setItemMeta(infoMeta);
			
		}
		
		ItemStack itemUP = stackByString(jp.getConfig().getString("Rankup-GUI.Item_UP.ID"));
		ItemMeta upMeta = itemUP.getItemMeta();
		upMeta.setDisplayName(jp.getConfig().getString("Rankup-GUI.Item_UP.Nome")
				.replace("&", "§")
				.replace("@rank", pNewRank.getRankName())
				.replace("@atual", pRank.getRankName())
				.replace("@preco", String.valueOf(pNewRank.getPreco()))
				.replace("@restante", String.valueOf(Main.economy.getBalance(p.getName()) - pNewRank.getPreco()))
				.replace("@dinheiro", String.valueOf(Main.economy.getBalance(p.getName()))));
		List<String> upLore = new ArrayList<>();
		jp.getConfig().getStringList("Rankup-GUI.Item_UP.Lore").forEach(r -> upLore.add(r
				.replace("&", "§")
				.replace("@rank", pNewRank.getRankName())
				.replace("@atual", pRank.getRankName())
				.replace("@preco", String.valueOf(pNewRank.getPreco()))
				.replace("@restante", String.valueOf(Main.economy.getBalance(p.getName()) - pNewRank.getPreco()))
				.replace("@dinheiro", String.valueOf(Main.economy.getBalance(p.getName())))));
		upMeta.setLore(upLore);
		itemUP.setItemMeta(upMeta);
		
		inv.setItem(9, itemCancelar);
		inv.setItem(13, itemInfo);
		inv.setItem(17, itemUP);
		
		p.openInventory(inv);
		
	}
	
	private ItemStack stackByString(String itemId) {
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
	
	public static boolean upPlayer(Player p, PlayerInfo pInfo, Rank pRank, Rank pNewRank) { 
		
		double rankPreco = pNewRank.getPreco();
		String oldRank = pRank.getRankName();
		//Verifica se o player tem dinheiro, e se tiver, remove
		if(Main.economy.has(p.getName(), rankPreco)) {
			
			Main.economy.withdrawPlayer(p.getName(), rankPreco);
			
		} else {
			
			msg.getStringList("Sem-Money").stream().forEach(r -> p.sendMessage(ChatColor.translateAlternateColorCodes('&', r
					.replace("@preco", String.valueOf(rankPreco))
					.replace("@player", p.getName())
					.replace("@dinheiro", String.valueOf(Main.economy.getBalance(p)))
					.replace("@rank", pNewRank.getRankName())
					.replace("@oldrank", pInfo.getPlayerRank().getRankName()))));
			return true;
			
		}

		//Verifica se o titulo está ativado
		if(jp.getConfig().getBoolean("Title.Usar")) {
		
			if(jp.getConfig().getInt("Title.Modo") == 1) {
				
				String title = ChatColor.translateAlternateColorCodes('&', jp.getConfig().getString("Title.Titulo")
						.replace("@rank", pNewRank.getRankName())
						.replace("@oldrank", pInfo.getPlayerRank().getRankName())
						.replace("@player", p.getName())
						.replace("@preco", String.valueOf(rankPreco))
						.replace("@dinheiro", String.valueOf(Main.economy.getBalance(p))));
				String sub = ChatColor.translateAlternateColorCodes('&', jp.getConfig().getString("Title.Subtitulo")
						.replace("@rank", pNewRank.getRankName())
						.replace("@oldrank", pInfo.getPlayerRank().getRankName())
						.replace("@player", p.getName())
						.replace("@preco", String.valueOf(rankPreco))
						.replace("@dinheiro", String.valueOf(Main.economy.getBalance(p))));	
			
				p.sendTitle(title, sub);
				
			} else if(jp.getConfig().getInt("Title.Modo") == 2) {
				
				for(Player all : Bukkit.getOnlinePlayers()) {
				
					String title = ChatColor.translateAlternateColorCodes('&', jp.getConfig().getString("Title.Titulo")
						.replace("@rank", pNewRank.getRankName())
						.replace("@oldrank", pInfo.getPlayerRank().getRankName())
						.replace("@player", p.getName())
						.replace("@preco", String.valueOf(rankPreco))
						.replace("@dinheiro", String.valueOf(Main.economy.getBalance(p))));
					String sub = ChatColor.translateAlternateColorCodes('&', jp.getConfig().getString("Title.Subtitulo")
						.replace("@rank", pNewRank.getRankName())
						.replace("@oldrank", pInfo.getPlayerRank().getRankName())
						.replace("@player", p.getName())
						.replace("@preco", String.valueOf(rankPreco))
						.replace("@dinheiro", String.valueOf(Main.economy.getBalance(p))));
				
					all.sendTitle(title, sub);
				}
				
			}
			
		}
		
		//Verifica se a opção de soltar foguete está ativada
		if(jp.getConfig().getBoolean("Soltar-Foguete")) {
		
			p.getWorld().spawn(p.getLocation(), Firework.class);
		
		}
					
		//Seta o novo rank para o player
		new RankManager().updateRank(p, pNewRank.getRankId());
		
		//Verifica se o "anuncio" está ativado
		if(jp.getConfig().getBoolean("Anuncio")) {
			
			//Se estiver, verifica se o anuncio especial de rank máximo está ativado
			//E também se o novo rank do player é o último
			if((jp.getConfig().getBoolean("Anuncio-Rank-Maximo")) && (pInfo.getNextRank() == null)) {
				
				msg.getStringList("Anuncio-Rank-Maximo").stream().forEach(r -> Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', r
						.replace("@rank", pNewRank.getRankName())
						.replace("@oldrank", oldRank)
						.replace("@player", p.getName())
						.replace("@preco", String.valueOf(rankPreco))
						.replace("@dinheiro", String.valueOf(Main.economy.getBalance(p))))));	
				
			} else {
			
				msg.getStringList("Anuncio").stream().forEach(r -> Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', r
						.replace("@rank", pNewRank.getRankName())
						.replace("@oldrank", oldRank)
						.replace("@player", p.getName())
						.replace("@preco", String.valueOf(rankPreco))
						.replace("@dinheiro", String.valueOf(Main.economy.getBalance(p))))));	
				
			}
		
			
		} else {
			
			if((jp.getConfig().getBoolean("Anuncio-Rank-Maximo")) && (pInfo.getNextRank() == null)) {
				
				msg.getStringList("Anuncio-Rank-Maximo").stream().forEach(r -> Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', r
						.replace("@rank", pNewRank.getRankName())
						.replace("@oldrank", oldRank)
						.replace("@player", p.getName())
						.replace("@preco", String.valueOf(rankPreco))
						.replace("@dinheiro", String.valueOf(Main.economy.getBalance(p))))));	
				
			}
			
		}
		
		if(jp.getConfig().getBoolean("Enviar-Mensagem")) {
			
			msg.getStringList("Mensagem-Upou-Com-Sucesso").stream().forEach(r -> p.sendMessage(ChatColor.translateAlternateColorCodes('&', r
					.replace("@rank", pNewRank.getRankName())
					.replace("@oldrank", oldRank)
					.replace("@player", p.getName())
					.replace("@preco", String.valueOf(rankPreco))
					.replace("@dinheiro", String.valueOf(Main.economy.getBalance(p))))));
			
		}
		
		
		
		//Chama o evento da API
		Bukkit.getPluginManager().callEvent(new PlayerRankupEvent(p, pInfo.getPlayerRank().getRankName(), oldRank, pInfo.getPlayerRank().getTag(), rankPreco));		
		
		return false;
	
	}

}

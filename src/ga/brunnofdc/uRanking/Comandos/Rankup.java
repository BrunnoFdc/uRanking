package ga.brunnofdc.uRanking.Comandos;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;

import ga.brunnofdc.uRanking.Main;
import ga.brunnofdc.uRanking.API.PlayerRankupEvent;
import ga.brunnofdc.uRanking.Core.LocaleManager;
import ga.brunnofdc.uRanking.Core.Rank;
import ga.brunnofdc.uRanking.Core.RankManager;
import ga.brunnofdc.uRanking.Core.Player.PlayerInfo;
import ga.brunnofdc.uRanking.Util.Title;

@SuppressWarnings("deprecation")
public class Rankup implements CommandExecutor {

	FileConfiguration msg = LocaleManager.getMensagens();
	Main jp = Main.plugin;
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
	
		if(command.getName().equalsIgnoreCase("rankup")) {
			
			if(!(sender instanceof Player)) {
				
				sender.sendMessage("§cEsse comando só pode ser executado por players!");
				return true;
				
			}
			
			Player p = (Player)sender;
			
			if(!RankManager.PLAYER_RANKS.containsKey(p.getUniqueId())) {
				
				p.sendMessage("§cNão foi possível encontrar seu rank, por favor, relogue!");
			}
			
			//Verifica se o player tem permissão
			if(!(p.hasPermission("uranking.rankup"))) {
				
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', msg.getString("Sem-Permissao")));
				return true;
			
			}
			
			
			PlayerInfo pInfo = new PlayerInfo(p.getUniqueId());
			final String oldRank = pInfo.getPlayerRank().getRankName();
			Rank pRank = pInfo.getPlayerRank();
			Rank pNewRank = pInfo.getNextRank();

			if(pRank == null) {
				
				p.sendMessage("§4Não foi possível encontrar seu rank atual. Tente relogar para resolver!");
				return true;
				
			}
			
			//Verifica se existe um proximo rank
			if(pNewRank == null) {
				
				msg.getStringList("Rank-Maximo").stream().forEach(r -> p.sendMessage(ChatColor.translateAlternateColorCodes('&', 
						r.replace("@rank", pRank.getRankName())
						.replace("@player", p.getName()))));
				return true;
							
			}
			
			
			double rankPreco = pNewRank.getPreco();
			
			//Verifica se o player têm dinheiro, e se tiver, remove
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
				
					Title.sendTitleSubTitle(p, title, sub);
				
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
					
					Title.sendTitleSubTitle(all, title, sub);
					
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
			
		}
		
		return false;
		
	}

}

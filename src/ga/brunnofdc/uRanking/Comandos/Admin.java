package ga.brunnofdc.uRanking.Comandos;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import ga.brunnofdc.uRanking.Main;
import ga.brunnofdc.uRanking.Core.LocaleManager;
import ga.brunnofdc.uRanking.Core.Rank;
import ga.brunnofdc.uRanking.Core.RankManager;
import ga.brunnofdc.uRanking.Core.Player.PlayerInfo;

public class Admin implements CommandExecutor {
	
	Main jp;
	 
	FileConfiguration msg = LocaleManager.getMensagens();
	private String TAG_DEFAULT = "§b[uRanking]§f ";
	private String TAG_ERROR = "§c[uRanking]§4 ";
	public Admin(Main m) {
		
		jp = m;
		
	}
	
	//TODO: Refazer essa zona
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		if(command.getName().equalsIgnoreCase("uranking")) {
			
			if(!sender.hasPermission("uranking.admin")) {
				
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg.getString("Sem-Permissao")));
				return true;
				
			}
			if(args.length > 0) {
				
				if(args[0].equalsIgnoreCase("reload")) {
					
					jp.reloadConfig();
					RankManager.PLAYER_RANKS.clear();
					Main.setupRanks(sender);
					LocaleManager.reload();
					sender.sendMessage(TAG_DEFAULT + "Configurações recarregadas com sucesso!");
					sender.sendMessage(TAG_DEFAULT + "Todos os jogadores precisam relogar para que os ranks sejam setados novamente!");
					
				}
				
				
				if(args[0].equalsIgnoreCase("setar")) {
					
					if(args.length != 3) {
						
						sender.sendMessage(TAG_ERROR + "§4Uso correto: /uranking setar <nome> <rank>");
						return true;
						
					}
					
					Player p = Bukkit.getPlayer(args[1]);
					
					if(p == null) {
						
						sender.sendMessage(TAG_ERROR + "§4O player especificado não foi encontrado!");
						return true;
						
					}
					
					Rank newRank = new RankManager().getRankById(args[2]);
					if(newRank == null) {
						
						sender.sendMessage(TAG_ERROR + "§4O rank especificado não foi encontrado!");
						return true;
					}
					
					
					//Seta o novo rank para o player
					new RankManager().updateRank(p, newRank.getRankId());
					
					sender.sendMessage(TAG_DEFAULT + "§fO rank do jogador foi setado com sucesso!");					
					return true;
					
				}
				
				if(args[0].equalsIgnoreCase("upar")) {
					
					if(args.length != 2) {
						
						sender.sendMessage(TAG_ERROR + "§4Uso correto: /uranking upar <nome>");
						return true;
						
					}
					
					Player p = Bukkit.getPlayer(args[1]);
					if(p == null) {
						
						sender.sendMessage(TAG_ERROR + "§4O player especificado não foi encontrado!");
						return true;
						
					}
					
					//Verifica se o player tem permissão
					if(!(p.hasPermission("uranking.rankup"))) {
						
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', msg.getString("Sem-Permissao")));
						return true;
					
					}
					
					if(!RankManager.PLAYER_RANKS.containsKey(p.getUniqueId())) {
						
						p.sendMessage("§cNão foi possÍvel encontrar seu rank, por favor, relogue!");
						return true;
						
					}
					
					PlayerInfo pInfo = new PlayerInfo(p.getUniqueId());
					Rank pRank = pInfo.getPlayerRank();
					Rank pNewRank = pInfo.getNextRank();

					Rankup.upPlayer(p, pInfo, pRank, pNewRank);
					sender.sendMessage(TAG_DEFAULT + "§fO rank do jogador foi upado com sucesso!");
					return true;
					
				}
				
				if(args[0].equalsIgnoreCase("forcar")) {
					
					if(args.length != 3) {
						
						sender.sendMessage(TAG_ERROR + "§4Uso correto: /uranking forcar <nome> <rank>");
						return true;
						
					}
					
					Player p = Bukkit.getPlayer(args[1]);
					
					if(p == null) {
						
						sender.sendMessage(TAG_ERROR + "§4O player especificado não foi encontrado!");
						return true;
						
					}
					
					if(!RankManager.PLAYER_RANKS.containsKey(p.getUniqueId())) {
						
						p.sendMessage("§cNão foi possível encontrar seu rank, por favor, relogue!");
						return true;
						
					}
					
					PlayerInfo pInfo = new PlayerInfo(p.getUniqueId());
					Rank pNewRank = new RankManager().getRankById(args[2]);
					if(pNewRank == null) {
						
						sender.sendMessage(TAG_ERROR + "§4O rank especificado não foi encontrado!");
						return true;
					
					}
					
					Rankup.upPlayer(p, pInfo, pInfo.getPlayerRank(), pNewRank);
					sender.sendMessage(TAG_DEFAULT + "§fO jogador foi forçado a subir para o rank especificado com sucesso!");
					return true;
				}
				
				if(args[0].equalsIgnoreCase("reset")) {
					
					if(args.length != 2) {
						
						sender.sendMessage(TAG_ERROR + "§4Uso correto: /uranking reset <nome>");
						return true;
						
					}
					
					Player p = Bukkit.getPlayer(args[1]);
					
					if(p == null) {
						
						sender.sendMessage(TAG_ERROR + "§4O player especificado não foi encontrado!");
						return true;
						
					}
					
					new RankManager().resetDefaultRank(p);
					sender.sendMessage(TAG_DEFAULT + "§fO rank do jogador foi resetado com suceso!");
					return true;
					
				}
				
				
			} else {
				
				sender.sendMessage("§b§luRanking §f- §av" + Bukkit.getPluginManager().getPlugin("uRanking").getDescription().getVersion());
			    sender.sendMessage("§a/uranking reload §7- §fRecarrega a configuração (OBS: Os jogadores precisarão relogar)");
				sender.sendMessage("§a/uranking upar <nome> §7- §fFaz o jogador upar seu rank, removendo dinheiro e rodando os comandos");
			    sender.sendMessage("§a/uranking setar <nome> <rank> §7- §fSeta o rank para um jogador");
			    sender.sendMessage("§a/uranking forcar <nome> <rank> §7- §fFaz o jogador upar seu rank para um especifico, removendo dinheiro e rodando os comandos");
			    sender.sendMessage("§a/uranking reset <nome> §7- §fReseta o rank do jogador (É necessário remover as permissões manualmente)");
				
			}
			
		}
		
		return false;
		
	}


}

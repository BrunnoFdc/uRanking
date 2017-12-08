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
public class Admin implements CommandExecutor {
	
	Main jp;
	 
	FileConfiguration msg = LocaleManager.getMensagens();
	private String TAG_DEFAULT = "§b[uRanking] ";
	private String TAG_ERROR = "§c[uRanking] ";
	public Admin(Main m) {
		
		jp = m;
		
	}
	
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
						
						p.sendMessage("§cNão foi possível encontrar seu rank, por favor, relogue!");
						return true;
						
					}
					
					PlayerInfo pInfo = new PlayerInfo(p.getUniqueId());
					final String oldRank = pInfo.getPlayerRank().getRankName();
					Rank pRank = pInfo.getPlayerRank();
					Rank pNewRank = pInfo.getNextRank();

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
							
							msg.getStringList("Anuncio-Rank-Maximo").stream().forEach(r -> Bukkit.broadcastMessage(r
									.replace("@rank", pNewRank.getRankName())
									.replace("@oldrank", pInfo.getPlayerRank().getRankName())
									.replace("@player", p.getName())
									.replace("@preco", String.valueOf(rankPreco))
									.replace("@dinheiro", String.valueOf(Main.economy.getBalance(p)))));	
							
						} else {
						
							msg.getStringList("Anuncio").stream().forEach(r -> Bukkit.broadcastMessage(r
									.replace("@rank", pNewRank.getRankName())
									.replace("@oldrank", pInfo.getPlayerRank().getRankName())
									.replace("@player", p.getName())
									.replace("@preco", String.valueOf(rankPreco))
									.replace("@dinheiro", String.valueOf(Main.economy.getBalance(p)))));	
							
						}
					
						
					} else {
						
						if((jp.getConfig().getBoolean("Anuncio-Rank-Maximo")) && (pInfo.getNextRank() == null)) {
							
							msg.getStringList("Anuncio-Rank-Maximo").stream().forEach(r -> Bukkit.broadcastMessage(r
									.replace("@rank", pNewRank.getRankName())
									.replace("@oldrank", pInfo.getPlayerRank().getRankName())
									.replace("@player", p.getName())
									.replace("@preco", String.valueOf(rankPreco))
									.replace("@dinheiro", String.valueOf(Main.economy.getBalance(p)))));	
							
						}
						
					}
					
					if(jp.getConfig().getBoolean("Enviar-Mensagem")) {
						
						msg.getStringList("Mensagem-Upou-Com-Sucesso").stream().forEach(r -> p.sendMessage(r
								.replace("@rank", pNewRank.getRankName())
								.replace("@oldrank", pInfo.getPlayerRank().getRankName())
								.replace("@player", p.getName())
								.replace("@preco", String.valueOf(rankPreco))
								.replace("@dinheiro", String.valueOf(Main.economy.getBalance(p)))));
						
					}
					
					Bukkit.getPluginManager().callEvent(new PlayerRankupEvent(p, pInfo.getPlayerRank().getRankName(), oldRank, pInfo.getPlayerRank().getTag(), rankPreco));		
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
							
							msg.getStringList("Anuncio-Rank-Maximo").stream().forEach(r -> Bukkit.broadcastMessage(r
									.replace("@rank", pNewRank.getRankName())
									.replace("@oldrank", pInfo.getPlayerRank().getRankName())
									.replace("@player", p.getName())
									.replace("@preco", String.valueOf(rankPreco))
									.replace("@dinheiro", String.valueOf(Main.economy.getBalance(p)))));	
							
						} else {
						
							msg.getStringList("Anuncio").stream().forEach(r -> Bukkit.broadcastMessage(r
									.replace("@rank", pNewRank.getRankName())
									.replace("@oldrank", pInfo.getPlayerRank().getRankName())
									.replace("@player", p.getName())
									.replace("@preco", String.valueOf(rankPreco))
									.replace("@dinheiro", String.valueOf(Main.economy.getBalance(p)))));	
							
						}
					
						
					} else {
						
						if((jp.getConfig().getBoolean("Anuncio-Rank-Maximo")) && (pInfo.getNextRank() == null)) {
							
							msg.getStringList("Anuncio-Rank-Maximo").stream().forEach(r -> Bukkit.broadcastMessage(r
									.replace("@rank", pNewRank.getRankName())
									.replace("@oldrank", pInfo.getPlayerRank().getRankName())
									.replace("@player", p.getName())
									.replace("@preco", String.valueOf(rankPreco))
									.replace("@dinheiro", String.valueOf(Main.economy.getBalance(p)))));	
							
						}
						
					}
					
					if(jp.getConfig().getBoolean("Enviar-Mensagem")) {
						
						msg.getStringList("Mensagem-Upou-Com-Sucesso").stream().forEach(r -> p.sendMessage(r
								.replace("@rank", pNewRank.getRankName())
								.replace("@oldrank", pInfo.getPlayerRank().getRankName())
								.replace("@player", p.getName())
								.replace("@preco", String.valueOf(rankPreco))
								.replace("@dinheiro", String.valueOf(Main.economy.getBalance(p)))));
						
					}
					
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

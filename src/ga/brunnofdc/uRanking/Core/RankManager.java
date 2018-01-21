package ga.brunnofdc.uRanking.Core;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import ga.brunnofdc.uRanking.Main;

public class RankManager {

	//Armazena o rank do jogador na memoria
	public static HashMap<UUID, Rank> PLAYER_RANKS = new HashMap<>();
	
	//Pega o RankID de um jogador, e se não houver, retorna o rank inicial
	private String getPlayerRank(Player p) {
		
		UUID uuid = p.getUniqueId();
		String pRank = null;
		if(Main.MYSQL_USE) {
			
			Connection conn = MySQL.mysqlConn();
			if(conn == null) {
				
				MySQL.connect(Main.plugin);
				conn = MySQL.mysqlConn();
			}
			
			
			try {
			
				if(Main.USE_UUIDS) {
					
					ResultSet rs = conn.prepareStatement("SELECT * FROM `uranking` WHERE `uuid` = '" +  uuid.toString() + "'").executeQuery();
					if(rs.next()) {
					
						pRank = rs.getString("rank");
					
					} else {
					
						setDefaultRank(p);
						pRank = getDefaultRank();
					
					}
				
				} else {
					
					ResultSet rs = conn.prepareStatement("SELECT * FROM `uranking` WHERE `nome` = '" +  p.getName() + "'").executeQuery();
					if(rs.next()) {
					
						pRank = rs.getString("rank");
					
					} else {
					
						setDefaultRank(p);
						pRank = getDefaultRank();
					
					}
									
				
				}

					
			} catch (SQLException e) {
				
				e.printStackTrace();
			
			}

			
		} else {
			
			String rank = Flatfile.getData().getString(uuid.toString() + ".Rank");
			if(rank == null) {
				
				setDefaultRank(p);
				pRank = getDefaultRank();
				
			} else {
				
				pRank = rank;
				
			}
			
		}
		
		return pRank;
		
	}
	
	//Pega o RankID do rank inicial
	private String getDefaultRank() {
	
		return Main.RANKS_ORDERED.get(1).getRankId();
		
	}
	
	//Quando o jogador logar, use para colocar o rank dele na memoria
	public void setRank(Player p) {
		
		String rankid = getPlayerRank(p);
		int pos = getPositionByRank(rankid);
		Rank pRank = new Rank(Main.RANKS_ORDERED.get(pos).getRankId(),
							  Main.RANKS_ORDERED.get(pos).getRankName(),
							  Main.RANKS_ORDERED.get(pos).getTag(),
							  Main.RANKS_ORDERED.get(pos).getPreco(),
							  Main.RANKS_ORDERED.get(pos).getCommands(),
							  pos);
		
		PLAYER_RANKS.put(p.getUniqueId(), pRank);
		
		
	}
	
	//Setar outro rank para o jogador
	public void updateRank(Player p, String rank) {
		
		if(Main.MYSQL_USE) {
			
			Connection conn = MySQL.mysqlConn();
			if(conn == null) {
				
				MySQL.connect(Main.plugin);
				conn = MySQL.mysqlConn();
				
			}
			
			try {
				
				String sql;
				
				if(Main.USE_UUIDS) {
					
					sql = "UPDATE `uranking` SET rank = '" + rank + "' WHERE uuid = '" + p.getUniqueId().toString() + "'";
					
				} else {
					
					sql = "UPDATE `uranking` SET rank = '" + rank + "' WHERE nome = '" + p.getName() + "'";
					
				}
				
				conn.prepareStatement(sql).executeUpdate();
				
			} catch(SQLException e) {
				
				
			}
			
		} else {
			
			Flatfile.getData().set(p.getUniqueId().toString() + ".Rank", rank);
			Flatfile.saveData();
		}
		
		int pos = getPositionByRank(rank);
		Rank pRank = new Rank(Main.RANKS_ORDERED.get(pos).getRankId(),
							  Main.RANKS_ORDERED.get(pos).getRankName(),
							  Main.RANKS_ORDERED.get(pos).getTag(),
							  Main.RANKS_ORDERED.get(pos).getPreco(),
							  Main.RANKS_ORDERED.get(pos).getCommands(),
							  pos);
		PLAYER_RANKS.put(p.getUniqueId(), pRank);
		
		pRank.getCommands().forEach(c -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), c.replace("@player", p.getName())));

		
		
	}
	
	//Se o jogador não tiver rank, será usado isso
	//Salva os dados no banco
	public void setDefaultRank(Player p) {
		
		UUID uuid = p.getUniqueId();
		if(Main.MYSQL_USE) {
			
			Connection conn = MySQL.mysqlConn();
			
			if(conn == null) {
				MySQL.connect(Main.plugin);
				conn = MySQL.mysqlConn();
				
			}
			
			try {
			
				String sql;
				if(Main.USE_UUIDS) {
					
					sql = "INSERT INTO `rankups` (uuid, rank) VALUES ('" +  uuid.toString() + "', '" + getDefaultRank() + "')";
					
				} else {
					
					sql = "INSERT INTO `rankups` (nome, rank) VALUES ('" +  p.getName() + "', '" + getDefaultRank() + "')";
					
				}
				
				conn.prepareStatement(sql).executeUpdate();
					
			} catch (SQLException e) {
				
				e.printStackTrace();
			
			}

			
		} else {

			Flatfile.getData().set(uuid.toString() + ".Rank", getDefaultRank());
			Flatfile.saveData();
			
		}
		
		for(String cmd : Main.RANKS_ORDERED.get(1).getCommands()) {
			
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd.replace("@player", p.getName()));
			
		}
		
	}
	
	//Para resetar o rank do jogador
	public void resetDefaultRank(Player p) {
		
		UUID uuid = p.getUniqueId();
		if(Main.MYSQL_USE) {
			
			Connection conn = MySQL.mysqlConn();
			
			if(conn == null) {
				
				MySQL.connect(Main.plugin);
				conn = MySQL.mysqlConn();
				
			}
			
			try {
				
				String sql;
				if(Main.USE_UUIDS) {
					
					sql = "UPDATE `rankups` SET rank='"+ getDefaultRank() + " WHERE uuid=" + uuid.toString();
					
				} else {
					
					sql = "UPDATE `rankups` SET rank='"+ getDefaultRank() + " WHERE nome=" + p.getName();
					
				}
				
				conn.prepareStatement(sql).executeUpdate();
					
			} catch (SQLException e) {
				
				e.printStackTrace();
			
			}

			
		} else {
			
			Flatfile.getData().set(uuid.toString() + ".Rank", getDefaultRank());
			Flatfile.saveData();
			
		}
		
		PLAYER_RANKS.remove(p.getUniqueId());
		Rank rank = new Rank(
				Main.RANKS_ORDERED.get(1).getRankId(), 
				Main.RANKS_ORDERED.get(1).getRankName(), 
				Main.RANKS_ORDERED.get(1).getTag(), 
				Main.RANKS_ORDERED.get(1).getPreco(), 
				Main.RANKS_ORDERED.get(1).getCommands(), 
				1);
		PLAYER_RANKS.put(p.getUniqueId(), rank);
		for(String cmd : Main.RANKS_ORDERED.get(1).getCommands()) {
			
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd.replace("@player", p.getName()));
			
		}
		
	}

	//Pega a posição a partir do rank
	public int getPositionByRank(String nome) {
		return Main.RANKS_ORDERED.values().stream().filter(r -> r.getRankId().equalsIgnoreCase(nome)).findFirst().get()
				.getPosition();
	}
	
	//Pega um rank a partir do ID
	public Rank getRankById(String nome) {
	
		return Main.RANKS_ORDERED.values().stream().filter(r -> r.getRankId().equalsIgnoreCase(nome)).findFirst().get();
		
	}

}

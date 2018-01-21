package ga.brunnofdc.uRanking.Core;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;


import org.bukkit.Bukkit;

import ga.brunnofdc.uRanking.Main;

public class MySQL {
	
	private static String ip;
	private static int port;
	private static String db;
	private static String user;
	private static String pass;
	private static Connection connection;
		
	public static Connection mysqlConn() {
		
		return connection;
		
	}
	
	//Usar sempre no onEnable
	//Serve pra configurar o MySQL ou conectar
	public static void setupMySQL(Main plugin) {
		
		Connection conn = null;
		ip = plugin.getConfig().getString("MySQL.IP");
		port = plugin.getConfig().getInt("MySQL.Porta");
		db = plugin.getConfig().getString("MySQL.Database");
		user = plugin.getConfig().getString("MySQL.Usuario");
		pass = plugin.getConfig().getString("MySQL.Senha");
		
		try {
		
			conn = DriverManager.getConnection("jdbc:mysql://" + ip + ":" + port + "/" + db, user, pass);
			Bukkit.getConsoleSender().sendMessage("§b[uRanking] §fConectado ao MySQL com sucesso!");
			connection = conn;
			
		} catch(SQLException e) {
			e.printStackTrace();
			Bukkit.getConsoleSender().sendMessage("§4[uRanking]§f Não foi possível conectar ao MySQL!");
			Bukkit.getConsoleSender().sendMessage("§4[uRanking]§f Por favor, revise os dados de conexão informados ou desabilite o MySQL.");
			Bukkit.getConsoleSender().sendMessage("§4[uRanking]§f Desabilitando plugin...");
			Bukkit.getPluginManager().disablePlugin(plugin);
			
			
		}
		
		if(Bukkit.getPluginManager().isPluginEnabled(plugin)) {
		  try {
				
				  Bukkit.getConsoleSender().sendMessage("§b[uRanking]§f Verificando e configurando banco de dados...");
				  String sql;
				  if(Main.USE_UUIDS) {
					  
					  sql = "CREATE TABLE IF NOT EXISTS `uranking` (`nome` TEXT NULL DEFAULT NULL, `rank` TEXT NULL DEFAULT NULL)";
					  
				  } else {
					  
					  sql = "CREATE TABLE IF NOT EXISTS `uranking` (`uuid` TEXT NULL DEFAULT NULL, `rank` TEXT NULL DEFAULT NULL)";
				  
				  }
				  conn.prepareStatement(sql).execute();
			      Bukkit.getConsoleSender().sendMessage("§b[uRanking]§f Banco de dados verificado e configurado com sucesso!");
			
		  } catch (SQLException e) {
			  Bukkit.getConsoleSender().sendMessage("§4[uRanking]§f Houve um erro ao verificar e configurar o banco de dados:");
			  e.printStackTrace();
		  }
		}
	  }
	
	public static void connect(Main plugin) {
		
		ip = plugin.getConfig().getString("MySQL.IP");
		port = plugin.getConfig().getInt("MySQL.Porta");
		db = plugin.getConfig().getString("MySQL.Database");
		user = plugin.getConfig().getString("MySQL.Usuario");
		pass = plugin.getConfig().getString("MySQL.Senha");
		
		try {
		
			connection = DriverManager.getConnection("jdbc:mysql://" + ip + ":" + port + "/" + db, user, pass);
			
			
		} catch(SQLException e) {
			
			e.printStackTrace();
			Bukkit.getConsoleSender().sendMessage("§4[uRanking]§f Não foi possível conectar ao MySQL!");
			Bukkit.getConsoleSender().sendMessage("§4[uRanking]§f Por favor, revise os dados de conexão informados ou desabilite o MySQL.");
			Bukkit.getConsoleSender().sendMessage("§4[uRanking]§f Desabilitando plugin...");
			Bukkit.getPluginManager().disablePlugin(plugin);
			
		}
				
	}


}

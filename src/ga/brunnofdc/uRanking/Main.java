package ga.brunnofdc.uRanking;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.JSONException;
import org.json.JSONObject;

import ga.brunnofdc.uRanking.Comandos.Admin;
import ga.brunnofdc.uRanking.Comandos.Ranks;
import ga.brunnofdc.uRanking.Comandos.Rankup;
import ga.brunnofdc.uRanking.Core.Flatfile;
import ga.brunnofdc.uRanking.Core.LocaleManager;
import ga.brunnofdc.uRanking.Core.MySQL;
import ga.brunnofdc.uRanking.Core.Rank;
import ga.brunnofdc.uRanking.Hook.Legendchat;
import ga.brunnofdc.uRanking.Hook.UltimateChat;
import ga.brunnofdc.uRanking.Listener.GUIListener;
import ga.brunnofdc.uRanking.Listener.JoinQuitListener;
import ga.brunnofdc.uRanking.Util.Update;
import net.milkbowl.vault.economy.Economy;

public class Main extends JavaPlugin {
	
	public static Main plugin;
	public static Map<Integer, Rank> RANKS_ORDERED = new HashMap<>();
	public static Economy economy = null;
	public static final String PLUGIN_PREFIX = "§b[uRanking]§f ";
	public static final String PLUGIN_ERROR_PREFIX = "§4[uRanking]§c ";
	public static String PLUGIN_VERSION = "";
	public static boolean CHAT_HOOK = false;
	public static boolean MYSQL_USE = false;
	public static boolean USE_UUIDS = false;
	
	public void onEnable() {
	
		plugin = this;
		PLUGIN_VERSION = getDescription().getVersion();
		ConsoleCommandSender b = Bukkit.getConsoleSender();
		b.sendMessage(PLUGIN_PREFIX + "Inicializando uRanking v" + PLUGIN_VERSION);
		checkVersion(PLUGIN_VERSION);
		setupConfig();
		if(getConfig().getBoolean("MySQL.Usar")) {
			
			MYSQL_USE = true;
			MySQL.setupMySQL(this);
			
		} else {
			
			Flatfile.setupDataFile(this);
			
		}
				
		if(!setupDependencies()) {
			
			return;
			
		}
		
		LocaleManager.setupMensagens(this);
		registerListeners();
		getCommand("rankup").setExecutor(new Rankup());
		getCommand("uranking").setExecutor(new Admin(this));
		
		if(Main.plugin.getConfig().getBoolean("GUI.Ativar")) {
		
			new Ranks("ranks");    
		
		}
		setupHooks();
		setupEconomy();
		setupRanks();
		if(getConfig().getBoolean("Usar-UUIDs")) {
			
			USE_UUIDS = true;
			
		}
		if(getConfig().getBoolean("Verificar-Atualizacoes")) {
			Update.verifyAtualizacao();
		}
		
	}
	
	public void onDisable() {
		
		try {
			MySQL.mysqlConn().close();
		} catch (SQLException e) {}
		
	}
	
	
	private void registerListeners() {
		
		Bukkit.getPluginManager().registerEvents(new JoinQuitListener(this), this);
		Bukkit.getPluginManager().registerEvents(new GUIListener(this), this);
	}
	
	private void setupConfig() {
		
		File file = new File(getDataFolder(), "config.yml");
	    if (!file.exists()) {
		      try
		      {
		        saveResource("config.yml", false);
		      }
		      catch (Exception localException1) {}
	    }
		
	}
 
	private boolean setupDependencies() {
		ConsoleCommandSender b = Bukkit.getConsoleSender();
		if(Bukkit.getPluginManager().isPluginEnabled("Vault")) {
			
			b.sendMessage(PLUGIN_PREFIX + "Vault encontrado! Dependencias §aOK§f!");
			return true;
			
		} else {
			
			b.sendMessage(PLUGIN_ERROR_PREFIX + "§Vault não encontrado! Desativando plugin...");
			Bukkit.getPluginManager().disablePlugin(this);
			return false;
		}
		
		
	}
	
	private void setupHooks() {

		ConsoleCommandSender b = Bukkit.getConsoleSender();
		if(Bukkit.getPluginManager().isPluginEnabled("Legendchat")) {
			
			b.sendMessage(PLUGIN_PREFIX + "Legendchat encontrado! Hook ativado.");
			Bukkit.getPluginManager().registerEvents(new Legendchat(this), this);
			CHAT_HOOK = true;
			
		} else if(Bukkit.getPluginManager().isPluginEnabled("UltimateChat")) {
			
			Bukkit.getPluginManager().registerEvents(new UltimateChat(this), this);
			b.sendMessage(PLUGIN_PREFIX + "uChat encontrado! Hook ativado.");
			CHAT_HOOK = true;
		}
		
		
	}

	private static void setupRanks() {
		
		int i = 1;
		for(String s : Main.plugin.getConfig().getConfigurationSection("Ranks").getKeys(false)) {
			FileConfiguration conf = Main.plugin.getConfig();
			Rank rank = new Rank(s, conf.getString("Ranks." + s + ".Nome").replaceAll("&", "§"), conf.getString("Ranks." + s + ".Tag"), conf.getDouble("Ranks." + s + ".Preco"), conf.getStringList("Ranks." + s + ".Comandos"), i);
			
			RANKS_ORDERED.put(i, rank);
			i++;
		}
		
		Bukkit.getConsoleSender().sendMessage("§b[uRanking] §fForam encontrados e armazenados §a" + (i - 1) + " §franks!");
		
	}
	
	public static void setupRanks(CommandSender sender) {
		
		int i = 1;
		for(String s : Main.plugin.getConfig().getConfigurationSection("Ranks").getKeys(false)) {
			FileConfiguration conf = Main.plugin.getConfig();
			Rank rank = new Rank(s, conf.getString("Ranks." + s + ".Nome").replaceAll("&", "§"), conf.getString("Ranks." + s + ".Tag"), conf.getDouble("Ranks." + s + ".Preco"), conf.getStringList("Ranks." + s + ".Comandos"), i);
			
			RANKS_ORDERED.put(i, rank);
			i++;
		}
		
		sender.sendMessage("§b[uRanking] §fForam encontrados e armazenados §a" + (i - 1) + " §franks!");
		
	}

	private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        economy = rsp.getProvider();
        return economy != null;
    }
	
	private void checkVersion(String version) {
				
		try {
			
			URL banner = new URL("https://api.github.com/repos/BrunnoFdc/uRanking/releases/latest");
			JSONObject json = new JSONObject(IOUtils.toString(banner));
			String github = json.getString("tag_name");
			
			if(!PLUGIN_VERSION.equals(github)) {
				
				Bukkit.getConsoleSender().sendMessage(PLUGIN_PREFIX + "Há um novo update disponível no plugin! Baixe em: http://bit.ly/uRankingDownload");
				
			}
				
		} catch (MalformedURLException e) {
			
			Bukkit.getConsoleSender().sendMessage(PLUGIN_ERROR_PREFIX + "Não foi possível verificar se o plugin possui atualizações. Por favor, cheque manualmente!");
			e.printStackTrace();
			
		} catch (JSONException e) {
		
			Bukkit.getConsoleSender().sendMessage(PLUGIN_ERROR_PREFIX + "Não foi possível verificar se o plugin possui atualizações. Por favor, cheque manualmente!");
			e.printStackTrace();
		
		} catch (IOException e) {
			
			Bukkit.getConsoleSender().sendMessage(PLUGIN_ERROR_PREFIX + "Não foi possível verificar se o plugin possui atualizações. Por favor, cheque manualmente!");
			e.printStackTrace();
		}
		
		
		
	}
	
}

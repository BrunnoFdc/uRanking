package ga.brunnofdc.uRanking.Util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import ga.brunnofdc.uRanking.Main;

public class Update {
	
  static boolean atualiza = false;
  static ConsoleCommandSender ccs = Bukkit.getConsoleSender();
  
  public static void verifyAtualizacao() {
    try {    
      String versao = Bukkit.getPluginManager().getPlugin("uRanking").getDescription().getVersion();
      Object Conexao;
      ((URLConnection)(Conexao = (new URL("http://brunnw.cf/plugins.php?atualizacao=3&versao=" + versao)).openConnection())).setConnectTimeout(5000);
      ((URLConnection)Conexao).setReadTimeout(5000);
      String $Version = ((BufferedReader)(Conexao = new BufferedReader(new InputStreamReader(((URLConnection)Conexao).getInputStream())))).readLine();
      if ($Version.equalsIgnoreCase("true")) {
        atualiza = true;
        ccs.sendMessage("�b[uRanking] �fUma nova atualiza��o est� dispon�vel!");
        ccs.sendMessage("�b[uRanking] �fBaixe em: http://www.brunnw.cf/cliente/");
      }
    } catch (Exception e) {
      ccs.sendMessage("�4[uRanking] �cN�o foi poss�vel verificar atualiza��es!");
    }
  }
  
  public static void avisoUpdates(Player p) {
   if(Main.plugin.getConfig().getBoolean("Avisar-Administradores")) {
    if ((p.isOp()) || (p.hasPermission("uranking.admin"))) {
     if(atualiza) {
       p.sendMessage("�b[uRanking] �fH� uma nova atualiza��o dispon�vel!");
       p.sendMessage("�b[uRanking] �fBaixe em: http://www.brunnw.cf/cliente/");
     }
    }
   }
  }
  
  
}
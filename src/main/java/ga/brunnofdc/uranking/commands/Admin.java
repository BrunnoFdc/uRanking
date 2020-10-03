package ga.brunnofdc.uranking.commands;

import ga.brunnofdc.uranking.ranking.Rank;
import ga.brunnofdc.uranking.ranking.RankCacheManager;
import ga.brunnofdc.uranking.ranking.RankUtils;
import ga.brunnofdc.uranking.ranking.RankedPlayer;
import ga.brunnofdc.uranking.uRanking;
import ga.brunnofdc.uranking.utils.Language;
import ga.brunnofdc.uranking.utils.enums.Message;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Admin implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if(!sender.hasPermission("uranking.admin")) {
            sender.sendMessage(Language.getMessage(Message.NO_PERMISSION).toArray());
            return false;
        }

        if(args.length > 0) {

            if(args[0].equalsIgnoreCase("rankup") || args[0].equalsIgnoreCase("force")) {

                boolean force = (args[0].equalsIgnoreCase("force"));
                if(args.length < 2) {
                    sender.sendMessage(uRanking.getChatTag("error") +
                            " §fUsage: /uranking " + (force ? "force" : "rankup") + " <player> [rank]");
                    return true;
                }

                Player target = Bukkit.getPlayer(args[1]);
                if(target == null) {
                    sender.sendMessage(uRanking.getChatTag("error") + " §fPlayer not found!");
                    return true;
                }

                if(args.length > 2) {
                    Rank newRank = RankUtils.getRankByID(args[2]);
                    if(newRank == null) {
                        sender.sendMessage(uRanking.getChatTag("error") + " §fInvalid rank!");
                        return true;
                    }
                    RankCacheManager.getRankedPlayer(target).rankUp(newRank, false, !force);
                    sender.sendMessage(uRanking.getChatTag("success") + " §fPlayer rank setted!");
                } else {
                    RankCacheManager.getRankedPlayer(target).rankUp(false, !force);
                    sender.sendMessage(uRanking.getChatTag("success") + " §fPlayer rank upped!");
                }

            } else if(args[0].equalsIgnoreCase("reset")) {

                if(args.length < 2) {
                    sender.sendMessage(uRanking.getChatTag("error") +
                            " §fUsage: /uranking reset <player>");
                    return true;
                }

                Player target = Bukkit.getPlayer(args[1]);
                if(target == null) {
                    sender.sendMessage(uRanking.getChatTag("error") + " §fPlayer not found!");
                    return true;
                }

                Rank initialRank = RankUtils.getRankByPosition(1);
                RankCacheManager.getRankedPlayer(target).setRank(initialRank);
                initialRank.getCommands().forEach(cmd -> {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd
                            .replace("@player", target.getName())
                            .replace("@rank", initialRank.getID())
                            .replace("@price", String.valueOf(initialRank.getPrice()))
                    );
                });
                sender.sendMessage(uRanking.getChatTag("success") + " §fPlayer rank reseted!");

            } else if(args[0].equalsIgnoreCase("reload")) {
                uRanking.getInstance().reloadConfig();
                Language.reload();
                RankCacheManager.emptyCache();
                if(Bukkit.getOnlinePlayers().size() > 0) {
                    Bukkit.getOnlinePlayers().forEach(p -> {
                        RankedPlayer ranked = new RankedPlayer(p);
                        RankCacheManager.storePlayer(p, ranked);
                    });
                }
                sender.sendMessage(uRanking.getChatTag("success") + " §fAll configuration files were reloaded.");
            } else {
                sendHelpMessages(sender);
            }
        } else {
            sendHelpMessages(sender);
        }
        return true;
    }

    public void sendHelpMessages(CommandSender sender) {
        int messagesLanguage = uRanking.getInstance().getConfig().getString("System-Settings.Language").equals("br") ? 1 : 0;
        String[][] messages = new String[][] {
                //English messages
                {
                        "Reload plugin's configuration file (Note: All online players will need to rejoin)",
                        "Force another player to rankup. The player must have sufficient amount of money.",
                        "Same as \"rankup\", but without taking player's money.",
                        "Reset another player rank. You will need to remove the permissions manually."
                },

                //Portuguese messages (Feel free to add another languages)
                {
                        "Recarrega as configurações do plugin (OBS: Os jogadores precisarão relogar)",
                        "Força um jogador a upar, e faz com que o preço do rank seja descontado.",
                        "O mesmo que o comando \"rankup\", porém o preço do rank não será descontado.",
                        "Reseta o rank de um jogador. Será necessário remover as permissões manualmente."
                }
        };
        sender.sendMessage("§b§luRanking §f- §av" + uRanking.getInstance().getDescription().getVersion());
        sender.sendMessage("§a/uranking reload §7- §f" + messages[messagesLanguage][0]);
        sender.sendMessage("§a/uranking rankup <player> [rank] §7- §f" + messages[messagesLanguage][1]);
        sender.sendMessage("§a/uranking force <player> [rank] §7- §f" + messages[messagesLanguage][2]);
        sender.sendMessage("§a/uranking reset <player> §7- §f" + messages[messagesLanguage][3]);
    }

}

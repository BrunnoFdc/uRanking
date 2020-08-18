package ga.brunnofdc.uranking.commands;

import ga.brunnofdc.uranking.economy.EconomicUnit;
import ga.brunnofdc.uranking.ranking.RankCacheManager;
import ga.brunnofdc.uranking.ranking.RankedPlayer;
import ga.brunnofdc.uranking.uRanking;
import ga.brunnofdc.uranking.utils.Language;
import ga.brunnofdc.uranking.utils.MiscUtils;
import ga.brunnofdc.uranking.utils.StringList;
import ga.brunnofdc.uranking.utils.enums.Message;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class Rankup implements CommandExecutor, Listener {
    private static ConfigurationSection guiSec = uRanking.getInstance().getConfig().getConfigurationSection("Rankup-GUI");

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage("This command is only available in-game");
            return true;
        }

        if(!sender.hasPermission("uranking.rankup")) {
            sender.sendMessage(Language.getMessage(Message.NO_PERMISSION).toArray());
            return true;
        }

        Player psender = (Player) sender;
        RankedPlayer player = RankCacheManager.getRankedPlayer(psender);

        if(player == null) {
            psender.sendMessage(Language.getMessage(Message.RANK_NOT_STORED).toArray());
            return true;
        }

        if(guiSec.getBoolean("Enable")) {
            String invName = ChatColor.translateAlternateColorCodes('&',
                    RankedPlayer.translateRankupVariables(guiSec.getString("Inventory-Name"), player));

            ItemStack btnCancel = getMenuItemFormatted("Item_Cancel", player);
            ItemStack btnInfo = getMenuItemFormatted("Item_Info", player);
            ItemStack btnConfirm = getMenuItemFormatted("Item_Confirm", player);

            Inventory inv = Bukkit.createInventory(null, 27, invName);
            inv.setItem(11, btnCancel);
            inv.setItem(13, btnInfo);
            inv.setItem(15, btnConfirm);
            psender.openInventory(inv);

        } else {
            player.rankUp(true, true);
        }

        return true;

    }

    @EventHandler
    public void onClickItem(InventoryClickEvent event) {
        InventoryView view = event.getView();
        Player player = (Player) event.getWhoClicked();
        RankedPlayer ranked = RankCacheManager.getRankedPlayer(player);
        if(ranked == null) {
            return;
        }
        String expectedInvName = ChatColor.translateAlternateColorCodes('&',
                RankedPlayer.translateRankupVariables(guiSec.getString("Inventory-Name"), ranked));

        if(view.getTitle().equals(expectedInvName)) {
            if(event.getClick() == ClickType.LEFT || event.getClick() == ClickType.RIGHT) {
                if(event.getCurrentItem().isSimilar(getMenuItemFormatted("Item_Cancel", ranked))) {
                    event.getView().close();
                    player.sendMessage(Language.getMessage(Message.RANKUP_CANCELED).toArray());
                }
                if(event.getCurrentItem().isSimilar(getMenuItemFormatted("Item_Confirm", ranked))) {
                    event.getView().close();
                    ranked.rankUp(true, true);
                }
                event.setCancelled(true);
            }
        }

    }

    private static ItemStack getMenuItemFormatted(String path, RankedPlayer player) {
        ItemStack item;
        ItemMeta meta;
        if(path.equals("Item_Info") && guiSec.getBoolean("Items.Item_Info.Head-Mode.Enable")) {
            item = new ItemStack(Material.SKULL_ITEM, 1 , (short) 3);
            meta = item.getItemMeta();
            ((SkullMeta) meta).setOwner(guiSec.getString("Items.Item_Info.Head-Mode.Player").replace("@player", player.getPlayer().getName()));
        } else {
            item = MiscUtils.getItemStackByItemID(guiSec.getString("Items." + path + ".ID"));
            meta = item.getItemMeta();
        }
        String itemName = ChatColor.translateAlternateColorCodes('&', guiSec.getString("Items." + path + ".Name"));
        StringList itemLore = new StringList(guiSec.getStringList("Items." + path + ".Lore")).translateColors();
        meta.setDisplayName(RankedPlayer.translateRankupVariables(itemName, player));
        meta.setLore(RankedPlayer.translateRankupVariables(itemLore, player));
        item.setItemMeta(meta);
        return item;
    }

}

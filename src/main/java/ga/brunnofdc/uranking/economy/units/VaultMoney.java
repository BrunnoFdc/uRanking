package ga.brunnofdc.uranking.economy.units;

import ga.brunnofdc.uranking.economy.EconomicUnit;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class VaultMoney extends EconomicUnit {

    private Economy economy;

    public VaultMoney() {
        RegisteredServiceProvider<Economy> economyProvider = Bukkit.getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }
    }

    @Override
    public double getBalance(Player player) {
        return economy.getBalance(player);
    }

    @Override
    public void setBalance(Player player, double amount) {
        economy.withdrawPlayer(player, economy.getBalance(player));
        economy.depositPlayer(player, amount);
    }

    @Override
    public String getID() {
        return "Vault";
    }
}

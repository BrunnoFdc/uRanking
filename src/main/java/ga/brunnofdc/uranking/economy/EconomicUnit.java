package ga.brunnofdc.uranking.economy;

import org.bukkit.entity.Player;

public abstract class EconomicUnit {

    public abstract double getBalance(Player player);

    public abstract void setBalance(Player player, double amount);

    public abstract String getID();

    public boolean has(Player player, double amount) {
        return (getBalance(player) >= amount);
    }

    public void deposit(Player player, double amount) {
        setBalance(player, getBalance(player) + amount);
    }

    public void withdraw(Player player, double amount) {
        if(has(player, amount)) {
            setBalance(player, getBalance(player) - amount);
        }
    }


}

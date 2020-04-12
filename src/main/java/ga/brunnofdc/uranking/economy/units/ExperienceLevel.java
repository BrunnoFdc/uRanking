package ga.brunnofdc.uranking.economy.units;

import ga.brunnofdc.uranking.economy.EconomicUnit;
import org.bukkit.entity.Player;

public class ExperienceLevel extends EconomicUnit {


    @Override
    public double getBalance(Player player) {
        return player.getLevel();
    }

    @Override
    public void setBalance(Player player, double amount) {
        if(amount > 2147483647D) {
            player.setLevel(2147483547);
        } else {
            player.setLevel((int) amount);
        }
    }

    @Override
    public String getID() {
        return "XP-Level";
    }
}

package ga.brunnofdc.uranking.ranking;

import org.bukkit.ChatColor;

import java.util.List;

public class Rank implements Comparable<Rank> {

    private String id;
    private String name;
    private String prefix;
    private int position;
    private double price;
    private List<String> cmds;

    public Rank(String id, String name, String prefix, int position, double price, List<String> commands) {
        this.id = id;
        this.price = price;
        this.name = ChatColor.translateAlternateColorCodes('&', name);
        this.position = position;
        this.cmds = commands;
        this.prefix = prefix;
    }

    public int getPosition() {
        return this.position;
    }

    public String getName() {
        return this.name + ChatColor.RESET;

    }

    public String getID() {

        return this.id;

    }

    public String getPrefix() {

        return this.prefix + ChatColor.RESET;

    }

    public double getPrice() {

        return this.price;

    }

    public List<String> getCommands() {

        return this.cmds;

    }

    @Override
    public int compareTo(Rank rank) {
        return Integer.compare(this.getPosition(), rank.getPosition());
    }

}
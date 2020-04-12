package ga.brunnofdc.uranking.data;

import ga.brunnofdc.uranking.data.sources.Flatfile;
import ga.brunnofdc.uranking.data.sources.MySQL;
import ga.brunnofdc.uranking.uRanking;
import ga.brunnofdc.uranking.utils.SystemDefs;
import ga.brunnofdc.uranking.utils.enums.Property;
import org.bukkit.plugin.java.JavaPlugin;

public class DataManager {

    private static RankDataSource usedDataSource;

    public static void setup(JavaPlugin plugin) {
        if(SystemDefs.getProp(Property.USE_MYSQL)) {
            try {
                usedDataSource = new MySQL(plugin);
                uRanking.getInstance().getLogger().info("MySQL was enabled successfully!");
            } catch (Exception e) {
                uRanking.getInstance().getLogger().severe("Could not connect to MySQL database. Check your credentials!");
                uRanking.getInstance().getLogger().info("Switching to Flatfile!");
                usedDataSource = new Flatfile(plugin);
            }
        } else {
            usedDataSource = new Flatfile(plugin);
        }
    }

    public static RankDataSource getDataSource() {
        return usedDataSource;
    }

}

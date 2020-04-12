package ga.brunnofdc.uranking.data.sources;

import ga.brunnofdc.uranking.data.RankDataSource;
import ga.brunnofdc.uranking.ranking.Rank;
import ga.brunnofdc.uranking.ranking.RankUtils;
import ga.brunnofdc.uranking.uRanking;
import ga.brunnofdc.uranking.utils.ErrorReporter;
import ga.brunnofdc.uranking.utils.enums.ErrorType;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Flatfile extends RankDataSource {

    private FileConfiguration data;
    private File file;

    public Flatfile(Plugin plugin) {

        File file = new File(plugin.getDataFolder(), "data.yml");

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                ErrorReporter.sendReport(ErrorType.FLATFILE_FILE_CREATION, e);
            }
        }

        this.file = file;
        this.data = YamlConfiguration.loadConfiguration(this.file);
    }

    //TODO: Test
    public boolean exists(UUID playeruuid) {
        return (data.isSet("Player-Ranks." + playeruuid.toString()));
    }

    @Nullable
    public Rank read(UUID playeruuid) {
        String rankid = data.getString("Player-Ranks." + playeruuid.toString());
        return RankUtils.getRankByID(rankid);
    }

    public void set(UUID playeruuid, String rankid) {
        data.set("Player-Ranks." + playeruuid.toString(), rankid);
        try {
            data.save(file);
        } catch (IOException e) {
            uRanking.getInstance().getLogger().severe("Houve um problema ao salvar o arquivo data.yml");
            e.printStackTrace();
        }
    }

    public Map<UUID, String> index() {

        Map<UUID, String> result = new HashMap<>();
        data.getConfigurationSection("Player-Ranks").getValues(false).forEach((key, value) -> {
            result.put(UUID.fromString(key), value.toString());
        });
        return result;

    }

    public void onDisable() {
        try {
            data.save(file);
        } catch (IOException e) {
            uRanking.getInstance().getLogger().severe("Houve um problema ao salvar o arquivo data.yml");
            e.printStackTrace();
        }
    }

}

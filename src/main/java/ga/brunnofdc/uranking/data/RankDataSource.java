package ga.brunnofdc.uranking.data;

import ga.brunnofdc.uranking.ranking.Rank;
import org.bukkit.entity.HumanEntity;

import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public abstract class RankDataSource {

    public abstract void exists(UUID playeruuid, Consumer<Boolean> callback);

    protected abstract void read(UUID playeruuid, Consumer<Rank> callback);

    public abstract void set(UUID playeruuid, String rankid);

    public abstract Map<UUID, String> index();

    public abstract void onDisable();

    public void read(HumanEntity player, Consumer<Rank> callback) {
        read(player.getUniqueId(), callback);
    }

    public void exists(HumanEntity player, Consumer<Boolean> callback) {
        exists(player.getUniqueId(), callback);
    }

    public void set(HumanEntity player, String rankid) {
        set(player.getUniqueId(), rankid);
    }

}

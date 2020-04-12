package ga.brunnofdc.uranking.data;

import ga.brunnofdc.uranking.ranking.Rank;
import org.bukkit.entity.HumanEntity;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.UUID;

public abstract class RankDataSource {

    public abstract boolean exists(UUID playeruuid);

    @Nullable
    public abstract Rank read(UUID playeruuid);

    public abstract void set(UUID playeruuid, String rankid);

    public abstract Map<UUID, String> index();

    public abstract void onDisable();

    /* Overloaded methods */

    @Nullable
    public Rank read(HumanEntity player) {
        return read(player.getUniqueId());
    }

    public boolean exists(HumanEntity player) {
        return exists(player.getUniqueId());
    }

    public void set(HumanEntity player, String rankid) {
        set(player.getUniqueId(), rankid);
    }

}

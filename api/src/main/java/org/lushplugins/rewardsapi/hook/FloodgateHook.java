package org.lushplugins.rewardsapi.hook;

import org.lushplugins.lushlib.hook.Hook;
import org.geysermc.floodgate.api.FloodgateApi;

import java.util.UUID;

public class FloodgateHook extends Hook {

    public FloodgateHook() {
        super(HookId.FLOODGATE);
    }

    public boolean isFloodgatePlayer(UUID uuid) {
        return FloodgateApi.getInstance().isFloodgatePlayer(uuid);
    }
}

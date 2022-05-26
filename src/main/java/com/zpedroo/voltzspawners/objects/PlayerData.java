package com.zpedroo.voltzspawners.objects;

import java.util.UUID;

public class PlayerData {

    private final UUID uuid;
    private boolean killAll;
    private boolean update;

    public PlayerData(UUID uuid, boolean killAll) {
        this.uuid = uuid;
        this.killAll = killAll;
        this.update = false;
    }

    public UUID getUniqueId() {
        return uuid;
    }

    public boolean isKillAll() {
        return killAll;
    }

    public boolean isQueueUpdate() {
        return update;
    }

    public void setKillAll(boolean killAll) {
        this.killAll = killAll;
        this.update = true;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }
}
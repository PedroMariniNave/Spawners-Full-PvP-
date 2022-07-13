package com.zpedroo.voltzspawners.api;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.*;
import com.zpedroo.voltzspawners.objects.*;
import java.math.*;
import org.bukkit.event.*;

@Getter
@Setter
public class SpawnerBuyEvent extends Event implements Cancellable {

    private final Player player;
    private final Spawner spawner;
    private BigInteger amount;
    private BigInteger price;
    private static final HandlerList HANDLERS_LIST = new HandlerList();
    private boolean isCancelled;

    public SpawnerBuyEvent(Player player, Spawner spawner, BigInteger amount, BigInteger price) {
        this.isCancelled = false;
        this.player = player;
        this.spawner = spawner;
        this.amount = amount;
        this.price = price;
    }

    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }
}

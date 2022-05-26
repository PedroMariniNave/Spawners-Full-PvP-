package com.zpedroo.voltzspawners.api;

import com.zpedroo.voltzspawners.objects.Spawner;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.math.BigInteger;

public class SpawnerBuyEvent extends Event implements Cancellable {

    private final Player player;
    private final Spawner spawner;
    private BigInteger amount;
    private BigInteger price;
    private static final HandlerList HANDLERS_LIST = new HandlerList();
    private boolean isCancelled = false;

    public SpawnerBuyEvent(Player player, Spawner spawner, BigInteger amount, BigInteger price) {
        this.player = player;
        this.spawner = spawner;
        this.amount = amount;
        this.price = price;
    }

    public Player getPlayer() {
        return player;
    }

    public Spawner getSpawner() {
        return spawner;
    }

    public BigInteger getAmount() {
        return amount;
    }

    public BigInteger getPrice() {
        return price;
    }

    public void setAmount(BigInteger amount) {
        this.amount = amount;
    }

    public void setPrice(BigInteger price) {
        this.price = price;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean isCancelled) {
        this.isCancelled = isCancelled;
    }
}
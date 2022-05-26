package com.zpedroo.voltzspawners.objects;

<<<<<<< HEAD
import com.zpedroo.multieconomy.objects.general.Currency;
import com.zpedroo.voltzspawners.enums.PlayerAction;
=======
import com.zpedroo.multieconomy.objects.Currency;
import com.zpedroo.voltzspawners.utils.enums.PlayerAction;
>>>>>>> d1a39a0d6c92e3622fb633fd31c3e383d802bd98
import org.bukkit.entity.Player;

import java.math.BigInteger;

public class PlayerChat {

<<<<<<< HEAD
    private final Player player;
    private final PlacedSpawner placedSpawner;
    private final Spawner spawner;
    private final BigInteger price;
    private final Currency currency;
    private final PlayerAction playerAction;
=======
    private Player player;
    private PlacedSpawner placedSpawner;
    private Spawner spawner;
    private BigInteger price;
    private Currency currency;
    private PlayerAction playerAction;
>>>>>>> d1a39a0d6c92e3622fb633fd31c3e383d802bd98

    public PlayerChat(Player player, PlacedSpawner placedSpawner, PlayerAction playerAction) {
        this(player, placedSpawner, null, null, null, playerAction);
    }

    public PlayerChat(Player player, Spawner spawner, Currency currency, BigInteger price, PlayerAction playerAction) {
        this(player, null, spawner, price, currency, playerAction);
    }

    public PlayerChat(Player player, PlacedSpawner placedSpawner, Spawner spawner, BigInteger price, Currency currency, PlayerAction playerAction) {
        this.player = player;
        this.placedSpawner = placedSpawner;
        this.spawner = spawner;
        this.price = price;
        this.currency = currency;
        this.playerAction = playerAction;
    }

    public Player getPlayer() {
        return player;
    }

    public PlacedSpawner getPlacedSpawner() {
        return placedSpawner;
    }

    public Spawner getSpawner() {
        return spawner;
    }

    public BigInteger getPrice() {
        return price;
    }

    public Currency getCurrency() {
        return currency;
    }

    public PlayerAction getAction() {
        return playerAction;
    }
}
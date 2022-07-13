package com.zpedroo.voltzspawners.objects;

import com.zpedroo.multieconomy.objects.general.Currency;
import com.zpedroo.voltzspawners.enums.PlayerAction;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.math.BigInteger;

@Getter
public class PlayerChat {
    
    private final Player player;
    private final PlacedSpawner placedSpawner;
    private final Spawner spawner;
    private final BigInteger price;
    private final Currency currency;
    private final PlayerAction action;
    
    public PlayerChat(Player player, PlacedSpawner placedSpawner, PlayerAction action) {
        this(player, placedSpawner, null, null, null, action);
    }
    
    public PlayerChat(Player player, Spawner spawner, Currency currency, BigInteger price, PlayerAction action) {
        this(player, null, spawner, price, currency, action);
    }
    
    public PlayerChat(Player player, PlacedSpawner placedSpawner, Spawner spawner, BigInteger price, Currency currency, PlayerAction action) {
        this.player = player;
        this.placedSpawner = placedSpawner;
        this.spawner = spawner;
        this.price = price;
        this.currency = currency;
        this.action = action;
    }
}
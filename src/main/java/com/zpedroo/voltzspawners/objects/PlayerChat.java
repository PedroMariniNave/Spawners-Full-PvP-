package com.zpedroo.voltzspawners.objects;

import com.zpedroo.multieconomy.objects.general.Currency;
import com.zpedroo.voltzspawners.enums.PlayerAction;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.math.BigInteger;
import java.util.Map;

@Getter
public class PlayerChat {
    
    private final Player player;
    private final PlacedSpawner placedSpawner;
    private final Spawner spawner;
    private final Map<Currency, BigInteger> prices;
    private final PlayerAction action;
    
    public PlayerChat(Player player, PlacedSpawner placedSpawner, PlayerAction action) {
        this(player, placedSpawner, null, null, action);
    }
    
    public PlayerChat(Player player, Spawner spawner, Map<Currency, BigInteger> prices, PlayerAction action) {
        this(player, null, spawner, prices, action);
    }
    
    public PlayerChat(Player player, PlacedSpawner placedSpawner, Spawner spawner, Map<Currency, BigInteger> prices, PlayerAction action) {
        this.player = player;
        this.placedSpawner = placedSpawner;
        this.spawner = spawner;
        this.prices = prices;
        this.action = action;
    }
}
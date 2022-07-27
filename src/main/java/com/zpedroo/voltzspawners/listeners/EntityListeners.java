package com.zpedroo.voltzspawners.listeners;

import com.gmail.nossr50.api.ExperienceAPI;
import com.gmail.nossr50.datatypes.skills.SkillType;
import com.gmail.nossr50.datatypes.skills.XPGainReason;
import com.zpedroo.voltzspawners.VoltzSpawners;
import com.zpedroo.voltzspawners.managers.DataManager;
import com.zpedroo.voltzspawners.managers.EntityManager;
import com.zpedroo.voltzspawners.managers.SerializatorManager;
import com.zpedroo.voltzspawners.objects.DropItem;
import com.zpedroo.voltzspawners.objects.PlacedSpawner;
import com.zpedroo.voltzspawners.objects.SpawnerDrop;
import com.zpedroo.voltzspawners.utils.checker.PlotChecker;
import com.zpedroo.voltzspawners.utils.config.Settings;
import org.apache.commons.lang.StringUtils;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import static com.zpedroo.voltzspawners.utils.config.Settings.STACK_RADIUS;

public class EntityListeners implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof LivingEntity) || !event.getEntity().hasMetadata("MobAmount")) return;
        if (!(event.getDamager() instanceof Player)) return;

        LivingEntity entity = (LivingEntity) event.getEntity();
        String serialized = entity.getMetadata("Spawner").get(0).asString();
        PlacedSpawner spawner = DataManager.getInstance().getPlacedSpawner(SerializatorManager.getLocationSerialization().deserialize(serialized));
        if (spawner == null) return;

        Player player = (Player) event.getDamager();
        if (!PlotChecker.canInteractInPlot(player, entity.getLocation()) || entity.getNoDamageTicks() > 0) {
            event.setCancelled(true);
            return;
        }
        if (entity.getHealth() - event.getDamage() > 2) return;

        event.setDamage(1);
        entity.setHealth(entity.getMaxHealth());

        boolean isKillAll = DataManager.getInstance().getPlayerData(player).isKillAll();
        BigInteger stack = new BigInteger(entity.getMetadata("MobAmount").get(0).asString());
        BigInteger amountToKill = isKillAll ? stack : BigInteger.ONE;
        BigInteger dropAmount = setLooting(amountToKill, getLootingBonuses(player.getItemInHand()));

        entity.setMetadata("MobKilledAmount", new FixedMetadataValue(VoltzSpawners.get(), amountToKill)); // useful for other plugins
        EntityManager.removeStack(entity, amountToKill, spawner);
        dropOrStackSpawnerDrops(spawner, dropAmount, entity.getLocation());
        callDeathEvent(entity);
        addMcMMOExp(spawner, player);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onDeath(EntityDeathEvent event) {
        if (!event.getEntity().hasMetadata("MobAmount")) return;
        if (isCustomDeathCall(event)) return;

        event.getDrops().clear();
        event.getEntity().remove();
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onTeleport(final EntityTeleportEvent event) {
        if (!event.getEntity().hasMetadata("MobAmount")) return;

        event.setCancelled(true);
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void onEggSpawn(final ItemSpawnEvent event) {
        if (!event.getEntity().getItemStack().getType().equals(Material.EGG)) return;

        event.setCancelled(true);
    }
    
    private void dropOrStackSpawnerDrops(PlacedSpawner spawner, BigInteger amount, Location location) {
        firstLoop: for (SpawnerDrop spawnerDrop : spawner.getSpawner().getDrops()) {
            for (Entity nearbyItem : location.getWorld().getNearbyEntities(location, STACK_RADIUS, STACK_RADIUS, STACK_RADIUS).stream().filter(
                    nearbyItem -> nearbyItem instanceof Item && nearbyItem.hasMetadata("DropItem") && ((Item) nearbyItem).getItemStack().isSimilar(spawnerDrop.getDisplayItem())
            ).collect(Collectors.toList())) {
                DropItem dropItem = (DropItem) nearbyItem.getMetadata("DropItem").get(0).value();
                if (dropItem == null || dropItem.getDrop().isSingleDrop()) continue;

                BigInteger newDropStackAmount = dropItem.getStackAmount().add(amount);
                dropItem.setStackAmount(newDropStackAmount);
                continue firstLoop;
            }

            double chance = spawnerDrop.getChance();
            double number = ThreadLocalRandom.current().nextDouble(0, 100);
            if (number > chance) continue;

            DropItem dropItem = new DropItem(spawnerDrop, spawner, spawnerDrop.isSingleDrop() ? BigInteger.ONE : amount);
            dropItem.drop(location);
        }
    }

    private void callDeathEvent(LivingEntity entity) {
        EntityDeathEvent event = new EntityDeathEvent(entity, new ArrayList<>(0));
        Bukkit.getPluginManager().callEvent(event);
    }

    private void addMcMMOExp(PlacedSpawner spawner, Player player) {
        ItemStack item = player.getItemInHand();
        if (item == null || isFarmSword(item)) return;

        SkillType skillType = null;
        if (StringUtils.endsWith(player.getItemInHand().getType().toString(), "_AXE")) {
            skillType = SkillType.AXES;
        } else if (StringUtils.endsWith(player.getItemInHand().getType().toString(), "_SWORD")) {
            skillType = SkillType.SWORDS;
        }

        if (skillType == null) return;

        int exp = spawner.getSpawner().getMcMMOExp();
        ExperienceAPI.addXP(player, skillType.getName(), exp, XPGainReason.UNKNOWN.name());
    }

    private boolean isCustomDeathCall(EntityDeathEvent event) {
        return event.getDrops().isEmpty();
    }

    private boolean isFarmSword(ItemStack item) {
        return item.getEnchantmentLevel(Enchantment.LOOT_BONUS_MOBS) > 0;
    }

    private int getLootingBonuses(ItemStack item) {
        if (item == null || item.getType().equals(Material.AIR)) return 0;

        return item.getEnchantmentLevel(Enchantment.LOOT_BONUS_MOBS);
    }
    
    private BigInteger setLooting(BigInteger value, int looting) {
        if (looting <= 0) return value;

        return value.multiply(new BigInteger(Settings.LOOTING_BONUS)).multiply(BigInteger.valueOf(looting));
    }
}

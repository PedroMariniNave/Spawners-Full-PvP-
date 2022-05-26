package com.zpedroo.voltzspawners.listeners;

import com.gmail.nossr50.api.ExperienceAPI;
import com.gmail.nossr50.datatypes.skills.SkillType;
import com.gmail.nossr50.datatypes.skills.XPGainReason;
import com.zpedroo.onlinetime.api.OnlineTimeAPI;
import com.zpedroo.voltzspawners.managers.DataManager;
import com.zpedroo.voltzspawners.managers.EntityManager;
import com.zpedroo.voltzspawners.objects.Drop;
import com.zpedroo.voltzspawners.objects.DropItem;
import com.zpedroo.voltzspawners.objects.PlacedSpawner;
import com.zpedroo.voltzspawners.utils.checker.PlotChecker;
import com.zpedroo.voltzspawners.utils.config.Messages;
import com.zpedroo.voltzspawners.utils.config.Settings;
import com.zpedroo.voltzspawners.utils.serialization.LocationSerialization;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTeleportEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.inventory.ItemStack;

import java.math.BigInteger;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import static com.zpedroo.voltzspawners.utils.config.Settings.STACK_RADIUS;

public class EntityListeners implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof LivingEntity)) return;
        if (!(event.getDamager() instanceof Player)) return;

        LivingEntity entity = (LivingEntity) event.getEntity();
        if (!entity.hasMetadata("MobAmount")) return;

        String serialized = entity.getMetadata("Spawner").get(0).asString();
        PlacedSpawner spawner = DataManager.getInstance().getPlacedSpawner(LocationSerialization.deserializeLocation(serialized));
        if (spawner == null) return;
        if (entity.getHealth() - event.getDamage() > 0) return;

        event.setCancelled(true);

        Player player = (Player) event.getDamager();
        if (entity.getNoDamageTicks() > 0 || !PlotChecker.canInteractInPlot(player, entity.getLocation())) return;
        if (OnlineTimeAPI.getLevel(player) < spawner.getSpawner().getRequiredLevel()) {
            player.sendMessage(Messages.LOCKED_SPAWNER);
            return;
        }

        entity.setNoDamageTicks(10);

        boolean isKillAll = DataManager.getInstance().loadPlayerData(player).isKillAll();
        BigInteger stack = new BigInteger(entity.getMetadata("MobAmount").get(0).asString());
        BigInteger amountToKill = isKillAll ? stack : BigInteger.ONE;
        BigInteger dropAmount = setLooting(amountToKill, getLootingBonuses(player.getItemInHand()));

        EntityManager.removeStack(entity, amountToKill, spawner);
        dropOrStackSpawnerDrops(spawner, dropAmount, entity.getLocation());

        if (player.getItemInHand() == null || player.getItemInHand().getEnchantmentLevel(Enchantment.LOOT_BONUS_MOBS) > 0) return;

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

    @EventHandler(priority = EventPriority.LOWEST)
    public void onDeath(EntityDeathEvent event) {
        if (!event.getEntity().hasMetadata("MobAmount")) return;

        event.getDrops().clear();
        event.getEntity().remove();
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onTeleport(EntityTeleportEvent event) {
        if (!event.getEntity().hasMetadata("MobAmount")) return;

        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onEggSpawn(ItemSpawnEvent event) {
        if (!event.getEntity().getItemStack().getType().equals(Material.EGG)) return;

        event.setCancelled(true);
    }

    private void dropOrStackSpawnerDrops(PlacedSpawner spawner, BigInteger amount, Location location) {
        dropsLoop: for (Drop drop : spawner.getSpawner().getDrops()) {
            for (Entity nearbyItem : location.getWorld().getNearbyEntities(location, STACK_RADIUS, STACK_RADIUS, STACK_RADIUS).stream().filter(
                    nearbyItem -> nearbyItem instanceof Item &&
                            nearbyItem.hasMetadata("DropID") &&
                            ((Item) nearbyItem).getItemStack().isSimilar(drop.getDisplayItem())
            ).collect(Collectors.toList())) {
                long dropID = nearbyItem.getMetadata("DropID").get(0).asLong();
                DropItem dropItem = DataManager.getInstance().getCache().getDrops().get(dropID);
                if (dropItem == null) continue;

                BigInteger newDropStackAmount = dropItem.getStackAmount().add(amount);
                dropItem.setStackAmount(newDropStackAmount);
                continue dropsLoop;
            }

            double chance = drop.getChance();
            double number = ThreadLocalRandom.current().nextDouble(0, 100);
            if (number > chance) continue;

            DropItem dropItem = new DropItem(drop, spawner, drop.isSingleDrop() ? BigInteger.ONE : amount);
            dropItem.dropAndCache(location);
        }
    }

    private int getLootingBonuses(ItemStack item) {
        if (item == null || item.getType().equals(Material.AIR) || !item.hasItemMeta()) return 0;

        return item.getEnchantmentLevel(Enchantment.LOOT_BONUS_MOBS);
    }

    private BigInteger setLooting(BigInteger value, int looting) {
        if (looting <= 0) return value;

        return value.add(value.multiply(new BigInteger(Settings.LOOTING_BONUS))).multiply(BigInteger.valueOf(looting));
    }
}
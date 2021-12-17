package com.zpedroo.voltzspawners.listeners;

import com.gmail.nossr50.api.ExperienceAPI;
import com.gmail.nossr50.datatypes.skills.SkillType;
import com.gmail.nossr50.datatypes.skills.XPGainReason;
import com.zpedroo.voltzspawners.managers.DataManager;
import com.zpedroo.voltzspawners.managers.EntityManager;
import com.zpedroo.voltzspawners.objects.Drop;
import com.zpedroo.voltzspawners.objects.PlacedSpawner;
import com.zpedroo.voltzspawners.utils.config.Settings;
import com.zpedroo.voltzspawners.utils.serialization.LocationSerialization;
import org.apache.commons.lang.StringUtils;
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
import java.util.stream.Collectors;

import static com.zpedroo.voltzspawners.utils.config.Settings.*;

public class EntityListeners implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof LivingEntity)) return;
        if (!(event.getDamager() instanceof Player)) return;
        if (event.isCancelled()) return;

        Player player = (Player) event.getDamager();
        LivingEntity entity = (LivingEntity) event.getEntity();

        if (!(event.getDamager() instanceof Player)) return;
        if (!entity.hasMetadata("MobAmount")) return;

        String serialized = entity.getMetadata("Spawner").get(0).asString();
        PlacedSpawner spawner = DataManager.getInstance().getPlacedSpawner(LocationSerialization.deserializeLocation(serialized));
        if (spawner == null) return;
        if (!spawner.canInteract(player) && !spawner.isPublic()) {
            event.setCancelled(true);
            return;
        }
        if (entity.getHealth() - event.getDamage() > 0) return;

        event.setCancelled(true);
        event.setDamage(0);
        entity.setHealth(entity.getMaxHealth());

        BigInteger stack = new BigInteger(entity.getMetadata("MobAmount").get(0).asString());
        BigInteger dropAmount = setLooting(spawner.getSpawner().getDropsAmount().multiply(stack), getLootingBonuses(player.getItemInHand()));

        EntityManager.removeStack(entity, stack, spawner);

        dropsLoop: for (ItemStack dropItem : spawner.getSpawner().getDrops()) {
            for (Entity nearbyItem : entity.getNearbyEntities(STACK_RADIUS, STACK_RADIUS, STACK_RADIUS).stream().filter(
                    nearbyItem -> nearbyItem instanceof Item &&
                            nearbyItem.hasMetadata("DropID") &&
                            ((Item) nearbyItem).getItemStack().isSimilar(dropItem)
            ).collect(Collectors.toList())) {
                int dropID = nearbyItem.getMetadata("DropID").get(0).asInt();
                Drop drop = DataManager.getInstance().getCache().getDrops().get(dropID);
                if (drop == null) continue;

                BigInteger newDropStackAmount = drop.getStackAmount().add(dropAmount);
                drop.setStackAmount(newDropStackAmount);
                continue dropsLoop;
            }

            Drop drop = new Drop(dropItem, spawner, dropAmount);
            drop.drop(entity.getLocation());
        }

        SkillType skillType = null;
        if (player.getItemInHand() == null) return;
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

    private Integer getLootingBonuses(ItemStack item) {
        if (item == null || item.getType().equals(Material.AIR) || !item.hasItemMeta()) return 0;

        return item.getEnchantmentLevel(Enchantment.LOOT_BONUS_MOBS);
    }

    private BigInteger setLooting(BigInteger value, int looting) {
        if (looting <= 0) return value;

        return value.add(value.multiply(new BigInteger(Settings.LOOTING_BONUS).divide(BigInteger.valueOf(100L))).multiply(BigInteger.valueOf(looting)));
    }
}
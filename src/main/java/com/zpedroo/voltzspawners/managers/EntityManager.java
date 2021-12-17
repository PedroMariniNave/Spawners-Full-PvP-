package com.zpedroo.voltzspawners.managers;

import com.zpedroo.voltzspawners.VoltzSpawners;
import com.zpedroo.voltzspawners.objects.PlacedSpawner;
import com.zpedroo.voltzspawners.utils.config.Settings;
import com.zpedroo.voltzspawners.utils.formatter.NumberFormatter;
import com.zpedroo.voltzspawners.utils.serialization.LocationSerialization;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.entity.*;
import org.bukkit.metadata.FixedMetadataValue;

import java.math.BigInteger;
import java.util.Random;

public class EntityManager {

    public static void spawn(PlacedSpawner spawner, BigInteger amount) {
        int radius = Settings.STACK_RADIUS * 2;

        for (Entity near : spawner.getLocation().getWorld().getNearbyEntities(spawner.getLocation(), radius, radius, radius)) {
            if (near == null || !near.getType().equals(spawner.getSpawner().getEntityType())) continue;
            if (!near.hasMetadata("Spawner")) continue;

            String serialized = LocationSerialization.serializeLocation(spawner.getLocation());
            if (!StringUtils.equals(near.getMetadata("Spawner").get(0).asString(), serialized)) continue;

            final BigInteger stack = new BigInteger(near.getMetadata("MobAmount").get(0).asString());
            BigInteger newStack = stack.add(amount);

            near.setMetadata("MobAmount", new FixedMetadataValue(VoltzSpawners.get(), newStack.toString()));
            near.setMetadata("Spawner", new FixedMetadataValue(VoltzSpawners.get(), serialized));

            near.setCustomName(StringUtils.replaceEach(spawner.getSpawner().getEntityName(), new String[]{
                    "{stack}"
            }, new String[]{
                    NumberFormatter.getInstance().format(newStack)
            }));
            spawner.addEntity(near);
            return;
        }

        int minRange = 1;
        int maxRange = 3;

        Random random = new Random();
        double x = spawner.getLocation().getX() + random.nextDouble() * (maxRange - minRange) + 0.5D;
        double y = spawner.getLocation().getY() + 3D; // fix spawn bugs
        double z = spawner.getLocation().getZ() + random.nextDouble() * (maxRange - minRange) + 0.5D;

        Location location = new Location(spawner.getLocation().getWorld(), x, y, z);

        while (!canSpawn(location) && location.getY() != spawner.getLocation().getY()) {
            location.setY(location.getY() - 1);
        }

        LivingEntity entity = (LivingEntity) spawner.getLocation().getWorld().spawnEntity(location, spawner.getSpawner().getEntityType());
        entity.setMetadata("MobAmount", new FixedMetadataValue(VoltzSpawners.get(), amount.toString()));
        entity.setMetadata("Spawner", new FixedMetadataValue(VoltzSpawners.get(), LocationSerialization.serializeLocation(spawner.getLocation())));
        entity.setCustomName(StringUtils.replaceEach(spawner.getSpawner().getEntityName(), new String[]{
                "{stack}"
        }, new String[]{
                NumberFormatter.getInstance().format(amount)
        }));

        spawner.addEntity(entity);
        disableAI(entity);
        entity.setRemoveWhenFarAway(false);
        ((CraftEntity) entity).getHandle().b(true); // silent true

        switch (entity.getType()) {
            case MAGMA_CUBE:
                ((MagmaCube) entity).setSize(2);
            case SLIME:
                ((Slime) entity).setSize(2);
                break;
            case ZOMBIE:
                ((Zombie) entity).setBaby(true);
                break;
            case SHEEP:
                ((Sheep) entity).setBaby();
                ((Sheep) entity).setColor(DyeColor.ORANGE);
                ((Ageable) entity).setAgeLock(true);
                break;
            case COW:
                ((Cow) entity).setBaby();
                ((Ageable) entity).setAgeLock(true);
                break;
            case PIG:
                ((Pig) entity).setBaby();
                ((Ageable) entity).setAgeLock(true);
                break;
            case MUSHROOM_COW:
                ((MushroomCow) entity).setBaby();
                ((Ageable) entity).setAgeLock(true);
                break;
        }
    }

    public static void removeStack(Entity entity, BigInteger amount, PlacedSpawner placedSpawner) {
        String spawner = entity.getMetadata("Spawner").get(0).asString();
        BigInteger stack = new BigInteger(entity.getMetadata("MobAmount").get(0).asString());
        BigInteger newStack = stack.subtract(amount);
        if (newStack.signum() <= 0) {
            entity.remove();
            return;
        }

        entity.setMetadata("MobAmount", new FixedMetadataValue(VoltzSpawners.get(), newStack.toString()));
        entity.setMetadata("Spawner", new FixedMetadataValue(VoltzSpawners.get(), spawner));
        entity.setCustomName(ChatColor.translateAlternateColorCodes('&', StringUtils.replaceEach(placedSpawner.getSpawner().getEntityName(), new String[]{
                "{stack}"
        }, new String[]{
                NumberFormatter.getInstance().format(newStack)
        })));
    }

    private static void disableAI(Entity entity) {
        net.minecraft.server.v1_8_R3.Entity nmsEntity = ((CraftEntity) entity).getHandle();
        NBTTagCompound tag = nmsEntity.getNBTTag();
        if (tag == null) {
            tag = new NBTTagCompound();
        }

        nmsEntity.c(tag);
        tag.setInt("NoAI", 1);
        nmsEntity.f(tag);
    }

    private static boolean canSpawn(Location location) {
        Block block = location.getBlock().getRelative(BlockFace.DOWN);

        return !block.getType().equals(Material.AIR) && !block.getType().toString().contains("SLAB");
    }
}
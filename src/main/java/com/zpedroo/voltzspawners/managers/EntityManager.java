package com.zpedroo.voltzspawners.managers;

import com.zpedroo.voltzspawners.VoltzSpawners;
import com.zpedroo.voltzspawners.objects.PlacedSpawner;
import com.zpedroo.voltzspawners.utils.formatter.NumberFormatter;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftAgeable;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.entity.*;
import org.bukkit.metadata.FixedMetadataValue;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.util.Random;

import static com.zpedroo.voltzspawners.utils.config.Settings.STACK_RADIUS;

public class EntityManager {

    public static void spawn(PlacedSpawner spawner, BigInteger amount) {
        Location location = spawner.getLocation();
        for (Entity nearbyEntity : location.getWorld().getNearbyEntities(location, STACK_RADIUS, STACK_RADIUS, STACK_RADIUS)) {
            if (!(nearbyEntity instanceof LivingEntity)) continue;
            if (!nearbyEntity.getType().equals(spawner.getSpawner().getEntityType())) continue;
            if (!nearbyEntity.hasMetadata("Spawner")) continue;

            BigInteger stack = new BigInteger(nearbyEntity.getMetadata("MobAmount").get(0).asString());
            BigInteger newStack = stack.add(amount);

            setEntityMetadata(spawner, newStack, (LivingEntity) nearbyEntity);
            setEntityName((LivingEntity) nearbyEntity, spawner, newStack);
            spawner.addEntity(nearbyEntity);
            return;
        }

        Location spawnLocation = getRandomLocation(spawner);
        int tryLimit = 10;
        while (--tryLimit > 0 && !canSpawn(spawnLocation)) spawnLocation.setY(spawnLocation.getY() - 1D);

        LivingEntity entity = (LivingEntity)spawner.getLocation().getWorld().spawnEntity(spawnLocation, spawner.getSpawner().getEntityType());
        setEntityMetadata(spawner, amount, entity);
        setEntityName(entity, spawner, amount);
        spawner.addEntity(entity);
        disableAI(entity);
        disableEntitySounds((CraftEntity) entity);
        fixEntitySize(entity);
    }

    @NotNull
    private static Location getRandomLocation(PlacedSpawner spawner) {
        int minRange = 1;
        int maxRange = 3;
        Random random = new Random();
        double x = spawner.getLocation().getX() + random.nextDouble() * (maxRange - minRange) + 1.5D;
        double y = spawner.getLocation().getY() + 3D;
        double z = spawner.getLocation().getZ() + random.nextDouble() * (maxRange - minRange) + 1.5D;
        return new Location(spawner.getLocation().getWorld(), x, y, z);
    }

    public static void removeStack(LivingEntity entity, BigInteger amount, PlacedSpawner spawner) {
        BigInteger stack = new BigInteger(entity.getMetadata("MobAmount").get(0).asString());
        BigInteger newStack = stack.subtract(amount);
        if (newStack.signum() <= 0) {
            entity.remove();
            return;
        }

        setEntityMetadata(spawner, newStack, entity);
        setEntityName(entity, spawner, newStack);
    }

    private static void setEntityName(LivingEntity entity, PlacedSpawner spawner, BigInteger newStack) {
        entity.setCustomName(StringUtils.replace(spawner.getSpawner().getEntityName(), "{stack}", NumberFormatter.getInstance().format(newStack)));
    }

    private static void setEntityMetadata(PlacedSpawner spawner, BigInteger amount, LivingEntity entity) {
        entity.setMetadata("MobAmount", new FixedMetadataValue(VoltzSpawners.get(), amount.toString()));
        entity.setMetadata("Spawner", new FixedMetadataValue(VoltzSpawners.get(), SerializatorManager.getLocationSerialization().serialize(spawner.getLocation())));
    }
    
    private static void disableAI(Entity entity) {
        net.minecraft.server.v1_8_R3.Entity nmsEntity = ((CraftEntity)entity).getHandle();
        NBTTagCompound tag = nmsEntity.getNBTTag();
        if (tag == null) tag = new NBTTagCompound();

        nmsEntity.c(tag);
        tag.setInt("NoAI", 1);
        nmsEntity.f(tag);
    }

    private static void disableEntitySounds(CraftEntity entity) {
        entity.getHandle().b(true);
    }

    private static void fixEntitySize(LivingEntity entity) {
        switch (entity.getType()) {
            case COW:
            case PIG:
            case CHICKEN:
            case RABBIT:
            case WOLF:
            case OCELOT:
            case MUSHROOM_COW:
                ((CraftAgeable) entity).setAdult();
                break;
            case ZOMBIE:
                ((Zombie) entity).setBaby(false);
                break;
            case MAGMA_CUBE:
                ((MagmaCube) entity).setSize(3);
                break;
            case SLIME:
                ((Slime) entity).setSize(3);
                break;
        }
    }
    
    private static boolean canSpawn(Location location) {
        Block block = location.getBlock().getRelative(BlockFace.DOWN);
        return !block.getType().equals(Material.AIR) && !block.getType().toString().contains("SLAB");
    }
}
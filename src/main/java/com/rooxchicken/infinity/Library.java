package com.rooxchicken.infinity;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.RayTraceResult;

import com.google.common.base.Predicate;

public class Library
{
    /*
     * GUIDE:
     * _ seperates parts of data
     * 
     * 1: infb63_ marks the signature for data
     * 2: mode: (0 is for login. marks ability type) (1 marks ability cooldown) (2 sends tree data) (3 opens selection dialogue)
     * 2.0_1: marks ability type
     * 2.1_1: ability type (0 for primary, 1 for unlockable)
     * 2.1_2_M: cooldown length (in ticks)
     * 2.2 format: tex (n for null), posX, posY, description (n for none), render, unlocked
     * 2.3: open selection dialogue (preceded by sending each ability)
     */
    public static void sendPlayerData(Player player, String data)
    {
        player.sendMessage("infb63_" + data);
    }

    public static void checkHasPoints(Player player)
    {
        PersistentDataContainer data = player.getPersistentDataContainer();
        if(!data.has(Infinity.pointsKey, PersistentDataType.INTEGER))
            data.set(Infinity.pointsKey, PersistentDataType.INTEGER, 1);
    }

    public static int getPoints(Player player)
    {
        PersistentDataContainer data = player.getPersistentDataContainer();
        checkHasPoints(player);

        return data.get(Infinity.pointsKey, PersistentDataType.INTEGER);
    }

    public static void subtractPoint(Player player)
    {
        PersistentDataContainer data = player.getPersistentDataContainer();
        checkHasPoints(player);

        data.set(Infinity.pointsKey, PersistentDataType.INTEGER, data.get(Infinity.pointsKey, PersistentDataType.INTEGER) - 1);
    }

    public static void addPoint(Player player)
    {
        PersistentDataContainer data = player.getPersistentDataContainer();
        checkHasPoints(player);

        data.set(Infinity.pointsKey, PersistentDataType.INTEGER, data.get(Infinity.pointsKey, PersistentDataType.INTEGER) + 1);
    }

    public static void setPoints(Player player, int points)
    {
        PersistentDataContainer data = player.getPersistentDataContainer();
        data.set(Infinity.pointsKey, PersistentDataType.INTEGER, points);
    }
    
    public static Entity getTarget(Player player, int range)
    {
        Predicate<Entity> p = new Predicate<Entity>() {

            @Override
            public boolean apply(Entity input)
            {
                return(input != player);
            }
            
        };
        RayTraceResult ray = player.getWorld().rayTrace(player.getEyeLocation(), player.getLocation().getDirection(), range, FluidCollisionMode.NEVER, true, 0.2, p);
        
        if(ray != null)
            return ray.getHitEntity();
        else
            return null;
    }

    public static Block getBlock(Player player, int range)
    {
        return getBlock(player, range, player.getLocation().getPitch());
    }

    public static Block getBlock(Player player, int range, float pitch)
    {
        Predicate<Entity> p = new Predicate<Entity>() {

            @Override
            public boolean apply(Entity input)
            {
                return(input != player);
            }
            
        };

        Location dir = player.getLocation().clone();
        dir.setPitch(pitch);

        RayTraceResult ray = player.getWorld().rayTrace(player.getEyeLocation(), dir.getDirection(), range, FluidCollisionMode.NEVER, true, 0.2, p);
        
        if(ray != null)
            return ray.getHitBlock();
        else
            return null;
    }

    public static List<Block> getSphereBlocks(Location location, int radius)
    {
        List<Block> blocks = new ArrayList<>();

        int bx = location.getBlockX();
        int by = location.getBlockY();
        int bz = location.getBlockZ();

        for (int x = bx - radius; x <= bx + radius; x++)
        {
            for (int y = by - radius; y <= by + radius; y++)
            {
                for (int z = bz - radius; z <= bz + radius; z++)
                {
                    double distance = ((bx - x) * (bx - x) + (bz - z) * (bz - z) + (by - y) * (by - y));
                    if (distance < radius * radius && (distance < (radius - 1) * (radius - 1)))
                    {
                        Block block = new Location(location.getWorld(), x, y, z).getBlock();
                        if(block.getType() != Material.AIR && block.getType() != Material.BEDROCK)
                            blocks.add(block);
                    }
                }
            }
        }

        return blocks;
    }

    public static Object[] getNearbyEntities(Location where, int range)
    {
        return where.getWorld().getNearbyEntities(where, range, range, range).toArray();
    }

    public static double ClampD(double v, double min, double max)
    {
        if(v < min)
            return min;
        else if(v > max)
            return max;
        else
            return v;
    }
}

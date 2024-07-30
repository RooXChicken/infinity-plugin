package com.rooxchicken.infinity;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
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

    public static void sendGlobalData(String data)
    {
        for(Player player : Bukkit.getOnlinePlayers())
            player.sendMessage("infb63_" + data);
    }

    public static void sendSkillTree(Player player)
    {
        sendPlayerData(player, "2_srt_Menu_0_1.0_1.0_1.0_false_3.0");
        sendPlayerData(player, "2_menu/strength_-42_0_Strength Class_true_true_false_false_0");
        sendPlayerData(player, "2_menu/speed_-21_0_Speed Class_true_true_false_false_1");
        sendPlayerData(player, "2_menu/health_0_0_Health Class_true_true_false_false_2");
        sendPlayerData(player, "2_menu/luck_21_0_Luck Class_true_true_false_false_3");
        sendPlayerData(player, "2_menu/stealth_42_0_Stealth Class_true_true_false_false_4");
        sendPlayerData(player, "3_" + Library.getPoints(player) + "_true");
    }

    public static int getPointMax(Player player)
    {
        int max = 6;
        for(ItemStack item : player.getInventory().getContents())
        {
            if(item != null && item.hasItemMeta() && item.getItemMeta().equals(Infinity.unlimiter.getItemMeta()))
                max += getKills(player);

            if(item != null && item.hasItemMeta() && item.getItemMeta().equals(Infinity.extra.getItemMeta()))
                max += item.getAmount();
        }

        return max;
    }

    public static void checkHasKills(Player player)
    {
        PersistentDataContainer data = player.getPersistentDataContainer();
        if(!data.has(Infinity.killsKey, PersistentDataType.INTEGER))
            data.set(Infinity.killsKey, PersistentDataType.INTEGER, 0);
    }

    public static void resetKills(Player player)
    {
        PersistentDataContainer data = player.getPersistentDataContainer();
        checkHasKills(player);

        data.set(Infinity.killsKey, PersistentDataType.INTEGER, 0);
    }

    public static void addKill(Player player)
    {
        PersistentDataContainer data = player.getPersistentDataContainer();
        checkHasKills(player);

        data.set(Infinity.killsKey, PersistentDataType.INTEGER, data.get(Infinity.killsKey, PersistentDataType.INTEGER) + 1);
    }

    public static int getKills(Player player)
    {
        PersistentDataContainer data = player.getPersistentDataContainer();
        checkHasKills(player);

        return data.get(Infinity.killsKey, PersistentDataType.INTEGER);
    }

    public static void checkHasPoints(Player player)
    {
        PersistentDataContainer data = player.getPersistentDataContainer();
        if(!data.has(Infinity.pointsKey, PersistentDataType.INTEGER))
            data.set(Infinity.pointsKey, PersistentDataType.INTEGER, 0);
    }

    public static int getPoints(Player player)
    {
        PersistentDataContainer data = player.getPersistentDataContainer();
        checkHasPoints(player);

        int count = 0;
        for(ItemStack item : player.getInventory().getContents())
            if(item != null && item.hasItemMeta() && item.getItemMeta().equals(Infinity.token.getItemMeta()))
                count += item.getAmount();

        return count;
        //return data.get(Infinity.pointsKey, PersistentDataType.INTEGER);
    }

    public static void subtractPoint(Player player)
    {
        PersistentDataContainer data = player.getPersistentDataContainer();
        checkHasPoints(player);
        data.set(Infinity.pointsKey, PersistentDataType.INTEGER, data.get(Infinity.pointsKey, PersistentDataType.INTEGER) + 1);

        for(ItemStack item : player.getInventory().getContents())
            if(item != null && item.hasItemMeta() && item.getItemMeta().equals(Infinity.token.getItemMeta()))
            {
                item.setAmount(item.getAmount() - 1);
                return;
            }

    }

    public static void addPoint(Player player)
    {
        PersistentDataContainer data = player.getPersistentDataContainer();
        checkHasPoints(player);
        data.set(Infinity.pointsKey, PersistentDataType.INTEGER, data.get(Infinity.pointsKey, PersistentDataType.INTEGER) - 1);

        player.getInventory().addItem(Infinity.token);

    }

    public static boolean canSpend(Player player)
    {
        PersistentDataContainer data = player.getPersistentDataContainer();
        checkHasPoints(player);


        return (data.get(Infinity.pointsKey, PersistentDataType.INTEGER) < getPointMax(player));
    }

    public static void setPoints(Player player, int points)
    {
        PersistentDataContainer data = player.getPersistentDataContainer();

        for(int i = 0; i < points; i++)
            player.getInventory().addItem(Infinity.token);
        //data.set(Infinity.pointsKey, PersistentDataType.INTEGER, points);
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

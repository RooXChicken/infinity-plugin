package com.rooxchicken.infinity.Abilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Warden;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDamageEvent.DamageModifier;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.Pair;
import com.rooxchicken.infinity.Infinity;
import com.rooxchicken.infinity.Library;
import com.rooxchicken.infinity.Data.Node;
import com.rooxchicken.infinity.Data.PlayerSelectGUI;

public class LuckClass extends Ability
{
    private Infinity plugin;
    public int type = -1;

    private HashMap<Player, Integer> playerSneaktimeMap;
    private HashMap<Player, PotionEffect> playerEffectMap;
    private HashMap<Player, Boolean> playerSwapMap;
    
    private NamespacedKey ability7CooldownKey;

    private NamespacedKey node0AbilityKey;
    private NamespacedKey node1AbilityKey;
    private NamespacedKey node2AbilityKey;
    private NamespacedKey node3AbilityKey;
    private NamespacedKey node4AbilityKey;
    private NamespacedKey node5AbilityKey;
    private NamespacedKey node6AbilityKey;
    private NamespacedKey node7AbilityKey;
    private NamespacedKey node8AbilityKey;

    private ArrayList<Material> doubleBlockList;
    private HashMap<Player, ItemStack> playerProjectileMap;

    public LuckClass(Infinity _plugin)
    {
        super(_plugin);
        plugin = _plugin;
        
        playerNodeMap = new HashMap<Player, ArrayList<Node>>();
        playerProjectileMap = new HashMap<Player, ItemStack>();
        playerEffectMap = new HashMap<Player, PotionEffect>();
        playerSwapMap = new HashMap<Player, Boolean>();
        playerSneaktimeMap = new HashMap<Player, Integer>();
        doubleBlockList = new ArrayList<Material>();

        doubleBlockList.add(Material.RAW_IRON);
        doubleBlockList.add(Material.RAW_COPPER);
        doubleBlockList.add(Material.COAL);
        doubleBlockList.add(Material.RAW_GOLD);
        doubleBlockList.add(Material.DIAMOND);
        doubleBlockList.add(Material.GOLD_NUGGET);
        doubleBlockList.add(Material.QUARTZ);
        doubleBlockList.add(Material.REDSTONE);
        doubleBlockList.add(Material.LAPIS_LAZULI);
        doubleBlockList.add(Material.EMERALD);
        nodeList = new ArrayList<Node>();
        
        name = "Luck";
        header = "2_srt_Luck_4_0.2_0.8_0.2_true_1.0";

        nodeList.add(new Node(_plugin, "luck", "n", "n", 0, 41, -1, false, false, null, null, null, null));
        nodeList.add(new Node(_plugin, "luck", "icons/52", "Luck 3 Effect", 0, 40, 0, true, false, this::node0Learn, this::node0Unlearn, this::node0Status, this::node0CanUnlearn));

        nodeList.add(new Node(_plugin, "luck", "lline", "n", -14, 35, -1, false, true, null, null, null, null));
        nodeList.add(new Node(_plugin, "luck", "luarrow", "n", -14, 35, -1, false, true, null, null, null, null));
        nodeList.add(new Node(_plugin, "luck", "rline", "n", 12, 35, -1, false, true, null, null, null, null));
        nodeList.add(new Node(_plugin, "luck", "ruarrow", "n", 12, 35, -1, false, true, null, null, null, null));

        nodeList.add(new Node(_plugin, "luck", "n", "n", -20, 19, -1, false, true, null, null, null, null));
        nodeList.add(new Node(_plugin, "luck", "icons/19", "Ore drops are doubled", -20, 20, 1, true, false, this::node1Learn, this::node1Unlearn, this::node1Status, this::node1CanUnlearn));        
        nodeList.add(new Node(_plugin, "luck", "n", "n", -20, -10, -1, false, false, null, null, null, null));
        nodeList.add(new Node(_plugin, "luck", "rline", "n", -5, 13, -1, false, true, null, null, null, null));
        nodeList.add(new Node(_plugin, "luck", "rline", "n", 0, 8, -1, false, true, null, null, null, null));
        nodeList.add(new Node(_plugin, "luck", "rline", "n", 5, 3, -1, false, true, null, null, null, null));


        nodeList.add(new Node(_plugin, "luck", "n", "n", 20, 19, -1, false, true, null, null, null, null));
        nodeList.add(new Node(_plugin, "luck", "icons/18", "Mob drops are doubled", 20, 20, 2, true, false, this::node2Learn, this::node2Unlearn, this::node2Status, this::node2CanUnlearn));
        nodeList.add(new Node(_plugin, "luck", "n", "n", 20, 0, -1, false, false, null, null, null, null));
        nodeList.add(new Node(_plugin, "luck", "lline", "n", 5, 13, -1, false, true, null, null, null, null));
        nodeList.add(new Node(_plugin, "luck", "lline", "n", 0, 8, -1, false, true, null, null, null, null));
        nodeList.add(new Node(_plugin, "luck", "lline", "n", -5, 3, -1, false, true, null, null, null, null));

        nodeList.add(new Node(_plugin, "luck", "n", "n", 20, -4, -1, false, true, null, null, null, null));
        nodeList.add(new Node(_plugin, "luck", "icons/32", "25% chance not to consume items on use", 20, -5, 3, true, false, this::node3Learn, this::node3Unlearn, this::node3Status, this::node3CanUnlearn));
        nodeList.add(new Node(_plugin, "luck", "rarrow", "n", 35, -5, -1, false, false, null, null, null, null));
        nodeList.add(new Node(_plugin, "luck", "n", "n", 45, -5, -1, false, false, null, null, null, null));
        nodeList.add(new Node(_plugin, "luck", "n", "n", 20, -5, -1, false, true, null, null, null, null));
        nodeList.add(new Node(_plugin, "luck", "uarrow", "n", 20, -20, -1, false, false, null, null, null, null));
        nodeList.add(new Node(_plugin, "luck", "n", "n", 20, -30, -1, false, false, null, null, null, null));
        
        nodeList.add(new Node(_plugin, "luck", "icons/53", "All entities bounce off of you", 50, -5, 5, true, true, this::node5Learn, this::node5Unlearn, this::node5Status, this::node5CanUnlearn));
        nodeList.add(new Node(_plugin, "luck", "icons/31", "25% chance for newly gained Potion effects to be doubled in length", 20, -35, 6, true, true, this::node6Learn, this::node6Unlearn, this::node6Status, this::node6CanUnlearn));


        nodeList.add(new Node(_plugin, "luck", "n", "n", -20, -4, -1, false, true, null, null, null, null));
        nodeList.add(new Node(_plugin, "luck", "icons/38", "Gain XP 1.2x faster", -20, -5, 4, true, false, this::node4Learn, this::node4Unlearn, this::node4Status, this::node4CanUnlearn));
        nodeList.add(new Node(_plugin, "luck", "larrow", "n", -35, -5, -1, false, false, null, null, null, null));
        nodeList.add(new Node(_plugin, "luck", "n", "n", -45, -5, -1, false, false, null, null, null, null));
        nodeList.add(new Node(_plugin, "luck", "n", "n", -20, -5, -1, false, true, null, null, null, null));
        nodeList.add(new Node(_plugin, "luck", "uarrow", "n", -20, -20, -1, false, false, null, null, null, null));
        nodeList.add(new Node(_plugin, "luck", "n", "n", -20, -30, -1, false, false, null, null, null, null));

        nodeList.add(new Node(_plugin, "luck", "icons/55", "Swap places with the entity you attack (COOLDOWN: 3m, TOGGLE WITH FAST SHIFT -> UNSHIFT)", -50, -5, 7, true, true, this::node7Learn, this::node7Unlearn, this::node7Status, this::node7CanUnlearn));
        nodeList.add(new Node(_plugin, "luck", "icons/8", "7% chance to Evade an attack", -20, -35, 8, true, true, this::node8Learn, this::node8Unlearn, this::node8Status, this::node8CanUnlearn));

        ability7CooldownKey = new NamespacedKey(_plugin, "luck_ability7CD");

        node0AbilityKey = new NamespacedKey(_plugin, "luck_0Ability");
        node1AbilityKey = new NamespacedKey(_plugin, "luck_1Ability");
        node2AbilityKey = new NamespacedKey(_plugin, "luck_2Ability");
        node3AbilityKey = new NamespacedKey(_plugin, "luck_3Ability");
        node4AbilityKey = new NamespacedKey(_plugin, "luck_4Ability");
        node5AbilityKey = new NamespacedKey(_plugin, "luck_5Ability");
        node6AbilityKey = new NamespacedKey(_plugin, "luck_6Ability");
        node7AbilityKey = new NamespacedKey(_plugin, "luck_7Ability");
        node8AbilityKey = new NamespacedKey(_plugin, "luck_8Ability");
    }

    public Node findNode(Player player, String icon)
    {
        for(int i = 0; i < playerNodeMap.get(player).size(); i++)
        {
            String name = "icons/" + icon;
            if(playerNodeMap.get(player).get(i).icon.equals(name))
            {
                return playerNodeMap.get(player).get(i);
            }
        }

        return null;
    }

    public Node findNode(Player player, int clickIndex)
    {
        for(int i = 0; i < playerNodeMap.get(player).size(); i++)
        {
            if(playerNodeMap.get(player).get(i).clickIndex == clickIndex)
                return playerNodeMap.get(player).get(i);
        }

        return null;
    }

    // public void resetCooldown(Player player)
    // {
    //     player.getPersistentDataContainer().set(ability8CooldownKey, PersistentDataType.INTEGER, 0);
    //     player.getPersistentDataContainer().set(ability3CooldownKey, PersistentDataType.INTEGER, 0);
    // }

    @EventHandler
    private void doublePotionLength(EntityPotionEffectEvent event)
    {
        if(event.getNewEffect() == null || !(event.getEntity() instanceof Player))
            return;

        Player player = (Player)event.getEntity();
        PersistentDataContainer data = player.getPersistentDataContainer();

        if(data.has(node6AbilityKey, PersistentDataType.BOOLEAN) && data.get(node6AbilityKey, PersistentDataType.BOOLEAN) && Math.random() < 0.25)
        {
            PotionEffect potion = event.getNewEffect();
            if(!playerEffectMap.containsKey(player))
                playerEffectMap.put(player, potion);
            
            if(comparePotionEffects(playerEffectMap.get(player), potion))
            {
                playerEffectMap.remove(player);
                playerEffectMap.put(player, potion);
                return;
            }

            playerEffectMap.remove(player);
            playerEffectMap.put(player, potion);

            player.addPotionEffect(new PotionEffect(potion.getType(), potion.getDuration() * 2, potion.getAmplifier()));
            event.setCancelled(true);
        }
    }

    private boolean comparePotionEffects(PotionEffect potion1, PotionEffect potion2)
    {
        PotionEffectType type1 = potion1.getType();
        PotionEffectType type2 = potion2.getType();

        int amplifier1 = potion1.getAmplifier();
        int amplifier2 = potion2.getAmplifier();

        int duration1 = potion1.getDuration();
        int duration2 = potion2.getDuration();

        return (type1.equals(type2) && amplifier1 == amplifier2 && duration1 * 2 == duration2);
    }

    @EventHandler
    public void preventConsumeEvent(ProjectileLaunchEvent event)
    {
        if(event.getEntity() instanceof Projectile && !(event.getEntity().getShooter() instanceof Player))
            return;
        
        Player player = (Player)event.getEntity().getShooter();
        PersistentDataContainer data = player.getPersistentDataContainer();

        if(data.has(node3AbilityKey, PersistentDataType.BOOLEAN) && data.get(node3AbilityKey, PersistentDataType.BOOLEAN) && Math.random() < 0.25)
        {
            ItemStack item = playerProjectileMap.get(player);
            item.setAmount(item.getAmount() + 1);
            player.updateInventory();
        }
    }

    @EventHandler
    public void getConsumedItem(PlayerInteractEvent event)
    {
        Player player = (Player)event.getPlayer();
        PersistentDataContainer data = player.getPersistentDataContainer();

        if(event.getItem() != null && data.has(node3AbilityKey, PersistentDataType.BOOLEAN) && data.get(node3AbilityKey, PersistentDataType.BOOLEAN))
        {
            if(playerProjectileMap.containsKey(player))
                playerProjectileMap.remove(player);
            
            playerProjectileMap.put(player, event.getItem());
        }
    }

    @EventHandler
    public void multiplyXPGain(PlayerExpChangeEvent event)
    {
        Player player = event.getPlayer();
        PersistentDataContainer data = player.getPersistentDataContainer();

        if(data.has(node4AbilityKey, PersistentDataType.BOOLEAN) && data.get(node4AbilityKey, PersistentDataType.BOOLEAN))
            event.setAmount((int)(event.getAmount() * 1.2));
    }

    @EventHandler
    public void doubleMobDrops(EntityDeathEvent event)
    {
        if(event.getEntity().getKiller() == null)
            return;

        Player player = event.getEntity().getKiller();
        PersistentDataContainer data = player.getPersistentDataContainer();

        if(data.has(node2AbilityKey, PersistentDataType.BOOLEAN) && data.get(node2AbilityKey, PersistentDataType.BOOLEAN))
        {
            for(ItemStack item : event.getDrops())
                item.setAmount(item.getAmount() * 2);
        }
    }

    @EventHandler
    public void doubleBlockDrops(BlockBreakEvent event)
    {
        if(event.getPlayer() == null)
            return;

        Player player = event.getPlayer();
        PersistentDataContainer data = player.getPersistentDataContainer();

        if(data.has(node1AbilityKey, PersistentDataType.BOOLEAN) && data.get(node1AbilityKey, PersistentDataType.BOOLEAN))
        {
            for(ItemStack item : event.getBlock().getDrops(player.getInventory().getItemInMainHand()))
            {
                if(doubleBlockList.contains(item.getType()))
                {
                    event.setDropItems(false);
                    ItemStack drop = item.clone();
                    drop.setAmount(drop.getAmount() * 2);
                    player.getWorld().dropItemNaturally(event.getBlock().getLocation(), drop);
                }
            }
        }
    }

    @EventHandler
    public void evade(EntityDamageByEntityEvent event)
    {
        if(!(event.getEntity() instanceof Player))
            return;

        Player player = (Player)event.getEntity();
        PersistentDataContainer data = player.getPersistentDataContainer();

        if(data.has(node8AbilityKey, PersistentDataType.BOOLEAN) && data.get(node8AbilityKey, PersistentDataType.BOOLEAN) && Math.random() < 0.07)
        {
            event.setCancelled(true);
            player.getWorld().spawnParticle(Particle.REDSTONE, player.getLocation().clone().add(0,1,0), 50, 0.3, 0.4, 0.3, new Particle.DustOptions(Color.GREEN, 1.0f));
        }
    }

    @EventHandler
    public void swapPlaces(EntityDamageByEntityEvent event)
    {
        if(!(event.getDamager() instanceof Player))
            return;

        Player player = (Player)event.getDamager();
        PersistentDataContainer data = player.getPersistentDataContainer();

        if(!playerSwapMap.containsKey(player))
            playerSwapMap.put(player, false);
        
        if(data.has(ability7CooldownKey, PersistentDataType.INTEGER) && data.get(ability7CooldownKey, PersistentDataType.INTEGER) <= 0 && playerSwapMap.get(player))
        {
            Location playerLoc = player.getLocation().clone();
            Location entityLoc = event.getEntity().getLocation().clone();

            event.getEntity().teleport(playerLoc);
            player.teleport(entityLoc);

            data.set(ability7CooldownKey, PersistentDataType.INTEGER, 3*60*20);
        }
    }

    public String tickAbilities(Player player)
    {
        String bar = "";
        PersistentDataContainer data = player.getPersistentDataContainer();

        if(data.has(node0AbilityKey, PersistentDataType.BOOLEAN) && data.get(node0AbilityKey, PersistentDataType.BOOLEAN))
            player.addPotionEffect(new PotionEffect(PotionEffectType.LUCK, 2, 2));

        if(data.has(node5AbilityKey, PersistentDataType.BOOLEAN) && data.get(node5AbilityKey, PersistentDataType.BOOLEAN))
        {
            for(Object o : Library.getNearbyEntities(player.getLocation(), 1))
            {
                Entity entity = (Entity)o;
                if(player != entity && player.getLocation().clone().add(0,1,0).distance(entity.getLocation()) < 1.8)
                    entity.setVelocity(entity.getVelocity().multiply(-1));
            }
        }

        if(data.has(node7AbilityKey, PersistentDataType.BOOLEAN) && data.get(node7AbilityKey, PersistentDataType.BOOLEAN))
        {
            if(!data.has(ability7CooldownKey, PersistentDataType.INTEGER))
                data.set(ability7CooldownKey, PersistentDataType.INTEGER, 0);

            int cooldown = data.get(ability7CooldownKey, PersistentDataType.INTEGER) - 1;
            data.set(ability7CooldownKey, PersistentDataType.INTEGER, cooldown);

            if(player.isSneaking())
            {
                if(!playerSneaktimeMap.containsKey(player))
                    playerSneaktimeMap.put(player, 0);

                int time = playerSneaktimeMap.get(player) + 1;

                playerSneaktimeMap.remove(player);
                playerSneaktimeMap.put(player, time);
            }
            else
            {
                if(playerSneaktimeMap.containsKey(player) && playerSneaktimeMap.get(player) < 3)
                {
                    boolean swap = false;
                    if(playerSwapMap.containsKey(player))
                    {
                        swap = playerSwapMap.get(player);
                        playerSwapMap.remove(player);
                    }
                    playerSwapMap.put(player, !swap);
                }

                playerSneaktimeMap.remove(player);
            }

            if(!playerSwapMap.containsKey(player))
                playerSwapMap.put(player, false);

            if(playerSwapMap.get(player))
                bar += "↔ " + ((cooldown < 0) ? "READY " : (cooldown/20 + "s "));
            else
                bar += "↔ INACTIVE ";

            
        }
        //bar += "↔";

        return bar;
    }

    public void node0Learn(Player player) { player.getPersistentDataContainer().set(node0AbilityKey, PersistentDataType.BOOLEAN, true); }
    public void node0Unlearn(Player player) { player.getPersistentDataContainer().set(node0AbilityKey, PersistentDataType.BOOLEAN, false); }
    public void node0Status(Player player, Node node) { node.locked = false; }
    public void node0CanUnlearn(Player player, Node node) { if(!findNode(player, 2).aquired && !findNode(player, 1).aquired) unlearnNode(player, node); }

    public void node1Learn(Player player) { player.getPersistentDataContainer().set(node1AbilityKey, PersistentDataType.BOOLEAN, true); }
    public void node1Unlearn(Player player) { player.getPersistentDataContainer().set(node1AbilityKey, PersistentDataType.BOOLEAN, false); }
    public void node1Status(Player player, Node node) { node.locked = !(findNode(player, 0).aquired && !findNode(player, 2).aquired); }
    public void node1CanUnlearn(Player player, Node node) { if(!findNode(player, 4).aquired && !findNode(player, 3).aquired) unlearnNode(player, node); }

    public void node2Learn(Player player) { player.getPersistentDataContainer().set(node2AbilityKey, PersistentDataType.BOOLEAN, true); }
    public void node2Unlearn(Player player) { player.getPersistentDataContainer().set(node2AbilityKey, PersistentDataType.BOOLEAN, false); }
    public void node2Status(Player player, Node node) { node.locked = !(findNode(player, 0).aquired && !findNode(player, 1).aquired); }
    public void node2CanUnlearn(Player player, Node node) { if(!findNode(player, 4).aquired && !findNode(player, 3).aquired) unlearnNode(player, node); }

    public void node3Learn(Player player) { player.getPersistentDataContainer().set(node3AbilityKey, PersistentDataType.BOOLEAN, true); }
    public void node3Unlearn(Player player) { player.getPersistentDataContainer().set(node3AbilityKey, PersistentDataType.BOOLEAN, false); }
    public void node3Status(Player player, Node node) { node.locked = !(findNode(player, 2).aquired || findNode(player, 1).aquired); }
    public void node3CanUnlearn(Player player, Node node) { if(!findNode(player, 5).aquired && !findNode(player, 6).aquired) unlearnNode(player, node); }

    public void node4Learn(Player player) { player.getPersistentDataContainer().set(node4AbilityKey, PersistentDataType.BOOLEAN, true); }
    public void node4Unlearn(Player player) { player.getPersistentDataContainer().set(node4AbilityKey, PersistentDataType.BOOLEAN, false); }
    public void node4Status(Player player, Node node) { node.locked = !(findNode(player, 1).aquired || findNode(player, 2).aquired); }
    public void node4CanUnlearn(Player player, Node node) { if(!findNode(player, 7).aquired && !findNode(player, 8).aquired) unlearnNode(player, node); }

    public void node5Learn(Player player) { player.getPersistentDataContainer().set(node5AbilityKey, PersistentDataType.BOOLEAN, true); }
    public void node5Unlearn(Player player) { player.getPersistentDataContainer().set(node5AbilityKey, PersistentDataType.BOOLEAN, false); }
    public void node5Status(Player player, Node node) { node.locked = !(findNode(player, 3).aquired && !findNode(player, 6).aquired && !findNode(player, 7).aquired && !findNode(player, 8).aquired); }
    public void node5CanUnlearn(Player player, Node node) { unlearnNode(player, node); }

    public void node6Learn(Player player) { player.getPersistentDataContainer().set(node6AbilityKey, PersistentDataType.BOOLEAN, true); }
    public void node6Unlearn(Player player) { player.getPersistentDataContainer().set(node6AbilityKey, PersistentDataType.BOOLEAN, false); }
    public void node6Status(Player player, Node node) { node.locked = !(findNode(player, 3).aquired && !findNode(player, 5).aquired && !findNode(player, 7).aquired && !findNode(player, 8).aquired); }
    public void node6CanUnlearn(Player player, Node node) { unlearnNode(player, node); }

    public void node7Learn(Player player) { player.getPersistentDataContainer().set(node7AbilityKey, PersistentDataType.BOOLEAN, true); }
    public void node7Unlearn(Player player) { player.getPersistentDataContainer().set(node7AbilityKey, PersistentDataType.BOOLEAN, false); }
    public void node7Status(Player player, Node node) { node.locked = !(findNode(player, 4).aquired && !findNode(player, 8).aquired && !findNode(player, 5).aquired && !findNode(player, 6).aquired); }
    public void node7CanUnlearn(Player player, Node node) { unlearnNode(player, node); }

    public void node8Learn(Player player) { player.getPersistentDataContainer().set(node8AbilityKey, PersistentDataType.BOOLEAN, true); }
    public void node8Unlearn(Player player) { player.getPersistentDataContainer().set(node8AbilityKey, PersistentDataType.BOOLEAN, false); }
    public void node8Status(Player player, Node node) { node.locked = !(findNode(player, 4).aquired && !findNode(player, 7).aquired && !findNode(player, 5).aquired && !findNode(player, 6).aquired); }
    public void node8CanUnlearn(Player player, Node node) { unlearnNode(player, node); }

}

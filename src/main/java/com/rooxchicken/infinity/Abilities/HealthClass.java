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
import org.bukkit.event.entity.EntityResurrectEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;
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
import com.rooxchicken.infinity.Tasks.Task;

public class HealthClass extends Ability
{
    private Infinity plugin;
    public int type = -1;

    private HashMap<Player, PotionEffect> playerEffectMap;
    private HashMap<PotionEffectType, PotionEffectType> badGoodPotionMap;

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

    public HealthClass(Infinity _plugin)
    {
        super(_plugin);
        plugin = _plugin;
        
        playerNodeMap = new HashMap<Player, ArrayList<Node>>();
        playerEffectMap = new HashMap<Player, PotionEffect>();
        badGoodPotionMap = new HashMap<PotionEffectType, PotionEffectType>();
        nodeList = new ArrayList<Node>();

        badGoodPotionMap.put(PotionEffectType.SLOWNESS, PotionEffectType.SPEED);
        badGoodPotionMap.put(PotionEffectType.WEAKNESS, PotionEffectType.STRENGTH);
        badGoodPotionMap.put(PotionEffectType.INSTANT_DAMAGE, PotionEffectType.INSTANT_HEALTH);
        badGoodPotionMap.put(PotionEffectType.POISON, PotionEffectType.REGENERATION);
        badGoodPotionMap.put(PotionEffectType.BLINDNESS, PotionEffectType.NIGHT_VISION);
        badGoodPotionMap.put(PotionEffectType.MINING_FATIGUE, PotionEffectType.HASTE);
        badGoodPotionMap.put(PotionEffectType.HUNGER, PotionEffectType.SATURATION);
        badGoodPotionMap.put(PotionEffectType.WITHER, PotionEffectType.REGENERATION);
        badGoodPotionMap.put(PotionEffectType.UNLUCK, PotionEffectType.LUCK);
        
        name = "Health";
        header = "2_srt_Health_6_0.85_0.28_0.63_true_1.0";

        nodeList.add(new Node(_plugin, "health", "heartbg", "heart", -52, -32, -1, false, true, null, null, null, null));
        nodeList.add(new Node(_plugin, "health", "icons/15", "For every 2 points you put into this tree, gain 1 heart", 0, 40, 0, true, true, this::node0Learn, this::node0Unlearn, this::node0Status, this::node0CanUnlearn));
        
        nodeList.add(new Node(_plugin, "health", "icons/12", "If any level of the Strength Tree is aquired, gain Regeneration 1 upon being damaged", -30, 20, 1, true, true, this::node1Learn, this::node1Unlearn, this::node1Status, this::node1CanUnlearn));        
        nodeList.add(new Node(_plugin, "health", "icons/24", "If any level of the Luck Tree is aquired, Golden Apple absorption is doubled", 30, 20, 2, true, true, this::node2Learn, this::node2Unlearn, this::node2Status, this::node2CanUnlearn));

        nodeList.add(new Node(_plugin, "health", "icons/11", "Immune to all negative effects", -50, -15, 3, true, true, this::node3Learn, this::node3Unlearn, this::node3Status, this::node3CanUnlearn));
        nodeList.add(new Node(_plugin, "health", "icons/16", "All negative effects turn into positive effects", 50, -15, 4, true, true, this::node4Learn, this::node4Unlearn, this::node4Status, this::node4CanUnlearn));
        
        nodeList.add(new Node(_plugin, "health", "icons/56", "Gain +1 heart", 25, -30, 5, true, true, this::node5Learn, this::node5Unlearn, this::node5Status, this::node5CanUnlearn));
        nodeList.add(new Node(_plugin, "health", "icons/3", "Drinkable potions aren't consumed upon use", -25, -30, 6, true, true, this::node6Learn, this::node6Unlearn, this::node6Status, this::node6CanUnlearn));

        nodeList.add(new Node(_plugin, "health", "icons/17", "10% chance for Totems not to be consumed upon use", 0, -15, 7, true, true, this::node7Learn, this::node7Unlearn, this::node7Status, this::node7CanUnlearn));

        node0AbilityKey = new NamespacedKey(_plugin, "health_0Ability");
        node1AbilityKey = new NamespacedKey(_plugin, "health_1Ability");
        node2AbilityKey = new NamespacedKey(_plugin, "health_2Ability");
        node3AbilityKey = new NamespacedKey(_plugin, "health_3Ability");
        node4AbilityKey = new NamespacedKey(_plugin, "health_4Ability");
        node5AbilityKey = new NamespacedKey(_plugin, "health_5Ability");
        node6AbilityKey = new NamespacedKey(_plugin, "health_6Ability");
        node7AbilityKey = new NamespacedKey(_plugin, "health_7Ability");
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

    public Node findNodeRaw(int clickIndex)
    {
        for(int i = 0; i < nodeList.size(); i++)
        {
            if(nodeList.get(i).clickIndex == clickIndex)
                return nodeList.get(i);
        }

        return null;
    }

    public void reset(Player player)
    {
        for(int i = 7; i >= 0; i--)
            unlearnLogic(player, findNodeRaw(i));
    }

    @EventHandler
    private void doublePotionLength(EntityPotionEffectEvent event)
    {
        if(event.getNewEffect() == null || !(event.getEntity() instanceof Player))
            return;

        Player player = (Player)event.getEntity();
        PersistentDataContainer data = player.getPersistentDataContainer();
    }


    public String tickAbilities(Player player)
    {
        String bar = "";
        PersistentDataContainer data = player.getPersistentDataContainer();

        //if(data.has(node7AbilityKey, PersistentDataType.BOOLEAN) && data.get(node7AbilityKey, PersistentDataType.BOOLEAN))
        

        return bar;
    }

    @EventHandler
    public void giveRegen(EntityDamageByEntityEvent event)
    {
        if(!(event.getEntity() instanceof Player))
            return;

        Player player = (Player)event.getEntity();
        PersistentDataContainer data = player.getPersistentDataContainer();

        if(data.has(node1AbilityKey, PersistentDataType.BOOLEAN) && data.get(node1AbilityKey, PersistentDataType.BOOLEAN))
        {
            if(data.has(plugin.strength.node0AbilityKey, PersistentDataType.BOOLEAN) && data.get(plugin.strength.node0AbilityKey, PersistentDataType.BOOLEAN))
                player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20, 0));
        }
    }

    @EventHandler
    public void addExtraAbsorption(PlayerItemConsumeEvent event)
    {
        Player player = event.getPlayer();
        PersistentDataContainer data = player.getPersistentDataContainer();

        ItemStack item = event.getItem();

        if(item.getType().equals(Material.GOLDEN_APPLE) && data.has(node2AbilityKey, PersistentDataType.BOOLEAN) && data.get(node2AbilityKey, PersistentDataType.BOOLEAN) && data.has(plugin.luck.node0AbilityKey, PersistentDataType.BOOLEAN) && data.get(plugin.luck.node0AbilityKey, PersistentDataType.BOOLEAN))
        {
            if(player.hasPotionEffect(PotionEffectType.ABSORPTION) && player.getPotionEffect(PotionEffectType.ABSORPTION).getAmplifier() == 1)
                player.removePotionEffect(PotionEffectType.ABSORPTION);

            player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 2400, 1));
        }
    }

    @EventHandler
    private void negateBadEffects(EntityPotionEffectEvent event)
    {
        if(event.getNewEffect() == null || !(event.getEntity() instanceof Player))
            return;

        Player player = (Player)event.getEntity();
        PersistentDataContainer data = player.getPersistentDataContainer();

        if((data.has(node3AbilityKey, PersistentDataType.BOOLEAN) && data.get(node3AbilityKey, PersistentDataType.BOOLEAN)) || data.has(node4AbilityKey, PersistentDataType.BOOLEAN) && data.get(node4AbilityKey, PersistentDataType.BOOLEAN))
        {
            PotionEffect potion = event.getNewEffect();
            if(!playerEffectMap.containsKey(player))
                playerEffectMap.put(player, potion);

            if(data.has(node3AbilityKey, PersistentDataType.BOOLEAN) && data.get(node3AbilityKey, PersistentDataType.BOOLEAN))
            {
                if(badGoodPotionMap.containsKey(potion.getType()) || potion.getType().equals(PotionEffectType.NAUSEA) || potion.getType().equals(PotionEffectType.GLOWING))
                    event.setCancelled(true);
                return;
            }

            if(!badGoodPotionMap.containsKey(potion.getType()))
                return;
            
            if(comparePotionEffects(playerEffectMap.get(player), potion))
            {
                playerEffectMap.remove(player);
                playerEffectMap.put(player, potion);
                return;
            }

            playerEffectMap.remove(player);
            playerEffectMap.put(player, potion);

            player.addPotionEffect(new PotionEffect(badGoodPotionMap.get(potion.getType()), potion.getDuration(), potion.getAmplifier()));
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

        return (type1.equals(type2) && amplifier1 == amplifier2 && duration1 == duration2);
    }

    @EventHandler
    public void preventDrinkPotionConsumption(PlayerItemConsumeEvent event)
    {
        Player player = event.getPlayer();
        PersistentDataContainer data = player.getPersistentDataContainer();

        ItemStack item = event.getItem();

        if(item.getType().equals(Material.POTION) && data.has(node6AbilityKey, PersistentDataType.BOOLEAN) && data.get(node6AbilityKey, PersistentDataType.BOOLEAN))
        {
            int _slot = player.getInventory().first(item);
            if(player.getInventory().getItemInOffHand().equals(item))
                _slot = -2;

            int slot = _slot;
                
            ItemStack duplicate = item.clone();
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable()
            {
                public void run()
                {
                    if(slot == -2)
                        player.getInventory().setItemInOffHand(duplicate);
                    else
                        player.getInventory().setItem(slot, duplicate);
                }
            }, 0);

        }
    }

    @EventHandler
    public void playerItemConsumeEvent(EntityResurrectEvent event)
    {
        if(!(event.getEntity() instanceof Player))
            return;

        Player player = (Player)event.getEntity();
        PersistentDataContainer data = player.getPersistentDataContainer();

        ItemStack item = player.getInventory().getItemInMainHand();
        if(item == null || !item.getType().equals(Material.TOTEM_OF_UNDYING))
            item = player.getInventory().getItemInOffHand();

        if(data.has(node7AbilityKey, PersistentDataType.BOOLEAN) && data.get(node7AbilityKey, PersistentDataType.BOOLEAN) && Math.random() < 0.1)
        {
            item.setAmount(item.getAmount() + 1);
            player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ZOMBIE_VILLAGER_CONVERTED, 1.0f, 1.0f);
        }
    }

    public void node0Learn(Player player) { }
    public void node0Unlearn(Player player) { }
    public void node0Status(Player player, Node node) { node.locked = !(true); }
    public void node0CanUnlearn(Player player, Node node) { if(!findNode(player, 1).aquired && !findNode(player, 2).aquired) unlearnNode(player, node); }

    public void node1Learn(Player player) { player.getPersistentDataContainer().set(node1AbilityKey, PersistentDataType.BOOLEAN, true); player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(22); }
    public void node1Unlearn(Player player) { player.getPersistentDataContainer().set(node1AbilityKey, PersistentDataType.BOOLEAN, false); player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(20); }
    public void node1Status(Player player, Node node) { node.locked = !(findNode(player, 0).aquired && !findNode(player, 2).aquired); }
    public void node1CanUnlearn(Player player, Node node) { if(!findNode(player, 3).aquired) unlearnNode(player, node); }

    public void node2Learn(Player player) { player.getPersistentDataContainer().set(node2AbilityKey, PersistentDataType.BOOLEAN, true); player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(22); }
    public void node2Unlearn(Player player) { player.getPersistentDataContainer().set(node2AbilityKey, PersistentDataType.BOOLEAN, false); player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(20); }
    public void node2Status(Player player, Node node) { node.locked = !(findNode(player, 0).aquired && !findNode(player, 1).aquired); }
    public void node2CanUnlearn(Player player, Node node) { if(!findNode(player, 4).aquired) unlearnNode(player, node); }

    public void node3Learn(Player player) { player.getPersistentDataContainer().set(node3AbilityKey, PersistentDataType.BOOLEAN, true); }
    public void node3Unlearn(Player player) { player.getPersistentDataContainer().set(node3AbilityKey, PersistentDataType.BOOLEAN, false); }
    public void node3Status(Player player, Node node) { node.locked = !(findNode(player, 1).aquired); }
    public void node3CanUnlearn(Player player, Node node) { if(!findNode(player, 6).aquired) unlearnNode(player, node); }

    public void node4Learn(Player player) { player.getPersistentDataContainer().set(node4AbilityKey, PersistentDataType.BOOLEAN, true); }
    public void node4Unlearn(Player player) { player.getPersistentDataContainer().set(node4AbilityKey, PersistentDataType.BOOLEAN, false); }
    public void node4Status(Player player, Node node) { node.locked = !(findNode(player, 2).aquired); }
    public void node4CanUnlearn(Player player, Node node) { if(!findNode(player, 5).aquired) unlearnNode(player, node); }

    public void node5Learn(Player player) { player.getPersistentDataContainer().set(node5AbilityKey, PersistentDataType.BOOLEAN, true); player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(26); }
    public void node5Unlearn(Player player) { player.getPersistentDataContainer().set(node5AbilityKey, PersistentDataType.BOOLEAN, false); player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(22); }
    public void node5Status(Player player, Node node) { node.locked = !(findNode(player, 4).aquired); }
    public void node5CanUnlearn(Player player, Node node) { if(!findNode(player, 7).aquired) unlearnNode(player, node); }

    public void node6Learn(Player player) { player.getPersistentDataContainer().set(node6AbilityKey, PersistentDataType.BOOLEAN, true); player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(24); }
    public void node6Unlearn(Player player) { player.getPersistentDataContainer().set(node6AbilityKey, PersistentDataType.BOOLEAN, false); player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(22); }
    public void node6Status(Player player, Node node) { node.locked = !(findNode(player, 3).aquired); }
    public void node6CanUnlearn(Player player, Node node) { if(!findNode(player, 7).aquired) unlearnNode(player, node); }

    public void node7Learn(Player player) { player.getPersistentDataContainer().set(node7AbilityKey, PersistentDataType.BOOLEAN, true); }
    public void node7Unlearn(Player player) { player.getPersistentDataContainer().set(node7AbilityKey, PersistentDataType.BOOLEAN, false); }
    public void node7Status(Player player, Node node) { node.locked = !(findNode(player, 6).aquired || findNode(player, 5).aquired); }
    public void node7CanUnlearn(Player player, Node node) { unlearnNode(player, node); }

}

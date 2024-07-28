package com.rooxchicken.infinity.Abilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDamageEvent.DamageModifier;
import org.bukkit.event.player.PlayerInteractEvent;
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

import com.rooxchicken.infinity.Infinity;
import com.rooxchicken.infinity.Library;
import com.rooxchicken.infinity.Data.Node;
import com.rooxchicken.infinity.Data.PlayerSelectGUI;

public class StrengthClass extends Ability
{
    private Infinity plugin;
    public int type = -1;

    private NamespacedKey ability2CountKey;
    public NamespacedKey ability3CooldownKey;
    public NamespacedKey ability8CooldownKey;
    public NamespacedKey ability8TimerKey;
    
    private NamespacedKey node1AbilityKey;
    private NamespacedKey node2AbilityKey;
    private NamespacedKey node3AbilityKey;
    private NamespacedKey node5AbilityKey;
    private NamespacedKey node6AbilityKey;
    private NamespacedKey node7AbilityKey;
    private NamespacedKey node8AbilityKey;
    private NamespacedKey node9AbilityKey;

    private ArrayList<Player> jumps;
    public ArrayList<PlayerSelectGUI> guis;
    public HashMap<Player, Player> playerTrackMap;
    public HashMap<Player, ItemStack> playerCompassMap;

    public StrengthClass(Infinity _plugin)
    {
        super(_plugin);
        plugin = _plugin;
    
        playerNodeMap = new HashMap<Player, ArrayList<Node>>();
        playerCompassMap = new HashMap<Player, ItemStack>();
        playerTrackMap = new HashMap<Player, Player>();
        guis = new ArrayList<PlayerSelectGUI>();
        nodeList = new ArrayList<Node>();
        
        name = "Strength";
        header = "2_srt_Strength_1_0.9_0.2_0.2_true_1.0";

        nodeList.add(new Node(_plugin, "strength", "n", "n", 0, 41, -1, false, false, null, null, null, null));
        nodeList.add(new Node(_plugin, "strength", "icons/12", "+0.25 attack damage", 0, 40, 0, true, false, this::node0Learn, this::node0Unlearn, this::node0Status, this::node0CanUnlearn));
        nodeList.add(new Node(_plugin, "strength", "larrow", "n", -20, 40, -1, false, false, null, null, null, null));
        nodeList.add(new Node(_plugin, "strength", "n", "n", -40, 40, -1, false, false, null, null, null, null));
        nodeList.add(new Node(_plugin, "strength", "n", "n", 0, 40, -1, false, true, null, null, null, null));
        nodeList.add(new Node(_plugin, "strength", "rarrow", "n", 20, 40, -1, false, false, null, null, null, null));
        nodeList.add(new Node(_plugin, "strength", "n", "n", 40, 40, -1, false, false, null, null, null, null));
        nodeList.add(new Node(_plugin, "strength", "n", "n", 0, 40, -1, false, true, null, null, null, null));
        nodeList.add(new Node(_plugin, "strength", "n", "n", 0, 15, -1, false, false, null, null, null, null));
        nodeList.add(new Node(_plugin, "strength", "icons/11", "Immune to Weakness", 0, 10, 1, true, false, this::node1Learn, this::node1Unlearn, this::node1Status, this::node1CanUnlearn));
        nodeList.add(new Node(_plugin, "strength", "icons/30", "Every 6 crits does 1.2x damage", 0, -20, 2, true, false, this::node2Learn, this::node2Unlearn, this::node2Status, this::node2CanUnlearn));
        nodeList.add(new Node(_plugin, "strength", "uarrow", "n", 0, -35, -1, false, false, null, null, null, null));
        nodeList.add(new Node(_plugin, "strength", "icons/27", "Give every player in a 20 block radius glowing (COOLDOWN: 1m, SHIFT+OFFHAND)", 0, -50, 3, true, false, this::node3Learn, this::node3Unlearn, this::node3Status, this::node3CanUnlearn));

        nodeList.add(new Node(_plugin, "strength", "n", "n", -40, 41, -1, false, true, null, null, null, null));
        nodeList.add(new Node(_plugin, "strength", "icons/44", "+0.25 attack damage", -40, 40, 4, true, false, this::node4Learn, this::node4Unlearn, this::node4Status, this::node4CanUnlearn));
        nodeList.add(new Node(_plugin, "strength", "larrow", "n", -60, 40, -1, false, false, null, null, null, null));
        nodeList.add(new Node(_plugin, "strength", "n", "n", -70, 40, -1, false, false, null, null, null, null));
        nodeList.add(new Node(_plugin, "strength", "n", "n", -40, 40, -1, false, true, null, null, null, null));
        nodeList.add(new Node(_plugin, "strength", "uarrow", "n", -40, 20, -1, false, false, null, null, null, null));
        nodeList.add(new Node(_plugin, "strength", "n", "n", -40, 10, -1, false, false, null, null, null, null));

        nodeList.add(new Node(_plugin, "strength", "icons/14", "Strength 1 Effect", -75, 40, 5, true, true, this::node5Learn, this::node5Unlearn, this::node5Status, this::node5CanUnlearn));
        nodeList.add(new Node(_plugin, "strength", "icons/16", "Extra attack damage while Weakness is applied", -40, 5, 6, true, true, this::node6Learn, this::node6Unlearn, this::node6Status, this::node6CanUnlearn));

        nodeList.add(new Node(_plugin, "strength", "n", "n", 40, 41, -1, false, true, null, null, null, null));
        nodeList.add(new Node(_plugin, "strength", "icons/22", "10% chance to deal 4x damage to armor durability", 40, 40, 7, true, false, this::node7Learn, this::node7Unlearn, this::node7Status, this::node7CanUnlearn));
        nodeList.add(new Node(_plugin, "strength", "rarrow", "n", 60, 40, -1, false, false, null, null, null, null));
        nodeList.add(new Node(_plugin, "strength", "n", "n", 70, 40, -1, false, false, null, null, null, null));
        nodeList.add(new Node(_plugin, "strength", "n", "n", 40, 40, -1, false, true, null, null, null, null));
        nodeList.add(new Node(_plugin, "strength", "uarrow", "n", 40, 20, -1, false, false, null, null, null, null));
        nodeList.add(new Node(_plugin, "strength", "n", "n", 40, 10, -1, false, false, null, null, null, null));

        nodeList.add(new Node(_plugin, "strength", "icons/7", "Right click a compass to Track a specified player (COOLDOWN: 30m)", 75, 40, 8, true, true, this::node8Learn, this::node8Unlearn, this::node8Status, this::node8CanUnlearn));
        nodeList.add(new Node(_plugin, "strength", "icons/26", "The lower your health, the more damage you deal (CAP: 2x damage at 1.0hp)", 40, 5, 9, true, true, this::node9Learn, this::node9Unlearn, this::node9Status, this::node9CanUnlearn));
        
        ability2CountKey = new NamespacedKey(_plugin, "strength_critCount");
        ability3CooldownKey = new NamespacedKey(_plugin, "strength_glowCD");
        ability8CooldownKey = new NamespacedKey(_plugin, "strength_compassCD");
        ability8TimerKey = new NamespacedKey(_plugin, "strength_compassTimer");

        node1AbilityKey = new NamespacedKey(_plugin, "strength_1Ability");
        node2AbilityKey = new NamespacedKey(_plugin, "strength_2Ability");
        node3AbilityKey = new NamespacedKey(_plugin, "strength_3Ability");
        node5AbilityKey = new NamespacedKey(_plugin, "strength_5Ability");
        node6AbilityKey = new NamespacedKey(_plugin, "strength_6Ability");
        node7AbilityKey = new NamespacedKey(_plugin, "strength_7Ability");
        node8AbilityKey = new NamespacedKey(_plugin, "strength_8Ability");
        node9AbilityKey = new NamespacedKey(_plugin, "strength_9Ability");
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

    public void resetCooldown(Player player)
    {
        player.getPersistentDataContainer().set(ability8CooldownKey, PersistentDataType.INTEGER, 0);
        player.getPersistentDataContainer().set(ability3CooldownKey, PersistentDataType.INTEGER, 0);
    }

    public String tickAbilities(Player player)
    {
        String bar = "";
        PersistentDataContainer data = player.getPersistentDataContainer();

        if(data.has(node1AbilityKey, PersistentDataType.BOOLEAN) && data.get(node1AbilityKey, PersistentDataType.BOOLEAN))
        {
            if(player.hasPotionEffect(PotionEffectType.WEAKNESS))
            {
                player.removePotionEffect(PotionEffectType.WEAKNESS);
                player.getWorld().spawnParticle(Particle.REDSTONE, player.getLocation().clone().add(0,1,0), 50, 0.3, 0.5, 0.3, new Particle.DustOptions(Color.RED, 1.0f));
                player.getWorld().playSound(player.getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, 1.0f, 1.0f);
            }
        }

        if(data.has(node6AbilityKey, PersistentDataType.BOOLEAN) && data.get(node6AbilityKey, PersistentDataType.BOOLEAN))
        {
            if(player.hasPotionEffect(PotionEffectType.WEAKNESS))
                player.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(5.25);
            else
                player.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(1.5);
        }

        if(playerTrackMap.containsKey(player))
        {
            int cooldown = data.get(ability8TimerKey, PersistentDataType.INTEGER) - 1;
            Player track = playerTrackMap.get(player);
            ItemStack compass = plugin.strength.playerCompassMap.get(player);
            CompassMeta meta = (CompassMeta)compass.getItemMeta();
            meta.setLodestone(track.getLocation());

            if(cooldown <= 0)
            {
                meta.setDisplayName("§rCompass");
                meta.setLodestone(null);
                playerTrackMap.remove(player);
                playerCompassMap.remove(player);
            }

            compass.setItemMeta(meta);


            data.set(ability8TimerKey, PersistentDataType.INTEGER, cooldown);
        }

        if(data.has(ability8CooldownKey, PersistentDataType.INTEGER))
            data.set(ability8CooldownKey, PersistentDataType.INTEGER, data.get(ability8CooldownKey, PersistentDataType.INTEGER) - 1);

        if(data.has(node3AbilityKey, PersistentDataType.BOOLEAN) && data.get(node3AbilityKey, PersistentDataType.BOOLEAN))
        if(data.has(ability3CooldownKey, PersistentDataType.INTEGER))
        {
            int cooldown = data.get(ability3CooldownKey, PersistentDataType.INTEGER) - 1;
            data.set(ability3CooldownKey, PersistentDataType.INTEGER, cooldown);

            bar += "✴ " + ((cooldown < 0) ? "READY " : (cooldown/20 + "s "));
        }

        if(data.has(node5AbilityKey, PersistentDataType.BOOLEAN) && data.get(node5AbilityKey, PersistentDataType.BOOLEAN))
            player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 2, 0));

        return bar;
    }

    @EventHandler
    private void useGlowing(PlayerSwapHandItemsEvent event)
    {
        Player player = event.getPlayer();
        PersistentDataContainer data = player.getPersistentDataContainer();

        if(!player.isSneaking() || !(data.has(node3AbilityKey, PersistentDataType.BOOLEAN) && data.get(node3AbilityKey, PersistentDataType.BOOLEAN)))
            return;

        if(!data.has(ability3CooldownKey, PersistentDataType.INTEGER))
            data.set(ability3CooldownKey, PersistentDataType.INTEGER, 0);

        if(data.get(ability3CooldownKey, PersistentDataType.INTEGER) <= 0)
        {
            event.setCancelled(true);
            player.getWorld().spawnParticle(Particle.REDSTONE, player.getLocation(), 600, 10, 10, 10, new Particle.DustOptions(Color.RED, 0.8f));

            for(Object o : Library.getNearbyEntities(player.getLocation(), 20))
            {
                if(o instanceof Player && o != player)
                {
                    ((Player)o).addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 100, 0));
                }
            }

            data.set(ability3CooldownKey, PersistentDataType.INTEGER, 60*20);
        }
    }

    @EventHandler
    public void openCompass(PlayerInteractEvent event)
    {
        if(!event.getAction().equals(Action.RIGHT_CLICK_AIR) && !event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
            return;

        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if(item != null && item.getType().equals(Material.COMPASS))
        {
            PersistentDataContainer data = player.getPersistentDataContainer();
            if(data.has(node8AbilityKey, PersistentDataType.BOOLEAN) && data.get(node8AbilityKey, PersistentDataType.BOOLEAN))
            {
                if(!data.has(ability8CooldownKey, PersistentDataType.INTEGER))
                    data.set(ability8CooldownKey, PersistentDataType.INTEGER, 0);

                if(data.get(ability8CooldownKey, PersistentDataType.INTEGER) <= 0)
                {
                    playerCompassMap.put(player, item);
                    PlayerSelectGUI gui = new PlayerSelectGUI(plugin, player);
                    guis.add(gui);
                }
                else
                {
                    int cooldown = data.get(ability8CooldownKey, PersistentDataType.INTEGER)/20;
                    int min = cooldown/60;
                    int seconds = cooldown % 60;
                    String text = "";
                    if(min > 0)
                        text += " " + min + "m";
                    if(seconds > 0)
                        text += " " + seconds + "s";
                    player.sendMessage("§4This ability is on cooldown for" + text + "!");
                }
            }
        }
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event)
    {
        if(!(event.getDamager() instanceof Player) || !(event.getEntity() instanceof LivingEntity))
            return;

        Player player = (Player)event.getDamager();
        LivingEntity entity = (LivingEntity)event.getEntity();
        PersistentDataContainer data = player.getPersistentDataContainer();

        if(player.getVelocity().getY() < -0.09 && data.has(node2AbilityKey, PersistentDataType.BOOLEAN) && data.get(node2AbilityKey, PersistentDataType.BOOLEAN))
        {
            if(!data.has(ability2CountKey, PersistentDataType.INTEGER))
                data.set(ability2CountKey, PersistentDataType.INTEGER, 0);

            int crits = data.get(ability2CountKey, PersistentDataType.INTEGER) + 1;
            if(crits > 5)
            {
                event.setDamage(event.getDamage() * 1.2);

                entity.getWorld().playSound(entity.getLocation(), Sound.ENTITY_PLAYER_ATTACK_CRIT, 1.0f, 0.7f);
                entity.getWorld().spawnParticle(Particle.REDSTONE, entity.getLocation().clone().add(0,1,0), 100, 0.5, 0.6, 0.5, new Particle.DustOptions(Color.MAROON, 0.8f));

                data.set(ability2CountKey, PersistentDataType.INTEGER, 0);
            }
            else
            {
                player.getWorld().spawnParticle(Particle.REDSTONE, player.getLocation().clone().add(0,1,0), 10*crits, 0.5, 0.6, 0.5, new Particle.DustOptions(Color.RED, 0.8f));
                data.set(ability2CountKey, PersistentDataType.INTEGER, crits);
            }
        }

        if(data.has(node9AbilityKey, PersistentDataType.BOOLEAN) && data.get(node9AbilityKey, PersistentDataType.BOOLEAN))
        {
            if(player.getHealth() < 16)
            {
                double multiplier = Math.min(2, 2 - (player.getHealth()/16 - 1.0/32));
                Bukkit.getLogger().info(multiplier + "");
                event.setDamage(event.getDamage() * multiplier);
            }
        }

        if(data.has(node7AbilityKey, PersistentDataType.BOOLEAN) && data.get(node7AbilityKey, PersistentDataType.BOOLEAN) && Math.random() < 0.1)
        {
            if(entity instanceof Player)
            {
                Player victim = (Player)entity;
                PlayerInventory inv = victim.getInventory();

                ItemStack helmet = inv.getHelmet();
                ItemStack chest = inv.getChestplate();
                ItemStack legs = inv.getLeggings();
                ItemStack boots = inv.getBoots();

                if(helmet != null)
                {
                    Damageable helMeta = (Damageable)helmet.getItemMeta();
                    helMeta.setDamage(helMeta.getDamage() + (int)(event.getFinalDamage() * 4));
                    helmet.setItemMeta(helMeta);
                }

                if(chest != null)
                {
                    Damageable chestMeta = (Damageable)chest.getItemMeta();
                    chestMeta.setDamage(chestMeta.getDamage() + (int)(event.getFinalDamage() * 4));
                    chest.setItemMeta(chestMeta);
                }

                if(legs != null)
                {
                    Damageable legsMeta = (Damageable)legs.getItemMeta();
                    legsMeta.setDamage(legsMeta.getDamage() + (int)(event.getFinalDamage() * 4));
                    legs.setItemMeta(legsMeta);
                }

                if(boots != null)
                {
                    Damageable bootsMeta = (Damageable)boots.getItemMeta();
                    bootsMeta.setDamage(bootsMeta.getDamage() + (int)(event.getFinalDamage() * 4));
                    boots.setItemMeta(bootsMeta);
                }

                victim.getWorld().playSound(victim.getLocation(), Sound.BLOCK_ANVIL_LAND, 0.4f, 1.0f);
                victim.getWorld().spawnParticle(Particle.REDSTONE, victim.getLocation().clone().add(0,1,0), 100, 0.3, 0.5, 0.3, new Particle.DustOptions(Color.GRAY, 1.0f));
            }
        }
    }

    public void node0Learn(Player player) { player.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(1.25); }
    public void node0Unlearn(Player player) { player.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(1.0); }
    public void node0Status(Player player, Node node) { node.locked = false; }
    public void node0CanUnlearn(Player player, Node node) { if(!findNode(player, 1).aquired && !findNode(player, 4).aquired && !findNode(player, 7).aquired) unlearnNode(player, node); }

    public void node1Learn(Player player) { player.getPersistentDataContainer().set(node1AbilityKey, PersistentDataType.BOOLEAN, true); }
    public void node1Unlearn(Player player) { player.getPersistentDataContainer().set(node1AbilityKey, PersistentDataType.BOOLEAN, false); }
    public void node1Status(Player player, Node node) { if(findNode(player, 0).aquired) node.locked = false; else node.locked = true; }
    public void node1CanUnlearn(Player player, Node node) { if(!findNode(player, 2).aquired) unlearnNode(player, node); }

    public void node2Learn(Player player) { player.getPersistentDataContainer().set(node2AbilityKey, PersistentDataType.BOOLEAN, true); }
    public void node2Unlearn(Player player) { player.getPersistentDataContainer().set(node2AbilityKey, PersistentDataType.BOOLEAN, false); }
    public void node2Status(Player player, Node node) { if(findNode(player, 1).aquired) node.locked = false; else node.locked = true; }
    public void node2CanUnlearn(Player player, Node node) { if(!findNode(player, 3).aquired) unlearnNode(player, node); }
    
    public void node3Learn(Player player) { player.getPersistentDataContainer().set(node3AbilityKey, PersistentDataType.BOOLEAN, true); }
    public void node3Unlearn(Player player) { player.getPersistentDataContainer().set(node3AbilityKey, PersistentDataType.BOOLEAN, false); }
    public void node3Status(Player player, Node node) { if(findNode(player, 2).aquired && !findNode(player, 4).aquired && !findNode(player, 7).aquired) node.locked = false; else node.locked = true; }
    public void node3CanUnlearn(Player player, Node node) { unlearnNode(player, node); }

    public void node4Learn(Player player) { player.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(1.5); }
    public void node4Unlearn(Player player) { player.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(1.25); }
    public void node4Status(Player player, Node node) { if(findNode(player, 0).aquired && !findNode(player, 7).aquired && !findNode(player, 3).aquired) node.locked = false; else node.locked = true; }
    public void node4CanUnlearn(Player player, Node node) { if(!findNode(player, 5).aquired && !findNode(player, 6).aquired) unlearnNode(player, node); }

    public void node5Learn(Player player) { player.getPersistentDataContainer().set(node5AbilityKey, PersistentDataType.BOOLEAN, true); }
    public void node5Unlearn(Player player) { player.getPersistentDataContainer().set(node5AbilityKey, PersistentDataType.BOOLEAN, false); player.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(2.0); }
    public void node5Status(Player player, Node node) { if(findNode(player,4).aquired && !findNode(player, 6).aquired) node.locked = false; else node.locked = true; }
    public void node5CanUnlearn(Player player, Node node) { unlearnNode(player, node); }

    public void node6Learn(Player player) { player.getPersistentDataContainer().set(node6AbilityKey, PersistentDataType.BOOLEAN, true); }
    public void node6Unlearn(Player player) { player.getPersistentDataContainer().set(node6AbilityKey, PersistentDataType.BOOLEAN, false); }
    public void node6Status(Player player, Node node) { if(findNode(player, 4).aquired && !findNode(player, 5).aquired) node.locked = false; else node.locked = true; }
    public void node6CanUnlearn(Player player, Node node) { unlearnNode(player, node); }

    public void node7Learn(Player player) { player.getPersistentDataContainer().set(node7AbilityKey, PersistentDataType.BOOLEAN, true); }
    public void node7Unlearn(Player player) { player.getPersistentDataContainer().set(node7AbilityKey, PersistentDataType.BOOLEAN, false); }
    public void node7Status(Player player, Node node) { if(findNode(player, 0).aquired && !findNode(player, 4).aquired && !findNode(player, 3).aquired) node.locked = false; else node.locked = true; }
    public void node7CanUnlearn(Player player, Node node) { if(!findNode(player, 8).aquired && !findNode(player, 9).aquired)unlearnNode(player, node); }

    public void node8Learn(Player player) { player.getPersistentDataContainer().set(node8AbilityKey, PersistentDataType.BOOLEAN, true); }
    public void node8Unlearn(Player player) { player.getPersistentDataContainer().set(node8AbilityKey, PersistentDataType.BOOLEAN, false); }
    public void node8Status(Player player, Node node) { if(findNode(player, 7).aquired && !findNode(player, 9).aquired) node.locked = false; else node.locked = true; }
    public void node8CanUnlearn(Player player, Node node) { unlearnNode(player, node); }

    public void node9Learn(Player player) { player.getPersistentDataContainer().set(node9AbilityKey, PersistentDataType.BOOLEAN, true); }
    public void node9Unlearn(Player player) { player.getPersistentDataContainer().set(node9AbilityKey, PersistentDataType.BOOLEAN, false); }
    public void node9Status(Player player, Node node) { if(findNode(player, 7).aquired && !findNode(player, 8).aquired) node.locked = false; else node.locked = true; }
    public void node9CanUnlearn(Player player, Node node) { unlearnNode(player, node); }

}

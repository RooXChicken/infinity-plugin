package com.rooxchicken.infinity.Abilities;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import com.rooxchicken.infinity.Infinity;
import com.rooxchicken.infinity.Data.Node;

public class SpeedClass extends Ability
{
    private Infinity plugin;
    public int type = -1;

    private NamespacedKey node1AbilityKey;

    private NamespacedKey doubleJumpCooldownKey;

    private NamespacedKey node2AbilityKey;
    private NamespacedKey node3AbilityKey;
    private NamespacedKey node4AbilityKey;
    private NamespacedKey node5AbilityKey;
    private NamespacedKey node7AbilityKey;
    private NamespacedKey node8AbilityKey;

    private ArrayList<Player> jumps;

    public SpeedClass(Infinity _plugin)
    {
        super(_plugin);
        
        playerNodeMap = new HashMap<Player, ArrayList<Node>>();
        nodeList = new ArrayList<Node>();
        
        name = "Speed";
        header = "2_srt_Speed_2_0.4_0.8_1.0_true_1.0";

        nodeList.add(new Node(_plugin, "speed", "n", "n", 20, 41, -1, false, false, null, null, null, null));
        nodeList.add(new Node(_plugin, "speed", "icons/28", "+10% generic speed", 20, 40, 0, true, false, this::node0Learn, this::node0Unlearn, this::node0Status, this::node0CanUnlearn));
        nodeList.add(new Node(_plugin, "speed", "rarrow", "n", 40, 40, -1, false, false, null, null, null, null));
        nodeList.add(new Node(_plugin, "speed", "n", "n", 60, 40, -1, false, false, null, null, null, null));
        nodeList.add(new Node(_plugin, "speed", "n", "n", 20, 40, -1, false, true, null, null, null, null));
        nodeList.add(new Node(_plugin, "speed", "uarrow", "n", 20, 20, -1, false, false, null, null, null, null));
        nodeList.add(new Node(_plugin, "speed", "n", "n", 20, 10, -1, false, false, null, null, null, null));
        nodeList.add(new Node(_plugin, "speed", "n", "n", 20, 40, -1, false, true, null, null, null, null));
        nodeList.add(new Node(_plugin, "speed", "larrow", "n", 0, 40, -1, false, false, null, null, null, null));

        nodeList.add(new Node(_plugin, "speed", "icons/1", "Double Jump (COOLDOWN: 30s)", -20, 40, 1, true, false, this::node1Learn, this::node1Unlearn, this::node1Status, this::node1CanUnlearn));
        nodeList.add(new Node(_plugin, "speed", "larrow", "n", -40, 40, -1, false, false, null, null, null, null));
        nodeList.add(new Node(_plugin, "speed", "n", "n", -60, 40, -1, false, false, null, null, null, null));
        nodeList.add(new Node(_plugin, "speed", "n", "n", -20, 40, -1, false, true, null, null, null, null));
        nodeList.add(new Node(_plugin, "speed", "uarrow", "n", -20, 20, -1, false, false, null, null, null, null));
        nodeList.add(new Node(_plugin, "speed", "n", "n", -20, 10, -1, false, false, null, null, null, null));
        nodeList.add(new Node(_plugin, "speed", "n", "n", -40, 10, -1, false, false, null, null, null, null));
        nodeList.add(new Node(_plugin, "speed", "n", "n", -40, -10, -1, false, false, null, null, null, null));

        nodeList.add(new Node(_plugin, "speed", "n", "n", -20, 10, -1, false, true, null, null, null, null));
        nodeList.add(new Node(_plugin, "speed", "n", "n", 0, 10, -1, false, false, null, null, null, null));
        nodeList.add(new Node(_plugin, "speed", "n", "n", 0, -10, -1, false, false, null, null, null, null));

        nodeList.add(new Node(_plugin, "speed", "icons/20", "Speed 2 Effect", -60, 40, 2, true, true, this::node2Learn, this::node2Unlearn, this::node2Status, this::node2CanUnlearn));
        nodeList.add(new Node(_plugin, "speed", "icons/41", "Dash (REPLACED DOUBLE JUMP)", -40, -10, 3, true, true, this::node3Learn, this::node3Unlearn, this::node3Status, this::node3CanUnlearn));
        
        nodeList.add(new Node(_plugin, "speed", "n", "n", 0, -9, -1, false, true, null, null, null, null));
        nodeList.add(new Node(_plugin, "speed", "icons/36", "Haste 2 Effect", 0, -10, 5, true, false, this::node5Learn, this::node5Unlearn, this::node5Status, this::node5CanUnlearn));
        nodeList.add(new Node(_plugin, "speed", "n", "n", 0, -25, -1, false, false, null, null, null, null));
        nodeList.add(new Node(_plugin, "speed", "n", "n", 10, -25, -1, false, false, null, null, null, null));
        nodeList.add(new Node(_plugin, "speed", "n", "n", 10, -35, -1, false, false, null, null, null, null));
        
        nodeList.add(new Node(_plugin, "speed", "n", "n", 20, 4, -1, false, true, null, null, null, null));
        nodeList.add(new Node(_plugin, "speed", "icons/42", "Speed 1 Effect", 20, 5, 4, true, false, this::node4Learn, this::node4Unlearn, this::node4Status, this::node4CanUnlearn));
        nodeList.add(new Node(_plugin, "speed", "n", "n", 20, -25, -1, false, false, null, null, null, null));
        nodeList.add(new Node(_plugin, "speed", "n", "n", 10, -25, -1, false, false, null, null, null, null));
        nodeList.add(new Node(_plugin, "speed", "n", "n", 10, -35, -1, false, false, null, null, null, null));

        nodeList.add(new Node(_plugin, "speed", "icons/19", "+5% generic speed +10% chance of slowness on hit", 10, -35, 8, true, false, this::node8Learn, this::node8Unlearn, this::node8Status, this::node8CanUnlearn));
        
        //nodeList.add(new Node(_plugin, "speed", "n", "n", 0, -10, -1, false, false, null, null, null, null));
        nodeList.add(new Node(_plugin, "speed", "n", "n", 60, 39, -1, false, true, null, null, null, null));
        
        
        nodeList.add(new Node(_plugin, "speed", "icons/4", "+6% attack speed", 60, 40, 6, true, false, this::node6Learn, this::node6Unlearn, this::node6Status, this::node6CanUnlearn));
        nodeList.add(new Node(_plugin, "speed", "uarrow", "n", 60, 20, -1, false, false, null, null, null, null));
        nodeList.add(new Node(_plugin, "speed", "n", "n", 60, 5, -1, false, false, null, null, null, null));
        nodeList.add(new Node(_plugin, "speed", "lline", "n", 35, 15, -1, false, true, null, null, null, null));
        nodeList.add(new Node(_plugin, "speed", "lline", "n", 40, 20, -1, false, true, null, null, null, null));
        nodeList.add(new Node(_plugin, "speed", "lline", "n", 45, 25, -1, false, true, null, null, null, null));
        nodeList.add(new Node(_plugin, "speed", "icons/39", "+3% attack speed", 60, 5, 7, true, true, this::node7Learn, this::node7Unlearn, this::node7Status, this::node7CanUnlearn));
        
        jumps = new ArrayList<Player>();

        node1AbilityKey = new NamespacedKey(_plugin, "speed_1Ability");
        doubleJumpCooldownKey = new NamespacedKey(_plugin, "speed_DJCD");
        node2AbilityKey = new NamespacedKey(_plugin, "speed_2Ability");
        node3AbilityKey = new NamespacedKey(_plugin, "speed_3Ability");
        node4AbilityKey = new NamespacedKey(_plugin, "speed_4Ability");
        node5AbilityKey = new NamespacedKey(_plugin, "speed_5Ability");
        node7AbilityKey = new NamespacedKey(_plugin, "speed_7Ability");
        node8AbilityKey = new NamespacedKey(_plugin, "speed_8Ability");
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

    public void resetCooldown(Player player)
    {
        player.getPersistentDataContainer().set(doubleJumpCooldownKey, PersistentDataType.INTEGER, 0);
    }

    @EventHandler
    public void doubleJump(PlayerToggleFlightEvent event)
    {
        Player player = event.getPlayer();
        PersistentDataContainer data = player.getPersistentDataContainer();

        if(player.getGameMode().equals(GameMode.CREATIVE))
            return;

        player.setAllowFlight(false);
        event.setCancelled(true);

        if(!data.has(node1AbilityKey, PersistentDataType.BOOLEAN) || !data.get(node1AbilityKey, PersistentDataType.BOOLEAN))
        if(!data.has(node3AbilityKey, PersistentDataType.BOOLEAN) || !data.get(node3AbilityKey, PersistentDataType.BOOLEAN))
            return;

        if(!data.has(doubleJumpCooldownKey, PersistentDataType.INTEGER))
            data.set(doubleJumpCooldownKey, PersistentDataType.INTEGER, 0);

        int cooldown = data.get(doubleJumpCooldownKey, PersistentDataType.INTEGER);

        if(cooldown <= 0)
        {
            data.set(doubleJumpCooldownKey, PersistentDataType.INTEGER, 15*20);
            player.getWorld().playSound(player.getLocation(), Sound.BLOCK_BIG_DRIPLEAF_FALL, 1, 1);
            
            Location launch = player.getLocation().clone();

            if(data.has(node3AbilityKey, PersistentDataType.BOOLEAN) && data.get(node3AbilityKey, PersistentDataType.BOOLEAN))
            {
                launch.setPitch(-5);
                player.setVelocity(launch.getDirection().multiply(1.6));
                player.getWorld().spawnParticle(Particle.REDSTONE, player.getLocation().clone().add(0,1,0), 175, 0.4, 0.7, 0.4, new Particle.DustOptions(Color.TEAL, 1f));
            }
            else
            {
                launch.setPitch(-70);
                player.setVelocity(launch.getDirection());
                player.getWorld().spawnParticle(Particle.REDSTONE, player.getLocation().clone().subtract(0,0.5,0), 175, 1, 0.1, 1, new Particle.DustOptions(Color.TEAL, 1f));
            
                if(!jumps.contains(player))
                    jumps.add(player);
            }
        }
    }

    @EventHandler
    public void preventFallDamage(EntityDamageEvent event)
    {
        if(!event.getCause().equals(DamageCause.FALL) || !(event.getEntity() instanceof Player))
            return;

        event.setCancelled(jumps.contains(event.getEntity()));
        jumps.remove(event.getEntity());
    }

    public String tickAbilities(Player player)
    {
        String bar = "";
        PersistentDataContainer data = player.getPersistentDataContainer();

        if(data.has(node1AbilityKey, PersistentDataType.BOOLEAN) && data.get(node1AbilityKey, PersistentDataType.BOOLEAN))
        {
            int cooldown =  data.get(doubleJumpCooldownKey, PersistentDataType.INTEGER) - 1;
            data.set(doubleJumpCooldownKey, PersistentDataType.INTEGER, cooldown);
            bar = "☁ " + ((cooldown >= 0) ? (cooldown/20 + 1) + "s " : "READY ");

            if(player.isOnGround() && data.get(doubleJumpCooldownKey, PersistentDataType.INTEGER) <= 0)
                player.setAllowFlight(true);
        }

        if(data.has(node2AbilityKey, PersistentDataType.BOOLEAN) && data.get(node2AbilityKey, PersistentDataType.BOOLEAN))
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 2, 1));

        if(data.has(node4AbilityKey, PersistentDataType.BOOLEAN) && data.get(node4AbilityKey, PersistentDataType.BOOLEAN))
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 2, 0));

        if(data.has(node5AbilityKey, PersistentDataType.BOOLEAN) && data.get(node5AbilityKey, PersistentDataType.BOOLEAN))
            player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 2, 1));

        //player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.1f * data.get(attackSpeedKey, null));

        return bar;
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event)
    {
        if(!(event.getDamager() instanceof Player) || !(event.getEntity() instanceof LivingEntity))
            return;

        Player player = (Player)event.getDamager();
        LivingEntity entity = (LivingEntity)event.getEntity();
        PersistentDataContainer data = player.getPersistentDataContainer();

        // if(data.has(node7AbilityKey, PersistentDataType.BOOLEAN) && data.get(node7AbilityKey, PersistentDataType.BOOLEAN) && Math.random() < 0.08)
        // {
        //     event.getEntity().getWorld().strikeLightning(event.getEntity().getLocation());
        //     event.setDamage(event.getDamage() * 1.6);
        // }

        if(data.has(node8AbilityKey, PersistentDataType.BOOLEAN) && data.get(node8AbilityKey, PersistentDataType.BOOLEAN) && Math.random() < 0.1)
        {
            entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 1));
        }
    }

    public void node0Learn(Player player) { player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.11f); }
    public void node0Unlearn(Player player) { player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.1f); }
    public void node0Status(Player player, Node node) { node.locked = false; }
    public void node0CanUnlearn(Player player, Node node) { if(!findNode(player, "1").aquired && !findNode(player, "42").aquired && !findNode(player, "4").aquired) unlearnNode(player, node); }

    public void node1Learn(Player player) { player.getPersistentDataContainer().set(node1AbilityKey, PersistentDataType.BOOLEAN, true); player.getPersistentDataContainer().set(doubleJumpCooldownKey, PersistentDataType.INTEGER, 0); }
    public void node1Unlearn(Player player) { player.getPersistentDataContainer().set(node1AbilityKey, PersistentDataType.BOOLEAN, false); player.setAllowFlight(false); }
    public void node1Status(Player player, Node node) { if(!findNode(player, "4").aquired && !findNode(player, "42").aquired && findNode(player, "28").aquired) node.locked = false; else node.locked = true; }
    public void node1CanUnlearn(Player player, Node node) { if(!findNode(player, "41").aquired && !findNode(player, "36").aquired && !findNode(player, "20").aquired) unlearnNode(player, node); }

    public void node2Learn(Player player) { player.getPersistentDataContainer().set(node2AbilityKey, PersistentDataType.BOOLEAN, true); }
    public void node2Unlearn(Player player) { player.getPersistentDataContainer().set(node2AbilityKey, PersistentDataType.BOOLEAN, false); }
    public void node2Status(Player player, Node node) { if(findNode(player, "1").aquired && !findNode(player, "36").aquired && !findNode(player, "41").aquired) node.locked = false; else node.locked = true; }
    public void node2CanUnlearn(Player player, Node node) { unlearnNode(player, node); }

    public void node3Learn(Player player) { player.getPersistentDataContainer().set(node3AbilityKey, PersistentDataType.BOOLEAN, true); }
    public void node3Unlearn(Player player) { player.getPersistentDataContainer().set(node3AbilityKey, PersistentDataType.BOOLEAN, false); }
    public void node3Status(Player player, Node node) { if(findNode(player, "1").aquired && !findNode(player, "20").aquired) node.locked = false; else node.locked = true; }
    public void node3CanUnlearn(Player player, Node node) { unlearnNode(player, node); }

    public void node4Learn(Player player) { player.getPersistentDataContainer().set(node4AbilityKey, PersistentDataType.BOOLEAN, true); }
    public void node4Unlearn(Player player) { player.getPersistentDataContainer().set(node4AbilityKey, PersistentDataType.BOOLEAN, false); }
    public void node4Status(Player player, Node node) { if(!findNode(player, "39").aquired && !findNode(player, "1").aquired && findNode(player, "28").aquired) node.locked = false; else node.locked = true; }
    public void node4CanUnlearn(Player player, Node node) { if(!findNode(player, "19").aquired) unlearnNode(player, node); }

    public void node5Learn(Player player) { player.getPersistentDataContainer().set(node5AbilityKey, PersistentDataType.BOOLEAN, true); }
    public void node5Unlearn(Player player) { player.getPersistentDataContainer().set(node5AbilityKey, PersistentDataType.BOOLEAN, false); }
    public void node5Status(Player player, Node node) { if(findNode(player, "1").aquired && !findNode(player, "20").aquired) node.locked = false; else node.locked = true; }
    public void node5CanUnlearn(Player player, Node node) { if(!findNode(player, "19").aquired) unlearnNode(player, node); }

    public void node6Learn(Player player) { player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(4.24f); }
    public void node6Unlearn(Player player) { player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(4f); }
    public void node6Status(Player player, Node node) { if(findNode(player, "28").aquired && !findNode(player, "1").aquired) node.locked = false; else node.locked = true; }
    public void node6CanUnlearn(Player player, Node node) { if(!findNode(player, "39").aquired) unlearnNode(player, node); }

    public void node7Learn(Player player) { player.getPersistentDataContainer().set(node7AbilityKey, PersistentDataType.BOOLEAN, true); player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(4.36f); }
    public void node7Unlearn(Player player) { player.getPersistentDataContainer().set(node7AbilityKey, PersistentDataType.BOOLEAN, false); player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(4.24f); }
    public void node7Status(Player player, Node node) { if(!findNode(player, "42").aquired && findNode(player, "4").aquired) node.locked = false; else node.locked = true; }
    public void node7CanUnlearn(Player player, Node node) { unlearnNode(player, node); }

    public void node8Learn(Player player) { player.getPersistentDataContainer().set(node8AbilityKey, PersistentDataType.BOOLEAN, true); player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).getBaseValue() * 1.05f); }
    public void node8Unlearn(Player player) { player.getPersistentDataContainer().set(node8AbilityKey, PersistentDataType.BOOLEAN, false); player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).getBaseValue() * 0.95f); }
    public void node8Status(Player player, Node node) { if(findNode(player, "36").aquired || findNode(player, "42").aquired) node.locked = false; else node.locked = true; }
    public void node8CanUnlearn(Player player, Node node) { unlearnNode(player, node); }

}

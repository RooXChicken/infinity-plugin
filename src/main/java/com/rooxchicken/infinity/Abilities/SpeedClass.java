package com.rooxchicken.infinity.Abilities;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
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

    private ArrayList<Player> jumps;

    public SpeedClass(Infinity _plugin)
    {
        super(_plugin);

        name = "Speed";
        nodes = new ArrayList<Node>();

        header = "2_srt_Speed_2_0.4_0.8_1.0_true_1.0";

        nodes.add(new Node(_plugin, "n", "n", 0, 21, -1, false, null, null, null, null));
        nodes.add(new Node(_plugin, "icons/28", "+10% generic speed", 0, 20, 0, true, this::node0Learn, this::node0Unlearn, this::node0Status, this::node0CanUnlearn));
        nodes.add(new Node(_plugin, "n", "n", 0, -20, -1, false, null, null, null, null));
        nodes.add(new Node(_plugin, "lArrow", "n", -20, -20, -1, false, null, null, null, null));
        nodes.add(new Node(_plugin, "icons/1", "Double Jump (COOLDOWN: 30s)", -40, -20, 1, true, this::node1Learn, this::node1Unlearn, this::node1Status, this::node1CanUnlearn));
        nodes.add(new Node(_plugin, "icons/20", "Speed 2 Effect", -80, -20, 2, true, this::node2Learn, this::node2Unlearn, this::node2Status, this::node4CanUnlearn));
        nodes.add(new Node(_plugin, "n", "n", -60, -20, -1, false, null, null, null, null));
        nodes.add(new Node(_plugin, "icons/41", "Dash (REPLACED DOUBLE JUMP)", -60, -60, 3, true, this::node3Learn, this::node3Unlearn, this::node3Status, this::node3CanUnlearn));
        nodes.add(new Node(_plugin, "n", "n", -60, -20, -1, false, null, null, null, null));
        nodes.add(new Node(_plugin, "rArrow", "n", 20, -20, -1, false, null, null, null, null));
        nodes.add(new Node(_plugin, "icons/42", "Speed 1 Effect", 40, -20, 4, true, this::node4Learn, this::node4Unlearn, this::node4Status, this::node4CanUnlearn));
    
        jumps = new ArrayList<Player>();

        node1AbilityKey = new NamespacedKey(_plugin, "speed_1Ability");
        doubleJumpCooldownKey = new NamespacedKey(_plugin, "speed_DJCD");
        node2AbilityKey = new NamespacedKey(_plugin, "speed_2Ability");
        node3AbilityKey = new NamespacedKey(_plugin, "speed_3Ability");
        node4AbilityKey = new NamespacedKey(_plugin, "speed_4Ability");
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
            data.set(doubleJumpCooldownKey, PersistentDataType.INTEGER, 30*20);
            player.getWorld().playSound(player.getLocation(), Sound.BLOCK_BIG_DRIPLEAF_FALL, 1, 1);
            
            Location launch = player.getLocation().clone();

            if(data.has(node3AbilityKey, PersistentDataType.BOOLEAN) && data.get(node3AbilityKey, PersistentDataType.BOOLEAN))
            {
                launch.setPitch(-5);
                player.setVelocity(launch.getDirection().multiply(1.6));
                player.getWorld().spawnParticle(Particle.REDSTONE, player.getLocation().clone().add(0,1,0), 175, 0.4, 0.7, 0.4, new Particle.DustOptions(Color.WHITE, 1f));
            }
            else
            {
                launch.setPitch(-Math.abs(launch.getPitch()));
                player.setVelocity(launch.getDirection());
                player.getWorld().spawnParticle(Particle.REDSTONE, player.getLocation().clone().subtract(0,0.5,0), 175, 1, 0.1, 1, new Particle.DustOptions(Color.WHITE, 1f));
            
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
            bar = "☁ " + ((cooldown >= 0) ? (cooldown/20 + 1) + "s" : "READY");

            if(player.isOnGround() && data.get(doubleJumpCooldownKey, PersistentDataType.INTEGER) <= 0)
                player.setAllowFlight(true);
        }

        if(data.has(node2AbilityKey, PersistentDataType.BOOLEAN) && data.get(node2AbilityKey, PersistentDataType.BOOLEAN))
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 2, 1));

        if(data.has(node1AbilityKey, PersistentDataType.BOOLEAN) && data.get(node1AbilityKey, PersistentDataType.BOOLEAN))
        {
            
        }

        if(data.has(node4AbilityKey, PersistentDataType.BOOLEAN) && data.get(node4AbilityKey, PersistentDataType.BOOLEAN))
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 2, 0));

        return bar;
    }

    public void node0Learn(Player player) { player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.11f); }
    public void node0Unlearn(Player player) { player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.1f); }
    public void node0Status(Player player, Node node) { node.locked = false; }
    public void node0CanUnlearn(Player player, Node node) { if(!nodes.get(4).aquired && !nodes.get(10).aquired) unlearnNode(player, node); }

    public void node1Learn(Player player) { player.getPersistentDataContainer().set(node1AbilityKey, PersistentDataType.BOOLEAN, true); player.getPersistentDataContainer().set(doubleJumpCooldownKey, PersistentDataType.INTEGER, 0); }
    public void node1Unlearn(Player player) { player.getPersistentDataContainer().set(node1AbilityKey, PersistentDataType.BOOLEAN, false); player.setAllowFlight(false); }
    public void node1Status(Player player, Node node) { if(nodes.get(1).aquired && !nodes.get(10).aquired) node.locked = false; else node.locked = true; }
    public void node1CanUnlearn(Player player, Node node) { if(!nodes.get(5).aquired && !nodes.get(7).aquired) unlearnNode(player, node); }

    public void node2Learn(Player player) { player.getPersistentDataContainer().set(node2AbilityKey, PersistentDataType.BOOLEAN, true); }
    public void node2Unlearn(Player player) { player.getPersistentDataContainer().set(node2AbilityKey, PersistentDataType.BOOLEAN, false); }
    public void node2Status(Player player, Node node) { if(nodes.get(4).aquired) node.locked = false; else node.locked = true; }
    public void node2CanUnlearn(Player player, Node node) { unlearnNode(player, node); }

    public void node3Learn(Player player) { player.getPersistentDataContainer().set(node3AbilityKey, PersistentDataType.BOOLEAN, true); }
    public void node3Unlearn(Player player) { player.getPersistentDataContainer().set(node3AbilityKey, PersistentDataType.BOOLEAN, false); }
    public void node3Status(Player player, Node node) { if(nodes.get(4).aquired) node.locked = false; else node.locked = true; }
    public void node3CanUnlearn(Player player, Node node) { unlearnNode(player, node); }

    public void node4Learn(Player player) { player.getPersistentDataContainer().set(node4AbilityKey, PersistentDataType.BOOLEAN, true); }
    public void node4Unlearn(Player player) { player.getPersistentDataContainer().set(node4AbilityKey, PersistentDataType.BOOLEAN, false); }
    public void node4Status(Player player, Node node) { if(nodes.get(1).aquired && !nodes.get(4).aquired) node.locked = false; else node.locked = true; }
    public void node4CanUnlearn(Player player, Node node) { unlearnNode(player, node); }

}

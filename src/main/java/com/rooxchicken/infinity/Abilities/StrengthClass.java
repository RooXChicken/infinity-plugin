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

public class StrengthClass extends Ability
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

    public StrengthClass(Infinity _plugin)
    {
        super(_plugin);
        
        playerNodeMap = new HashMap<Player, ArrayList<Node>>();
        nodeList = new ArrayList<Node>();
        
        name = "Strength";
        header = "2_srt_Strength_1_0.9_0.2_0.2_true_1.0";

        nodeList.add(new Node(_plugin, "strength", "n", "n", 0, 41, -1, false, false, null, null, null, null));
        nodeList.add(new Node(_plugin, "strength", "icons/12", "+0.5 attack damage", 0, 40, 0, true, false, this::node0Learn, this::node0Unlearn, this::node0Status, this::node0CanUnlearn));
        nodeList.add(new Node(_plugin, "strength", "lArrow", "n", -20, 40, -1, false, false, null, null, null, null));
        nodeList.add(new Node(_plugin, "strength", "n", "n", -40, 40, -1, false, false, null, null, null, null));
        nodeList.add(new Node(_plugin, "strength", "n", "n", 0, 40, -1, false, true, null, null, null, null));
        nodeList.add(new Node(_plugin, "strength", "rArrow", "n", 20, 40, -1, false, false, null, null, null, null));
        nodeList.add(new Node(_plugin, "strength", "n", "n", 40, 40, -1, false, false, null, null, null, null));
        nodeList.add(new Node(_plugin, "strength", "n", "n", 0, 40, -1, false, true, null, null, null, null));
        nodeList.add(new Node(_plugin, "strength", "n", "n", 0, 15, -1, false, false, null, null, null, null));
        nodeList.add(new Node(_plugin, "strength", "icons/11", "Immune to Weakness", 0, 10, 1, true, false, this::node1Learn, this::node1Unlearn, this::node1Status, this::node1CanUnlearn));
        nodeList.add(new Node(_plugin, "strength", "icons/30", "Every 8 crits does 1.5x damage", 0, -20, 2, true, false, this::node2Learn, this::node2Unlearn, this::node2Status, this::node2CanUnlearn));
        nodeList.add(new Node(_plugin, "strength", "uArrow", "n", 0, -35, -1, false, false, null, null, null, null));
        nodeList.add(new Node(_plugin, "strength", "icons/27", "Give every player in a 20 block radius glowing (COOLDOWN: 1m)", 0, -50, 3, true, false, this::node3Learn, this::node3Unlearn, this::node3Status, this::node3CanUnlearn));

        nodeList.add(new Node(_plugin, "strength", "n", "n", -40, 41, -1, false, true, null, null, null, null));
        nodeList.add(new Node(_plugin, "strength", "icons/44", "+0.5 attack damage", -40, 40, 4, true, false, this::node4Learn, this::node4Unlearn, this::node4Status, this::node4CanUnlearn));
        nodeList.add(new Node(_plugin, "strength", "lArrow", "n", -60, 40, -1, false, false, null, null, null, null));
        nodeList.add(new Node(_plugin, "strength", "n", "n", -70, 40, -1, false, false, null, null, null, null));
        nodeList.add(new Node(_plugin, "strength", "n", "n", -40, 40, -1, false, true, null, null, null, null));
        nodeList.add(new Node(_plugin, "strength", "uArrow", "n", -40, 20, -1, false, false, null, null, null, null));
        nodeList.add(new Node(_plugin, "strength", "n", "n", -40, 10, -1, false, false, null, null, null, null));

        nodeList.add(new Node(_plugin, "strength", "icons/14", "Strength 1 Effect", -75, 40, 5, true, true, this::node5Learn, this::node5Unlearn, this::node5Status, this::node5CanUnlearn));
        nodeList.add(new Node(_plugin, "strength", "icons/16", "Extra attack damage while Weakness is applied", -40, 5, 6, true, true, this::node6Learn, this::node6Unlearn, this::node6Status, this::node6CanUnlearn));

        nodeList.add(new Node(_plugin, "strength", "n", "n", 40, 41, -1, false, true, null, null, null, null));
        nodeList.add(new Node(_plugin, "strength", "icons/22", "10% chance to deal 4x damage to armor durability", 40, 40, 7, true, false, this::node7Learn, this::node7Unlearn, this::node7Status, this::node7CanUnlearn));
        nodeList.add(new Node(_plugin, "strength", "rArrow", "n", 60, 40, -1, false, false, null, null, null, null));
        nodeList.add(new Node(_plugin, "strength", "n", "n", 70, 40, -1, false, false, null, null, null, null));
        nodeList.add(new Node(_plugin, "strength", "n", "n", 40, 40, -1, false, true, null, null, null, null));
        nodeList.add(new Node(_plugin, "strength", "uArrow", "n", 40, 20, -1, false, false, null, null, null, null));
        nodeList.add(new Node(_plugin, "strength", "n", "n", 40, 10, -1, false, false, null, null, null, null));

        nodeList.add(new Node(_plugin, "strength", "icons/7", "Right click a compass to track a specified player (COOLDOWN: 30m)", 75, 40, 8, true, true, this::node8Learn, this::node8Unlearn, this::node8Status, this::node8CanUnlearn));
        nodeList.add(new Node(_plugin, "strength", "icons/26", "The lower your health, the more damage you deal (CAP: 2x damage at 1hp)", 40, 5, 9, true, true, this::node9Learn, this::node9Unlearn, this::node9Status, this::node9CanUnlearn));
        // node1AbilityKey = new NamespacedKey(_plugin, "speed_1Ability");
        // doubleJumpCooldownKey = new NamespacedKey(_plugin, "speed_DJCD");
        // node2AbilityKey = new NamespacedKey(_plugin, "speed_2Ability");
        // node3AbilityKey = new NamespacedKey(_plugin, "speed_3Ability");
        // node4AbilityKey = new NamespacedKey(_plugin, "speed_4Ability");
        // node5AbilityKey = new NamespacedKey(_plugin, "speed_5Ability");
        // node7AbilityKey = new NamespacedKey(_plugin, "speed_7Ability");
        // node8AbilityKey = new NamespacedKey(_plugin, "speed_8Ability");
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

    public String tickAbilities(Player player)
    {
        String bar = "";
        PersistentDataContainer data = player.getPersistentDataContainer();

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

        // if(data.has(node8AbilityKey, PersistentDataType.BOOLEAN) && data.get(node8AbilityKey, PersistentDataType.BOOLEAN) && Math.random() < 0.1)
        // {
        //     entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 1));
        // }
    }

    public void node0Learn(Player player) { }
    public void node0Unlearn(Player player) { }
    public void node0Status(Player player, Node node) { node.locked = false; }
    public void node0CanUnlearn(Player player, Node node) { unlearnNode(player, node); }

    public void node1Learn(Player player) { }
    public void node1Unlearn(Player player) { }
    public void node1Status(Player player, Node node) { node.locked = false; }
    public void node1CanUnlearn(Player player, Node node) { unlearnNode(player, node); }

    public void node2Learn(Player player) { }
    public void node2Unlearn(Player player) { }
    public void node2Status(Player player, Node node) { node.locked = false; }
    public void node2CanUnlearn(Player player, Node node) { unlearnNode(player, node); }
    
    public void node3Learn(Player player) { }
    public void node3Unlearn(Player player) { }
    public void node3Status(Player player, Node node) { node.locked = false; }
    public void node3CanUnlearn(Player player, Node node) { unlearnNode(player, node); }

    public void node4Learn(Player player) { }
    public void node4Unlearn(Player player) { }
    public void node4Status(Player player, Node node) { node.locked = false; }
    public void node4CanUnlearn(Player player, Node node) { unlearnNode(player, node); }

    public void node5Learn(Player player) { }
    public void node5Unlearn(Player player) { }
    public void node5Status(Player player, Node node) { node.locked = false; }
    public void node5CanUnlearn(Player player, Node node) { unlearnNode(player, node); }

    public void node6Learn(Player player) { }
    public void node6Unlearn(Player player) { }
    public void node6Status(Player player, Node node) { node.locked = false; }
    public void node6CanUnlearn(Player player, Node node) { unlearnNode(player, node); }

    public void node7Learn(Player player) { }
    public void node7Unlearn(Player player) { }
    public void node7Status(Player player, Node node) { node.locked = false; }
    public void node7CanUnlearn(Player player, Node node) { unlearnNode(player, node); }

    public void node8Learn(Player player) { }
    public void node8Unlearn(Player player) { }
    public void node8Status(Player player, Node node) { node.locked = false; }
    public void node8CanUnlearn(Player player, Node node) { unlearnNode(player, node); }

    public void node9Learn(Player player) { }
    public void node9Unlearn(Player player) { }
    public void node9Status(Player player, Node node) { node.locked = false; }
    public void node9CanUnlearn(Player player, Node node) { unlearnNode(player, node); }

}

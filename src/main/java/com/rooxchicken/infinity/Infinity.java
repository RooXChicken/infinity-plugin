package com.rooxchicken.infinity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.io.filefilter.CanExecuteFileFilter;
import org.bukkit.Bukkit;
import org.bukkit.FluidCollisionMode;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerCommandSendEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import com.comphenix.protocol.ProtocolLib;
import com.comphenix.protocol.ProtocolLibrary;
import com.google.common.base.Predicate;
import com.rooxchicken.infinity.Abilities.Ability;
import com.rooxchicken.infinity.Abilities.HealthClass;
import com.rooxchicken.infinity.Abilities.SpeedClass;
import com.rooxchicken.infinity.Abilities.StealthClass;
import com.rooxchicken.infinity.Abilities.StrengthClass;
import com.rooxchicken.infinity.Commands.FirstAbility;
import com.rooxchicken.infinity.Commands.ResetCooldown;
import com.rooxchicken.infinity.Commands.SecondAbility;
import com.rooxchicken.infinity.Commands.SetPoints;
import com.rooxchicken.infinity.Commands.SkillTree;
import com.rooxchicken.infinity.Commands.NodeAction;
import com.rooxchicken.infinity.Commands.VerifyMod;
import com.rooxchicken.infinity.Tasks.Task;
import com.rooxchicken.infinity.Tasks.TickPlayers;

public class Infinity extends JavaPlugin implements Listener
{
    public static NamespacedKey skillTreeKey;
    public static NamespacedKey pointsKey;

    public static ArrayList<Task> tasks;
    public ArrayList<Player> hasMod;

    private List<String> blockedCommands = new ArrayList<>();

    public StrengthClass strength;
    public SpeedClass speed;
    public HealthClass health;
    public StealthClass stealth;

    @Override
    public void onEnable()
    {
        tasks = new ArrayList<Task>();
        tasks.add(new TickPlayers(this));

        hasMod = new ArrayList<Player>();

        skillTreeKey = new NamespacedKey(this, "skillTree");
        pointsKey = new NamespacedKey(this, "points");

        getServer().getPluginManager().registerEvents(this, this);
        
        this.getCommand("hdn_ability1_srt").setExecutor(new FirstAbility(this, 0));
        this.getCommand("hdn_ability2_srt").setExecutor(new SecondAbility(this, 0));

        blockedCommands.add("hdn_ability1_srt");
		blockedCommands.add("hdn_ability2_srt");

        this.getCommand("hdn_ability1_rpt").setExecutor(new FirstAbility(this, 1));
        this.getCommand("hdn_ability2_rpt").setExecutor(new SecondAbility(this, 1));

        blockedCommands.add("hdn_ability1_rpt");
		blockedCommands.add("hdn_ability2_rpt");

        this.getCommand("hdn_ability1_end").setExecutor(new FirstAbility(this, 2));
        this.getCommand("hdn_ability2_end").setExecutor(new SecondAbility(this, 2));

        blockedCommands.add("hdn_ability1_end");
		blockedCommands.add("hdn_ability2_end");

        this.getCommand("hdn_verifymod").setExecutor(new VerifyMod(this));
		blockedCommands.add("hdn_verifymod");

        this.getCommand("hdn_action").setExecutor(new NodeAction(this));
		blockedCommands.add("hdn_action");
        
        this.getCommand("skilltree").setExecutor(new SkillTree(this));
        this.getCommand("setpoints").setExecutor(new SetPoints(this));
        this.getCommand("resetcooldown").setExecutor(new ResetCooldown(this));

        strength = new StrengthClass(this);
        speed = new SpeedClass(this);
        health = new HealthClass(this);
        stealth = new StealthClass(this);

        getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable()
        {
            public void run()
            {
                ArrayList<Task> _tasks = new ArrayList<Task>();
                for(Task t : tasks)
                    _tasks.add(t);
                
                ArrayList<Task> toRemove = new ArrayList<Task>();

                for(Task t : _tasks)
                {
                    t.tick();

                    if(t.cancel)
                        toRemove.add(t);
                }

                for(Task t : toRemove)
                {
                    t.onCancel();
                    tasks.remove(t);
                }
            }
        }, 0, 1);

        getLogger().info("Infinity (limited to 1987) (made by roo)");
    }

    @EventHandler
    private void unRegisterPlayer(PlayerQuitEvent e)
    {
        hasMod.remove(e.getPlayer());
    }

    @EventHandler
	private void removeCommands(PlayerCommandSendEvent e)
    {
		e.getCommands().removeAll(blockedCommands);
	}

    @EventHandler
    public void unregisterProtocollib(PluginDisableEvent event)
    {
        ProtocolLibrary.getProtocolManager().removePacketListeners(this);
    }
    
    @EventHandler
    private void preventKick(PlayerKickEvent event)
    {
        if(event.getReason().equals("Kicked for spamming"))
            event.setCancelled(true);
    }

    public void verifyMod(Player player)
    {
        if(!hasMod.contains(player))
            hasMod.add(player);
    }
}

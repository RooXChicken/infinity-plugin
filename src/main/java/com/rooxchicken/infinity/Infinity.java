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
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import com.google.common.base.Predicate;
import com.rooxchicken.infinity.Abilities.Ability;
import com.rooxchicken.infinity.Commands.FirstAbility;
import com.rooxchicken.infinity.Commands.ResetCooldown;
import com.rooxchicken.infinity.Commands.SecondAbility;
import com.rooxchicken.infinity.Commands.SkillTree;
import com.rooxchicken.infinity.Commands.VerifyMod;
import com.rooxchicken.infinity.Tasks.Task;

public class Infinity extends JavaPlugin implements Listener
{
    public static NamespacedKey skillTreeKey;

    public static ArrayList<Task> tasks;
    public ArrayList<Player> hasMod;

    private List<String> blockedCommands = new ArrayList<>();

    @Override
    public void onEnable()
    {
        tasks = new ArrayList<Task>();
        hasMod = new ArrayList<Player>();
        //tasks.add(new CooldownTask(this));

        skillTreeKey = new NamespacedKey(this, "skillTree");

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
        
        this.getCommand("skilltree").setExecutor(new SkillTree(this));

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

    // //format: name_index_ability1 name_ability1 desc (tooltip)_ability2 name_ability2 desc (tooltip)_unlock-requirements
    // private void sendAbilityDesc(Player player)
    // {
    //     sendPlayerData(player, "2_.");
    //     sendPlayerData(player, "2_Varuna_0_Condiut Power_Typhoon (COOLDOWN: 30s)_Creates a large ring of light blue+particles that spikes entities into+the air, dealing damage.+(BUFFED IN WATER/RAIN)_WaterJet (COOLDOWN: 1m)_Shoots a large water beam that+pushes entities back and deals+large damage._Build a condiut (will be deleted after)");
    //     sendPlayerData(player, "2_Agni_1_Fire Resistance_HeatSeek (COOLDOWN: 30s)_Releases a seeking shockwave that+scans the area for players, lighting+them on fire and giving them glowing.+Repeats until the scan is over._Cinder (COOLDOWN: 1m 30s)_Shoots a fire arrow that does+more damage the longer its held.+(BUFFED WHEN ON FIRE/IN NETHER)_Bring a ghast into the overworld");
    //     sendPlayerData(player, "2_Enil_2_Haste 2_Jolt (COOLDOWN: 45s)_Shoot a beam of lighting that gives+the target mining fatigue 3 and+slowness 3._Polarity (COOLDOWN: 2m)_Begins to charge the player with+electricity whenever attacking an+entity. Shoots an electric shockwave+when enough damage is dealt._Strike a creeper with Jolt while it's thunderstorming");
    //     sendPlayerData(player, "2_Boreas_3_Speed 2_Gust (COOLDOWN: 30s)_Launches the player backward,+acting as a dodge_Vortex (COOLDOWN: 1m)_Picks up players and entities in a+ball of wind that can be launched._Get these advancements: [Caves and Cliffs, Star Trader, Sniper Duel]");
    //     sendPlayerData(player, "2_Ymir_4_Fast Ice Speed_Frostbite (COOLDOWN: 1m)_Creates an icy shield that+completely protects against the next+3 hits, and freezes whoever attacks+the shield._Permafrost (COOLDOWN: 2m)_Summons an ice dome that slowly+freezes everything inside._Bring a blaze to an ice biome and kill it");
    //     sendPlayerData(player, "2_Dolus_5_Increased gravity_Retract (COOLDOWN: 45s)_Creates a gravitational pull that+brings all entities closer_Crush (COOLDOWN: 2m)_Creates a dome where players+cannot jump and entities are slowed_Survive at y=-67 or less for 1 minute");

    // }

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

package com.rooxchicken.infinity;

import java.util.ArrayList;
import java.util.Date;
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
import org.bukkit.enchantments.Enchantment;
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
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
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
import com.rooxchicken.infinity.Abilities.LuckClass;
import com.rooxchicken.infinity.Abilities.SpeedClass;
import com.rooxchicken.infinity.Abilities.StealthClass;
import com.rooxchicken.infinity.Abilities.StrengthClass;
import com.rooxchicken.infinity.Commands.AddAllPoints;
import com.rooxchicken.infinity.Commands.FirstAbility;
import com.rooxchicken.infinity.Commands.ResetCooldown;
import com.rooxchicken.infinity.Commands.ResetTree;
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
    public static NamespacedKey killsKey;
    public static NamespacedKey nodeActionKey;

    public static ArrayList<Task> tasks;
    public static ArrayList<Player> hasMod;

    private List<String> blockedCommands = new ArrayList<>();

    public StrengthClass strength;
    public SpeedClass speed;
    public HealthClass health;
    public StealthClass stealth;
    public LuckClass luck;

    public static ItemStack token;
    public static ItemStack unlimiter;
    public static ItemStack extra;
    public NodeAction nodeAction;

    @Override
    public void onEnable()
    {
        tasks = new ArrayList<Task>();
        tasks.add(new TickPlayers(this));

        token = new ItemStack(Material.PAPER);
        {ItemMeta meta = token.getItemMeta();
        meta.setDisplayName("§6Token");
        meta.setCustomModelData(1);
        meta.addEnchant(Enchantment.UNBREAKING, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        ArrayList<String> lore = new ArrayList<String>();
        lore.add("§6Allows you to gain a skill in the skill tree!");
        lore.add("§6Right click or use /skilltree to use");
        meta.setLore(lore);
        token.setItemMeta(meta);}

        extra = new ItemStack(Material.PAPER);
        {ItemMeta meta = extra.getItemMeta();
        meta.setDisplayName("§7Extra");
        meta.setCustomModelData(3);
        meta.addEnchant(Enchantment.UNBREAKING, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        ArrayList<String> lore = new ArrayList<String>();
        lore.add("§7For each Extra in your inventory,");
        lore.add("§7you can spend 1 extra point in the Skill Tree!");
        lore.add("§7Must be kept in your inventory");
        meta.setLore(lore);
        extra.setItemMeta(meta);}

        unlimiter = new ItemStack(Material.PAPER);
        {ItemMeta meta = unlimiter.getItemMeta();
        meta.setDisplayName("§4Unlimiter");
        meta.setCustomModelData(2);
        meta.addEnchant(Enchantment.UNBREAKING, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        ArrayList<String> lore = new ArrayList<String>();
        lore.add("§4For each kill you get with this item in your inventory,");
        lore.add("§4you can spend 1 extra point in the Skill Tree");
        lore.add("§4Kills: 0");
        meta.setLore(lore);
        unlimiter.setItemMeta(meta);}

        hasMod = new ArrayList<Player>();

        skillTreeKey = new NamespacedKey(this, "skillTree");
        pointsKey = new NamespacedKey(this, "points");
        killsKey = new NamespacedKey(this, "kills");
        nodeActionKey = new NamespacedKey(this, "nodeAction");

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

        nodeAction = new NodeAction(this);
        this.getCommand("hdn_action").setExecutor(nodeAction);
		blockedCommands.add("hdn_action");
        
        this.getCommand("skilltree").setExecutor(new SkillTree(this));
        this.getCommand("setpoints").setExecutor(new SetPoints(this));
        this.getCommand("resetcooldown").setExecutor(new ResetCooldown(this));
        this.getCommand("resettree").setExecutor(new ResetTree(this));
        this.getCommand("addallpoints").setExecutor(new AddAllPoints(this));

        strength = new StrengthClass(this);
        speed = new SpeedClass(this);
        health = new HealthClass(this);
        stealth = new StealthClass(this);
        luck = new LuckClass(this);

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
    public void useToken(PlayerInteractEvent event)
    {
        if(!event.getAction().equals(Action.RIGHT_CLICK_AIR) && !event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
            return;

        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if(item != null && item.hasItemMeta() && item.getItemMeta().equals(token.getItemMeta()))
        {
            Library.sendSkillTree(player);
        }
    }

    @EventHandler
    public void dropRecentNode(PlayerDeathEvent event)
    {
        Player player = event.getEntity();
        PersistentDataContainer data = player.getPersistentDataContainer();

        Library.resetKills(player, event.getDrops());

        if(player.getKiller() != null)
        {
            Player killer = player.getKiller();
            for(ItemStack item : killer.getInventory().getContents())
            {
                if(item != null && item.hasItemMeta() && item.getItemMeta().equals(unlimiter.getItemMeta()))
                {
                    killer.getInventory().setItem(killer.getInventory().first(item), Library.addKill(killer, item).clone());
                    
                    if(Library.getKills(killer) > 5)
                        player.ban("§4§lERROR: Infinity = 0!", (Date)null, null);
                }
            }
        }

        if(!data.has(nodeActionKey, PersistentDataType.STRING))
            return;

        String action = data.get(nodeActionKey, PersistentDataType.STRING);
        if(action.equals(""))
            return;

        String[] args = action.split("_");
        
        if(nodeAction.unlearnRaw(player, Integer.parseInt(args[0]), Integer.parseInt(args[1])))
            player.getWorld().dropItemNaturally(player.getLocation(), token);

        data.set(nodeActionKey, PersistentDataType.STRING, "");
    }

    @EventHandler
    private void registerPlayer(PlayerJoinEvent e)
    {
        Library.sendPlayerData(e.getPlayer(), "0");
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


    public void verifyMod(Player player)
    {
        if(!hasMod.contains(player))
            hasMod.add(player);
    }
}

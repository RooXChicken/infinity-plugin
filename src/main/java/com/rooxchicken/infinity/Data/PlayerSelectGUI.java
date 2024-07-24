package com.rooxchicken.infinity.Data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.material.Skull;
import org.bukkit.persistence.PersistentDataType;

import com.rooxchicken.infinity.Infinity;

public class PlayerSelectGUI implements Listener
{
    private Infinity plugin;
    private Player player;
    private int guiIndex = 0;

    public PlayerSelectGUI(Infinity _plugin, Player _player)
    {
        plugin = _plugin;
        player = _player;

        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);

        Inventory inventory = Bukkit.createInventory(null, 9, "Select a Player");
        player.openInventory(inventory);

        updateGUI();

    }

    @EventHandler
    public void onOpen(InventoryOpenEvent event)
    {
        
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event)
    {
        if(event.getPlayer().equals(player))
        {
            HandlerList.unregisterAll(this);
            plugin.strength.guis.remove(this);
        }
    }


    @EventHandler
    public void handleClick(InventoryClickEvent event)
    {
        event.setCancelled(true);
        int slot = event.getSlot();

        ItemStack clicked = event.getCurrentItem();
        switch(slot)
        {
            case 0:
                guiIndex--;
            break;
            case 8:
                guiIndex++;
            break;
            
            default:
                if(clicked.getType().equals(Material.PLAYER_HEAD))
                {
                    SkullMeta meta = (SkullMeta)clicked.getItemMeta();
                    Player track = meta.getOwningPlayer().getPlayer();
                    if(track.isValid())
                    {
                        player.getPersistentDataContainer().set(plugin.strength.ability8TimerKey, PersistentDataType.INTEGER, 60*20);
                        player.getPersistentDataContainer().set(plugin.strength.ability8CooldownKey, PersistentDataType.INTEGER, 30*60*20);
                        plugin.strength.playerTrackMap.put(player, track);
                        track.sendMessage("§4You are being tracked...");
                        track.playSound(track.getLocation(), Sound.ENTITY_WITHER_SPAWN, 0.7f, 1.0f);
                        player.closeInventory();
                        return;
                    }
                }
            break;
        }

        if(guiIndex > getPlayers().size()/9)
            guiIndex = 0;
        if(guiIndex < 0)
            guiIndex = getPlayers().size()/9;

        updateGUI();
    }

    private ArrayList<Player> getPlayers()
    {
        ArrayList<Player> players = new ArrayList<Player>();
        for(Player p : Bukkit.getOnlinePlayers())
            if(player != p)
            players.add(p);

        return players;
    }

    public void updateGUI()
    {
        Inventory inventory = player.getOpenInventory().getTopInventory();
        ItemStack leftArrow = new ItemStack(Material.RED_DYE);
        ItemMeta lMeta = leftArrow.getItemMeta();
        lMeta.setDisplayName("§7Left");
        leftArrow.setItemMeta(lMeta);

        ItemStack rightArrow = new ItemStack(Material.GREEN_DYE);
        ItemMeta rMeta = leftArrow.getItemMeta();
        rMeta.setDisplayName("§7Right");
        rightArrow.setItemMeta(rMeta);

        inventory.setItem(0, leftArrow);
        inventory.setItem(8, rightArrow);

        ArrayList<Player> players = getPlayers();

        for(int i = 2; i < 7; i++)
        {
            int index = i - 2;
            if(index < players.size())
            {
                ItemStack head = new ItemStack(Material.PLAYER_HEAD);
                SkullMeta meta = (SkullMeta)head.getItemMeta();
                
                meta.setOwningPlayer(players.get(index + (9*guiIndex)));

                head.setItemMeta(meta);
                inventory.setItem(i, head);
            }
        }

        //player.getOpenInventory().setItem(guiIndex, rightArrow);
    }
}

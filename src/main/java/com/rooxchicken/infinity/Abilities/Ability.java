package com.rooxchicken.infinity.Abilities;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import com.rooxchicken.infinity.Infinity;
import com.rooxchicken.infinity.Library;

public abstract class Ability implements Listener
{
    private Infinity plugin;
    public String name = "null";
    public int type = -1;
    public ArrayList<String> nodes;

    public Ability(Infinity _plugin) { plugin = _plugin; Bukkit.getServer().getPluginManager().registerEvents(this, plugin); }

    public void sendNodes(Player player)
    {
        for(String node : nodes)
        {
            Library.sendPlayerData(player, node);
        }
    }
}

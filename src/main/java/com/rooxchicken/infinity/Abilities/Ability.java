package com.rooxchicken.infinity.Abilities;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import com.rooxchicken.infinity.Infinity;
import com.rooxchicken.infinity.Library;
import com.rooxchicken.infinity.Data.Node;

public abstract class Ability implements Listener
{
    private Infinity plugin;
    public String name = "null";
    public int type = -1;

    public String header;
    public HashMap<Player, ArrayList<Node>> playerNodeMap;
    public ArrayList<Node> nodeList;

    public Ability(Infinity _plugin) { plugin = _plugin; Bukkit.getServer().getPluginManager().registerEvents(this, plugin); }

    public void sendNodes(Player player, boolean resetZoom)
    {
        if(playerNodeMap.containsKey(player))
            playerNodeMap.remove(player);
        
        ArrayList<Node> nodes = new ArrayList<Node>();
        for(Node node : nodeList)
            nodes.add(node.cloneNode());

        playerNodeMap.put(player, nodes);
        for(Node node : playerNodeMap.get(player))
            node.checkStatus(player);

        Library.sendPlayerData(player, header);

        for(Node node : playerNodeMap.get(player))
            Library.sendPlayerData(player, node.sendNode(player));

        Library.sendPlayerData(player, "3_" + Library.getPoints(player) + "_" + resetZoom);
    }

    public void unlearnNode(Player player, Node node)
    {
        PersistentDataContainer data = player.getPersistentDataContainer();

        node.aquired = false;
        Library.addPoint(player);
        data.set(node.key, PersistentDataType.BOOLEAN, false);

        player.playSound(player.getLocation(), Sound.BLOCK_BEACON_DEACTIVATE, 0.6f, 1);
        node.unlearn.accept(player);
    }
}

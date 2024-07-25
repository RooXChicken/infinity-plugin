package com.rooxchicken.infinity.Data;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import com.rooxchicken.infinity.Infinity;
import com.rooxchicken.infinity.Library;

public class Node
{
    Infinity plugin;
    public String abilityClass;
    public String icon;
    public String description;

    public int x;
    public int y;
    public int clickIndex;

    public boolean drawTexture;
    public boolean aquired;
    public boolean locked;
    public boolean skip;

    public NamespacedKey key;
    public Consumer<Player> learn;
    public Consumer<Player> unlearn;
    public BiConsumer<Player, Node> status;
    public BiConsumer<Player, Node> canUnlearn;

    public Node(Infinity _plugin, String _abilityClass, String _icon, String _description, int _x, int _y, int _clickIndex, boolean _drawTexture, boolean _skip, Consumer<Player> _learn, Consumer<Player> _unlearn, BiConsumer<Player, Node> _status, BiConsumer<Player, Node> _canUnlearn)
    {
        plugin = _plugin;
        abilityClass = _abilityClass;
        icon = _icon;
        description = _description;
        
        x = _x;
        y = _y;
        clickIndex = _clickIndex;

        drawTexture = _drawTexture;
        skip = _skip;
        aquired = false;

        locked = false;

        learn = _learn;
        unlearn = _unlearn;
        status = _status;
        canUnlearn = _canUnlearn;
        
        if(clickIndex != -1)
            key = new NamespacedKey(_plugin, abilityClass + "_" + clickIndex);
    }

    public void checkStatus(Player player)
    {
        if(key != null)
        {
            PersistentDataContainer data = player.getPersistentDataContainer();
            if(!data.has(key, PersistentDataType.BOOLEAN))
                data.set(key, PersistentDataType.BOOLEAN, false);

            aquired = data.get(key, PersistentDataType.BOOLEAN);
        }

        if(status != null)
            status.accept(player, this);
    }

    public String sendNode(Player player)
    {
        checkStatus(player);

        return "2_" + icon + "_" + x + "_" + y + "_" + description + " CI: " + clickIndex + "_" + drawTexture + "_" + skip + "_" + aquired + "_" + locked + "_" + clickIndex;
    }

    public void action(Player player, int mouse)
    {
        PersistentDataContainer data = player.getPersistentDataContainer();
        if(key == null || locked)
            return;

        if(mouse == 0)
        {
            if(Library.getPoints(player) > 0 && !data.get(key, PersistentDataType.BOOLEAN))
            {
                aquired = true;
                Library.subtractPoint(player);
                data.set(key, PersistentDataType.BOOLEAN, true);

                player.playSound(player.getLocation(), Sound.BLOCK_BEACON_ACTIVATE, 0.6f, 1);
                learn.accept(player);
            }
        }

        if(mouse == 1)
        {
            if(data.get(key, PersistentDataType.BOOLEAN))
            {
                canUnlearn.accept(player, this);
            }
        }
    }

    public Node cloneNode()
    {
        return new Node(plugin, abilityClass, icon, description, x, y, clickIndex, drawTexture, skip, learn, unlearn, status, canUnlearn);
    }
}

package com.rooxchicken.infinity.Tasks;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.rooxchicken.infinity.Infinity;

public class RemoveCompass extends Task
{
    private Infinity plugin;
    public RemoveCompass(Infinity _plugin)
    {
        super(_plugin);
        plugin = _plugin;
        tickThreshold = 40;
    }

    @Override
    public void run()
    {
        for(Player player : Bukkit.getOnlinePlayers())
        {
            if(!plugin.strength.playerTrackMap.containsKey(player))
            for(ItemStack item : player.getInventory().getContents())
            {
                if(item != null && item.getType().equals(Material.COMPASS))
                {
                    if(item.hasItemMeta() && !item.getItemMeta().getDisplayName().contains("Compass"))
                        player.getInventory().setItem(player.getInventory().first(item), new ItemStack(Material.COMPASS));
                }
            }
        }
    }
}

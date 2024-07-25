package com.rooxchicken.infinity.Tasks;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.rooxchicken.infinity.Infinity;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;

public class TickPlayers extends Task
{
    Infinity plugin;

    public TickPlayers(Infinity _plugin)
    {
        super(_plugin);

        plugin = _plugin;
        tickThreshold = 1;
    }

    @Override
    public void run()
    {
        for(Player player : Bukkit.getOnlinePlayers())
        {
            String bar = "";
            bar += plugin.speed.tickAbilities(player);
            bar += plugin.strength.tickAbilities(player);
            //bar += plugin.stealth.tickAbilities(player);

            if(!bar.equals(""))
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(bar));
        }
    }
}

package com.rooxchicken.infinity.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.rooxchicken.infinity.Infinity;
public class ResetTree implements CommandExecutor
{
    private Infinity plugin;

    public ResetTree(Infinity _plugin)
    {
        plugin = _plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        Player player = Bukkit.getPlayer(sender.getName());

        plugin.strength.reset(player);
        plugin.speed.reset(player);
        plugin.health.reset(player);
        plugin.luck.reset(player);
        plugin.stealth.reset(player);
        
        player.sendMessage("§aYour tree has been reset!");

        return true;
    }

}
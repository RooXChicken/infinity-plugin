package com.rooxchicken.infinity.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.rooxchicken.infinity.Infinity;
public class ResetCooldown implements CommandExecutor
{
    private Infinity plugin;

    public ResetCooldown(Infinity _plugin)
    {
        plugin = _plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if(!sender.isOp())
            return false;

        Player player = Bukkit.getPlayer(sender.getName());
        plugin.speed.resetCooldown(player);
        plugin.strength.resetCooldown(player);
        plugin.luck.resetCooldown(player);

        return true;
    }

}

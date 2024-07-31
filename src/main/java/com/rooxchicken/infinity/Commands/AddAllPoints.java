package com.rooxchicken.infinity.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.rooxchicken.infinity.Infinity;
import com.rooxchicken.infinity.Library;
public class AddAllPoints implements CommandExecutor
{
    private Infinity plugin;

    public AddAllPoints(Infinity _plugin)
    {
        plugin = _plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if(!sender.isOp())
            return false;

        for(Player player : Bukkit.getOnlinePlayers())
        {
            int amount = Integer.parseInt(args[0]);
        
            Library.setPoints(player, amount);
        }

        return true;
    }

}

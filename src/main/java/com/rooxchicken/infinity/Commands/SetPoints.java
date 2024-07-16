package com.rooxchicken.infinity.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.rooxchicken.infinity.Infinity;
import com.rooxchicken.infinity.Library;
public class SetPoints implements CommandExecutor
{
    private Infinity plugin;

    public SetPoints(Infinity _plugin)
    {
        plugin = _plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if(!sender.isOp())
            return false;

        Library.setPoints(Bukkit.getPlayer(sender.getName()), Integer.parseInt(args[0]));

        return true;
    }

}

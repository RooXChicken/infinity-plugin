package com.rooxchicken.infinity.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.rooxchicken.infinity.Infinity;
import com.rooxchicken.infinity.Library;
public class VerifyMod implements CommandExecutor
{
    private Infinity plugin;

    public VerifyMod(Infinity _plugin)
    {
        plugin = _plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        plugin.verifyMod(Bukkit.getPlayer(sender.getName()));
        //Library.sendPlayerData(Bukkit.getPlayer(sender.getName()), "1_gangnamstylemc_0");

        return true;
    }

}

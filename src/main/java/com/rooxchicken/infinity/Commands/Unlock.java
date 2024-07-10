package com.rooxchicken.infinity.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.rooxchicken.infinity.Infinity;
import com.rooxchicken.infinity.Abilities.Ability;
public class Unlock implements CommandExecutor
{
    private Infinity plugin;

    public Unlock(Infinity _plugin)
    {
        plugin = _plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        Player player = Bukkit.getPlayer(sender.getName());

        int menu = Integer.parseInt(args[0]);

        switch(menu)
        {
            case 0:
                switch(Integer.parseInt(args[1]))
                {
                    //case 0: plugin.strength.sendNodes(); break;
                    case 1: plugin.speed.sendNodes(player); break;
                    case 2: plugin.health.sendNodes(player); break;
                    //case 3: plugin.luck.sendNodes(); break;
                    //case 4: plugin.stealth.sendNodes(); break;
                }
            break;
        }

        return true;
    }

}

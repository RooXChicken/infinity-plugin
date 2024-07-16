package com.rooxchicken.infinity.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.rooxchicken.infinity.Infinity;
import com.rooxchicken.infinity.Abilities.Ability;
public class NodeAction implements CommandExecutor
{
    private Infinity plugin;

    public NodeAction(Infinity _plugin)
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
                    case 1: plugin.speed.sendNodes(player, true); break;
                    case 2: plugin.health.sendNodes(player, true); break;
                    //case 3: plugin.luck.sendNodes(); break;
                    //case 4: plugin.stealth.sendNodes(); break;
                }
            break;
            case 1:
            break;

            case 2:
                switch(Integer.parseInt(args[1]))
                {
                    case 0: plugin.speed.nodes.get(1).action(player, Integer.parseInt(args[2])); break;
                    case 1: plugin.speed.nodes.get(4).action(player, Integer.parseInt(args[2])); break;
                    case 2: plugin.speed.nodes.get(5).action(player, Integer.parseInt(args[2])); break;
                    case 3: plugin.speed.nodes.get(7).action(player, Integer.parseInt(args[2])); break;
                    case 4: plugin.speed.nodes.get(10).action(player, Integer.parseInt(args[2])); break;
                }
                plugin.speed.sendNodes(player, false);
            break;
        }

        return true;
    }

}

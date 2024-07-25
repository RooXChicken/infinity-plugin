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
                    case 0: plugin.strength.sendNodes(player, true); break;
                    case 1: plugin.speed.sendNodes(player, true); break;
                    case 2: plugin.health.sendNodes(player, true); break;
                    //case 3: plugin.luck.sendNodes(); break;
                    case 4: plugin.stealth.sendNodes(player, true); break;
                }
            break;
            case 1:
                plugin.strength.findNode(player,Integer.parseInt(args[1])).action(player, Integer.parseInt(args[2]));
                plugin.strength.sendNodes(player, false);
            break;

            case 2:
                switch(Integer.parseInt(args[1]))
                {
                    case 0: plugin.speed.findNode(player, "28").action(player, Integer.parseInt(args[2])); break;
                    case 1: plugin.speed.findNode(player, "1").action(player, Integer.parseInt(args[2])); break;
                    case 2: plugin.speed.findNode(player, "20").action(player, Integer.parseInt(args[2])); break;
                    case 3: plugin.speed.findNode(player, "41").action(player, Integer.parseInt(args[2])); break;
                    case 4: plugin.speed.findNode(player, "42").action(player, Integer.parseInt(args[2])); break;
                    case 5: plugin.speed.findNode(player, "36").action(player, Integer.parseInt(args[2])); break;
                    case 6: plugin.speed.findNode(player, "4").action(player, Integer.parseInt(args[2])); break;
                    case 7: plugin.speed.findNode(player, "39").action(player, Integer.parseInt(args[2])); break;
                    case 8: plugin.speed.findNode(player, "19").action(player, Integer.parseInt(args[2])); break;
                }
                plugin.speed.sendNodes(player, false);
            break;

            case 5:
                plugin.stealth.findNode(player, Integer.parseInt(args[1])).action(player, Integer.parseInt(args[2]));
                plugin.stealth.sendNodes(player, false);
            break;
        }

        return true;
    }

}

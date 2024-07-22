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
                    //case 4: plugin.stealth.sendNodes(); break;
                }
            break;
            case 1:
                switch(Integer.parseInt(args[1]))
                {
                    case 0: plugin.strength.findNode(player, "12").action(player, Integer.parseInt(args[2])); break;
                    case 1: plugin.strength.findNode(player, "11").action(player, Integer.parseInt(args[2])); break;
                    case 2: plugin.strength.findNode(player, "30").action(player, Integer.parseInt(args[2])); break;
                    case 3: plugin.strength.findNode(player, "27").action(player, Integer.parseInt(args[2])); break;
                    case 4: plugin.strength.findNode(player, "44").action(player, Integer.parseInt(args[2])); break;
                    case 5: plugin.strength.findNode(player, "14").action(player, Integer.parseInt(args[2])); break;
                    case 6: plugin.strength.findNode(player, "16").action(player, Integer.parseInt(args[2])); break;
                    case 7: plugin.strength.findNode(player, "22").action(player, Integer.parseInt(args[2])); break;
                    case 8: plugin.strength.findNode(player, "7").action(player, Integer.parseInt(args[2])); break;
                    case 9: plugin.strength.findNode(player, "26").action(player, Integer.parseInt(args[2])); break;
                }
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
                    //case 6: plugin.speed.nodes.get(22).action(player, Integer.parseInt(args[2])); break;
                }
                plugin.speed.sendNodes(player, false);
            break;
        }

        return true;
    }

}

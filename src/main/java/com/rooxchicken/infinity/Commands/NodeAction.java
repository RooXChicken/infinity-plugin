package com.rooxchicken.infinity.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import com.rooxchicken.infinity.Infinity;
import com.rooxchicken.infinity.Library;
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

        logic(player, menu, Integer.parseInt(args[1]), Integer.parseInt(args[2]));
        return true;
    }
        

    public void logic(Player player, int menu, int node, int action)
    {
        player.getPersistentDataContainer().set(Infinity.nodeActionKey, PersistentDataType.STRING, menu + "_" + node + "_" + action);

        if(menu != 0 && action == 0 && !Library.canSpend(player))
        {
            int max = Library.getPointMax(player);
            player.sendMessage("§4You cannot spend more than " + max + " tokens!");
            return;
        }

        switch(menu)
        {
            case 0:
                switch(node)
                {
                    case 0: plugin.strength.sendNodes(player, true); break;
                    case 1: plugin.speed.sendNodes(player, true); break;
                    case 2: plugin.health.sendNodes(player, true); break;
                    case 3: plugin.luck.sendNodes(player, true); break;
                    case 4: plugin.stealth.sendNodes(player, true); break;
                }
            break;
            case 1:
                plugin.strength.findNode(player, node).action(player, action);
                plugin.strength.sendNodes(player, false);
            break;

            case 2:
                switch(node)
                {
                    case 0: plugin.speed.findNode(player, "28").action(player, action); break;
                    case 1: plugin.speed.findNode(player, "1").action(player, action); break;
                    case 2: plugin.speed.findNode(player, "20").action(player, action); break;
                    case 3: plugin.speed.findNode(player, "41").action(player, action); break;
                    case 4: plugin.speed.findNode(player, "42").action(player, action); break;
                    case 5: plugin.speed.findNode(player, "36").action(player, action); break;
                    case 6: plugin.speed.findNode(player, "4").action(player, action); break;
                    case 7: plugin.speed.findNode(player, "39").action(player, action); break;
                    case 8: plugin.speed.findNode(player, "19").action(player, action); break;
                }
                plugin.speed.sendNodes(player, false);
            break;

            case 4:
                plugin.luck.findNode(player, node).action(player, action);
                plugin.luck.sendNodes(player, false);
            break;

            case 5:
                plugin.stealth.findNode(player, node).action(player, action);
                plugin.stealth.sendNodes(player, false);
            break;
            case 6:
                plugin.health.findNode(player, node).action(player, action);
                plugin.health.sendNodes(player, false);
            break;
        }
    }

    public void unlearnRaw(Player player, int menu, int node)
    {
        //player.getPersistentDataContainer().set(Infinity.nodeActionKey, PersistentDataType.STRING, menu + "_" + node + "_" + action);
        switch(menu)
        {
            case 1:
                plugin.strength.unlearnNode(player, plugin.strength.findNodeRaw(node));
            break;

            case 2:
                plugin.speed.unlearnNode(player, plugin.speed.findNodeRaw(node));
            break;

            case 4:
                plugin.luck.unlearnNode(player, plugin.luck.findNodeRaw(node));
            break;

            case 5:
                plugin.stealth.unlearnNode(player, plugin.stealth.findNodeRaw(node));
            break;
            case 6:
                plugin.health.unlearnNode(player, plugin.health.findNodeRaw(node));
            break;
        }
    }

}

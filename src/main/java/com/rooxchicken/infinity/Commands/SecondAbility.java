package com.rooxchicken.infinity.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.rooxchicken.infinity.Infinity;
import com.rooxchicken.infinity.Abilities.Ability;

public class SecondAbility implements CommandExecutor
{
    private Infinity plugin;
    private int state = -1;

    public SecondAbility(Infinity _plugin, int _state)
    {
        plugin = _plugin;
        state = _state;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        // Ability ability = plugin.getPlayerAbility(Bukkit.getPlayer(sender.getName()));
        // if(ability != null)
        //     ability.activateSecondAbility(state);

        return true;
    }

}

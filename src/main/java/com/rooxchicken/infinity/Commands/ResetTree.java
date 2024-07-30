package com.rooxchicken.infinity.Commands;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

import com.rooxchicken.infinity.Infinity;
import com.rooxchicken.infinity.Library;
public class ResetTree implements CommandExecutor
{
    private Infinity plugin;

    public ResetTree(Infinity _plugin)
    {
        plugin = _plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        Player player = Bukkit.getPlayer(sender.getName());

        plugin.strength.reset(player);
        plugin.speed.reset(player);
        plugin.health.reset(player);
        plugin.luck.reset(player);
        plugin.stealth.reset(player);

        player.getPersistentDataContainer().set(Infinity.pointsKey, PersistentDataType.INTEGER, 0);
        
        player.sendMessage("§aYour tree has been reset!");
        player.playSound(player.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1, 1);

        return true;
    }

}

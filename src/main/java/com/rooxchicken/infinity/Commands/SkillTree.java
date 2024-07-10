package com.rooxchicken.infinity.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

import com.rooxchicken.infinity.Infinity;
import com.rooxchicken.infinity.Library;
public class SkillTree implements CommandExecutor
{
    private Infinity plugin;

    public SkillTree(Infinity _plugin)
    {
        plugin = _plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        Library.sendPlayerData(Bukkit.getPlayer(sender.getName()), "2_srt_Menu_0_1.0_1.0_1.0_false_3.0");
        //Library.sendPlayerData(Bukkit.getPlayer(sender.getName()), "2_menu/strength_0_0_Strength Class_true_false_0");
        Library.sendPlayerData(Bukkit.getPlayer(sender.getName()), "2_menu/strength_-42_0_Strength Class_true_false_0");
        Library.sendPlayerData(Bukkit.getPlayer(sender.getName()), "2_menu/speed_-21_0_Speed Class_true_false_1");
        Library.sendPlayerData(Bukkit.getPlayer(sender.getName()), "2_menu/health_0_0_Health Class_true_false_2");
        Library.sendPlayerData(Bukkit.getPlayer(sender.getName()), "2_menu/luck_21_0_Luck Class_true_false_3");
        Library.sendPlayerData(Bukkit.getPlayer(sender.getName()), "2_menu/stealth_42_0_Stealth Class_true_false_4");
        Library.sendPlayerData(Bukkit.getPlayer(sender.getName()), "3");
        
        return true;
    }

}

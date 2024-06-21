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
        Library.sendPlayerData(Bukkit.getPlayer(sender.getName()), "2_srt_Speed_0.4_0.8_1.0");
        Library.sendPlayerData(Bukkit.getPlayer(sender.getName()), "2_n_0_21_n_false_false");
        Library.sendPlayerData(Bukkit.getPlayer(sender.getName()), "2_icons/28_0_20_+10% generic speed_true_true");
        Library.sendPlayerData(Bukkit.getPlayer(sender.getName()), "2_n_0_-20_n_false_false");
        Library.sendPlayerData(Bukkit.getPlayer(sender.getName()), "2_lArrow_-20_-20_n_false_false");
        Library.sendPlayerData(Bukkit.getPlayer(sender.getName()), "2_icons/1_-40_-20_Double Jump (COOLDOWN: 30s)_true_false");
        Library.sendPlayerData(Bukkit.getPlayer(sender.getName()), "2_icons/20_-80_-20_Speed 2 Effect_true_false");
        Library.sendPlayerData(Bukkit.getPlayer(sender.getName()), "2_n_-40_-20_n_false_false");
        Library.sendPlayerData(Bukkit.getPlayer(sender.getName()), "2_icons/41_-40_-60_Dash (REPLACES DOUBLE JUMP)_true_false");
        Library.sendPlayerData(Bukkit.getPlayer(sender.getName()), "2_n_-40_-20_n_false_false");
        Library.sendPlayerData(Bukkit.getPlayer(sender.getName()), "2_rArrow_20_-20_n_false_false");
        Library.sendPlayerData(Bukkit.getPlayer(sender.getName()), "2_icons/42_40_-20_Speed 1 Effect_true_false");
        Library.sendPlayerData(Bukkit.getPlayer(sender.getName()), "3");
        
        return true;
    }

}

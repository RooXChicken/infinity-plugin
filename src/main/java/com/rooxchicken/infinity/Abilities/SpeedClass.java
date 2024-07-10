package com.rooxchicken.infinity.Abilities;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import com.rooxchicken.infinity.Infinity;

public class SpeedClass extends Ability implements Listener
{
    private Infinity plugin;
    public int type = -1;

    public SpeedClass(Infinity _plugin)
    {
        super(_plugin);


        name = "Speed";
        nodes = new ArrayList<String>();

        nodes.add("2_srt_Speed_2_0.4_0.8_1.0_true_1.0");
        nodes.add("2_n_0_21_n_false_false_-1");
        nodes.add("2_icons/28_0_20_+10% generic speed_true_false_0");
        nodes.add("2_n_0_-20_n_false_false_-1");
        nodes.add("2_lArrow_-20_-20_n_false_false_-1");
        nodes.add("2_icons/1_-40_-20_Double Jump (COOLDOWN: 30s)_true_false_1");
        nodes.add("2_icons/20_-80_-20_Speed 2 Effect_true_false_2");
        nodes.add("2_n_-40_-20_n_false_false_-1");
        nodes.add("2_icons/41_-40_-60_Dash (REPLACES DOUBLE JUMP)_true_false_3");
        nodes.add("2_n_-40_-20_n_false_false_-1");
        nodes.add("2_rArrow_20_-20_n_false_false_-1");
        nodes.add("2_icons/42_40_-20_Speed 1 Effect_true_false_4");
        nodes.add("3");
    }
}

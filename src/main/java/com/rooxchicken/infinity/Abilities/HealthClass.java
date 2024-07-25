package com.rooxchicken.infinity.Abilities;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import com.rooxchicken.infinity.Infinity;
import com.rooxchicken.infinity.Data.Node;

public class HealthClass extends Ability implements Listener
{
    private Infinity plugin;
    public int type = -1;

    public HealthClass(Infinity _plugin)
    {
        super(_plugin);


        name = "Health";
        //nodes = new ArrayList<Node>();

        header = "2_srt_Health_2_0.96_0.38_0.32_true_1.0";
        // nodes.add("2_n_0_21_n_false_false_-1");
        // nodes.add("2_icons/10_0_20_Gain 1 heart for each point spent in this tree_true_false_0");
        // nodes.add("2_n_0_-20_n_false_false_-1");
        // nodes.add("2_larrow_-20_-20_n_false_false_-1");
        // nodes.add("2_icons/1_-40_-20_Double Jump (COOLDOWN: 30s)_true_false_1");
        // nodes.add("2_icons/20_-80_-20_Speed 2 Effect_true_false_2");
        // nodes.add("2_n_-40_-20_n_false_false_-1");
        // nodes.add("2_icons/41_-40_-60_Dash (REPLACES DOUBLE JUMP)_true_false_3");
        // nodes.add("2_n_-40_-20_n_false_false_-1");
        // nodes.add("2_rarrow_20_-20_n_false_false_-1");
        // nodes.add("2_icons/42_40_-20_Speed 1 Effect_true_false_4");
        // nodes.add("3");
    }
}

package com.edasaki.rpg.buycraft;

import java.sql.PreparedStatement;

import com.edasaki.core.sql.SQLManager;
import com.edasaki.core.utils.RScheduler;
import com.edasaki.rpg.AbstractManagerRPG;
import com.edasaki.rpg.SakiRPG;

public class BuycraftManager extends AbstractManagerRPG {

    public BuycraftManager(SakiRPG plugin) {
        super(plugin);
    }

    @Override
    public void initialize() {

    }

    public static BuycraftReward getReward(String reward) {
        for (BuycraftReward br : BuycraftReward.values())
            if (reward.equalsIgnoreCase(br.toString()))
                return br;
        return null;
    }

    public static void removeReward(String name, String reward) {
        BuycraftReward br = getReward(reward);
        if (br == null) {
            try {
                throw new Exception("Attempt to remove invalid buycraft reward " + reward);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }
        removeReward(name, br);
    }

    public static void removeReward(String name, BuycraftReward br) {
        String temp = br.toString() + " ";
        RScheduler.scheduleAsync(plugin, () -> {
            String query = "UPDATE main set buycraft = IF(INSTR(buycraft, ?) <> 0, CONCAT(LEFT(buycraft, INSTR(buycraft, ?) - 1), '', SUBSTRING(buycraft FROM INSTR(buycraft, ?) + CHAR_LENGTH(?))), buycraft) where name = ?;";
            AutoCloseable[] ac_dub2 = SQLManager.prepare(query);
            PreparedStatement concat_reward = (PreparedStatement) ac_dub2[0];
            try {
                concat_reward.setString(1, temp);
                concat_reward.setString(2, temp);
                concat_reward.setString(3, temp);
                concat_reward.setString(4, temp);
                concat_reward.setString(5, name);
            } catch (Exception e) {
                e.printStackTrace();
            }
            SQLManager.execute(ac_dub2);
            SQLManager.close(ac_dub2);
            System.out.println("Removed reward " + br.toString() + " from " + name + ".");
        });
    }

    public static void giveReward(String name, String reward) {
        BuycraftReward br = getReward(reward);
        if (br == null) {
            try {
                throw new Exception("Attempt to give invalid buycraft reward " + reward);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }
        giveReward(name, br);
    }

    public static void giveReward(String name, BuycraftReward br) {
        RScheduler.scheduleAsync(plugin, () -> {
            String query = "update main set buycraft = concat(IF(ISNULL(buycraft), '', buycraft), ?, ' ') where name = ?;";
            AutoCloseable[] ac_dub2 = SQLManager.prepare(query);
            PreparedStatement concat_reward = (PreparedStatement) ac_dub2[0];
            try {
                concat_reward.setString(1, br.toString());
                concat_reward.setString(2, name);
            } catch (Exception e) {
                e.printStackTrace();
            }
            SQLManager.execute(ac_dub2);
            SQLManager.close(ac_dub2);
            System.out.println("Gave reward " + br.toString() + " to " + name + ".");
        });
    }

}

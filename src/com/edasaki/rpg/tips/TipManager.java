package com.edasaki.rpg.tips;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.edasaki.core.options.SakiOption;
import com.edasaki.core.utils.RScheduler;
import com.edasaki.core.utils.RTicks;
import com.edasaki.rpg.AbstractManagerRPG;
import com.edasaki.rpg.SakiRPG;

public class TipManager extends AbstractManagerRPG {

    private static final String PREFIX = ChatColor.GRAY + "[0] " + ChatColor.AQUA + ChatColor.BOLD + "Bot " + ChatColor.WHITE + "Rensa: " + ChatColor.GOLD;

    private static int last = -1;

    private static final String[] TIPS = {
            "Be sure to turn on particles in your Minecraft client!",
            "Most spells are displayed using particles. Make sure you have particles turned on!",
            "Vote every day with &e/vote&6 to earn awesome rewards!",
            "Buy awesome stuff from &e/rewards&6 with your Reward Points, earned by voting!",
            "Make sure to set up your spells with &e/spell&6!",
            "Be sure to check &e/help&6 for a list of all the commands!",
            "Check your quest progress with &e/quests&6 and your location with &e/loc&6.",
            "Party up! Team up with other players using &e/party&6 for bonus EXP.",
            "Danger Levels 3 and 4 allow PvP. Be careful in them!",
            "You will only lose items when you die in Danger Level 4.",
            "Shards are the main currency of Zentrela, so keep your shards safe!",
            "Hover over players' names in chat to see extra information. Click on their names for even more!",
            "Be sure to check &e/options&6 to see what information you can hide or show!",
            "I can provide convenient clickable links! Just say !help in chat for a list of chat commands!",
            "Stuck? Try using the &eJumper Trinket&6 from &e/trinket&6 to get out!",
            "Be sure to join the official Discord chat to chill with players and staff! Type &e!discord&6 for more information.",
            "Help support the server! store.zentrela.net has all kinds of cool stuff!",
            "Use &e!store&6 for a link to the official Zentrela store, where you can buy ranks and more!",
            "Check out the Zentrela plug.dj at https://plug.dj/zentrela!",
            "Zentrela is a new server, so we're still coming out with content! There will be lots more quests, shops, NPCs, mobs, builds, and dungeons in the future!",
            "Too few quests? Not enough dungeons? The Zentrela team is working hard to bring you more fun content!",
            "Want to know how much damage you're taking? Turn on Damage Messages in &e/options&6!",
            "Check out the Zentrela Wiki for all kinds of help! http://zentrela.wikia.com/",
            "Remember to vote every day to earn Reward Points! Type &e/vote&6 for a link to the voting page.",
            "Want some awesome rewards from &e/rewards&6? &e/Vote&6 every day to stock up on Reward Points!",
    };

    private static boolean scheduled = false;

    static {
        for (int k = 0; k < TIPS.length; k++)
            TIPS[k] = PREFIX + ChatColor.translateAlternateColorCodes('&', TIPS[k]);
    }

    public TipManager(SakiRPG plugin) {
        super(plugin);
    }

    public static void sayTip() {
        int rand = (int) (Math.random() * TIPS.length);
        for (int k = 0; k < 3; k++) {
            if (rand == last)
                rand = (int) (Math.random() * TIPS.length);
        }
        last = rand;
        for (Player p : plugin.getServer().getOnlinePlayers()) {
            if (plugin.getPD(p) != null) {
                if (plugin.getPD(p).getOption(SakiOption.SAKIBOT_TIPS))
                    p.sendMessage(TIPS[rand]);
            }
        }
    }

    public static void schedule() {
        if (scheduled)
            return;
        scheduled = true;
        RScheduler.schedule(plugin, new Runnable() {
            public void run() {
                scheduled = false;
                if (plugin.isEnabled()) {
                    sayTip();
                }
            }
        }, RTicks.seconds((int) (Math.random() * 60 + 30)));
    }

    @Override
    public void initialize() {
        schedule();
    }

}

package com.edasaki.rpg.spells.alchemist;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.edasaki.core.utils.RMath;
import com.edasaki.core.utils.RTicks;
import com.edasaki.rpg.PlayerDataRPG;
import com.edasaki.rpg.spells.Spell;
import com.edasaki.rpg.spells.SpellEffect;

public class MysteryDrink extends SpellEffect {

    public static final String DAMAGE_BUFF_ID = "mystery drink damage";
    public static final String DAMAGE_DEBUFF_ID = "mystery drink weak";
    public static final String REGEN_BUFF_ID = "mystery drink regen";

    private static final HashMap<String, Long> cooldown = new HashMap<String, Long>();

    @Override
    public boolean cast(final Player p, final PlayerDataRPG pd, int level) {
        if (cooldown.containsKey(p.getName())) {
            if (System.currentTimeMillis() < cooldown.get(p.getName())) {
                p.sendMessage(ChatColor.RED + "You are still under the effects of a Mystery Drink!");
                return false;
            }
        }
        int duration = 3;
        switch (level) {
            case 1:
                duration = 3;
                break;
            case 2:
                duration = 6;
                break;
            case 3:
                duration = 9;
                break;
        }
        cooldown.put(p.getName(), System.currentTimeMillis() + (duration * 1000));
        Spell.notify(p, "You drink a mysterious elixir.");
        int rand = (int) (Math.random() * 4);
        ArrayList<Player> toGive = new ArrayList<Player>();
        if(pd.party != null) {
            for(Player p2 : pd.party.getPlayers()){
                if(p2 != null && p2.isOnline() && RMath.flatDistance(p2.getLocation(), p.getLocation()) < 10)
                    toGive.add(p2);
            }
        }
        for (Player p2 : toGive) {
            PlayerDataRPG pd2 = Spell.plugin.getPD(p2);
            if (pd2 == null)
                continue;
            if (pd2 != pd)
                Spell.notify(p2, ChatColor.GRAY + "> " + ChatColor.GREEN + "You feel the effects of " + pd.getName() + "'s Mystery Drink!");
            switch (rand) {
                case 0:
                    p2.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, RTicks.seconds(duration), 2));
                    Spell.notify(p2, "You feel much faster.");
                    Spell.notifyDelayed(p2, "Your speed wears off...", duration);
                    break;
                case 1:
                    p2.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, RTicks.seconds(duration), 1));
                    Spell.notify(p2, "You feel you can jump higher.");
                    Spell.notifyDelayed(p2, "Your better jumping wears off...", duration);
                    break;
                case 2:
                    pd2.giveBuff(DAMAGE_BUFF_ID, 1.5, duration * 1000);
                    Spell.notify(p2, "You feel stronger. You now do 50% more damage!");
                    Spell.notifyDelayed(p2, "Your strength wears off...", duration);
                    break;
                case 3:
                    pd2.giveBuff(REGEN_BUFF_ID, 0.01, duration * 1000);
                    Spell.notify(p2, "You feel rejuvenated, and begin regenerating HP faster.");
                    Spell.notifyDelayed(p2, "Your regeneration wears off...", duration);
                    break;
            }
        }
        toGive.clear();
        return true;
    }
}

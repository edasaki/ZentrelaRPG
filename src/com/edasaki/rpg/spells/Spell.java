package com.edasaki.rpg.spells;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.IntToDoubleFunction;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.edasaki.core.options.SakiOption;
import com.edasaki.core.utils.RMath;
import com.edasaki.core.utils.RScheduler;
import com.edasaki.core.utils.RTicks;
import com.edasaki.rpg.PlayerDataRPG;
import com.edasaki.rpg.SakiRPG;
import com.edasaki.rpg.combat.DamageType;
import com.edasaki.rpg.commands.owner.ManaCommand;
import com.edasaki.rpg.mobs.MobData;
import com.edasaki.rpg.mobs.MobManager;
import com.edasaki.rpg.parties.PartyManager;

public class Spell {
    public static SakiRPG plugin = null;
    public static final long LONG_DURATION = Integer.MAX_VALUE;

    public int manaCost = 2; //manaCost of 0 == passive
    public int maxLevel = 0;
    public int row, col;
    public String name = "TEMP_NAME";
    private String[] descriptions = null;
    private String descriptionFormat = "Error Code 113 - spell description.";
    private IntToDoubleFunction[] functions;
    public HashMap<Spell, Integer> requirements;

    private SpellEffect spellEffect;

    public static void notify(Player p, String s) {
        if (plugin.getPD(p) != null && plugin.getPD(p).getOption(SakiOption.SPELL_MESSAGES))
            p.sendMessage(ChatColor.GRAY + ">> " + ChatColor.ITALIC + s);
    }

    public static void notifyDelayed(final Player p, final String s, double delaySeconds) {
        if (plugin.getPD(p) != null && plugin.getPD(p).getOption(SakiOption.SPELL_MESSAGES))
            RScheduler.schedule(plugin, new Runnable() {
                public void run() {
                    p.sendMessage(ChatColor.GRAY + ">> " + ChatColor.ITALIC + s);
                }
            }, RTicks.seconds(delaySeconds));
    }

    public static ArrayList<Entity> damageNearby(int damage, Entity damager, Location loc, double radius, ArrayList<Entity> ignore) {
        return damageNearby(damage, damager, loc, radius, ignore, true, false, false);
    }

    public static ArrayList<Entity> damageNearby(int damage, Entity damager, Location loc, double radius, ArrayList<Entity> ignore, boolean isSpell, boolean crit, boolean playersOnly) {
        ArrayList<Entity> hit = new ArrayList<Entity>();
        if (damager == null || !damager.isValid())
            return hit;
        for (Entity e : RMath.getNearbyEntities(loc, radius)) {
            if (playersOnly && !(e instanceof Player))
                continue;
            if ((ignore != null && ignore.contains(e)) || e == damager)
                continue;
            if (damageEntity(e, damage, damager, isSpell, crit))
                hit.add(e);
        }
        return hit;
    }

    public static boolean damageEntity(Entity e, int damage, Entity damager, boolean isSpell, boolean crit) {
        if (e == null || !e.isValid())
            return false;
        if (!((damager instanceof Player) || (e instanceof Player))) // Player must be involved
            return false;
        if (e instanceof Player) {
            Player p2 = (Player) e;
            if (damager instanceof Player && PartyManager.sameParty(p2, (Player) damager))
                return false;
            if (Spell.plugin.getPD(p2) != null) {
                if (!Spell.plugin.getPD(p2).damage(damage, damager, isSpell ? DamageType.NORMAL_SPELL : DamageType.NORMAL, crit))
                    return false;
                if (crit)
                    Spell.plugin.getPD(p2).playCrit();
            }
            return true;
        } else if (MobManager.spawnedMobs_onlyMain.containsKey(e.getUniqueId())) {
            MobData md = MobManager.spawnedMobs_onlyMain.get(e.getUniqueId());
            if (!md.damage(damage, damager, isSpell ? DamageType.NORMAL_SPELL : DamageType.NORMAL, crit))
                return false;
            if (crit)
                md.playCrit();
            return true;
        }
        return false;
    }

    public static boolean canDamage(Entity e, boolean pvp) {
        if (e instanceof Player) {
            Player p2 = (Player) e;
            PlayerDataRPG pd2 = Spell.plugin.getPD(p2);
            if (pd2 != null && (pvp ? pd2.isPVP() && !pd2.isStealthed() : pd2.isPVE()))
                return true;
        } else if (MobManager.spawnedMobs_onlyMain.containsKey(e.getUniqueId())) {
            return true;
        }
        return false;
    }

    public static void knockbackEntity(Entity e, Entity attacker, double multiplier) {
        if (e instanceof Player && Spell.plugin.getPD((Player) e) != null) {
            Spell.plugin.getPD((Player) e).knockback(attacker, multiplier);
        } else if (MobManager.spawnedMobs_onlyMain.containsKey(e.getUniqueId())) {
            MobManager.spawnedMobs_onlyMain.get(e.getUniqueId()).knockback(attacker, multiplier);
        }
    }

    public String getDescription(int level) {
        if (descriptions != null) {
            return descriptions[level - 1];
        } else {
            try {
                Object[] arr = new Object[functions.length];
                for (int k = 0; k < functions.length; k++)
                    arr[k] = functions[k].applyAsDouble(level);
                return String.format(descriptionFormat, arr);
            } catch (Exception e) {
                e.printStackTrace();
                return descriptionFormat;
            }
        }
    }

    public Spell(String name, int manaCost, int maxLevel, int row, int col, String[] descriptions, Object[] requirements, SpellEffect spellEffect) {
        this.name = name;
        this.manaCost = manaCost;
        this.maxLevel = maxLevel;
        this.row = row;
        this.col = col;
        this.descriptions = descriptions;
        this.requirements = new HashMap<Spell, Integer>();
        if (requirements != null) {
            for (int k = 0; k < requirements.length; k += 2) {
                this.requirements.put((Spell) requirements[k], (int) requirements[k + 1]);
            }
        }
        this.spellEffect = spellEffect;
    }

    public Spell(String name, int manaCost, int maxLevel, int row, int col, String descriptionFormat, IntToDoubleFunction[] functions, Object[] requirements, SpellEffect spellEffect) {
        this.name = name;
        this.manaCost = manaCost;
        this.maxLevel = maxLevel;
        this.row = row;
        this.col = col;
        this.descriptionFormat = descriptionFormat;
        this.functions = functions;
        this.requirements = new HashMap<Spell, Integer>();
        if (requirements != null) {
            for (int k = 0; k < requirements.length; k += 2) {
                this.requirements.put((Spell) requirements[k], (int) requirements[k + 1]);
            }
        }
        this.spellEffect = spellEffect;
        this.spellEffect.functions = functions;
    }

    @Override
    public String toString() {
        return this.name;
    }

    public boolean isPassive() {
        return manaCost == 0;
    }

    public void cast(Player p, PlayerDataRPG pd) {
        ItemStack item = p.getEquipment().getItemInMainHand();
        if (item == null || !SpellManager.validateWeapon(pd, pd.classType, item)) {
            return;
        } else {
            if (pd.mana < manaCost) {
                p.sendMessage(ChatColor.RED + "You don't have enough mana for that!" + ChatColor.DARK_AQUA + " [Need " + manaCost + "]");
            } else {
                boolean res = true;
                try {
                    res = spellEffect.cast(p, pd, pd.getSpellLevel(this));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (res) {
                    if (pd.getOption(SakiOption.MANA_MESSAGES))
                        p.sendMessage(ChatColor.DARK_AQUA + ">> -" + manaCost + " Mana [" + name + "]");
                    if (!ManaCommand.infMana.contains(p.getName()))
                        pd.mana -= manaCost;
                }
            }
            pd.updateHealthManaDisplay();
        }
    }
}

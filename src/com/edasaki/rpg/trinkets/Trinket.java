package com.edasaki.rpg.trinkets;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.bukkit.util.Vector;

import com.edasaki.core.utils.RFormatter;
import com.edasaki.core.utils.RParticles;
import com.edasaki.core.utils.RTicks;
import com.edasaki.rpg.PlayerDataRPG;
import com.edasaki.rpg.spells.Spell;

import de.slikey.effectlib.util.ParticleEffect;

public enum Trinket {

    RECOVERY("Recovery", PotionType.JUMP, new TrinketStat[] { TrinketStat.HP_MEDIUM, TrinketStat.DEFENSE_SMALL }, new TrinketSpell() {

        @Override
        public String getName() {
            return "Quick Heal";
        }

        @Override
        public String getDescription() {
            return "Heal for 25% of your Max HP.";
        }

        @Override
        public int getCooldown() {
            return 30;
        }

        @Override
        public boolean cast(Player p, PlayerDataRPG pd) {
            pd.heal((int) Math.ceil(0.25 * (pd.getCurrentMaxHP())));
            return true;
        }

    }),

    MANIC("Manic", PotionType.INSTANT_DAMAGE, new TrinketStat[] { TrinketStat.HP_NEGATIVE, TrinketStat.DAMAGE_MULTIPLIER_LARGE }, new TrinketSpell() {

        @Override
        public String getName() {
            return "Wild Swing";
        }

        @Override
        public String getDescription() {
            return "Lash out at nearby enemies, dealing more damage when you are at low HP.";
        }

        @Override
        public int getCooldown() {
            return 15;
        }

        @Override
        public boolean cast(Player p, PlayerDataRPG pd) {
            RParticles.show(ParticleEffect.EXPLOSION_LARGE, p.getLocation(), 5);
            //            int damage = pd.getCurrentMaxHP() - pd.hp;
            double percent = ((double) pd.hp) / pd.getCurrentMaxHP();
            double mult = 1.5;
            if (percent < 0.1)
                mult = 8;
            else if (percent < 0.25)
                mult = 5;
            else if (percent < 0.5)
                mult = 3;
            Spell.damageNearby((int) Math.ceil(mult * pd.getDamage(true)), p, p.getLocation(), 3, new ArrayList<Entity>());
            return true;
        }

    }),

    HAWKEYE("Hawkeye", PotionType.POISON, new TrinketStat[] { TrinketStat.CRIT_CHANCE_LARGE, TrinketStat.DAMAGE_MULTIPLIER_SMALL }, new TrinketSpell() {

        @Override
        public String getName() {
            return "Hyperfocus";
        }

        @Override
        public String getDescription() {
            return "Gain +20% Crit Chance for 10 seconds.";
        }

        @Override
        public int getCooldown() {
            return 15;
        }

        @Override
        public boolean cast(Player p, final PlayerDataRPG pd) {
            pd.giveBuff(HAWKEYE_BUFF_ID, 1, 10000, "You feel Hyperfocus wearing off.");
            return true;
        }

    }),

    GUARDIAN("Guardian", PotionType.NIGHT_VISION, new TrinketStat[] { TrinketStat.HP_SMALL, TrinketStat.DEFENSE_LARGE }, new TrinketSpell() {

        @Override
        public String getName() {
            return "Protect";
        }

        @Override
        public String getDescription() {
            return "All party members gain 20% damage reduction for 10 seconds.";
        }

        @Override
        public int getCooldown() {
            return 60;
        }

        @Override
        public boolean cast(Player p, final PlayerDataRPG pd) {
            double val = 0.8;
            if (pd.party == null) {
                pd.giveBuff(GUARDIAN_BUFF_ID, val, 10000, "You feel Protect wearing off.");
            } else {
                pd.party.sendMessage(p.getName() + " casted the Trinket Spell Protect!");
                for (Player p2 : pd.party.getPlayers()) {
                    if (p2 != null && TrinketManager.plugin.getPD(p2) != null) {
                        TrinketManager.plugin.getPD(p2).giveBuff(GUARDIAN_BUFF_ID, val, 10000, "You feel Protect wearing off.");
                    }
                }
            }
            return true;
        }

    }),

    JUMPER("Jumper", PotionType.SLOWNESS, new TrinketStat[] { TrinketStat.HP_SMALL }, new TrinketSpell() {

        @Override
        public String getName() {
            return "Jump";
        }

        @Override
        public String getDescription() {
            return "Jump forward! A great leg workout.";
        }

        @Override
        public int getCooldown() {
            return 7;
        }

        @Override
        public boolean cast(Player p, final PlayerDataRPG pd) {
            p.setVelocity(p.getEyeLocation().getDirection().normalize().multiply(0.8).setY(0).add(new Vector(0, 0.8, 0)));
            return true;
        }

    }),

    RUNNER("Runner", PotionType.SPEED, new TrinketStat[] { TrinketStat.SPEED_LARGE }, new TrinketSpell() {

        @Override
        public String getName() {
            return "Unstoppable";
        }

        @Override
        public String getDescription() {
            return "Gain a large speed buff for 10 seconds.";
        }

        @Override
        public int getCooldown() {
            return 60;
        }

        @Override
        public boolean cast(Player p, final PlayerDataRPG pd) {
            p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, RTicks.seconds(10), 2), false);
            return true;
        }

    }),

    ;

    public static final String HAWKEYE_BUFF_ID = "hawkeye trinket buff";
    public static final String GUARDIAN_BUFF_ID = "guardian trinket buff";

    public static Trinket getTrinket(String s) {
        if (s == null)
            return null;
        for (Trinket t : values()) {
            if (t.name.equalsIgnoreCase(s) || t.toString().equalsIgnoreCase(s))
                return t;
        }
        return null;
    }

    private static final long[] EXP_FOR_LEVEL = {
            0, // 1
            2000, // 2
            12000, // to reach level 3
            92000, // 4
            792000, // 5
            1992000, // 6
            4392000, // 7
            7892000, // 8
            12192000, // 9
            17192000, // 10
    };

    public static int getTrinketLevel(long exp) {
        int max = 1;
        for (int k = 0; k < EXP_FOR_LEVEL.length; k++)
            if (exp > EXP_FOR_LEVEL[k])
                max = k + 1;
        return max;
    }

    public String name;
    public PotionType potionType;
    public TrinketStat[] stats;
    public TrinketSpell spell;

    public ArrayList<String> getLore(PlayerDataRPG pd) {
        long exp = pd.getTrinketExp(this);
        int level = getTrinketLevel(exp);
        ArrayList<String> lore = new ArrayList<String>();
        lore.add("");
        for (TrinketStat ts : stats) {
            lore.add(ts.getFormat(level));
        }
        lore.add("");
        lore.add(ChatColor.GOLD + "Trinket Spell " + ChatColor.GRAY + "- " + ChatColor.YELLOW + spell.getName());
        lore.addAll(RFormatter.stringToLore(spell.getDescription(), ChatColor.AQUA));
        lore.add(ChatColor.LIGHT_PURPLE + "Cooldown: " + ChatColor.WHITE + spell.getCooldown() + "s");
        lore.add("");
        lore.add(ChatColor.LIGHT_PURPLE + "Trinket Level: " + ChatColor.WHITE + level);
        if (level < 10)
            lore.add(ChatColor.LIGHT_PURPLE + "Trinket EXP: " + ChatColor.WHITE + exp + "/" + EXP_FOR_LEVEL[level]);
        lore.add("");
        lore.add(ChatColor.GRAY + "Press F [Swap Items] to ");
        lore.add(ChatColor.GRAY + "cast your Trinket Spell.");
        return lore;
    }

    public ItemStack createItem(PlayerDataRPG pd, ArrayList<String> lore) {
        ItemStack item = new ItemStack(Material.TIPPED_ARROW);
        PotionMeta pm = (PotionMeta) item.getItemMeta();
        pm.setBasePotionData(new PotionData(potionType));
        if (pd.trinket != null && pd.trinket == this)
            pm.setDisplayName(ChatColor.GREEN + name + " Trinket");
        else
            pm.setDisplayName(ChatColor.DARK_AQUA + name + " Trinket");
        pm.setLore(lore);
        pm.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_POTION_EFFECTS);
        item.setItemMeta(pm);
        return item;
    }

    public void updateTrinket(PlayerDataRPG pd, ItemStack item) {
        if (pd == null)
            return;
        if (!item.hasItemMeta())
            return;
        ItemMeta im = item.getItemMeta();
        ArrayList<String> lore = getLore(pd);
        if (im == null || lore == null)
            return;
        im.setLore(lore);
        item.setItemMeta(im);
    }

    public ItemStack getEquipItem(PlayerDataRPG pd) {
        return createItem(pd, getLore(pd));
    }

    public ItemStack getDisplayItem(PlayerDataRPG pd) {
        ArrayList<String> lore = getLore(pd);
        if (pd.trinket != null && pd.trinket == this)
            lore.add(0, ChatColor.GRAY + "You currently have this trinket equipped.");
        else
            lore.add(0, ChatColor.GRAY + "Click to equip this trinket!");
        return createItem(pd, lore);
    }

    Trinket(String name, PotionType potionType, TrinketStat[] stats, TrinketSpell spell) {
        this.name = name;
        this.potionType = potionType;
        this.stats = stats;
        this.spell = spell;
    }

}

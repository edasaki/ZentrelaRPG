package com.edasaki.rpg.spells;

import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerAnimationType;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;

import com.edasaki.core.utils.RMessages;
import com.edasaki.core.utils.RScheduler;
import com.edasaki.core.utils.RTicks;
import com.edasaki.rpg.AbstractManagerRPG;
import com.edasaki.rpg.PlayerDataRPG;
import com.edasaki.rpg.SakiRPG;
import com.edasaki.rpg.classes.ClassType;
import com.edasaki.rpg.items.EquipType;

public class SpellManager extends AbstractManagerRPG {

    public static HashMap<UUID, Caster> casters = new HashMap<UUID, Caster>();
    private static HashMap<UUID, Long> lastPress = new HashMap<UUID, Long>();

    public static boolean isCasting(Player p) {
        if (!casters.containsKey(p.getUniqueId()))
            return false;
        Caster caster = casters.get(p.getUniqueId());
        return caster.state != CastState.NONE;
    }

    public SpellManager(SakiRPG plugin) {
        super(plugin);
    }

    @Override
    public void initialize() {
        Spell.plugin = plugin;
    }

    public enum CastState {
        NONE(""), R("Right"), RL("Right-Left"), RLR("Right-Left-Right"), RLL("Right-Left-Left"), RR("Right-Right"), RRL("Right-Right-Left"), RRR("Right-Right-Right");
        public String string;

        @Override
        public String toString() {
            return string;
        }

        CastState(String s) {
            this.string = s;
        }
    };

    public static class Caster {
        public CastState state = CastState.NONE;
        public int pressCount = 0;

        public void press(boolean left) {
            pressCount++;
            final int lastPressCount = pressCount;
            RScheduler.schedule(plugin, new Runnable() {
                public void run() {
                    if (lastPressCount != pressCount)
                        return;
                    clear();
                }
            }, RTicks.seconds(1));
            switch (state) {
                case NONE:
                    if (!left)
                        state = CastState.R;
                    break;
                case R:
                    if (left)
                        state = CastState.RL;
                    else
                        state = CastState.RR;
                    break;
                case RL:
                    if (left)
                        state = CastState.RLL;
                    else
                        state = CastState.RLR;
                    break;
                case RR:
                    if (left)
                        state = CastState.RRL;
                    else
                        state = CastState.RRR;
                    break;
                case RLL:
                case RLR:
                case RRL:
                case RRR:
                    state = CastState.NONE;
                    break;
                default:
                    break;
            }
        }

        public void clear() {
            state = CastState.NONE;
        }
    }

    @EventHandler
    public void onSneakEvent(PlayerToggleSneakEvent event) {
        Player p = event.getPlayer();
        if (!casters.containsKey(p.getUniqueId()))
            casters.put(p.getUniqueId(), new Caster());
        final Caster c = casters.get(p.getUniqueId());
        PlayerDataRPG pd = plugin.getPD(p);
        if (pd != null)
            pd.updateHealthManaDisplay();
        c.clear();
    }

    public static boolean isSpellWeapon(ItemStack item) {
        if (item == null || item.getType() == null)
            return false;
        return EquipType.isWeapon(item); // every weapon is a spell weapon :^)
    }

    public static boolean validateWeapon(PlayerDataRPG pd, ClassType classType, ItemStack item) {
        if (item == null || classType == null)
            return false;
        boolean rightWep = false;
        String className = null, weapon = null;
        switch (classType) {
            case VILLAGER:
                rightWep = true;
                break;
            case CRUSADER:
                if (EquipType.SWORD.isType(item))
                    rightWep = true;
                className = "a Crusader";
                weapon = "a Sword";
                break;
            case PALADIN:
                if (EquipType.MACE.isType(item))
                    rightWep = true;
                className = "a Paladin";
                weapon = "a Mace";
                break;
            case ASSASSIN:
                if (EquipType.DAGGER.isType(item))
                    rightWep = true;
                className = "an Assassin";
                weapon = "a Dagger";
                break;
            case ALCHEMIST:
                if (EquipType.ELIXIR.isType(item))
                    rightWep = true;
                className = "an Alchemist";
                weapon = "an Elixir";
                break;
            case REAPER:
                if (EquipType.SCYTHE.isType(item))
                    rightWep = true;
                className = "a Reaper";
                weapon = "a Scythe";
                break;
            case ARCHER:
                if (EquipType.BOW.isType(item))
                    rightWep = true;
                className = "an Archer";
                weapon = "a Bow";
                break;
            case WIZARD:
                if (EquipType.WAND.isType(item))
                    rightWep = true;
                className = "a Wizard";
                weapon = "a Wand";
                break;
        }
        if (!rightWep) {
            pd.sendMessage(ChatColor.GRAY + "> " + ChatColor.RED + "You are " + className + " and can only cast spells with " + weapon + "!");
        }
        return rightWep;
    }

    private void click(Player p, boolean left) {
        if (p.isGliding())
            return;
        if (!casters.containsKey(p.getUniqueId()))
            casters.put(p.getUniqueId(), new Caster());
        PlayerDataRPG pd = plugin.getPD(p);
        ItemStack item = p.getEquipment().getItemInMainHand();
        boolean spellWep = false;
        if (item != null)
            spellWep = isSpellWeapon(item);
        if (pd != null) {
            if (lastPress.containsKey(p.getUniqueId())) {
                if (System.currentTimeMillis() - lastPress.get(p.getUniqueId()) < 50)
                    return;
            }
            lastPress.put(p.getUniqueId(), System.currentTimeMillis());
            final Caster c = casters.get(p.getUniqueId());
            if (spellWep) {
                c.press(left);
            } else {
                c.clear();
            }
            checkState(p, pd, c);
        }
    }

    @EventHandler
    public void onSpellClicksonDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            Player p = (Player) event.getDamager();
            click(p, true); // damage is always left
        }
    }

    @EventHandler
    public void onSpellClicksOnEntity(PlayerInteractEntityEvent event) {
        Player p = event.getPlayer();
        click(p, false); // interact is always right
    }

    @EventHandler
    public void event(PlayerAnimationEvent event) {
        Block focused = event.getPlayer().getTargetBlock((Set<Material>) null, 5);
        if (event.getAnimationType() == PlayerAnimationType.ARM_SWING && focused.getType() != Material.AIR && event.getPlayer().getGameMode() == GameMode.ADVENTURE) {
            click(event.getPlayer(), true); // anim is left swing
        }
    }

    @EventHandler
    public void onSpellClicks(PlayerInteractEvent event) {
        Player p = event.getPlayer();
        if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK)
            click(p, true);
        else if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)
            click(p, false);
    }

    public void checkState(Player p, PlayerDataRPG pd, Caster c) {
        pd.updateHealthManaDisplay();
        ItemStack item = p.getEquipment().getItemInMainHand();
        if (c.state == CastState.RLL) {
            if (item == null || !validateWeapon(pd, pd.classType, item))
                return;
            if (pd.spell_RLL != null) {
                pd.spell_RLL.cast(p, pd);
            } else {
                p.sendMessage(ChatColor.RED + "You don't have any spell bound to the " + ChatColor.GOLD + "RLL" + ChatColor.RED + " spellcast!");
                p.sendMessage(ChatColor.RED + "You can bind a spell to the " + ChatColor.GOLD + "RLL" + ChatColor.RED + " spellcast using " + ChatColor.YELLOW + "/spell" + ChatColor.RED + "!");
            }
            RScheduler.schedule(plugin, new Runnable() {
                public void run() {
                    if (c.state == CastState.RLL) {
                        c.clear();
                        RMessages.sendActionBar(p, ChatColor.RESET + "");
                    }
                }
            }, RTicks.seconds(0.2));
        } else if (c.state == CastState.RLR) {
            if (item == null || !validateWeapon(pd, pd.classType, item))
                return;
            if (pd.spell_RLR != null) {
                pd.spell_RLR.cast(p, pd);
            } else {
                p.sendMessage(ChatColor.RED + "You don't have any spell bound to the " + ChatColor.GOLD + "RLR" + ChatColor.RED + " spellcast!");
                p.sendMessage(ChatColor.RED + "You can bind a spell to the " + ChatColor.GOLD + "RLR" + ChatColor.RED + " spellcast using " + ChatColor.YELLOW + "/spell" + ChatColor.RED + "!");
            }
            RScheduler.schedule(plugin, new Runnable() {
                public void run() {
                    if (c.state == CastState.RLR) {
                        c.clear();
                        RMessages.sendActionBar(p, ChatColor.RESET + "");
                    }
                }
            }, RTicks.seconds(0.2));
        } else if (c.state == CastState.RRL) {
            if (item == null || !validateWeapon(pd, pd.classType, item))
                return;
            if (pd.spell_RRL != null) {
                pd.spell_RRL.cast(p, pd);
            } else {
                p.sendMessage(ChatColor.RED + "You don't have any spell bound to the " + ChatColor.GOLD + "RRL" + ChatColor.RED + " spellcast!");
                p.sendMessage(ChatColor.RED + "You can bind a spell to the " + ChatColor.GOLD + "RRL" + ChatColor.RED + " spellcast using " + ChatColor.YELLOW + "/spell" + ChatColor.RED + "!");
            }
            RScheduler.schedule(plugin, new Runnable() {
                public void run() {
                    if (c.state == CastState.RRL) {
                        c.clear();
                        RMessages.sendActionBar(p, ChatColor.RESET + "");
                    }
                }
            }, RTicks.seconds(0.2));
        } else if (c.state == CastState.RRR) {
            if (item == null || !validateWeapon(pd, pd.classType, item))
                return;
            if (pd.spell_RRR != null) {
                pd.spell_RRR.cast(p, pd);
            } else {
                p.sendMessage(ChatColor.RED + "You don't have any spell bound to the " + ChatColor.GOLD + "RRR" + ChatColor.RED + " spellcast!");
                p.sendMessage(ChatColor.RED + "You can bind a spell to the " + ChatColor.GOLD + "RRR" + ChatColor.RED + " spellcast using " + ChatColor.YELLOW + "/spell" + ChatColor.RED + "!");
            }
            RScheduler.schedule(plugin, new Runnable() {
                public void run() {
                    if (c.state == CastState.RRR) {
                        c.clear();
                        RMessages.sendActionBar(p, ChatColor.RESET + "");
                    }
                }
            }, RTicks.seconds(0.2));
        }
        pd.updateHealthManaDisplay();
    }
}

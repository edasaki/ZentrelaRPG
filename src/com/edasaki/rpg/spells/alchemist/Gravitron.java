package com.edasaki.rpg.spells.alchemist;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

import com.edasaki.core.utils.RMath;
import com.edasaki.core.utils.RMetadata;
import com.edasaki.core.utils.RParticles;
import com.edasaki.core.utils.RScheduler;
import com.edasaki.rpg.PlayerDataRPG;
import com.edasaki.rpg.drops.DropManager;
import com.edasaki.rpg.mobs.MobManager;
import com.edasaki.rpg.parties.PartyManager;
import com.edasaki.rpg.spells.Spell;
import com.edasaki.rpg.spells.SpellEffect;

import de.slikey.effectlib.util.ParticleEffect;

public class Gravitron extends SpellEffect {

    @Override
    public boolean cast(final Player p, final PlayerDataRPG pd, int level) {
        Location loc = p.getLocation().add(0, p.getEyeHeight() * 0.8, 0);
        loc.add(p.getLocation().getDirection().normalize().multiply(0.4));
        final Item item = p.getWorld().dropItem(loc, new ItemStack(Material.ANVIL));
        item.setMetadata(RMetadata.META_NO_PICKUP, new FixedMetadataValue(Spell.plugin, 0));
        //        ItemManager.attachLabel(item, ChatColor.BOLD + "= " + p.getName() + "'s Gravitron =");
        Spell.plugin.getInstance(DropManager.class).attachLabel(item, ChatColor.DARK_GRAY.toString() + ChatColor.ITALIC + p.getName());
        Vector dir = p.getLocation().getDirection().normalize();
        dir.setY(dir.getY() * 1.1);
        dir.multiply(0.6);
        item.setVelocity(dir);
        int duration = (int) functions[0].applyAsDouble(level);
        final int fDuration = duration;
        for (int k = 1; k <= duration; k++) {
            final int kVal = k;
            RScheduler.schedule(Spell.plugin, new Runnable() {
                public void run() {
                    if (item == null || !item.isValid())
                        return;
                    ArrayList<Vector> vectors = new ArrayList<Vector>();
                    Vector v = item.getLocation().getDirection().normalize();
                    v.setY(0);
                    vectors.add(v);
                    double z = v.getZ();
                    double x = v.getX();
                    double radians = Math.atan(z / x);
                    if (x < 0)
                        radians += Math.PI;
                    for (int k = 1; k < 24; k++) {
                        Vector v2 = new Vector();
                        v2.setY(v.getY());
                        v2.setX(Math.cos(radians + k * Math.PI / 12));
                        v2.setZ(Math.sin(radians + k * Math.PI / 12));
                        vectors.add(v2.normalize());
                    }
                    for (Vector vec : vectors) {
                        Location loc = item.getLocation().clone().add(vec.multiply(4)).add(0, 0.1, 0);
                        RParticles.showWithSpeed(ParticleEffect.SPELL_MOB, loc, 3, 1);
                    }
                    for (Entity e : RMath.getNearbyEntitiesCylinder(item.getLocation(), 4, 9)) {
                        boolean pull = false;
                        if (e instanceof Player && e != p) {
                            Player p2 = (Player) e;
                            if (Spell.plugin.getPD(p2) != null && Spell.plugin.getPD(p2).isPVP() && !PartyManager.sameParty(p, p2))
                                pull = true;
                        } else if (MobManager.spawnedMobs_onlyMain.containsKey(e.getUniqueId())) {
                            pull = true;
                        }
                        if (pull) {
                            Vector pullVector = e.getLocation().toVector().subtract(item.getLocation().toVector()).multiply(-0.5);
                            pullVector.setY(0);
                            e.setVelocity(pullVector);
                        }
                    }
                    if (kVal == fDuration) {
                        DropManager.removeLabel(item);
                        item.remove();
                    }
                }
            }, k * 20);
        }
        Spell.notify(p, "You throw your trusty Gravitron.");
        return true;
    }
}

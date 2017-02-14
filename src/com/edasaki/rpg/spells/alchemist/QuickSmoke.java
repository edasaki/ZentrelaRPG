package com.edasaki.rpg.spells.alchemist;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

import com.edasaki.core.utils.RMetadata;
import com.edasaki.core.utils.RParticles;
import com.edasaki.core.utils.RScheduler;
import com.edasaki.rpg.PlayerDataRPG;
import com.edasaki.rpg.drops.DropManager;
import com.edasaki.rpg.spells.Spell;
import com.edasaki.rpg.spells.SpellEffect;

import de.slikey.effectlib.util.ParticleEffect;

public class QuickSmoke extends SpellEffect {

    @Override
    public boolean cast(final Player p, final PlayerDataRPG pd, int level) {
        Location loc = p.getLocation().add(0, p.getEyeHeight() * 0.8, 0);
        loc.add(p.getLocation().getDirection().normalize().multiply(0.4));
        final Item item = p.getWorld().dropItem(loc, new ItemStack(Material.FURNACE));
        item.setMetadata(RMetadata.META_NO_PICKUP, new FixedMetadataValue(Spell.plugin, 0));
        //        ItemManager.attachLabel(item, ChatColor.BOLD + "= " + p.getName() + "'s Smoke =");
        Spell.plugin.getInstance(DropManager.class).attachLabel(item, ChatColor.DARK_GRAY.toString() + ChatColor.ITALIC + p.getName());
        Vector dir = p.getLocation().getDirection().normalize();
        dir.setY(dir.getY() * 1.1);
        dir.multiply(0.6);
        item.setVelocity(dir);
        int duration = 1;
        switch (level) {
            case 1:
                duration = 3;
                break;
            case 2:
                duration = 5;
                break;
            case 3:
                duration = 7;
                break;
        }
        final int fDuration = duration * 2;
        for (int k = 1; k <= fDuration; k++) {
            final int kVal = k;
            RScheduler.schedule(Spell.plugin, new Runnable() {
                static final int RANGE = 10;

                public void run() {
                    if (item == null || !item.isValid())
                        return;
                    for (int k = 0; k < 25; k++) {
                        switch ((int) (Math.random() * 4)) {
                            case 0:
                                RParticles.showWithOffsetPositiveY(ParticleEffect.SMOKE_LARGE, item.getLocation(), RANGE, 30);
                                break;
                            case 1:
                                RParticles.showWithOffsetPositiveY(ParticleEffect.SMOKE_NORMAL, item.getLocation(), RANGE, 30);
                                break;
                            case 2:
                                RParticles.showWithOffsetPositiveY(ParticleEffect.CLOUD, item.getLocation(), RANGE, 30);
                                break;
                            case 3:
                                RParticles.showWithOffsetPositiveY(ParticleEffect.EXPLOSION_LARGE, item.getLocation(), RANGE, 20);
                                break;
                        }
                    }
                    if (kVal == fDuration) {
                        DropManager.removeLabel(item);
                        item.remove();
                    }
                }
            }, k * 10);
        }
        Spell.notify(p, "You throw a device that generates smoke.");
        return true;
    }
}

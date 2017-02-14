package com.edasaki.rpg.mobs;

import java.util.HashMap;

import org.bukkit.ChatColor;

import com.edasaki.rpg.mobs.spells.AoeArrowSpell;
import com.edasaki.rpg.mobs.spells.AoeBrawlerSpell;
import com.edasaki.rpg.mobs.spells.BeamMobSpell;
import com.edasaki.rpg.mobs.spells.BrawlerSpell;
import com.edasaki.rpg.mobs.spells.DerplaxSnoreSpell;
import com.edasaki.rpg.mobs.spells.FireBoltSpell;
import com.edasaki.rpg.mobs.spells.FreezeSpell;
import com.edasaki.rpg.mobs.spells.HweenPumpkinBomb;
import com.edasaki.rpg.mobs.spells.InvisibleSpell;
import com.edasaki.rpg.mobs.spells.LeapSpell;
import com.edasaki.rpg.mobs.spells.ManaDrainSpell;
import com.edasaki.rpg.mobs.spells.MelodaBomb;
import com.edasaki.rpg.mobs.spells.MelodaIceSpell;
import com.edasaki.rpg.mobs.spells.MobSpell;
import com.edasaki.rpg.mobs.spells.PikochoThunderSpell;
import com.edasaki.rpg.mobs.spells.QuadBeamSpell;
import com.edasaki.rpg.mobs.spells.ReflectSpell;
import com.edasaki.rpg.mobs.spells.SlimeKingBurstSpell;
import com.edasaki.rpg.mobs.spells.SlowMobSpell;
import com.edasaki.rpg.mobs.spells.TeleportSpell;

public class MobSpellbook {

    private static HashMap<String, MobSpell> map = new HashMap<String, MobSpell>();

    static {
        map.put("beam", new BeamMobSpell());

        map.put("slow", new SlowMobSpell(5, 1, 10000, ChatColor.AQUA + "Freeze!"));
        map.put("slow1", new SlowMobSpell(5, 1, 10000, ChatColor.AQUA + "Freeze!"));
        map.put("slow2", new SlowMobSpell(6, 2, 12000, ChatColor.AQUA + "Freeze!!"));
        map.put("slow3", new SlowMobSpell(8, 2, 14000, ChatColor.AQUA + "Freeze!!!"));

        map.put("sslow", new SlowMobSpell(5, 1, 10000, null));
        map.put("sslow1", new SlowMobSpell(5, 1, 10000, null));
        map.put("sslow2", new SlowMobSpell(6, 2, 12000, null));
        map.put("sslow3", new SlowMobSpell(8, 2, 14000, null));

        map.put("teleport1", new TeleportSpell(10000, 15000));
        map.put("teleport2", new TeleportSpell(10000, 8000));
        map.put("teleport3", new TeleportSpell(8000, 8000));
        map.put("teleport4", new TeleportSpell(5000, 7000));
        map.put("teleport5", new TeleportSpell(5000, 5000));

        map.put("slimekingburst", new SlimeKingBurstSpell());
        map.put("thunder", new PikochoThunderSpell());
        map.put("snore", new DerplaxSnoreSpell());
        map.put("leap", new LeapSpell());

        map.put("reflect1", new ReflectSpell(3, 24000));
        map.put("reflect2", new ReflectSpell(4, 22000));
        map.put("reflect3", new ReflectSpell(5, 20000));
        map.put("reflect4", new ReflectSpell(6, 18000));
        map.put("reflect5", new ReflectSpell(7, 16000));

        //        map.put("gravity1", new GravitySpell(10, 15000));
        //        map.put("gravity2", new GravitySpell(14, 14000));
        //        map.put("gravity3", new GravitySpell(18, 13000));
        //        map.put("gravity4", new GravitySpell(22, 12000));
        //        map.put("gravity5", new GravitySpell(25, 10000));

        map.put("freeze1", new FreezeSpell(3, 15000, 3, 1));
        map.put("freeze2", new FreezeSpell(5, 15000, 3, 3));
        map.put("freeze3", new FreezeSpell(8, 15000, 4, 5));
        map.put("freeze4", new FreezeSpell(12, 15000, 5, 8));
        map.put("freeze5", new FreezeSpell(15, 15000, 6, 15));

        map.put("invisible1", new InvisibleSpell(20, 5000));
        map.put("invisible2", new InvisibleSpell(20, 3000));
        map.put("invisible3", new InvisibleSpell(20, 2000));
        map.put("invisible4", new InvisibleSpell(20, 1500));
        map.put("invisible5", new InvisibleSpell(20, 1200));

        map.put("qbeam1", new QuadBeamSpell(5, 5000));
        map.put("qbeam2", new QuadBeamSpell(7, 4000));
        map.put("qbeam3", new QuadBeamSpell(10, 3000));

        map.put("mana1", new ManaDrainSpell(1, 9500));
        map.put("mana2", new ManaDrainSpell(2, 8500));
        map.put("mana3", new ManaDrainSpell(2, 6500));

        map.put("brawler1", new BrawlerSpell(1000));
        map.put("brawler2", new BrawlerSpell(800));
        map.put("brawler3", new BrawlerSpell(600));
        map.put("brawler4", new BrawlerSpell(400));
        map.put("brawler5", new BrawlerSpell(200));

        map.put("aoebrawler1", new AoeBrawlerSpell(1000));
        map.put("aoebrawler2", new AoeBrawlerSpell(800));
        map.put("aoebrawler3", new AoeBrawlerSpell(600));
        map.put("aoebrawler4", new AoeBrawlerSpell(400));
        map.put("aoebrawler5", new AoeBrawlerSpell(200));

        map.put("aoearrow1", new AoeArrowSpell(1500));
        map.put("aoearrow2", new AoeArrowSpell(1200));
        map.put("aoearrow3", new AoeArrowSpell(1000));
        map.put("aoearrow4", new AoeArrowSpell(800));
        map.put("aoearrow5", new AoeArrowSpell(600));

        map.put("fbolt1", new FireBoltSpell(8, 5000));
        map.put("fbolt2", new FireBoltSpell(9, 4000));
        map.put("fbolt3", new FireBoltSpell(10, 3000));

        map.put("beam1", new BeamMobSpell());
        map.put("beam2", new BeamMobSpell(3000, 10));
        map.put("beam3", new BeamMobSpell(2000, 13));
        map.put("beam4", new BeamMobSpell(1000, 16));
        map.put("beam5", new BeamMobSpell(500, 22));
        map.put("beam6", new BeamMobSpell(300, 25));
        map.put("beam7", new BeamMobSpell(200, 35));

        map.put("shortbeam1", new BeamMobSpell(3000, 2));
        map.put("shortbeam2", new BeamMobSpell(3000, 3));
        map.put("shortbeam3", new BeamMobSpell(3000, 4));
        map.put("shortbeam4", new BeamMobSpell(3000, 5));
        map.put("shortbeam5", new BeamMobSpell(3000, 6));

        map.put("hweenbomb", new HweenPumpkinBomb());
        map.put("melodabomb", new MelodaBomb());
        map.put("melodaice", new MelodaIceSpell(8, 3000));
    }

    public static MobSpell getSpell(String s) {
        s = s.toLowerCase();
        if (map.containsKey(s))
            return map.get(s);
        try {
            throw new Exception("Invalid mob spell id: \"" + s + "\"");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

package com.edasaki.rpg.mobs;

import java.util.HashMap;

public enum MobAttribute {
    RAPID("rapid"), // rapidfire
    PASSIVE("passive"), // passive mob
    RANGED("ranged"), // no melee
    ARROW1("arrow1"), // tier 1 arrow shooter
    ARROW2("arrow2"), // tier 2 arrow shooter
    ARROW3("arrow3"), // tier 3 arrow shooter
    ARROW4("arrow4"), // tier 4 arrow shooter
    ARROW5("arrow5"), // tier 5 arrow shooter
    HYPERGOLEM("hypergolem"), // hyper golem
    LOWKNOCKBACK("lowknockback"), // mob only gets knocked back a bit
    ANGRYWOLF("angrywolf"), // red eye wolf
    TAMED("tamed"), // tamed mob
    LOWWANDER("lowwander"), // returns to original loc every 10 sec
    POISON1("poison1"), // inflict level 1 poison for 5 seconds
    BABY("baby"),
    CHICKEN1("chicken1"), //chicken mount
    HORSE1("horse1"), //med speed horse mount
    HUSK("husk"), // husk
    STRAY("stray"), //stray
    WITHER("wither"), //wither,
    SLIME1("slime1"), // small slime
    SLIME2("slime2"), // med slime
    SLIME3("slime3"), // large slime
    SLIME4("slime4"), // giant slime
    SLIME5("slime5"), // giant slime
    SLIME6("slime6"), // giant slime
    SLIME7("slime7"), // giant slime
    SLIME8("slime8"), // giant slime
    NODROP("nodrop"), // does not drop randomly generated items
    SLOW1("slow1"), // kinda slow
    SLOW2("slow2"), // pretty slow
    SLOW3("slow3"), // really slow
    FAST1("fast1"), // kinda fast
    FAST2("fast2"), // pretty fast
    FAST3("fast3"), // ultra fast
    BOSS("boss"), // has gold boss name
    ANGELWINGS("angelwings"), //
    INVISIBLE("invisible"),
    RANDOMPASSIVE("randompassive"), //passive, but they run around randomly
    ;
    private static HashMap<String, MobAttribute> map = new HashMap<String, MobAttribute>();

    static {
        for (MobAttribute ma : MobAttribute.values())
            map.put(ma.id, ma);
    }

    private String id;

    MobAttribute(String id) {
        this.id = id;
    }

    public static MobAttribute get(String s) {
        return map.get(s);
    }
}

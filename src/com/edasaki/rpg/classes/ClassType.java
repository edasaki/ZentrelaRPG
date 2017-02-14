package com.edasaki.rpg.classes;

public enum ClassType {
    VILLAGER("Villager"), // default class
    CRUSADER("Crusader"), // swords
    PALADIN("Paladin"), // maces
    ASSASSIN("Assassin"), // daggers
    ALCHEMIST("Alchemist"), // elixirs
    REAPER("Reaper"), // scythes
    ARCHER("Archer"), // bows
    WIZARD("Wizard"); // wands

    public String name;

    ClassType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    public static ClassType getClassType(String s) {
        for (ClassType ct : ClassType.values()) {
            if (s.equalsIgnoreCase(ct.toString()))
                return ct;
        }
        return VILLAGER;
    }
}

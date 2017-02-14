package com.edasaki.rpg.skills;

import com.google.gson.annotations.SerializedName;

public enum Skill {
    //"Gathering"
    @SerializedName("w") WOODCUTTING("Woodcutting"),
    @SerializedName("m") MINING("Mining"),
    @SerializedName("f") FISHING("Fishing"),

    //"Production"
    @SerializedName("c") CRAFTING("Crafting"),
    @SerializedName("co") COOKING("Cooking"),
    @SerializedName("s") SMITHING("Smithing"),
    @SerializedName("fir") FIREMAKING("Firemaking")

    ;

    Skill(String name) {

    }
}

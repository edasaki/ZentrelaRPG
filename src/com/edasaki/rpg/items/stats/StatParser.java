package com.edasaki.rpg.items.stats;

import org.bukkit.ChatColor;

public enum StatParser {
    LEVEL(ChatColor.GRAY + "Level " + ChatColor.WHITE + "%d", new EquipStatParser() {
        @Override
        public void parse(StatAccumulator sa, String s) {
            try {
                if (!s.contains("Level"))
                    return;
                int level = Integer.parseInt(s.substring("Level ".length()));
                sa.level = level;
            } catch (Exception e) {
                return;
            }
        }
    }),
    HP(ChatColor.DARK_GREEN + "HP " + ChatColor.WHITE + "+%d", new EquipStatParser() {
        @Override
        public void parse(StatAccumulator sa, String s) {
            try {
                if (!s.contains("HP") || s.contains("%"))
                    return;
                int val = Integer.parseInt(s.substring("HP +".length()));
                sa.setHP(val);
            } catch (Exception e) {
                return;
            }
        }
    }),
    HP_MULT(ChatColor.DARK_GREEN + "HP " + ChatColor.WHITE + "+%d%%", new EquipStatParser() {
        @Override
        public void parse(StatAccumulator sa, String s) {
            try {
                if (!(s.contains("HP") && s.contains("%")))
                    return;
                int val = Integer.parseInt(s.substring("HP +".length(), s.lastIndexOf('%')));
                sa.setHPMultiplier(val);
            } catch (Exception e) {
                return;
            }
        }
    }),
    DEFENSE(ChatColor.DARK_AQUA + "Defense " + ChatColor.WHITE + "+%d", new EquipStatParser() {
        @Override
        public void parse(StatAccumulator sa, String s) {
            try {
                if (!s.contains("Defense") || s.contains("%"))
                    return;
                int val = Integer.parseInt(s.substring("Defense +".length()));
                sa.setDefense(val);
            } catch (Exception e) {
                return;
            }
        }
    }),
    DEFENSE_MULT(ChatColor.DARK_AQUA + "Defense " + ChatColor.WHITE + "+%d%%", new EquipStatParser() {
        @Override
        public void parse(StatAccumulator sa, String s) {
            try {
                if (!(s.contains("Defense") && s.contains("%")))
                    return;
                int val = Integer.parseInt(s.substring("Defense +".length(), s.lastIndexOf('%')));
                sa.setDefenseMultiplier(val);
            } catch (Exception e) {
                return;
            }
        }
    }),
    DAMAGE(ChatColor.GREEN + "Damage " + ChatColor.WHITE + "%d - %d", new EquipStatParser() {
        @Override
        public void parse(StatAccumulator sa, String s) {
            try {
                if (!(s.contains("Damage") && s.contains("-")))
                    return;
                int low = Integer.parseInt(s.substring("Damage ".length(), s.indexOf(" - ")));
                int high = Integer.parseInt(s.substring(s.indexOf(" - ") + " - ".length()));
                sa.setDamage(low, high);
            } catch (Exception e) {
                return;
            }
        }
    }),
    CRIT_CHANCE(ChatColor.GOLD + "Crit Chance " + ChatColor.WHITE + "+%d%%", new EquipStatParser() {
        @Override
        public void parse(StatAccumulator sa, String s) {
            try {
                if (!s.contains("Crit Chance"))
                    return;
                int val = Integer.parseInt(s.substring("Crit Chance +".length(), s.lastIndexOf('%')));
                sa.setCritChance(val);
            } catch (Exception e) {
                return;
            }
        }
    }),
    CRIT_DAMAGE(ChatColor.GOLD + "Crit Damage " + ChatColor.WHITE + "+%d%%", new EquipStatParser() {
        @Override
        public void parse(StatAccumulator sa, String s) {
            try {
                if (!s.contains("Crit Damage"))
                    return;
                int val = Integer.parseInt(s.substring("Crit Damage +".length(), s.lastIndexOf('%')));
                sa.setCritDamage(val);
            } catch (Exception e) {
                return;
            }
        }
    }),

    RARITY_FINDER(ChatColor.YELLOW + "Treasure Hunter " + ChatColor.WHITE + "+%d%%", new EquipStatParser() {
        @Override
        public void parse(StatAccumulator sa, String s) {
            try {
                if (!s.contains("Treasure Hunter"))
                    return;
                int val = Integer.parseInt(s.substring("Treasure Hunter +".length(), s.lastIndexOf('%')));
                sa.setRarityFinder(val);
            } catch (Exception e) {
                return;
            }
        }
    }),

    MANA_REGEN(ChatColor.AQUA + "Mana Regen Rate " + ChatColor.WHITE + "+%d%%", new EquipStatParser() {
        @Override
        public void parse(StatAccumulator sa, String s) {
            try {
                if (!s.contains("Mana Regen Rate"))
                    return;
                int val = Integer.parseInt(s.substring("Mana Regen Rate +".length(), s.lastIndexOf('%')));
                sa.setManaRegenRate(val);
            } catch (Exception e) {
                return;
            }
        }
    }),

    SPEED(ChatColor.BLUE + "Speed " + ChatColor.WHITE + "+%d%%", new EquipStatParser() {
        @Override
        public void parse(StatAccumulator sa, String s) {
            try {
                if (!s.contains("Speed"))
                    return;
                int val = Integer.parseInt(s.substring("Speed +".length(), s.lastIndexOf('%')));
                sa.setSpeed(val);
            } catch (Exception e) {
                return;
            }
        }
    }),

    SPELL_DAMAGE(ChatColor.LIGHT_PURPLE + "Spell Damage " + ChatColor.WHITE + "+%d%%", new EquipStatParser() {
        @Override
        public void parse(StatAccumulator sa, String s) {
            try {
                if (!s.contains("Spell Damage"))
                    return;
                int val = Integer.parseInt(s.substring("Spell Damage +".length(), s.lastIndexOf('%')));
                sa.setSpellDamage(val);
            } catch (Exception e) {
                return;
            }
        }
    }),

    ATTACK_DAMAGE(ChatColor.DARK_RED + "Attack Damage " + ChatColor.WHITE + "+%d%%", new EquipStatParser() {
        @Override
        public void parse(StatAccumulator sa, String s) {
            try {
                if (!s.contains("Attack Damage"))
                    return;
                int val = Integer.parseInt(s.substring("Attack Damage +".length(), s.lastIndexOf('%')));
                sa.setAttackDamage(val);
            } catch (Exception e) {
                return;
            }
        }
    }),

    HP_REGEN(ChatColor.RED + "HP Regen " + ChatColor.WHITE + "+%d%%", new EquipStatParser() {
        @Override
        public void parse(StatAccumulator sa, String s) {
            try {
                if (!s.contains("HP Regen"))
                    return;
                int val = Integer.parseInt(s.substring("HP Regen +".length(), s.lastIndexOf('%')));
                sa.setHPRegen(val);
            } catch (Exception e) {
                return;
            }
        }
    }),

    ATTACK_SPEED(ChatColor.DARK_PURPLE + "Attack Speed " + ChatColor.WHITE + "+%d%%", new EquipStatParser() {
        @Override
        public void parse(StatAccumulator sa, String s) {
            try {
                if (!s.contains("Attack Speed"))
                    return;
                int val = Integer.parseInt(s.substring("Attack Speed +".length(), s.lastIndexOf('%')));
                sa.setAttackSpeed(val);
            } catch (Exception e) {
                return;
            }
        }
    }),

    // attack speed
    // % chance to inflict __
    ;

    private String format;
    private EquipStatParser esp;

    StatParser(String format, EquipStatParser esp) {
        this.format = format;
        this.esp = esp;
    }

    public String format(Object... args) {
        try {
            return String.format(format, args);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void parse(StatAccumulator as, String s) {
        esp.parse(as, s);
    }
}

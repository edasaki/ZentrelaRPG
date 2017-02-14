package com.edasaki.rpg.commands.builder;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.edasaki.rpg.PlayerDataRPG;
import com.edasaki.rpg.commands.RPGAbstractCommand;
import com.edasaki.rpg.general.SchematicManager;
import com.edasaki.rpg.general.SchematicManager.Schematic;
import com.edasaki.rpg.general.SchematicManager.SchematicUserConfig;

public class TerraformCommand extends RPGAbstractCommand {

    public TerraformCommand(String... commandNames) {
        super(commandNames);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
    }

    @Override
    public void executePlayer(Player p, PlayerDataRPG pd, String[] args) {
        if (args.length == 0) {
            SchematicManager.giveItem(p);
        } else {
            switch (args[0]) {
                case "load":
                    SchematicUserConfig cfg = SchematicManager.getConfig(p);
                    for (int k = 1; k < args.length; k++) {
                        Schematic sch = SchematicManager.loadSchematic(args[k]);
                        if (sch != null) {
                            cfg.loadedSchematics.add(sch);
                            p.sendMessage("Added " + args[k] + " to schematic list.");
                        } else {
                            p.sendMessage("Error loading schematic.");
                        }
                    }
                    p.sendMessage("New list: " + cfg.loadedSchematics.toString());
                    break;
                case "loadrotated":
                    cfg = SchematicManager.getConfig(p);
                    for (int k = 1; k < args.length; k++) {
                        for (int j = 1; j <= 4; j++) {
                            Schematic sch = SchematicManager.loadSchematic(args[k] + "-" + j);
                            if (sch != null) {
                                cfg.loadedSchematics.add(sch);
                                p.sendMessage("Added " + args[k] + " to schematic list.");
                            } else {
                                p.sendMessage("Error loading schematic.");
                            }
                        }
                    }
                    p.sendMessage("New list: " + cfg.loadedSchematics.toString());
                    break;
                case "clear":
                    cfg = SchematicManager.getConfig(p);
                    cfg.loadedSchematics.clear();
                    p.sendMessage("Cleared list! " + cfg.loadedSchematics.toString());
                    break;
                case "yoffset":
                    cfg = SchematicManager.getConfig(p);
                    cfg.ylower = Integer.parseInt(args[1]);
                    cfg.yupper = Integer.parseInt(args[2]);
                    p.sendMessage("Set y offset to range from " + cfg.ylower + " to " + cfg.yupper);
                    break;
                case "xoffset":
                    cfg = SchematicManager.getConfig(p);
                    cfg.xlower = Integer.parseInt(args[1]);
                    cfg.xupper = Integer.parseInt(args[2]);
                    p.sendMessage("Set x offset to range from " + cfg.xlower + " to " + cfg.xupper);
                    break;
                case "zoffset":
                    cfg = SchematicManager.getConfig(p);
                    cfg.zlower = Integer.parseInt(args[1]);
                    cfg.zupper = Integer.parseInt(args[2]);
                    p.sendMessage("Set z offset to range from " + cfg.zlower + " to " + cfg.zupper);
                    break;
                case "grow":
                    SchematicManager.grow(p, Integer.parseInt(args[1]));
                    break;
                case "toggle":
                    cfg = SchematicManager.getConfig(p);
                    if (cfg.mode == 0)
                        cfg.mode = 1;
                    else
                        cfg.mode = 0;
                    p.sendMessage("Toggled to " + (cfg.mode == 0 ? "normal" : "grow") + " mode.");
                    break;
                case "tree":
                    cfg = SchematicManager.getConfig(p);
                    cfg.mode = 2;
                    p.sendMessage("Set to tree mode");
                    break;
                case "growradius":
                    SchematicManager.getConfig(p).growRadius = Integer.parseInt(args[1]);
                    p.sendMessage("Set grow radius to " + args[1]);
                    break;
                case "growdensity":
                    SchematicManager.getConfig(p).growDensity = Double.parseDouble(args[1]);
                    p.sendMessage("Set grow density to " + args[1]);
                    break;
                case "filter":
                    Material m = Material.valueOf(args[1].toUpperCase());
                    if (m != null) {
                        SchematicManager.getConfig(p).filter.add(m);
                        p.sendMessage(SchematicManager.getConfig(p).filter.toString());
                        p.sendMessage("Added material " + m + " to block filter");
                    }
                    break;
                case "clearfilter":
                    SchematicManager.getConfig(p).filter.clear();
                    p.sendMessage("Cleared block filter");
                    break;
                case "rotateroutine":
                    String s = args[1];
                    p.performCommand("/schematic save " + s + "-1");
                    p.performCommand("/rotate 90");
                    p.performCommand("/schematic save " + s + "-2");
                    p.performCommand("/rotate 90");
                    p.performCommand("/schematic save " + s + "-3");
                    p.performCommand("/rotate 90");
                    p.performCommand("/schematic save " + s + "-4");
                    break;
                case "verbose":
                    SchematicManager.getConfig(p).verbose = !SchematicManager.getConfig(p).verbose;
                    p.sendMessage("Toggled verbose mode (debug messages)");
                    break;
            }
        }
    }

    @Override
    public void executeConsole(CommandSender sender, String[] args) {
    }

}

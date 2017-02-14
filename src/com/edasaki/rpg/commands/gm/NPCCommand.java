package com.edasaki.rpg.commands.gm;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.edasaki.core.utils.gson.LocationAdapter;
import com.edasaki.core.utils.gson.RGson;
import com.edasaki.rpg.PlayerDataRPG;
import com.edasaki.rpg.commands.RPGAbstractCommand;
import com.edasaki.rpg.npcs.NPCType;
import com.edasaki.rpg.npcs.generics.GenericNPC;
import com.edasaki.rpg.npcs.generics.GenericNPCManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

public class NPCCommand extends RPGAbstractCommand {

    private HashMap<UUID, ArrayList<GenericNPC>> map = new HashMap<UUID, ArrayList<GenericNPC>>();
    private HashMap<UUID, File> file = new HashMap<UUID, File>();

    public NPCCommand(String... commandNames) {
        super(commandNames);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
    }

    @Override
    public void executePlayer(Player p, PlayerDataRPG pd, String[] args) {
        try {
            UUID uuid = p.getUniqueId();
            switch (args[0].toLowerCase()) {
                case "new":
                    save(p);
                    String s = args[1];
                    if (new File(new File(plugin.getDataFolder(), "generics"), p.getName() + "-" + s + ".json").exists()) {
                        p.sendMessage("A config file already exists with that name! Choose something else.");
                        return;
                    }
                    map.remove(uuid);
                    file.remove(uuid);
                    map.put(uuid, new ArrayList<GenericNPC>());
                    file.put(uuid, new File(new File(plugin.getDataFolder(), "generics"), p.getName() + "-" + s + ".json"));
                    p.sendMessage("Started new config file: " + s);
                    break;
                case "edit":
                    s = args[1];
                    File f = new File(new File(plugin.getDataFolder(), "generics"), p.getName() + "-" + s + ".json");
                    if (!f.exists() || file.values().contains(f)) {
                        p.sendMessage("That config file doesn't exist or is currently in use!");
                        return;
                    }
                    map.remove(uuid);
                    file.remove(uuid);
                    ArrayList<GenericNPC> list = new ArrayList<GenericNPC>();
                    Gson gson = RGson.getGson();
                    try (FileReader reader = new FileReader(f)) {
                        GenericNPC[] vils = gson.fromJson(reader, GenericNPC[].class);
                        for (GenericNPC gv : vils) {
                            if (gv == null)
                                continue;
                            list.add(gv);
                        }
                    } catch (JsonSyntaxException | JsonIOException | IOException e) {
                        System.err.println("Error reading NPC in " + f.getName() + ".");
                        e.printStackTrace();
                    }
                    map.put(uuid, list);
                    file.put(uuid, f);
                    p.sendMessage("Loaded config file " + s + " with " + list.size() + " NPCs (/npc list for full list)");
                    break;
                case "spawn":
                    if (!map.containsKey(uuid)) {
                        p.sendMessage("You are not currently editing a file!");
                        p.sendMessage("Make a new file first with /npc new <filename>");
                        return;
                    }
                    String name = args[1].replaceAll("_", " ");
                    NPCType type = NPCType.valueOf(args[2].toUpperCase());
                    int leash = Integer.parseInt(args[3]);
                    GenericNPC gv = new GenericNPC((int) (Math.random() * 10000000), name, type, p.getLocation(), leash);
                    p.sendMessage("Created NPC " + name + " of type " + type + " with leash  " + leash + " at your position.");
                    map.get(uuid).add(gv);
                    save(p);
                    break;
                case "addline":
                    if (!map.containsKey(uuid)) {
                        p.sendMessage("You are not currently editing a file!");
                        p.sendMessage("Make a new file first with /npc new <filename>");
                        return;
                    }
                    name = args[1].replaceAll("_", " ");
                    StringBuilder sb = new StringBuilder();
                    for (int k = 2; k < args.length; k++) {
                        sb.append(args[k]);
                        sb.append(' ');
                    }
                    String line = sb.toString().trim();
                    boolean edited = false;
                    for (GenericNPC gn : map.get(uuid)) {
                        if (gn.name.equalsIgnoreCase(name)) {
                            gn.lines.add(line);
                            p.sendMessage("Added new new line to NPC " + gn.name);
                            p.sendMessage(line);
                            edited = true;
                            break;
                        }
                    }
                    if (!edited)
                        p.sendMessage("Could not find NPC with that name (be sure to replace spaces with underscores!");
                    save(p);
                    break;
                case "listlines":
                    if (!map.containsKey(uuid)) {
                        p.sendMessage("You are not currently editing a file!");
                        p.sendMessage("Make a new file first with /npc new <filename>");
                        return;
                    }
                    name = args[1].replaceAll("_", " ");
                    edited = false;
                    for (GenericNPC gn : map.get(uuid)) {
                        if (gn.name.equalsIgnoreCase(name)) {
                            p.sendMessage(gn.name + "'s dialogue lines: ");
                            for (int k = 0; k < gn.lines.size(); k++)
                                p.sendMessage(k + ". " + gn.lines.get(k));
                            edited = true;
                            break;
                        }
                    }
                    if (!edited)
                        p.sendMessage("Could not find NPC with that name (be sure to replace spaces with underscores!");
                    break;
                case "removeline":
                    if (!map.containsKey(uuid)) {
                        p.sendMessage("You are not currently editing a file!");
                        p.sendMessage("Make a new file first with /npc new <filename>");
                        return;
                    }
                    name = args[1].replaceAll("_", " ");
                    int index = Integer.parseInt(args[2]);
                    edited = false;
                    for (GenericNPC gn : map.get(uuid)) {
                        if (gn.name.equalsIgnoreCase(name)) {
                            if (index >= 0 && index < gn.lines.size()) {
                                String removed = gn.lines.remove(index);
                                p.sendMessage("Removed line from NPC " + gn.name);
                                p.sendMessage(removed);
                            }
                            edited = true;
                            break;
                        }
                    }
                    if (!edited)
                        p.sendMessage("Could not find NPC with that name (be sure to replace spaces with underscores!");
                    save(p);
                    break;
                case "list":
                    if (!map.containsKey(uuid)) {
                        p.sendMessage("You are not currently editing a file!");
                        p.sendMessage("Make a new file first with /npc new <filename>");
                        return;
                    }
                    p.sendMessage("NPCs in: " + file.get(uuid));
                    for (GenericNPC gn : map.get(uuid))
                        p.sendMessage(gn.name);
                    break;
                case "delete":
                    if (!map.containsKey(uuid)) {
                        p.sendMessage("You are not currently editing a file!");
                        p.sendMessage("Make a new file first with /npc new <filename>");
                        return;
                    }
                    name = args[1].replaceAll("_", " ");
                    boolean found = false;
                    for (int k = 0; k < map.get(uuid).size(); k++) {
                        if (map.get(uuid).get(k).name.equalsIgnoreCase(name)) {
                            p.sendMessage("Removed NPC " + map.get(uuid).get(k).name);
                            map.get(uuid).remove(k);
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        p.sendMessage("Could not find NPC with that name (be sure to replace spaces with underscores!");
                    }
                    save(p);
                    break;
                case "append":
                    if (!map.containsKey(uuid)) {
                        p.sendMessage("You are not currently editing a file!");
                        p.sendMessage("Make a new file first with /npc new <filename>");
                        return;
                    }
                    name = args[1].replaceAll("_", " ");
                    index = Integer.parseInt(args[2]);
                    edited = false;
                    sb = new StringBuilder();
                    for (int k = 3; k < args.length; k++) {
                        sb.append(args[k]);
                        sb.append(' ');
                    }
                    line = sb.toString().trim();
                    for (GenericNPC gn : map.get(uuid)) {
                        if (gn.name.equalsIgnoreCase(name)) {
                            if (index >= 0 && index < gn.lines.size()) {
                                String edit = gn.lines.get(index);
                                p.sendMessage("Edited line from NPC " + gn.name);
                                if (!edit.endsWith(" "))
                                    edit += " ";
                                edit += line;
                                p.sendMessage(edit);
                                gn.lines.set(index, edit);
                            }
                            edited = true;
                            break;
                        }
                    }
                    if (!edited)
                        p.sendMessage("Could not find NPC with that name (be sure to replace spaces with underscores!");
                    save(p);
                    break;
                default:
                    throw new Exception();
            }
        } catch (Exception e) {
            p.sendMessage("/npc new <filename> - create new set of NPCs stored in the file - new spawns are saved automatically. NO SPACES ALLOWED.");
            p.sendMessage("/npc edit <filename> - edit an existing set of NPCs");
            p.sendMessage("/npc spawn <name> <type> <leash> - spawn an NPC at your location with the given name, type, and leash. Use UNDERSCORES as spaces in names.");
            p.sendMessage("/npc addline <name> <dialogue line> - add a line of dialogue to the NPC");
            p.sendMessage("/npc append <name> <index> <dialogue line> - add more to a single line of dialogue on the npc.");
            p.sendMessage("/npc listlines <name> - get a list of all dialogue on this NPC");
            p.sendMessage("/npc removeline <name> <index> - remove a line of dialogue to the NPC. the index is the number next to the dialogue from listlines");
            p.sendMessage("/npc list - list all NPCs in the file you're currently editing");
            p.sendMessage("/npc delete <name> - remove the NPC from the file (permanently deletes it!)");
        }
    }

    @Override
    public void executeConsole(CommandSender sender, String[] args) {
    }

    private void save(Player p) {
        if (map.containsKey(p.getUniqueId()) && file.containsKey(p.getUniqueId())) {
            Gson gson = new GsonBuilder().setPrettyPrinting().registerTypeAdapter(Location.class, new LocationAdapter()).create();
            try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(file.get(p.getUniqueId()))))) {
                writer.write(gson.toJson(map.get(p.getUniqueId())));
                writer.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        GenericNPCManager.reloadGenerics();
    }

}

package com.edasaki.rpg.quests;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Scanner;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import com.edasaki.core.menus.MenuManager;
import com.edasaki.core.unlocks.Unlock;
import com.edasaki.core.utils.RMessages;
import com.edasaki.rpg.AbstractManagerRPG;
import com.edasaki.rpg.PlayerDataRPG;
import com.edasaki.rpg.SakiRPG;
import com.edasaki.rpg.items.ItemManager;
import com.edasaki.rpg.items.RPGItem;
import com.edasaki.rpg.npcs.NPCManager;
import com.edasaki.rpg.npcs.NPCType;

public class QuestManager extends AbstractManagerRPG {

    public static HashMap<String, Quest> quests = new HashMap<String, Quest>();
    public static ArrayList<Quest> activeQuestList = new ArrayList<Quest>();

    public static HashMap<String, MobTrackerInfo> trackerInfo = new HashMap<String, MobTrackerInfo>();

    public QuestManager(SakiRPG plugin) {
        super(plugin);
    }

    @Override
    public void initialize() {
        Quest.plugin = plugin;
        reload();
    }

    public static void reload() {
        for (Quest q : activeQuestList) {
            for (QuestVillager qv : q.npcs.values()) {
                NPCManager.unregister(qv);
            }
        }
        quests.clear();
        activeQuestList.clear();
        File dir = new File(plugin.getDataFolder(), "quests");
        if (!dir.exists())
            dir.mkdirs();
        for (File f : dir.listFiles()) {
            if (f.getName().endsWith(".txt")) {
                readQuest(f);
            }
        }
        for (Quest q : quests.values()) {
            if (q.active)
                activeQuestList.add(q);
        }
        Collections.sort(activeQuestList);
    }

    public static void showQuestMenu(final PlayerDataRPG pd) {
        if (pd.getPlayer() != null) {
            final Player p = pd.getPlayer();
            ArrayList<Object[]> display = new ArrayList<Object[]>();
            int row = 0;
            int col = 0;
            for (final Quest q : activeQuestList) {
                String current = "";
                int currProg = pd.getQuestProgress(q);
                Material m = Material.PAPER;
                if (q.parts == null) {
                    System.out.println("WARNING: COULD NOT DISPLAY QUEST " + q);
                    continue;
                }
                if (currProg <= 0) {
                    current = q.parts.get(0).desc;
                } else if (currProg < q.parts.size() - 1) {
                    current = q.parts.get(currProg).desc;
                    m = Material.BOOK_AND_QUILL;
                } else {
                    current = "Quest Complete!";//q.parts.get(q.parts.size() - 1).desc;
                    m = Material.BOOK;
                }
                if (q.reqQuestsProcessed == null) {
                    q.reqQuestsProcessed = new ArrayList<Quest>();
                    for (String s : q.reqQuests) {
                        if (QuestManager.quests.containsKey(s)) {
                            q.reqQuestsProcessed.add(QuestManager.quests.get(s));
                        }
                    }
                }
                boolean questAvailable = pd.level >= q.reqLevel;
                ArrayList<String> reqQuests = new ArrayList<String>();
                if (q.reqQuestsProcessed.size() > 0) {
                    for (Quest quest : q.reqQuestsProcessed) {
                        if (pd.completedQuest(quest)) {
                            reqQuests.add(ChatColor.GRAY + "- " + ChatColor.GREEN + quest.name);
                        } else {
                            questAvailable = false;
                            reqQuests.add(ChatColor.GRAY + "- " + ChatColor.RED + quest.name);
                        }
                    }
                } else {
                    reqQuests.add(ChatColor.GRAY + "- None");
                }
                if (!questAvailable)
                    m = Material.EMPTY_MAP;
                ArrayList<Object> list = new ArrayList<Object>();
                list.add(ChatColor.DARK_AQUA);
                list.add(q.desc);
                list.add(ChatColor.YELLOW);
                list.add("Required Level: " + (pd.level >= q.reqLevel ? ChatColor.WHITE : ChatColor.RED) + q.reqLevel);
                list.add(ChatColor.YELLOW);
                list.add("Required Quests: ");
                for (String s : reqQuests) {
                    list.add(ChatColor.YELLOW);
                    list.add(s);
                }
                list.add(ChatColor.YELLOW);
                list.add("Total Rewards: ");
                for (String s : q.totalRewards) {
                    list.add(ChatColor.YELLOW);
                    list.add(ChatColor.GRAY + "- " + ChatColor.DARK_GREEN + s);
                }
                list.add(null);
                list.add("");
                list.add(ChatColor.AQUA);
                list.add(current);
                Object[] o = new Object[] {
                        row,
                        col,
                        m,
                        ChatColor.WHITE + q.name,
                        list.toArray(),
                        new Runnable() {
                            public void run() {
                                //                                p.sendMessage("Test: " + q.name + " quest");
                            }
                        }
                };
                col++;
                if (col >= 9) {
                    row++;
                    col = 0;
                }
                display.add(o);
            }
            Inventory menu = MenuManager.createMenu(p, "Zentrela Quest Log", 6, display.toArray(new Object[display.size()][]));
            p.openInventory(menu);
        }
    }

    public static void readQuest(File f) {
        Scanner scan = null;
        try {
            String next = "";
            scan = new Scanner(f);
            // Quest Name
            next = scan.nextLine().trim();
            String questName = next;
            // Display Order
            next = scan.nextLine().trim();
            int displayOrder = Integer.parseInt(next.substring("Display Order:".length()).trim());
            // Req Level
            next = scan.nextLine().trim();
            int reqLevel = Integer.parseInt(next.substring("Required Level:".length()).trim());
            // Req Quests
            next = scan.nextLine().trim();
            String[] data = next.substring("Required Quests:".length()).trim().split(", ");
            ArrayList<String> reqQuests = new ArrayList<String>();
            for (String s : data) {
                if ((s = s.trim()).equalsIgnoreCase("none"))
                    continue;
                else
                    reqQuests.add(s.trim());
            }
            // Total Rewards
            next = scan.nextLine().trim();
            ArrayList<String> totalRewards = new ArrayList<String>();
            for (String s : next.substring("Total Rewards:".length()).trim().split(", ")) {
                totalRewards.add(s.trim());
            }
            // Description
            next = scan.nextLine().trim();
            String desc = next.substring("Description:".length()).trim();
            // Create quest
            Quest q = new Quest(questName, reqLevel, reqQuests, desc, totalRewards);
            q.displayOrder = displayOrder;
            String identifier = f.getName().substring(0, f.getName().lastIndexOf('.'));
            q.identifier = identifier;
            quests.put(identifier, q);
            // read in the NPCs
            do {
                next = scan.nextLine().trim();
                if (next.startsWith("//") || next.length() == 0)
                    continue;
                if (next.equalsIgnoreCase("#start#"))
                    break;
                data = next.split(", ");
                int id = Integer.parseInt(data[0]);
                String name = data[1];
                boolean isBaby = Boolean.parseBoolean(data[2]);
                double x = Double.parseDouble(data[3]);
                double y = Double.parseDouble(data[4]);
                double z = Double.parseDouble(data[5]);
                String world = data[6];
                QuestVillager qv = new QuestVillager(q, id, name, isBaby ? NPCType.BABYVILLAGER : NPCType.VILLAGER, x, y, z, world);
                qv.register();
                q.npcs.put(qv.id, qv);
            } while (!next.equalsIgnoreCase("#start#"));
            // read in the quest parts
            int currentPartNum = 0;
            QuestPart qp = null;
            boolean nextPart = true;
            while (scan.hasNextLine()) {
                next = scan.nextLine().trim();
                if (next.length() == 0 || next.startsWith("//") || next.startsWith("#"))
                    continue;
                if (nextPart) {
                    QuestPart last = null;
                    if (qp != null) {
                        q.parts.put(currentPartNum, qp);
                        currentPartNum++;
                        last = qp;
                    }
                    qp = new QuestPart(q, currentPartNum);
                    if (last != null)
                        qp.registerTransfer(last);
                }
                nextPart = true;
                data = next.split(" ");
                if (next.startsWith("desc")) {
                    qp.desc = next.substring("desc ".length());
                    qp.desc = qp.desc.replaceAll("\\(", ChatColor.WHITE + ChatColor.BOLD.toString() + "(");
                    qp.desc = qp.desc.replaceAll("\\)", ")" + ChatColor.RESET);
                    qp.newDesc = true;
                    nextPart = false;
                } else if (next.startsWith("set_default")) {
                    int id = Integer.parseInt(data[1]);
                    StringBuilder sb = new StringBuilder();
                    for (int k = 2; k < data.length; k++) {
                        sb.append(data[k]);
                        sb.append(' ');
                    }
                    qp.register(id, ChatColor.translateAlternateColorCodes('&', sb.toString()));
                    nextPart = false;
                } else if (next.startsWith("say")) {
                    int id = Integer.parseInt(data[1]);
                    StringBuilder sb = new StringBuilder();
                    for (int k = 2; k < data.length; k++) {
                        sb.append(data[k]);
                        sb.append(' ');
                    }
                    qp.partType = QuestPartType.SAY;
                    qp.sayId = id;
                    qp.sayMsg = ChatColor.translateAlternateColorCodes('&', sb.toString().trim());
                } else if (next.startsWith("self")) {
                    int id = Integer.parseInt(data[1]);
                    StringBuilder sb = new StringBuilder();
                    for (int k = 2; k < data.length; k++) {
                        sb.append(data[k]);
                        sb.append(' ');
                    }
                    qp.partType = QuestPartType.SELF;
                    qp.selfId = id;
                    qp.selfMsg = ChatColor.translateAlternateColorCodes('&', sb.toString().trim());
                } else if (next.startsWith("give ")) {
                    nextPart = false;
                    RPGItem rpgi = ItemManager.itemIdentifierToRPGItemMap.get(data[1]);
                    if (rpgi == null) {
                        RMessages.announce("ERROR: Could not find quest item " + data[1]);
                    } else {
                        qp.giveItems.put(rpgi, Integer.parseInt(data[2]));
                    }
                } else if (next.startsWith("give_shards")) {
                    nextPart = false;
                    qp.giveShards = Integer.parseInt(data[1]);
                } else if (next.startsWith("give_exp")) {
                    nextPart = false;
                    qp.giveExp = Long.parseLong(data[1]);
                } else if (next.startsWith("take")) {
                    qp.partType = QuestPartType.TAKE;
                    RPGItem rpgi = ItemManager.itemIdentifierToRPGItemMap.get(data[1]);
                    if (rpgi == null) {
                        RMessages.announce("ERROR: Could not find quest item " + data[1]);
                    } else {
                        qp.takeItem = rpgi;
                        qp.takeItemGenerated = rpgi.generate();
                        qp.takeAmount = Integer.parseInt(data[2]);
                        qp.takeId = Integer.parseInt(data[3]);
                        StringBuilder sb = new StringBuilder();
                        for (int k = 4; k < data.length; k++) {
                            sb.append(data[k]);
                            sb.append(' ');
                        }
                        String[] temp = sb.toString().trim().split("##");
                        qp.takeSuccess = ChatColor.translateAlternateColorCodes('&', temp[0].trim());
                        qp.takeFail = ChatColor.translateAlternateColorCodes('&', temp[1].trim());
                    }
                } else if (next.startsWith("warp")) {
                    int id = Integer.parseInt(data[1]);
                    double x = Double.parseDouble(data[2]);
                    double y = Double.parseDouble(data[3]);
                    double z = Double.parseDouble(data[4]);
                    float yaw = Float.parseFloat(data[5]);
                    float pitch = Float.parseFloat(data[6]);
                    World w = plugin.getServer().getWorld(data[7]);
                    if (w == null)
                        throw new Exception("Missing quest world " + data[7]);
                    StringBuilder sb = new StringBuilder();
                    for (int k = 8; k < data.length; k++) {
                        sb.append(data[k]);
                        sb.append(' ');
                    }
                    Location loc = new Location(w, x, y, z, yaw, pitch);
                    qp.partType = QuestPartType.WARP;
                    qp.warpId = id;
                    qp.warpLoc = loc;
                    qp.warpMsg = ChatColor.translateAlternateColorCodes('&', sb.toString().trim());
                } else if (next.startsWith("state")) {
                    // state 1 blah_blah 222 ## 222
                    qp.partType = QuestPartType.STATE;
                    qp.stateId = Integer.parseInt(data[1]);
                    qp.stateIdentifier = data[2];
                    StringBuilder sb = new StringBuilder();
                    for (int k = 3; k < data.length; k++) {
                        sb.append(data[k]);
                        sb.append(' ');
                    }
                    String[] temp = sb.toString().trim().split("##");
                    qp.stateSuccess = temp[0];
                    qp.stateFail = temp[1];
                } else if (next.startsWith("unlock")) {
                    // unlock UNLOCK_ENUM_NAME
                    nextPart = false;
                    qp.giveUnlock = Unlock.valueOf(data[1].toUpperCase());
                } else if (next.startsWith("givestate")) {
                    // givestate blah_blah_lol
                    nextPart = false;
                    qp.giveState = data[1];
                } else if (next.startsWith("addtracker")) {
                    //addtracker tracker1 crypt_skeleton 35 Skeletons ## That wasn't so tough. Better go back to Benny at (x2, y2, z2).
                    nextPart = false;
                    qp.hasTracker = true;
                    qp.addingTrackerIdentifier = q.identifier + "-" + data[1];
                    qp.mobsToTrack = data[2].split(",");

                    StringBuilder sb = new StringBuilder();
                    for (int k = 4; k < data.length; k++) {
                        sb.append(data[k]);
                        sb.append(' ');
                    }
                    String[] temp = sb.toString().trim().split("##");

                    MobTrackerInfo mti = new MobTrackerInfo();
                    mti.mobsToTrack = qp.mobsToTrack;
                    mti.trackerMobName = temp[0].trim();
                    mti.questName = q.name;
                    mti.trackerFinishedNotification = temp[1].trim();
                    mti.requiredCount = Integer.parseInt(data[3]);
                    mti.identifier = qp.addingTrackerIdentifier;
                    trackerInfo.put(qp.addingTrackerIdentifier, mti);
                } else if (next.startsWith("tracker")) {
                    // tracker 1 tracker1 *pant* *pant* Hm? Back already? ## Ha! Not done yet? 35 bonies is a lot to kill! You probably can't do it.
                    qp.partType = QuestPartType.TRACKER;
                    qp.trackerNPCID = Integer.parseInt(data[1]);
                    qp.trackerIdentifierToCheck = q.identifier + "-" + data[2];
                    StringBuilder sb = new StringBuilder();
                    for (int k = 3; k < data.length; k++) {
                        sb.append(data[k]);
                        sb.append(' ');
                    }
                    String[] temp = sb.toString().trim().split("##");
                    qp.trackerSuccess = temp[0].trim();
                    qp.trackerFail = temp[1].trim();
                } else if (next.startsWith("custom")) {
                    int id = Integer.parseInt(data[1]);
                    int num = Integer.parseInt(data[2]);
                    qp.partType = QuestPartType.CUSTOM;
                    qp.customId = id;
                    qp.customNum = num;
                } else {
                    throw new Exception("Unknown quest instruction " + next);
                }
            }
            if (qp != null) {
                q.parts.put(currentPartNum, qp);
            }
            quests.put(identifier, q);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Error reading quest in " + f.getName() + ".");
            e.printStackTrace();
        } finally {
            if (scan != null)
                scan.close();
        }
        System.out.println("Finished reading quest " + f.getName() + ".");
    }

}

package com.edasaki.rpg.quests;

import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.edasaki.core.unlocks.Unlock;
import com.edasaki.core.utils.RUtils;
import com.edasaki.rpg.PlayerDataRPG;
import com.edasaki.rpg.economy.ShardManager;
import com.edasaki.rpg.items.ItemManager;
import com.edasaki.rpg.items.RPGItem;
import com.edasaki.rpg.warps.WarpCallback;
import com.edasaki.rpg.warps.WarpManager;

public class QuestPart {

    public Quest quest;

    public int id;
    public String desc;
    public boolean newDesc = false;

    public int sayId;
    public String sayMsg;

    public int selfId;
    public String selfMsg;

    public int warpId;
    public Location warpLoc;
    public String warpMsg;

    public int stateId;
    public String stateIdentifier;
    public String stateSuccess;
    public String stateFail;

    public Unlock giveUnlock;
    public String giveState;

    // addtracker
    public boolean hasTracker = false;
    public String addingTrackerIdentifier;
    public String[] mobsToTrack;

    // complete tracker
    public int trackerNPCID;
    public String trackerIdentifierToCheck;
    public String trackerSuccess;
    public String trackerFail;

    public int customId;
    public int customNum;

    public int takeId;
    public RPGItem takeItem;
    public ItemStack takeItemGenerated;
    public int takeAmount;
    public String takeSuccess;
    public String takeFail;

    public long giveExp = 0;
    public int giveShards = 0;

    public HashMap<RPGItem, Integer> giveItems = new HashMap<RPGItem, Integer>();

    public QuestPartType partType = null;

    /*
     * This maps QNPC ID -> Dialogue.
     * set_default should only be used once for each QNPC in one part. 
     */
    private HashMap<Integer, String[]> defaultText = new HashMap<Integer, String[]>();

    public QuestPart(Quest q, int id) {
        this.quest = q;
        this.id = id;
    }

    public boolean isNPC(QuestVillager qv) {
        if (partType == null)
            return false;
        switch (this.partType) {
            case SAY:
                return this.sayId == qv.id;
            case SELF:
                return this.selfId == qv.id;
            case TAKE:
                return this.takeId == qv.id;
            case WARP:
                return this.warpId == qv.id;
            case CUSTOM:
                return this.customId == qv.id;
            case STATE:
                return this.stateId == qv.id;
            case TRACKER:
                return this.trackerNPCID == qv.id;
        }
        return false;
    }

    public boolean interact(QuestVillager qv, Player p, PlayerDataRPG pd) {
        if (id == 0) {
            p.sendMessage("");
            p.sendMessage(ChatColor.GRAY + "=======================================");
            p.sendMessage(ChatColor.YELLOW + " Quest Started! " + ChatColor.GOLD + quest.name);
            p.sendMessage(ChatColor.WHITE + " Use " + ChatColor.YELLOW + "/quest " + ChatColor.WHITE + "to track your quest progress!");
            p.sendMessage(ChatColor.GRAY + "=======================================");
        } else if (newDesc && id != 0) {
            p.sendMessage("");
            p.sendMessage(ChatColor.GRAY + "=======================================");
            p.sendMessage(ChatColor.YELLOW + " Quest Log Updated! ");
            p.sendMessage(ChatColor.WHITE + " " + desc);
            p.sendMessage(ChatColor.WHITE + " Use " + ChatColor.YELLOW + "/quest " + ChatColor.WHITE + "to track your quest progress!");
            p.sendMessage(ChatColor.GRAY + "=======================================");
        }
        boolean cont = false;
        if (quest.parts.containsKey(this.id + 1)) {
            if (quest.parts.get(this.id + 1).isNPC(qv)) {
                cont = true;
            }
        }
        if (giveItems != null && !giveItems.isEmpty()) {
            boolean hasSpace = true;
            if (!RUtils.hasEmptySpaces(p, giveItems.size())) {
                p.sendMessage("");
                p.sendMessage(ChatColor.GRAY + ">> " + ChatColor.RED + "You don't have enough inventory space!");
                p.sendMessage(ChatColor.GRAY + ">> " + ChatColor.RED + "You need " + ChatColor.YELLOW + giveItems.size() + ChatColor.RED + " empty slots!");
                p.sendMessage(ChatColor.GRAY + ">> " + ChatColor.RED + "Please clear out some space and talk to " + qv.name + ChatColor.RED + " again.");
                hasSpace = false;
            }
            if (hasSpace) {
                for (Entry<RPGItem, Integer> e : giveItems.entrySet()) {
                    ItemStack item = e.getKey().generate();
                    item.setAmount(e.getValue());
                    p.sendMessage(ChatColor.GRAY + ">> " + ChatColor.WHITE + "You received " + e.getKey().name + ChatColor.WHITE + ".");
                    p.getInventory().addItem(item);
                }
            } else {
                return false;
            }
        }
        if (this.partType == QuestPartType.TAKE) {
            if (ItemManager.take(p, takeItemGenerated, takeAmount)) {
                p.sendMessage(ChatColor.GRAY + ">> " + ChatColor.WHITE + qv.name + ChatColor.WHITE + " took " + takeAmount + "x " + takeItem.name + ChatColor.WHITE + ".");
                if (cont)
                    qv.say(p, takeSuccess + " " + ChatColor.GRAY + ChatColor.BOLD + "(...)");
                else
                    qv.say(p, takeSuccess);
            } else {
                qv.say(p, takeFail);
                return false;
            }
        }
        if (this.partType == QuestPartType.STATE) {
            if (pd.hasState(this.stateIdentifier)) {
                if (cont)
                    qv.say(p, this.stateSuccess + " " + ChatColor.GRAY + ChatColor.BOLD + "(...)");
                else
                    qv.say(p, this.stateSuccess);
            } else {
                qv.say(p, this.stateFail);
                return false;
            }
        }
        if (this.partType == QuestPartType.TRACKER) {
            MobTrackerInfo mi = QuestManager.trackerInfo.get(this.trackerIdentifierToCheck);
            if (pd.getMobCount(this.trackerIdentifierToCheck, mi.mobsToTrack) >= mi.requiredCount) {
                if (cont) {
                    qv.say(p, this.trackerSuccess + " " + ChatColor.GRAY + ChatColor.BOLD + "(...)");
                } else {
                    qv.say(p, this.trackerSuccess);
                }
                pd.removeMobCounter(this.trackerIdentifierToCheck, mi.mobsToTrack);
            } else {
                qv.say(p, this.trackerFail);
                return false;
            }
        }
        if (hasTracker) {
            for (String s : this.mobsToTrack) {
                pd.addNewMobCounter(s, this.addingTrackerIdentifier);
            }
        }
        if (giveExp > 0) {
            pd.gainExp(giveExp);
            p.sendMessage(ChatColor.GRAY + ">> " + ChatColor.WHITE + "You received " + ChatColor.YELLOW + giveExp + " EXP" + ChatColor.WHITE + ".");
        }
        if (giveShards > 0) {
            p.sendMessage(ChatColor.GRAY + ">> " + ChatColor.WHITE + "You received " + ChatColor.YELLOW + giveShards + " Shards" + ChatColor.WHITE + ".");
            ShardManager.giveCurrency(p, giveShards);
        }
        if (giveUnlock != null) {
            pd.addUnlock(giveUnlock);
            giveUnlock.sendMessage(pd);
        }
        if (giveState != null) {
            pd.addState(giveState);
        }
        if (!quest.parts.containsKey(this.id + 1)) {
            p.sendMessage("");
            p.sendMessage(ChatColor.GOLD + "=======================================");
            p.sendMessage(ChatColor.YELLOW + " Quest Complete! " + ChatColor.WHITE + quest.name);
            p.sendMessage(ChatColor.GOLD + "=======================================");
        }
        switch (this.partType) {
            case SAY:
                if (cont)
                    qv.say(p, sayMsg + " " + ChatColor.GRAY + ChatColor.BOLD + "(...)");
                else
                    qv.say(p, sayMsg);
                break;
            case SELF:
                p.sendMessage("");
                String tmp;
                if (selfMsg.startsWith("#")) {
                    tmp = ChatColor.WHITE + ChatColor.ITALIC.toString() + selfMsg;
                } else if (selfMsg.charAt(0) == '(' && selfMsg.charAt(selfMsg.length() - 1) == ')') {
                    tmp = ChatColor.YELLOW + "You" + ChatColor.WHITE + ": " + ChatColor.ITALIC + selfMsg;
                } else {
                    //                    tmp = ChatColor.YELLOW + pd.getPlayer().getName() + ChatColor.WHITE + ": " + selfMsg;
                    tmp = ChatColor.YELLOW + "You" + ChatColor.WHITE + ": " + selfMsg;
                }
                if (cont)
                    p.sendMessage(tmp + " " + ChatColor.GRAY + ChatColor.BOLD + "(...)");
                else
                    p.sendMessage(tmp);
                break;
            case TAKE:
                // do this AFTER give items to prevent taking items and then not continuing
                break;
            case WARP:
                qv.say(p, warpMsg);
                WarpManager.warp(p, this.warpLoc, new WarpCallback() {
                    @Override
                    public void complete(boolean warpSuccess) {
                        if (warpSuccess) {
                            pd.advanceQuest(quest);
                        } else {
                            qv.say(p, "Talk to me again when you're ready to warp!");
                        }
                    }
                });
                return false;
            case CUSTOM:
                return CustomQuestParts.check(this.customNum, p, pd, quest, qv);
            case STATE:
                break;
            default:
                break;
        }
        return true;
    }

    public void register(int npcId, String msg) {
        String[] arr = msg.split("##");
        for (int k = 0; k < arr.length; k++)
            arr[k] = ChatColor.GRAY + arr[k].trim();
        defaultText.put(npcId, arr);
    }

    public void registerTransfer(QuestPart qp) {
        for (Entry<Integer, String[]> e : qp.defaultText.entrySet())
            this.defaultText.put(e.getKey(), e.getValue());
        this.desc = qp.desc;
    }

    public String getMessage(int npcId) {
        String[] s = null;
        if (defaultText.containsKey(npcId))
            s = defaultText.get(npcId);
        if (s != null) {
            return s[(int) (Math.random() * s.length)];
        }
        return "Uh oh! Error 192, please report this on the Zentrela forum, thanks!";
    }

    @Override
    public String toString() {
        return id + " - " + desc + " " + defaultText + " TYPE: " + partType;
    }

}

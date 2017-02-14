package com.edasaki.rpg.chat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.edasaki.core.chat.ChatFilter;
import com.edasaki.core.options.SakiOption;
import com.edasaki.core.players.Rank;
import com.edasaki.core.punishments.PunishmentManager;
import com.edasaki.core.shield.SakiShieldCore;
import com.edasaki.core.shield.SakiShieldCore.Activity;
import com.edasaki.core.utils.RMath;
import com.edasaki.core.utils.RScheduler;
import com.edasaki.core.utils.RSound;
import com.edasaki.core.utils.fanciful.FancyMessage;
import com.edasaki.rpg.AbstractManagerRPG;
import com.edasaki.rpg.PlayerDataRPG;
import com.edasaki.rpg.SakiRPG;
import com.edasaki.rpg.tips.TipManager;

public class ChatManager extends AbstractManagerRPG {

    private static HashMap<String, Long> lastChat = new HashMap<String, Long>();

    private static long lastDiscord = 0;
    private static long lastMap = 0;
    private static long lastTwitter = 0;
    private static long lastBully = 0;
    private static long lastForums = 0;
    private static long lastStaff = 0;
    private static long lastTip = 0;
    private static long lastIP = 0;
    private static long lastStore = 0;
    private static long lastWiki = 0;
    private static long lastWebsite = 0;
    private static long lastPatch = 0;

    private static long lastHelp = 0;

    public static HashMap<String, Long> lastGlobalChat = new HashMap<String, Long>();

    public static HashMap<String, String> lastReceivedWhisperFrom = new HashMap<String, String>();
    public static HashMap<String, Long> lastReceivedWhisperTime = new HashMap<String, Long>();
    public static HashSet<String> monitors = new HashSet<String>();

    private long lastTrigger = 0;
    public static ArrayList<String> shadowMute = new ArrayList<String>();

    public static HashMap<String, Long> lastSentPM = new HashMap<String, Long>();

    public ChatManager(SakiRPG plugin) {
        super(plugin);
    }

    @Override
    public void initialize() {

    }

    public static void sendWhisper(Player sender, String targetName, PlayerDataRPG pd, String message) {
        if (System.currentTimeMillis() - lastSentPM.getOrDefault(pd.getName(), 0l) < 100) {
            SakiShieldCore.warn(pd, Activity.FAST_CHAT);
            return;
        }
        if (PunishmentManager.isMuted(sender)) {
            pd.sendMessage(ChatColor.RED + "You are muted and cannot message others!");
            pd.sendMessage(ChatColor.RED + PunishmentManager.getMuteReason(sender));
            return;
        }
        Player target = plugin.getServer().getPlayerExact(targetName);
        if (target != null && target.isOnline() && plugin.getPD(target) != null) {
            if (target == sender) {
                sender.sendMessage(ChatColor.RED + "You can't message yourself, silly! That's what thinking is for.");
                return;
            }
            PlayerDataRPG pd2 = plugin.getPD(target);
            if (pd2.isIgnoring(pd)) {
                pd.sendMessage(ChatColor.RED + pd2.getName() + " is ignoring you and is not receiving your messages.");
                return;
            }
            String senderName = pd.getChatNameColor() + sender.getName();
            String receiverName = pd2.getChatNameColor() + target.getName();

            FancyMessage fm = new FancyMessage();
            fm.text("[");
            fm.color(ChatColor.GRAY);
            fm.then(senderName);
            ChatManager.chatHover(fm, pd);
            fm.then("->");
            fm.color(ChatColor.GRAY);
            fm.then(receiverName);
            ChatManager.chatHover(fm, pd2);
            fm.then("] ");
            fm.color(ChatColor.GRAY);
            fm.then(pd.getOption(SakiOption.CHAT_FILTER) || pd2.getOption(SakiOption.CHAT_FILTER) ? ChatFilter.getFiltered(message) : message);
            fm.style(ChatColor.ITALIC);
            fm.send(sender);
            if (PunishmentManager.isMuted(target)) {
                sender.sendMessage(ChatColor.RED + target.getName() + " is currently muted and won't be able to respond.");
            }
            if (!ChatManager.shadowMute.contains(sender.getName())) {
                fm.send(target);
                if (!ChatManager.lastReceivedWhisperFrom.containsKey(target.getName()) || !ChatManager.lastReceivedWhisperFrom.get(target.getName()).equals(sender.getName()))
                    RSound.playSound(target, Sound.ENTITY_EXPERIENCE_ORB_PICKUP);
                else if (!ChatManager.lastReceivedWhisperTime.containsKey(target.getName()) || System.currentTimeMillis() - ChatManager.lastReceivedWhisperTime.get(target.getName()) > 30000)
                    RSound.playSound(target, Sound.ENTITY_EXPERIENCE_ORB_PICKUP);
                ChatManager.lastReceivedWhisperFrom.put(target.getName(), sender.getName());
                ChatManager.lastReceivedWhisperFrom.put(sender.getName(), target.getName());
                ChatManager.lastReceivedWhisperTime.put(target.getName(), System.currentTimeMillis());
            }
            for (String m : ChatManager.monitors) {
                Player monitor = plugin.getServer().getPlayerExact(m);
                if (monitor != null && monitor.isOnline()) {
                    if (sender != monitor && target != monitor) {
                        monitor.sendMessage(ChatColor.DARK_GRAY + "[MONITOR]");
                        fm.send(monitor);
                    }
                }
            }
            Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + ChatColor.stripColor(fm.toOldMessageFormat()));
        } else {
            sender.sendMessage(ChatColor.RED + "Could not find online player '" + targetName + "'.");
        }
    }

    private HashMap<ChatMention, Long> mentions = new HashMap<ChatMention, Long>();

    private static class ChatMention {
        public final String talker;
        public final String mentioned;

        @Override
        public boolean equals(Object other) {
            if (other == null)
                return false;
            if (!(other instanceof ChatMention))
                return false;
            ChatMention cm = (ChatMention) other;
            return cm.talker.equals(this.talker) && cm.mentioned.equals(this.mentioned);
        }

        @Override
        public int hashCode() {
            return talker.hashCode() + mentioned.hashCode();
        }

        public ChatMention(String talker, String mentioned) {
            this.talker = talker.toLowerCase();
            this.mentioned = mentioned.toLowerCase();
        }
    }

    public static FancyMessage chatHover(FancyMessage fm, PlayerDataRPG pd) {
        StringBuilder sb = new StringBuilder();
        sb.append(pd.getChatNameColor());
        sb.append(pd.getPlayer().getName());
        sb.append(ChatColor.GRAY);
        sb.append(" | ");
        sb.append(ChatColor.GREEN);
        sb.append(pd.classType);
        sb.append(ChatColor.GRAY);
        sb.append(" | ");
        sb.append(ChatColor.AQUA);
        sb.append("Level ");
        sb.append(pd.level);
        sb.append(ChatColor.GRAY);
        sb.append(" | ");
        sb.append(ChatColor.GOLD);
        sb.append("Rank: ");
        sb.append(pd.getChatNameColor());
        sb.append(pd.getFullRankClean());
        return fm.tooltip(sb.toString()).command("/information " + pd.getPlayer().getName());
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {

        boolean global = false;

        // Tips only fire if someone is talking
        if (System.currentTimeMillis() - lastTrigger > 30000) {
            TipManager.schedule();
            lastTrigger = System.currentTimeMillis();
        }

        // Get PlayerData
        PlayerDataRPG pd = plugin.getPD(event.getPlayer());
        if (pd == null || pd.getPlayer() == null || !pd.loadedSQL) {
            event.setCancelled(true);
            return;
        }

        if (!pd.check(Rank.VIP)) {
            if (lastChat.containsKey(pd.getName()) && System.currentTimeMillis() - lastChat.get(pd.getName()) < 2000) {
                pd.sendMessage(ChatColor.RED + " You can only chat once every 2 seconds.");
                pd.sendMessage(ChatColor.RED + " Get a rank at " + ChatColor.YELLOW + "store.zentrela.net" + ChatColor.RED + " to have no chat limits!");
                event.setCancelled(true);
                return;
            }
            lastChat.put(pd.getName(), System.currentTimeMillis());
        }

        if (System.currentTimeMillis() - lastSentPM.getOrDefault(pd.getName(), 0l) < 100) {
            SakiShieldCore.warn(pd, Activity.FAST_CHAT);
            return;
        }

        // Handle Party Chat
        if (event.getMessage().startsWith("\\") && event.getMessage().length() > 1) {
            if (pd.party != null) {
                pd.party.sendMessage(event.getPlayer(), event.getMessage().substring(1));
                Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "Party ID " + pd.party.id + " " + event.getPlayer().getName() + ": " + event.getMessage());
            } else {
                event.getPlayer().sendMessage(ChatColor.RED + "You are not in a party, so you can't use Party Chat! " + ChatColor.YELLOW + "/party");
            }
            event.setCancelled(true);
            return;
        }

        // No chat if muted
        if (PunishmentManager.isMuted(event.getPlayer())) {
            pd.sendMessage(ChatColor.RED + "You are muted and cannot talk!");
            pd.sendMessage(ChatColor.RED + PunishmentManager.getMuteReason(event.getPlayer()));
            event.setCancelled(true);
            return;
        }

        if ((!pd.getOption(SakiOption.FORCE_GLOBAL_CHAT) && event.getMessage().startsWith("#")) || (pd.getOption(SakiOption.FORCE_GLOBAL_CHAT) && !event.getMessage().startsWith("#"))) {
            if (!SakiRPG.TEST_REALM && !pd.check(Rank.VIP) && lastGlobalChat.containsKey(pd.getName()) && System.currentTimeMillis() - lastGlobalChat.get(pd.getName()) < 5000) {
                global = false;
                pd.sendMessage(ChatColor.RED + " You can only global chat once every 5 seconds.");
                pd.sendMessage(ChatColor.RED + " Get a rank at " + ChatColor.YELLOW + "store.zentrela.net" + ChatColor.RED + " to have no chat limits!");
            } else {
                global = true;
                lastGlobalChat.put(pd.getName(), System.currentTimeMillis());
                if (!pd.getOption(SakiOption.FORCE_GLOBAL_CHAT))
                    event.setMessage(event.getMessage().substring(1).trim());
            }
        } else if (SakiRPG.TEST_REALM) {
            global = true;
        }

        // Create two versions of message, one filtered and one unfiltered
        FancyMessage fm_unfiltered = new FancyMessage();
        FancyMessage fm_filtered = new FancyMessage();
        boolean sentLinkLimitMessage = false;
        FancyMessage[] fms = new FancyMessage[] { fm_unfiltered, fm_filtered };
        for (int k = 0; k < fms.length; k++) {
            FancyMessage fm = fms[k];
            chatHover(fm.text("[" + pd.level + "] ").color(ChatColor.GRAY), pd);
            chatHover(fm.then(pd.getChatRankPrefix()), pd);
            chatHover(fm.then(event.getPlayer().getName()), pd);
            pd.addBadgesSuffix(fm);
            fm.then(": ").color(ChatColor.WHITE);
            if (global) {
                fm.then("[Global] ").color(ChatColor.DARK_GRAY).tooltip("Type # at the start of your message to use global chat!\nNormal chat is local, meaning only nearby players can see it.");
            }
            // super ugly code bug basically check if this is the filtered one
            String[] data = (k == 1 ? ChatFilter.getFiltered(event.getMessage()) : event.getMessage()).split(" ");
            ChatColor playerChatColor = pd.getChatColor();
            int linkCount = 0;
            for (int a = 0; a < data.length; a++) {
                String tmp = data[a].trim();
                boolean itemLink = false;
                if (pd.check(Rank.VIP) && pd.loadedSQL) {
                    if (tmp.toLowerCase().matches("\\[slot\\d\\]")) {
                        int slot = 0;
                        try {
                            slot = Integer.parseInt(tmp.replaceAll("[^0-9]", ""));
                        } catch (Exception e) {
                        }
                        if (slot < 1 || slot > 9) {
                            pd.sendMessage(ChatColor.RED + "Invalid slot! Slot must be from 1 to 9, such as [slot2].");
                        } else {
                            if (linkCount >= 5) {
                                if (!sentLinkLimitMessage) {
                                    sentLinkLimitMessage = true;
                                    RScheduler.schedule(plugin, () -> {
                                        pd.sendMessage(ChatColor.RED + "You may only link up to 5 items in one message.");
                                    }, 2);
                                }
                            } else {
                                linkCount++;
                                itemLink = true;
                                ItemStack item = pd.getPlayer().getInventory().getItem(slot - 1);
                                String name = ChatColor.GRAY + "null";
                                List<String> hover = new ArrayList<String>();
                                if (item != null) {
                                    ItemMeta im = item.getItemMeta();
                                    if (im != null) {
                                        if (im.hasDisplayName()) {
                                            name = im.getDisplayName();
                                        }
                                        if (im.hasLore()) {
                                            hover.add(name);
                                            for (String s : im.getLore())
                                                hover.add(s);
                                        }
                                    }
                                }
                                String[] namedata = name.split(" ");
                                String lastColor = ChatColor.getLastColors(name);
                                for (int i = 0; i < namedata.length; i++) {
                                    if (ChatColor.getLastColors(namedata[i]).length() > 0) {
                                        lastColor = ChatColor.getLastColors(namedata[i]);
                                    }
                                    fm.then(lastColor + namedata[i]);
                                    fm.tooltip(hover);
                                    fm.then(i == namedata.length - 1 ? "" : " ");
                                    fm.tooltip(hover);
                                }
                                fm.tooltip(hover);
                            }
                        }
                    }
                }
                if (!itemLink) {
                    fm.then(tmp + (a == data.length - 1 ? "" : " "));
                    fm.color(playerChatColor);
                    if (tmp.startsWith("http://") || tmp.startsWith("https://") || tmp.endsWith(".net") || tmp.endsWith(".com") || tmp.endsWith("org")) {
                        if (!tmp.startsWith("http://") && !tmp.startsWith("https://"))
                            tmp = "http://" + tmp;
                        fm.link(tmp);
                    }
                } else {
                    if (a != data.length - 1)
                        fm.then(" ");
                }
            }
        }

        // Make sure the vanilla message isn't sent
        String message = event.getMessage();
        event.setFormat("");
        event.setCancelled(true);

        // Show in console
        Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + ChatColor.stripColor(fm_unfiltered.toOldMessageFormat()));

        // Check mentions
        ArrayList<String> finalMentioned = new ArrayList<String>();
        if (pd.check(Rank.MOD) && (event.getMessage().contains("@everyone"))) {
            for (Player p : plugin.getServer().getOnlinePlayers()) {
                RSound.playSound(p, Sound.ENTITY_EXPERIENCE_ORB_PICKUP);
                p.sendMessage(ChatColor.GRAY + "> " + event.getPlayer().getName() + " just mentioned everyone.");
            }
        } else if (message.contains("@")) {
            ArrayList<String> mentioned = new ArrayList<String>();
            String[] split = message.split(" ");
            for (int k = 0; k < split.length; k++) {
                String s = split[k];
                if (s.startsWith("@")) {
                    if (s.length() > 1) {
                        mentioned.add(s.substring(1));
                    } else if (s.length() == 1 && k + 1 < split.length) {
                        mentioned.add(split[k + 1]);
                    }
                }
            }
            boolean mentionedSomeone = false;
            for (String s : mentioned) {
                Player p = plugin.getServer().getPlayerExact(s);
                if (p != null && p.isValid() && p.isOnline()) {
                    ChatMention cm = new ChatMention(event.getPlayer().getName(), s);
                    if (mentions.containsKey(cm)) {
                        if (System.currentTimeMillis() - mentions.get(cm) < 60000) {
                            event.getPlayer().sendMessage(ChatColor.RED + "> You already mentioned " + p.getName() + " in the past minute!");
                            continue;
                        }
                    }
                    event.getPlayer().sendMessage(ChatColor.GREEN + "> You mentioned " + p.getName() + ".");
                    mentions.put(cm, System.currentTimeMillis());
                    finalMentioned.add(p.getName());
                    if (!ChatManager.shadowMute.contains(event.getPlayer().getName())) {
                        RSound.playSound(p, Sound.ENTITY_EXPERIENCE_ORB_PICKUP);
                        p.sendMessage(ChatColor.GRAY + "> Your name was just mentioned by " + event.getPlayer().getName() + ".");
                    }
                    mentionedSomeone = true;
                } else {
                    event.getPlayer().sendMessage(ChatColor.RED + "> Mentioned player '" + s + "' is not online.");
                }
            }
            if (mentionedSomeone)
                RSound.playSound(event.getPlayer(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP);
        }
        // Make sure not shadowmuted, then send the actual message to everyone
        Location talkerLoc = pd.getPlayer().getLocation();
        boolean messaged = false;
        FancyMessage fm_filtered_mention = null, fm_unfiltered_mention = null;
        if (!ChatManager.shadowMute.contains(event.getPlayer().getName())) {
            for (Player p : plugin.getServer().getOnlinePlayers()) {
                if (plugin.getPD(p) != null) {
                    PlayerDataRPG pd2 = plugin.getPD(p);
                    if (pd2.isIgnoring(pd))
                        continue;
                    if (!global && pd2.getOption(SakiOption.LOCAL_CHAT) && RMath.flatDistance(p.getLocation(), talkerLoc) > 50) {
                        continue;
                    }
                    if (global && !pd2.getOption(SakiOption.GLOBAL_CHAT)) {
                        continue;
                    }
                    if (pd2 != pd && pd2.getOption(SakiOption.LOCAL_CHAT))
                        messaged = true;
                    if (finalMentioned.contains(p.getName())) {
                        if (fm_filtered_mention == null) {
                            try {
                                fm_filtered_mention = fm_filtered.clone().color(ChatColor.YELLOW);
                            } catch (CloneNotSupportedException e) {
                                e.printStackTrace();
                            }
                        }
                        if (fm_unfiltered_mention == null) {
                            try {
                                fm_unfiltered_mention = fm_unfiltered.clone().color(ChatColor.YELLOW);
                            } catch (CloneNotSupportedException e) {
                                e.printStackTrace();
                            }
                        }
                        (plugin.getPD(p).getOption(SakiOption.CHAT_FILTER) ? fm_filtered_mention : fm_unfiltered_mention).send(p);
                    } else {
                        (plugin.getPD(p).getOption(SakiOption.CHAT_FILTER) ? fm_filtered : fm_unfiltered).send(p);
                    }
                }
            }
        } else {
            messaged = true;
            (pd.getOption(SakiOption.CHAT_FILTER) ? fm_filtered : fm_unfiltered).send(event.getPlayer());
        }
        if (!messaged && !global) {
            pd.sendMessage(ChatColor.RED + "> There was no one nearby to see your chat message...");
            if (pd.getOption(SakiOption.LOCAL_CHAT_WARNING)) {
                pd.sendMessage("");
                pd.sendMessage(ChatColor.GRAY + "> You can talk in " + ChatColor.AQUA + "Global Chat " + ChatColor.GRAY + "by starting your message with a " + ChatColor.YELLOW + "#");
                pd.sendMessage(ChatColor.GRAY + "> Like this: " + ChatColor.YELLOW + "#Hi everyone!");
                pd.sendMessage(ChatColor.GRAY + "> Turn off this message in /options (\"Local Chat Warning\").");
            }
        }
        // Rensa commands
        String lower = event.getMessage().toLowerCase();
        if (lower.startsWith("!discord")) {
            if (System.currentTimeMillis() - lastDiscord > 30000) {
                fm_unfiltered = new FancyMessage();
                String link = "http://zentrela.net/discord";
                fm_unfiltered.text("[0] ").color(ChatColor.GRAY).link(link);
                fm_unfiltered.then("Bot ").color(ChatColor.AQUA).style(ChatColor.BOLD).link(link);
                fm_unfiltered.then("Rensa: ").color(ChatColor.WHITE).link(link);
                fm_unfiltered.then("Join the Discord chat! Click for more info.").color(ChatColor.YELLOW).link(link);
                fm_unfiltered.send(plugin.getServer().getOnlinePlayers());
                lastDiscord = System.currentTimeMillis();
            } else {
                event.getPlayer().sendMessage(ChatColor.RED + "The Discord link was requested less than 30 seconds ago. Scroll up in chat to find it!");
            }
        } else if (lower.startsWith("!map")) {
            if (System.currentTimeMillis() - lastMap > 30000) {
                fm_unfiltered = new FancyMessage();
                String link = "http://zentrela.net/map/";
                fm_unfiltered.text("[0] ").color(ChatColor.GRAY).link(link);
                fm_unfiltered.then("Bot ").color(ChatColor.AQUA).style(ChatColor.BOLD).link(link);
                fm_unfiltered.then("Rensa: ").color(ChatColor.WHITE).link(link);
                fm_unfiltered.then("Check out the Zentrela map! Click for a link.").color(ChatColor.YELLOW).link(link);
                fm_unfiltered.send(plugin.getServer().getOnlinePlayers());
                lastMap = System.currentTimeMillis();
            } else {
                event.getPlayer().sendMessage(ChatColor.RED + "The Map link was requested less than 30 seconds ago. Scroll up in chat to find it!");
            }
        } else if (lower.startsWith("!twitter")) {
            if (System.currentTimeMillis() - lastTwitter > 30000) {
                fm_unfiltered = new FancyMessage();
                String link = "https://twitter.com/Zentrela";
                fm_unfiltered.text("[0] ").color(ChatColor.GRAY).link(link);
                fm_unfiltered.then("Bot ").color(ChatColor.AQUA).style(ChatColor.BOLD).link(link);
                fm_unfiltered.then("Rensa: ").color(ChatColor.WHITE).link(link);
                fm_unfiltered.then("Follow Zentrela on Twitter!.").color(ChatColor.YELLOW).link(link);
                fm_unfiltered.send(plugin.getServer().getOnlinePlayers());
                lastTwitter = System.currentTimeMillis();
            } else {
                event.getPlayer().sendMessage(ChatColor.RED + "The Twitter link was requested less than 30 seconds ago. Scroll up in chat to find it!");
            }
        } else if (lower.startsWith("!bully")) {
            if (System.currentTimeMillis() - lastBully > 30000) {
                fm_unfiltered = new FancyMessage();
                String link = "https://twitter.com/antibullyranger";
                fm_unfiltered.text("[0] ").color(ChatColor.GRAY).link(link);
                fm_unfiltered.then("Bot ").color(ChatColor.AQUA).style(ChatColor.BOLD).link(link);
                fm_unfiltered.then("Rensa: ").color(ChatColor.WHITE).link(link);
                fm_unfiltered.then("Hey! No bullying.").color(ChatColor.YELLOW).link(link);
                fm_unfiltered.send(plugin.getServer().getOnlinePlayers());
                lastBully = System.currentTimeMillis();
            } else {
                event.getPlayer().sendMessage(ChatColor.RED + "The bully link was requested less than 30 seconds ago. Scroll up in chat to find it!");
            }
        } else if (lower.startsWith("!forum")) {
            if (System.currentTimeMillis() - lastForums > 30000) {
                fm_unfiltered = new FancyMessage();
                String link = "http://zentrela.net/forums/";
                fm_unfiltered.text("[0] ").color(ChatColor.GRAY).link(link);
                fm_unfiltered.then("Bot ").color(ChatColor.AQUA).style(ChatColor.BOLD).link(link);
                fm_unfiltered.then("Rensa: ").color(ChatColor.WHITE).link(link);
                fm_unfiltered.then("Click for a link to the Zentrela forums!").color(ChatColor.YELLOW).link(link);
                fm_unfiltered.send(plugin.getServer().getOnlinePlayers());
                lastForums = System.currentTimeMillis();
            } else {
                event.getPlayer().sendMessage(ChatColor.RED + "The forums link was requested less than 30 seconds ago. Scroll up in chat to find it!");
            }
        } else if (lower.startsWith("!staff")) {
            if (System.currentTimeMillis() - lastStaff > 30000) {
                fm_unfiltered = new FancyMessage();
                String link = "http://zentrela.net/staff.html";
                fm_unfiltered.text("[0] ").color(ChatColor.GRAY).link(link);
                fm_unfiltered.then("Bot ").color(ChatColor.AQUA).style(ChatColor.BOLD).link(link);
                fm_unfiltered.then("Rensa: ").color(ChatColor.WHITE).link(link);
                fm_unfiltered.then("Click for a link to the Staff Handbook!").color(ChatColor.YELLOW).link(link);
                fm_unfiltered.send(plugin.getServer().getOnlinePlayers());
                lastStaff = System.currentTimeMillis();
            } else {
                event.getPlayer().sendMessage(ChatColor.RED + "The staff handbook link was requested less than 30 seconds ago. Scroll up in chat to find it!");
            }
        } else if (lower.startsWith("!tip")) {
            if (System.currentTimeMillis() - lastTip > 15000) {
                TipManager.sayTip();
                lastTip = System.currentTimeMillis();
            } else {
                event.getPlayer().sendMessage(ChatColor.RED + "A tip was requested less than 15 seconds ago.");
            }
        } else if (lower.startsWith("!ip")) {
            if (System.currentTimeMillis() - lastIP > 30000) {
                fm_unfiltered = new FancyMessage();
                String link = "play.zentrela.net";
                fm_unfiltered.text("[0] ").color(ChatColor.GRAY).link(link);
                fm_unfiltered.then("Bot ").color(ChatColor.AQUA).style(ChatColor.BOLD).link(link);
                fm_unfiltered.then("Rensa: ").color(ChatColor.WHITE).link(link);
                fm_unfiltered.then("The server IP is play.zentrela.net!").color(ChatColor.YELLOW).link(link);
                fm_unfiltered.send(plugin.getServer().getOnlinePlayers());
                lastIP = System.currentTimeMillis();
            } else {
                event.getPlayer().sendMessage(ChatColor.RED + "The server IP was requested less than 30 seconds ago. Scroll up in chat to find it!");
            }
        } else if (lower.startsWith("!store") || lower.startsWith("!crates")) {
            if (System.currentTimeMillis() - lastStore > 30000) {
                fm_unfiltered = new FancyMessage();
                String link = "http://store.zentrela.net/";
                fm_unfiltered.text("[0] ").color(ChatColor.GRAY).link(link);
                fm_unfiltered.then("Bot ").color(ChatColor.AQUA).style(ChatColor.BOLD).link(link);
                fm_unfiltered.then("Rensa: ").color(ChatColor.WHITE).link(link);
                fm_unfiltered.then("Click for a link to the Zentrela Store!").color(ChatColor.YELLOW).link(link);
                fm_unfiltered.send(plugin.getServer().getOnlinePlayers());
                lastStore = System.currentTimeMillis();
            } else {
                event.getPlayer().sendMessage(ChatColor.RED + "The store link was requested less than 30 seconds ago. Scroll up in chat to find it!");
            }
        } else if (lower.startsWith("!wiki")) {
            if (System.currentTimeMillis() - lastWiki > 30000) {
                fm_unfiltered = new FancyMessage();
                String link = "http://zentrela.wikia.com/";
                fm_unfiltered.text("[0] ").color(ChatColor.GRAY).link(link);
                fm_unfiltered.then("Bot ").color(ChatColor.AQUA).style(ChatColor.BOLD).link(link);
                fm_unfiltered.then("Rensa: ").color(ChatColor.WHITE).link(link);
                fm_unfiltered.then("Click for a link to the Zentrela Wiki!").color(ChatColor.YELLOW).link(link);
                fm_unfiltered.send(plugin.getServer().getOnlinePlayers());
                lastWiki = System.currentTimeMillis();
            } else {
                event.getPlayer().sendMessage(ChatColor.RED + "The wiki link was requested less than 30 seconds ago. Scroll up in chat to find it!");
            }
        } else if (lower.startsWith("!website") || lower.startsWith("!site")) {
            if (System.currentTimeMillis() - lastWebsite > 30000) {
                fm_unfiltered = new FancyMessage();
                String link = "http://www.zentrela.net/";
                fm_unfiltered.text("[0] ").color(ChatColor.GRAY).link(link);
                fm_unfiltered.then("Bot ").color(ChatColor.AQUA).style(ChatColor.BOLD).link(link);
                fm_unfiltered.then("Rensa: ").color(ChatColor.WHITE).link(link);
                fm_unfiltered.then("Click for a link to the Zentrela Website!").color(ChatColor.YELLOW).link(link);
                fm_unfiltered.send(plugin.getServer().getOnlinePlayers());
                lastWebsite = System.currentTimeMillis();
            } else {
                event.getPlayer().sendMessage(ChatColor.RED + "The website link was requested less than 30 seconds ago. Scroll up in chat to find it!");
            }
        } else if (lower.startsWith("!update") || lower.startsWith("!patch")) {
            if (System.currentTimeMillis() - lastPatch > 30000) {
                fm_unfiltered = new FancyMessage();
                String link = "http://www.zentrela.net/p";
                fm_unfiltered.text("[0] ").color(ChatColor.GRAY).link(link);
                fm_unfiltered.then("Bot ").color(ChatColor.AQUA).style(ChatColor.BOLD).link(link);
                fm_unfiltered.then("Rensa: ").color(ChatColor.WHITE).link(link);
                fm_unfiltered.then("Click for a link to the Zentrela Patch Notes!").color(ChatColor.YELLOW).link(link);
                fm_unfiltered.send(plugin.getServer().getOnlinePlayers());
                lastPatch = System.currentTimeMillis();
            } else {
                event.getPlayer().sendMessage(ChatColor.RED + "The patch notes link was requested less than 30 seconds ago. Scroll up in chat to find it!");
            }
        } else if (lower.startsWith("!help")) {
            if (System.currentTimeMillis() - lastHelp > 30000) {
                fm_unfiltered = new FancyMessage();
                ArrayList<String> tt = new ArrayList<String>();
                tt.add(ChatColor.AQUA + ChatColor.BOLD.toString() + "Rensa's Chat Commands");
                tt.add(ChatColor.GREEN + " !help " + ChatColor.GRAY + "- " + ChatColor.DARK_AQUA + "View all chat commands");
                tt.add(ChatColor.GREEN + " !discord " + ChatColor.GRAY + "- " + ChatColor.DARK_AQUA + "Link to the Discord chat");
                tt.add(ChatColor.GREEN + " !map " + ChatColor.GRAY + "- " + ChatColor.DARK_AQUA + "Link to the Online Map");
                tt.add(ChatColor.GREEN + " !twitter " + ChatColor.GRAY + "- " + ChatColor.DARK_AQUA + "Link to the Official Twitter");
                tt.add(ChatColor.GREEN + " !bully " + ChatColor.GRAY + "- " + ChatColor.DARK_AQUA + "Call the Anti-bully Ranger");
                tt.add(ChatColor.GREEN + " !forums " + ChatColor.GRAY + "- " + ChatColor.DARK_AQUA + "Link to the Forums");
                tt.add(ChatColor.GREEN + " !staff " + ChatColor.GRAY + "- " + ChatColor.DARK_AQUA + "Link to the Staff Handbook");
                tt.add(ChatColor.GREEN + " !tip " + ChatColor.GRAY + "- " + ChatColor.DARK_AQUA + "Get a random tip");
                tt.add(ChatColor.GREEN + " !ip " + ChatColor.GRAY + "- " + ChatColor.DARK_AQUA + "Get the server IP");
                tt.add(ChatColor.GREEN + " !wiki " + ChatColor.GRAY + "- " + ChatColor.DARK_AQUA + "Link to the Zentrela Wiki");
                tt.add(ChatColor.GREEN + " !store " + ChatColor.GRAY + "- " + ChatColor.DARK_AQUA + "Link to the Zentrela Store");
                tt.add(ChatColor.GREEN + " !website " + ChatColor.GRAY + "- " + ChatColor.DARK_AQUA + "Link to the Zentrela Website");
                tt.add(ChatColor.GREEN + " !updates " + ChatColor.GRAY + "- " + ChatColor.DARK_AQUA + "Link to the Zentrela Patch Notes");
                tt.add("");
                tt.add(ChatColor.GRAY + "\"Chat Commands\" start with !");
                tt.add(ChatColor.GRAY + "\"Commands\" start with /");
                fm_unfiltered.text("[0] ").color(ChatColor.GRAY).tooltip(tt);
                fm_unfiltered.then("Bot ").color(ChatColor.AQUA).style(ChatColor.BOLD).tooltip(tt);
                fm_unfiltered.then("Rensa: ").color(ChatColor.WHITE).tooltip(tt);
                fm_unfiltered.then("Hover over this message for a list of chat commands!").color(ChatColor.GOLD).tooltip(tt);
                fm_unfiltered.send(plugin.getServer().getOnlinePlayers());
                lastHelp = System.currentTimeMillis();
            } else {
                event.getPlayer().sendMessage(ChatColor.RED + "The chat commands list was requested less than 30 seconds ago.");
            }
        }
    }

}

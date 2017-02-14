package com.edasaki.rpg.commands.member;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.edasaki.core.utils.fanciful.FancyMessage;
import com.edasaki.rpg.PlayerDataRPG;
import com.edasaki.rpg.commands.RPGAbstractCommand;

public class InfoCommand extends RPGAbstractCommand {

    public InfoCommand(String... commandNames) {
        super(commandNames);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
    }

    @Override
    public void executePlayer(Player p, PlayerDataRPG pd, String[] args) {
        if (args.length == 1) {
            Player p2 = plugin.getServer().getPlayer(args[0]);
            if (p2 == null || !p2.isOnline()) {
                p.sendMessage(ChatColor.RED + "Could not find player " + args[0] + ".");
                return;
            }
            PlayerDataRPG pd2 = plugin.getPD(p2);
            if (pd2 == null || pd2.getPlayer() == null || !pd2.getPlayer().isOnline()) {
                p.sendMessage(ChatColor.RED + "Could not find player " + args[0] + ".");
                return;
            }
            p.sendMessage("");
            p.sendMessage(ChatColor.DARK_AQUA + "== " + ChatColor.AQUA + "Player Information" + ChatColor.DARK_AQUA + " ==");
            p.sendMessage(pd2.getChatRankPrefix() + pd2.getPlayer().getName() + ChatColor.GRAY + " - " + ChatColor.GOLD + "Level " + pd2.level + ChatColor.GRAY + " - " + ChatColor.GREEN + pd2.classType);
            //            p.sendMessage(ChatColor.DARK_AQUA + "== " + ChatColor.AQUA + "Equipment [Hover for Stats]" + ChatColor.DARK_AQUA + " ==");
            //            boolean displayed = false;
            //            displayed = display(pd2.getPlayer().getEquipment().getItemInMainHand(), p);
            //            ItemStack[] list = pd2.getPlayer().getEquipment().getArmorContents();
            //            for (int k = list.length - 1; k >= 0; k--) {
            //                if (display(list[k], p))
            //                    displayed = true;
            //            }
            //            if (!displayed)
            //                p.sendMessage(ChatColor.GRAY + ">> " + ChatColor.WHITE + "Nothing equipped!");
            //            p.sendMessage("");    
            if (pd2 != pd) {
                p.sendMessage("");
                FancyMessage fm = new FancyMessage();
                fm.text("Click here ").color(ChatColor.YELLOW).command("/party invite " + p2.getName());
                fm.then("to invite " + p2.getName() + " to your party!").color(ChatColor.GREEN).command("/party invite " + p2.getName());
                fm.send(p);
                p.sendMessage("");
                fm = new FancyMessage();
                fm.text("Click here ").color(ChatColor.YELLOW).command("/trade " + p2.getName());
                fm.then("to send a trade offer to " + p2.getName() + "!").color(ChatColor.GREEN).command("/trade " + p2.getName());
                fm.send(p);
                p.sendMessage("");
            }
        } else {
            p.sendMessage(ChatColor.RED + "Incorrect syntax! " + ChatColor.YELLOW + "/spy <name>");
        }
    }

    //    private boolean display(ItemStack item, Player p) {
    //        if (item == null || !item.hasItemMeta() || !item.getItemMeta().hasDisplayName())
    //            return false;
    //        ItemMeta im = item.getItemMeta();
    //        String name = item.getItemMeta().getDisplayName();
    //
    //        FancyMessage fm = new FancyMessage();
    //        fm.text(">> ").color(ChatColor.GRAY);
    //        fm.then(name);
    //        if (im.hasLore()) {
    //            ArrayList<String> list = new ArrayList<String>();
    //            list.add(name);
    //            list.addAll(im.getLore());
    //            fm.tooltip(list);
    //        }
    //        fm.send(p);
    //        return true;
    //    }

    @Override
    public void executeConsole(CommandSender sender, String[] args) {
    }

}

package com.edasaki.rpg.drops;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.entity.ItemMergeEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.edasaki.core.options.SakiOption;
import com.edasaki.core.utils.RMetadata;
import com.edasaki.core.utils.RTags;
import com.edasaki.rpg.AbstractManagerRPG;
import com.edasaki.rpg.PlayerDataRPG;
import com.edasaki.rpg.SakiRPG;
import com.edasaki.rpg.items.ItemBalance;

public class DropManager extends AbstractManagerRPG {

    private static HashMap<UUID, Long> itemMapTime = new HashMap<UUID, Long>();
    private static HashMap<UUID, UUID> itemMapOwner = new HashMap<UUID, UUID>();
    private static HashMap<UUID, Long> playerMapMessaged = new HashMap<UUID, Long>();
    private static HashMap<UUID, Entity> itemLabels = new HashMap<UUID, Entity>();

    public DropManager(SakiRPG plugin) {
        super(plugin);
    }

    @Override
    public void initialize() {
    }

    public Entity removeLabel(UUID uuid) {
        return itemLabels.getOrDefault(uuid, null);
    }

    public static Item dropItem(ItemStack item, Location loc, UUID priority) {
        Item i = loc.getWorld().dropItemNaturally(loc, item);
        if (priority != null) {
            UUID iuuid = i.getUniqueId();
            itemMapTime.put(iuuid, System.currentTimeMillis());
            itemMapOwner.put(iuuid, priority);
        }
        return i;
    }

    @EventHandler
    public void pickupItemEvent(PlayerPickupItemEvent event) {
        UUID iuuid = event.getItem().getUniqueId();
        if (itemMapTime.containsKey(iuuid)) {
            if (System.currentTimeMillis() - itemMapTime.get(iuuid) < 10000) {
                UUID owneruuid = itemMapOwner.get(iuuid);
                Player owner = plugin.getServer().getPlayer(owneruuid);
                PlayerDataRPG pdo = plugin.getPD(owner);
                PlayerDataRPG pd = plugin.getPD(event.getPlayer());
                if (owneruuid == null || owneruuid.equals(event.getPlayer().getUniqueId()) || (pdo != null && pd != null && pd.party != null && pd.party != pdo.party && pd.party.isLootshareActive())) {
                    itemMapTime.remove(iuuid);
                    itemMapOwner.remove(iuuid);
                    return; // all good
                } else {
                    event.setCancelled(true);
                    if (plugin.getPD(event.getPlayer()) != null && plugin.getPD(event.getPlayer()).getOption(SakiOption.ITEM_PROTECT)) {
                        if (playerMapMessaged.containsKey(event.getPlayer().getUniqueId()) && System.currentTimeMillis() - playerMapMessaged.get(event.getPlayer().getUniqueId()) < 5000) {
                            // dont spam msg
                        } else {
                            event.getPlayer().sendMessage(ChatColor.RED + " You can't pick up this item yet because of item protection!");
                            event.getPlayer().sendMessage(ChatColor.RED + " Monster drops are only lootable by the highest damage dealer for 10 seconds.");
                            event.getPlayer().sendMessage(ChatColor.RED + " Skill-related items are only lootable by the creator of the item for 10 seconds.");
                            playerMapMessaged.put(event.getPlayer().getUniqueId(), System.currentTimeMillis());
                        }
                    }
                }
            } else {
                itemMapTime.remove(iuuid);
                itemMapOwner.remove(iuuid);
            }
        }
    }

    public void attachLabel(Item item, String name) {
        ArmorStand as = RTags.makeStand(name, item.getLocation(), true);
        item.setPassenger(as);
        itemLabels.put(item.getUniqueId(), as);
    }

    public static void removeLabel(Item item) {
        if (itemLabels.containsKey(item.getUniqueId())) {
            Entity e = itemLabels.remove(item.getUniqueId());
            if (e != null)
                e.remove();
            e = null;
        }
    }

    @EventHandler
    public void onItemDrop(ItemSpawnEvent event) {
        removeLabel(event.getEntity());
        Item item = event.getEntity();
        ItemStack is = item.getItemStack();
        if (is == null)
            return;
        if (is.hasItemMeta()) {
            ItemMeta im = is.getItemMeta();
            if (im.hasDisplayName()) {

                //                if (im.getDisplayName().contains(ChatColor.YELLOW.toString()) || im.getDisplayName().contains(ChatColor.GOLD.toString()) || im.getDisplayName().contains(ChatColor.AQUA.toString())) {
                if (im.getDisplayName().contains(" of ") || (!im.getDisplayName().contains("Potion") && !im.getDisplayName().contains(ChatColor.GRAY.toString()))) {
                    attachLabel(item, im.getDisplayName());
                } else {
                    String name = im.getDisplayName();
                    boolean labeled = false;
                    for (String s : ItemBalance.RARITY_NAMES) {
                        if (s.length() == 0)
                            continue;
                        if (name.contains(s)) {
                            attachLabel(item, im.getDisplayName());
                            labeled = true;
                            break;
                        }
                    }
                    if (!labeled) {
                        for (String s : ItemBalance.SAGE_NAMES) {
                            if (s.length() == 0)
                                continue;
                            if (name.contains(s)) {
                                attachLabel(item, im.getDisplayName());
                                break;
                            }
                        }
                    }
                }
            }
        }

    }

    @EventHandler
    public void onItemDespawn(ItemDespawnEvent event) {
        if (event.getEntity().hasMetadata(RMetadata.META_NO_PICKUP)) {
            event.setCancelled(true);
            return;
        }
        removeLabel(event.getEntity());
    }

    @EventHandler
    public void onItemMerge(ItemMergeEvent event) {
        if (event.getEntity().hasMetadata(RMetadata.META_NO_PICKUP) || event.getTarget().hasMetadata(RMetadata.META_NO_PICKUP)) {
            event.setCancelled(true);
            return;
        }
        removeLabel(event.getEntity());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onItemPickup(PlayerPickupItemEvent event) {
        if (event.getItem().hasMetadata(RMetadata.META_NO_PICKUP)) {
            event.setCancelled(true);
            return;
        }
        if (event.getRemaining() == 0 && !event.isCancelled())
            removeLabel(event.getItem());
    }

}

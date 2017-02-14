package com.edasaki.rpg.npcs;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.world.ChunkLoadEvent;

import com.edasaki.core.utils.ChunkWrapper;
import com.edasaki.core.utils.RScheduler;
import com.edasaki.core.utils.RTicks;
import com.edasaki.rpg.AbstractManagerRPG;
import com.edasaki.rpg.SakiRPG;

public class NPCManager extends AbstractManagerRPG {

    public static HashMap<UUID, NPCEntity> npcs = new HashMap<UUID, NPCEntity>();
    private static HashMap<ChunkWrapper, HashSet<NPCEntity>> villagersPerChunk = new HashMap<ChunkWrapper, HashSet<NPCEntity>>();

    private HashMap<UUID, Long> lastClick = new HashMap<UUID, Long>();

    public NPCManager(SakiRPG plugin) {
        super(plugin);
    }

    @Override
    public void initialize() {
    }

    public static void register(NPCEntity vil) {
        ChunkWrapper cw = new ChunkWrapper(vil.getClonedLoc().getChunk());
        if (!villagersPerChunk.containsKey(cw)) {
            villagersPerChunk.put(cw, new HashSet<NPCEntity>());
        }
        villagersPerChunk.get(cw).add(vil);
        vil.spawn();
    }

    public static void unregister(NPCEntity vil) {
        try {
            vil.despawn();
        } catch (Exception e) {
            e.printStackTrace();
        }
        ChunkWrapper cw = new ChunkWrapper(vil.getClonedLoc().getChunk());
        if (villagersPerChunk.containsKey(cw)) {
            villagersPerChunk.get(cw).remove(vil);
        }
    }

    @EventHandler
    public void onEntityInteract(PlayerInteractEntityEvent event) {
        if (event.getRightClicked() != null && event.getRightClicked() instanceof Villager)
            event.setCancelled(true);
        if (event.getRightClicked() != null && npcs.containsKey(event.getRightClicked().getUniqueId())) {
            event.setCancelled(true);
            Player p = event.getPlayer();
            if (plugin.getPD(p) != null) {
                if (lastClick.containsKey(p.getUniqueId())) {
                    if (System.currentTimeMillis() - lastClick.get(p.getUniqueId()) < 333) {
                        return;
                    }
                }
                lastClick.put(p.getUniqueId(), System.currentTimeMillis());
                npcs.get(event.getRightClicked().getUniqueId()).interact(p, plugin.getPD(p));
            }
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            if (event.getEntity() != null && npcs.containsKey(event.getEntity().getUniqueId())) {
                event.setCancelled(true);
                Player p = (Player) (event.getDamager());
                if (plugin.getPD(p) != null) {
                    if (lastClick.containsKey(p.getUniqueId())) {
                        if (System.currentTimeMillis() - lastClick.get(p.getUniqueId()) < 333) {
                            return;
                        }
                    }
                    lastClick.put(p.getUniqueId(), System.currentTimeMillis());
                    npcs.get(event.getEntity().getUniqueId()).interact(p, plugin.getPD(p));
                }
            }
        }
    }

    @EventHandler
    public void onChunkLoad(ChunkLoadEvent event) {
        handleChunk(event.getChunk());
    }

    public static void handleChunk(Chunk chunk) {
        final ChunkWrapper cw = new ChunkWrapper(chunk);
        if (!villagersPerChunk.containsKey(cw))
            return;
        RScheduler.schedule(plugin, new Runnable() {
            public void run() {
                HashSet<NPCEntity> vils = villagersPerChunk.get(cw);
                for (NPCEntity v : vils)
                    v.register();
            }
        }, RTicks.seconds(2));
    }

}

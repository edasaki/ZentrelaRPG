package com.edasaki.rpg.general;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Item;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.entity.SlimeSplitEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.hanging.HangingBreakEvent.RemoveCause;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerShearEntityEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.weather.ThunderChangeEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.event.world.StructureGrowEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.util.Vector;

import com.edasaki.core.commands.mod.BackCommand;
import com.edasaki.core.players.Rank;
import com.edasaki.core.utils.RScheduler;
import com.edasaki.core.utils.RTicks;
import com.edasaki.rpg.AbstractManagerRPG;
import com.edasaki.rpg.PlayerDataRPG;
import com.edasaki.rpg.SakiRPG;
import com.edasaki.rpg.commands.builder.WorldEditCommand;
import com.edasaki.rpg.drops.DropManager;
import com.edasaki.rpg.mobs.MobData;
import com.edasaki.rpg.mobs.MobManager;
import com.edasaki.rpg.npcs.NPCManager;

public class EnvironmentManager extends AbstractManagerRPG {

    public static final String[] BUILD_WORLD = { SakiRPG.GAME_WORLD, SakiRPG.LOBBY_WORLD, SakiRPG.TUTORIAL_WORLD };

    public EnvironmentManager(SakiRPG plugin) {
        super(plugin);
    }

    @Override
    public void initialize() {
        RScheduler.scheduleRepeating(plugin, new Runnable() {
            public void run() {
                for (World w : plugin.getServer().getWorlds()) {
                    w.setTime(3000);
                }
            }
        }, RTicks.minutes(1));
    }

    @EventHandler
    public void onEntityChangeBlock(EntityChangeBlockEvent event) {
        if (event.getEntity() instanceof FallingBlock) {
            event.setCancelled(true);
        }
    }

    public static boolean canBuild(PlayerDataRPG pd) {
        if (!pd.isValid())
            return false;
        Player p = pd.getPlayer();
        World w = p.getWorld();
        for (String s : BUILD_WORLD)
            if (w.getName().equalsIgnoreCase(s) && !pd.check(Rank.BUILDER))
                return false;
        return true;
    }

    @EventHandler
    public void onEntityInteract(EntityInteractEvent event) {
        if (event.getBlock().getType() == Material.SOIL)
            event.setCancelled(true);
    }

    @EventHandler
    public void onShear(PlayerShearEntityEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onEatCake(PlayerInteractEvent event) {
        if (event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.CAKE_BLOCK) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onCraft(CraftItemEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        final Player p = event.getPlayer();
        for (String s : BUILD_WORLD) {
            if (!event.getFrom().getWorld().getName().equalsIgnoreCase(s) && event.getTo().getWorld().getName().equalsIgnoreCase(s)) {
                if (WorldEditCommand.worldedit_pa.containsKey(p.getUniqueId())) {
                    WorldEditCommand.worldedit_pa.remove(p.getUniqueId()).unsetPermission("worldedit.*");
                    p.sendMessage("Removed worldedit perms due to worldchange into build world.");
                }
            }
        }
        BackCommand.lastLoc.put(p.getName(), event.getFrom());
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        PlayerDataRPG pd = plugin.getPD(event.getPlayer());
        if (pd == null)
            event.setCancelled(true);
        if (event.getPlayer().getGameMode() != GameMode.CREATIVE)
            event.setCancelled(true);
        if (!canBuild(pd))
            event.setCancelled(true);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        PlayerDataRPG pd = plugin.getPD(event.getPlayer());
        if (pd == null)
            event.setCancelled(true);
        if (event.getPlayer().getGameMode() != GameMode.CREATIVE)
            event.setCancelled(true);
        if (!canBuild(pd))
            event.setCancelled(true);
        event.getPlayer().updateInventory();
    }

    @EventHandler
    public void onHangingBreak(HangingBreakEvent event) {
        if (event.getCause() == RemoveCause.DEFAULT || event.getCause() == RemoveCause.EXPLOSION)
            event.setCancelled(true);
    }

    @EventHandler
    public void onBucketEmpty(PlayerBucketEmptyEvent event) {
        PlayerDataRPG pd = plugin.getPD(event.getPlayer());
        if (pd == null)
            event.setCancelled(true);
        if (event.getPlayer().getGameMode() != GameMode.CREATIVE)
            event.setCancelled(true);
        if (!canBuild(pd))
            event.setCancelled(true);
        event.getPlayer().updateInventory();
    }

    @EventHandler
    public void onBucketFill(PlayerBucketFillEvent event) {
        PlayerDataRPG pd = plugin.getPD(event.getPlayer());
        if (pd == null)
            event.setCancelled(true);
        if (event.getPlayer().getGameMode() != GameMode.CREATIVE)
            event.setCancelled(true);
        if (!canBuild(pd))
            event.setCancelled(true);
        event.getPlayer().updateInventory();
    }

    @EventHandler
    public void onHangingBreak(HangingBreakByEntityEvent event) {
        if (!(event.getRemover() instanceof Player))
            event.setCancelled(true);
        PlayerDataRPG pd = plugin.getPD((Player) event.getRemover());
        if (pd == null)
            event.setCancelled(true);
        if (((Player) event.getRemover()).getGameMode() != GameMode.CREATIVE)
            event.setCancelled(true);
        if (!canBuild(pd))
            event.setCancelled(true);
    }

    @EventHandler
    public void onHangingPlace(HangingPlaceEvent event) {
        PlayerDataRPG pd = plugin.getPD(event.getPlayer());
        if (pd == null)
            event.setCancelled(true);
        if (event.getPlayer().getGameMode() != GameMode.CREATIVE)
            event.setCancelled(true);
        if (!canBuild(pd))
            event.setCancelled(true);
        event.getPlayer().updateInventory();
    }

    @EventHandler
    public void onItemFrameInteract(PlayerInteractEntityEvent event) {
        Player p = event.getPlayer();
        Entity e = event.getRightClicked();
        if (e instanceof ItemFrame && p.getGameMode() != GameMode.CREATIVE) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockGrow(BlockGrowEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onStructureGrow(StructureGrowEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerArmorStandManipulate(PlayerArmorStandManipulateEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockFlow(BlockFromToEvent event) {
        if (!event.getBlock().getWorld().getName().equals("flat") && !event.getBlock().getWorld().getName().contains("main") && !event.getBlock().getWorld().getName().equalsIgnoreCase(SakiRPG.GAME_WORLD))
            event.setCancelled(true);
    }

    @EventHandler
    public void onWeatherChange(WeatherChangeEvent event) {
        event.getWorld().setThunderDuration(0);
        event.getWorld().setWeatherDuration(0);
        event.getWorld().setThundering(false);
        event.setCancelled(true);
    }

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        if (event.getSpawnReason() != SpawnReason.CUSTOM) {
            event.getEntity().setHealth(0);
            event.getEntity().remove();
        }
    }

    @EventHandler
    public void onExpGain(PlayerExpChangeEvent event) {
        event.setAmount(0);
    }

    @EventHandler
    public void onRegainHealth(EntityRegainHealthEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onEntityCombust(EntityCombustEvent event) {
        event.getEntity().setFireTicks(0);
        event.setCancelled(true);
    }

    @EventHandler
    public void onItemDamage(EntityDamageEvent event) {
        if (event.getEntityType() == EntityType.DROPPED_ITEM && (event.getCause() == DamageCause.LAVA || event.getCause() == DamageCause.FIRE_TICK || event.getCause() == DamageCause.FIRE)) {
            Vector v = Vector.getRandom().normalize();
            v = v.setX(v.getX() - 0.5f);
            v = v.setZ(v.getZ() - 0.5f);
            v = v.multiply(0.35);
            if (v.getX() > 0 && v.getX() < 0.10)
                v.setX(0.10);
            if (v.getZ() > 0 && v.getZ() < 0.10)
                v.setZ(0.10);
            if (v.getX() < 0 && v.getX() > -0.10)
                v.setX(-0.10);
            if (v.getZ() < 0 && v.getZ() > -0.10)
                v.setZ(-0.10);
            v.setY(0.3);
            event.getEntity().setVelocity(v);
            event.setCancelled(true);
            RScheduler.schedule(plugin, () -> {
                event.getEntity().setFireTicks(0);
            }, RTicks.seconds(1));
        }
        if (event.getEntityType() == EntityType.ARMOR_STAND && (event.getCause() == DamageCause.LAVA || event.getCause() == DamageCause.FIRE_TICK || event.getCause() == DamageCause.FIRE)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerInteractCrops(PlayerInteractEvent event) {
        if (event.getAction() == Action.PHYSICAL) {
            Block b = event.getClickedBlock();
            if (b.getType() == Material.SOIL) {
                event.setCancelled(true);
                b.setTypeIdAndData(b.getType().getId(), b.getData(), true);
            }
            if (b.getType() == Material.CROPS) {
                event.setCancelled(true);
                b.setTypeIdAndData(b.getType().getId(), b.getData(), true);
            }
        }
    }

    @EventHandler
    public void hungerDecay(FoodLevelChangeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onThunderChange(ThunderChangeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onWorldLoad(WorldLoadEvent event) {
        World w = event.getWorld();
        if (w.hasStorm())
            w.setStorm(false);
        if (w.isThundering())
            w.setThundering(false);
    }

    @EventHandler
    public void onSlimeSplit(SlimeSplitEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockIgnite(BlockIgniteEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onChunkLoad(final ChunkLoadEvent event) {
        if (event.getWorld().getName().contains(SakiRPG.GAME_WORLD) && event.isNewChunk()) {
            event.getChunk().unload(false, false);
        } else {
            handleChunk(event.getChunk());
        }
    }

    public static void handleChunk(Chunk chunk) {
        RScheduler.schedule(plugin, new Runnable() {
            public void run() {
                if (chunk.isLoaded())
                    despawnEntities(chunk.getEntities());
            }
        }, 10);
    }

    @EventHandler
    public void onChunkUnload(ChunkUnloadEvent event) {
        despawnEntities(event.getChunk().getEntities());
    }

    public static void despawnEntities(Entity[] list) {
        for (Entity e : list) {
            if (e == null || e.getUniqueId() == null)
                continue;
            if (e instanceof Player)
                continue;
            try {
                if (MobManager.spawnedMobs != null && MobManager.spawnedMobs.containsKey(e.getUniqueId())) {
                    MobManager.spawnedMobs.get(e.getUniqueId()).despawn();
                }
                if (NPCManager.npcs != null && NPCManager.npcs.containsKey(e.getUniqueId())) {
                    NPCManager.npcs.remove(e.getUniqueId()).despawn();
                }
                if (e instanceof Item || e instanceof LivingEntity || e instanceof ArmorStand || e instanceof Projectile || e instanceof EnderCrystal) {
                    if (e instanceof LivingEntity)
                        ((LivingEntity) e).setHealth(0.0);
                    DropManager dm = plugin.getInstance(DropManager.class);
                    if (dm != null) {
                        Entity label = dm.removeLabel(e.getUniqueId());
                        if (label != null)
                            label.remove();
                        e.remove();
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

    }

    @EventHandler
    public void onBlockFade(BlockFadeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockSpread(BlockSpreadEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockForm(BlockFormEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onLeafDecay(LeavesDecayEvent event) {
        event.setCancelled(true);
    }

    private static final Material[] RESTRICTED_TYPES = {
            Material.ANVIL,
            Material.ENCHANTMENT_TABLE,
            Material.WORKBENCH,
            Material.DISPENSER,
            Material.FURNACE,
            Material.BREWING_STAND,
            Material.BURNING_FURNACE,
            Material.TRAPPED_CHEST,
            Material.HOPPER,
            Material.HOPPER_MINECART,
            Material.DROPPER,
            Material.CHEST,
            Material.ENDER_CHEST
    };

    @EventHandler
    public void onHopper(InventoryPickupItemEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onAnvilOrEnchantInteract(PlayerInteractEvent event) {
        if (event.getClickedBlock() != null) {
            if (event.getPlayer().getGameMode() == GameMode.CREATIVE && (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK))
                return;
            Material type = event.getClickedBlock().getType();
            for (Material m : RESTRICTED_TYPES) {
                if (m == type) {
                    event.setCancelled(true);
                    break;
                }
            }
        }
    }

    private HashMap<String, Long> lastVoid = new HashMap<String, Long>();

    @EventHandler
    public void onEntityDamageEvent(EntityDamageEvent event) {
        if (event.getCause() == DamageCause.VOID && event.getEntity() instanceof Player) {
            if (lastVoid.containsKey(((Player) event.getEntity()).getName()) && System.currentTimeMillis() - lastVoid.get(((Player) event.getEntity()).getName()) < 5000)
                return;
            ((Player) event.getEntity()).sendMessage(ChatColor.RED + "Woah! Careful there, pal!");
            event.getEntity().setFallDistance(0);
            event.getEntity().teleport(event.getEntity().getWorld().getSpawnLocation());
            event.getEntity().setFallDistance(0);
        } else if (event.getCause() == DamageCause.VOID && MobManager.spawnedMobs.get(event.getEntity().getUniqueId()) != null) {
            MobData md = MobManager.spawnedMobs.get(event.getEntity().getUniqueId());
            event.getEntity().teleport(md.ai.originalLoc);
        }
    }

    @EventHandler
    public void onItemSpawn(ItemSpawnEvent e) {
        if (e.getEntity().getItemStack().getType() == Material.EGG) {
            if (e.getEntity().getVelocity().getY() * 10 == 2) {
                e.getEntity().remove();
            }
        }
    }

}

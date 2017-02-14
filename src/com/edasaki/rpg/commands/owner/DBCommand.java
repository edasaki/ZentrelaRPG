package com.edasaki.rpg.commands.owner;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.codec.binary.Base64;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftArmorStand;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftSlime;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import com.edasaki.core.ManagerInstances;
import com.edasaki.core.unlocks.Unlock;
import com.edasaki.core.utils.ChunkWrapper;
import com.edasaki.core.utils.CustomArmorStand;
import com.edasaki.core.utils.REntities;
import com.edasaki.core.utils.RHead;
import com.edasaki.core.utils.RMath;
import com.edasaki.core.utils.RMessages;
import com.edasaki.core.utils.RMetadata;
import com.edasaki.core.utils.RParticles;
import com.edasaki.core.utils.RScheduler;
import com.edasaki.core.utils.RSound;
import com.edasaki.core.utils.RTags;
import com.edasaki.core.utils.RTicks;
import com.edasaki.core.utils.entities.CustomHorse;
import com.edasaki.core.utils.entities.CustomPig;
import com.edasaki.core.utils.entities.CustomRabbit;
import com.edasaki.core.utils.entities.CustomSheep;
import com.edasaki.core.utils.entities.CustomSlime;
import com.edasaki.core.utils.entities.CustomZombie;
import com.edasaki.core.utils.fanciful.FancyMessage;
import com.edasaki.core.utils.gson.RGson;
import com.edasaki.rpg.PlayerDataRPG;
import com.edasaki.rpg.buffs.BuffManager;
import com.edasaki.rpg.combat.DamageType;
import com.edasaki.rpg.commands.RPGAbstractCommand;
import com.edasaki.rpg.economy.ShardManager;
import com.edasaki.rpg.holograms.HologramManager;
import com.edasaki.rpg.items.ItemManager;
import com.edasaki.rpg.mobs.MobAttribute;
import com.edasaki.rpg.mobs.MobBalance;
import com.edasaki.rpg.mobs.MobData;
import com.edasaki.rpg.mobs.MobDrop;
import com.edasaki.rpg.mobs.MobManager;
import com.edasaki.rpg.mobs.MobType;
import com.edasaki.rpg.mobs.spells.MobSpell;
import com.edasaki.rpg.music.MusicManager;
import com.edasaki.rpg.npcs.generics.GenericNPCManager;
import com.edasaki.rpg.packets.PacketManager;
import com.edasaki.rpg.particles.EffectFactory;
import com.edasaki.rpg.particles.custom.misc.CustomWarpEffect;
import com.edasaki.rpg.particles.custom.spells.HweenPumpkinBombEffect;
import com.edasaki.rpg.quests.Quest;
import com.edasaki.rpg.quests.QuestManager;
import com.edasaki.rpg.skills.Skill;
import com.edasaki.rpg.skills.SkillManager;
import com.edasaki.rpg.spells.Spell;
import com.edasaki.rpg.utils.RSerializer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import de.slikey.effectlib.util.ParticleEffect;
import de.slikey.effectlib.util.ParticleEffect.BlockData;
import net.minecraft.server.v1_10_R1.AttributeInstance;
import net.minecraft.server.v1_10_R1.EntityInsentient;
import net.minecraft.server.v1_10_R1.EntityLiving;
import net.minecraft.server.v1_10_R1.EntityPlayer;
import net.minecraft.server.v1_10_R1.GenericAttributes;
import net.minecraft.server.v1_10_R1.PacketPlayOutUpdateTime;

public class DBCommand extends RPGAbstractCommand {

    public DBCommand(String commandName) {
        super(commandName);
    }

    private LivingEntity sheep = null;
    private CustomSheep cs = null;
    private Horse horse = null;
    private BossBar bar = null;

    @SuppressWarnings("unused")
    @Override
    public void executePlayer(final Player p, final PlayerDataRPG pd, String[] args) {
        int i = Integer.parseInt(args[0]);
        switch (i) {
            case 1:
                Location loc = p.getLocation().getBlock().getLocation().add(0.5, 0, 0.5);
                loc.getBlock().setType(Material.CHEST);
                loc.add(0, -0.2, 0);
                ArmorStand as = (ArmorStand) loc.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
                as.setCustomName("* Misaka's Shop *");
                as.setSmall(true);
                as.setGravity(false);
                as.setCustomNameVisible(true);
                as.setVisible(false);
                break;
            case 2:
                loc = p.getLocation().getBlock().getLocation().add(0.5, 0, 0.5);
                loc.getWorld().spawnEntity(loc, EntityType.VILLAGER);
                loc.add(0, -0.2, 0);
                as = (ArmorStand) loc.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
                as.setCustomName(ChatColor.RED + "Johnson");
                as.setSmall(true);
                as.setGravity(false);
                as.setCustomNameVisible(true);
                as.setVisible(false);
                break;
            case 3:
                pd.damage(Integer.parseInt(args[1]), p, DamageType.NORMAL);
                break;
            case 4:
                //				pd.getPlayer().getInventory().addItem(ItemManager.getTestWeapon());
                //				pd.getPlayer().getInventory().addItem(ItemManager.getTestArmor());
                break;
            case 5:
                LivingEntity le = REntities.createLivingEntity(CustomPig.class, p.getLocation());
                as = (ArmorStand) p.getWorld().spawnEntity(p.getLocation(), EntityType.ARMOR_STAND);
                as.setCustomName("Euna the Pig");
                as.setCustomNameVisible(true);
                as.setSmall(true);
                as.setGravity(false);
                as.setVisible(false);
                Slime slime = (Slime) p.getWorld().spawnEntity(p.getLocation(), EntityType.SLIME);
                slime.setSize(-2);
                slime.setPassenger(as);
                //				slime.addPotionEffect(effect)
                le.setPassenger(slime);
                break;
            case 6:
                le = REntities.createLivingEntity(CustomZombie.class, p.getLocation());
                as = (ArmorStand) p.getWorld().spawnEntity(p.getLocation(), EntityType.ARMOR_STAND);
                as.setCustomName(ChatColor.RED + "Misaka the Zombo");
                as.setCustomNameVisible(true);
                as.setSmall(true);
                as.setGravity(false);
                as.setVisible(false);
                slime = (Slime) p.getWorld().spawnEntity(p.getLocation(), EntityType.SLIME);
                slime.setSize(-1);
                slime.setPassenger(as);
                slime.addPotionEffect(PotionEffectType.INVISIBILITY.createEffect(Integer.MAX_VALUE, 0));
                le.setPassenger(slime);
                break;
            case 7:
                Set<Player> set = new HashSet<Player>();
                set.addAll(plugin.getServer().getOnlinePlayers());
                //                MidiUtil.playMidiQuietly(new File(plugin.getDataFolder().toPath() + "/music/" + args[1]), 1, set);
                break;
            case 8:
                Item item = p.getWorld().dropItem(p.getTargetBlock((Set<Material>) null, 50).getLocation(), new ItemStack(Material.GOLD_AXE));
                as = (ArmorStand) p.getWorld().spawnEntity(p.getLocation(), EntityType.ARMOR_STAND);
                if (Math.random() > 0.5)
                    as.setCustomName(ChatColor.GOLD + "Godlike");
                else
                    as.setCustomName(ChatColor.RED + "Supreme");
                as.setCustomNameVisible(true);
                as.setSmall(true);
                as.setGravity(false);
                as.setVisible(false);
                slime = (Slime) p.getWorld().spawnEntity(p.getLocation(), EntityType.SLIME);
                slime.setSize(-2);
                slime.setPassenger(as);
                slime.addPotionEffect(PotionEffectType.INVISIBILITY.createEffect(Integer.MAX_VALUE, 0));
                item.setPassenger(slime);
                break;
            case 9:
                FancyMessage fm = new FancyMessage("[test lol]").command("/information").tooltip(ChatColor.RED + "bon" + ChatColor.GOLD + "jour\ntest second line!").then(" and this part doesn't hover!");
                for (Player p2 : plugin.getServer().getOnlinePlayers())
                    fm.send(p2);
                break;
            case 10:
                //                VillagerData vd = VillagerData.createNPC("test npc lol", p.getLocation());
                break;
            case 11:
                le = REntities.createLivingEntity(CustomZombie.class, p.getLocation());
                as = (ArmorStand) p.getWorld().spawnEntity(p.getLocation(), EntityType.ARMOR_STAND);
                as.setSmall(true);
                as.setGravity(false);
                as.setVisible(false);
                as.setMarker(true);
                as.setCustomName(ChatColor.GOLD + "test");
                as.setCustomNameVisible(true);
                final Item itemEntity = le.getWorld().dropItem(as.getLocation(), new ItemStack(Material.STONE_BUTTON));
                itemEntity.setMetadata(RMetadata.META_NO_PICKUP, new FixedMetadataValue(Spell.plugin, 0));
                final Item itemEntity2 = le.getWorld().dropItem(as.getLocation(), new ItemStack(Material.WOOD_BUTTON));
                itemEntity2.setMetadata(RMetadata.META_NO_PICKUP, new FixedMetadataValue(Spell.plugin, 0));
                itemEntity.setPassenger(itemEntity2);
                itemEntity2.setPassenger(as);

                le.setPassenger(itemEntity);
                break;
            case 12:
                final Zombie z2 = (Zombie) p.getWorld().spawnEntity(p.getLocation(), EntityType.ZOMBIE);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        ((CraftEntity) z2).getHandle().setInvisible(true);
                    }
                }.runTaskLater(plugin, 10);
                break;
            case 13:
                le = REntities.createLivingEntity(CustomRabbit.class, p.getLocation());
                as = (ArmorStand) REntities.createLivingEntity(CustomArmorStand.class, p.getLocation());
                as.setSmall(true);
                as.setGravity(false);
                as.setVisible(false);
                as.setMarker(true);
                as.setCustomName(ChatColor.GOLD + "test");
                as.setCustomNameVisible(true);
                le.setPassenger(as);
                break;
            case 14:
                le = REntities.createLivingEntity(CustomZombie.class, p.getLocation());
                as = (ArmorStand) REntities.createLivingEntity(CustomArmorStand.class, p.getLocation());
                as.setSmall(true);
                //                as.setGravity(false);
                //                as.setVisible(false);
                //                as.setMarker(true);
                //                as.setMarker(true);
                slime = (Slime) p.getWorld().spawnEntity(p.getLocation(), EntityType.SLIME);
                slime.setSize(-1);
                slime.setPassenger(as);
                //                slime.addPotionEffect(PotionEffectType.INVISIBILITY.createEffect(Integer.MAX_VALUE, 0));
                as.setCustomName(ChatColor.GOLD + "test2");
                as.setCustomNameVisible(true);
                le.setPassenger(slime);
                break;
            case 15:
                le = REntities.createLivingEntity(CustomPig.class, p.getLocation());
                as = (ArmorStand) REntities.createLivingEntity(CustomArmorStand.class, p.getLocation());
                as.setSmall(true);
                //                as.setGravity(false);
                //                as.setVisible(false);
                //                as.setMarker(true);
                //                as.setMarker(true);
                slime = (Slime) p.getWorld().spawnEntity(p.getLocation(), EntityType.SLIME);
                slime.setSize(-1);
                slime.setPassenger(as);
                //                slime.addPotionEffect(PotionEffectType.INVISIBILITY.createEffect(Integer.MAX_VALUE, 0));
                as.setCustomName(ChatColor.GOLD + "test2");
                as.setCustomNameVisible(true);
                le.setPassenger(slime);
                break;
            case 16:
                /*
                loc = p.getLocation();
                net.minecraft.server.v1_8_R3.World world = ((CraftWorld) (loc.getWorld())).getHandle();
                CustomItem e = null;
                try {
                    e = CustomItem.class.getDeclaredConstructor(net.minecraft.server.v1_8_R3.World.class).newInstance(world);
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
                e.setPositionRotation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
                world.addEntity(e, SpawnReason.CUSTOM);
                ((Item) e.getBukkitEntity()).setMetadata(RMetadata.META_NO_PICKUP, new FixedMetadataValue(plugin, 0));
                */
                break;
            case 17:
                le = REntities.createLivingEntity(CustomZombie.class, p.getLocation());
                as = (ArmorStand) REntities.createLivingEntity(CustomArmorStand.class, p.getLocation());
                as.setSmall(true);
                as.setMarker(true);
                as.setGravity(false);
                as.setVisible(false);
                as.setCustomName(ChatColor.GOLD + "test2");
                as.setCustomNameVisible(true);
                slime = (Slime) REntities.createLivingEntity(CustomSlime.class, p.getLocation());
                ((CustomSlime) ((CraftSlime) slime).getHandle()).isTag = true;
                slime.setSize(1);
                slime.setPassenger(as);
                ((CustomSlime) ((CraftSlime) slime).getHandle()).setInvisible(true);
                le.setPassenger(slime);
                break;
            case 18:
                le = REntities.createLivingEntity(CustomRabbit.class, p.getLocation());
                as = (ArmorStand) REntities.createLivingEntity(CustomArmorStand.class, p.getLocation());
                as.setSmall(true);
                as.setMarker(true);
                as.setGravity(false);
                as.setVisible(false);
                as.setCustomName(ChatColor.GOLD + "test2");
                as.setCustomNameVisible(true);
                slime = (Slime) REntities.createLivingEntity(CustomSlime.class, p.getLocation());
                ((CustomSlime) ((CraftSlime) slime).getHandle()).isTag = true;
                slime.setSize(1);
                slime.setPassenger(as);
                ((CustomSlime) ((CraftSlime) slime).getHandle()).setInvisible(true);
                le.setPassenger(slime);
                break;
            case 19:
                le = REntities.createLivingEntity(CustomPig.class, p.getLocation());
                as = (ArmorStand) REntities.createLivingEntity(CustomArmorStand.class, p.getLocation());
                as.setSmall(true);
                as.setMarker(true);
                as.setGravity(false);
                as.setVisible(false);
                as.setCustomName(ChatColor.GOLD + "test2");
                as.setCustomNameVisible(true);
                slime = (Slime) REntities.createLivingEntity(CustomSlime.class, p.getLocation());
                ((CustomSlime) ((CraftSlime) slime).getHandle()).isTag = true;
                slime.setSize(1);
                slime.setPassenger(as);
                ((CustomSlime) ((CraftSlime) slime).getHandle()).setInvisible(true);
                le.setPassenger(slime);
                break;
            case 20:
                int[] base = { Integer.parseInt(args[1]), Integer.parseInt(args[2]) };
                RMessages.announce("");
                RMessages.announce("base: " + Arrays.toString(base));
                int half = (int) Math.ceil((base[1] - base[0]) / 2.0);
                RMessages.announce("half: " + half);
                RMessages.announce("lower range: " + base[0] + ", " + (base[0] + half));
                RMessages.announce("upper range: " + (base[0] + half) + ", " + base[1]);
                int noTier_low = RMath.randInt(base[0], base[0] + half);
                int noTier_high = RMath.randInt(base[0] + half, base[1]);
                RMessages.announce("generated: " + noTier_low + ", " + noTier_high);
                for (int rarity = 1; rarity <= 3; rarity++) {
                    int low_a = base[0] + rarity * half;
                    int low_b = low_a + half;
                    int high_a = base[0] + (rarity + 1) * half;
                    int high_b = high_a + half;
                    int low = RMath.randInt(low_a, low_b);
                    int high = RMath.randInt(high_a, high_b);
                    RMessages.announce("rarity " + rarity + " range: (" + low_a + ", " + low_b + ") - (" + high_a + ", " + high_b + ")");
                    RMessages.announce("generated rarity " + rarity + ": " + low + ", " + high);
                }
                break;
            case 21:
                //                ItemManager.reduceDurability(p.getItemInHand(), pd);
                break;
            case 22:
                //                ItemManager.setDurability(Integer.parseInt(args[1]), p.getItemInHand(), pd);
                break;
            case 23:
                ItemStack is = p.getItemInHand();
                ItemMeta im = is.getItemMeta();
                //                Class<?> craftmetaitem = 
                break;
            case 24:
                ShardManager.giveCurrency(p, Integer.parseInt(args[1]));
                break;
            case 25:
                //                ShardManager.giveCubes(p, Integer.parseInt(args[1]));
                break;
            case 26:
                //                if (ShardManager.isShard(p.getItemInHand()))
                //                    p.sendMessage("is shard");
                //                else
                //                    p.sendMessage("not shard");
                break;
            case 27:
                //                if (ShardManager.isCube(p.getItemInHand()))
                //                    p.sendMessage("is cube");
                //                else
                //                    p.sendMessage("not cube");
                break;
            case 28:
                p.sendMessage("count: " + ShardManager.countCurrency(p));
                break;
            case 29:
                ShardManager.giveCurrency(p, Integer.parseInt(args[1]));
                break;
            case 30:
                ShardManager.takeCurrency(p, Integer.parseInt(args[1]));
                break;
            case 31:
                for (Entity e : p.getNearbyEntities(20, 20, 20))
                    RParticles.spawnRandomFirework(e.getLocation());
                break;
            case 32:
                //                WorldBossManager.spawnBoss(p.getTargetBlock((HashSet<Byte>) null, 100).getLocation().add(0, 1.25, 0), Integer.parseInt(args[1]));
                break;
            case 33:
                p.sendMessage("Starting TPS test.");
                final long curr = System.currentTimeMillis();
                RScheduler.schedule(plugin, new Runnable() {
                    public void run() {
                        pd.sendMessage("Done: " + (System.currentTimeMillis() - curr));
                    }
                }, 21);
                break;
            case 34:
                //                for (Region r : RegionManager.regions)
                //                    RMessages.announce(r.toString());
                break;
            case 35:
                String name = "test_mob";
                MobType mt = MobManager.mobTypes.get(name);
                mt.spawn(p.getTargetBlock((HashSet<Byte>) null, 100).getLocation().add(0, -3, 0));
                break;
            case 36:
                for (Quest q : QuestManager.activeQuestList)
                    RMessages.announce(q.toString());
                break;
            case 37:
                pd.questProgress.clear();
                p.sendMessage("Cleared quest progress.");
                break;
            case 38:
                pd.questProgress.put(args[1], Integer.parseInt(args[2]));
                p.sendMessage("Updated quest progress: " + pd.questProgress);
                break;
            case 39:
//                WorldBossManager.spawnBoss(WorldBossManager.GENERATORS[Integer.parseInt(args[1])], p.getTargetBlock((HashSet<Byte>) null, 100).getLocation().add(0, 1.25, 0), Integer.parseInt(args[2]));
                break;
            case 40:
                sheep = REntities.createLivingEntity(CustomSheep.class, p.getLocation());
                cs = (CustomSheep) ((CraftLivingEntity) sheep).getHandle();
                break;
            case 41:
                if (sheep != null) {
                    p.sendMessage("set velocity of sheep");
                    Vector v = p.getLocation().subtract(sheep.getLocation()).toVector().normalize().multiply(0.3);
                    cs.customX = v.getX();
                    cs.customY = v.getY();
                    cs.customZ = v.getZ();
                }
                break;
            case 42:
                horse = (Horse) REntities.createLivingEntity(CustomHorse.class, p.getLocation());
                p.sendMessage("Spawned horse");
                break;
            case 43:
                if (horse != null) {
                    net.minecraft.server.v1_10_R1.Entity temph = ((CraftEntity) horse).getHandle();
                    ((CustomHorse) temph).rearUp();
                }
                break;
            case 44:
                Player target = plugin.getServer().getPlayer(args[1]);
                if (target == null || !target.isOnline()) {
                    p.sendMessage(target.getName() + " is not online.");
                    break;
                }
                EntityPlayer nmsPlayer = ((CraftPlayer) target).getHandle();
                Location location = target.getLocation();
                nmsPlayer.playerConnection.sendPacket(new PacketPlayOutUpdateTime(24000, Integer.parseInt(args[2]), true));
                RMessages.announce("Sent packet for time " + args[2] + " to " + target);
                break;
            case 45:
                target = plugin.getServer().getPlayer(args[1]);
                if (target == null || !target.isOnline()) {
                    p.sendMessage(target.getName() + " is not online.");
                    break;
                }
                if (PacketManager.protocol.hasInjected(target)) {
                    PacketManager.protocol.uninjectPlayer(target);
                    p.sendMessage(ChatColor.YELLOW + "Player " + target + " has been uninjected.");
                } else {
                    PacketManager.protocol.injectPlayer(target);
                    p.sendMessage(ChatColor.DARK_GREEN + "Player " + target + " has been injected.");
                }
                break;
            case 46:
                target = plugin.getServer().getPlayer(args[1]);
                if (target == null || !target.isOnline()) {
                    p.sendMessage(target.getName() + " is not online.");
                    break;
                }
                PacketManager.registerTime(target, Long.parseLong(args[2]));
                p.sendMessage("Stored next time for " + target.getName());
                break;
            case 47:
                RMessages.announce("Mob Spawns: " + MobManager.spawns.toString());
                break;
            case 48:
                BlockData data48 = new BlockData(Material.CLAY, (byte) 4);
                RParticles.showWithDataAndOffset(ParticleEffect.BLOCK_DUST, p.getEyeLocation().add(p.getLocation().getDirection().normalize().multiply(0.2)), data48, 10, 2, 0, 2);
                break;
            case 49:
                BuffManager.createPowerup(p.getLocation());
                break;
            case 50:
                //                BossBarAPI.setMessage(p, args[1], Float.parseFloat(args[2]));
                break;
            case 51:
                le = REntities.createLivingEntity(CustomZombie.class, p.getLocation());
                as = RTags.makeStand("test line 1", le.getLocation(), Boolean.parseBoolean(args[1]));
                ArmorStand as2 = RTags.makeStand("test line 2", le.getLocation(), Boolean.parseBoolean(args[2]));
                //                ArmorStand as_hp = RTags.makeStand("[||||| -1 |||||]", loc, true);
                //                ArmorStand as_name = RTags.makeStand(displayName, loc, false);
                //                slime = RTags.makeSlime(Integer.parseInt(args[3]), le.getLocation());
                //                Slime slime2 = RTags.makeSlime(Integer.parseInt(args[4]), le.getLocation());

                //hp riding below
                //                slime2.setPassenger(as2);
                //                as.setPassenger(slime2);
                //                slime.setPassenger(as);
                //                le.setPassenger(slime);
                break;
            case 52:
                RMessages.announce(p.getLocation().getBlock().getType().toString());
                break;
            case 53:
                le = REntities.createLivingEntity(CustomZombie.class, p.getLocation());
                le.setCustomName("test nigga");
                le.setCustomNameVisible(true);
                break;
            case 54:
                if (bar != null) {
                    bar.removeAll();
                }
                BarColor color = BarColor.values()[(int) (Math.random() * BarColor.values().length)];
                RMessages.announce("BarColor: " + color);
                BarStyle style = BarStyle.values()[(int) (Math.random() * BarStyle.values().length)];
                RMessages.announce("BarStyle: " + style);
                bar = Bukkit.createBossBar(args[0], color, style);
                bar.setProgress(Math.random());
                StringBuilder sb = new StringBuilder();
                for (int k = 1; k < args.length; k++) {
                    sb.append(args[k]);
                    sb.append(' ');
                }
                if (args.length > 1) {
                    bar.setTitle(sb.toString().trim());
                }
                for (Player p2 : plugin.getServer().getOnlinePlayers()) {
                    bar.addPlayer(p2);
                }
                break;
            case 55:
                if (bar != null)
                    bar.removeAll();
                break;
            case 56:
                RParticles.showWithOffsetPositiveY(ParticleEffect.SLIME, p.getLocation(), Double.parseDouble(args[1]), Integer.parseInt(args[2]));
                break;
            case 57:
                RParticles.showWithOffsetPositiveY(ParticleEffect.FLAME, p.getLocation(), Double.parseDouble(args[1]), Integer.parseInt(args[2]));
                break;
            case 58:
                RMessages.announce(p.getEquipment().getItemInMainHand().toString());
                break;
            case 59:
                le = REntities.createLivingEntity(CustomZombie.class, p.getLocation());
                le.setCustomName("Pretend Merchant");
                le.setCustomNameVisible(Boolean.parseBoolean(args[1]));
                break;
            case 60:
                p.getInventory().setItemInOffHand(p.getInventory().getItemInMainHand());
                break;
            case 61:
                pd.nextTrinketCast = 0;
                p.sendMessage("reset trinket cast time");
                break;
            case 62:
                String skullOwner = args[1];
                ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
                SkullMeta sm = (SkullMeta) skull.getItemMeta();
                sm.setOwner(skullOwner);
                skull.setItemMeta(sm);
                p.getInventory().addItem(skull);
                p.sendMessage("Received skull");
                break;
            case 63:
                skull = RHead.getSkull("http://textures.minecraft.net/texture/4e4ff83c879dce67f3a01d682882527189fbd63386073d8e828b76e214ac1");
                MobData md = MobManager.createMob("test_head", p.getLocation());
                EntityEquipment ee = md.entity.getEquipment();
                ee.setHelmet(skull);
                break;
            case 64:
                md = MobManager.createMob("test_head", p.getLocation());
                ee = md.entity.getEquipment();
                ee.setHelmet(p.getInventory().getItemInMainHand());
                break;
            case 65:
                RMessages.announce("armor contents: " + Arrays.toString(p.getInventory().getArmorContents()));
                RMessages.announce("full contents: " + Arrays.toString(p.getInventory().getContents()));
                break;
            case 66:
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                try {
                    new ObjectOutputStream(out).writeObject(p.getInventory().getItemInMainHand());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                String temp = new String(Base64.encodeBase64(out.toByteArray()));
                RMessages.announce("encoded to base64: " + p.getInventory().getItemInMainHand());
                System.out.println(temp);

                ByteArrayInputStream in = new ByteArrayInputStream(Base64.decodeBase64(temp.getBytes()));
                try {
                    Object obj = new ObjectInputStream(in).readObject();
                    if (obj instanceof ItemStack) {
                        RMessages.announce("decoded from base64: " + ((ItemStack) obj));
                    } else {
                        RMessages.announce("failed to decode itemstack");
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case 67:
                is = p.getInventory().getItemInMainHand();
                RMessages.announce("serializing with reflection: " + is);
                System.out.println(temp = RSerializer.serializeItemStack(is));
                RMessages.announce("deserialized with reflection: " + RSerializer.deserializeItemStack(temp));
                break;
            case 68:
                skull = RHead.getSkull("http://textures.minecraft.net/texture/4e4ff83c879dce67f3a01d682882527189fbd63386073d8e828b76e214ac1");
                p.getInventory().addItem(skull);
                p.sendMessage("added genned skull");
                break;
            case 69:
                Object tempObj = LocalDate.now();
                System.out.println(tempObj);
                break;
            case 70:
                p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 300, 10, false, false));
                Location dest = p.getLocation().add(p.getLocation().getDirection().multiply(3));
                CustomWarpEffect we = EffectFactory.getWarpEffect(p, 3);
                we.setEntity(p);
                we.start();
                RScheduler.schedule(plugin, new Runnable() {
                    Location lastLoc = null;
                    int count = 5;

                    private void endWarp() {
                        p.removePotionEffect(PotionEffectType.CONFUSION);
                        we.cancel();
                        lastLoc = null;
                    }

                    public void run() {
                        if (p == null || !p.isOnline())
                            return;
                        if (lastLoc == null) {
                            lastLoc = p.getLocation();
                        } else {
                            Location curr = p.getLocation();
                            if (curr.getX() != lastLoc.getX() || curr.getY() != lastLoc.getY() || curr.getZ() != lastLoc.getZ()) {
                                RMessages.sendTitle(p, ChatColor.RED + "Warp cancelled", "", 2, 30, 5);
                                endWarp();
                                return;
                            }
                        }
                        // THIS IS NOT THE RIGHT ONE LOL THIS IS DBCOMMAND
                        if (count == 0)
                            RMessages.sendTitle(p, "Warping now!", ChatColor.GREEN + "Move to cancel the warp", 2, 16, 2);
                        else
                            RMessages.sendTitle(p, "Warping in " + count, ChatColor.GREEN + "Move to cancel the warp", 2, 16, 2);
                        count--;
                        if (count < 0) {
                            RParticles.showWithOffset(ParticleEffect.CLOUD, p.getLocation().add(0, 1, 0), 1.5, 30);
                            p.teleport(dest);
                            RParticles.showWithOffset(ParticleEffect.CLOUD, p.getLocation().add(0, 1, 0), 1.5, 30);
                            endWarp();
                        } else {
                            RScheduler.schedule(plugin, this, RTicks.seconds(1));
                        }
                    }
                });
                break;
            case 71:
                System.out.println(HologramManager.holograms);
                System.out.println(HologramManager.hologramsPerChunk);
                break;
            case 72:
                target = plugin.getServer().getPlayer(args[1]);
                if (target != null && target.isOnline()) {
                    Collection<PotionEffect> col = target.getActivePotionEffects();
                    for (PotionEffect pe : col) {
                        target.removePotionEffect(pe.getType());
                    }
                    p.sendMessage("Removed all potion effects from " + target.getName());
                }
                break;
            case 73:
                as = (ArmorStand) p.getWorld().spawnEntity(p.getLocation(), EntityType.ARMOR_STAND);
                as.setGravity(false);
                as.setAI(false);
                as.setVisible(false);
                as.setCustomName("test");
                as.setCustomNameVisible(true);
                as.setHelmet(new ItemStack(Material.CHEST));
                RScheduler.schedule(plugin, new Runnable() {
                    int counter = 0;

                    public void run() {
                        as.setBodyPose(as.getBodyPose().add(1, 0, 0));
                        if (counter++ < 20)
                            RScheduler.schedule(plugin, this);
                    }
                });
                break;
            case 74:
                as = (ArmorStand) p.getWorld().spawnEntity(p.getLocation(), EntityType.ARMOR_STAND);
                as.setGravity(false);
                as.setAI(false);
                as.setVisible(false);
                as.setCustomName("test");
                as.setCustomNameVisible(true);
                as.setHelmet(new ItemStack(Material.CHEST));
                RScheduler.schedule(plugin, new Runnable() {
                    int counter = 0;

                    public void run() {
                        as.setBodyPose(as.getBodyPose().add(0, 1, 0));
                        if (counter++ < 20)
                            RScheduler.schedule(plugin, this);
                    }
                });
                break;
            case 75:
                as = (ArmorStand) p.getWorld().spawnEntity(p.getLocation(), EntityType.ARMOR_STAND);
                as.setGravity(false);
                as.setAI(false);
                as.setVisible(false);
                as.setCustomName("test");
                as.setCustomNameVisible(true);
                as.setHelmet(new ItemStack(Material.CHEST));
                RScheduler.schedule(plugin, new Runnable() {
                    int counter = 0;

                    public void run() {
                        as.setBodyPose(as.getBodyPose().add(0, 0, 1));
                        if (counter++ < 20)
                            RScheduler.schedule(plugin, this);
                    }
                });
                break;
            case 76:
                as = (ArmorStand) p.getWorld().spawnEntity(p.getLocation().add(0, -1, 0), EntityType.ARMOR_STAND);
                as.setGravity(false);
                as.setAI(false);
                as.setVisible(false);
                as.setCustomName(ChatColor.GOLD + "Dungeon Rewards " + ChatColor.GRAY + "[Right-click to open]");
                as.setCustomNameVisible(true);
                as.setHelmet(new ItemStack(Material.CHEST));
                RScheduler.schedule(plugin, new Runnable() {
                    int counter = 0;

                    public void run() {
                        ((CraftArmorStand) as).getHandle().yaw += 10;
                        if (counter++ < 20000)
                            RScheduler.schedule(plugin, this, 1);
                    }
                }, 1);
                break;
            case 77:
                RMessages.announce(new ChunkWrapper(p.getLocation().getChunk()).toString());
                break;
            case 78:
                le = REntities.createLivingEntity(CustomZombie.class, p.getLocation());
                ee = le.getEquipment();
                ee.setItemInMainHand(new ItemStack(Material.DIAMOND_SWORD));
                ee.setItemInOffHand(new ItemStack(Material.GOLD_SWORD));
                break;
            case 79:
                RMessages.announce(p.getName() + " initiated forced server crash.");
                for (int k = 0; k < 2000000000; k++) {
                    try {
                        Thread.sleep(100000);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                }
                p.sendMessage("Crash failed");
                break;
            case 80:
                for (int k = 0; k < 36; k++) {
                    RMessages.announce(p.getInventory().getItem(k) + "");
                }
                RMessages.announce(Arrays.toString(p.getInventory().getArmorContents()));
                break;
            case 81:
                RSound.playSound(p, Sound.valueOf(args[1].toUpperCase()));
                break;
            case 82:
                pd.gainExp(Long.parseLong(args[1]));
                break;
            case 83:
                mt = new MobType();
                mt.identifier = "db_83_mob_" + mt.level;
                mt.entityClass = CustomZombie.class;
                mt.level = Integer.parseInt(args[1]);
                mt.name = "Base Level " + mt.level + " Mob";
                mt.prefixes = new ArrayList<String>();
                mt.suffixes = new ArrayList<String>();
                mt.exp = MobBalance.getMobEXP(mt.level, 1);
                mt.damageLow = MobBalance.getMobDamage(mt.level, 1, 1)[0];
                mt.damageHigh = MobBalance.getMobDamage(mt.level, 1, 1)[1];
                mt.maxHP = MobBalance.getMobHP(mt.level, 1);
                mt.equips = new ArrayList<ItemStack>();
                mt.attributes = new ArrayList<MobAttribute>();
                mt.spells = new ArrayList<MobSpell>();
                mt.drops = new ArrayList<MobDrop>();
                mt.spawn(p.getTargetBlock((Set<Material>) null, 100).getLocation().add(0, 1, 0));
                RMessages.announce("Spawned base mob of level " + mt.level);
                RMessages.announce("Values: EXP: " + mt.exp + ", Damage: " + mt.damageLow + "-" + mt.damageHigh + ", HP: " + mt.maxHP);
                break;
            case 84:
                le = REntities.createLivingEntity(CustomZombie.class, p.getLocation());
                le.setCustomName("mob 1");
                le.setCustomNameVisible(true);
                ((CraftLivingEntity) le).getHandle().P = 0.6f;
                RMessages.announce("le P value: " + ((CraftLivingEntity) le).getHandle().P);
                RScheduler.schedule(plugin, new Runnable() {
                    int count = 30;

                    public void run() {
                        AttributeInstance ati = ((EntityLiving) (((CraftLivingEntity) le).getHandle())).getAttributeInstance(GenericAttributes.FOLLOW_RANGE);
                        ati.setValue(100);
                        ((EntityInsentient) ((CraftLivingEntity) le).getHandle()).getNavigation().a(p.getLocation().getX(), p.getLocation().getY(), p.getLocation().getZ(), 1.5);
                        if (count-- > 0)
                            RScheduler.schedule(plugin, this, RTicks.seconds(3));
                        else
                            le.remove();
                    }
                }, RTicks.seconds(1));
                LivingEntity le2 = REntities.createLivingEntity(CustomZombie.class, p.getLocation());
                le2.setCustomName("mob 2");
                le2.setCustomNameVisible(true);
                RMessages.announce("le2 P value: " + ((CraftLivingEntity) le2).getHandle().P);
                ((CraftLivingEntity) le2).getHandle().P = 2;
                RMessages.announce("le2 P value modified: " + ((CraftLivingEntity) le2).getHandle().P);
                RScheduler.schedule(plugin, new Runnable() {
                    int count = 30;

                    public void run() {
                        AttributeInstance ati = ((EntityLiving) (((CraftLivingEntity) le2).getHandle())).getAttributeInstance(GenericAttributes.FOLLOW_RANGE);
                        ati.setValue(100);
                        ((EntityInsentient) ((CraftLivingEntity) le2).getHandle()).getNavigation().a(p.getLocation().getX(), p.getLocation().getY(), p.getLocation().getZ(), 1.5);
                        if (count-- > 0)
                            RScheduler.schedule(plugin, this, RTicks.seconds(3));
                        else
                            le2.remove();
                    }
                }, RTicks.seconds(1));
                break;
            case 85:
                MusicManager.playMusic(p);
                break;
            case 86:
                //                CustomFreezeSpellEffect effect = EffectFactory.getFreezeSpellEffect(p.getLocation().add(0, 0.15, 0), 3);
                //                effect.setEntity(p);
                //                effect.start();
                p.sendMessage("starting freeze effect");
                break;
            case 87:
                try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(new File(plugin.getDataFolder(), "blah.json"))))) {
                    writer.write(RGson.getGson().toJson(GenericNPCManager.genericVillagers));
                    writer.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                break;
            case 88:
                SkillManager.giveAxe(p);
                break;
            case 89:
                for (int k = 1; k < 120; k++)
                    System.out.println(xpTable(k));
                break;
            case 90:
                SkillManager.openMenu(p);
                break;
            case 91:
                p.launchProjectile(FishHook.class);
                break;
            case 92:
                //                PetManager.givePet(p);
                break;
            case 93:
                FallingBlock fall = p.getLocation().getWorld().spawnFallingBlock(p.getLocation(), Material.JACK_O_LANTERN, (byte) 0);
                fall.setDropItem(false);
                fall.setHurtEntities(false);
                RScheduler.schedule(plugin, new Runnable() {
                    public void run() {
                        if (fall.isDead()) {
                            System.out.println("dead");
                        } else {
                            RScheduler.schedule(plugin, this, 3);
                        }
                    }
                });
                break;
            case 94:
                HweenPumpkinBombEffect effect = new HweenPumpkinBombEffect(EffectFactory.em(), p.getLocation());
                effect.run();
                break;
            case 95:
                ManagerInstances.debug();
                break;
            case 96:
                RMessages.announce(p.getName() + "sending bedrock packet");
                p.sendBlockChange(p.getTargetBlock((HashSet<Byte>) null, 100).getLocation(), Material.BEDROCK, (byte) 0);
                break;
            case 97:
                RMessages.announce(p.getName() + "sending bedrock packet2");
                p.sendBlockChange(p.getLocation(), Material.BEDROCK, (byte) 0);
                break;
            case 98:
                RMessages.announce(p.getName() + "sending air packet1");
                p.sendBlockChange(p.getTargetBlock((HashSet<Byte>) null, 100).getLocation(), Material.AIR, (byte) 0);
                break;
            case 99:
                RMessages.announce(p.getName() + "sending barrier packet1");
                p.sendBlockChange(p.getTargetBlock((HashSet<Byte>) null, 100).getLocation(), Material.BARRIER, (byte) 0);
                break;
            case 100:
                RMessages.announce(p.getName() + "sending air packet2");
                p.sendBlockChange(p.getLocation().add(0, -1, 0), Material.AIR, (byte) 0);
                break;
            case 101:
                p.sendBlockChange(new Location(plugin.getServer().getWorld("world"), -281, 64, 84), Material.WOOD, (byte) 0);
                p.sendBlockChange(new Location(plugin.getServer().getWorld("world"), -281, 65, 84), Material.WOOD, (byte) 0);
                break;
            case 102:
                p.sendBlockChange(new Location(plugin.getServer().getWorld("world"), -281, 64, 84), Material.AIR, (byte) 0);
                p.sendBlockChange(new Location(plugin.getServer().getWorld("world"), -281, 65, 84), Material.AIR, (byte) 0);
                break;
            case 103:
                RScheduler.schedule(plugin, () -> {
                    p.setVelocity(p.getLocation().getDirection().setY(0).normalize().setY(0.7).multiply(3));
                    RScheduler.schedule(plugin, () -> {
                        p.setGliding(true);
                        p.setVelocity(p.getLocation().getDirection().setY(0).normalize().multiply(3));
                    }, 5);
                }, 10);
                //                p.setGliding(true);
                break;
            case 104:
                p.sendMessage("Count: " + ItemManager.count(p, args[1]));
                break;
            case 105:
                p.sendMessage("Successful take? " + ItemManager.take(p, args[1], Integer.parseInt(args[2])));
                break;
            case 106:
                p.sendMessage("unlocked? " + pd.unlocked(Unlock.valueOf(args[1].toUpperCase())));
                break;
            case 107:
                pd.addUnlock(Unlock.valueOf(args[1].toUpperCase()));
                p.sendMessage("added " + Unlock.valueOf(args[1].toUpperCase()));
                p.sendMessage("unlocks: " + pd.unlocks);
                break;
            case 108:
                pd.removeUnlock(Unlock.valueOf(args[1].toUpperCase()));
                p.sendMessage("removed " + Unlock.valueOf(args[1].toUpperCase()));
                p.sendMessage("unlocks: " + pd.unlocks);
                break;
            case 109:
                p.sendMessage("unlocks: " + pd.unlocks);
                break;
            case 110:
                System.out.println(Unlock.valueOf(args[1].toUpperCase()) + " " + Unlock.valueOf(args[1].toUpperCase()).hashCode());
                for (Unlock u : pd.unlocks)
                    System.out.println(u + " " + u.hashCode());
                break;
            case 111:
                System.out.println(Unlock.valueOf(args[1].toUpperCase()) + " " + Unlock.valueOf(args[1].toUpperCase()).hashCode());
                for (Object u : pd.unlocks)
                    System.out.println(u + " " + u.hashCode());
                break;
            case 112:
                pd.addState(args[1]);
                pd.sendMessage("added state " + args[1]);
                break;
            case 113:
                pd.removeState(args[1]);
                pd.sendMessage("removed state " + args[1]);
                break;
            case 114:
                pd.addNewMobCounter(args[1], args[2]);
                break;
            case 115:
                ShardManager.takeShards(p, Integer.parseInt(args[1]));
                break;
            case 116:
                ShardManager.takeCubes(p, Integer.parseInt(args[1]));
                break;
            case 117:
                ShardManager.takePurified(p, Integer.parseInt(args[1]));
                break;
            case 118:
                ShardManager.giveCurrency(p, Integer.parseInt(args[1]));
                break;
            case 119:
                ShardManager.takeCurrency(p, Integer.parseInt(args[1]));
                break;
            case 120:
                List<String> list = new ArrayList<String>();
                for (MobType mt2 : MobManager.mobTypes.values()) {
                    list.add(String.format("%03d", mt2.level) + " - " + mt2.name);
                }
                Collections.sort(list);
                for (String s : list)
                    System.out.println(s);
                break;
            case 121:
                RScheduler.schedule(plugin, () -> {
                    System.out.println(p.getOpenInventory() instanceof PlayerInventory);
                }, RTicks.seconds(2));
                break;
            case 122:
                //                PetManager.test(p);
                break;
        }
    }

    @Override
    public void executeConsole(CommandSender sender, String[] args) {
        HashSet<Skill> tmp = new HashSet<Skill>();
        tmp.add(Skill.WOODCUTTING);
        tmp.add(Skill.FIREMAKING);
        String j;
        //        System.out.println(j = new GsonBuilder().registerTypeAdapterFactory(new EnumTypeAdapterFactory()).create().toJson(tmp));
        //        System.out.println(new GsonBuilder().registerTypeAdapterFactory(new EnumTypeAdapterFactory()).create().fromJson(j, HashSet.class));
        //        for (Object e : new GsonBuilder().registerTypeAdapterFactory(new EnumTypeAdapterFactory()).create().fromJson(j, HashSet.class)) {

        //        System.out.println(j = new GsonBuilder().create().toJson(tmp));
        //        System.out.println(new GsonBuilder().create().fromJson(j, HashSet.class));
        //        for (Object e : new GsonBuilder().create().fromJson(j, HashSet.class)) {

        System.out.println(j = new Gson().toJson(tmp));
        System.out.println(new Gson().fromJson(j, HashSet.class));
        System.out.println("attempting to deserialize now..");
        for (Object e : new Gson().<HashSet<Skill>> fromJson(j, new TypeToken<HashSet<Skill>>() {
        }.getType())) {
            System.out.println(e.getClass());
            System.out.println(e.toString());
        }
        //        System.out.println(j = RGson.getConciseGson().toJson(tmp));
        //        System.out.println(RGson.getGson().fromJson(j, HashMap.class));
        //        for (Object e : RGson.getGson().fromJson(j, HashMap.class).entrySet()) {
        //            System.out.println(e.getClass());
        //            System.out.println("" + (e instanceof Entry));
        //            if (e instanceof Entry) {
        //                System.out.println(((Entry) e).getKey().getClass());
        //                System.out.println(((Entry) e).getValue().getClass());
        //            }

        //        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(new File(plugin.getDataFolder(), "mobs.json"))))) {
        //            writer.write(RGson.getGson().toJson(MobManager.mobTypes));
        //            writer.close();
        //        } catch (IOException e1) {
        //            e1.printStackTrace();
        //        }
        //        Player p = plugin.getServer().getPlayer(args[0]);
        //        if (p.isOnline()) {
        //            p.getInventory().addItem(RHead.getSkull(args[1]));
        //            p.sendMessage("received skull from console");
        //        }
        //        sender.sendMessage("test lol");
        //        sender.sendMessage(Arrays.toString(args));
        //        sender.sendMessage(RSerializer.serialize(args));
        //        sender.sendMessage("Length: " + RSerializer.serialize(args).length());
        //        sender.sendMessage(Arrays.toString(RSerializer.deserializeArray(RSerializer.serialize(args))));
        //        ItemStack item = new ItemStack(Material.STONE_SWORD);
        //        ItemMeta im = item.getItemMeta();
        //        im.setDisplayName("Test name");
        //        item.setItemMeta(im);
        //        sender.sendMessage(RSerializer.serializeItemStack(item));
        //        sender.sendMessage(RSerializer.deserializeItemStack(RSerializer.serializeItemStack(item)).toString());
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

    }

    public int xpTable(int level) {
        int totalXP = 0;
        for (int k = 1; k < level; k++) {
            totalXP += (int) (k + Math.pow(2D, k / 7D) * 300D);
        }
        return totalXP / 4;
    }

}

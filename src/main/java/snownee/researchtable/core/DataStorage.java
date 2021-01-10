package snownee.researchtable.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Nullable;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap.Entry;
import it.unimi.dsi.fastutil.objects.Object2IntMaps;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.ThreadedFileIOBase;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import snownee.kiwi.network.NetworkChannel;
import snownee.researchtable.ResearchTable;
import snownee.researchtable.core.team.TeamHelper;
import snownee.researchtable.network.PacketSyncClient;

@EventBusSubscriber(modid = ResearchTable.MODID)
public class DataStorage {
    private static DataStorage INSTANCE;
    private final WorldServer world;
    private static final Map<UUID, Object2IntMap<String>> records = new HashMap<>();
    private static final Map<String, Object2IntMap<String>> players = new HashMap<>();
    private static boolean changed = false;
    public static Object2IntMap<String> clientData;

    public DataStorage(WorldServer world) {
        this.world = world;
        load();
    }

    private void load() {
        records.clear();
        players.clear();
        changed = false;
        clientData = null;

        File folder = new File(world.getSaveHandler().getWorldDirectory(), "data/");
        File file = new File(folder, ResearchTable.MODID + ".dat");
        NBTTagCompound data = null;

        if (file.exists() && file.isFile()) {
            try (InputStream stream = new FileInputStream(file)) {
                data = CompressedStreamTools.readCompressed(stream);
            } catch (Exception ex1) {
                try {
                    data = CompressedStreamTools.read(file);
                } catch (Exception ex2) {
                    ex2.printStackTrace();
                }
            }
        }
        if (data == null) {
            return;
        }

        int format = data.getInteger("__v");
        if (format == 0) {
            for (String player : data.getKeySet()) {
                if (data.hasKey(player, Constants.NBT.TAG_COMPOUND)) {
                    NBTTagCompound compound = data.getCompoundTag(player);
                    Object2IntMap<String> researches = readPlayerData(compound);
                    if (!researches.isEmpty()) {
                        players.put(player, researches);
                    }
                }
            }
        } else if (format == 1) {
            NBTTagCompound playersData = data.getCompoundTag("oldRecords");
            for (String player : playersData.getKeySet()) {
                if (data.hasKey(player, Constants.NBT.TAG_COMPOUND)) {
                    NBTTagCompound compound = playersData.getCompoundTag(player);
                    Object2IntMap<String> researches = readPlayerData(compound);
                    if (!researches.isEmpty()) {
                        players.put(player, researches);
                    }
                }
            }

            NBTTagList recordsData = data.getTagList("records", Constants.NBT.TAG_COMPOUND);
            for (NBTBase raw : recordsData) {
                NBTTagCompound recordData = (NBTTagCompound) raw;
                UUID k = recordData.getUniqueId("k");
                Object2IntMap<String> v = readPlayerData(recordData.getCompoundTag("v"));
                if (!v.isEmpty()) {
                    records.put(k, v);
                }
            }
        } else {
            throw new RuntimeException("Unsupported format version");
        }
    }

    private void save() {
        if (!changed) {
            return;
        }
        NBTTagCompound data = new NBTTagCompound();
        data.setInteger("__v", 1);

        NBTTagList playersDataList = new NBTTagList();
        players.forEach((player, researches) -> {
            if (!researches.isEmpty()) {
                NBTTagCompound playersData = new NBTTagCompound();
                playersData.setTag(player, writePlayerData(researches));
                playersDataList.appendTag(playersData);
            }
        });
        if (!playersDataList.isEmpty()) {
            data.setTag("oldRecords", playersDataList);
        }

        NBTTagList recordsDataList = new NBTTagList();
        records.forEach((k, v) -> {
            if (!v.isEmpty()) {
                NBTTagCompound recordsData = new NBTTagCompound();
                recordsData.setUniqueId("k", k);
                recordsData.setTag("v", writePlayerData(v));
                recordsDataList.appendTag(recordsData);
            }
        });
        if (!recordsDataList.isEmpty()) {
            data.setTag("records", recordsDataList);
        }

        File folder = new File(world.getSaveHandler().getWorldDirectory(), "data/");
        File file = new File(folder, ResearchTable.MODID + ".dat");
        ThreadedFileIOBase.getThreadedIOInstance().queueIO(() -> {
            try {
                if (!file.exists()) {
                    if (!folder.exists()) {
                        folder.mkdirs();
                    }
                    file.createNewFile();
                }
                OutputStream stream = new FileOutputStream(file);
                CompressedStreamTools.writeCompressed(data, stream);
                changed = false;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        });
    }

    public static boolean loaded() {
        return INSTANCE != null;
    }

    // TODO: register event
    public static int complete(UUID uuid, Research research) {
        return setCount(uuid, research, count(uuid, research) + 1);
    }

    public static int setCount(UUID uuid, Research research, int count) {
        if (!loaded()) {
            return -1;
        }
        Object2IntMap<String> researches = getRecords(uuid);
        if (count > 0) {
            researches.put(research.getName(), count);
        } else {
            researches.remove(research.getName());
        }
        changed = true;
        syncClientAllMembers(uuid);
        return count;
    }

    public static Object2IntMap<String> getRecords(UUID uuid) {
        if (!loaded()) {
            return Object2IntMaps.EMPTY_MAP;
        }
        UUID owner = TeamHelper.provider.getOwner(uuid);
        if (owner != null) {
            return records.computeIfAbsent(owner, $ -> new Object2IntOpenHashMap<>());
        } else {
            return records.computeIfAbsent(uuid, $ -> new Object2IntOpenHashMap<>());
        }
    }

    public static int count(UUID uuid, String research) {
        if (loaded()) {
            return getRecords(uuid).getInt(research);
        } else if (clientData != null) {
            return clientData.getInt(research);
        }
        return 0;
    }

    public static int count(UUID uuid, Research research) {
        return count(uuid, research.getName());
    }

    public static boolean hasAllOf(UUID uuid, Collection<String> researches) {
        for (String research : researches) {
            if (count(uuid, research) == 0)
                return false;
        }
        return true;
    }

    @SubscribeEvent
    public static void onWorldLoaded(WorldEvent.Load event) {
        if (event.getWorld().provider.getDimension() == 0 && !event.getWorld().isRemote) {
            INSTANCE = new DataStorage((WorldServer) event.getWorld());
        }
    }

    @SubscribeEvent
    public static void onWorldSaved(WorldEvent.Save event) {
        if (loaded() && event.getWorld() == INSTANCE.world) {
            INSTANCE.save();
        }
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerLoggedInEvent event) {
        if (!loaded() || event.player instanceof FakePlayer) {
            return;
        }
        String name = event.player.getName();
        UUID uuid = event.player.getGameProfile().getId();
        if (players.containsKey(name)) {
            Object2IntMap<String> data = players.get(name);
            players.remove(name);
            mergeProgress(data, uuid);
            syncClientAllMembers(uuid);
        } else {
            syncClient(uuid);
        }
    }

    public static void mergeProgress(Object2IntMap<String> from, UUID uuid) {
        Object2IntMap<String> to = getRecords(uuid);
        for (Entry<String> entry : from.object2IntEntrySet()) {
            int fromInt = entry.getIntValue();
            int toInt = to.getInt(entry.getKey());
            if (fromInt > toInt) {
                to.put(entry.getKey(), fromInt);
            }
        }
        changed = true;
    }

    private static void syncClientAllMembers(UUID uuid) {
        TeamHelper.provider.getMembers(uuid).forEach(DataStorage::syncClient);
    }

    private static void syncClient(UUID uuid) {
        EntityPlayer player = getPlayer(uuid);
        if (player == null) {
            return;
        }
        if (player instanceof EntityPlayerMP && !(player instanceof FakePlayer)) {
            Object2IntMap<String> data = getRecords(player.getGameProfile().getId());
            if (!data.isEmpty()) {
                NetworkChannel.INSTANCE.sendToPlayer(new PacketSyncClient(data), (EntityPlayerMP) player);
            }
        }
    }

    public static Object2IntMap<String> readPlayerData(NBTTagCompound data) {
        Set<String> keySet = data.getKeySet();
        Object2IntMap<String> researches = new Object2IntOpenHashMap<>(keySet.size());
        for (String research : keySet) {
            if (data.hasKey(research, Constants.NBT.TAG_INT)) {
                int count = data.getInteger(research);
                if (count > 0) {
                    researches.put(research, count);
                }
            }
        }
        return researches;
    }

    public static NBTTagCompound writePlayerData(Object2IntMap<String> map) {
        NBTTagCompound data = new NBTTagCompound();
        map.forEach((research, count) -> {
            data.setInteger(research, count);
        });
        return data;
    }

    @Nullable
    public static EntityPlayerMP getPlayer(UUID uuid) {
        return FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUUID(uuid);
    }

    public static void onPlayerAdd(UUID uuid, UUID owner) {
        mergeProgress(records.getOrDefault(uuid, Object2IntMaps.EMPTY_MAP), owner);
        records.remove(uuid);
        syncClientAllMembers(owner);
    }
}

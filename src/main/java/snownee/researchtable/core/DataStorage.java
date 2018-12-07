package snownee.researchtable.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.ThreadedFileIOBase;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import snownee.kiwi.network.NetworkChannel;
import snownee.researchtable.ResearchTable;
import snownee.researchtable.network.PacketSyncClient;

@EventBusSubscriber(modid = ResearchTable.MODID)
public class DataStorage
{
    private static DataStorage INSTANCE;
    private final WorldServer world;
    private final Map<String, Map<String, Integer>> players = new HashMap<>();
    private boolean changed = false;
    public static Map<String, Integer> clientData;

    public DataStorage(WorldServer world)
    {
        this.world = world;
        load();
    }

    private void load()
    {
        File folder = new File(world.getSaveHandler().getWorldDirectory(), "data/");
        File file = new File(folder, ResearchTable.MODID + ".dat");
        NBTTagCompound data = null;

        if (file.exists() && file.isFile())
        {
            try (InputStream stream = new FileInputStream(file))
            {
                data = CompressedStreamTools.readCompressed(stream);
            }
            catch (Exception ex1)
            {
                try
                {
                    data = CompressedStreamTools.read(file);
                }
                catch (Exception ex2)
                {
                    ex2.printStackTrace();
                }
            }
        }
        if (data == null)
        {
            data = new NBTTagCompound();
        }

        for (String player : data.getKeySet())
        {
            if (data.hasKey(player, Constants.NBT.TAG_COMPOUND))
            {
                NBTTagCompound compound = data.getCompoundTag(player);
                Map<String, Integer> researches = readPlayerData(compound);
                if (!researches.isEmpty())
                {
                    players.put(player, researches);
                }
            }
        }
    }

    private void save()
    {
        if (changed)
        {
            NBTTagCompound data = new NBTTagCompound();
            players.forEach((player, researches) -> {
                if (!researches.isEmpty())
                {
                    data.setTag(player, writePlayerData(researches));
                }
            });

            File folder = new File(world.getSaveHandler().getWorldDirectory(), "data/");
            File file = new File(folder, ResearchTable.MODID + ".dat");
            ThreadedFileIOBase.getThreadedIOInstance().queueIO(() -> {
                try
                {
                    if (!file.exists())
                    {
                        if (!folder.exists())
                        {
                            folder.mkdirs();
                        }
                        file.createNewFile();
                    }
                    OutputStream stream = new FileOutputStream(file);
                    CompressedStreamTools.writeCompressed(data, stream);
                    changed = false;
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                return false;
            });
        }
    }

    public static boolean loaded()
    {
        return INSTANCE != null;
    }

    // TODO: register event
    public static int complete(String playerName, Research research)
    {
        return setCount(playerName, research, count(playerName, research) + 1);
    }

    public static int setCount(String playerName, Research research, int count)
    {
        if (loaded())
        {
            if (!INSTANCE.players.containsKey(playerName))
            {
                INSTANCE.players.put(playerName, new HashMap<>());
            }
            Map<String, Integer> researches = INSTANCE.players.get(playerName);
            if (count > 0)
            {
                researches.put(research.getName(), count);
            }
            else
            {
                researches.remove(research.getName());
            }
            INSTANCE.changed = true;
            EntityPlayer player = INSTANCE.world.getPlayerEntityByName(playerName);
            if (player != null)
            {
                syncClient(player);
            }
            return count;
        }
        return 0;
    }

    public static int count(String playerName, Research research)
    {
        if (loaded())
        {
            if (INSTANCE.players.containsKey(playerName))
            {
                return INSTANCE.players.get(playerName).getOrDefault(research.getName(), 0);
            }
        }
        else if (clientData != null)
        {
            return clientData.getOrDefault(research.getName(), 0);
        }
        return 0;
    }

    @SubscribeEvent
    public static void onWorldLoaded(WorldEvent.Load event)
    {
        if (event.getWorld().provider.getDimension() == 0 && !event.getWorld().isRemote)
        {
            INSTANCE = new DataStorage((WorldServer) event.getWorld());
        }
    }

    @SubscribeEvent
    public static void onWorldSaved(WorldEvent.Save event)
    {
        if (loaded() && event.getWorld() == INSTANCE.world)
        {
            INSTANCE.save();
        }
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerLoggedInEvent event)
    {
        syncClient(event.player);
    }

    private static void syncClient(EntityPlayer player)
    {
        if (loaded() && player instanceof EntityPlayerMP && !(player instanceof FakePlayer)
                && INSTANCE.players.containsKey(player.getName()))
        {
            NetworkChannel.INSTANCE.sendToPlayer(new PacketSyncClient(INSTANCE.players.get(player.getName())),
                    (EntityPlayerMP) player);
        }
    }

    public static Map<String, Integer> readPlayerData(NBTTagCompound data)
    {
        Set<String> keySet = data.getKeySet();
        Map<String, Integer> researches = new HashMap<>(keySet.size());
        for (String research : keySet)
        {
            if (data.hasKey(research, Constants.NBT.TAG_INT))
            {
                int count = data.getInteger(research);
                if (count > 0)
                {
                    researches.put(research, count);
                }
            }
        }
        return researches;
    }

    public static NBTTagCompound writePlayerData(Map<String, Integer> map)
    {
        NBTTagCompound data = new NBTTagCompound();
        map.forEach((research, count) -> {
            data.setInteger(research, count);
        });
        return data;
    }

}

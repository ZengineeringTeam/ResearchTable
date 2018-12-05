package snownee.researchtable.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.ThreadedFileIOBase;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import snownee.researchtable.ResearchTable;

@EventBusSubscriber(modid = ResearchTable.MODID)
public class DataStorage
{
    private static DataStorage INSTANCE;
    private final WorldServer world;
    private final Map<String, Map<String, Integer>> players = new HashMap<>();
    private boolean changed = false;

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
                Set<String> keySet = compound.getKeySet();
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
                    NBTTagCompound compound = new NBTTagCompound();
                    researches.forEach((research, count) -> {
                        compound.setInteger(research, count);
                    });
                    data.setTag(player, compound);
                }
            });

            File folder = new File(world.getSaveHandler().getWorldDirectory(), "data/");
            File file = new File(folder, ResearchTable.MODID + ".dat");
            System.out.println(data);
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
        if (loaded())
        {
            if (!INSTANCE.players.containsKey(playerName))
            {
                INSTANCE.players.put(playerName, new HashMap<>());
            }
            Map<String, Integer> researches = INSTANCE.players.get(playerName);
            int count = researches.getOrDefault(research.getName(), 0);
            researches.put(research.getName(), ++count);
            INSTANCE.changed = true;
            return count;
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

}

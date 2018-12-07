package snownee.researchtable.plugin.hwyla;

import java.util.List;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import snownee.kiwi.util.Util;
import snownee.researchtable.ResearchTable;
import snownee.researchtable.block.TileTable;
import snownee.researchtable.core.Research;

@Deprecated
public class TableInfoProvider implements IWailaDataProvider
{
    @Override
    public List<String> getWailaBody(ItemStack itemStack, List<String> tooltip, IWailaDataAccessor accessor, IWailaConfigHandler config)
    {
        if (accessor.getTileEntity() instanceof TileTable)
        {
            TileTable table = (TileTable) accessor.getTileEntity();
            if (accessor.getNBTData() != null)
            {
                table.handleUpdateTag(accessor.getNBTData());
            }
            if (table.ownerName != null && !table.ownerName.isEmpty())
            {
                tooltip.add(I18n.format(ResearchTable.MODID + ".gui.owner", TextFormatting.WHITE + table.ownerName));
            }
            Research research = table.getResearch();
            if (research != null)
            {
                String title = research.getTitle();
                if (I18n.hasKey(title))
                {
                    title = I18n.format(title);
                }
                tooltip.add(I18n.format(ResearchTable.MODID + ".gui.researching", TextFormatting.WHITE + title));
                tooltip.add(I18n.format(ResearchTable.MODID + ".gui.progress",
                        TextFormatting.WHITE + Util.MESSAGE_FORMAT.format(new Float[] { table.getProgress() }) + "%"));
            }
        }
        return tooltip;
    }

    @Override
    public NBTTagCompound getNBTData(EntityPlayerMP player, TileEntity te, NBTTagCompound tag, World world, BlockPos pos)
    {
        if (te instanceof TileTable)
        {
            tag = ((TileTable) te).getUpdateTag();
        }
        return tag;
    }
}

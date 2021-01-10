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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import snownee.kiwi.util.NBTHelper.Tag;
import snownee.kiwi.util.Util;
import snownee.researchtable.ResearchTable;
import snownee.researchtable.block.TileTable;

public class TableInfoProvider implements IWailaDataProvider {
    @Override
    @SideOnly(Side.CLIENT)
    public List<String> getWailaBody(ItemStack itemStack, List<String> tooltip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        if (accessor.getTileEntity() instanceof TileTable && accessor.getNBTData() != null) {
            NBTTagCompound tag = accessor.getNBTData();
            if (tag.hasKey("owner", Tag.STRING)) {
                tooltip.add(I18n.format(ResearchTable.MODID + ".gui.owner", TextFormatting.WHITE + tag.getString("owner")));
            }
            if (tag.hasKey("research", Tag.STRING)) {
                String title = tag.getString("research");
                title = I18n.hasKey(title) ? I18n.format(title) : title;
                tooltip.add(I18n.format(ResearchTable.MODID + ".gui.researching", TextFormatting.WHITE + title));
            }
            if (tag.hasKey("progress", Tag.FLOAT)) {
                float progress = tag.getFloat("progress");
                tooltip.add(I18n.format(ResearchTable.MODID + ".gui.progress", TextFormatting.WHITE + Util.MESSAGE_FORMAT.format(new Float[] { progress }) + "%"));
            }
        }
        return tooltip;
    }

    @Override
    public NBTTagCompound getNBTData(EntityPlayerMP player, TileEntity te, NBTTagCompound tag, World world, BlockPos pos) {
        if (te instanceof TileTable) {
            TileTable table = (TileTable) te;
            if (table.ownerName != null && !table.ownerName.isEmpty()) {
                tag.setString("owner", table.ownerName);
            }
            if (table.getResearch() != null) {
                tag.setString("research", table.getResearch().getTitleRaw());
                tag.setFloat("progress", table.getProgress());
            }
        }
        return tag;
    }
}

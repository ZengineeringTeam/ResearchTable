package snownee.researchtable.plugin.top;

import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoProvider;
import mcjty.theoneprobe.api.ProbeMode;
import mcjty.theoneprobe.apiimpl.styles.ProgressStyle;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import snownee.researchtable.ResearchTable;
import snownee.researchtable.ResearchTableModule;
import snownee.researchtable.block.TileTable;
import snownee.researchtable.core.Research;

public class TableInfoProvider implements IProbeInfoProvider {

    @Override
    public String getID() {
        return ResearchTable.MODID;
    }

    @Override
    public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {
        if (mode == ProbeMode.EXTENDED || mode == ProbeMode.DEBUG) {
            if (blockState.getBlock() == ResearchTableModule.TABLE) {
                TileEntity tile = world.getTileEntity(data.getPos());
                if (tile instanceof TileTable) {
                    TileTable table = (TileTable) tile;
                    if (table.ownerName != null && !table.ownerName.isEmpty()) {
                        probeInfo.text(I18n.translateToLocalFormatted(ResearchTable.MODID + ".gui.owner", TextFormatting.WHITE + table.ownerName));
                    }
                    Research research = table.getResearch();
                    if (research != null) {
                        String title = research.getTitleRaw();
                        if (I18n.canTranslate(title)) {
                            title = I18n.translateToLocalFormatted(title);
                        }
                        probeInfo.text(I18n.translateToLocalFormatted(ResearchTable.MODID + ".gui.researching", TextFormatting.WHITE + title));
                        probeInfo.progress((int) (table.getProgress()), 100, new ProgressStyle().filledColor(0xFF00CC33).alternateFilledColor(0xFF00CC33).backgroundColor(0).suffix("%"));
                    }
                }
            }
        }
    }

}

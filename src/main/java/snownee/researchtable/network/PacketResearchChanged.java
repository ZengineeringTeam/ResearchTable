package snownee.researchtable.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import snownee.kiwi.network.PacketMod;
import snownee.researchtable.block.TileTable;
import snownee.researchtable.core.Research;
import snownee.researchtable.core.ResearchList;

public class PacketResearchChanged implements PacketMod {
    private BlockPos pos;
    private Research research;
    private Action action;

    public PacketResearchChanged() {
    }

    public PacketResearchChanged(BlockPos pos, Research research, Action action) {
        this.pos = pos;
        this.research = research;
        this.action = action;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void handleClient(EntityPlayerSP player) {
    }

    @Override
    public void handleServer(EntityPlayerMP player) {
        TileEntity tile = player.world.getTileEntity(pos);
        if (tile instanceof TileTable) {
            TileTable table = (TileTable) tile;
            if (!table.hasPermission(player)) {
                return;
            }
            switch (action) {
            case START:
                if (!research.canResearch(player, table.getData())) {
                    return;
                }
                if (table.getResearch() == null && research != null) {
                    research.start(player.world, pos, player);
                    table.setResearch(research);
                }
                break;
            case STOP:
                if (table.getResearch() == research && !table.canComplete()) {
                    table.setResearch(null);
                }
                break;
            case COMPLETE:
                if (table.getResearch() == research && table.canComplete()) {
                    table.complete(player);
                }
                break;
            case SUBMIT:
                if (table.getResearch() == research && !table.canComplete()) {
                    table.submit(player);
                }
                break;
            }
            table.hasChanged = true; // server
        }
    }

    @Override
    public void readDataFrom(ByteBuf buf) {
        int x = buf.readInt();
        int y = buf.readInt();
        int z = buf.readInt();
        pos = new BlockPos(x, y, z);
        research = ResearchList.find(ByteBufUtils.readUTF8String(buf)).orElseGet(() -> null);
        action = Action.values()[buf.readByte() % 4];
    }

    @Override
    public void writeDataTo(ByteBuf buf) {
        buf.writeInt(pos.getX());
        buf.writeInt(pos.getY());
        buf.writeInt(pos.getZ());
        ByteBufUtils.writeUTF8String(buf, research.getName());
        buf.writeByte(action.ordinal());
    }

    public enum Action {
        START, STOP, COMPLETE, SUBMIT
    }

}

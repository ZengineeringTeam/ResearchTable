package snownee.researchtable.network;

import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import snownee.kiwi.network.PacketMod;
import snownee.researchtable.core.DataStorage;

public class PacketSyncClient implements PacketMod {
    private Object2IntMap<String> map;

    public PacketSyncClient() {
    }

    public PacketSyncClient(Object2IntMap<String> map) {
        this.map = map;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void handleClient(EntityPlayerSP player) {
        DataStorage.clientData = map;
    }

    @Override
    public void handleServer(EntityPlayerMP player) {
    }

    @Override
    public void readDataFrom(ByteBuf buf) {
        map = DataStorage.readPlayerData(ByteBufUtils.readTag(buf));
    }

    @Override
    public void writeDataTo(ByteBuf buf) {
        ByteBufUtils.writeTag(buf, DataStorage.writePlayerData(map));
    }

}

package snownee.researchtable.block;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import snownee.kiwi.tile.TileBase;

public class TileTable extends TileBase
{
    public TileTable()
    {
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing)
    {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
        {

        }
        return super.getCapability(capability, facing);
    }

    @Override
    protected void readPacketData(NBTTagCompound arg0)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    protected NBTTagCompound writePacketData(NBTTagCompound arg0)
    {
        // TODO Auto-generated method stub
        return null;
    }
}

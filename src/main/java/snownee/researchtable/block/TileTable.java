package snownee.researchtable.block;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import snownee.kiwi.tile.TileBase;

public class TileTable extends TileBase
{
    private String currentResearch;

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
    protected void readPacketData(NBTTagCompound data)
    {
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        // TODO Auto-generated method stub
        super.readFromNBT(compound);
    }

    @Override
    protected NBTTagCompound writePacketData(NBTTagCompound data)
    {
        if (currentResearch != null)
        {
            data.setString("currentResearch", currentResearch);
        }
        return data;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        // TODO Auto-generated method stub
        return super.writeToNBT(compound);
    }
}

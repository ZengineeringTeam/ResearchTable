package snownee.researchtable.block;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import snownee.kiwi.tile.TileBase;
import snownee.researchtable.core.ConditionTypes;
import snownee.researchtable.core.DataStorage;
import snownee.researchtable.core.ICondition;
import snownee.researchtable.core.Research;
import snownee.researchtable.core.ResearchList;

public class TileTable extends TileBase
{

    public class ResearchItemWrapper implements IItemHandler
    {

        ResearchItemWrapper()
        {
        }

        @Override
        public int getSlots()
        {
            return research != null && !canComplete ? 1 : 0;
        }

        @Override
        public ItemStack getStackInSlot(int slot)
        {
            return ItemStack.EMPTY;
        }

        @Override
        public ItemStack insertItem(int slot, ItemStack stack, boolean simulate)
        {
            if (slot == 0 && research != null && !stack.isEmpty() && !canComplete)
            {
                long matched = match(ConditionTypes.ITEM, stack, simulate);
                return ItemHandlerHelper.copyStackWithSize(stack, stack.getCount() - (int) matched);
            }
            return stack;
        }

        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate)
        {
            return ItemStack.EMPTY;
        }

        @Override
        public int getSlotLimit(int slot)
        {
            return Integer.MAX_VALUE;
        }
    }

    public class ResearchEnergyWrapper implements IEnergyStorage
    {

        @Override
        public int receiveEnergy(int maxReceive, boolean simulate)
        {
            if (canReceive())
            {
                return (int) match(ConditionTypes.ENERGY, (long) maxReceive, simulate);
            }
            return 0;
        }

        @Override
        public int extractEnergy(int maxExtract, boolean simulate)
        {
            return 0;
        }

        @Override
        public int getEnergyStored()
        {
            return 0;
        }

        @Override
        public int getMaxEnergyStored()
        {
            return 0;
        }

        @Override
        public boolean canExtract()
        {
            return false;
        }

        @Override
        public boolean canReceive()
        {
            return research != null && !canComplete;
        }

    }

    public class ResearchFluidWrapper implements IFluidHandler
    {
        private final FluidTankProperties info;

        public ResearchFluidWrapper()
        {
            info = new FluidTankProperties();
        }

        @Override
        public IFluidTankProperties[] getTankProperties()
        {
            return new IFluidTankProperties[] { info };
        }

        @Override
        public int fill(FluidStack resource, boolean doFill)
        {
            if (research != null && !canComplete)
            {
                return (int) match(ConditionTypes.FLUID, resource, !doFill);
            }
            return 0;
        }

        @Override
        public FluidStack drain(FluidStack resource, boolean doDrain)
        {
            return null;
        }

        @Override
        public FluidStack drain(int maxDrain, boolean doDrain)
        {
            return null;
        }

    }

    private class FluidTankProperties implements IFluidTankProperties
    {

        @Override
        @Nullable
        public FluidStack getContents()
        {
            return null;
        }

        @Override
        public int getCapacity()
        {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean canFill()
        {
            return research != null && !canComplete;
        }

        @Override
        public boolean canDrain()
        {
            return false;
        }

        @Override
        public boolean canFillFluidType(FluidStack fluidStack)
        {
            return canFill() && match(ConditionTypes.FLUID, fluidStack, true) > 0;
        }

        @Override
        public boolean canDrainFluidType(FluidStack fluidStack)
        {
            return false;
        }

    }

    @Nullable
    private Research research;
    @Nullable
    private long[] progress;
    public boolean hasChanged;
    public String ownerName;
    private ResearchItemWrapper itemHandler = new ResearchItemWrapper();
    private ResearchEnergyWrapper energyHandler = new ResearchEnergyWrapper();
    private ResearchFluidWrapper fluidHandler = new ResearchFluidWrapper();
    private boolean canComplete;

    public TileTable()
    {
    }

    @Nullable
    public Research getResearch()
    {
        return research;
    }

    public void setResearch(@Nullable Research research)
    {
        if (this.research != research)
        {
            this.research = research;
            if (research == null)
            {
                progress = null;
            }
            else
            {
                progress = new long[research.getConditions().size()];
            }
            canComplete = false;
        }
    }

    @Override
    protected void readPacketData(NBTTagCompound data)
    {
        if (data.hasKey("owner", Constants.NBT.TAG_STRING))
        {
            ownerName = data.getString("owner");
        }
        if (data.hasKey("research", Constants.NBT.TAG_STRING))
        {
            String name = data.getString("research");
            Optional<Research> result = ResearchList.find(name);
            if (result.isPresent())
            {
                setResearch(result.get());
                for (int i = 0; i < progress.length; i++)
                {
                    if (!data.hasKey("progress" + i, Constants.NBT.TAG_LONG))
                    {
                        break;
                    }
                    progress[i] = data.getLong("progress" + i);
                }
                refreshCanComplete();
            }
        }
        else
        {
            setResearch(null);
        }
        hasChanged = true; // client
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        readPacketData(compound);
    }

    @Override
    protected NBTTagCompound writePacketData(NBTTagCompound data)
    {
        if (ownerName != null)
        {
            data.setString("owner", ownerName);
        }
        if (research != null)
        {
            data.setString("research", research.getName());
            for (int i = 0; i < progress.length; i++)
            {
                data.setLong("progress" + i, progress[i]);
            }
        }
        return data;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        writePacketData(compound);
        return compound;
    }

    public float getProgress()
    {
        if (research == null)
        {
            return 0;
        }
        List<ICondition> conditions = research.getConditions();
        double p = 0, sum = 0;
        for (int i = 0; i < conditions.size(); i++)
        {
            p += progress[i];
            sum += conditions.get(i).getGoal();
        }
        return sum == 0 ? 0 : (float) (p / sum) * 100;
    }

    public long getProgress(int index)
    {
        if (progress != null && index >= 0 && index < progress.length)
        {
            return progress[index];
        }
        return 0;
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing)
    {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
        {
            return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(itemHandler);
        }
        if (capability == CapabilityEnergy.ENERGY)
        {
            return CapabilityEnergy.ENERGY.cast(energyHandler);
        }
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
        {
            return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(fluidHandler);
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing)
    {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || capability == CapabilityEnergy.ENERGY || capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
    }

    public boolean hasPermission(EntityPlayer player)
    {
        if (ownerName == null || ownerName.isEmpty())
        {
            return true;
        }
        return player.getName().equals(ownerName);
    }

    public boolean canComplete()
    {
        return canComplete;
    }

    private void refreshCanComplete()
    {
        if (research == null)
        {
            canComplete = false;
            return;
        }
        List<ICondition> conditions = research.getConditions();
        for (int i = 0; i < progress.length; i++)
        {
            if (conditions.get(i).getGoal() > progress[i])
            {
                canComplete = false;
                return;
            }
        }
        canComplete = true;
    }

    public void complete(EntityPlayer player)
    {
        if (research == null || world.isRemote)
        {
            return;
        }
        if (ownerName != null)
        {
            DataStorage.complete(ownerName, research);
        }
        else
        {
            DataStorage.complete(player.getName(), research);
        }
        research.complete(world, pos, player);
        setResearch(null);
        hasChanged = true; // server
    }

    public void submit(EntityPlayer player)
    {
        // TODO: insert null NBT items first
        if (research == null || world.isRemote)
        {
            return;
        }
        for (int i = 0; i < player.inventory.mainInventory.size(); ++i)
        {
            ItemStack stack = player.inventory.mainInventory.get(i);
            ItemStack remain = itemHandler.insertItem(0, stack, false);
            if (remain != stack)
            {
                player.inventory.mainInventory.set(i, remain);
            }
        }
    }

    public <T> long match(Supplier<Class<T>> type, T e, boolean simulate)
    {
        List<ICondition> conditions = research.getConditions();
        long matched = 0;
        for (int i = 0; i < conditions.size(); ++i)
        {
            ICondition condition = conditions.get(i);
            if (condition.getMatchType() == type)
            {
                long matchedIn = condition.matches(e);
                if (matchedIn < 0)
                {
                    matchedIn = 0;
                }
                if (matchedIn > condition.getGoal() - progress[i])
                {
                    matchedIn = condition.getGoal() - progress[i];
                }
                if (matched + matchedIn < matched)
                {
                    matchedIn = Long.MAX_VALUE - matched;
                }
                matched += matchedIn;
                if (matchedIn > 0 && !simulate)
                {
                    progress[i] += matchedIn;
                }
                if (matched == Long.MAX_VALUE)
                {
                    break;
                }
            }
        }
        if (matched > 0 && !simulate)
        {
            refreshCanComplete();
            hasChanged = true;
        }
        return matched;
    }
}

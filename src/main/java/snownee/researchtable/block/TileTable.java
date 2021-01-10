package snownee.researchtable.block;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.base.Objects;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.UsernameCache;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.FakePlayer;
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
import snownee.researchtable.ResearchTable;
import snownee.researchtable.core.ConditionTypes;
import snownee.researchtable.core.DataStorage;
import snownee.researchtable.core.ICondition;
import snownee.researchtable.core.Research;
import snownee.researchtable.core.ResearchList;
import snownee.researchtable.core.team.TeamHelper;

public class TileTable extends TileBase {

    public class ResearchItemWrapper implements IItemHandler {

        ResearchItemWrapper() {
        }

        @Override
        public int getSlots() {
            return research != null && !canComplete ? 1 : 0;
        }

        @Override
        public ItemStack getStackInSlot(int slot) {
            return ItemStack.EMPTY;
        }

        @Override
        public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
            if (slot == 0 && research != null && !stack.isEmpty() && !canComplete) {
                long matched = match(ConditionTypes.ITEM, stack, simulate);
                return ItemHandlerHelper.copyStackWithSize(stack, stack.getCount() - (int) matched);
            }
            return stack;
        }

        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            return ItemStack.EMPTY;
        }

        @Override
        public int getSlotLimit(int slot) {
            return Integer.MAX_VALUE;
        }
    }

    public class ResearchEnergyWrapper implements IEnergyStorage {

        @Override
        public int receiveEnergy(int maxReceive, boolean simulate) {
            if (canReceive()) {
                return (int) match(ConditionTypes.ENERGY, (long) maxReceive, simulate);
            }
            return 0;
        }

        @Override
        public int extractEnergy(int maxExtract, boolean simulate) {
            return 0;
        }

        @Override
        public int getEnergyStored() {
            return 0;
        }

        @Override
        public int getMaxEnergyStored() {
            return 0;
        }

        @Override
        public boolean canExtract() {
            return false;
        }

        @Override
        public boolean canReceive() {
            return research != null && !canComplete;
        }

    }

    public class ResearchFluidWrapper implements IFluidHandler {
        private final FluidTankProperties info;

        public ResearchFluidWrapper() {
            info = new FluidTankProperties();
        }

        @Override
        public IFluidTankProperties[] getTankProperties() {
            return new IFluidTankProperties[] { info };
        }

        @Override
        public int fill(FluidStack resource, boolean doFill) {
            if (research != null && !canComplete) {
                return (int) match(ConditionTypes.FLUID, resource, !doFill);
            }
            return 0;
        }

        @Override
        public FluidStack drain(FluidStack resource, boolean doDrain) {
            return null;
        }

        @Override
        public FluidStack drain(int maxDrain, boolean doDrain) {
            return null;
        }

    }

    private class FluidTankProperties implements IFluidTankProperties {

        @Override
        @Nullable
        public FluidStack getContents() {
            return null;
        }

        @Override
        public int getCapacity() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean canFill() {
            return research != null && !canComplete;
        }

        @Override
        public boolean canDrain() {
            return false;
        }

        @Override
        public boolean canFillFluidType(FluidStack fluidStack) {
            return canFill() && match(ConditionTypes.FLUID, fluidStack, true) > 0;
        }

        @Override
        public boolean canDrainFluidType(FluidStack fluidStack) {
            return false;
        }

    }

    @Nullable
    private Research research;
    @Nullable
    private Research lastResearch;
    @Nullable
    private long[] progress;
    public boolean hasChanged;
    @Nonnull
    public String ownerName;
    @Nonnull
    private UUID ownerUUID;
    private ResearchItemWrapper itemHandler = new ResearchItemWrapper();
    private ResearchEnergyWrapper energyHandler = new ResearchEnergyWrapper();
    private ResearchFluidWrapper fluidHandler = new ResearchFluidWrapper();
    private boolean canComplete;
    private NBTTagCompound data = new NBTTagCompound();
    public boolean powered;

    @Nullable
    public Research getResearch() {
        return research;
    }

    @Nullable
    public Research getLastResearch() {
        return lastResearch;
    }

    public UUID getOwnerUUID() {
        return this.ownerUUID;
    }

    public void setOwnerUUID(UUID uuid) {
        if (this.ownerUUID == null) {
            this.ownerUUID = uuid;
        } else {
            ResearchTable.logger.debug("An attempt of re-setting research table owner uuid occurred. Action aborted.");
        }
    }

    public void setResearch(@Nullable Research research) {
        if (this.research == research) {
            return;
        }
        if (this.research != null) {
            lastResearch = this.research;
        }
        this.research = research;
        if (research == null) {
            progress = null;
        } else {
            progress = new long[research.getConditions().size()];
        }
        refreshCanComplete();
    }

    @Override
    protected void readPacketData(NBTTagCompound tag) {
        if (tag.hasKey("data", Constants.NBT.TAG_COMPOUND)) {
            data = tag.getCompoundTag("data");
        }
        if (tag.hasKey("owner", Constants.NBT.TAG_COMPOUND)) {
            NBTTagCompound credential = tag.getCompoundTag("owner");
            this.ownerName = credential.getString("name");
            this.ownerUUID = NBTUtil.getUUIDFromTag(credential.getCompoundTag("uuid"));
        } else {
            ResearchTable.logger.error("Invalid table data: pos={} tag={}", pos, tag);
            invalidate();
            // TODO Warn about missing owner info
        }
        if (tag.hasKey("research", Constants.NBT.TAG_STRING)) {
            String name = tag.getString("research");
            Optional<Research> result = ResearchList.find(name);
            if (result.isPresent()) {
                setResearch(result.get());
                for (int i = 0; i < progress.length; i++) {
                    if (!tag.hasKey("progress" + i, Constants.NBT.TAG_LONG)) {
                        continue;
                    }
                    progress[i] = tag.getLong("progress" + i);
                }
                refreshCanComplete();
            }
        } else {
            setResearch(null);
        }
        hasChanged = true; // client
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        if (compound.hasKey("last", Constants.NBT.TAG_STRING)) {
            lastResearch = ResearchList.find(compound.getString("last")).orElse(null);
        }
        if (compound.hasKey("powered", Constants.NBT.TAG_BYTE)) {
            powered = compound.getBoolean("powered");
        }
        readPacketData(compound);
    }

    @Override
    protected NBTTagCompound writePacketData(NBTTagCompound tag) {
        NBTTagCompound credential = new NBTTagCompound();
        if (ownerName != null || ownerUUID != null) {
            if (ownerName != null) {
                credential.setString("name", ownerName);
            }
            if (ownerUUID != null) {
                credential.setTag("uuid", NBTUtil.createUUIDTag(this.ownerUUID));
            }
        }
        tag.setTag("owner", credential);
        if (research != null) {
            tag.setString("research", research.getName());
            for (int i = 0; i < progress.length; i++) {
                tag.setLong("progress" + i, progress[i]);
            }
        }
        tag.setTag("data", data);
        return tag;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        writePacketData(compound);
        if (lastResearch != null) {
            compound.setString("last", lastResearch.getName());
        }
        compound.setBoolean("powered", powered);
        return compound;
    }

    public float getProgress() {
        if (research == null) {
            return 0;
        }
        List<ICondition> conditions = research.getConditions();
        if (conditions.isEmpty()) {
            return 100;
        }
        double sum = 0;
        for (int i = 0; i < conditions.size(); i++) {
            if (conditions.get(i).getGoal() == 0) {
                continue;
            }
            sum += (double) progress[i] / conditions.get(i).getGoal();
        }
        return (float) (sum / conditions.size()) * 100;
    }

    public long getProgress(int index) {
        if (progress != null && index >= 0 && index < progress.length) {
            return progress[index];
        }
        return 0;
    }

    public NBTTagCompound getData() {
        return data;
    }

    public void setData(NBTTagCompound data) {
        this.data = data;
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(itemHandler);
        }
        if (capability == CapabilityEnergy.ENERGY) {
            return CapabilityEnergy.ENERGY.cast(energyHandler);
        }
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(fluidHandler);
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || capability == CapabilityEnergy.ENERGY || capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
    }

    public boolean hasPermission(@Nullable EntityPlayer player) {
        if (player == null || player.world.isRemote) // Minecraft.player sometimes can be null
        {
            return true;
        }
        return player.getGameProfile().getId().equals(this.ownerUUID) || Objects.equal(TeamHelper.provider.getOwner(player.getGameProfile().getId()), ownerUUID);

        //ResearchTable.logger.warn("Player {} ('{}', UUID '{}') tried to access this table with owner of '{}' (UUID: '{}') but failed. This may be a bug.", player.getName(), player, player.getUniqueID(), this.ownerName, this.ownerUUID);
    }

    public boolean canComplete() {
        return canComplete;
    }

    private void refreshCanComplete() {
        if (research == null) {
            canComplete = false;
            markDirty();
            return;
        }
        List<ICondition> conditions = research.getConditions();
        for (int i = 0; i < progress.length; i++) {
            if (conditions.get(i).getGoal() > progress[i]) {
                canComplete = false;
                markDirty();
                return;
            }
        }
        canComplete = true;
        markDirty();
    }

    public void complete(EntityPlayer player) {
        if (research == null || world.isRemote) {
            return;
        }
        if (DataStorage.complete(ownerUUID, research) > 0) {
            research.complete(world, pos, player);
            hasChanged = true; // server
        }
        setResearch(null);
    }

    public void submit(EntityPlayer player) {
        // TODO: insert null NBT items first
        if (research == null || world.isRemote) {
            return;
        }
        for (int i = 0; i < player.inventory.mainInventory.size(); ++i) {
            ItemStack stack = player.inventory.mainInventory.get(i);
            ItemStack remain = itemHandler.insertItem(0, stack, false);
            if (remain != stack) {
                player.inventory.mainInventory.set(i, remain);
            }
        }
    }

    public <T> long match(Supplier<Class<T>> type, T e, boolean simulate) {
        List<ICondition> conditions = research.getConditions();
        long matched = 0;
        for (int i = 0; i < conditions.size(); ++i) {
            ICondition condition = conditions.get(i);
            if (condition.getMatchType() == type) {
                long matchedIn = condition.matches(e);
                if (matchedIn < 0) {
                    matchedIn = 0;
                }
                if (matchedIn > condition.getGoal() - progress[i]) {
                    matchedIn = condition.getGoal() - progress[i];
                }
                if (matched + matchedIn < matched) {
                    matchedIn = Long.MAX_VALUE - matched;
                }
                matched += matchedIn;
                if (matchedIn > 0 && !simulate) {
                    progress[i] += matchedIn;
                }
                if (matched == Long.MAX_VALUE) {
                    break;
                }
            }
        }
        if (matched > 0 && !simulate) {
            refreshCanComplete();
            hasChanged = true;
        }
        return matched;
    }

    public void putOwnerInfo(EntityPlayer player) {
        if (player instanceof FakePlayer) {
            return;
        }
        UUID uuid = player.getGameProfile().getId();
        UUID owner = TeamHelper.provider.getOwner(uuid);
        if (owner != null) {
            ownerName = TeamHelper.provider.getTeamName(owner);
            if (ownerName == null) {
                ownerName = UsernameCache.getLastKnownUsername(owner);
            }
            if (ownerName == null) {
                ownerName = player.getName();
            }
            setOwnerUUID(owner);
        } else {
            ownerName = player.getName();
            setOwnerUUID(uuid);
        }
    }

}

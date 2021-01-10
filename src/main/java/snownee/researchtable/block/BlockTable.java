package snownee.researchtable.block;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.ItemHandlerHelper;
import snownee.kiwi.block.BlockModHorizontal;
import snownee.kiwi.util.NBTHelper;
import snownee.kiwi.util.Util;
import snownee.researchtable.ModConfig;
import snownee.researchtable.ResearchTable;
import snownee.researchtable.core.EventOpenTable;
import snownee.researchtable.core.Research;

public class BlockTable extends BlockModHorizontal {
    private static final AxisAlignedBB AABB = new AxisAlignedBB(0.1, 0, 0.1, 0.9, 0.9, 0.9);

    public BlockTable(String name, Material blockMaterial) {
        super(name, blockMaterial);
        setCreativeTab(CreativeTabs.DECORATIONS);
        setLightLevel(0.5F);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void mapModel() {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "facing=north"));
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }

    @Override
    public BlockRenderLayer getRenderLayer() {
        return ModConfig.renderLayer;
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileTable();
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack stack = playerIn.getHeldItem(hand);
        ItemStack copy = ItemHandlerHelper.copyStackWithSize(stack, 1);
        IFluidHandlerItem fluidHandler = FluidUtil.getFluidHandler(copy);
        if (fluidHandler != null) {
            IFluidHandler fluidDestination = FluidUtil.getFluidHandler(worldIn, pos, facing);
            if (fluidDestination != null) {
                FluidStack transferred = FluidUtil.tryFluidTransfer(fluidDestination, fluidHandler, Integer.MAX_VALUE, true);
                if (transferred != null && !playerIn.isCreative()) {
                    if (stack.getCount() > 1) {
                        stack.shrink(1);
                        ItemHandlerHelper.giveItemToPlayer(playerIn, fluidHandler.getContainer());
                    } else {
                        playerIn.setHeldItem(hand, fluidHandler.getContainer());
                    }
                }
            }
        } else {
            TileEntity tile = worldIn.getTileEntity(pos);
            if (tile instanceof TileTable) {
                TileTable table = (TileTable) tile;
                if (!table.hasPermission(playerIn)) {
                    playerIn.sendMessage(new TextComponentTranslation(ResearchTable.MODID + ".noPermission"));
                } else if (!worldIn.isRemote && !MinecraftForge.EVENT_BUS.post(new EventOpenTable(playerIn, table))) {
                    table.putOwnerInfo(playerIn);
                    playerIn.openGui(ResearchTable.getInstance(), 0, worldIn, pos.getX(), pos.getY(), pos.getZ());
                }
            }
        }
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        NBTTagCompound compound = stack.getTagCompound();
        if (compound != null) {
            if (compound.hasKey("BlockEntityTag", Constants.NBT.TAG_COMPOUND)) {
                String ownerName = null;
                NBTTagCompound tileCompound = compound.getCompoundTag("BlockEntityTag");
                if (tileCompound.hasKey("owner", Constants.NBT.TAG_COMPOUND)) {
                    ownerName = NBTHelper.of(tileCompound).getString("owner.name");
                } else if (tileCompound.hasKey("owner", Constants.NBT.TAG_STRING)) {
                    ownerName = tileCompound.getString("owner");
                }
                if (ownerName != null) {
                    tooltip.add(I18n.format(ResearchTable.MODID + ".gui.owner", TextFormatting.RESET + ownerName + TextFormatting.GRAY));
                }
            }
            if (compound.hasKey("title", Constants.NBT.TAG_STRING)) {
                String title = compound.getString("title");
                if (I18n.hasKey(title)) {
                    title = I18n.format(title);
                }
                tooltip.add(I18n.format(ResearchTable.MODID + ".gui.researching", TextFormatting.RESET + title + TextFormatting.GRAY));
                if (compound.hasKey("progress", Constants.NBT.TAG_FLOAT)) {
                    float progress = compound.getFloat("progress");
                    tooltip.add(I18n.format(ResearchTable.MODID + ".gui.progress", TextFormatting.RESET + Util.MESSAGE_FORMAT.format(new Float[] { progress }) + "%" + TextFormatting.GRAY));
                }
            }
        }
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        ItemStack stack = new ItemStack(this);
        TileEntity tile = worldIn.getTileEntity(pos);
        if (tile instanceof TileTable) {
            TileTable table = ((TileTable) tile);
            NBTTagCompound compound = new NBTTagCompound();
            NBTTagCompound tileCompound = table.writeToNBT(new NBTTagCompound());
            tileCompound.removeTag("x");
            tileCompound.removeTag("y");
            tileCompound.removeTag("z");
            tileCompound.removeTag("id");
            compound.setTag("BlockEntityTag", tileCompound);
            Research research = table.getResearch();
            if (research != null) {
                compound.setString("title", research.getTitleRaw());
                compound.setFloat("progress", table.getProgress());
            }
            stack.setTagCompound(compound);
        }
        spawnAsEntity(worldIn, pos, stack);
        if (tile != null) {
            worldIn.removeTileEntity(pos);
        }
    }

    @Override
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        // NO-OP
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        if (!worldIn.isRemote) {
            TileEntity tile = worldIn.getTileEntity(pos);
            if (tile instanceof TileTable) {
                ((TileTable) tile).putOwnerInfo((EntityPlayer) placer);
            }
        }
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return AABB;
    }

    @Override
    public boolean hasComparatorInputOverride(IBlockState state) {
        return true;
    }

    @Override
    public int getComparatorInputOverride(IBlockState blockState, World worldIn, BlockPos pos) {
        TileEntity tile = worldIn.getTileEntity(pos);
        if (tile instanceof TileTable) {
            TileTable table = (TileTable) tile;
            if (table.getResearch() == null) {
                return 0;
            }
            if (table.canComplete()) {
                return 15;
            }
            return 1 + MathHelper.ceil(table.getProgress() * 0.13f);
        }
        return 0;
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        TileEntity tile = worldIn.getTileEntity(pos);
        if (tile instanceof TileTable) {
            TileTable table = (TileTable) tile;
            boolean powered = worldIn.isBlockPowered(pos) || worldIn.isBlockPowered(pos.up());
            if (powered && !table.powered) {
                // TODO
            }
            table.powered = powered;
        }
    }
}

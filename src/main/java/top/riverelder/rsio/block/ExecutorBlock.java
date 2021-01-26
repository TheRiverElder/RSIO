package top.riverelder.rsio.block;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.common.util.Constants;
import top.riverelder.rsio.RSIOMod;
import top.riverelder.rsio.item.AllItems;
import top.riverelder.rsio.tileentity.ExecutorTileEntity;

import javax.annotation.Nullable;

public class ExecutorBlock extends Block {

    public static final BooleanProperty HAS_CONTENT = BooleanProperty.create("has_content");
    public static final BooleanProperty RUNNING = BooleanProperty.create("running");

    public ExecutorBlock() {
        super(Properties.create(Material.IRON)
                .harvestTool(ToolType.PICKAXE)
                .hardnessAndResistance(1, 6)
        );
        this.setRegistryName(new ResourceLocation(RSIOMod.NAME, "executor"));
        setDefaultState(this.getStateContainer().getBaseState().with(HAS_CONTENT, false));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(HAS_CONTENT);
        super.fillStateContainer(builder);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new ExecutorTileEntity();
    }

    @Override
    public void onBlockClicked(BlockState blockState, World worldIn, BlockPos blockPos, PlayerEntity player) {
        if (player.getHeldItemMainhand().getHarvestLevel(ToolType.PICKAXE, player, blockState) < blockState.getHarvestLevel()) {
            player.playSound(SoundEvents.BLOCK_STONE_BUTTON_CLICK_OFF, 1, 1);
        }
    }

    @Override
    public ActionResultType onBlockActivated(BlockState blockState, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        if (hand != Hand.MAIN_HAND) return ActionResultType.PASS;

        ItemStack heldItem = player.getHeldItemMainhand();
        TileEntity te = world.getTileEntity(pos);
        if (!(te instanceof ExecutorTileEntity)) {
            return ActionResultType.FAIL;
        }
        ExecutorTileEntity ete = (ExecutorTileEntity) te;

        ActionResultType result = ActionResultType.PASS;

        if (heldItem.isEmpty() && player.isSneaking() && ete.hasContent()) {
            heldItem = new ItemStack(AllItems.CHIP, 1);
            AllItems.CHIP.setData(heldItem, ete.getTitle(), ete.getAuthor(), ete.getBytes());

            player.setHeldItem(Hand.MAIN_HAND, heldItem);
            ete.clear();

            result = ActionResultType.SUCCESS;
        } else if (AllItems.CHIP.hasContent(heldItem) && !ete.hasContent()) {
            String title = AllItems.CHIP.getTitle(heldItem);
            String author = AllItems.CHIP.getAuthor(heldItem);
            byte[] bytes = AllItems.CHIP.getBytes(heldItem);
            ete.setData(title, author, bytes, true, false);

            heldItem.setCount(0);

            result = ActionResultType.CONSUME;
        } else {
            ete.setRunning(!ete.isRunning());
        }

        setState(world, pos, blockState, ete.hasContent(), ete.isRunning());

        return result;
    }

    public void setState(World world, BlockPos pos, BlockState state, boolean hasContent, boolean running) {
        world.setBlockState(pos, state.with(HAS_CONTENT, hasContent).with(RUNNING, running), Constants.BlockFlags.BLOCK_UPDATE);
    }

    @Override
    public void onBlockHarvested(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
        super.onBlockHarvested(worldIn, pos, state, player);
        dropChip(worldIn, pos);
    }

    @Override
    public void onExplosionDestroy(World worldIn, BlockPos pos, Explosion explosionIn) {
        dropChip(worldIn, pos);
    }

    public void dropChip(World worldIn, BlockPos pos) {
        TileEntity te = worldIn.getTileEntity(pos);
        if (!(te instanceof ExecutorTileEntity)) return;
        ExecutorTileEntity ete = (ExecutorTileEntity) te;

        ItemStack chip = ete.crateChip();
        if (chip == null) return;

        worldIn.addEntity(new ItemEntity(
                worldIn,
                pos.getX() + worldIn.getRandom().nextDouble(),
                pos.getY() + worldIn.getRandom().nextDouble(),
                pos.getZ() + worldIn.getRandom().nextDouble(),
                chip)
        );
    }
}
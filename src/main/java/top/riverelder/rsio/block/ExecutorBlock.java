package top.riverelder.rsio.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import top.riverelder.rsio.RSIOMod;

import javax.annotation.Nullable;

public class ExecutorBlock extends Block {
    public ExecutorBlock() {
        super(Properties.create(Material.IRON)
                .harvestTool(ToolType.PICKAXE)
                .hardnessAndResistance(1, 6)
        );
        this.setRegistryName(new ResourceLocation(RSIOMod.NAME, "executor"));
    }

    @Override
    public void onBlockClicked(BlockState blockState, World worldIn, BlockPos blockPos, PlayerEntity player) {
        if (player.getHeldItemMainhand().getHarvestLevel(ToolType.PICKAXE, player, blockState) < blockState.getHarvestLevel()) {
            player.playSound(SoundEvents.BLOCK_STONE_BUTTON_CLICK_OFF, 1, 1);
        }
    }

    @Override
    public ActionResultType onBlockActivated(BlockState blockState, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        if (world.isBlockPowered(pos)) {
            player.playSound(SoundEvents.BLOCK_STONE_BUTTON_CLICK_ON, 1, 1);
        } else {
            player.playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 1, 1);
        }
        return ActionResultType.SUCCESS;
    }


}
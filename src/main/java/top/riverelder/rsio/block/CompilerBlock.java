package top.riverelder.rsio.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.DistExecutor;
import top.riverelder.rsio.item.AllItems;
import top.riverelder.rsio.tileentity.CompilerTileEntity;
import top.riverelder.rsio.util.OpenGui;

import javax.annotation.Nullable;

public class CompilerBlock extends Block {

    public CompilerBlock() {
        super(Properties.create(Material.IRON)
                .harvestTool(ToolType.PICKAXE)
                .harvestLevel(2)
                .hardnessAndResistance(2, 6)
                .setRequiresTool()
        );
        this.setRegistryName("rsio:compiler");
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new CompilerTileEntity();
    }

    @Override
    public ActionResultType onBlockActivated(BlockState blockState, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        player.playSound(SoundEvents.BLOCK_STONE_BUTTON_CLICK_ON, 1, 1);

        ItemStack heldItem = player.getHeldItemMainhand();

        TileEntity te = world.getTileEntity(pos);
        if (te instanceof CompilerTileEntity && heldItem.getItem() == AllItems.CHIP) {
            CompilerTileEntity cte = (CompilerTileEntity) te;
            byte[] bytes = cte.getBytes();
            if (bytes != null) {
                return AllItems.CHIP.setData(heldItem, cte.getTitle(), cte.getAuthor(), bytes) ? ActionResultType.SUCCESS : ActionResultType.FAIL;
            }
        }

        if (world.isRemote && hand == Hand.MAIN_HAND) {
            DistExecutor.runWhenOn(Dist.CLIENT, () -> (() -> OpenGui.openGui(this, world, pos)));
        }
        return ActionResultType.SUCCESS;
    }

    public CompilerTileEntity getCompilerTileEntity(World world, BlockPos pos) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof CompilerTileEntity) {
            CompilerTileEntity compilerTileEntity = (CompilerTileEntity) tileEntity;
            return compilerTileEntity;
        }
        return null;
    }
}

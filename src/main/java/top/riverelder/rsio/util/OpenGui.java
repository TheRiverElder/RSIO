package top.riverelder.rsio.util;

import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import top.riverelder.rsio.gui.CompilerScreen;
import top.riverelder.rsio.block.CompilerBlock;
import top.riverelder.rsio.tileentity.CompilerTileEntity;

public class OpenGui {
    public static void openGui(CompilerBlock compiler, World world, BlockPos pos) {
        Minecraft.getInstance().displayGuiScreen(new CompilerScreen(
                        new StringTextComponent("test"),
                        () -> compiler.getCompilerTileEntity(world, pos)
                )
        );
    }
}

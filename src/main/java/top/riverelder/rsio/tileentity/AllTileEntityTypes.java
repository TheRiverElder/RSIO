package top.riverelder.rsio.tileentity;

import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import top.riverelder.rsio.RSIOMod;
import top.riverelder.rsio.block.AllBlocks;

public class AllTileEntityTypes {

    public static final TileEntityType<?> COMPILER =
            TileEntityType.Builder
                    .create(CompilerTileEntity::new, AllBlocks.COMPILER)
                    .build(null)
            .setRegistryName(new ResourceLocation(RSIOMod.NAME, "compiler"));

}

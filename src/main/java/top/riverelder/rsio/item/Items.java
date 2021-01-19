package top.riverelder.rsio.item;

import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import top.riverelder.rsio.block.Blocks;

public class Items {

    public static Item COMPILER = new BlockItem(Blocks.COMPILER,
            new Item.Properties().group(ItemGroup.DECORATIONS)
            ).setRegistryName(Blocks.COMPILER.getRegistryName());

    public static Item EXECUTOR = new BlockItem(Blocks.EXECUTOR,
            new Item.Properties().group(ItemGroup.DECORATIONS)
            ).setRegistryName(Blocks.EXECUTOR.getRegistryName());

    public static Item CHIP = new ChipItem();
}

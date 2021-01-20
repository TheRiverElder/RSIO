package top.riverelder.rsio.item;

import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import top.riverelder.rsio.block.AllBlocks;

public class AllItems {

    public static Item COMPILER = new BlockItem(AllBlocks.COMPILER,
            new Item.Properties().group(ItemGroup.DECORATIONS)
            ).setRegistryName(AllBlocks.COMPILER.getRegistryName());

    public static Item EXECUTOR = new BlockItem(AllBlocks.EXECUTOR,
            new Item.Properties().group(ItemGroup.DECORATIONS)
            ).setRegistryName(AllBlocks.EXECUTOR.getRegistryName());

    public static Item CHIP = new ChipItem();
}

package top.riverelder.rsio.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import top.riverelder.rsio.RSIOMod;
import top.riverelder.rsio.util.OpenGui;

public class ChipItem extends Item {
    public ChipItem() {
        super(new Properties().group(ItemGroup.MISC));
        this.setRegistryName(new ResourceLocation(RSIOMod.NAME, "chip"));
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }
}

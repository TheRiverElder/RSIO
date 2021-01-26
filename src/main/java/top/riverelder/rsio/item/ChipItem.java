package top.riverelder.rsio.item;

import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.ByteArrayNBT;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import top.riverelder.rsio.RSIOMod;
import top.riverelder.rsio.util.TagKey;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

public class ChipItem extends Item {
    public ChipItem() {
        super(new Properties().group(ItemGroup.MISC).maxStackSize(1));
        this.setRegistryName(new ResourceLocation(RSIOMod.NAME, "chip"));
    }

    public boolean setData(ItemStack stack, String title, String author, byte[] bytes) {
        if (stack.getItem() == AllItems.CHIP) {
            CompoundNBT nbt = stack.getOrCreateTag();
            nbt.putString(TagKey.TITLE, title);
            nbt.putString(TagKey.AUTHOR, author);
            nbt.putByteArray(TagKey.BYTES, bytes);
            stack.setTag(nbt);
            return true;
        }
        return false;
    }

    public String getTitle(ItemStack stack) {
        if (stack.getItem() == AllItems.CHIP && stack.hasTag() && stack.getTag().contains(TagKey.TITLE, 8)) {
            return stack.getTag().getString(TagKey.TITLE);
        }
        return null;
    }

    public String getAuthor(ItemStack stack) {
        if (stack.getItem() == AllItems.CHIP && stack.hasTag() && stack.getTag().contains(TagKey.AUTHOR, 8)) {
            return stack.getTag().getString(TagKey.AUTHOR);
        }
        return null;
    }

    public byte[] getBytes(ItemStack stack) {
        if (stack.getItem() == AllItems.CHIP && stack.hasTag() && stack.getTag().contains(TagKey.BYTES, 7)) {
            return stack.getTag().getByteArray(TagKey.BYTES);
        }
        return null;
    }

    public boolean clear(ItemStack stack) {
        if (stack.getItem() == AllItems.CHIP) {
            stack.removeChildTag(TagKey.TITLE);
            stack.removeChildTag(TagKey.AUTHOR);
            stack.removeChildTag(TagKey.BYTES);
            return true;
        }
        return false;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        if (playerIn.isSneaking()) {
            ItemStack stack = playerIn.getHeldItemMainhand();
            clear(stack);
            return ActionResult.resultSuccess(stack);
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    public boolean hasContent(ItemStack stack) {
        return stack.getItem() == AllItems.CHIP && stack.hasTag() && stack.getTag().contains(TagKey.BYTES, 7);
    }

    @Override
    public boolean hasEffect(ItemStack stack) {
        return hasContent(stack);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if (hasContent(stack)) {
            tooltip.add(new TranslationTextComponent("text." + RSIOMod.NAME + ".program_title", getTitle(stack)));
            tooltip.add(new TranslationTextComponent("text." + RSIOMod.NAME + ".program_author", getAuthor(stack)));
            tooltip.add(new TranslationTextComponent("text." + RSIOMod.NAME + ".program_size", getBytes(stack).length));
        }
    }
}

package top.riverelder.rsio.tileentity;

import net.minecraft.block.BlockState;
import net.minecraft.block.RedstoneDiodeBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.Constants;
import top.riverelder.rsio.item.AllItems;
import top.riverelder.rsio.util.TagKey;

import javax.annotation.Nullable;
import java.util.Arrays;

public class ExecutorTileEntity extends TileEntity implements ITickableTileEntity {

    public static final int NORTH_STRONG = 0;
    public static final int SOUTH_STRONG = 1;
    public static final int WEST_STRONG = 2;
    public static final int EAST_STRONG = 3;
    public static final int UP_STRONG = 4;
    public static final int DOWN_STRONG = 5;
    public static final int NORTH_WEAK = 6;
    public static final int SOUTH_WEAK = 7;
    public static final int WEST_WEAK = 8;
    public static final int EAST_WEAK = 9;
    public static final int UP_WEAK = 10;
    public static final int DOWN_WEAK = 11;

    private String title = "";
    private String author = "";
    private byte[] bytes = new byte[0];
    private boolean hasContent = false;
    private boolean running = false;

//    private ;

    public ExecutorTileEntity() {
        super(AllTileEntityTypes.EXECUTOR);
    }

    private void notifyUpdate() {
        markDirty();
        if (this.world != null && !this.world.isRemote) {
            this.world.notifyBlockUpdate(this.pos, this.getBlockState(), this.getBlockState(), Constants.BlockFlags.BLOCK_UPDATE);
        }
    }

    public void setData(String title, String author, byte[] bytes, boolean hasContent, boolean isInternal) {
        this.title = title == null ? "" : title;
        this.author = author == null ? "" : author;
        this.bytes = bytes == null ? new byte[0] : bytes;
        this.hasContent = hasContent;
        this.running = false;

        if (!isInternal) {
            notifyUpdate();
        }
    }

    private void setInternalData(String title, String author, byte[] bytes, boolean hasContent, boolean running) {
        this.title = title == null ? "" : title;
        this.author = author == null ? "" : author;
        this.bytes = bytes == null ? new byte[0] : bytes;
        this.hasContent = hasContent;
        this.running = running;

        notifyUpdate();
    }

    public void setRunning(boolean running) {
        this.running = running;

        notifyUpdate();
    }

    public boolean isRunning() {
        return running;
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(this.pos, 1, getUpdateTag());
    }

    @Override
    public CompoundNBT getUpdateTag() {
        CompoundNBT compound = super.getUpdateTag();
        compound.putString(TagKey.TITLE, title);
        compound.putString(TagKey.AUTHOR, author);
        compound.putByteArray(TagKey.BYTES, bytes);
        compound.putBoolean(TagKey.HAS_CONTENT, hasContent);
        compound.putBoolean(TagKey.RUNNING, running);
        return compound;
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        handleUpdateTag(getBlockState(), pkt.getNbtCompound());
    }

    @Override
    public void handleUpdateTag(BlockState state, CompoundNBT nbt) {
        this.setInternalData(
                nbt.getString(TagKey.TITLE),
                nbt.getString(TagKey.AUTHOR),
                nbt.getByteArray(TagKey.BYTES),
                nbt.getBoolean(TagKey.HAS_CONTENT),
                nbt.getBoolean(TagKey.RUNNING)
        );
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        this.setInternalData(
                nbt.getString(TagKey.TITLE),
                nbt.getString(TagKey.AUTHOR),
                nbt.getByteArray(TagKey.BYTES),
                nbt.getBoolean(TagKey.HAS_CONTENT),
                nbt.getBoolean(TagKey.RUNNING)
        );
        super.read(state, nbt);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        compound.putString(TagKey.TITLE, title);
        compound.putString(TagKey.AUTHOR, author);
        compound.putByteArray(TagKey.BYTES, bytes);
        compound.putBoolean(TagKey.HAS_CONTENT, hasContent);
        compound.putBoolean(TagKey.RUNNING, running);
        return super.write(compound);
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public boolean hasContent() {
        return hasContent && bytes != null;
    }

    public void clear() {
        this.setData("", "", new byte[0], false, false);
    }

    public ItemStack crateChip() {
        if (hasContent) {
            ItemStack stack = new ItemStack(AllItems.CHIP, 1);
            AllItems.CHIP.setData(stack, getTitle(), getAuthor(), getBytes());
            return stack;
        }
        return null;
    }

    public int getStrongPower(Direction face) {
        return this.world.getStrongPower(this.pos, face);
    }

    public int getStrongPower(BlockPos pos) {
        return this.world.getStrongPower(pos);
    }

    public int getWeakPower(Direction face) {
        return this.world.getBlockState(this.pos).getWeakPower(world, this.pos, face);
    }

    public void outputStrongPower(Direction face, int power) {
        power = Math.max(0, Math.min(power, 15));
    }

    @Override
    public void tick() {
        if (!hasContent()) return;

        
    }

    private void fillRedstoneState(byte[] memory) {
//        memory[NORTH_STRONG] = getStrongPower(Direction.NORTH);
    }

    private void setRedstoneState(byte[] memory) {

    }
}

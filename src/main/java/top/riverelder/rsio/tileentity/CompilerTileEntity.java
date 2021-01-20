package top.riverelder.rsio.tileentity;

import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.util.Constants;
import top.riverelder.rsio.RSIOMod;
import top.riverelder.rsio.network.CEditCodePacket;
import top.riverelder.rsio.network.Networking;

import javax.annotation.Nullable;
import java.util.Arrays;

public class CompilerTileEntity extends TileEntity {

    public static final String KEY_TITLE = "title";
    public static final String KEY_AUTHOR = "author";
    public static final String KEY_CODE = "code";
    public static final String KEY_BYTES = "bytes";

    private String title;
    private String author;
    private String code;
    private byte[] bytes;

    public CompilerTileEntity() {
        super(AllTileEntityTypes.COMPILER);
        fillNullData();
    }

    public void save(String title, String author, String code, byte[] bytes, boolean doSendToServer) {
        setData(title, author, code, bytes);
        markDirty();
        if (!world.isRemote) {
            world.notifyBlockUpdate(pos, getBlockState(), getBlockState(), Constants.BlockFlags.BLOCK_UPDATE);
        } else {
            if (doSendToServer) {
                Networking.CHANNEL.sendToServer(new CEditCodePacket(this.pos, this.title, this.author, this.code, this.bytes));
            }
            Minecraft.getInstance().player.sendMessage(new TranslationTextComponent("text." + RSIOMod.NAME + ".program_save_succeeded", title, author), null);
        }
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(this.pos, 1, getUpdateTag());
    }

    @Override
    public CompoundNBT getUpdateTag() {
        CompoundNBT compound = super.getUpdateTag();
        compound.putString(KEY_TITLE, title);
        compound.putString(KEY_AUTHOR, author);
        compound.putString(KEY_CODE, code);
        compound.putByteArray(KEY_BYTES, bytes);
        return compound;
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        handleUpdateTag(getBlockState(), pkt.getNbtCompound());
    }

    @Override
    public void handleUpdateTag(BlockState state, CompoundNBT nbt) {
        setData(
                nbt.getString(KEY_TITLE),
                nbt.getString(KEY_AUTHOR),
                nbt.getString(KEY_CODE),
                nbt.getByteArray(KEY_BYTES)
        );
    }

    public void setData(String title, String author, String code, byte[] bytes) {
        this.title = title != null ? title : "Untitled";
        this.author = author != null ? author : "Unknown";
        this.code = code != null ? code : "// Code here";
        this.bytes = bytes != null ? bytes : new byte[0];
//        System.out.println("setData " + this);
    }

    public void fillNullData() {
        this.setData(title, author, code, bytes);
    }

    public String getTitle() {
        if (title == null) {
            title = "Untitled";
            markDirty();
        }
        return title;
    }

    public String getAuthor() {
        if (author == null) {
            author = "Unknown";
            markDirty();
        }
        return author;
    }

    public String getCode() {
        if (code == null) {
            code = "// Code here";
            markDirty();
        }
        return code;
    }

    public byte[] getBytes() {
        if (bytes == null) {
            bytes = new byte[0];
            markDirty();
        }
        return bytes;
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        setData(
                nbt.getString(KEY_TITLE),
                nbt.getString(KEY_AUTHOR),
                nbt.getString(KEY_CODE),
                nbt.getByteArray(KEY_BYTES)
        );
        System.out.println("read " + this);
        super.read(state, nbt);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        compound.putString(KEY_TITLE, title);
        compound.putString(KEY_AUTHOR, author);
        compound.putString(KEY_CODE, code);
        compound.putByteArray(KEY_BYTES, bytes);
        System.out.println("write " + this);
        return super.write(compound);
    }

    @Override
    public String toString() {
        return "CompilerTileEntity{" +
                "title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", code='" + code + '\'' +
                ", bytes=" + Arrays.toString(bytes) +
                '}';
    }
}

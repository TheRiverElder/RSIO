package top.riverelder.rsio.network;

import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.network.NetworkEvent;
import top.riverelder.rsio.RSIOMod;
import top.riverelder.rsio.tileentity.CompilerTileEntity;

import java.util.Arrays;
import java.util.function.Supplier;

public class CEditCodePacket {

//    private static final Logger LOGGER = LogManager.getLogManager().getLogger(RSIOMod.NAME);

    private final BlockPos pos;
    private final String title;
    private final String author;
    private final String code;
    private final byte[] bytes;

    public CEditCodePacket(PacketBuffer buffer) {
        pos = buffer.readBlockPos();
        title = buffer.readString();
        author = buffer.readString();
        code = buffer.readString();
        bytes = buffer.readByteArray();
    }

    public CEditCodePacket(BlockPos pos, String title, String author, String code, byte[] bytes) {
        this.pos = pos;
        this.title = title;
        this.author = author;
        this.code = code;
        this.bytes = bytes;
    }

    public void toBytes(PacketBuffer buffer) {
        buffer.writeBlockPos(pos);
        buffer.writeString(title);
        buffer.writeString(author);
        buffer.writeString(code);
        buffer.writeByteArray(bytes);
    }

    public void handler(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(this::updateCode);
        ctx.get().setPacketHandled(true);
    }

    public void updateCode() {
        ServerWorld world = RSIOMod.MINECRAFT_SERVER.getWorld(World.OVERWORLD).getWorldServer();
        TileEntity te = world.getTileEntity(this.pos);
        if (!(te instanceof CompilerTileEntity)) return;
        CompilerTileEntity cte = (CompilerTileEntity) te;
        cte.save(this.title, this.author, this.code, this.bytes, false);
    }

    @Override
    public String toString() {
        return "CEditCodePacket{" +
                "pos=" + pos +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", code='" + code + '\'' +
                ", bytes=" + Arrays.toString(bytes) +
                '}';
    }
}

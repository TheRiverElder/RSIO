package top.riverelder.rsio.network;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import top.riverelder.rsio.RSIOMod;

public class Networking {

    public static SimpleChannel CHANNEL;
    public static final String VERSION = "1.0";

    public static final int ID_EDIT_CODE = 1;

    public static void registerMessage() {
        CHANNEL = NetworkRegistry.newSimpleChannel(
                new ResourceLocation(RSIOMod.NAME, "code_update"),
                () -> VERSION,
                VERSION::equals,
                VERSION::equals
        );
        CHANNEL.registerMessage(
                ID_EDIT_CODE,
                CEditCodePacket.class,
                CEditCodePacket::toBytes,
                CEditCodePacket::new,
                CEditCodePacket::handler
        );
    }



}

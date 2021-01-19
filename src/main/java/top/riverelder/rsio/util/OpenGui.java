package top.riverelder.rsio.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.StringTextComponent;
import top.riverelder.rsio.gui.CompilerScreen;

public class OpenGui {
    public static void openGui() {
        Minecraft.getInstance().displayGuiScreen(new CompilerScreen(new StringTextComponent("test")));
    }
}

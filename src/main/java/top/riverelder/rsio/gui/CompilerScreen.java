package top.riverelder.rsio.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import top.riverelder.rsio.tileentity.CompilerTileEntity;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

@OnlyIn(Dist.CLIENT)
public class CompilerScreen extends Screen {

    private ResourceLocation background = new ResourceLocation("rsio", "textures/gui/compiler_bg.png");

    private TextFieldWidget txtTitleInput;
    private MultiLineTextFieldWidget txtMultiLineCodeInput;
    private Button btnCompile;
    private Button btnClear;
    private Button btnSave;
//    private Slot sltChipIn;
//    private Slot sltChipOut;

    private Supplier<CompilerTileEntity> compilerGetter;

    public CompilerScreen(ITextComponent titleIn, @Nonnull Supplier<CompilerTileEntity> compilerGetter) {
        super(titleIn);
        this.compilerGetter = compilerGetter;
    }

    @Override
    protected void init() {
        int xMid = this.width / 2;
        int innerWidth = 300;
        int buttonWidth = 60;
        int lineHeight = 16;
        int gap = 10;
        int innerLeft = xMid - innerWidth / 2;
        int innerRight = xMid + innerWidth / 2;

        this.minecraft.keyboardListener.enableRepeatEvents(true);

        this.btnSave = new Button(innerLeft, 20, buttonWidth, lineHeight, new StringTextComponent("Save"), this::onBtnSaveClick);
        this.btnCompile = new Button(innerLeft + buttonWidth + gap, 20, buttonWidth, lineHeight, new StringTextComponent("Compile"), this::onBtnCompileClick);
        this.btnClear = new Button(innerLeft + 2 * (buttonWidth + gap), 20, buttonWidth, lineHeight, new StringTextComponent("Clear"), this::onBtnClearClick);
        this.addButton(btnSave);
        this.addButton(btnCompile);
        this.addButton(btnClear);

        this.txtTitleInput = new TextFieldWidget(
                this.font,
                innerLeft + 3 * (buttonWidth + gap),
                20,
                80,
                lineHeight,
                new StringTextComponent("Title")
        );
        this.txtTitleInput.setText("Title");
        this.children.add(this.txtTitleInput);

//        this.children.add(sltChipIn);

        this.txtMultiLineCodeInput = new MultiLineTextFieldWidget(
                this.font,
                innerLeft, 46, innerWidth, 200, lineHeight,
                new StringTextComponent("Code")
        );
        this.txtMultiLineCodeInput.setText("// Code here");
        this.children.add(this.txtMultiLineCodeInput);

        // fill data

        CompilerTileEntity compilerTileEntity = compilerGetter.get();
        if (compilerTileEntity != null) {
            txtTitleInput.setText(compilerTileEntity.getTitle());
            txtMultiLineCodeInput.setText(compilerTileEntity.getCode());
        }

        super.init();
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        int xMid = this.width / 2;

        this.renderBackground(matrixStack);
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(background);
        int textureWidth = 800;
        int textureHeight = 600;
        this.blit(matrixStack, xMid - 200, 10, 0, 0, 400, 250, textureWidth / 2, textureHeight / 2);
//        this.drawString(matrixStack, this.font, "Compiler", xMid - 10, 30, 0xeb0505);

        this.txtTitleInput.render(matrixStack, mouseX, mouseY, partialTicks);
        this.txtMultiLineCodeInput.render(matrixStack, mouseX, mouseY, partialTicks);
        this.btnCompile.render(matrixStack, mouseX, mouseY, partialTicks);
        this.btnClear.render(matrixStack, mouseX, mouseY, partialTicks);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }

    private void onBtnCompileClick(Button btn) {
        String code = txtTitleInput.getText();
        if ("".equals(code)) {
            this.minecraft.player.sendChatMessage("Hello " + minecraft.player.getDisplayName().getString());
        } else {
            this.minecraft.player.sendChatMessage("Hello " + code);
        }
    }

    private void onBtnClearClick(Button btn) {
        txtTitleInput.setText("");
        txtMultiLineCodeInput.setText("");
    }

    private void onBtnSaveClick(Button btn) {
        String title = this.txtTitleInput.getText();
        String code = this.txtMultiLineCodeInput.getText();

        CompilerTileEntity compilerTileEntity = compilerGetter.get();
//        System.out.println(compilerTileEntity);
        if (compilerTileEntity != null) {
            compilerTileEntity.save(title, this.minecraft.player.getDisplayName().getString(), code, new byte[0], true);
            this.minecraft.player.sendChatMessage("Program saved: " + title + ".");
        } else {
            this.minecraft.player.sendChatMessage("Saving failed: " + title + ".");
        }
        this.closeScreen();
    }
}

package top.riverelder.rsio.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class MultiLineTextFieldWidget extends Widget {

    private int lineHeight;
    private FontRenderer font;
    private ArrayList<TextFieldWidget> lineWidgets = new ArrayList<>();
    private int focusLineIndex;

    public MultiLineTextFieldWidget(FontRenderer font, int x, int y, int width, int height, int lineHeight, ITextComponent title) {
        super(x, y, width, height, title);
        this.font = font;
        this.lineHeight = lineHeight;
        setText("");
    }

    public String getText() {
        return lineWidgets.stream().map(lw -> lw.getText()).collect(Collectors.joining("\n"));
    }

    public void setText(@Nonnull String text) {
        String[] lines = text.split("\n");
        lineWidgets.clear();
        for (int i = 0; i < lines.length; i++) {
            lineWidgets.add(createNewLine(i, lines[i]));
        }
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        lineWidgets.forEach(lw -> lw.render(matrixStack, mouseX, mouseY, partialTicks));
//        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }

    public void resetLineWidgets() {
        for (int i = 0; i < lineWidgets.size(); i++) {
            TextFieldWidget lw = lineWidgets.get(i);
            lw.x = this.x;
            lw.y = this.y + i * lineHeight;
            lw.setWidth(this.width);
            lw.setHeight(this.lineHeight);
        }
    }

    public void focusLine(int focusLineIndex, int blurLineIndex) {
        if (focusLineIndex >= 0 && focusLineIndex < lineWidgets.size()) {
            TextFieldWidget focusLineWidget = lineWidgets.get(focusLineIndex);
            focusLineWidget.changeFocus(true);

            if (blurLineIndex >= 0 && blurLineIndex < lineWidgets.size()) {
                TextFieldWidget blurLineWidget = lineWidgets.get(blurLineIndex);
                blurLineWidget.changeFocus(false);
                focusLineWidget.setCursorPosition(blurLineWidget.getCursorPosition());
            }
        }
    }

    public void focusLine(int focusLineIndex, int blurLineIndex, int cursorPosition) {
        if (focusLineIndex >= 0 && focusLineIndex < lineWidgets.size()) {
            TextFieldWidget focusLineWidget = lineWidgets.get(focusLineIndex);
            focusLineWidget.changeFocus(true);
            if (cursorPosition < 0) {
                focusLineWidget.setCursorPositionEnd();
            } else {
                focusLineWidget.setCursorPosition(cursorPosition);
            }

            if (blurLineIndex >= 0 && blurLineIndex < lineWidgets.size()) {
                lineWidgets.get(blurLineIndex).changeFocus(false);
            }
        }
    }

    public void breakLine(int index) {
        TextFieldWidget currentLineWidget = lineWidgets.get(index);

        String ps = currentLineWidget.getText();
        String leftString = ps.substring(0, currentLineWidget.getCursorPosition());
        String newString = ps.substring(currentLineWidget.getCursorPosition());
        currentLineWidget.setText(leftString);

        TextFieldWidget newLineWidget = createNewLine(index + 1, newString);
        lineWidgets.add(index + 1, newLineWidget);
        currentLineWidget.changeFocus(false);
//        currentLineWidget.setFocused2(false);
        newLineWidget.changeFocus(true);
//        newLineWidget.setFocused2(true);
        newLineWidget.setCursorPositionZero();

        resetLineWidgets();
    }

    public void joinToPriorLine(int index) {
        if (lineWidgets.size() <= 1 || index - 1 < 0) return;

        TextFieldWidget currentLineWidget = lineWidgets.get(index);
        TextFieldWidget priorLineWidget = lineWidgets.get(index - 1);

        String ps = currentLineWidget.getText();
        currentLineWidget.changeFocus(false);
//        currentLineWidget.setFocused2(false);
        lineWidgets.remove(index);

        String prevPriorLineText = priorLineWidget.getText();
        priorLineWidget.setText(prevPriorLineText + ps);
        priorLineWidget.changeFocus(true);
//        priorLineWidget.setFocused2(true);
        priorLineWidget.setCursorPosition(prevPriorLineText.length());

        resetLineWidgets();
    }

    public void joinToNextLine(int index) {
        if (lineWidgets.size() <= 1 || index + 1 >= lineWidgets.size()) return;

        TextFieldWidget currentLineWidget = lineWidgets.get(index);
        TextFieldWidget nextLineWidget = lineWidgets.get(index + 1);

        String ps = currentLineWidget.getText();
        currentLineWidget.changeFocus(false);
//        currentLineWidget.setFocused2(false);
        lineWidgets.remove(index);

        String prevNextLineText = nextLineWidget.getText();
        nextLineWidget.setText(ps + prevNextLineText);
        nextLineWidget.changeFocus(true);
//        priorLineWidget.setFocused2(true);
        nextLineWidget.setCursorPosition(ps.length());

        resetLineWidgets();
    }

    private TextFieldWidget createNewLine(int index, String initialText) {
        TextFieldWidget lw = new TextFieldWidget(
                this.font, this.x, this.y + index * this.lineHeight, this.width, this.lineHeight,
                new StringTextComponent("Line_" + index)
        );
        lw.setText(initialText);
        return lw;
    }

    public boolean isInRange(double x, double y) {
        return x >= this.x && y >= this.y && x < this.x + this.width && y < this.y + this.height;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (!isInRange(mouseX, mouseY)) return false;

        for (int i = 0; i < lineWidgets.size(); i++) {
            lineWidgets.get(i).mouseClicked(mouseX, mouseY, button);
            if (lineWidgets.get(i).isFocused()) {
                focusLineIndex = i;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (!isInRange(mouseX, mouseY)) return false;

        lineWidgets.forEach(lw -> lw.mouseReleased(mouseX, mouseY, button));
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        boolean result = false;
        System.out.println("keyCode = " + keyCode);

        int i = 0;
        for (TextFieldWidget lw : lineWidgets) {
            if (lw.isFocused()) {
                switch (keyCode) {
                    // Enter
                    case 257: breakLine(i); result = true; break;
                    // Backspace
                    case 259: {
                        if (lw.getCursorPosition() == 0) {
                            joinToPriorLine(i);
                            result = true;
                        }
                    } break;
                    // Delete
                    case 261: {
                        if (lw.getCursorPosition() >= lw.getText().length()) {
                            joinToNextLine(i);
                            result = true;
                        }
                    } break;
                    // Arrow up
                    case 265: focusLine(i - 1, i); result = true; break;
                    // Arrow down
                    case 264: focusLine(i + 1, i); result = true; break;
                    // Arrow left
                    case 263: {
                        if (lw.getCursorPosition() == 0) {
                            focusLine(i - 1, i, -1);
                            result = true;
                        }
                    } break;
                    // Arrow right
                    case 262: {
                        if (lw.getCursorPosition() >= lw.getText().length()) {
                            focusLine(i + 1, i, 0);
                            result = true;
                        }
                    } break;
                }
                if (!result) {
                    result |= lw.keyPressed(keyCode, scanCode, modifiers);
                }
            }
            if (result) break;
            i++;
        }
        return result;
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        boolean result = false;
        for (TextFieldWidget lw : lineWidgets) {
            result |= lw.keyReleased(keyCode, scanCode, modifiers);
        }
        return result;
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        boolean result = false;
        for (TextFieldWidget lw : lineWidgets) {
            result |= lw.charTyped(codePoint, modifiers);
        }
        return result;
    }
}

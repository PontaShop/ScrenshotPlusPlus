package xyz.pontashop.screnshotplusplus;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;

import java.io.IOException;

import org.lwjgl.input.Mouse;

public class CropGui extends GuiScreen {
    private int startX;
    private int startY;
    private int endX;
    private int endY;
    private boolean isSelecting;

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        if (mouseButton == 0) {
            startX = mouseX;
            startY = mouseY;
            isSelecting = true;
        }
    }

    @Override
    public void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);

        if (isSelecting) {
            endX = mouseX;
            endY = mouseY;
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        Minecraft.getMinecraft().thePlayer.closeScreen();
        isSelecting = false;

        ScaledResolution resolution = new ScaledResolution(Minecraft.getMinecraft());
        int x = Math.min(startX, endX);
        int y = Math.min(startY, endY);
        int width = Math.abs(endX - startX);
        int height = Math.abs(endY - startY);
        TakeScreenShot.takeScreenshot(startX * resolution.getScaleFactor(), startY * resolution.getScaleFactor(), width * resolution.getScaleFactor(), height * resolution.getScaleFactor());

        startX = 0;
        startY = 0;
        endX = 0;
        endY = 0;

        this.drawScreen(mouseX, mouseY, 0);
    }
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        FontRenderer fr = Minecraft.getMinecraft().fontRendererObj;

        if (isSelecting) {

            int x = Math.min(startX, endX);
            int y = Math.min(startY, endY);
            int width = Math.abs(endX - startX);
            int height = Math.abs(endY - startY);
            int color = 0x80FFFFFF;
            drawRect(x - 1, y - 1, x + width, y - 2, color);

            drawRect(x, y - 1, x - 1, y + height, color);

            drawRect(x + width, y - 2, x + width + 1, y + height, color);

            drawRect(x - 1, y + height, x + width + 1, y + height + 1, color);

        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
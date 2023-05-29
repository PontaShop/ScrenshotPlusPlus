package xyz.pontashop.screnshotplusplus;

import java.nio.IntBuffer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.shader.Framebuffer;
import net.minecraftforge.fml.client.FMLClientHandler;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

public class TakeScreenShot {
    private static IntBuffer pixelBuffer;
    private static int[] pixelValues;

    public static void takeScreenshot(int x, int y, int w, int h) {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {

            e.printStackTrace();
        }

        Minecraft mc = FMLClientHandler.instance().getClient();
        Framebuffer frameBuffer = mc.getFramebuffer();
        int screenshotWidth = mc.displayWidth;
        int screenshotHeight = mc.displayHeight;
        if (OpenGlHelper.isFramebufferEnabled()) {
            screenshotWidth = frameBuffer.framebufferTextureWidth;
            screenshotHeight = frameBuffer.framebufferTextureHeight;
        }
        int targetCapacity = screenshotWidth * screenshotHeight;
        if (pixelBuffer == null || pixelBuffer.capacity() < targetCapacity) {
            pixelBuffer = BufferUtils.createIntBuffer(targetCapacity);
            pixelValues = new int[targetCapacity];
        }
        GL11.glPixelStorei(3333, 1);
        GL11.glPixelStorei(3317, 1);
        pixelBuffer.clear();
        if (OpenGlHelper.isFramebufferEnabled()) {
            GlStateManager.bindTexture((int) frameBuffer.framebufferTexture);
            GL11.glGetTexImage(3553, 0, 32993, 33639, pixelBuffer);
        } else {
            GL11.glReadPixels(0, 0, screenshotWidth, screenshotHeight, 32993, 33639, pixelBuffer);
        }
        pixelBuffer.get(pixelValues);
        TextureUtil.processPixelValues((int[]) pixelValues, (int) screenshotWidth, (int) screenshotHeight);
        int[] pixelCopy = new int[pixelValues.length];
        System.arraycopy(pixelValues, 0, pixelCopy, 0, pixelValues.length);
        ScreenshotSaver.saveScreenshotAsync(screenshotWidth, screenshotHeight, pixelCopy, frameBuffer, x, y, w, h);
    }
}
package xyz.pontashop.screnshotplusplus;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.imageio.ImageIO;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

public class ScreenshotSaver
implements Runnable {
    private int width;
    private int height;
    private String captureTime;
    private int[] pixels;
    private Framebuffer frameBuffer;
    private int x, y, w, h;
    public static BufferedImage image = null;

    public static void saveScreenshotAsync(int width, int height, int[] pixels, Framebuffer frameBuffer, int x, int y, int w, int h) {
        ScreenshotSaver saver = new ScreenshotSaver();
        saver.width = width;
        saver.height = height;
        saver.captureTime = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss").format(new Date());
        saver.pixels = pixels;
        saver.frameBuffer = frameBuffer;
        saver.x = x;
        saver.y = y;
        saver.w = w;
        saver.h = h;
        new Thread(saver).start();
    }

    public void run() {
        if (OpenGlHelper.isFramebufferEnabled()) {
            int diff;
            image = new BufferedImage(this.frameBuffer.framebufferWidth, this.frameBuffer.framebufferHeight, BufferedImage.TYPE_INT_RGB);
            for (int i = diff = this.frameBuffer.framebufferTextureHeight - this.frameBuffer.framebufferHeight; i < this.frameBuffer.framebufferTextureHeight; ++i) {
                for (int j = 0; j < this.frameBuffer.framebufferWidth; ++j) {
                    int pixel = this.pixels[i * this.frameBuffer.framebufferTextureWidth + j];
                    image.setRGB(j, i - diff, pixel);
                }
            }
        } else {
            image = new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_RGB);
            image.setRGB(0, 0, this.width, this.height, this.pixels, 0, this.width);
        }

        int cropX = x;
        int cropY = y;
        int cropWidth = w;
        int cropHeight = h;
        System.out.println(x + " " + y + " " + w + " " + h);
        if (cropX < 0 || cropY < 0 || cropX + cropWidth > image.getWidth() || cropY + cropHeight > image.getHeight()) {

            System.out.println("Error: Crop region exceeds image boundaries.");
            return;
        }

        image = image.getSubimage(cropX, cropY, cropWidth, cropHeight);

        File ssDir = new File("screenshots");
        File ssFile = new File("screenshots", this.captureTime + ".png");
        int iterator = 0;
        while (ssFile.exists()) {
            ssFile = new File("screenshots", this.captureTime + "_" + ++iterator + ".png");
        }
        String name = this.captureTime + ".png";
        if (iterator > 0)
            name = this.captureTime + "_" + iterator + ".png";
        try {
            ssDir.mkdirs();
            ImageIO.write(image, "png", ssFile);
            ChatComponentText comp1 = new ChatComponentText("Saved screenshot as " + name + " ");

            ChatComponentText comp2 = new ChatComponentText("[OPEN]");
            ChatStyle comp2Style = new ChatStyle().setColor(EnumChatFormatting.BLUE);
            comp2Style.setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, Minecraft.getMinecraft().mcDataDir.getCanonicalPath() + "\\screenshots\\" + name));
            comp2.setChatStyle(comp2Style);
            ChatComponentText comp3 = new ChatComponentText(" [COPY]");
            ChatStyle comp3Style = new ChatStyle().setColor(EnumChatFormatting.GOLD);
            comp3Style.setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/"));
            comp3.setChatStyle(comp3Style);

            ChatComponentText chat = new ChatComponentText("");
            chat.appendSibling(comp1);
            chat.appendSibling(comp2);
            chat.appendSibling(comp3);

            Minecraft.getMinecraft().thePlayer.addChatMessage(chat);
        } catch (IOException e) {
            e.printStackTrace();

        }
    }
}
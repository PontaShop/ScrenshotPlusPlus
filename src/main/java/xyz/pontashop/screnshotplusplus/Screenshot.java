package xyz.pontashop.screnshotplusplus;

import org.lwjgl.input.Keyboard;

import com.ibm.icu.impl.ICUService.Key;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

@Mod(modid = "Screnshot++", version = "1.0")

public class Screenshot {

    Minecraft mc = Minecraft.getMinecraft();
    public static KeyBinding takescreenshot;

    @EventHandler
    public void init(FMLInitializationEvent event) {

        KeyBinding screenshotKey = Minecraft.getMinecraft().gameSettings.keyBindScreenshot;
        screenshotKey.setKeyCode(Keyboard.KEY_NONE);
        takescreenshot = new KeyBinding("Take Screenshot", Keyboard.KEY_F2, "Screnshot++");
        ClientRegistry.registerKeyBinding(takescreenshot);
        MinecraftForge.EVENT_BUS.register(new ScreenshotSaver());
        ClientCommandHandler.instance.registerCommand(new CopyToClipboard());
        MinecraftForge.EVENT_BUS.register(this);
    }
    @SubscribeEvent
    public void onUpdate(TickEvent.ClientTickEvent e) {
        ScaledResolution resolution = new ScaledResolution(Minecraft.getMinecraft());

        if (takescreenshot.isPressed()) {

            if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
                Minecraft.getMinecraft().displayGuiScreen(new CropGui());
            } else
                TakeScreenShot.takeScreenshot(0, 0, resolution.getScaledWidth() * resolution.getScaleFactor(), resolution.getScaledHeight() * resolution.getScaleFactor());
        }

    }
}
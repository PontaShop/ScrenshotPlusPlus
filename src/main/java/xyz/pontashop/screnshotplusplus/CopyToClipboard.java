package xyz.pontashop.screnshotplusplus;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;

public class CopyToClipboard implements ICommand, ClipboardOwner {

    @Override
    public int compareTo(ICommand arg0) {
        return 0;
    }

    @Override
    public String getCommandName() {
        return "";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "yay bad code!";
    }

    @Override
    public List < String > getCommandAliases() {
        List < String > aliases = new ArrayList < String > ();
        aliases.add("");
        return aliases;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {

        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        BufferedImage imagee = ScreenshotSaver.image;

        Transferable transferableImage = new Transferable() {
            @Override
            public DataFlavor[] getTransferDataFlavors() {
                return new DataFlavor[] {
                    DataFlavor.imageFlavor
                };
            }

            @Override
            public boolean isDataFlavorSupported(DataFlavor flavor) {
                return flavor.equals(DataFlavor.imageFlavor);
            }

            @Override
            public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException {
                if (flavor.equals(DataFlavor.imageFlavor)) {
                    return imagee;
                } else {
                    throw new UnsupportedFlavorException(flavor);
                }
            }
        };

        clipboard.setContents(transferableImage, null);

        sender.addChatMessage(new ChatComponentText("Image copied to clipboard!"));
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    @Override
    public List < String > addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        return null;
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return false;
    }

    @Override
    public void lostOwnership(Clipboard clipboard, Transferable contents) {

    }

}
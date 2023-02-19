package me.trojan.module.actions.captcha;

import me.trojan.Cache;
import me.trojan.base.BaseScriptAction;
import me.trojan.helpers.FileUtils;
import me.trojan.helpers.ReflectionHelper;
import net.eq2online.macros.core.Macros;
import net.eq2online.macros.scripting.api.*;
import net.eq2online.util.Game;
import net.eq2online.util.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.MapItemRenderer;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemStack;
import net.minecraft.world.storage.MapData;
import org.apache.commons.lang3.StringUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GetMapData extends BaseScriptAction {
    private static final Pattern CAPTCHA_FILE_PATTERN = Pattern.compile("(\\d+)\\. Captcha Data from \\d+-\\d+-\\d+ \\d+-\\d+-\\d+\\.txt");
    private final Minecraft mc = Minecraft.getMinecraft();
    private String dataToLog;
    private String folderDirectory;
    private boolean captchaStored;

    public GetMapData() {
        super("getmapdata");
    }

    public IReturnValue execute(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {
        if(!isHeldItemMap()) {
            if(params.length > 0)
                provider.setVariable(macro, params[0], "No ItemMap found.");
            return new ReturnValue("No ItemMap found.");
        }

        /* Get data of the map and convert it to a string to be able to use it within a ReturnValue Object.*/
        int[] buffer = getDataOfHeldMapAsByteArray();
        String mapData = new String(convertPngToBase64(buffer));

        storeCaptchaInMapDirectory(mapData);
        if(params.length > 0)
         provider.setVariable(macro, params[0], mapData);
        return new ReturnValue(mapData);
    }

    private void storeCaptchaInMapDirectory(String mapData) {
        dataToLog = mapData;
        folderDirectory = Macros.getInstance().getMacrosDirectory().getAbsolutePath() + File.separator + "maps/";
        if(folderDirectory.endsWith("/") || folderDirectory.endsWith("\\"))
            folderDirectory = StringUtils.chop(folderDirectory);
        captchaStored = storeCaptchaDataInMapsFolder();
        if(!hasStoredSuccessfully())
            Game.addChatMessage(Util.convertAmpCodes("&3CaptchaLogger > &4Unable to store captcha data within the /maps-folder."));
    }

    public boolean hasStoredSuccessfully() {
        return captchaStored;
    }

    private int[] getDataOfHeldMapAsByteArray() {
        /* Objects of MapData and the MapItemRenderer
         These are required as parameters in order to invoke the method 'func_148248_b'. */
        MapItemRenderer renderer = mc.entityRenderer.getMapItemRenderer();

        /* Invoking the methods from their obfuscated fields, and storing the returned value in the int[]-array buffer. */
        int[] buffer = new int[0];
        try {
            if(Cache.func_148248_b == null)
                Cache.func_148248_b = ReflectionHelper.accessPrivateMethod(MapItemRenderer.class, "func_148248_b", MapData.class);
            Object instance = Cache.func_148248_b.invoke(renderer, getMapData());

            if(Cache.field_148241_e == null)
                Cache.field_148241_e = ReflectionHelper.accessPrivateField(instance.getClass(), "field_148241_e");
            buffer = (int[]) Cache.field_148241_e.get(instance);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchFieldException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return buffer;
    }

    /* Returns an Object of MapData holding important information about the map. */
    private MapData getMapData() {
        ItemStack heldItem = mc.player.getHeldItemMainhand();
        ItemMap map = (ItemMap) heldItem.getItem();
        return map.getMapData(heldItem, mc.world);
    }

    /* Returns true if the player is holding a Map in his main-hand.
     Otherwise, returns false. */
    private boolean isHeldItemMap() {
        ItemStack heldItem = mc.player.getHeldItemMainhand();
        String itemID = Game.getItemName(heldItem.getItem());
        return itemID.equals("filled_map");
    }

    private byte[] convertPngToBase64(int[] buffer) {
        /* Check if the passed byte array has content, if not, return an empty byte array. */
        if(buffer.length == 0)
            return new byte[0];

        /*
         Using BufferedImage and ImageIO to create an image from the bytes the passed byte array contains. width*height = 128x128 (default minecraft ItemMap size)
         After creating the image and writing the data to the ByteArrayOutputStream, converting the ByteArrayOutputStream into a byte array.
         Then, this byte array is used as parameter in the encode method of the Base64-class to create a proper Bae64-encoded byte array.
         This data can then be used to convert it back to an image, and can be sent to certain endpoints, such as recaptcha solving APIs or similar services (deep learning self-created method or whatever).
        */
        BufferedImage image = new BufferedImage(128, 128, 1);
        image.getRaster().setDataElements(0, 0, 128, 128, buffer);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "png", outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Base64.getEncoder().encode(outputStream.toByteArray());
    }

    /* Returns true if the CAPTCHA Data has been stored successfully in the /maps-folder. Otherwise returns false. */
    private boolean storeCaptchaDataInMapsFolder() {
        File directory = FileUtils.createDirectory(folderDirectory);
        File textFileInMapsDirectory = FileUtils.createFile(folderDirectory + File.separator, getNewFileName(directory));
        return FileUtils.writeDataToFile(textFileInMapsDirectory, dataToLog);
    }

    /* Returns the file name which will be applied to this newly created file. The file name always has the following format:
     <int captchaFileCounter>. Captcha Data from <String timestamp>.txt */
    private String getNewFileName(File directory) {
        return (getFileCount(directory) + 1) + ". Captcha Data from " + FileUtils.getTimestamp() + ".txt";
    }

    /* Returns the highest number in front of a file currently existing within the directory. */
    private int getFileCount(File directory) {
        int highestFileNumber = 0;
        File[] filesInDirectory = directory.listFiles();
        if(filesInDirectory == null)
            return 0;

        for(File file : filesInDirectory) {
            String fileName = file.getName();
            Matcher matcher = CAPTCHA_FILE_PATTERN.matcher(fileName);
            if (matcher.matches()) {
                int currentFileNumber = Integer.parseInt(matcher.group(1));
                highestFileNumber = Math.max(currentFileNumber, highestFileNumber);
            }
        }
        return highestFileNumber;
    }
}


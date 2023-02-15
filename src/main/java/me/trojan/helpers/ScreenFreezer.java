package me.trojan.helpers;

import com.mumfrey.liteloader.GameLoopListener;
import com.mumfrey.liteloader.client.LiteLoaderEventBrokerClient;
import net.minecraft.client.Minecraft;

import java.io.File;

public class ScreenFreezer implements GameLoopListener {
    private final Minecraft mc = Minecraft.getMinecraft();
    private boolean isDisabled = false;
    public static final ScreenFreezer INSTANCE = new ScreenFreezer();

    /*
     In the constructor, the ScreenFreezer is added to the LoopListener's
     as this class implements the GameLoopListener interface and has to be registered in order to listen to events.
    */
    private ScreenFreezer() {
        LiteLoaderEventBrokerClient.getInstance().addLoopListener(this);
    }

    /* Called every frame, before the world is ticked. (Documentation from the implemented GameLoopListener interface) */
    @Override
    public void onRunGameLoop(Minecraft mc) {
        if(isDisabled)
            LiteLoaderEventBrokerClient.getInstance().onTick(true, 0.0F);
    }


    /**
     * Sets the value of isDisabled to false, which stops the constant execution of
     * LiteLoaderEventBrokerClient#getInstance()#onTick() within the onRunGameLoop method.
     * Additionally, sets skipRenderWorld to false, as the state of this boolean is responsible for the stopping of rendering.
     */
    public void enableRender() {
        isDisabled = false;
        mc.skipRenderWorld = false;
    }

    /**
     * Sets the value of isDisabled to true, which starts the constant execution of
     * LiteLoaderEventBrokerClient#getInstance()#onTick() within the onRunGameLoop method.
     * Additionally, sets skipRenderWorld to true, as the state of this boolean is responsible for the stopping of rendering.
     */
    public void disableRender() {
        isDisabled = true;
        mc.skipRenderWorld = true;
    }

    public static ScreenFreezer getInstance() {
        return INSTANCE;
    }

    /*
     Force implementation of these methods (String getVersion, void init, void upgradeSettings, String getName)
     due to the implementation of the Interface GameLoopListener, don't do anything with these, they are not needed for this use-case.
    */
    @Override
    public String getVersion() {
        return null;
    }

    @Override
    public void init(File configPath) {

    }

    @Override
    public void upgradeSettings(String version, File configPath, File oldConfigPath) {
    }

    @Override
    public String getName() {
        return null;
    }
}

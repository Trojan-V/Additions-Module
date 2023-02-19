package me.trojan.module.variables;

import com.mumfrey.liteloader.ChatListener;
import me.trojan.base.BaseVariableProvider;
import me.trojan.helpers.MacroModRegistryHelper;
import net.minecraft.util.text.ITextComponent;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MaddoxCode extends BaseVariableProvider implements ChatListener {
    private static final Pattern MADDOX_PATTERN = Pattern.compile("\"action\":\"run_command\",\"value\":\"(/cb [a-z0-9-]+)");
    private String maddoxCode = "No Maddox Code found.";

    @Override
    public void onChat(ITextComponent chat, String message) {
        if(!message.contains("[OPEN MENU]"))
            return;
        String json = ITextComponent.Serializer.componentToJson(chat);
        Matcher matcher = MADDOX_PATTERN.matcher(json);
        if (matcher.find())
            maddoxCode = matcher.group(1);
    }

    @Override
    public void updateVariables(boolean clock) {
        if(!clock)
            return;
        storeVariable("MADDOXCODE", maddoxCode);
    }

    @Override
    public void onInit() {
        try {
            MacroModRegistryHelper.registerChatListener(this);
            super.onInit();
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getVersion() {
        return null;
    }

    @Override
    public void init(File configPath) {}

    @Override
    public void upgradeSettings(String version, File configPath, File oldConfigPath) {}

    @Override
    public String getName() {
        return null;
    }
}

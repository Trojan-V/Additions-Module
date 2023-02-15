package me.trojan.module.actions.title;

import me.trojan.Cache;
import me.trojan.base.BaseScriptAction;
import net.eq2online.macros.scripting.api.*;
import net.minecraft.client.gui.GuiIngame;

import java.lang.reflect.Field;

public class GetTitle extends BaseScriptAction {
    public GetTitle() {
        super("getTitle");
    }

    @Override
    public IReturnValue execute(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {
        if(Cache.title == null)
            getAndCacheTitleField();
        try {
            Object titleObject = Cache.title.get(mc.ingameGUI);
            return new ReturnValue((String) titleObject);
        } catch (IllegalAccessException ignored) {}
        return new ReturnValue("");
    }

    private void getAndCacheTitleField() {
        try {
            Field displayedTitle = GuiIngame.class.getDeclaredField("field_175201_x");
            displayedTitle.setAccessible(true);
            Cache.title = displayedTitle;
        } catch (NoSuchFieldException ignored) {}
    }
}

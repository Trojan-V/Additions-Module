package me.trojan.module.actions.title;

import me.trojan.Cache;
import me.trojan.base.BaseScriptAction;
import net.eq2online.macros.scripting.api.*;
import net.minecraft.client.gui.GuiIngame;

import java.lang.reflect.Field;

public class GetSubTitle extends BaseScriptAction {
    public GetSubTitle() {
        super("getSubTitle");
    }

    @Override
    public IReturnValue execute(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {
        if(Cache.subTitle == null)
            getAndCacheSubTitleField();
        try {
            Object subTitleObject = Cache.subTitle.get(mc.ingameGUI);
            return new ReturnValue((String) subTitleObject);
        } catch (IllegalAccessException ignored) {}
        return new ReturnValue("");
    }

    private void getAndCacheSubTitleField() {
        try {
            Field displayedSubTitle = GuiIngame.class.getDeclaredField("field_175200_y");
            displayedSubTitle.setAccessible(true);
            Cache.subTitle = displayedSubTitle;
        } catch (NoSuchFieldException ignored) {}
    }
}

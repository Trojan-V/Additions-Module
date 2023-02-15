package me.trojan.module.actions.rendering.renderzero;

import me.trojan.base.BaseScriptAction;
import net.eq2online.macros.scripting.api.*;
import net.eq2online.macros.scripting.parser.ScriptCore;

public class RenderZero extends BaseScriptAction {
    private static final int DEFAULT_CHUNK_VALUE = 8;
    private boolean isRenderDistanceZero;

    public RenderZero() {
        super("renderzero");
    }

    @Override
    public IReturnValue execute(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {
        int renderDistanceChunks = -1;
        if (params.length > 0)
            renderDistanceChunks = ScriptCore.tryParseInt(provider.expand(macro, params[0], false), -1);

        toggleRenderDistanceChunks(renderDistanceChunks);
        return new ReturnValue("");
    }

    /* Parameter: Amount of chunks the Render Distance should be set to
     If no parameter is provided, this value will be defaulted to @Reference(DEFAULT_CHUNK_VALUE) */
    private void toggleRenderDistanceChunks(int chunks) {
        if(isRenderDistanceZero)
            setChunks(chunks <= 0 ? DEFAULT_CHUNK_VALUE : chunks);
        else
            setChunks(0);

        updateIsRenderDistanceZeroBoolean();
    }

    private void updateIsRenderDistanceZeroBoolean() {
        isRenderDistanceZero = mc.gameSettings.renderDistanceChunks == 0;
    }

    private void setChunks(int chunks) {
        mc.gameSettings.renderDistanceChunks = chunks;
    }
}

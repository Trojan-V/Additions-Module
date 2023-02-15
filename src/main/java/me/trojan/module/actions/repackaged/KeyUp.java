package me.trojan.module.actions.repackaged;

import me.trojan.base.BaseScriptAction;
import net.eq2online.macros.scripting.api.IMacro;
import net.eq2online.macros.scripting.api.IMacroAction;
import net.eq2online.macros.scripting.api.IReturnValue;
import net.eq2online.macros.scripting.api.IScriptActionProvider;
import net.eq2online.macros.scripting.parser.ScriptCore;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;

public class KeyUp extends BaseScriptAction {
    protected boolean keyState;

    public KeyUp() {
        super("keyup");
        keyState = false;
    }

    protected KeyUp(String actionName) {
        super(actionName);
    }

    public boolean isThreadSafe() {
        return false;
    }

    @Override
    public IReturnValue execute(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {
        if(params.length == 0)
            return null;

        String parameter = provider.expand(macro, params[0], false).toLowerCase();
        GameSettings gameSettings = mc.gameSettings;
        switch(parameter) {
            case "forward":
                KeyBinding.setKeyBindState(gameSettings.keyBindForward.getKeyCode(), keyState);
                return null;
            case "back":
                KeyBinding.setKeyBindState(gameSettings.keyBindBack.getKeyCode(), keyState);
                return null;
            case "left":
                KeyBinding.setKeyBindState(gameSettings.keyBindLeft.getKeyCode(), keyState);
                return null;
            case "right":
                KeyBinding.setKeyBindState(gameSettings.keyBindRight.getKeyCode(), keyState);
                return null;
            case "jump":
                KeyBinding.setKeyBindState(gameSettings.keyBindJump.getKeyCode(), keyState);
                return null;
            case "sneak":
                KeyBinding.setKeyBindState(gameSettings.keyBindSneak.getKeyCode(), keyState);
                return null;
            case "playerlist":
                KeyBinding.setKeyBindState(gameSettings.keyBindPlayerList.getKeyCode(), keyState);
                return null;
            case "sprint":
                KeyBinding.setKeyBindState(gameSettings.keyBindSprint.getKeyCode(), keyState);
                return null;
            case "attack":
                KeyBinding.setKeyBindState(gameSettings.keyBindAttack.getKeyCode(), keyState);
                return null;
        }

        int keyCode = ScriptCore.tryParseInt(parameter, 0);
        if (keyCode > 0 && keyCode < 255 && keyCode != gameSettings.keyBindAttack.getKeyCode() && keyCode != gameSettings.keyBindUseItem.getKeyCode())
            KeyBinding.setKeyBindState(keyCode, keyState);
        return null;
    }
}

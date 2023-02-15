package me.trojan.module.actions.repackaged;

import me.trojan.base.BaseScriptAction;
import net.eq2online.macros.core.mixin.IKeyBinding;
import net.eq2online.macros.scripting.api.IMacro;
import net.eq2online.macros.scripting.api.IMacroAction;
import net.eq2online.macros.scripting.api.IReturnValue;
import net.eq2online.macros.scripting.api.IScriptActionProvider;
import net.eq2online.util.Game;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;

import java.util.ArrayList;
import java.util.List;

public class Key extends BaseScriptAction {
    private final List<KeyBinding> pressedKeys = new ArrayList<>();
    public Key() {
        super("key");
    }

    public boolean isThreadSafe() {
        return false;
    }

    @Override
    public IReturnValue execute(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {
        if(params.length == 0)
            return null;

        int keyCode = 0;
        GameSettings gameSettings = mc.gameSettings;
        String parameter = provider.expand(macro, params[0], false).toLowerCase();
        switch(parameter) {
            case "inventory":
                keyCode = gameSettings.keyBindInventory.getKeyCode();
                break;
            case "drop":
                keyCode = gameSettings.keyBindDrop.getKeyCode();
                break;
            case "chat":
                keyCode = gameSettings.keyBindChat.getKeyCode();
                break;
            case "attack":
                keyCode = gameSettings.keyBindAttack.getKeyCode();
                break;
            case "use":
                keyCode = gameSettings.keyBindUseItem.getKeyCode();
                break;
            case "pick":
                keyCode = gameSettings.keyBindPickBlock.getKeyCode();
                break;
            case "screenshot":
                keyCode = gameSettings.keyBindScreenshot.getKeyCode();
                break;
            case "smoothcamera":
                keyCode = gameSettings.keyBindSmoothCamera.getKeyCode();
                break;
            case "swaphands":
                keyCode = gameSettings.keyBindSwapHands.getKeyCode();
                break;
        }
        if(keyCode == 0)
            return null;

        KeyBinding keyBinding = Game.getKeybinding(keyCode);
        if(keyBinding == null)
            return null;

        if (((IKeyBinding) keyBinding).getPresses() < 1) {
            KeyBinding.onTick(keyCode);
        }

        if (!keyBinding.isKeyDown()) {
            KeyBinding.setKeyBindState(keyBinding.getKeyCode(), true);
            pressedKeys.add(keyBinding);
        }

        return null;
    }

    public int onTick(IScriptActionProvider provider) {
        int tickedActionCount;
        for(tickedActionCount = 0; pressedKeys.size() > 0; tickedActionCount++) {
            KeyBinding keyBinding = pressedKeys.remove(0);
            KeyBinding.setKeyBindState(keyBinding.getKeyCode(), false);
        }

        return tickedActionCount;
    }
}

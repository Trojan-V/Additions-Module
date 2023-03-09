package me.trojan.module.actions;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import me.trojan.base.BaseScriptAction;
import net.eq2online.macros.scripting.api.IMacro;
import net.eq2online.macros.scripting.api.IMacroAction;
import net.eq2online.macros.scripting.api.IReturnValue;
import net.eq2online.macros.scripting.api.IScriptActionProvider;
import net.eq2online.util.Game;

public class Test extends BaseScriptAction {
    public Test() {
        super("test");
    }

    @Override
    public IReturnValue execute(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {
        Game.addChatMessage("Invoking test...");
        Gson gson = new Gson();
        JsonObject jsonObject = new JsonObject();
        Game.addChatMessage("Invoked test");
        return null;
    }
}

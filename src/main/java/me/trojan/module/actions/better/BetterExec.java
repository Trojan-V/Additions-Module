package me.trojan.module.actions.better;

import me.trojan.base.BaseScriptAction;
import me.trojan.engine.EditedMacroExecVariableProvider;
import net.eq2online.macros.core.MacroTemplate;
import net.eq2online.macros.core.MacroTriggerType;
import net.eq2online.macros.core.Macros;
import net.eq2online.macros.scripting.api.*;
import net.eq2online.macros.scripting.parser.ScriptContext;
import net.eq2online.util.Game;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Pattern;

public class BetterExec extends BaseScriptAction {
    private static final Pattern PATTERN_FILE_NAME_OR_DIRECTORY = Pattern.compile("^(?:[/\\w-. ]+)?[\\w-. ]+\\.txt$");

    public BetterExec() {
        super("exec");
    }

    @Override
    public boolean isThreadSafe() {
        return false;
    }

    @Override
    public IReturnValue execute(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {
        if (params.length == 0)
            return new ReturnValue(false);

        String macroFileName = provider.expand(macro, params[0], false);
        if(!PATTERN_FILE_NAME_OR_DIRECTORY.matcher(macroFileName).matches())
            return new ReturnValue(false);

        String macroTaskName = params.length > 1 ? provider.expand(macro, params[1], false) : null;
        IVariableProvider execParameters = params.length > 2 ?
                new EditedMacroExecVariableProvider(params, 2, provider, macro) : null;

        if (instance.getState() != null) {
            IMacroTemplate macroTemplate = instance.getState();
            provider.getMacroEngine().playMacro(macroTemplate, false, ScriptContext.MAIN, execParameters);
        } else {
            File fileToExecute = provider.getMacroEngine().getFile(macroFileName);
            if(!fileToExecute.isFile())
                return new ReturnValue(false);

            IMacroTemplate macroTemplate = createFloatingTemplate(macroFileName, macroTaskName);
            if(macroTemplate == null)
                return new ReturnValue(false);
            instance.setState(macroTemplate);
            Macros.getInstance().playMacro(macroTemplate, false, ScriptContext.MAIN, execParameters);
        }
        return new ReturnValue(true);
    }

    private IMacroTemplate createFloatingTemplate(String fileName, String taskName) {
        int key = getNextFreeIndex(taskName);
        if (key >= 10000)
            return null;

        File macroFilePath = new File(Macros.getInstance().getMacrosDirectory(), fileName);
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(macroFilePath))) {
            String currentLine;
            while ((currentLine = br.readLine()) != null) {
                if(currentLine.equals("") || currentLine.startsWith("$${") || currentLine.startsWith("}$$"))
                    continue;

                sb.append(currentLine);
                if(!currentLine.endsWith(";"))
                    sb.append("; ");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String macroReadFromFile = sb.toString();
        MacroTemplate template = new MacroTemplate(macros, mc, key);
        template.setKeyDownMacro("$${" + macroReadFromFile + "}$$");
        Macros.getInstance().setMacroTemplate("", key, template);
        return template;
    }

    private int getNextFreeIndex(String macroName) {
        return MacroTriggerType.NONE.getNextFreeIndex(macros, macroName);
    }
}

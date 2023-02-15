package me.trojan.engine;

import net.eq2online.macros.scripting.api.IMacro;
import net.eq2online.macros.scripting.api.IParameterProvider;
import net.eq2online.macros.scripting.api.IScriptActionProvider;
import net.eq2online.macros.scripting.variable.VariableCache;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class EditedMacroExecVariableProvider extends VariableCache implements IParameterProvider {
    private final List<Object> parameters;

    public EditedMacroExecVariableProvider(String[] parameters, int ignore, IScriptActionProvider provider, IMacro macro) {
        this.parameters = new ArrayList<>(parameters.length - ignore);

        for(int paramIndex = ignore; paramIndex < parameters.length; paramIndex++) {
            String parameter = provider.expand(macro, parameters[paramIndex], false);
            if (parameter.matches("^-?[\\d.]+$")) {
                try {
                    Integer paramValue = Integer.parseInt(parameter);
                    this.parameters.add(paramValue);
                } catch (NumberFormatException e) {
                    this.parameters.add(parameter);
                    //LiteLoaderLogger.warning("Couldn't parse parameter as integer" + e.getMessage() + " (Additions: EditedMacroExecVariableProvider:26)");
                }

            } else if ("true".equalsIgnoreCase(parameter)) {
                this.parameters.add(Boolean.TRUE);
            } else if ("false".equalsIgnoreCase(parameter)) {
                this.parameters.add(Boolean.FALSE);
            } else {
                this.parameters.add(parameter);
            }
        }

        storeParametersAsVariables();
    }

    private void storeParametersAsVariables() {
        int variableIndex = 1;

        for (Object parameter : parameters) {
            if (parameter instanceof Integer) {
                storeVariable(String.format("#var%d", variableIndex++), (Integer) parameter);
            } else if (parameter instanceof Boolean) {
                storeVariable(String.format("var%d", variableIndex++), (Boolean) parameter);
            } else {
                storeVariable(String.format("&var%d", variableIndex++), parameter.toString());
            }
        }
    }

    public Object getVariable(String variableName) {
        return this.getCachedValue(variableName);
    }

    public String provideParameters(String macro) {
        if (this.parameters != null) {
            int variableIndex = 1;

            Object parameter;
            for(Iterator<Object> var3 = parameters.iterator(); var3.hasNext(); macro = macro.replaceAll("\\x24\\x24\\[" + variableIndex++ + "]", parameter.toString())) {
                parameter = var3.next();
            }
        }

        return macro.replaceAll("\\x24\\x24\\[[0-9]+]", "");
    }
}

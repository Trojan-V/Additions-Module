package me.trojan.engine;

import net.eq2online.macros.core.Macros;
import net.eq2online.macros.scripting.Variable;
import net.eq2online.macros.scripting.api.IVariableProviderShared;
import net.eq2online.macros.scripting.variable.ArrayStorage;
import net.eq2online.macros.scripting.variable.VariableProviderArray;
import net.eq2online.xml.IArrayStorageBundle;
import net.eq2online.xml.PropertiesXMLUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Properties;
import java.util.Set;

/* The default VariableProviderShared is being removed within the ScriptActionInternalModuleInitialization class
 which can be found in the me.trojan.internal package. */
public class EditedVariableProviderShared extends VariableProviderArray implements IVariableProviderShared {
    private final Properties sharedVariables = new Properties();
    private final File propertiesFile;

    /* The boolean dirty is being set to true if a new variable has been added to the Properties object
     * and has not been stored within the propertiesFile (.globalvars.xml) yet.
     * Once the saveToGlobalVarsXml()-method has been called, this boolean is being set to false again. */
    private boolean dirty = false;

    private int unSavedTicks = 100;

    public EditedVariableProviderShared(Macros macros) {
        propertiesFile = macros.getFile(".globalvars.xml");
        load();
    }

    protected void markDirty() {
        dirty = true;
    }

    /* Loads the global (shared) variables from the '.globalvars.xml' file
     * This method is called only once, when the constructor of the EditedVariableProviderShared is called. */
    protected void load() {
        sharedVariables.clear();
        if (propertiesFile != null && propertiesFile.exists()) {
            try {
                BufferedInputStream inputStream = new BufferedInputStream(Files.newInputStream(propertiesFile.toPath()));
                IArrayStorageBundle arrayStorage = ArrayStorage.getStorageBundle(flagStore, counterStore, stringStore);
                dirty |= PropertiesXMLUtils.load(sharedVariables, arrayStorage, inputStream);
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        saveToGlobalVarsXml();
    }

    /* Writes all global (shared) variables to the '.globalvars.xml' file */
    protected void saveToGlobalVarsXml() {
        if (propertiesFile == null)
            return;
        dirty = false;

        try {
            BufferedOutputStream outputStream = new BufferedOutputStream(Files.newOutputStream(propertiesFile.toPath()));
            /* flagStore contains all booleans, counterStore all integers, stringStore all strings. */
            IArrayStorageBundle arrayStorage = ArrayStorage.getStorageBundle(flagStore, counterStore, stringStore);
            PropertiesXMLUtils.save(sharedVariables, arrayStorage, outputStream,
                    "Shared variables store for mod_Macros", String.valueOf(StandardCharsets.UTF_8));
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateVariables(boolean clock) {
        if(!clock)
            return;

        unSavedTicks--;
        if (dirty && unSavedTicks < 0) {
            saveToGlobalVarsXml();
            unSavedTicks = 100;
        }
    }

    /* Adds a new global (shared) variable to the sharedVariables Properties object. */
    public void setSharedVariable(String variableName, String variableValue) {
        sharedVariables.setProperty(variableName, variableValue);
        markDirty();
    }

    public String getSharedVariable(String variableName) {
        return sharedVariables.containsKey(variableName) ? sharedVariables.getProperty(variableName) : "";
    }

    // TODO: Find the solution why local booleans show "true"/"false" while global booleans show 0 or 1 in MKB

    /**
     * Old code which automatically converted string values which are integers to an integer,
     * which destroyed the availability of values like "00", as "00" would've been formatted automatically to 0.
     *
     * @Reference VariableProviderShared.java (line 93-113) to see the code used by default.
     */
    public Object getVariable(String variableName) {
        if (!variableName.startsWith("@") || !Variable.isValidVariableName(variableName))
            return null;

        /* Remove the '@' symbol from the variable by calling .substring on index 1 (index 0 will always be the @ for obvious reasons) */
        variableName = variableName.substring(1);
        if (!sharedVariables.containsKey(variableName))
            return super.getVariable(variableName);

        String propertyValue = sharedVariables.getProperty(variableName);

        /* Fix for preceding zeros getting removed, not entirely sure if this might break some functionality of MacroMod
         * in certain edge-cases. */
        if(propertyValue.matches("^-?0+\\d+$"))
            return propertyValue;

        if (propertyValue.matches("^-?\\d+$")) {
            try {
                return Integer.parseInt(propertyValue);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return propertyValue;
    }

    public Set<String> getVariables() {
        Set<String> globalVariables = super.getVariables();
        for (Object sharedVar : sharedVariables.keySet()) {
            globalVariables.add("@" + sharedVar.toString());
        }
        return globalVariables;
    }

    public int getSharedVariable(String variableName, int defaultValue) {
        try {
            return Integer.parseInt(getSharedVariable(variableName));
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return defaultValue;
        }
    }

    public void onInit() {}


    /* Counter should be named Integer for an easier understanding. It represents a MacroMod Integer. */
    public void setCounter(String counter, int value) {
        setSharedVariable("#" + counter, String.valueOf(value));
    }

    public void unsetCounter(String macroModInteger) {
        sharedVariables.remove("#" + macroModInteger.toLowerCase());
        markDirty();
    }

    public void unsetCounter(String macroModInteger, int offset) {
        markDirty();
        super.unsetCounter(macroModInteger, offset);
    }

    public void incrementCounter(String macroModInteger, int increment) {
        int currentValue = getCounter(macroModInteger);
        setSharedVariable("#" + macroModInteger, String.valueOf(currentValue + increment));
    }

    public void decrementCounter(String macroModInteger, int decrement) {
        incrementCounter(macroModInteger, decrement * -1);
    }

    public void incrementCounter(String macroModInteger, int offset, int increment) {
        markDirty();
        super.incrementCounter(macroModInteger, offset, increment);
    }

    public void decrementCounter(String macroModInteger, int offset, int increment) {
        markDirty();
        super.decrementCounter(macroModInteger, offset, increment);
    }

    public int getCounter(String counter) {
        return getSharedVariable("#" + counter, 0);
    }



    public String getString(String stringName) {
        return getSharedVariable("&" + stringName.toLowerCase());
    }

    public void setString(String stringName, String value) {
        setSharedVariable("&" + stringName.toLowerCase(), value);
    }

    public void unsetString(String stringName) {
        sharedVariables.remove("&" + stringName.toLowerCase());
        markDirty();
    }



    /* 'Flag' represents a MacroMod boolean value. */
    public boolean getFlag(String flag) {
        String flagValue = getSharedVariable(flag);
        return "1".equals(flagValue) || "true".equalsIgnoreCase(flagValue);
    }

    public void setFlag(String flag, boolean value) {
        setSharedVariable(flag, value ? "1" : "0");
    }

    public void setFlag(String flag) {
        setSharedVariable(flag, "1");
    }

    public void unsetFlag(String flag) {
        setSharedVariable(flag, "0");
    }

    public void setFlag(String flag, int offset, boolean value) {
        markDirty();
        super.setFlag(flag, offset, value);
    }

    public void setFlag(String flag, int offset) {
        markDirty();
        super.setFlag(flag, offset);
    }

    public void unsetFlag(String flag, int offset) {
        markDirty();
        super.unsetFlag(flag, offset);
    }

    public void setCounter(String counter, int offset, int value) {
        markDirty();
        super.setCounter(counter, offset, value);
    }

    public void setString(String stringName, int offset, String value) {
        markDirty();
        super.setString(stringName, offset, value);
    }

    public void unsetString(String stringName, int offset) {
        markDirty();
        super.unsetString(stringName, offset);
    }

    public boolean push(String arrayName, String value) {
        markDirty();
        return super.push(arrayName, value);
    }

    public String pop(String arrayName) {
        markDirty();
        return super.pop(arrayName);
    }

    public void delete(String arrayName, int offset) {
        markDirty();
        super.delete(arrayName, offset);
    }

    public void clear(String arrayName) {
        markDirty();
        super.clear(arrayName);
    }
}

package me.trojan.internal;

import me.trojan.helpers.IDirectoryStream;
import net.eq2online.console.Log;
import net.eq2online.macros.scripting.IErrorLogger;
import net.eq2online.macros.scripting.LoadedModuleInfo;
import net.eq2online.macros.scripting.api.*;
import net.eq2online.macros.scripting.parser.ScriptCore;
import net.minecraft.launchwrapper.LaunchClassLoader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Iterator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class CustomModuleLoader implements IDirectoryStream {
    private static final String MODULE_FILE_REGEX = "^module_.*\\.jar$";
    private final File modulesDir;

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public CustomModuleLoader(File macrosPath) {
        modulesDir = new File(macrosPath, "/modules");
        if(!modulesDir.exists())
            modulesDir.mkdirs();
    }

    /* We actually only want to load this module file and not any other modules within the directory, because all other modules are loaded by the default module loader.
     * That's why we use if */
    public void loadModules(IErrorLogger logger) {
        ClassLoader loader = ScriptCore.class.getClassLoader();
        LaunchClassLoader classLoader = (LaunchClassLoader) loader;

        if(!modulesDir.exists() || !modulesDir.isDirectory())
            return;

        try {
            Iterator<File> iteratorForModulesDirectory = getIteratorForDirectory();
            while (iteratorForModulesDirectory.hasNext()) {
                File moduleFile = iteratorForModulesDirectory.next();
                if(moduleFile.getName().startsWith("module_Additions-"))
                    loadModule(logger, classLoader, moduleFile);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void loadModule(IErrorLogger logger, LaunchClassLoader classLoader, File module) throws IOException {
        if (!module.isFile())
            return;

        classLoader.addURL(module.toURI().toURL());
        LoadedModuleInfo loadedModuleInfo = new LoadedModuleInfo(module);

        ZipInputStream zipInputStream = new ZipInputStream(Files.newInputStream(module.toPath()));
        do {
            ZipEntry zipEntry = zipInputStream.getNextEntry();
            if (zipEntry == null)
                break;

            String classFileName = new File(zipEntry.getName()).getName();
            if(zipEntry.isDirectory() || !classFileName.endsWith(".class") || classFileName.contains("$"))
                continue;

            String className = zipEntry.getName().split("\\.")[0].replace('/', '.');
            Log.info("className: " + className);

            boolean classWasLoaded = loadModuleClass(classLoader, loadedModuleInfo, className);
            if(!classWasLoaded && !classFileName.startsWith("ScriptedIterator")) {
                if(logger != null)
                    logger.logError("API: Error initialising " + module.getName());
                break;
            }
        } while(true);

        zipInputStream.close();
        loadedModuleInfo.printStatus();
    }


    /**
     * @param className The name of the class which should be loaded, delimited by dots
     *                  and starting with base package. (me.trojan.engine.EditedExpressionEvaluator)
     * @return true if the class has been loaded successfully (not dependent on the class actually being an action/variable/event/iterator
     *         for the module, just any class contained in this module will be loaded.
     *         If the class turns out to be an action/variable/event/iterator, it will be added to the loadedModuleInfo via certain checks.
     *         Here, those checks are if the classPackage contains either "actions"/"variables"/"events"/"iterators".
     */
    private boolean loadModuleClass(LaunchClassLoader classLoader, LoadedModuleInfo loadedModuleInfo, String className) {
        try {
            String classPackage = classLoader.findClass(className).getPackage().getName();

            if(classPackage.contains("actions")) {
                IScriptAction actionToAdd = addClassFromModule(classLoader, className, IScriptAction.class);
                IScriptAction actionReturnedAfterAddingIt = loadedModuleInfo.addAction(actionToAdd);
                return actionReturnedAfterAddingIt != null;
            }
            if(classPackage.contains("variables")) {
                IVariableProvider providerToAdd = addClassFromModule(classLoader, className, IVariableProvider.class);
                IVariableProvider providerReturnedAfterAddingIt = loadedModuleInfo.addProvider(providerToAdd);
                return providerReturnedAfterAddingIt != null;
            }
            if(classPackage.contains("iterators")) {
                IScriptedIterator iteratorToAdd = addClassFromModule(classLoader, className, IScriptedIterator.class);
                IScriptedIterator iteratorReturnedAfterAddingIt = loadedModuleInfo.addIterator(iteratorToAdd);
                return iteratorReturnedAfterAddingIt != null;
            }
            if(classPackage.contains("events")) {
                IMacroEventProvider eventToAdd = addClassFromModule(classLoader, className, IMacroEventProvider.class);
                IMacroEventProvider eventReturnedAfterAddingIt = loadedModuleInfo.addEventProvider(eventToAdd);
                return eventReturnedAfterAddingIt != null;
            }
        } catch (ClassNotFoundException e) {
            Log.info("ClassNotFoundException (CustomModuleLoader#loadModuleClass();) for class with className: " + className);
            e.printStackTrace();
        }
        return true;
    }

    /**
     * @param className The name of the class which is being loaded by the classLoader. classLoader#loadClass() takes the name as parameter.
     * @param moduleClassType Any class which extends IMacrosAPIModule. Can be IScriptAction, IVariableProvider, IScriptedIterator.
     * @return A new instance of the by @param{moduleClassType} provided class. Currently, this instance is just being returned to make a null check
     *         to see if the new instance was created successfully and therefore the action/var/event/iterator has been added successfully.
     */
    private <ModuleType extends IMacrosAPIModule> ModuleType addClassFromModule(ClassLoader classLoader, String className, Class<ModuleType> moduleClassType) {
        try {
            Class<?> moduleClass = classLoader.loadClass(className);
            if (!moduleClassType.isAssignableFrom(moduleClass))
                return null;

            @SuppressWarnings("unchecked")
            ModuleType moduleInstance = (ModuleType) moduleClass.newInstance();
            moduleInstance.onInit();
            return moduleInstance;
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
        }
        return null;
    }


    /* Methods from implemented interface to conveniently iterate over directories. */
    @Override
    public File getDirectory() {
        return this.modulesDir;
    }

    @Override
    public boolean filterFiles() {
        return true;
    }

    @Override
    public String filterFileNameRegex() {
        return MODULE_FILE_REGEX;
    }
}

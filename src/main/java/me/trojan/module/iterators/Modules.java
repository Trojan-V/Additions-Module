package me.trojan.module.iterators;

import me.trojan.base.BaseIterator;
import me.trojan.helpers.IDirectoryStream;
import net.eq2online.macros.core.Macros;
import net.eq2online.macros.scripting.api.IMacro;
import net.eq2online.macros.scripting.api.IScriptActionProvider;
import net.eq2online.macros.scripting.api.IScriptedIterator;
import net.eq2online.util.Game;

import java.io.File;
import java.util.Iterator;

public class Modules extends BaseIterator implements IDirectoryStream {
    public Modules() {
        super(null, null);
    }

    public Modules(IScriptActionProvider provider, IMacro macro) {
        super(provider, macro);

        try {
            Iterator<File> fileIterator = getIteratorForDirectory();
            while(fileIterator.hasNext()) {
                File nextFile = fileIterator.next();
                begin();
                add("FILENAME", nextFile.getName());
                add("FILEPATH", nextFile.getAbsolutePath());
                end();
            }
        } catch (Exception e) {
            Game.addChatMessage(e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public String getIteratorName() {
        return "modules";
    }

    @Override
    public Class<? extends IScriptedIterator> getIteratorClass() {
        return getClass();
    }

    @Override
    public File getDirectory() {
        return new File(Macros.getInstance().getMacrosDirectory(), "modules");
    }

    @Override
    public boolean filterFiles() {
        return true;
    }

    @Override
    public String filterFileNameRegex() {
        return "^module_.*";
    }
}

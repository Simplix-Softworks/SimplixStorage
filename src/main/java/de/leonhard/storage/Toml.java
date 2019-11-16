package de.leonhard.storage;

import de.leonhard.storage.internal.FileData;
import de.leonhard.storage.internal.FileType;
import de.leonhard.storage.internal.FlatFile;
import de.leonhard.storage.internal.settings.DataType;
import de.leonhard.storage.internal.settings.ReloadSettings;
import de.leonhard.storage.utils.FileUtils;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.io.File;
import java.io.IOException;

@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Toml extends FlatFile {

    public Toml(File file) {
        super(file, FileType.TOML);
        create();
        forceReload();
    }

    public Toml(String name, String path) {
        this(name, path, null, null);
    }

    public Toml(String name, String path, ReloadSettings reloadSettings, final DataType dataType) {
        super(name, path, FileType.TOML);
        create();
        if (reloadSettings != null) {
            this.reloadSettings = reloadSettings;
        }

        if (dataType != null)
            this.dataType = dataType;
        else
            this.dataType = DataType.INTELLIGENT;
        create();
        forceReload();
    }


    // ----------------------------------------------------------------------------------------------------
    // Abstract methods to implement
    // ----------------------------------------------------------------------------------------------------

    @Override
    protected void forceReload() {
        try {
            fileData = new FileData(com.electronwill.toml.Toml.read(getFile()));
        } catch (IOException e) {
            System.err.println("Exception while reloading '" + getName() + "'");
            System.err.println("Directory: '" + FileUtils.getParentDirPath(file) + "'");
            e.printStackTrace();
        }
    }

    @Override
    protected void write(FileData data) {
        try {
            com.electronwill.toml.Toml.write(data.toMap(), getFile());
        } catch (IOException ex) {
            System.err.println("Exception while writing fileData to file '" + getName() + "'");
            System.err.println("In '" + FileUtils.getParentDirPath(file) + "'");
            ex.printStackTrace();
        }
    }
}
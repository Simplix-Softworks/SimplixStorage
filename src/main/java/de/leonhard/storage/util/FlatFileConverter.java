package de.leonhard.storage.util;

import de.leonhard.storage.internal.FlatFile;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@UtilityClass
@SuppressWarnings("unused")
public class FlatFileConverter {

    public void addAllData(final @NotNull FlatFile source,
                           final @NotNull FlatFile destination) {
        Objects.requireNonNull(destination.getFileData()).clear();
        destination.getFileData().loadData(Objects.requireNonNull(source.getFileData()).toMap());
        destination.write();
    }
}

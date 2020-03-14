package de.leonhard.storage.util;

import de.leonhard.storage.internal.FlatFile;
import lombok.experimental.UtilityClass;

@UtilityClass
public class FlatFileConverter {

  public void addAllData(final FlatFile source, final FlatFile destination) {
    destination.getFileData().clear();
    destination.getFileData().loadData(source.getFileData().toMap());
    destination.write();
  }
}

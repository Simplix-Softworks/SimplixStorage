package de.leonhard.storage.internal.base.interfaces;

import de.leonhard.storage.internal.base.FlatFile;
import org.jetbrains.annotations.NotNull;


@SuppressWarnings("unused")
public interface ReloadBase {

	boolean shouldReload(final @NotNull FlatFile flatFile);
}
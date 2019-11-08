package de.leonhard.storage.internal.base.interfaces;

import java.io.File;
import java.nio.file.Path;
import org.jetbrains.annotations.NotNull;


@SuppressWarnings("unused")
public interface FileTypeBase {

	String addExtensionTo(final @NotNull String filePath);

	Path addExtensionTo(final @NotNull Path filePath);

	File addExtensionTo(final @NotNull File file);

	boolean isTypeOf(final @NotNull String filePath);

	boolean isTypeOf(final @NotNull Path filePath);

	boolean isTypeOf(final @NotNull File file);


	String toLowerCase();

	@Override
	String toString();
}
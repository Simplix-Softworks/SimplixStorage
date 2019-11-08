package de.leonhard.storage.internal.base;

import de.leonhard.storage.internal.base.interfaces.FileTypeBase;
import de.leonhard.storage.internal.utils.basic.FileTypeUtils;
import de.leonhard.storage.internal.utils.basic.Objects;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.jetbrains.annotations.NotNull;


@SuppressWarnings("unused")
public enum FileType implements FileTypeBase {

	JSON("json"),
	YAML("yml"),
	TOML("toml"),
	LIGHTNING("ls"),
	DEFAULT("txt");


	private final String extension;

	FileType(final @NotNull String extension) {
		this.extension = extension;
	}

	@Override
	public String addExtensionTo(final @NotNull String filePath) {
		return (Objects.notNull(filePath, "Path must not be null") + "." + this.extension);
	}

	@Override
	public Path addExtensionTo(final @NotNull Path filePath) {
		return Paths.get(Objects.notNull(filePath, "Path must not be null") + "." + this.extension);
	}

	@Override
	public File addExtensionTo(final @NotNull File file) {
		return new File(Objects.notNull(file, "Path must not be null").getAbsolutePath() + "." + this.extension);
	}

	@Override
	public boolean isTypeOf(final @NotNull String filePath) {
		return FileTypeUtils.getExtension(Objects.notNull(filePath, "FilePath must not be null")).equals(this.toLowerCase());
	}

	@Override
	public String toLowerCase() {
		return this.extension.toLowerCase();
	}

	@Override
	public boolean isTypeOf(final @NotNull Path filePath) {
		return FileTypeUtils.getExtension(Objects.notNull(filePath, "FilePath must not be null")).equals(this.toLowerCase());
	}

	@Override
	public boolean isTypeOf(final @NotNull File file) {
		return FileTypeUtils.getExtension(Objects.notNull(file, "File must not be null")).equals(this.toLowerCase());
	}

	@Override
	public String toString() {
		return this.extension;
	}
}
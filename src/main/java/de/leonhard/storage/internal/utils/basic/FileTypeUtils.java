package de.leonhard.storage.internal.utils.basic;

import de.leonhard.storage.internal.base.FlatFile;
import java.io.File;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;


/**
 * Basic utility methods for the FileType Enum.
 */
@SuppressWarnings({"WeakerAccess", "unused"})
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FileTypeUtils {

	/**
	 * Add a give FileType extension to a String.
	 *
	 * @param path     the path which the extension should be added to.
	 * @param fileType the FileType to be used.
	 * @return the path with the give FileType extension.
	 */
	public static String addExtension(final @NotNull String path, final @NotNull FlatFile.FileType fileType) {
		return (path + "." + fileType);
	}

	/**
	 * Check whether a given File is of the given FileType.
	 *
	 * @param file     the File to be checked.
	 * @param fileType the FileType to be checked against.
	 * @return true if the File is of the given FileType, otherwise false.
	 */
	public static boolean isType(final @NotNull File file, final @NotNull FlatFile.FileType fileType) {
		return getFileType(file) == (fileType);
	}

	/**
	 * Returns the FileType of a given File.
	 *
	 * @param file the given File.
	 * @return the FileType of the File.
	 */
	public static FlatFile.FileType getFileType(final @NotNull File file) {
		return getFileType(getExtension(file));
	}

	/**
	 * Returns the FileType Enum associated with the given extension.
	 *
	 * @param extension the extension to be checked.
	 * @return the FileType Enum of the give extension or null if no matching Enum exists.
	 */
	public static FlatFile.FileType getFileType(final @NotNull String extension) {
		switch (extension) {
			case "json":
				return FlatFile.FileType.JSON;
			case "yml":
				return FlatFile.FileType.YAML;
			case "toml":
				return FlatFile.FileType.TOML;
			case "CSV":
				return FlatFile.FileType.CSV;
			case "ls":
				return FlatFile.FileType.LIGHTNING;
			default:
				return FlatFile.FileType.DEFAULT;
		}
	}

	/**
	 * Returns the extension of a given File.
	 *
	 * @param file the File to be checked.
	 * @return the extension of the given File.
	 */
	public static String getExtension(final @NotNull File file) {
		return getExtension(file.getName());
	}

	/**
	 * Returns the extension of a given File.
	 *
	 * @param path the Path of the File to be checked.
	 * @return the extension of the given File.
	 */
	public static String getExtension(final @NotNull String path) {
		return path.lastIndexOf(".") > 0 ? path.substring(path.lastIndexOf(".") + 1) : "";
	}
}
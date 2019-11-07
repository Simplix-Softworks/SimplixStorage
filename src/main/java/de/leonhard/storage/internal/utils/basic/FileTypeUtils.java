package de.leonhard.storage.internal.utils.basic;

import de.leonhard.storage.internal.base.FlatFile;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
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
	 * @param file     the filePath which the extension should be added to.
	 * @param fileType the FileType to be used.
	 * @return the filePath with the give FileType extension.
	 */
	public static File addExtension(final @NotNull File file, final @NotNull FlatFile.FileType fileType) {
		return new File(Objects.notNull(file, "Path must not be null").getAbsolutePath() + "." + Objects.notNull(fileType, "FileType must not be null"));
	}

	/**
	 * Add a give FileType extension to a String.
	 *
	 * @param filePath the filePath which the extension should be added to.
	 * @param fileType the FileType to be used.
	 * @return the filePath with the give FileType extension.
	 */
	public static String addExtension(final @NotNull String filePath, final @NotNull FlatFile.FileType fileType) {
		return (Objects.notNull(filePath, "Path must not be null") + "." + Objects.notNull(fileType, "FileType must not be null"));
	}

	/**
	 * Add a give FileType extension to a String.
	 *
	 * @param filePath the filePath which the extension should be added to.
	 * @param fileType the FileType to be used.
	 * @return the filePath with the give FileType extension.
	 */
	public static Path addExtension(final @NotNull Path filePath, final @NotNull FlatFile.FileType fileType) {
		return Paths.get(Objects.notNull(filePath, "Path must not be null") + "." + Objects.notNull(fileType, "FileType must not be null"));
	}


	/**
	 * Check whether a given File is of the given FileType.
	 *
	 * @param file     the File to be checked.
	 * @param fileType the FileType to be checked against.
	 * @return true if the File is of the given FileType, otherwise false.
	 */
	public static boolean isType(final @NotNull File file, final @NotNull FlatFile.FileType fileType) {
		return getFileType(Objects.notNull(file, "File must not be null")) == Objects.notNull(fileType, "FileType must not be null");
	}

	/**
	 * Check whether a given File is of the given FileType.
	 *
	 * @param filePath the File to be checked.
	 * @param fileType the FileType to be checked against.
	 * @return true if the File is of the given FileType, otherwise false.
	 */
	public static boolean isType(final @NotNull Path filePath, final @NotNull FlatFile.FileType fileType) {
		return getFileType(Objects.notNull(filePath, "FilePath must not be null")) == Objects.notNull(fileType, "FileType must not be null");
	}

	/**
	 * Check whether a given File is of the given FileType.
	 *
	 * @param filePath the File to be checked.
	 * @param fileType the FileType to be checked against.
	 * @return true if the File is of the given FileType, otherwise false.
	 */
	public static boolean isType(final @NotNull String filePath, final @NotNull FlatFile.FileType fileType) {
		return getFileType(Objects.notNull(filePath, "FilePath must not be null")) == Objects.notNull(fileType, "FileType must not be null");
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
	 * Returns the FileType Enum associated with the given filePath.
	 *
	 * @param filePath the filePath to be checked.
	 * @return the FileType Enum of the give filePath or null if no matching Enum exists.
	 */
	public static FlatFile.FileType getFileType(final @NotNull Path filePath) {
		return getFileType(getExtension(filePath));
	}

	/**
	 * Returns the FileType Enum associated with the given filePath.
	 *
	 * @param filePath the filePath to be checked.
	 * @return the FileType Enum of the give filePath or null if no matching Enum exists.
	 */
	public static FlatFile.FileType getFileType(final @NotNull String filePath) {
		switch (Objects.notNull(filePath, "Extension must not be null").toLowerCase()) {
			case "json":
				return FlatFile.FileType.JSON;
			case "yml":
				return FlatFile.FileType.YAML;
			case "toml":
				return FlatFile.FileType.TOML;
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
	 * @param filePath the Path of the File to be checked.
	 * @return the extension of the given File.
	 */
	public static String getExtension(final @NotNull Path filePath) {
		return getExtension(filePath.toString());
	}

	/**
	 * Returns the extension of a given File.
	 *
	 * @param filePath the Path of the File to be checked.
	 * @return the extension of the given File.
	 */
	public static String getExtension(final @NotNull String filePath) {
		Objects.checkNull(filePath, "FilePath must not be null");
		char ch;
		int len;
		if ((len = filePath.length()) == 0
			|| (ch = filePath.charAt(len - 1)) == '/'
			|| ch == '\\'
			|| ch == '.') {
			return "";
		}
		int dotInd = filePath.lastIndexOf('.');
		int sepInd = Math.max(filePath.lastIndexOf('/'), filePath.lastIndexOf('\\'));
		if (dotInd <= sepInd) {
			return "";
		} else {
			return filePath.substring(dotInd + 1).toLowerCase();
		}
	}
}
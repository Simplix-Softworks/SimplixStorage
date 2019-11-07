package de.leonhard.storage.internal.utils.basic;

import java.io.File;
import java.nio.file.Path;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;


/**
 * Basic utility methods for the FileType Enum.
 */
@SuppressWarnings("unused")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FileTypeUtils {

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
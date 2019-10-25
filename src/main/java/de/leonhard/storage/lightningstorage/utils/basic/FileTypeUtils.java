package de.leonhard.storage.lightningstorage.utils.basic;

import de.leonhard.storage.lightningstorage.internal.base.FlatFile;
import de.leonhard.storage.lightningstorage.internal.enums.ConfigSetting;
import de.leonhard.storage.lightningstorage.internal.enums.DataType;
import java.io.File;
import java.util.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


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
	public static String addExtension(@NotNull final String path, @NotNull final FlatFile.FileType fileType) {
		return (path + "." + fileType);
	}

	/**
	 * Check whether a given File is of the given FileType.
	 *
	 * @param file     the File to be checked.
	 * @param fileType the FileType to be checked against.
	 * @return true if the File is of the given FileType, otherwise false.
	 */
	public static boolean isType(@NotNull final File file, @NotNull final FlatFile.FileType fileType) {
		return getFileType(file) == (fileType);
	}

	/**
	 * Returns the FileType of a given File.
	 *
	 * @param file the given File.
	 * @return the FileType of the File.
	 */
	public static FlatFile.FileType getFileType(@NotNull final File file) {
		return getFileType(getExtension(file));
	}

	/**
	 * Returns the FileType Enum associated with the given extension.
	 *
	 * @param extension the extension to be checked.
	 * @return the FileType Enum of the give extension or null if no matching Enum exists.
	 */
	public static FlatFile.FileType getFileType(@NotNull final String extension) {
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
	public static String getExtension(@NotNull final File file) {
		return getExtension(file.getName());
	}

	/**
	 * Returns the extension of a given File.
	 *
	 * @param path the Path of the File to be checked.
	 * @return the extension of the given File.
	 */
	public static String getExtension(@NotNull final String path) {
		return path.lastIndexOf(".") > 0 ? path.substring(path.lastIndexOf(".") + 1) : "";
	}

	/**
	 * Get a Map of the proper Type defined by your ConfigSetting.
	 *
	 * @param configSetting the ConfigSetting to be used.
	 * @param map           the Map to be imported from(an empty Map will be returned if @param map is null)
	 * @return a Map containing the Data of @param map.
	 */
	public static Map<String, Object> getNewDataMap(@NotNull final DataType dataType, @Nullable final ConfigSetting configSetting, @Nullable final Map<String, Object> map) {
		if (dataType == DataType.SORTED) {
			return map == null ? new LinkedHashMap<>() : new LinkedHashMap<>(map);
		} else if (dataType == DataType.STANDARD) {
			return map == null ? new HashMap<>() : new HashMap<>(map);
		} else if (dataType == DataType.AUTOMATIC) {
			if (configSetting == null || configSetting == ConfigSetting.SKIP_COMMENTS) {
				return map == null ? new HashMap<>() : new HashMap<>(map);
			} else if (configSetting == ConfigSetting.PRESERVE_COMMENTS) {
				return map == null ? new LinkedHashMap<>() : new LinkedHashMap<>(map);
			} else {
				throw new IllegalStateException("Illegal ConfigSetting");
			}
		} else {
			throw new IllegalStateException("Illegal DataType");
		}
	}

	/**
	 * Get a List of the proper Type defined by your ConfigSetting.
	 *
	 * @param configSetting the ConfigSetting to be used.
	 * @param list          the Map to be imported from(an empty List will be returned if @param list is null)
	 * @return a List containing the Data of @param list.
	 */
	public static List<String> getNewDataList(@NotNull final DataType dataType, @Nullable final ConfigSetting configSetting, @Nullable final List<String> list) {
		if (dataType == DataType.SORTED) {
			return list == null ? new LinkedList<>() : new LinkedList<>(list);
		} else if (dataType == DataType.STANDARD) {
			return list == null ? new ArrayList<>() : new ArrayList<>(list);
		} else if (dataType == DataType.AUTOMATIC) {
			if (configSetting == null || configSetting == ConfigSetting.SKIP_COMMENTS) {
				return list == null ? new ArrayList<>() : new ArrayList<>(list);
			} else if (configSetting == ConfigSetting.PRESERVE_COMMENTS) {
				return list == null ? new LinkedList<>() : new LinkedList<>(list);
			} else {
				throw new IllegalStateException("Illegal ConfigSetting");
			}
		} else {
			throw new IllegalStateException("Illegal DataType");
		}
	}
}
package de.leonhard.storage.lightningstorage.utils;

import de.leonhard.storage.lightningstorage.editor.LightningEditor;
import de.leonhard.storage.lightningstorage.internal.base.FileData;
import de.leonhard.storage.lightningstorage.internal.enums.ConfigSetting;
import de.leonhard.storage.lightningstorage.internal.enums.DataType;
import de.leonhard.storage.lightningstorage.utils.basic.FileTypeUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * Adds the utility methods, used by LightningConfig
 */
@SuppressWarnings("unused")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LightningUtils {

	/**
	 * Get the Header from a give FileData.
	 *
	 * @param fileData      the FileData to be used.
	 * @param dataType      the FileDataType to be used with the given FileData.
	 * @param configSetting the ConfigSetting to be used.
	 * @return a List containing the Header of the FileData.
	 */
	public static List<String> getHeader(@NotNull final FileData fileData, @NotNull final DataType dataType, @NotNull ConfigSetting configSetting) {
		List<String> returnList = FileTypeUtils.getNewDataList(dataType, configSetting, null);
		for (String localKey : fileData.singleLayerKeySet()) {
			if (fileData.get(localKey) == LightningEditor.LineType.COMMENT) {
				returnList.add(localKey.substring(0, localKey.lastIndexOf("{=}")));
			} else {
				return returnList;
			}
		}
		return returnList;
	}

	/**
	 * Set the Header of a FileData.
	 *
	 * @param fileData      the FileData to be used.
	 * @param header        the Header to be set.
	 * @param dataType      the FileDataType to be used with the given FileData.
	 * @param configSetting the ConfigSetting to be used.
	 * @return a Map with the given Header.
	 */
	@SuppressWarnings("DuplicatedCode")
	public static Map<String, Object> setHeader(@NotNull final FileData fileData, @Nullable final List<String> header, @NotNull final DataType dataType, @NotNull ConfigSetting configSetting) {
		Map<String, Object> tempMap = fileData.toMap();
		for (String localKey : tempMap.keySet()) {
			if (tempMap.get(localKey) == LightningEditor.LineType.COMMENT) {
				tempMap.remove(localKey);
			} else {
				break;
			}
		}
		Map<String, Object> finalMap = FileTypeUtils.getNewDataMap(dataType, configSetting, null);
		if (header != null) {
			int commentLine = -1;
			for (String comment : header) {
				finalMap.put((comment.startsWith("#") ? comment : "#" + comment) + "{=}" + commentLine, LightningEditor.LineType.COMMENT);
			}
		}
		finalMap.putAll(tempMap);
		return finalMap;
	}

	/**
	 * Set the Header of a FileData.
	 *
	 * @param fileData      the FileData to be used.
	 * @param key           the Key of the SubBlock the Header shall be set to.
	 * @param header        the Header to be set.
	 * @param dataType      the FileDataType to be used with the given FileData.
	 * @param configSetting the ConfigSetting to be used.
	 * @return a Map with the given Header.
	 */
	@SuppressWarnings("DuplicatedCode")
	public static Map<String, Object> setHeader(@NotNull final FileData fileData, @NotNull final String key, @Nullable final List<String> header, @NotNull final DataType dataType, @NotNull ConfigSetting configSetting) {
		if (fileData.get(key) instanceof Map) {
			//noinspection unchecked
			Map<String, Object> tempMap = (Map<String, Object>) fileData.get(key);
			for (String localKey : tempMap.keySet()) {
				if (tempMap.get(localKey) == LightningEditor.LineType.COMMENT) {
					tempMap.remove(localKey);
				} else {
					break;
				}
			}
			Map<String, Object> finalMap = FileTypeUtils.getNewDataMap(dataType, configSetting, null);
			if (header != null) {
				int commentLine = -1;
				for (String comment : header) {
					finalMap.put((comment.startsWith("#") ? comment : "#" + comment) + "{=}" + commentLine, LightningEditor.LineType.COMMENT);
				}
			}
			finalMap.putAll(tempMap);
			fileData.insert(key, finalMap);
		}
		return fileData.toMap();
	}

	/**
	 * Get the Footer from a give FileData.
	 *
	 * @param fileData      the FileData to be used.
	 * @param dataType      the FileDataType to be used with the given FileData.
	 * @param configSetting the ConfigSetting to be used.
	 * @return a List containing the Footer of the FileData.
	 */
	public static List<String> getFooter(@NotNull final FileData fileData, @NotNull final DataType dataType, @NotNull ConfigSetting configSetting) {
		List<String> returnList = FileTypeUtils.getNewDataList(dataType, configSetting, null);
		List<String> keyList = new ArrayList<>(fileData.singleLayerKeySet());
		Collections.reverse(keyList);
		for (String localKey : keyList) {
			if (fileData.get(localKey) == LightningEditor.LineType.COMMENT) {
				returnList.add(localKey.substring(0, localKey.lastIndexOf("{=}")));
			} else {
				Collections.reverse(returnList);
				return returnList;
			}
		}
		Collections.reverse(returnList);
		return returnList;
	}

	/**
	 * Set the Footer of a FileData.
	 *
	 * @param fileData      the FileData to be used.
	 * @param footer        the Footer to be set.
	 * @param dataType      the FileDataType to be used with the given FileData.
	 * @param configSetting the ConfigSetting to be used.
	 * @return a Map with the given Footer.
	 */
	@SuppressWarnings("DuplicatedCode")
	public static Map<String, Object> setFooter(@NotNull final FileData fileData, @Nullable final List<String> footer, @NotNull final DataType dataType, @NotNull ConfigSetting configSetting) {
		Map<String, Object> tempMap = fileData.toMap();
		List<String> keyList = new ArrayList<>(tempMap.keySet());
		Collections.reverse(keyList);
		for (String localKey : keyList) {
			if (tempMap.get(localKey) == LightningEditor.LineType.COMMENT) {
				tempMap.remove(localKey);
			} else {
				break;
			}
		}
		Map<String, Object> finalMap = FileTypeUtils.getNewDataMap(dataType, configSetting, tempMap);
		if (footer != null) {
			int commentLine = -1;
			for (String comment : footer) {
				finalMap.put((comment.startsWith("#") ? comment : "#" + comment) + "{=}" + commentLine, LightningEditor.LineType.COMMENT);
			}
		}
		return finalMap;
	}

	/**
	 * Set the Footer of a FileData.
	 *
	 * @param fileData      the FileData to be used.
	 * @param key           the Key of the SubBlock the Footer shall be set to.
	 * @param footer        the Header to be set.
	 * @param dataType      the FileDataType to be used with the given FileData.
	 * @param configSetting the ConfigSetting to be used.
	 * @return a Map with the given Footer.
	 */
	@SuppressWarnings("DuplicatedCode")
	public static Map<String, Object> setFooter(@NotNull final FileData fileData, @NotNull final String key, @Nullable final List<String> footer, @NotNull final DataType dataType, @NotNull ConfigSetting configSetting) {
		if (fileData.get(key) instanceof Map) {
			//noinspection unchecked
			Map<String, Object> tempMap = (Map<String, Object>) fileData.get(key);
			List<String> keyList = new ArrayList<>(tempMap.keySet());
			Collections.reverse(keyList);
			for (String localKey : keyList) {
				if (tempMap.get(localKey) == LightningEditor.LineType.COMMENT) {
					tempMap.remove(localKey);
				} else {
					break;
				}
			}
			Map<String, Object> finalMap = FileTypeUtils.getNewDataMap(dataType, configSetting, tempMap);
			if (footer != null) {
				int commentLine = -1;
				for (String comment : footer) {
					finalMap.put((comment.startsWith("#") ? comment : "#" + comment) + "{=}" + commentLine, LightningEditor.LineType.COMMENT);
				}
			}
			finalMap.putAll(tempMap);
			fileData.insert(key, finalMap);
		}
		return fileData.toMap();
	}

	/**
	 * Get the Header from a give FileData.
	 *
	 * @param fileData      the FileData to be used.
	 * @param key           the key of the SubBlock the Header shall be getted from.
	 * @param dataType      the FileDataType to be used with the given FileData.
	 * @param configSetting the ConfigSetting to be used.
	 * @return a List containing the Header of the SubBlock.
	 */
	public static List<String> getHeader(@NotNull final FileData fileData, @NotNull final String key, @NotNull final DataType dataType, @NotNull ConfigSetting configSetting) {
		List<String> returnList = FileTypeUtils.getNewDataList(dataType, configSetting, null);
		for (String localKey : fileData.singleLayerKeySet(key)) {
			if (fileData.get(key + "." + localKey) == LightningEditor.LineType.COMMENT) {
				returnList.add(localKey.substring(0, localKey.lastIndexOf("{=}")));
			} else {
				return returnList;
			}
		}
		return returnList;
	}

	/**
	 * Get the Footer from a give FileData.
	 *
	 * @param fileData      the FileData to be used.
	 * @param key           the key of the SubBlock the Footer shall be getted from.
	 * @param dataType      the FileDataType to be used with the given FileData.
	 * @param configSetting the ConfigSetting to be used.
	 * @return a List containing the Footer of the SubBlock.
	 */
	public static List<String> getFooter(@NotNull final FileData fileData, final String key, @NotNull final DataType dataType, @NotNull ConfigSetting configSetting) {
		List<String> returnList = FileTypeUtils.getNewDataList(dataType, configSetting, null);
		List<String> keyList = new ArrayList<>(fileData.singleLayerKeySet(key));
		Collections.reverse(keyList);
		for (String localKey : keyList) {
			if (fileData.get(key + "." + localKey) == LightningEditor.LineType.COMMENT) {
				returnList.add(localKey.substring(0, localKey.lastIndexOf("{=}")));
			} else {
				Collections.reverse(returnList);
				return returnList;
			}
		}
		Collections.reverse(returnList);
		return returnList;
	}
}
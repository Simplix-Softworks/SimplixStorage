package de.leonhard.storage.lightningstorage.utils;

import de.leonhard.storage.lightningstorage.editor.LightningEditor;
import de.leonhard.storage.lightningstorage.internal.base.FileData;
import de.leonhard.storage.lightningstorage.internal.base.FlatFile;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@SuppressWarnings({"unused", "DuplicatedCode"})
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LightningUtils {

	public static List<String> getHeader(@NotNull final FileData fileData, @NotNull final FileData.Type fileDataType, @NotNull FlatFile.ConfigSetting configSetting) {
		List<String> returnList = fileDataType.getNewDataList(configSetting, null);
		for (String localKey : fileData.singleLayerKeySet()) {
			if (fileData.get(localKey) == LightningEditor.LineType.COMMENT) {
				returnList.add(localKey.substring(0, localKey.lastIndexOf("{=}")));
			} else {
				return returnList;
			}
		}
		return returnList;
	}

	public static Map<String, Object> setHeader(@NotNull final FileData fileData, @Nullable final List<String> header, @NotNull final FileData.Type fileDataType, @NotNull FlatFile.ConfigSetting configSetting) {
		Map<String, Object> tempMap = fileData.toMap();
		for (String localKey : tempMap.keySet()) {
			if (tempMap.get(localKey) == LightningEditor.LineType.COMMENT) {
				tempMap.remove(localKey);
			} else {
				break;
			}
		}
		Map<String, Object> finalMap = fileDataType.getNewDataMap(configSetting, null);
		if (header != null) {
			int commentLine = -1;
			for (String comment : header) {
				finalMap.put((comment.startsWith("#") ? comment : "#" + comment) + "{=}" + commentLine, LightningEditor.LineType.COMMENT);
			}
		}
		finalMap.putAll(tempMap);
		return finalMap;
	}

	public static Map<String, Object> setHeader(@NotNull final FileData fileData, @NotNull final String key, @Nullable final List<String> header, @NotNull final FileData.Type fileDataType, @NotNull FlatFile.ConfigSetting configSetting) {
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
			Map<String, Object> finalMap = fileDataType.getNewDataMap(configSetting, null);
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

	public static List<String> getFooter(@NotNull final FileData fileData, @NotNull final FileData.Type fileDataType, @NotNull FlatFile.ConfigSetting configSetting) {
		List<String> returnList = fileDataType.getNewDataList(configSetting, null);
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

	public static Map<String, Object> setFooter(@NotNull final FileData fileData, @Nullable final List<String> footer, @NotNull final FileData.Type fileDataType, @NotNull FlatFile.ConfigSetting configSetting) {
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
		Map<String, Object> finalMap = fileDataType.getNewDataMap(configSetting, tempMap);
		if (footer != null) {
			int commentLine = -1;
			for (String comment : footer) {
				finalMap.put((comment.startsWith("#") ? comment : "#" + comment) + "{=}" + commentLine, LightningEditor.LineType.COMMENT);
			}
		}
		return finalMap;
	}

	public static Map<String, Object> setFooter(@NotNull final FileData fileData, @NotNull final String key, @Nullable final List<String> footer, @NotNull final FileData.Type fileDataType, @NotNull FlatFile.ConfigSetting configSetting) {
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
			Map<String, Object> finalMap = fileDataType.getNewDataMap(configSetting, tempMap);
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

	public static List<String> getHeader(@NotNull final FileData fileData, @NotNull final String key, @NotNull final FileData.Type fileDataType, @NotNull FlatFile.ConfigSetting configSetting) {
		List<String> returnList = fileDataType.getNewDataList(configSetting, null);
		for (String localKey : fileData.singleLayerKeySet(key)) {
			if (fileData.get(key + "." + localKey) == LightningEditor.LineType.COMMENT) {
				returnList.add(localKey.substring(0, localKey.lastIndexOf("{=}")));
			} else {
				return returnList;
			}
		}
		return returnList;
	}

	public static List<String> getFooter(@NotNull final FileData fileData, final String key, @NotNull final FileData.Type fileDataType, @NotNull FlatFile.ConfigSetting configSetting) {
		List<String> returnList = fileDataType.getNewDataList(configSetting, null);
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
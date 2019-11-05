package de.leonhard.storage.internal.utils;

import de.leonhard.storage.internal.base.FileData;
import de.leonhard.storage.internal.editor.LightningEditor;
import de.leonhard.storage.internal.settings.DataType;
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
	 * @param fileData         the FileData to be used.
	 * @param dataType         the FileDataType to be used with the given FileData.
	 * @param preserveComments the CommentSetting to be used.
	 * @return a List containing the Header of the FileData.
	 */
	public static List<String> getHeader(final @NotNull FileData fileData, final @NotNull DataType dataType, final boolean preserveComments) {
		List<String> returnList = dataType.getNewDataList(preserveComments, null);
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
	 * @param fileData         the FileData to be used.
	 * @param header           the Header to be set.
	 * @param dataType         the FileDataType to be used with the given FileData.
	 * @param preserveComments the CommentSetting to be used.
	 * @return a Map with the given Header.
	 */
	@SuppressWarnings("DuplicatedCode")
	public static Map<String, Object> setHeader(final @NotNull FileData fileData, final @Nullable List<String> header, final @NotNull DataType dataType, final boolean preserveComments) {
		Map<String, Object> tempMap = fileData.toMap();
		for (String localKey : tempMap.keySet()) {
			if (tempMap.get(localKey) == LightningEditor.LineType.COMMENT) {
				tempMap.remove(localKey);
			} else {
				break;
			}
		}
		Map<String, Object> finalMap = dataType.getNewDataMap(preserveComments, null);
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
	 * @param fileData         the FileData to be used.
	 * @param key              the Key of the SubBlock the Header shall be set to.
	 * @param header           the Header to be set.
	 * @param dataType         the FileDataType to be used with the given FileData.
	 * @param preserveComments the CommentSetting to be used.
	 * @return a Map with the given Header.
	 */
	@SuppressWarnings("DuplicatedCode")
	public static Map<String, Object> setHeader(final @NotNull FileData fileData, final @NotNull String key, final @Nullable List<String> header, final @NotNull DataType dataType, final boolean preserveComments) {
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
			Map<String, Object> finalMap = dataType.getNewDataMap(preserveComments, null);
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
	 * @param fileData         the FileData to be used.
	 * @param dataType         the FileDataType to be used with the given FileData.
	 * @param preserveComments the CommentSetting to be used.
	 * @return a List containing the Footer of the FileData.
	 */
	public static List<String> getFooter(final @NotNull FileData fileData, final @NotNull DataType dataType, final boolean preserveComments) {
		List<String> returnList = dataType.getNewDataList(preserveComments, null);
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
	 * @param fileData         the FileData to be used.
	 * @param footer           the Footer to be set.
	 * @param dataType         the FileDataType to be used with the given FileData.
	 * @param preserveComments the CommentSetting to be used.
	 * @return a Map with the given Footer.
	 */
	@SuppressWarnings("DuplicatedCode")
	public static Map<String, Object> setFooter(final @NotNull FileData fileData, final @Nullable List<String> footer, final @NotNull DataType dataType, final boolean preserveComments) {
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
		Map<String, Object> finalMap = dataType.getNewDataMap(preserveComments, tempMap);
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
	 * @param fileData         the FileData to be used.
	 * @param key              the Key of the SubBlock the Footer shall be set to.
	 * @param footer           the Header to be set.
	 * @param dataType         the FileDataType to be used with the given FileData.
	 * @param preserveComments the CommentSetting to be used.
	 * @return a Map with the given Footer.
	 */
	@SuppressWarnings("DuplicatedCode")
	public static Map<String, Object> setFooter(final @NotNull FileData fileData, final @NotNull String key, final @Nullable List<String> footer, final @NotNull DataType dataType, final boolean preserveComments) {
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
			Map<String, Object> finalMap = dataType.getNewDataMap(preserveComments, tempMap);
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
	 * @param fileData         the FileData to be used.
	 * @param key              the key of the SubBlock the Header shall be getted from.
	 * @param dataType         the FileDataType to be used with the given FileData.
	 * @param preserveComments the CommentSetting to be used.
	 * @return a List containing the Header of the SubBlock.
	 */
	public static List<String> getHeader(final @NotNull FileData fileData, final @NotNull String key, final @NotNull DataType dataType, final boolean preserveComments) {
		List<String> returnList = dataType.getNewDataList(preserveComments, null);
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
	 * @param fileData         the FileData to be used.
	 * @param key              the key of the SubBlock the Footer shall be getted from.
	 * @param dataType         the FileDataType to be used with the given FileData.
	 * @param preserveComments the CommentSetting to be used.
	 * @return a List containing the Footer of the SubBlock.
	 */
	public static List<String> getFooter(final @NotNull FileData fileData, final String key, final @NotNull DataType dataType, final boolean preserveComments) {
		List<String> returnList = dataType.getNewDataList(preserveComments, null);
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
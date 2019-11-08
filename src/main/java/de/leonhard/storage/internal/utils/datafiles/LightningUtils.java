package de.leonhard.storage.internal.utils.datafiles;

import de.leonhard.storage.internal.base.interfaces.CommentBase;
import de.leonhard.storage.internal.base.interfaces.DataTypeBase;
import de.leonhard.storage.internal.data.FileData;
import de.leonhard.storage.internal.utils.editor.LightningEditor;
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
	 * @param fileData       the FileData to be used.
	 * @param dataType       the FileDataType to be used with the given FileData.
	 * @param commentSetting the CommentSetting to be used.
	 * @return a List containing the Header of the FileData.
	 */
	public static List<String> getHeader(final @NotNull FileData fileData, final @NotNull DataTypeBase dataType, final @NotNull CommentBase commentSetting) {
		List<String> returnList = dataType.getNewDataList(commentSetting, null);
		for (String tempKey : fileData.blockKeySet()) {
			if (fileData.get(tempKey) == LightningEditor.LineType.COMMENT) {
				returnList.add(tempKey.substring(0, tempKey.lastIndexOf("{=}")));
			} else {
				return returnList;
			}
		}
		return returnList;
	}

	/**
	 * Set the Header of a FileData.
	 *
	 * @param fileData       the FileData to be used.
	 * @param header         the Header to be set.
	 * @param dataType       the FileDataType to be used with the given FileData.
	 * @param commentSetting the CommentSetting to be used.
	 * @return a Map with the given Header.
	 */
	@SuppressWarnings("DuplicatedCode")
	public static Map<String, Object> setHeader(final @NotNull FileData fileData, final @Nullable List<String> header, final @NotNull DataTypeBase dataType, final @NotNull CommentBase commentSetting) {
		Map<String, Object> tempMap = fileData.toMap();
		for (String tempKey : tempMap.keySet()) {
			if (tempMap.get(tempKey) == LightningEditor.LineType.COMMENT) {
				tempMap.remove(tempKey);
			} else {
				break;
			}
		}
		Map<String, Object> finalMap = dataType.getNewDataMap(commentSetting, null);
		if (header != null) {
			int commentLine = 0;
			for (String comment : header) {
				finalMap.put((comment.startsWith("#") ? comment : "#" + comment) + "{=}" + commentLine, LightningEditor.LineType.COMMENT);
				commentLine++;
			}
		}
		finalMap.putAll(tempMap);
		return finalMap;
	}

	/**
	 * Set the Header of a FileData.
	 *
	 * @param fileData       the FileData to be used.
	 * @param key            the Key of the SubBlock the Header shall be set to.
	 * @param header         the Header to be set.
	 * @param dataType       the FileDataType to be used with the given FileData.
	 * @param commentSetting the CommentSetting to be used.
	 * @return a Map with the given Header.
	 */
	@SuppressWarnings("DuplicatedCode")
	public static Map<String, Object> setHeader(final @NotNull FileData fileData, final @NotNull String key, final @Nullable List<String> header, final @NotNull DataTypeBase dataType, final @NotNull CommentBase commentSetting) {
		if (fileData.get(key) instanceof Map) {
			//noinspection unchecked
			Map<String, Object> tempMap = (Map<String, Object>) fileData.get(key);
			for (String tempKey : tempMap.keySet()) {
				if (tempMap.get(tempKey) == LightningEditor.LineType.COMMENT) {
					tempMap.remove(tempKey);
				} else {
					break;
				}
			}
			Map<String, Object> finalMap = dataType.getNewDataMap(commentSetting, null);
			if (header != null) {
				int commentLine = 0;
				for (String comment : header) {
					finalMap.put((comment.startsWith("#") ? comment : "#" + comment) + "{=}" + commentLine, LightningEditor.LineType.COMMENT);
					commentLine++;
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
	 * @param fileData       the FileData to be used.
	 * @param dataType       the FileDataType to be used with the given FileData.
	 * @param commentSetting the CommentSetting to be used.
	 * @return a List containing the Footer of the FileData.
	 */
	public static List<String> getFooter(final @NotNull FileData fileData, final @NotNull DataTypeBase dataType, final @NotNull CommentBase commentSetting) {
		List<String> returnList = dataType.getNewDataList(commentSetting, null);
		List<String> keyList = new ArrayList<>(fileData.blockKeySet());
		Collections.reverse(keyList);
		for (String tempKey : keyList) {
			if (fileData.get(tempKey) == LightningEditor.LineType.COMMENT) {
				returnList.add(tempKey.substring(0, tempKey.lastIndexOf("{=}")));
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
	 * @param fileData       the FileData to be used.
	 * @param footer         the Footer to be set.
	 * @param dataType       the FileDataType to be used with the given FileData.
	 * @param commentSetting the CommentSetting to be used.
	 * @return a Map with the given Footer.
	 */
	@SuppressWarnings("DuplicatedCode")
	public static Map<String, Object> setFooter(final @NotNull FileData fileData, final @Nullable List<String> footer, final @NotNull DataTypeBase dataType, final @NotNull CommentBase commentSetting) {
		Map<String, Object> tempMap = fileData.toMap();
		List<String> keyList = new ArrayList<>(tempMap.keySet());
		Collections.reverse(keyList);
		for (String tempKey : keyList) {
			if (tempMap.get(tempKey) == LightningEditor.LineType.COMMENT) {
				tempMap.remove(tempKey);
			} else {
				break;
			}
		}
		Map<String, Object> finalMap = dataType.getNewDataMap(commentSetting, tempMap);
		if (footer != null) {
			int commentLine = 0;
			for (String comment : footer) {
				finalMap.put((comment.startsWith("#") ? comment : "#" + comment) + "{=}" + commentLine, LightningEditor.LineType.COMMENT);
				commentLine++;
			}
		}
		return finalMap;
	}

	/**
	 * Set the Footer of a FileData.
	 *
	 * @param fileData       the FileData to be used.
	 * @param key            the Key of the SubBlock the Footer shall be set to.
	 * @param footer         the Header to be set.
	 * @param dataType       the FileDataType to be used with the given FileData.
	 * @param commentSetting the CommentSetting to be used.
	 * @return a Map with the given Footer.
	 */
	@SuppressWarnings("DuplicatedCode")
	public static Map<String, Object> setFooter(final @NotNull FileData fileData, final @NotNull String key, final @Nullable List<String> footer, final @NotNull DataTypeBase dataType, final @NotNull CommentBase commentSetting) {
		if (fileData.get(key) instanceof Map) {
			//noinspection unchecked
			Map<String, Object> tempMap = (Map<String, Object>) fileData.get(key);
			List<String> keyList = new ArrayList<>(tempMap.keySet());
			Collections.reverse(keyList);
			for (String tempKey : keyList) {
				if (tempMap.get(tempKey) == LightningEditor.LineType.COMMENT) {
					tempMap.remove(tempKey);
				} else {
					break;
				}
			}
			Map<String, Object> finalMap = dataType.getNewDataMap(commentSetting, tempMap);
			if (footer != null) {
				int commentLine = 0;
				for (String comment : footer) {
					finalMap.put((comment.startsWith("#") ? comment : "#" + comment) + "{=}" + commentLine, LightningEditor.LineType.COMMENT);
					commentLine++;
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
	 * @param fileData       the FileData to be used.
	 * @param key            the Key of the SubBlock the Header shall be getted from.
	 * @param dataType       the FileDataType to be used with the given FileData.
	 * @param commentSetting the CommentSetting to be used.
	 * @return a List containing the Header of the SubBlock.
	 */
	public static List<String> getHeader(final @NotNull FileData fileData, final @NotNull String key, final @NotNull DataTypeBase dataType, final @NotNull CommentBase commentSetting) {
		List<String> returnList = dataType.getNewDataList(commentSetting, null);
		for (String tempKey : fileData.blockKeySet(key)) {
			if (fileData.get(key + "." + tempKey) == LightningEditor.LineType.COMMENT) {
				returnList.add(tempKey.substring(0, tempKey.lastIndexOf("{=}")));
			} else {
				return returnList;
			}
		}
		return returnList;
	}

	/**
	 * Get the Footer from a give FileData.
	 *
	 * @param fileData       the FileData to be used.
	 * @param key            the key of the SubBlock the Footer shall be getted from.
	 * @param dataType       the FileDataType to be used with the given FileData.
	 * @param commentSetting the CommentSetting to be used.
	 * @return a List containing the Footer of the SubBlock.
	 */
	public static List<String> getFooter(final @NotNull FileData fileData, final @NotNull String key, final @NotNull DataTypeBase dataType, final @NotNull CommentBase commentSetting) {
		List<String> returnList = dataType.getNewDataList(commentSetting, null);
		List<String> keyList = new ArrayList<>(fileData.blockKeySet(key));
		Collections.reverse(keyList);
		for (String tempKey : keyList) {
			if (fileData.get(key + "." + tempKey) == LightningEditor.LineType.COMMENT) {
				returnList.add(tempKey.substring(0, tempKey.lastIndexOf("{=}")));
			} else {
				Collections.reverse(returnList);
				return returnList;
			}
		}
		Collections.reverse(returnList);
		return returnList;
	}

	/**
	 * Get the Comments from a given FileData compatible with LightningFile.
	 *
	 * @param fileData       the FileData to be used.
	 * @param dataType       the FileDataType to be used with the given FileData.
	 * @param commentSetting the CommentSetting to be used.
	 * @param deep           defining, if it should get all comments or only the ones in the top Layer.
	 * @return a List containing the Comments of the FileData.
	 */
	public static List<String> getComments(final @NotNull FileData fileData, final @NotNull DataTypeBase dataType, final @NotNull CommentBase commentSetting, final boolean deep) {
		List<String> returnList = dataType.getNewDataList(commentSetting, null);
		List<String> keyList = deep ? new ArrayList<>(fileData.keySet()) : new ArrayList<>(fileData.blockKeySet());
		for (String tempKey : keyList) {
			if (fileData.get(tempKey) == LightningEditor.LineType.COMMENT) {
				returnList.add(tempKey.substring(0, tempKey.lastIndexOf("{=}")));
			}
		}
		return returnList;
	}

	/**
	 * Get the Comments from a given FileData compatible with LightningFile.
	 *
	 * @param fileData       the FileData to be used.
	 * @param key            the key of the SubBlock the Footer shall be getted from.
	 * @param dataType       the FileDataType to be used with the given FileData.
	 * @param commentSetting the CommentSetting to be used.
	 * @param deep           defining, if it should get all comments or only the ones in the given SubBlock.
	 * @return a List containing the Comments of the SubBlock.
	 */
	public static List<String> getComments(final @NotNull FileData fileData, final @NotNull String key, final @NotNull DataTypeBase dataType, final @NotNull CommentBase commentSetting, final boolean deep) {
		List<String> returnList = dataType.getNewDataList(commentSetting, null);
		List<String> keyList = deep ? new ArrayList<>(fileData.keySet(key)) : new ArrayList<>(fileData.blockKeySet(key));
		for (String tempKey : keyList) {
			if (fileData.get(key + "." + tempKey) == LightningEditor.LineType.COMMENT) {
				returnList.add(tempKey.substring(0, tempKey.lastIndexOf("{=}")));
			}
		}
		return returnList;
	}
}
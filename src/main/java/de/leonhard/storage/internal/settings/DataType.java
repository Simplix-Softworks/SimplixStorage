package de.leonhard.storage.internal.settings;

import de.leonhard.storage.internal.utils.basic.Objects;
import java.util.*;
import org.jetbrains.annotations.Nullable;


/**
 * An Enum defining how the Data should be stored
 */
public enum DataType {

	/**
	 * The Data is stored in a LinkedHashMap.
	 */
	SORTED,
	/**
	 * The Data is stored in a HashMap.
	 */
	STANDARD,
	/**
	 * The Storage type depends on the CommentSetting(HashMap for SKIP_COMMENTS, LinkedHashMap for PRESERVE_COMMENTS).
	 */
	AUTOMATIC;

	/**
	 * Get a Map of the proper Type defined by your CommentSetting.
	 *
	 * @param commentSetting the CommentSetting to be used.
	 * @param map            the Map to be imported from(an empty Map will be returned if @param map is null)
	 * @return a Map containing the Data of @param map.
	 */
	public Map<String, Object> getNewDataMap(final @Nullable Comment commentSetting, final @Nullable Map<String, Object> map) {
		if (this == DataType.SORTED) {
			return map == null ? new LinkedHashMap<>() : new LinkedHashMap<>(map);
		} else if (this == DataType.STANDARD) {
			return map == null ? new HashMap<>() : new HashMap<>(map);
		} else if (this == DataType.AUTOMATIC) {
			if (Objects.notNull(commentSetting) == Comment.PRESERVE) {
				return map == null ? new LinkedHashMap<>() : new LinkedHashMap<>(map);
			} else {
				return map == null ? new HashMap<>() : new HashMap<>(map);
			}
		} else {
			throw new IllegalStateException("Illegal DataType");
		}
	}

	/**
	 * Get a List of the proper Type defined by your CommentSetting.
	 *
	 * @param commentSetting the CommentSetting to be used.
	 * @param list           the Map to be imported from(an empty List will be returned if @param list is null)
	 * @return a List containing the Data of @param list.
	 */
	public List<String> getNewDataList(final @Nullable Comment commentSetting, final @Nullable List<String> list) {
		if (this == DataType.SORTED) {
			return list == null ? new LinkedList<>() : new LinkedList<>(list);
		} else if (this == DataType.STANDARD) {
			return list == null ? new ArrayList<>() : new ArrayList<>(list);
		} else if (this == DataType.AUTOMATIC) {
			if (Objects.notNull(commentSetting) == Comment.PRESERVE) {
				return list == null ? new LinkedList<>() : new LinkedList<>(list);
			} else {
				return list == null ? new ArrayList<>() : new ArrayList<>(list);
			}
		} else {
			throw new IllegalStateException("Illegal DataType");
		}
	}
}
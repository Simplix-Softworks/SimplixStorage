package de.leonhard.storage.internal.settings;

import de.leonhard.storage.internal.base.interfaces.CommentBase;
import de.leonhard.storage.internal.base.interfaces.DataTypeBase;
import de.leonhard.storage.internal.utils.basic.Objects;
import java.util.*;
import org.jetbrains.annotations.Nullable;


/**
 * An Enum defining how the Data should be stored
 */
@SuppressWarnings("unused")
public enum DataType implements DataTypeBase {

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


	@Override
	public Map<String, Object> getNewDataMap(final @Nullable CommentBase commentSetting, final @Nullable Map<String, Object> map) {
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

	@Override
	public List<String> getNewDataList(final @Nullable CommentBase commentSetting, final @Nullable List<String> list) {
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
package de.leonhard.storage.lightningstorage.internal.enums;

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
	 * The Storage type depends on the ConfigSetting(HashMap for SKIP_COMMENTS, LinkedHashMap for PRESERVE_COMMENTS).
	 */
	AUTOMATIC
}

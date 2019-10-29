package de.leonhard.storage.internal.enums;

/**
 * an Enum defining the reload behaviour of the Data classes
 */
public enum Reload {

	/**
	 * reloads every time you try to get something from the config
	 */
	AUTOMATICALLY,
	/**
	 * reloads only if the File has changed.
	 */
	INTELLIGENT,
	/**
	 * only reloads if you manually call the reload.
	 */
	MANUALLY
}
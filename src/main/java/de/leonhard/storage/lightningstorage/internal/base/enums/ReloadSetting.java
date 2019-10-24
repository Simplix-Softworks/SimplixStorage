package de.leonhard.storage.lightningstorage.internal.base.enums;

public enum ReloadSetting {

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
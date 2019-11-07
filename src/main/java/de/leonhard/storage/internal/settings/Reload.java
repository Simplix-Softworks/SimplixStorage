package de.leonhard.storage.internal.settings;

import de.leonhard.storage.internal.base.FlatFile;
import lombok.Getter;
import lombok.Setter;


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
	MANUALLY;

	@Getter
	@Setter
	private FlatFile flatFile;

	public boolean shouldReload() {
		switch (this) {
			case AUTOMATICALLY:
				return true;
			case INTELLIGENT:
				return this.flatFile.hasChanged();
			default:
				return false;
		}
	}
}
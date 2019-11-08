package de.leonhard.storage.internal.settings;

import de.leonhard.storage.internal.base.FlatFile;
import de.leonhard.storage.internal.base.interfaces.ReloadBase;
import org.jetbrains.annotations.NotNull;


/**
 * an Enum defining the reload behaviour of the Data classes
 */
@SuppressWarnings("unused")
public enum Reload implements ReloadBase {

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

	private FlatFile flatFile;

	@Override
	public FlatFile getFlatFile() {
		return flatFile;
	}

	@Override
	public void setFlatFile(final @NotNull FlatFile flatFile) {
		this.flatFile = flatFile;
	}

	@Override
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
package de.leonhard.storage.internal.settings;

import de.leonhard.storage.internal.base.FlatFile;
import de.leonhard.storage.internal.base.interfaces.ReloadBase;
import de.leonhard.storage.internal.utils.basic.Objects;
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
				if (this.flatFile == null) {
					return true;
				} else {
					return this.flatFile.hasChanged();
				}
			default:
				return false;
		}
	}

	@Override
	public boolean shouldReload(final @NotNull FlatFile flatFile) {
		switch (this) {
			case AUTOMATICALLY:
				return true;
			case INTELLIGENT:
				return Objects.notNull(flatFile).hasChanged();
			default:
				return false;
		}
	}
}
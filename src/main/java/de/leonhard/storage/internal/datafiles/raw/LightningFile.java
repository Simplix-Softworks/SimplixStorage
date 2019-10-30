package de.leonhard.storage.internal.datafiles.raw;

import de.leonhard.storage.internal.base.CommentEnabledFile;
import de.leonhard.storage.internal.base.FileData;
import de.leonhard.storage.internal.datafiles.section.LightningSection;
import de.leonhard.storage.internal.editor.LightningEditor;
import de.leonhard.storage.internal.enums.Comment;
import de.leonhard.storage.internal.enums.DataType;
import de.leonhard.storage.internal.enums.Reload;
import de.leonhard.storage.internal.utils.FileUtils;
import de.leonhard.storage.internal.utils.basic.Valid;
import java.io.File;
import java.io.InputStream;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * Class to manager Lightning-Type Files
 */
@SuppressWarnings("unused")
public class LightningFile extends CommentEnabledFile {

	public LightningFile(@NotNull final File file, @Nullable final InputStream inputStream, @Nullable final Reload reloadSetting, @Nullable final Comment commentSetting, @Nullable final DataType dataType) {
		super(file, FileType.LIGHTNING);
		if (create() && inputStream != null) {
			FileUtils.writeToFile(this.file, inputStream);
		}

		if (commentSetting != null) {
			setCommentSetting(commentSetting);
		}
		if (dataType != null) {
			setDataType(dataType);
		}
		if (reloadSetting != null) {
			setReloadSetting(reloadSetting);
		}

		this.fileData = new FileData(LightningEditor.readData(this.file, getDataType(), getCommentSetting()));
		this.lastLoaded = System.currentTimeMillis();
	}

	public void reload(@NotNull final Comment commentSetting) {
		setCommentSetting(commentSetting);
		reload();
	}

	@Override
	public void reload() {
		try {
			this.fileData.loadData(LightningEditor.readData(this.file, getDataType(), getCommentSetting()));
			this.lastLoaded = System.currentTimeMillis();
		} catch (IllegalArgumentException | IllegalStateException e) {
			System.err.println("Exception while reloading '" + this.file.getAbsolutePath() + "'");
			e.printStackTrace();
			throw new IllegalStateException();
		}
	}

	@Override
	public Object get(@NotNull final String key) {
		Valid.notNull(key, "Key must not be null");
		update();
		return fileData.get(key);
	}

	public synchronized void set(@NotNull final String key, @Nullable final Object value, @NotNull final Comment commentSetting) {
		setCommentSetting(commentSetting);
		set(key, value);
	}

	@Override
	public synchronized void set(@NotNull final String key, @Nullable final Object value) {
		Valid.notNull(key, "Key must not be null");
		if (insert(key, value)) {
			try {
				LightningEditor.writeData(this.file, this.fileData.toMap(), getCommentSetting());
			} catch (IllegalStateException | IllegalArgumentException e) {
				System.err.println("Error while writing to '" + getAbsolutePath() + "'");
				e.printStackTrace();
				throw new IllegalStateException();
			}
		}
	}

	public synchronized void remove(@NotNull final String key, @NotNull final Comment commentSetting) {
		setCommentSetting(commentSetting);
		remove(key);
	}

	@Override
	public synchronized void remove(@NotNull final String key) {
		Valid.notNull(key, "Key must not be null");

		update();

		if (fileData.containsKey(key)) {
			fileData.remove(key);

			try {
				LightningEditor.writeData(this.file, this.fileData.toMap(), getCommentSetting());
			} catch (IllegalStateException e) {
				System.err.println("Error while writing to '" + getAbsolutePath() + "'");
				e.printStackTrace();
				throw new IllegalStateException();
			}
		}
	}

	/**
	 * Get a Section with a defined SectionKey
	 *
	 * @param sectionKey the sectionKey to be used as a prefix by the Section
	 * @return the Section using the given sectionKey
	 */
	@Override
	public LightningSection getSection(@NotNull final String sectionKey) {
		return new LightningSection(this, sectionKey);
	}

	protected final LightningFile getLightningFileInstance() {
		return this;
	}

	@Override
	public boolean equals(@Nullable final Object obj) {
		if (obj == this) {
			return true;
		} else if (obj == null || this.getClass() != obj.getClass()) {
			return false;
		} else {
			LightningFile lightningFile = (LightningFile) obj;
			return super.equals(lightningFile.getFlatFileInstance());
		}
	}
}
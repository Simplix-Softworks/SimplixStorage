package de.leonhard.storage.internal.datafiles.raw;

import de.leonhard.storage.internal.base.CommentEnabledFile;
import de.leonhard.storage.internal.base.FileData;
import de.leonhard.storage.internal.datafiles.section.LightningSection;
import de.leonhard.storage.internal.editor.LightningEditor;
import de.leonhard.storage.internal.settings.Comment;
import de.leonhard.storage.internal.settings.DataType;
import de.leonhard.storage.internal.settings.Reload;
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
			this.setCommentSetting(commentSetting);
		}
		if (dataType != null) {
			this.setDataType(dataType);
		}
		if (reloadSetting != null) {
			this.setReloadSetting(reloadSetting);
		}

		this.fileData = new FileData(LightningEditor.readData(this.file, this.getDataType(), this.getCommentSetting()));
		this.lastLoaded = System.currentTimeMillis();
	}

	@Override
	public void reload() {
		try {
			this.fileData.loadData(LightningEditor.readData(this.file, this.getDataType(), this.getCommentSetting()));
			this.lastLoaded = System.currentTimeMillis();
		} catch (IllegalArgumentException | IllegalStateException e) {
			System.err.println("Exception while reloading '" + this.getAbsolutePath() + "'");
			e.printStackTrace();
			throw new IllegalStateException();
		}
	}

	@Override
	public Object get(@NotNull final String key) {
		Valid.notNull(key, "Key must not be null");
		update();
		return this.fileData.get(key);
	}

	@Override
	public synchronized void set(@NotNull final String key, @Nullable final Object value) {
		Valid.notNull(key, "Key must not be null");
		if (this.insert(key, value)) {
			try {
				LightningEditor.writeData(this.file, this.fileData.toMap(), this.getCommentSetting());
			} catch (IllegalStateException | IllegalArgumentException e) {
				System.err.println("Error while writing to '" + this.getAbsolutePath() + "'");
				e.printStackTrace();
				throw new IllegalStateException();
			}
		}
	}

	@Override
	public synchronized void remove(@NotNull final String key) {
		Valid.notNull(key, "Key must not be null");

		this.update();

		if (this.fileData.containsKey(key)) {
			this.fileData.remove(key);

			try {
				LightningEditor.writeData(this.file, this.fileData.toMap(), this.getCommentSetting());
			} catch (IllegalStateException e) {
				System.err.println("Error while writing to '" + this.getAbsolutePath() + "'");
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
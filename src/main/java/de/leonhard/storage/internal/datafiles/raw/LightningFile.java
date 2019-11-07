package de.leonhard.storage.internal.datafiles.raw;

import de.leonhard.storage.internal.base.CommentEnabledFile;
import de.leonhard.storage.internal.datafiles.section.LightningSection;
import de.leonhard.storage.internal.editor.LightningEditor;
import de.leonhard.storage.internal.settings.Comment;
import de.leonhard.storage.internal.settings.DataType;
import de.leonhard.storage.internal.settings.Reload;
import de.leonhard.storage.internal.utils.FileUtils;
import de.leonhard.storage.internal.utils.basic.Objects;
import java.io.File;
import java.io.InputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * Class to manage Lightning-Type Files
 */
@SuppressWarnings("unused")
public class LightningFile extends CommentEnabledFile {

	protected LightningFile(final @NotNull File file, final @Nullable InputStream inputStream, final @Nullable Reload reloadSetting, final @Nullable Comment commentSetting, final @Nullable DataType dataType) {
		super(file, FileType.LIGHTNING);

		if (create() && inputStream != null) {
			FileUtils.writeToFile(this.file, inputStream);
		}

		if (reloadSetting != null) {
			this.setReloadSetting(reloadSetting);
		}
		if (commentSetting != null) {
			this.setCommentSetting(commentSetting);
		}
		if (dataType != null) {
			this.setDataType(dataType);
		}

		this.fileData = new LocalFileData(LightningEditor.readData(this.file, this.getDataType(), this.getCommentSetting()));
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
	public Object get(final @NotNull String key) {
		Objects.checkNull(key, "Key must not be null");
		update();
		return this.fileData.get(key);
	}


	@Override
	public synchronized void set(final @NotNull String key, final @Nullable Object value) {
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
	public synchronized void setAll(final @NotNull Map<String, Object> dataMap) {
		if (this.insertAll(dataMap)) {
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
	public synchronized void setAll(final @NotNull String key, final @NotNull Map<String, Object> dataMap) {
		if (this.insertAll(key, dataMap)) {
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
	public synchronized void remove(final @NotNull String key) {
		Objects.checkNull(key, "Key must not be null");

		this.update();

		this.fileData.remove(key);

		try {
			LightningEditor.writeData(this.file, this.fileData.toMap(), this.getCommentSetting());
		} catch (IllegalStateException e) {
			System.err.println("Error while writing to '" + this.getAbsolutePath() + "'");
			e.printStackTrace();
			throw new IllegalStateException();
		}
	}

	@Override
	public synchronized void removeAll(final @NotNull List<String> keys) {
		Objects.checkNull(keys, "List must not be null");

		this.update();

		for (String key : keys) {
			this.fileData.remove(key);
		}

		try {
			LightningEditor.writeData(this.file, this.fileData.toMap(), this.getCommentSetting());
		} catch (IllegalStateException e) {
			System.err.println("Error while writing to '" + this.getAbsolutePath() + "'");
			e.printStackTrace();
			throw new IllegalStateException();
		}
	}

	@Override
	public synchronized void removeAll(final @NotNull String key, final @NotNull List<String> keys) {
		Objects.checkNull(key, "Key must not be null");
		Objects.checkNull(keys, "List must not be null");

		this.update();

		for (String tempKey : keys) {
			this.fileData.remove(key + "." + tempKey);
		}

		try {
			LightningEditor.writeData(this.file, this.fileData.toMap(), this.getCommentSetting());
		} catch (IllegalStateException e) {
			System.err.println("Error while writing to '" + this.getAbsolutePath() + "'");
			e.printStackTrace();
			throw new IllegalStateException();
		}
	}

	@Override
	public Set<String> keySet() {
		this.update();
		return this.keySet(this.fileData.toMap());
	}

	@Override
	public Set<String> keySet(final @NotNull String key) {
		Objects.checkNull(key, "Key must not be null");
		this.update();
		//noinspection unchecked
		return this.fileData.get(key) instanceof Map ? this.keySet((Map<String, Object>) this.fileData.get(key)) : null;
	}

	@Override
	public Set<String> blockKeySet() {
		this.update();
		return this.blockKeySet(this.fileData.toMap());
	}

	@Override
	public Set<String> blockKeySet(final @NotNull String key) {
		Objects.checkNull(key, "Key must not be null");
		this.update();
		//noinspection unchecked
		return this.fileData.get(key) instanceof Map ? this.blockKeySet((Map<String, Object>) this.fileData.get(key)) : null;
	}

	/**
	 * Get a Section with a defined SectionKey
	 *
	 * @param sectionKey the sectionKey to be used as a prefix by the Section
	 * @return the Section using the given sectionKey
	 */
	@Override
	public LightningSection getSection(final @NotNull String sectionKey) {
		return new LocalSection(sectionKey, this);
	}

	protected final LightningFile getLightningFileInstance() {
		return this;
	}

	private Set<String> blockKeySet(final Map<String, Object> map) {
		Set<String> localSet = new HashSet<>();
		for (String key : map.keySet()) {
			if (map.get(key) != LightningEditor.LineType.COMMENT && map.get(key) != LightningEditor.LineType.BLANK_LINE) {
				localSet.add(key);
			}
		}
		return localSet;
	}

	private Set<String> keySet(final Map<String, Object> map) {
		Set<String> localSet = new HashSet<>();
		for (String key : map.keySet()) {
			if (map.get(key) instanceof Map) {
				//noinspection unchecked
				for (String tempKey : this.keySet((Map<String, Object>) map.get(key))) {
					localSet.add(key + "." + tempKey);
				}
			} else if (map.get(key) != LightningEditor.LineType.COMMENT && map.get(key) != LightningEditor.LineType.BLANK_LINE) {
				localSet.add(key);
			}
		}
		return localSet;
	}

	@Override
	public boolean equals(final @Nullable Object obj) {
		if (obj == this) {
			return true;
		} else if (obj == null || this.getClass() != obj.getClass()) {
			return false;
		} else {
			LightningFile lightningFile = (LightningFile) obj;
			return this.getCommentSetting() == lightningFile.getCommentSetting()
				   && super.equals(lightningFile.getFlatFileInstance());
		}
	}


	private static class LocalSection extends LightningSection {

		private LocalSection(final @NotNull String sectionKey, final @NotNull LightningFile lightningFile) {
			super(sectionKey, lightningFile);
		}
	}
}
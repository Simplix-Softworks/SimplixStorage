package de.leonhard.storage.internal.data.config;

import de.leonhard.storage.internal.base.interfaces.CommentSettingBase;
import de.leonhard.storage.internal.base.interfaces.ConfigBase;
import de.leonhard.storage.internal.base.interfaces.DataTypeBase;
import de.leonhard.storage.internal.base.interfaces.ReloadSettingBase;
import de.leonhard.storage.internal.data.raw.YamlFile;
import de.leonhard.storage.internal.settings.Comment;
import de.leonhard.storage.internal.utils.editor.YamlEditor;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * Extended YamlFile with added methods for Config purposes
 */
@SuppressWarnings("unused")
public class YamlConfig extends YamlFile implements ConfigBase {

	private List<String> header;
	private List<String> footer;
	private List<String> comments;


	protected YamlConfig(final @NotNull File file, final @Nullable InputStream inputStream, final @Nullable ReloadSettingBase reloadSetting, final @Nullable CommentSettingBase commentSetting, final @Nullable DataTypeBase dataType) {
		super(file, inputStream, reloadSetting, commentSetting == null ? Comment.PRESERVE : commentSetting, dataType);
	}


	@Override
	public List<String> getHeader() {
		if (this.getCommentSetting() != Comment.PRESERVE) {
			return new ArrayList<>();
		} else if (!this.shouldReload()) {
			return this.header;
		} else {
			try {
				this.header = YamlEditor.readHeader(this.file);
				return this.header;
			} catch (IOException e) {
				System.err.println("Couldn't get header of '" + this.file.getAbsolutePath() + "'.");
				e.printStackTrace();
				return new ArrayList<>();
			}
		}
	}

	@Override
	public void setHeader(final @Nullable List<String> header) {
		if (header != null) {
			List<String> tmp = new ArrayList<>();
			//Updating the values to have a comments, if someone forgets to set them
			for (final String line : header) {
				if (!line.startsWith("#")) {
					tmp.add("#" + line);
				} else {
					tmp.add(line);
				}
			}
			this.header = tmp;

			if (this.file.length() == 0) {
				try {
					YamlEditor.write(this.file, this.header);
				} catch (IOException e) {
					System.err.println("Error while setting header of '" + this.file.getAbsolutePath() + "'");
					e.printStackTrace();
				}
			} else {
				try {
					final List<String> lines = YamlEditor.read(this.file);
					final List<String> oldHeader = YamlEditor.readHeader(this.file);

					List<String> newLines = this.header;
					lines.removeAll(oldHeader);
					newLines.addAll(lines);

					YamlEditor.write(this.file, newLines);
				} catch (final IOException e) {
					System.err.println("Exception while modifying header of '" + this.file.getAbsolutePath() + "'");
					e.printStackTrace();
				}
			}
		} else {
			this.header = new ArrayList<>();

			try {
				final List<String> lines = YamlEditor.read(this.file);
				final List<String> oldHeader = YamlEditor.readHeader(this.file);

				lines.removeAll(oldHeader);

				YamlEditor.write(this.file, lines);
			} catch (final IOException e) {
				System.err.println("Exception while modifying header of '" + this.file.getAbsolutePath() + "'");
				e.printStackTrace();
			}
		}
	}

	@Override
	public List<String> getFooter() {
		if (this.getCommentSetting() != Comment.PRESERVE) {
			return new ArrayList<>();
		} else if (!this.shouldReload()) {
			return this.footer;
		} else {
			try {
				this.footer = YamlEditor.readFooter(this.file);
				return this.footer;
			} catch (IOException e) {
				System.err.println("Couldn't get footer of '" + this.file.getAbsolutePath() + "'.");
				e.printStackTrace();
				return new ArrayList<>();
			}
		}
	}

	@Override
	public void setFooter(@Nullable List<String> footer) {
		if (footer != null) {
			List<String> tmp = new ArrayList<>();
			//Updating the values to have a comments, if someone forgets to set them
			for (final String line : footer) {
				if (!line.startsWith("#")) {
					tmp.add("#" + line);
				} else {
					tmp.add(line);
				}
			}
			this.footer = tmp;

			if (this.file.length() == 0) {
				try {
					YamlEditor.write(this.file, this.footer);
				} catch (IOException e) {
					System.err.println("Error while setting footer of '" + this.file.getAbsolutePath() + "'");
					e.printStackTrace();
				}
			} else {
				try {
					final List<String> lines = YamlEditor.read(this.file);
					final List<String> oldFooter = YamlEditor.readFooter(this.file);

					lines.removeAll(oldFooter);
					lines.addAll(this.footer);

					YamlEditor.write(this.file, lines);
				} catch (final IOException e) {
					System.err.println("Exception while modifying footer of '" + this.file.getAbsolutePath() + "'");
					e.printStackTrace();
				}
			}
		} else {
			this.footer = new ArrayList<>();

			try {
				final List<String> lines = YamlEditor.read(this.file);
				final List<String> oldFooter = YamlEditor.readFooter(this.file);

				lines.removeAll(oldFooter);

				YamlEditor.write(this.file, lines);
			} catch (final IOException e) {
				System.err.println("Exception while modifying footer of '" + this.file.getAbsolutePath() + "'");
				e.printStackTrace();
			}
		}
	}

	@Override
	public List<String> getComments() {
		if (this.getCommentSetting() != Comment.PRESERVE) {
			return new ArrayList<>();
		} else if (!this.shouldReload()) {
			return this.comments;
		} else {
			try {
				this.comments = YamlEditor.readComments(this.file);
				return this.comments;
			} catch (IOException e) {
				System.err.println("Couldn't get comments from '" + this.file.getAbsolutePath() + "'.");
				e.printStackTrace();
				return new ArrayList<>();
			}
		}
	}


	protected final YamlConfig getConfigInstance() {
		return this;
	}

	@Override
	public boolean equals(final @Nullable Object obj) {
		if (obj == this) {
			return true;
		} else if (obj == null || this.getClass() != obj.getClass()) {
			return false;
		} else {
			YamlConfig yamlConfig = (YamlConfig) obj;
			return this.header.equals(yamlConfig.header)
				   && super.equals(yamlConfig.getYamlFileInstance());
		}
	}
}
package de.leonhard.storage.internal.data.config;

import de.leonhard.storage.internal.base.ConfigBase;
import de.leonhard.storage.internal.data.raw.YamlFile;
import de.leonhard.storage.internal.settings.Comment;
import de.leonhard.storage.internal.settings.DataType;
import de.leonhard.storage.internal.settings.Reload;
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


	protected YamlConfig(final @NotNull File file, final @Nullable InputStream inputStream, final @Nullable Reload reloadSetting, final @Nullable Comment commentSetting, final @Nullable DataType dataType) {
		super(file, inputStream, reloadSetting, commentSetting == null ? Comment.PRESERVE : commentSetting, dataType);
	}


	@Override
	public List<String> getHeader() {
		if (this.getCommentSetting() != Comment.PRESERVE) {
			return new ArrayList<>();
		} else if (!shouldReload()) {
			return this.header;
		} else {
			try {
				this.header = yamlEditor.readHeader();
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

			if (getFile().length() == 0) {
				try {
					this.yamlEditor.write(this.header);
				} catch (IOException e) {
					System.err.println("Error while setting header of '" + this.file.getAbsolutePath() + "'");
					e.printStackTrace();
				}
			} else {
				try {
					final List<String> lines = this.yamlEditor.read();
					final List<String> oldHeader = this.yamlEditor.readHeader();

					List<String> newLines = this.header;
					lines.removeAll(oldHeader);
					newLines.addAll(lines);

					this.yamlEditor.write(newLines);
				} catch (final IOException e) {
					System.err.println("Exception while modifying header of '" + this.file.getAbsolutePath() + "'");
					e.printStackTrace();
				}
			}
		} else {
			this.header = new ArrayList<>();

			try {
				final List<String> lines = this.yamlEditor.read();
				final List<String> oldHeader = this.yamlEditor.readHeader();

				lines.removeAll(oldHeader);

				this.yamlEditor.write(lines);
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
		} else if (!shouldReload()) {
			return this.footer;
		} else {
			try {
				this.footer = yamlEditor.readFooter();
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

			if (getFile().length() == 0) {
				try {
					this.yamlEditor.write(this.footer);
				} catch (IOException e) {
					System.err.println("Error while setting footer of '" + this.file.getAbsolutePath() + "'");
					e.printStackTrace();
				}
			} else {
				try {
					final List<String> lines = this.yamlEditor.read();
					final List<String> oldFooter = this.yamlEditor.readFooter();

					lines.removeAll(oldFooter);
					lines.addAll(this.footer);

					this.yamlEditor.write(lines);
				} catch (final IOException e) {
					System.err.println("Exception while modifying footer of '" + this.file.getAbsolutePath() + "'");
					e.printStackTrace();
				}
			}
		} else {
			this.footer = new ArrayList<>();

			try {
				final List<String> lines = this.yamlEditor.read();
				final List<String> oldFooter = this.yamlEditor.readFooter();

				lines.removeAll(oldFooter);

				this.yamlEditor.write(lines);
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
		} else if (!shouldReload()) {
			return this.comments;
		} else {
			try {
				this.comments = yamlEditor.readComments();
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
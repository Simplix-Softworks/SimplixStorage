package de.leonhard.storage.util;

import lombok.Cleanup;
import lombok.experimental.UtilityClass;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Class for easier, more convenient & strait interaction with files
 */
@UtilityClass
public class FileUtils {

	public File getAndMake(final String name, final String path) {
		Valid.notNull(name);
		Valid.notNull(path);
		return getAndMake(new File(path, name));
	}

	public File getAndMake(final File file) {
		Valid.notNull(file);
		try {
			if (file.getParentFile() != null && !file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
			if (!file.exists()) {
				file.createNewFile();
			}

		} catch (final IOException ex) {
			System.err.println("Error while creating file '" + file.getName() + "'.");
			System.err.println("In: '" + getParentDirPath(file) + "'");
			ex.printStackTrace();
			throw new IllegalStateException();
		}
		return file;
	}

	// ----------------------------------------------------------------------------------------------------
	// Methods for handling the extension of files
	// ----------------------------------------------------------------------------------------------------

	public String getExtension(final String path) {
		Valid.notNull(path);
		return path.lastIndexOf(".") > 0 ? path.substring(path.lastIndexOf(".") + 1) : "";
	}

	public String getExtension(final File file) {
		Valid.notNull(file);
		return getExtension(file.getName());
	}

	public String replaceExtensions(final String fileName) {
		Valid.notNull(fileName, "FileName mustn't be null");
		if (!fileName.contains(".")) {
			return fileName;
		}
		return fileName.replace(getExtension(fileName), "");
	}

	public String getParentDirPath(final File file) {
		Valid.notNull(file);
		return getParentDirPath(file.getAbsolutePath());
	}

	/**
	 * Since file.getParentFile can be null we created an extension function to get
	 * the path of the parent file
	 *
	 * @param fileOrDirPath Path to file
	 * @return Path to file as String
	 */
	public String getParentDirPath(final String fileOrDirPath) {
		Valid.notNull(fileOrDirPath);
		final boolean endsWithSlash = fileOrDirPath.endsWith(File.separator);
		return fileOrDirPath.substring(0, fileOrDirPath.lastIndexOf(File.separatorChar,
				endsWithSlash ? fileOrDirPath.length() - 2 : fileOrDirPath.length() - 1));
	}

	public boolean hasChanged(final File file, final long timeStamp) {
		Valid.notNull(file);
		return timeStamp < file.lastModified();
	}

	// ----------------------------------------------------------------------------------------------------
	// Methods for reading & writing a file
	// ----------------------------------------------------------------------------------------------------

	public InputStream createInputStream(final File file) {
		Valid.notNull(file);
		try {
			return Files.newInputStream(file.toPath());
		} catch (final IOException ex) {
			System.err.println("Exception while creating InputStream from '" + file.getName() + "'");
			System.err.println("At: '" + file.getAbsolutePath() + "'");
			ex.printStackTrace();
			throw new IllegalStateException("InputStream would be null");
		}
	}

	public OutputStream createOutputStream(final File file) {
		try {
			return new FileOutputStream(file);
		} catch (final FileNotFoundException ex) {
			System.err.println("Exception while creating OutputStream from '" + file.getName() + "'");
			System.err.println("At: '" + file.getAbsolutePath() + "'");
			ex.printStackTrace();
			throw new IllegalStateException("OutputStream would be null");
		}
	}

	public Reader createReader(final File file) {
		try {
			return new FileReader(file);
		} catch (final FileNotFoundException ex) {
			System.err.println("Error while creating reader for '" + file.getName() + "'");
			System.err.println("In '" + getParentDirPath(file) + "'");
			ex.printStackTrace();
			throw new IllegalStateException("Can't return null as reader");
		}
	}

	public Writer createWriter(final File file) {
		try {
			return new FileWriter(file);
		} catch (final IOException ex) {
			System.err.println("Error while creating reader for '" + file.getName() + "'");
			System.err.println("In '" + getParentDirPath(file) + "'");
			ex.printStackTrace();
			throw new IllegalStateException("Can't return null as writer");
		}
	}

	public void write(final File file, final List<String> lines) {
		try {
			Files.write(file.toPath(), lines);
		} catch (final IOException ex) {
			System.err.println("Exception while writing to file '" + file.getName() + "'");
			System.err.println("In " + FileUtils.getParentDirPath(file) + "'");
		}
	}

	public void writeToFile(final File file, final InputStream inputStream) {
		try (FileOutputStream outputStream = new FileOutputStream(file)) {
			if (!file.exists()) {
				Files.copy(inputStream, file.toPath());
			} else {
				final byte[] data = new byte[8192];
				int count;
				while ((count = inputStream.read(data, 0, 8192)) != -1) {
					outputStream.write(data, 0, count);
				}
			}
		} catch (final IOException ex) {
			System.err.println("Exception while copying to + '" + file.getName() + "'");
			System.err.println("In '" + getParentDirPath(file) + "'");
			ex.printStackTrace();
		}
	}

	public byte[] readAllBytes(final File file) {
		try {
			return Files.readAllBytes(file.toPath());
		} catch (final IOException ex) {
			System.err.println("Exception while reading '" + file.getName() + "'");
			System.err.println("In '" + getParentDirPath(file) + "'");
			throw new IllegalStateException("Can't return null as byte[]");
		}
	}

	public List<String> readAllLines(final File file) {
		final byte[] fileBytes = readAllBytes(file);
		final String asString = new String(fileBytes);
		return new ArrayList<>(Arrays.asList(asString.split("\n")));
	}

	// ----------------------------------------------------------------------------------------------------
	// Misc
	// ----------------------------------------------------------------------------------------------------

	public void copyFolder(final File source, final File destination) throws IOException {
		Valid.notNull(source);
		Valid.notNull(destination);

		if (source.isDirectory()) {
			if (!destination.exists()) {
				destination.mkdirs();
			}

			final String[] files = source.list();

			if (files == null) {
				return;
			}

			for (final String file : files) {
				final File srcFile = new File(source, file);
				final File destFile = new File(destination, file);

				copyFolder(srcFile, destFile);
			}
		} else {

			@Cleanup final InputStream in = createInputStream(source);
			@Cleanup final OutputStream out = createOutputStream(destination);
			final byte[] buffer = new byte[1024];

			int length;
			while ((length = in.read(buffer)) > 0) {
				out.write(buffer, 0, length);
			}
		}
	}
}
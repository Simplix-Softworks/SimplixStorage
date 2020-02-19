package de.leonhard.storage.util;

import lombok.Cleanup;
import lombok.NonNull;
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

	// ----------------------------------------------------------------------------------------------------
	// Getting Files
	// ----------------------------------------------------------------------------------------------------

	public List<File> listFiles(@NonNull final File folder) {
		return listFiles(folder, null);
	}

	/**
	 * Returns a List of Files in a folder which
	 * ends with a specific extension.
	 * <p>
	 * If there are no files in the folder or the folder is not found,
	 * an empty list is returned instead of null.
	 *
	 * @param folder    Folder to search in.
	 * @param extension Extension to search for. Set to null to skip extension validation.
	 */
	public List<File> listFiles(@NonNull final File folder, @NonNull final String extension) {
		final List<File> result = new ArrayList<>();

		final File[] files = folder.listFiles();

		if (files == null) {
			return result;
		}

		for (final File file : files) {
			//if the extension is null we always add the file.
			if (extension == null || file.getName().endsWith(extension)) {
				result.add(file);
			}
		}

		return result;
	}

	public File getAndMake(@NonNull final String name, @NonNull final String path) {
		return getAndMake(new File(path, name));
	}

	public File getAndMake(@NonNull final File file) {
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

	public void extractInnerResource(@NonNull final String resource, final File destinationDirectory) {


	}

	// ----------------------------------------------------------------------------------------------------
	// Methods for handling the extension of files
	// ----------------------------------------------------------------------------------------------------

	public String getExtension(@NonNull final String path) {
		return path.lastIndexOf(".") > 0 ? path.substring(path.lastIndexOf(".") + 1) : "";
	}

	public String getExtension(@NonNull final File file) {
		return getExtension(file.getName());
	}

	public String replaceExtensions(@NonNull final String fileName) {
		if (!fileName.contains(".")) {
			return fileName;
		}
		return fileName.replace("." + getExtension(fileName), "");
	}

	public String getParentDirPath(@NonNull final File file) {
		return getParentDirPath(file.getAbsolutePath());
	}

	/**
	 * Since file.getParentFile can be null we created an extension function to get
	 * the path of the parent file
	 *
	 * @param fileOrDirPath Path to file
	 * @return Path to file as String
	 */
	public String getParentDirPath(@NonNull final String fileOrDirPath) {
		final boolean endsWithSlash = fileOrDirPath.endsWith(File.separator);
		return fileOrDirPath.substring(0, fileOrDirPath.lastIndexOf(File.separatorChar,
			endsWithSlash ? fileOrDirPath.length() - 2 : fileOrDirPath.length() - 1));
	}

	public boolean hasChanged(final File file, final long timeStamp) {
		if (file == null) {
			return false;
		}
		return timeStamp < file.lastModified();
	}

	// ----------------------------------------------------------------------------------------------------
	// Methods for reading & writing a file
	// ----------------------------------------------------------------------------------------------------

	public InputStream createInputStream(@NonNull final File file) {
		try {
			return Files.newInputStream(file.toPath());
		} catch (final IOException ex) {
			System.err.println("Exception while creating InputStream from '" + file.getName() + "'");
			System.err.println("At: '" + file.getAbsolutePath() + "'");
			ex.printStackTrace();
			throw new IllegalStateException(ex);
		}
	}

	public OutputStream createOutputStream(@NonNull final File file) {
		try {
			return new FileOutputStream(file);
		} catch (final FileNotFoundException ex) {
			System.err.println("Exception while creating OutputStream from '" + file.getName() + "'");
			System.err.println("At: '" + file.getAbsolutePath() + "'");
			ex.printStackTrace();
			throw new IllegalStateException(ex);
		}
	}

	public Reader createReader(@NonNull final File file) {
		try {
			return new FileReader(file);
		} catch (final FileNotFoundException ex) {
			System.err.println("Error while creating reader for '" + file.getName() + "'");
			System.err.println("In '" + getParentDirPath(file) + "'");
			ex.printStackTrace();
			throw new IllegalStateException(ex);
		}
	}

	public Writer createWriter(@NonNull final File file) {
		try {
			return new FileWriter(file);
		} catch (final IOException ex) {
			System.err.println("Error while creating reader for '" + file.getName() + "'");
			System.err.println("In '" + getParentDirPath(file) + "'");
			ex.printStackTrace();
			throw new IllegalStateException(ex);
		}
	}

	public void write(@NonNull final File file, @NonNull final List<String> lines) {
		try {
			Files.write(file.toPath(), lines);
		} catch (final IOException ex) {
			System.err.println("Exception while writing to file '" + file.getName() + "'");
			System.err.println("In " + FileUtils.getParentDirPath(file) + "'");
		}
	}

	public void writeToFile(@NonNull final File file, @NonNull final InputStream inputStream) {
		try (final FileOutputStream outputStream = new FileOutputStream(file)) {
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

	public byte[] readAllBytes(@NonNull final File file) {
		try {
			return Files.readAllBytes(file.toPath());
		} catch (final IOException ex) {
			System.err.println("Exception while reading '" + file.getName() + "'");
			System.err.println("In '" + getParentDirPath(file) + "'");
			throw new IllegalStateException(ex);
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

	public void copyFolder(@NonNull final File source, @NonNull final File destination) throws IOException {

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
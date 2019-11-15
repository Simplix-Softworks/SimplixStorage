package de.leonhard.storage.utils;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class FileUtils {

	public static File getAndMake(String name, String path) {
		Valid.notNull(name);
		Valid.notNull(path);
		return getAndMake(new File(path, name));
	}

	public static File getAndMake(File file) {
		Valid.notNull(file);
		try {
			if (file.getParentFile() != null && !file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
			if (!file.exists()) {
				file.createNewFile();
			}

		} catch (IOException ex) {
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

	public static String getExtension(String path) {
		Valid.notNull(path);
		return path.lastIndexOf(".") > 0 ? path.substring(path.lastIndexOf(".") + 1) : "";
	}

	public static String getExtension(File file) {
		Valid.notNull(file);
		return getExtension(file.getName());
	}

	public static String replaceExtensions(String fileName) {
		Valid.notNull(fileName, "FileName mustn't be null");
		if (!fileName.contains(".")) {
			return fileName;
		}
		return fileName.replace(getExtension(fileName), "");
	}

	public static String getParentDirPath(final File file) {
		Valid.notNull(file);
		return getParentDirPath(file.getAbsolutePath());
	}

	/**
	 * Since file.getParentFile can be null
	 * we created an extension function to get
	 * the path of the parent file
	 *
	 * @param fileOrDirPath Path to file
	 * @return Path to file as String
	 */
	public static String getParentDirPath(String fileOrDirPath) {
		Valid.notNull(fileOrDirPath);
		boolean endsWithSlash = fileOrDirPath.endsWith(File.separator);
		return fileOrDirPath.substring(0, fileOrDirPath.lastIndexOf(File.separatorChar,
				endsWithSlash ? fileOrDirPath.length() - 2 : fileOrDirPath.length() - 1));
	}

	public static boolean hasChanged(File file, long timeStamp) {
		Valid.notNull(file);
		return timeStamp < file.lastModified();
	}


	// ----------------------------------------------------------------------------------------------------
	// Methods for reading & writing a file
	// ----------------------------------------------------------------------------------------------------


	public static InputStream createNewInputStream(File file) {
		Valid.notNull(file);
		try {
			return Files.newInputStream(file.toPath());
		} catch (IOException ex) {
			System.err.println("Exception while creating InputStream from '" + file.getName() + "'");
			System.err.println("At: '" + file.getAbsolutePath() + "'");
			ex.printStackTrace();
			throw new IllegalStateException("InputStream would be null");
		}
	}

	public static void writeToFile(File file, InputStream inputStream) {
		try (final FileOutputStream outputStream = new FileOutputStream(file)) {
			if (!file.exists()) {
				Files.copy(inputStream, file.toPath());
			} else {
				byte[] data = new byte[8192];
				int count;
				while ((count = inputStream.read(data, 0, 8192)) != -1) {
					outputStream.write(data, 0, count);
				}
			}
		} catch (IOException ex) {
			System.err.println("Exception while copying to + '" + file.getName() + "'");
			System.err.println("In '" + getParentDirPath(file) + "'");
			ex.printStackTrace();
		}
	}

	public static List<String> readAllLines(File file) throws IOException {
		byte[] fileBytes = Files.readAllBytes(file.toPath());
		String asString = new String(fileBytes);
		return new ArrayList<>(Arrays.asList(asString.split("\n")));
	}

	// ----------------------------------------------------------------------------------------------------
	// Misc
	// ----------------------------------------------------------------------------------------------------

	public static void copyFolder(File source, File destination) {
		Valid.notNull(source);
		Valid.notNull(destination);
		if (source.isDirectory()) {
			if (!destination.exists()) {
				destination.mkdirs();
			}

			String[] files = source.list();

			for (String file : files) {
				File srcFile = new File(source, file);
				File destFile = new File(destination, file);

				copyFolder(srcFile, destFile);
			}
		} else {
			try (final InputStream in = new FileInputStream(source);
			     final OutputStream out = new FileOutputStream(destination)) {
				byte[] buffer = new byte[1024];

				int length;
				while ((length = in.read(buffer)) > 0) {
					out.write(buffer, 0, length);
				}
			} catch (Exception ex) {
				System.err.println("Exception copying '" + source.getName() + "' to '" + destination.getName() + "'");
				System.err.println("Source-Path: '" + source.getAbsolutePath() + "'");
				ex.printStackTrace();
			}
		}
	}
}
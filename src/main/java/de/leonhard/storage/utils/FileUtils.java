package de.leonhard.storage.utils;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileUtils {


	public static String getExtension(String path) {
		return path.lastIndexOf(".") > 0 ? path.substring(path.lastIndexOf(".") + 1) : "";
	}

	public static String getExtension(File file) {
		return getExtension(file.getName());
	}


	public static String replaceExtensions(String fileName) {
		Valid.notNull(fileName, "FileName mustn't be null");
		if (!fileName.contains(".")) {
			return fileName;
		}
		return fileName.replace(getExtension(fileName), "");
	}

	public static void copyFolder(File source, File destination) {
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
			InputStream in = null;
			OutputStream out = null;

			try {
				in = new FileInputStream(source);
				out = new FileOutputStream(destination);

				byte[] buffer = new byte[1024];

				int length;
				while ((length = in.read(buffer)) > 0) {
					out.write(buffer, 0, length);
				}
			} catch (Exception e) {
				try {
					if (in != null) {
						in.close();
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}

				try {
					if (out != null) {
						out.close();
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}


	public static boolean hasChanged(File file, long timeStamp) {
		return timeStamp < file.lastModified();
	}

	public static File getAndMake(String name, String path) {
		return getAndMake(new File(path, name));
	}

	public static InputStream createNewInputStream(File file) {
		try {
			return Files.newInputStream(file.toPath());
		} catch (IOException e) {
			System.err.println("Exception while creating InputStream from '" + file.getName() + "'");
			System.err.println("At: '" + file.getAbsolutePath() + "'");
			e.printStackTrace();
			throw new IllegalStateException("InputStream would be null");
		}
	}

	public static void writeToFile(File file, InputStream inputStream) {
		try (FileOutputStream outputStream = new FileOutputStream(file)) {
			if (!file.exists()) {
				Files.copy(inputStream, file.toPath());
			} else {
				byte[] data = new byte[8192];
				int count;
				while ((count = inputStream.read(data, 0, 8192)) != -1) {
					outputStream.write(data, 0, count);
				}
			}
		} catch (IOException e) {
			System.err.println("Exception while copying to + '" + file.getAbsolutePath() + "'");
			e.printStackTrace();
		}
	}

	public static File getAndMake(File file) {
		try {
			if (file.getParentFile() != null && !file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
			if (!file.exists()) {
				file.createNewFile();
			}

		} catch (IOException ioException) {
			System.err.println("Error while creating file '" + file.getName() + "'.");
			System.err.println("Path: '" + file.getAbsolutePath() + "'");
			ioException.printStackTrace();
			throw new IllegalStateException();
		}
		return file;
	}

	public static List<String> readAllLines(File file) throws IOException {
		byte[] fileBytes = Files.readAllBytes(file.toPath());
		String asString = new String(fileBytes);
		return new ArrayList<>(Arrays.asList(asString.split("\n")));
	}
}
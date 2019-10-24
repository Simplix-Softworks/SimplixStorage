package de.leonhard.storage.lightningstorage.utils;

import de.leonhard.storage.LightningStorage;
import java.io.*;
import java.nio.file.Files;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;


@SuppressWarnings("unused")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FileUtils {

	public static void createFile(@NotNull final File file) {
		try {
			if (file.getParentFile() != null && !file.getParentFile().exists()) {
				//noinspection ResultOfMethodCallIgnored
				file.getParentFile().mkdirs();
			}
			if (!file.exists()) {
				//noinspection ResultOfMethodCallIgnored
				file.createNewFile();
			}
		} catch (IOException e) {
			System.err.println("Error while creating file '" + file.getAbsolutePath() + "'.");
			e.printStackTrace();
			throw new IllegalStateException();
		}
	}

	/**
	 * Create a BufferedInputStream from a File.
	 *
	 * @param file the File to be read.
	 * @return BufferedInputstream containing the contents of the given File.
	 */
	public static BufferedInputStream createNewInputStream(@NotNull final File file) {
		try {
			return new BufferedInputStream(new FileInputStream(file));
		} catch (IOException e) {
			System.err.println("Error while creating InputStream from '" + file.getAbsolutePath() + "'");
			e.printStackTrace();
			throw new IllegalStateException();
		}
	}

	/**
	 * Create a BufferedInputStream from a given internal resource.
	 *
	 * @param resource the Path to the resource.
	 * @return BufferedInputStream containing the contents of the resource file.
	 */
	public static BufferedInputStream createNewInputStream(@NotNull final String resource) {
		return new BufferedInputStream(Objects.requireNonNull(LightningStorage.class.getClassLoader().getResourceAsStream(resource)));
	}

	/**
	 * Check if a given File has changed since the given TimeStamp.
	 *
	 * @param file      the File to be checked.
	 * @param timeStamp the TimeStamp to be checked against.
	 * @return true if the File has changed.
	 */
	public static boolean hasChanged(@NotNull final File file, final long timeStamp) {
		return timeStamp < file.lastModified();
	}

	/**
	 * Write the contents of a given InputStream to a File.
	 *
	 * @param file        the File to be written to.
	 * @param inputStream the InputStream which shall be written.
	 */
	public static synchronized void writeToFile(@NotNull final File file, @NotNull final InputStream inputStream) {
		try (BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file))) {
			if (!file.exists()) {
				Files.copy(inputStream, file.toPath());
			} else {
				final byte[] data = new byte[8192];
				int count;
				while ((count = inputStream.read(data, 0, 8192)) != -1) {
					outputStream.write(data, 0, count);
				}
			}
		} catch (IOException e) {
			System.err.println("Error while copying to + '" + file.getAbsolutePath() + "'");
			e.printStackTrace();
			throw new IllegalStateException();
		}
	}
}
package de.leonhard.storage.util;

import de.leonhard.storage.internal.provider.LightningProviders;
import lombok.Cleanup;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Class for easier, more convenient & strait interaction with files
 */
@UtilityClass
@SuppressWarnings({"unused", "ResultOfMethodCallIgnored"})
public class FileUtils {

    // ----------------------------------------------------------------------------------------------------
    // Getting Files
    // ----------------------------------------------------------------------------------------------------

    public @NotNull List<File> listFiles(@NonNull final File folder) {
        return listFiles(folder, null);
    }

    /**
     * Returns a List of Files in a folder which ends with a specific extension.
     *
     * <p>If there are no files in the folder or the folder is not found, an
     * empty list is returned instead of null.
     *
     * @param folder    Folder to search in.
     * @param extension Extension to search for. Set to null to skip extension validation.
     */
    public @NotNull List<File> listFiles(@NonNull final File folder,
                                         @Nullable final String extension) {
        final List<File> result = new ArrayList<>();

        val files = folder.listFiles();

        if (files == null) {
            return result;
        }

        // if the extension is null we always add the file.
        for (val file : files) {
            if (extension == null || file.getName().endsWith(extension)) {
                result.add(file);
            }
        }

        return result;
    }

    public @NotNull File getAndMake(@NonNull final String name,
                                    @NonNull final String path) {
        return getAndMake(new File(path, name));
    }

    public @NotNull File getAndMake(@NonNull final File file) {
        try {
            if (file.getParentFile() != null && !file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }

            if (!file.exists()) {
                file.createNewFile();
            }

        } catch (final @NotNull IOException ex) {
            throw LightningProviders.exceptionHandler().create
                    (ex,
                            "Error while creating file '" + file.getName() + "'.",
                            "In: '" + getParentDirPath(file) + "'");
        }
        return file;
    }

    // ----------------------------------------------------------------------------------------------------
    // Methods for handling the extension of files
    // ----------------------------------------------------------------------------------------------------

    private @NotNull String getExtension(@NonNull final String path) {
        return path.lastIndexOf(".") > 0 ? path.substring(path.lastIndexOf(".") + 1) : "";
    }

    public @NotNull String getExtension(@NonNull final File file) {
        return getExtension(file.getName());
    }

    public @NotNull String replaceExtensions(@NonNull final String fileName) {
        if (!fileName.contains(".")) {
            return fileName;
        }
        return fileName.replace("." + getExtension(fileName), "");
    }

    public @NotNull String getParentDirPath(@NonNull final File file) {
        return getParentDirPath(file.getAbsolutePath());
    }

    /**
     * Since file.getParentFile can be null we created an extension function to get the path of the
     * parent file
     *
     * @param fileOrDirPath Path to file
     * @return Path to file as String
     */
    private @NotNull String getParentDirPath(@NonNull final String fileOrDirPath) {
        val endsWithSlash = fileOrDirPath.endsWith(File.separator);
        return fileOrDirPath.substring(0,
                fileOrDirPath.lastIndexOf(File.separatorChar, endsWithSlash ? fileOrDirPath.length() - 2 : fileOrDirPath.length() - 1));
    }

    public boolean hasChanged(final @Nullable File file,
                              final long timeStamp) {
        if (file == null) {
            return false;
        }

        return timeStamp < file.lastModified();
    }

    // ----------------------------------------------------------------------------------------------------
    // Methods for reading & writing a file
    // ----------------------------------------------------------------------------------------------------

    public @NotNull InputStream createInputStream(@NonNull final File file) {
        try {
            return Files.newInputStream(file.toPath());
        } catch (final @NotNull IOException ex) {
            throw LightningProviders.exceptionHandler().create
                    (ex, "Error while creating InputStream from '" + file.getName() + "'.",
                            "In: '" + getParentDirPath(file) + "'");
        }
    }

    private @NotNull OutputStream createOutputStream(@NonNull final File file) {
        try {
            return new FileOutputStream(file);
        } catch (final @NotNull FileNotFoundException ex) {
            throw LightningProviders.exceptionHandler().create
                    (ex, "Error while creating OutputStream from '" + file.getName() + "'.",
                            "In: '" + getParentDirPath(file) + "'");
        }
    }

    public @NotNull Reader createReader(@NonNull final File file) {
        try {
            return new FileReader(file);
        } catch (final @NotNull FileNotFoundException ex) {
            throw LightningProviders.exceptionHandler().create
                    (ex, "Error while creating Reader for '" + file.getName() + "'.",
                            "In: '" + getParentDirPath(file) + "'");
        }
    }

    public @NotNull Writer createWriter(@NonNull final File file) {
        try {
            return new FileWriter(file);
        } catch (final @NotNull IOException ex) {
            throw LightningProviders.exceptionHandler().create
                    (ex, "Error while creating Writer for '" + file.getName() + "'.",
                            "In: '" + getParentDirPath(file) + "'");
        }
    }

    public void write(@NonNull final File file,
                      @NonNull final List<String> lines) {
        try {
            Files.write(file.toPath(), lines);
        } catch (final @NotNull IOException ex) {
            throw LightningProviders.exceptionHandler().create
                    (ex, "Error while writing to '" + file.getName() + "'.",
                            "In: '" + getParentDirPath(file) + "'");
        }
    }

    public void writeToFile(@NonNull final File file,
                            @NonNull final InputStream inputStream) {
        try (val outputStream = new FileOutputStream(file)) {
            if (!file.exists()) {
                Files.copy(inputStream, file.toPath());
            } else {
                val data = new byte[8192];
                int count;

                while ((count = inputStream.read(data, 0, 8192)) != -1) {
                    outputStream.write(data, 0, count);
                }
            }
        } catch (final @NotNull IOException ex) {
            throw LightningProviders.exceptionHandler().create
                    (ex, "Error while writing InputStream to '" + file.getName() + "'.",
                            "In: '" + getParentDirPath(file) + "'");
        }
    }

    private byte @NotNull [] readAllBytes(@NonNull final File file) {
        try {
            return Files.readAllBytes(file.toPath());
        } catch (final @NotNull IOException ex) {
            throw LightningProviders.exceptionHandler().create
                    (ex, "Error while reading '" + file.getName() + "'.",
                            "In: '" + getParentDirPath(file) + "'");
        }
    }

    public @NotNull List<String> readAllLines(@NonNull final File file) {
        val fileBytes = readAllBytes(file);
        val asString = new String(fileBytes);

        return new ArrayList<>(Arrays.asList(asString.split(System.lineSeparator())));
    }

    // ----------------------------------------------------------------------------------------------------
    // Archiving
    // ----------------------------------------------------------------------------------------------------

    @SneakyThrows
    public void zipFile(final @NotNull String sourceDirectory,
                        final String to) {
        val fileTo = getAndMake(new File(to + ".zip"));
        @Cleanup val zipOutputStream = new ZipOutputStream(createOutputStream(fileTo));
        val pathFrom = Paths.get(new File(sourceDirectory).toURI());

        Files.walk(pathFrom).filter(path -> !Files.isDirectory(path))
                .forEach(path ->
                {
                    final ZipEntry zipEntry = new ZipEntry(pathFrom.relativize(path).toString());
                    try {
                        zipOutputStream.putNextEntry(zipEntry);
                        Files.copy(path, zipOutputStream);
                        zipOutputStream.closeEntry();
                    } catch (final @NotNull IOException ex) {
                        ex.printStackTrace();
                    }
                });
    }

    // ----------------------------------------------------------------------------------------------------
    // Checksums
    // ----------------------------------------------------------------------------------------------------

    public @NotNull String md5ChecksumAsString(@NonNull final File filename) {
        val checkSum = md5Checksum(filename);
        val result = new StringBuilder();

        for (val b : checkSum) {
            result.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
        }

        return result.toString();
    }

    private byte[] md5Checksum(@NonNull final File file) {
        try (val fileInputStream = new FileInputStream(file)) {
            val buffer = new byte[1024];
            val complete = MessageDigest.getInstance("MD5");
            int numRead;

            do {
                numRead = fileInputStream.read(buffer);
                if (numRead > 0) {
                    complete.update(buffer, 0, numRead);
                }
            } while (numRead != -1);

            return complete.digest();
        } catch (final @NotNull IOException | NoSuchAlgorithmException ex) {
            throw LightningProviders.exceptionHandler().create
                    (ex, "Error while creating checksum of '" + file.getName() + "'.",
                            "In: '" + getParentDirPath(file) + "'");
        }
    }

    // ----------------------------------------------------------------------------------------------------
    // Misc
    // ----------------------------------------------------------------------------------------------------

    public void extractResource(@NonNull final String targetDirectory,
                                @NonNull final String resourcePath,
                                final boolean replace) {
        Valid.checkBoolean(!resourcePath.isEmpty(), "ResourcePath mustn't be empty");
        Valid.checkBoolean(!targetDirectory.isEmpty(), "Target directory mustn't be empty");

        val target = new File(targetDirectory, resourcePath);

        if (target.exists() && !replace) {
            return;
        }

        getAndMake(target);

        try (val inputStream = LightningProviders.inputStreamProvider().createInputStreamFromInnerResource(resourcePath)) {
            Valid.notNull(inputStream, "The embedded resource '" + resourcePath + "' cannot be found");

            assert inputStream != null;
            Files.copy(inputStream, target.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (final @NotNull IOException ioException) {
            throw LightningProviders.exceptionHandler().create(ioException,
                    "Exception while extracting file",
                    "Directory: '" + targetDirectory + "'",
                    "ResourcePath: '" + resourcePath + "'");
        }
    }

    public void extractResourceFolderContents(@NonNull final File sourceJarFile,
                                              @NonNull final File targetDirectory,
                                              @NonNull final String sourceDirectory,
                                              boolean replace) {
        if (!targetDirectory.exists()) {
            Valid.checkBoolean(targetDirectory.mkdirs(),
                    "Can't create directory '" + targetDirectory.getName() + "'",
                    "Parent: '" + getParentDirPath(targetDirectory) + "'");
        }

        Valid.checkBoolean(
                targetDirectory.isDirectory(),
                "Target directory must be an directory");

        try (val jarFile = new JarFile(sourceJarFile)) {
            for (final Enumeration<JarEntry> entries = jarFile.entries(); entries.hasMoreElements(); ) {
                val jarEntry = entries.nextElement();
                val entryName = jarEntry.getName();

                if (entryName.startsWith(sourceDirectory) && !jarEntry.isDirectory()) {
                    extractResource(targetDirectory.getAbsolutePath(), entryName, replace);
                }
            }

        } catch (final @NotNull Throwable throwable) {
            throw LightningProviders.exceptionHandler().create(throwable,
                    "Failed to extract folder from '" + targetDirectory + "' to '" + sourceDirectory + "'");
        }
    }

    private void copyFolder(@NonNull final File source, @NonNull final File destination) throws IOException {
        if (source.isDirectory()) {
            if (!destination.exists()) {
                destination.mkdirs();
            }

            val files = source.list();

            if (files == null) {
                return;
            }

            for (val file : files) {
                val srcFile = new File(source, file);
                val destFile = new File(destination, file);

                copyFolder(srcFile, destFile);
            }

        } else {
            @Cleanup val in = createInputStream(source);
            @Cleanup val out = createOutputStream(destination);

            val buffer = new byte[1024];

            int length;
            while ((length = in.read(buffer)) > 0) out.write(buffer, 0, length);
        }
    }
}
